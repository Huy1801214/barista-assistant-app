package com.example.barista.activity;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.ImageButton;

import com.example.barista.OrderList;
import com.example.barista.OrderPayment;
import com.example.barista.R;


public class ComfirmOrder extends AppCompatActivity {

    private ImageButton orderListButton, orderPaymentButton;
    private View orderListUnderLine, orderPaymentUnderLine;
    private static final Fragment ORDER_LIST_FRAGMENT = new OrderList();
    private static final Fragment ORDER_PAYMENT_FRAGMENT = new OrderPayment();

    public ComfirmOrder() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comfirm_order_activity);
        orderListButton      = findViewById(R.id.orderListButton);
        orderPaymentButton   = findViewById(R.id.orderPaymentButton);

        orderListUnderLine    = findViewById(R.id.orderListUnderLine);
        orderPaymentUnderLine = findViewById(R.id.orderPaymentUnderLine);

        selectTab(0);

        // 3. Thiết lập OnClickListener cho từng nút
        orderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(0);
            }
        });


        orderPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(2);
            }
        });

    }

    private void selectTab(int tabIndex) {
        // 1. Ẩn hết các underline trước
        orderListUnderLine.setVisibility(View.INVISIBLE);
        orderPaymentUnderLine.setVisibility(View.INVISIBLE);

        // 2. Hiển thị underline tương ứng
        switch (tabIndex) {
            case 0:
                orderListUnderLine.setVisibility(View.VISIBLE);
                switchFragment(ORDER_LIST_FRAGMENT);
                break;
            case 2:
                orderPaymentUnderLine.setVisibility(View.VISIBLE);
                switchFragment(ORDER_PAYMENT_FRAGMENT);
                break;
        }

    }
    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_order_view, fragment).commit();
    }

}

