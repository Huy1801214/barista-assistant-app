package com.example.barista.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.OrderListAdapter;
import com.example.barista.data.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    public OrderFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("cafee", 100));
        items.add(new OrderItem("tea", 50));

        RecyclerView recyclerView = view.findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        var adapter = new OrderListAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}
