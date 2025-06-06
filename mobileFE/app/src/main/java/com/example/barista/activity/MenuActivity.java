package com.example.barista.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

public class MenuActivity extends AppCompatActivity {
    private Button orderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        orderButton = findViewById(R.id.buttonPayment);
        orderButton.setOnClickListener(v -> openOrderMenu());

    }

    private void openOrderMenu() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }


}
