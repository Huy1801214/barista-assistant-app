package com.example.barista;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barista.adpater.OrderListAdapter;
import com.example.barista.data.OrderItem;
import com.example.barista.module.Sharedable;

import java.util.List;

public class OrderList extends Fragment {

    public OrderList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<OrderItem> items = (List<OrderItem>) Sharedable.get("ordersItems");
        // Set up RecyclerView and adapter

        RecyclerView recyclerView = view.findViewById(R.id.orderRecyclerView);
        Context ctx = requireContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        var adapter = new OrderListAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

}