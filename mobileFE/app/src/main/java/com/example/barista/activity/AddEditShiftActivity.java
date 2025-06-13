package com.example.barista.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;
import com.example.barista.data.User;
import com.example.barista.data.WorkShift;
import com.example.barista.request.WorkShiftRequest;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditShiftActivity extends AppCompatActivity {

    // Views
    private MaterialToolbar toolbar;
    private AutoCompleteTextView autoCompleteEmployee;
    private TextInputEditText etWorkDate, etStartTime, etEndTime, etNotes;
    private Button btnSaveShift;

    // Logic
    private ApiService apiService;
    private SessionManager sessionManager;
    private boolean isEditMode = false;
    private String editingShiftId = null;
    private String selectedEmployeeId = null;

    private final Map<String, String> employeeMap = new HashMap<>(); // Key: Tên, Value: ID
    private final Calendar workDateCalendar = Calendar.getInstance();
    private final Calendar startTimeCalendar = Calendar.getInstance();
    private final Calendar endTimeCalendar = Calendar.getInstance();

    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private final SimpleDateFormat displayTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shift);

        initViews();
        initLogic();
        setupToolbar();
        setupListeners();

        // Kiểm tra xem đây là chế độ Thêm hay Sửa
        if (getIntent().hasExtra("SHIFT_ID")) {
            isEditMode = true;
            editingShiftId = getIntent().getStringExtra("SHIFT_ID");
            toolbar.setTitle("Chỉnh sửa Ca làm việc");
            loadShiftDetails(editingShiftId);
        } else {
            isEditMode = false;
            toolbar.setTitle("Tạo Ca Làm Việc Mới");
            loadEmployees(null); // Tải danh sách nhân viên
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        autoCompleteEmployee = findViewById(R.id.autoCompleteTextViewEmployee);
        etWorkDate = findViewById(R.id.editTextWorkDate);
        etStartTime = findViewById(R.id.editTextStartTime);
        etEndTime = findViewById(R.id.editTextEndTime);
        etNotes = findViewById(R.id.editTextNotes);
        btnSaveShift = findViewById(R.id.buttonSaveShift);
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        etWorkDate.setOnClickListener(v -> showDatePicker());
        etStartTime.setOnClickListener(v -> showTimePicker(true));
        etEndTime.setOnClickListener(v -> showTimePicker(false));
        btnSaveShift.setOnClickListener(v -> saveShift());

        autoCompleteEmployee.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            selectedEmployeeId = employeeMap.get(selectedName);
        });
    }

    private void loadEmployees(String preSelectedEmployeeId) {
        Log.d("AddEditShift", "Bắt đầu tải danh sách nhân viên...");
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getStaffList(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("AddEditShift", "Tải thành công! Số lượng nhân viên: " + response.body().size());
                    employeeMap.clear();
                    List<String> employeeNames = new ArrayList<>();
                    for (User user : response.body()) {
                        employeeMap.put(user.getFullName(), user.getId());
                        employeeNames.add(user.getFullName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEditShiftActivity.this, android.R.layout.simple_dropdown_item_1line, employeeNames);
                    autoCompleteEmployee.setAdapter(adapter);

                    if (preSelectedEmployeeId != null) {
                        for (Map.Entry<String, String> entry : employeeMap.entrySet()) {
                            if (entry.getValue().equals(preSelectedEmployeeId)) {
                                autoCompleteEmployee.setText(entry.getKey(), false);
                                selectedEmployeeId = preSelectedEmployeeId;
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(AddEditShiftActivity.this, "Không thể tải danh sách nhân viên", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadShiftDetails(String shiftId) {
        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getShiftDetails(authToken, shiftId).enqueue(new Callback<WorkShift>() {
            @Override
            public void onResponse(@NonNull Call<WorkShift> call, @NonNull Response<WorkShift> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkShift shift = response.body();
                    populateFields(shift);
                    loadEmployees(shift.getAssignedEmployeeId()); // Tải NV và chọn sẵn
                } else {
                    Toast.makeText(AddEditShiftActivity.this, "Không thể tải chi tiết ca làm việc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkShift> call, @NonNull Throwable t) {
                Toast.makeText(AddEditShiftActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(WorkShift shift) {
        etNotes.setText(shift.getNotes());
        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(shift.getScheduledStartTime(), apiFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(shift.getScheduledEndTime(), apiFormatter);

            workDateCalendar.set(startDateTime.getYear(), startDateTime.getMonthValue() - 1, startDateTime.getDayOfMonth());
            startTimeCalendar.set(Calendar.HOUR_OF_DAY, startDateTime.getHour());
            startTimeCalendar.set(Calendar.MINUTE, startDateTime.getMinute());
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, endDateTime.getHour());
            endTimeCalendar.set(Calendar.MINUTE, endDateTime.getMinute());

            etWorkDate.setText(displayDateFormat.format(workDateCalendar.getTime()));
            etStartTime.setText(displayTimeFormat.format(startTimeCalendar.getTime()));
            etEndTime.setText(displayTimeFormat.format(endTimeCalendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            workDateCalendar.set(year, month, dayOfMonth);
            etWorkDate.setText(displayDateFormat.format(workDateCalendar.getTime()));
        }, workDateCalendar.get(Calendar.YEAR), workDateCalendar.get(Calendar.MONTH), workDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = isStartTime ? startTimeCalendar : endTimeCalendar;
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            TextInputEditText targetEditText = isStartTime ? etStartTime : etEndTime;
            targetEditText.setText(displayTimeFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void saveShift() {
        if (selectedEmployeeId == null || etWorkDate.getText().toString().isEmpty() || etStartTime.getText().toString().isEmpty() || etEndTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(
                workDateCalendar.get(Calendar.YEAR),
                workDateCalendar.get(Calendar.MONTH) + 1,
                workDateCalendar.get(Calendar.DAY_OF_MONTH),
                startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                startTimeCalendar.get(Calendar.MINUTE)
        );

        LocalDateTime endDateTime = LocalDateTime.of(
                workDateCalendar.get(Calendar.YEAR),
                workDateCalendar.get(Calendar.MONTH) + 1,
                workDateCalendar.get(Calendar.DAY_OF_MONTH),
                endTimeCalendar.get(Calendar.HOUR_OF_DAY),
                endTimeCalendar.get(Calendar.MINUTE)
        );

        if (endDateTime.isBefore(startDateTime)) {
            Toast.makeText(this, "Giờ kết thúc không thể trước giờ bắt đầu", Toast.LENGTH_SHORT).show();
            return;
        }

        WorkShiftRequest request = new WorkShiftRequest();
        request.setAssignedEmployeeId(selectedEmployeeId);
        request.setScheduledStartTime(startDateTime.toString());
        request.setScheduledEndTime(endDateTime.toString());
        request.setNotes(etNotes.getText().toString().trim());

        String authToken = "Bearer " + sessionManager.fetchAuthToken();
        Call<WorkShift> apiCall;

        if (isEditMode) {
            apiCall = apiService.updateShift(authToken, editingShiftId, request);
        } else {
            apiCall = apiService.createShift(authToken, request);
        }

        btnSaveShift.setEnabled(false);
        apiCall.enqueue(new Callback<WorkShift>() {
            @Override
            public void onResponse(@NonNull Call<WorkShift> call, @NonNull Response<WorkShift> response) {
                btnSaveShift.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditShiftActivity.this, "Lưu ca làm việc thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEditShiftActivity.this, "Lưu thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkShift> call, @NonNull Throwable t) {
                btnSaveShift.setEnabled(true);
                Toast.makeText(AddEditShiftActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}