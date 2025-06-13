package com.example.barista.fragments;

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

import com.example.barista.R;
import com.example.barista.activity.ComfirmOrder;
import com.example.barista.adpater.OrderListAdapter;
import com.example.barista.data.Cart;
import com.example.barista.data.ProductItems;
import com.example.barista.module.Sharedable;

public class OrderList extends Fragment {
    ComfirmOrder comfirmOrder;

    public OrderList() {
        // Required empty public constructor
    }

    public static OrderList newInstance() {
        OrderList fragment = new OrderList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setComfirmOrder(ComfirmOrder comfirmOrder) {
        this.comfirmOrder = comfirmOrder;
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
        Cart cart = (Cart) Sharedable.get(Cart.ID);
        if (cart == null) {
            cart = new Cart();
            Sharedable.put(Cart.ID, cart);
        }

        addSampleData();

        // Set up RecyclerView and adapter

        RecyclerView recyclerView = view.findViewById(R.id.orderRecyclerView);
        Context ctx = requireContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        var adapter = new OrderListAdapter(this, cart.getOrderItems());
        recyclerView.setAdapter(adapter);
    }

    private void addSampleData() {
        Cart cart = (Cart) Sharedable.get(Cart.ID);
        ProductItems productItems = (ProductItems) Sharedable.get(ProductItems.ID);

        if (cart == null) {
            cart = new Cart();
            Sharedable.put(Cart.ID, cart);
        }

        cart.addNewItem(productItems.getItem(0));
        cart.addNewItem(productItems.getItem(0));
        cart.addNewItem(productItems.getItem(2));
        cart.addNewItem(productItems.getItem(1));
    }

}