package com.example.barista.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

import com.example.barista.data.Voucher;
import com.example.barista.data.VoucherDto;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVoucherActivity extends AppCompatActivity {

    // Views
    private MaterialToolbar toolbar;
    private TextInputEditText etVoucherName, etVoucherCode, etVoucherValue, etStartDate, etEndDate;
    private TextInputLayout layoutVoucherValue;
    private RadioGroup radioGroupVoucherType;
    private SwitchMaterial switchStatus;
    private Button btnSaveVoucher;
    private ProgressBar progressBar; // Thêm ProgressBar

    // Logic components
    private ApiService apiService;
    private SessionManager sessionManager;

    // Date formatters
    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();
    private final SimpleDateFormat displaySdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US); // Format để hiển thị
    private final SimpleDateFormat apiSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // Format để gửi lên API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        initViews();
        initLogicComponents();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etVoucherName = findViewById(R.id.editTextVoucherName);
        etVoucherCode = findViewById(R.id.editTextVoucherCode);
        etVoucherValue = findViewById(R.id.editTextVoucherValue);
        layoutVoucherValue = findViewById(R.id.layoutVoucherValue);
        etStartDate = findViewById(R.id.editTextStartDate);
        etEndDate = findViewById(R.id.editTextEndDate);
        radioGroupVoucherType = findViewById(R.id.radioGroupVoucherType);
        switchStatus = findViewById(R.id.switchStatus);
        btnSaveVoucher = findViewById(R.id.buttonSaveVoucher);
        progressBar = new ProgressBar(this); // Sẽ được thêm sau
    }

    private void initLogicComponents() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish()); // Đóng activity khi nhấn nút back
    }

    private void setupListeners() {
        radioGroupVoucherType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPercentage) {
                layoutVoucherValue.setSuffixText("%");
            } else if (checkedId == R.id.radioFixedAmount) {
                layoutVoucherValue.setSuffixText("VNĐ");
            }
        });

        switchStatus.setOnCheckedChangeListener((buttonView, isChecked) ->
                switchStatus.setText(isChecked ? R.string.status_active : R.string.status_inactive));

        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate, startCalendar));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate, endCalendar));

        btnSaveVoucher.setOnClickListener(v -> collectDataAndSave());
    }

    private void showDatePickerDialog(TextInputEditText editText, Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(displaySdf.format(calendar.getTime()));
        };

        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void collectDataAndSave() {
        // --- 1. Thu thập và kiểm tra dữ liệu ---
        String name = etVoucherName.getText().toString().trim();
        String code = etVoucherCode.getText().toString().trim();
        String valueStr = etVoucherValue.getText().toString().trim();
        String startDateStr = etStartDate.getText().toString().trim();
        String endDateStr = etEndDate.getText().toString().trim();

        if (name.isEmpty() || code.isEmpty() || valueStr.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 2. Tạo đối tượng DTO để gửi đi ---
        VoucherDto newVoucher = new VoucherDto();
        newVoucher.setName(name);
        newVoucher.setCode(code);
        newVoucher.setValue(new BigDecimal(valueStr));
        newVoucher.setType((radioGroupVoucherType.getCheckedRadioButtonId() == R.id.radioPercentage) ? "PERCENTAGE" : "FIXED_AMOUNT");
        newVoucher.setStatus(switchStatus.isChecked() ? "ACTIVE" : "INACTIVE");

        // Chuyển đổi định dạng ngày tháng
        try {
            Date startDate = displaySdf.parse(startDateStr);
            Date endDate = displaySdf.parse(endDateStr);
            newVoucher.setStartDate(apiSdf.format(startDate));
            newVoucher.setEndDate(apiSdf.format(endDate));
        } catch (ParseException e) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 3. Gọi API ---
        createVoucherOnServer(newVoucher);
    }

    private void createVoucherOnServer(VoucherDto voucherDto) {
        setLoading(true); // Hiển thị loading

        String authToken = "Bearer " + sessionManager.fetchAuthToken();

        apiService.createVoucher(authToken, voucherDto).enqueue(new Callback<Voucher>() {
            @Override
            public void onResponse(@NonNull Call<Voucher> call, @NonNull Response<Voucher> response) {
                setLoading(false); // Ẩn loading

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddVoucherActivity.this, "Tạo voucher thành công!", Toast.LENGTH_LONG).show();
                    // Đặt kết quả để Activity trước có thể reload danh sách
                    setResult(RESULT_OK);
                    finish(); // Đóng màn hình hiện tại
                } else {
                    // Xử lý lỗi từ server (VD: mã voucher trùng)
                    String errorMessage = "Tạo voucher thất bại. Vui lòng thử lại.";
                    if (response.errorBody() != null) {
                        // Cố gắng đọc message lỗi từ server
                        // (Cần setup thêm để đọc chi tiết)
                        errorMessage = "Lỗi: Mã voucher có thể đã tồn tại.";
                    }
                    Toast.makeText(AddVoucherActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Voucher> call, @NonNull Throwable t) {
                setLoading(false); // Ẩn loading
                Toast.makeText(AddVoucherActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            btnSaveVoucher.setEnabled(false);
            // Hiển thị ProgressBar nếu có
             progressBar.setVisibility(View.VISIBLE);
        } else {
            btnSaveVoucher.setEnabled(true);
            // Ẩn ProgressBar
             progressBar.setVisibility(View.GONE);
        }
    }
}