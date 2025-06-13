package com.example.barista.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;

import com.example.barista.adpater.ShiftHistoryAdapter;
import com.example.barista.data.WorkShiftHistory;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftHistoryActivity extends AppCompatActivity {

    private TextInputEditText editTextStartDate, editTextEndDate;
    private Button buttonViewReport;
    private RecyclerView recyclerViewHistory;
    private ProgressBar progressBar;
    private ShiftHistoryAdapter adapter;
    private List<WorkShiftHistory> historyList;
    private ApiService apiService;
    private SessionManager sessionManager;
    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_history);

        initViews();
        setupToolbar();
        initLogicComponents();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        buttonViewReport = findViewById(R.id.buttonViewReport);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initLogicComponents() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
        historyList = new ArrayList<>();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ShiftHistoryAdapter(this, historyList);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(adapter);
    }

    private void setupListeners() {
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate, startCalendar));
        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate, endCalendar));
        buttonViewReport.setOnClickListener(v -> {
            if (editTextStartDate.getText().toString().isEmpty() || editTextEndDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn cả ngày bắt đầu và kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            loadHistoryFromServer();
        });
    }

    private void showDatePickerDialog(TextInputEditText editText, Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            editText.setText(displayFormat.format(calendar.getTime()));
        };
        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadHistoryFromServer() {
        setLoading(true);

        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime startDateTime = startCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
        LocalDateTime endDateTime = endCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59);

        String startDateStr = startDateTime.format(apiFormatter);
        String endDateStr = endDateTime.format(apiFormatter);
        Log.d("API_CALL", "Start Date Param: " + startDateStr);
        Log.d("API_CALL", "End Date Param: " + endDateStr);
        String employeeId = null; // TODO: Lấy từ Spinner nếu có

        String token = sessionManager.fetchAuthToken();
        if (token == null) {
            Toast.makeText(this, "Lỗi: Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        }
        String authToken = "Bearer " + token;
        Log.d("API_CALL", "Auth Token: " + authToken);

        apiService.getShiftHistory(authToken, startDateStr, endDateStr, employeeId).enqueue(new Callback<List<WorkShiftHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkShiftHistory>> call, @NonNull Response<List<WorkShiftHistory>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    historyList.clear();
                    historyList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (historyList.isEmpty()) {
                        Toast.makeText(ShiftHistoryActivity.this, "Không có dữ liệu trong khoảng thời gian này", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShiftHistoryActivity.this, "Tải báo cáo thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkShiftHistory>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(ShiftHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerViewHistory.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
