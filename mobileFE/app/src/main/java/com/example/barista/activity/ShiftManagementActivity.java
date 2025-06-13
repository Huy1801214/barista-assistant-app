package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.OnShiftActionListener;
import com.example.barista.adpater.WorkShiftAdapter;
import com.example.barista.data.WorkShift;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftManagementActivity extends AppCompatActivity implements OnShiftActionListener {

    private MaterialToolbar toolbar;
    private CalendarView calendarView;
    private TextView textViewSelectedDate, textViewNoShifts;
    private RecyclerView recyclerViewShifts;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddShift;

    private WorkShiftAdapter adapter;
    private List<WorkShift> shiftList;
    private ApiService apiService;
    private SessionManager sessionManager;
    private LocalDate selectedDate = LocalDate.now();

    private static final int ADD_EDIT_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_management);

        initViews();
        initLogicComponents();
        setupToolbar();
        setupRecyclerView();
        setupCalendar();
        setupListeners();

        loadShiftsForDate(selectedDate);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        calendarView = findViewById(R.id.calendarView);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        textViewNoShifts = findViewById(R.id.textViewNoShifts);
        recyclerViewShifts = findViewById(R.id.recyclerViewShifts);
        progressBar = findViewById(R.id.progressBar);
        fabAddShift = findViewById(R.id.fabAddShift);
    }

    private void initLogicComponents() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
        shiftList = new ArrayList<>();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        String userRole = sessionManager.getUserRole();
        adapter = new WorkShiftAdapter(this, shiftList, this, userRole);
        recyclerViewShifts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewShifts.setAdapter(adapter);
    }

    private void setupCalendar() {
        updateSelectedDateText(selectedDate);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
            updateSelectedDateText(selectedDate);
            loadShiftsForDate(selectedDate);
        });
    }

    private void setupListeners() {
        String userRole = sessionManager.getUserRole();
        if ("OWNER".equals(userRole) || "MANAGER".equals(userRole)) {
            fabAddShift.setVisibility(View.VISIBLE);
            fabAddShift.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddEditShiftActivity.class);
                startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
            });
        } else {
            fabAddShift.setVisibility(View.GONE);
        }
    }

    private void updateSelectedDateText(LocalDate date) {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("'Ca làm việc ngày:' dd/MM/yyyy");
        textViewSelectedDate.setText(date.format(displayFormatter));
    }

    private void loadShiftsForDate(LocalDate date) {
        setLoading(true);

        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String startDateTime = date.atStartOfDay().format(apiFormatter);
        String endDateTime = date.atTime(23, 59, 59).format(apiFormatter);
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        String userRole = sessionManager.getUserRole();

        Call<List<WorkShift>> apiCall;
        if ("OWNER".equals(userRole) || "MANAGER".equals(userRole)) {
            apiCall = apiService.getShiftsForStore(authToken, startDateTime, endDateTime);
        } else {
            apiCall = apiService.getMyShifts(authToken, startDateTime, endDateTime);
        }

        apiCall.enqueue(new Callback<List<WorkShift>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkShift>> call, @NonNull Response<List<WorkShift>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    shiftList.clear();
                    shiftList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    textViewNoShifts.setVisibility(shiftList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(ShiftManagementActivity.this, "Tải ca làm việc thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkShift>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(ShiftManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            loadShiftsForDate(selectedDate);
        }
    }

    @Override
    public void onClockIn(String shiftId, int position) {
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        apiService.clockIn(authToken, shiftId).enqueue(new Callback<WorkShift>() {
            @Override
            public void onResponse(@NonNull Call<WorkShift> call, @NonNull Response<WorkShift> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shiftList.set(position, response.body());
                    adapter.notifyItemChanged(position);
                    Toast.makeText(ShiftManagementActivity.this, "Đã vào ca!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShiftManagementActivity.this, "Vào ca thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkShift> call, @NonNull Throwable t) {
                Toast.makeText(ShiftManagementActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClockOut(String shiftId, int position) {
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        apiService.clockOut(authToken, shiftId).enqueue(new Callback<WorkShift>() {
            @Override
            public void onResponse(@NonNull Call<WorkShift> call, @NonNull Response<WorkShift> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shiftList.set(position, response.body());
                    adapter.notifyItemChanged(position);
                    Toast.makeText(ShiftManagementActivity.this, "Đã ra ca!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShiftManagementActivity.this, "Ra ca thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkShift> call, @NonNull Throwable t) {
                Toast.makeText(ShiftManagementActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMoreOptionsClicked(WorkShift shift, View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);
        popup.getMenuInflater().inflate(R.menu.shift_options_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit_shift) {
                Intent intent = new Intent(this, AddEditShiftActivity.class);
                intent.putExtra("SHIFT_ID", shift.getId());
                startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
                return true;
            } else if (itemId == R.id.action_cancel_shift) {
                cancelShift(shift.getId());
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void cancelShift(String shiftId) {
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        apiService.cancelShift(authToken, shiftId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShiftManagementActivity.this, "Đã hủy ca làm việc.", Toast.LENGTH_SHORT).show();
                    loadShiftsForDate(selectedDate);
                } else {
                    Toast.makeText(ShiftManagementActivity.this, "Hủy ca thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ShiftManagementActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerViewShifts.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        if (isLoading) {
            textViewNoShifts.setVisibility(View.GONE);
        }
    }
}