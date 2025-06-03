package com.example.barista;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.ImageButton;


public class ComfirmOrder extends AppCompatActivity {

    private ImageButton orderListButton, orderInfoButton, orderPaymentButton;
    private View orderListUnderLine, orderInfoUnderLine, orderPaymentUnderLine;
    private static Fragment orderListFragment = new OrderList();
    private int currentTab = 0;

    public ComfirmOrder() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comfirm_order_activity);
        orderListButton      = findViewById(R.id.orderListButton);
        orderInfoButton      = findViewById(R.id.orderInfoButton);
        orderPaymentButton   = findViewById(R.id.orderPaymentButton);

        orderListUnderLine    = findViewById(R.id.orderListUnderLine);
        orderInfoUnderLine    = findViewById(R.id.orderInfoUnderLine);
        orderPaymentUnderLine = findViewById(R.id.orderPaymentUnderLine);

        selectTab(0);

        // 3. Thiết lập OnClickListener cho từng nút
        orderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(0);
            }
        });

        orderInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(1);
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
        currentTab = tabIndex;

        // 1. Ẩn hết các underline trước
        orderListUnderLine.setVisibility(View.INVISIBLE);
        orderInfoUnderLine.setVisibility(View.INVISIBLE);
        orderPaymentUnderLine.setVisibility(View.INVISIBLE);

        // 2. Hiển thị underline tương ứng
        switch (tabIndex) {
            case 0:
                orderListUnderLine.setVisibility(View.VISIBLE);
                switchFragment(orderListFragment);
                break;
            case 1:
                orderInfoUnderLine.setVisibility(View.VISIBLE);
                // TODO: load hoặc hiển thị nội dung của "Order Info" vào frame_order_view
                break;
            case 2:
                orderPaymentUnderLine.setVisibility(View.VISIBLE);
                // TODO: load hoặc hiển thị nội dung của "Order Payment" vào frame_order_view
                break;
        }

    }
    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_order_view, fragment).commit();
    }

}

