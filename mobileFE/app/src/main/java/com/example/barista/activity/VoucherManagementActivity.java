package com.example.barista.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;

import com.example.barista.adpater.VoucherAdapter;
import com.example.barista.data.Voucher;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VoucherManagementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewVouchers;
    private FloatingActionButton fabAddVoucher;
    private VoucherAdapter adapter;
    private List<Voucher> voucherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_management);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();

        loadDummyData(); // Tạm thời dùng dữ liệu giả
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewVouchers = findViewById(R.id.recyclerViewVouchers);
        fabAddVoucher = findViewById(R.id.fabAddVoucher);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(this, voucherList);
        recyclerViewVouchers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVouchers.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddVoucher.setOnClickListener(v -> {
            Toast.makeText(this, "Mở màn hình thêm khuyến mãi mới", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AddEditVoucherActivity.class);
            // startActivity(intent);
        });
    }

    private void loadDummyData() {
        // Trong ứng dụng thật, bạn sẽ gọi API ở đây.
        voucherList.add(new Voucher("Giảm 10% tối đa 20K", "KM10", "Còn hiệu lực đến 30/06/2025", Voucher.Status.ACTIVE));
        voucherList.add(new Voucher("Miễn phí vận chuyển", "FREESHIP", "Còn hiệu lực đến 31/12/2025", Voucher.Status.ACTIVE));
        voucherList.add(new Voucher("Giảm 50K cho đơn từ 200K", "GIAM50K", "Đã hết hạn", Voucher.Status.EXPIRED));
        voucherList.add(new Voucher("Tặng 1 ly Trà sữa", "TANGMOTLY", "Đã tạm dừng", Voucher.Status.PAUSED));

        adapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }
}
