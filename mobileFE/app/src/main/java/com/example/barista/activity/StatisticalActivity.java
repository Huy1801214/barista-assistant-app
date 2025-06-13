package com.example.barista.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

import java.util.Calendar;

public class StatisticalActivity extends AppCompatActivity {
    TextView startLabelTime, startTextTime, endLabelTime, endTextTime;
    ImageButton goBack;
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
        }, year, month, today);

        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();

    }
}
