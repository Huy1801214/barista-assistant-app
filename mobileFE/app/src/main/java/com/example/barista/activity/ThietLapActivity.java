package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

public class ThietLapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap);

        LinearLayout llQuanLyNhanVien = findViewById(R.id.llQuanLyNhanVien);
        llQuanLyNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThietLapActivity.this, StaffManagementActivity.class);
                startActivity(intent);
            }
        });
    }
}
