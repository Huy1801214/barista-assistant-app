package com.example.barista.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.barista.R;
import com.example.barista.data.Cart;
import com.example.barista.module.Sharedable;


public class CompleteOrder extends Fragment {


    Button goBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(e -> {
            getActivity().finish();
        });
    }
}