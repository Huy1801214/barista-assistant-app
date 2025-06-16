package com.example.barista.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.OrderHistoryAdapter;
import com.example.barista.response.StatisticalResponse;
import com.example.barista.service.ApiClient;
import com.example.barista.service.StatisticalApi;
import com.example.barista.utils.NumberFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalActivity extends AppCompatActivity {
    TextView startLabelTime, startTextTime, endLabelTime, endTextTime;
    ImageButton goBack;
    StatisticalApi statisticalApi = ApiClient.getClient().create(StatisticalApi.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        startLabelTime = findViewById(R.id.startLabelTime);
        startTextTime = findViewById(R.id.startTextTime);
        endLabelTime = findViewById(R.id.endLabelTime);
        endTextTime = findViewById(R.id.endTextTime);

        goBack = findViewById(R.id.back_button);

        startTextTime.setOnClickListener(v -> showTimePicker(startTextTime));
        endTextTime.setOnClickListener(v -> showTimePicker(endTextTime));
        goBack.setOnClickListener(v -> finish());
        createDefaultDate();
    }

    private void createDefaultDate() {
        LocalDate today = LocalDate.now();
        String displayDate = today.getDayOfMonth() + "/" + today.getMonthValue() + "/" + today.getYear();
        startTextTime.setText(displayDate);
        endTextTime.setText(displayDate);
        createReport();
    }


    private void showTimePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if (!target.getText().toString().equals("--/--/--")) {
            String[] date = target.getText().toString().split("/");
            today = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]) - 1;
            year = Integer.parseInt(date[2]);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            target.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            createReport();
        }, year, month, today);

        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();


    }

    private String convertToISOFormat(String date) {
        String[] parts = date.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        // Thêm 0 vào trước ngày và tháng nếu cần
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        return year + "-" + month + "-" + day;
    }
    private void createReport() {
        //Lấy ngày bắt đầu và kết thúc từ 2 textview
        String startDate = startTextTime.getText().toString();
        String endDate = endTextTime.getText().toString();

        startDate = convertToISOFormat(startDate);
        endDate = convertToISOFormat(endDate);

        statisticalApi.getStatistical(startDate, endDate).enqueue(new Callback<StatisticalResponse>() {

            @Override
            public void onResponse(Call<StatisticalResponse> call, Response<StatisticalResponse> response) {
                StatisticalResponse statisticalResponse = response.body();
                if (statisticalResponse != null) {
                    TextView orderCount = findViewById(R.id.orderCountText);
                    TextView revenue = findViewById(R.id.revenueText);
                    TextView totalText = findViewById(R.id.totalText);
                    orderCount.setText(String.valueOf(statisticalResponse.getOrderCount()));
                    revenue.setText(NumberFormat.formatMoney(statisticalResponse.getRevenue()));
                    totalText.setText(NumberFormat.formatMoney(statisticalResponse.getTotal()));

                    TextView customerCount = findViewById(R.id.customerCount);
                    customerCount.setText(String.valueOf(statisticalResponse.getOrderCount()));


                    TextView totalOrderText = findViewById(R.id.totalOrderText);
                    totalOrderText.setText(String.valueOf(statisticalResponse.getOrderCount()));

                    TextView orderPrice = findViewById(R.id.orderPrice);
                    orderPrice.setText(NumberFormat.formatMoney(statisticalResponse.getTotal()));

                    TextView totalDiscountCountText = findViewById(R.id.totalDiscountCountText);
                    totalDiscountCountText.setText(String.valueOf(statisticalResponse.getDiscountCount()));

                    TextView totalDiscountText = findViewById(R.id.totalDiscountText);
                    totalDiscountText.setText(NumberFormat.formatMoney(statisticalResponse.getDiscount()));

                    TextView totalOrderCompleted = findViewById(R.id.totalOrderCompleted);
                    totalOrderCompleted.setText(NumberFormat.formatMoney(statisticalResponse.getRevenue()));

                    TextView totalOrderCompletedCountText = findViewById(R.id.totalOrderCompletedCountText);
                    totalOrderCompletedCountText.setText(String.valueOf(statisticalResponse.getOrderCount()));

                    RecyclerView history = findViewById(R.id.orderHistory);
                    history.setAdapter(new OrderHistoryAdapter(statisticalResponse));
                }
            }

            @Override
            public void onFailure(Call<StatisticalResponse> call, Throwable t) {

            }
        });
    }
}
