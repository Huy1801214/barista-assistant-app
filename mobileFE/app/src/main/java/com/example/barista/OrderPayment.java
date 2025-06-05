package com.example.barista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.barista.adpater.OrderListAdapter;
import com.example.barista.data.Cart;
import com.example.barista.data.OrderItem;
import com.example.barista.module.Sharedable;
import com.example.barista.utils.NumberFormat;

import java.util.ArrayList;
import java.util.List;

public class OrderPayment extends Fragment {

    private TextView previewPrice;
    private EditText inputCash;
    private TextView returnCash;
    private TextView totalPrice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        previewPrice = view.findViewById(R.id.preview_price);
        inputCash = view.findViewById(R.id.input_cash);
        returnCash = view.findViewById(R.id.return_cash);
        totalPrice = view.findViewById(R.id.total_price);

        inputCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    double cash = Double.parseDouble(input);

                    Cart cart = (Cart) Sharedable.get(Cart.ID);
                    double totalPrice = cart.getPrice();
                    double returnMoney = cash - totalPrice;
                    returnCash.setText(NumberFormat.formatMoney(returnMoney));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Cart cart = (Cart) Sharedable.get(Cart.ID);
        previewPrice.setText(NumberFormat.formatMoney(cart.getPrice()));
        totalPrice.setText(NumberFormat.formatMoney(cart.getPrice()));
    }
}