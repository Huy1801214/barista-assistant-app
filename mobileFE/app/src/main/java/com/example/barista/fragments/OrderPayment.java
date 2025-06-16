package com.example.barista.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.barista.R;
import com.example.barista.activity.ComfirmOrder;
import com.example.barista.data.Cart;
import com.example.barista.data.UserInfo;
import com.example.barista.data.Voucher;
import com.example.barista.module.Sharedable;
import com.example.barista.request.LoginResponse;
import com.example.barista.request.OrderRequest;
import com.example.barista.service.ApiClient;
import com.example.barista.service.OrderApi;
import com.example.barista.service.VouchersApi;
import com.example.barista.utils.NumberFormat;
import com.example.barista.utils.SessionManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPayment extends Fragment {

    private TextView previewPrice;
    private EditText inputCash;
    private TextView returnCash;
    private TextView totalPriceView;
    private EditText noteView;
    private EditText voucherView;
    private Button createOrderButton;

    VouchersApi vouchersApi;
    SessionManager sessionManager;

    private double discountAmount = 0;
    private OrderApi orderApi = ApiClient.getClient().create(OrderApi.class);
    ComfirmOrder comfirmOrder;

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
        sessionManager = new SessionManager(getContext());
        previewPrice = view.findViewById(R.id.preview_price);
        inputCash = view.findViewById(R.id.input_cash);
        returnCash = view.findViewById(R.id.return_cash);
        totalPriceView = view.findViewById(R.id.total_price);
        noteView = view.findViewById(R.id.note);
        voucherView = view.findViewById(R.id.voucher);
        createOrderButton = view.findViewById(R.id.createOrder);

        vouchersApi = ApiClient.getApiService();

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
                    double totalPrice = cart.getPrice() - discountAmount;
                    if (totalPrice > cash) {
                        return;
                    }

                    double returnMoney = Math.abs(cash - totalPrice);

                    returnCash.setText(NumberFormat.formatMoney(returnMoney));
                }
            }
        });

        voucherView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = s.toString();
                if (code.isEmpty()) {
                    voucherView.setTextColor(Color.BLACK);
                    return;
                }
                String authToken = "Bearer " + sessionManager.fetchAuthToken();

                vouchersApi.getVoucherByCode(authToken, code).enqueue(new Callback<List<Voucher>>() {

                    @Override
                    public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                        var voucher = response.body();

                        if (voucher != null && !voucher.isEmpty()) {
                            Cart cart = (Cart) Sharedable.get(Cart.ID);
                            double totalPrice = cart.getPrice();
                            Voucher.VoucherType type = voucher.get(0).getType();

                            if (type == Voucher.VoucherType.FIXED_AMOUNT) {
                                discountAmount = voucher.get(0).getValue();
                            } else if (type == Voucher.VoucherType.PERCENTAGE) {
                                discountAmount = totalPrice * (voucher.get(0).getValue() / 100);
                            }

                            updateTotalPrice();
                            voucherView.setTextColor(Color.BLACK);

                        } else {
                            voucherView.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Voucher>> call, Throwable t) {
                       throw new RuntimeException(t.getMessage());
                    }
                });
            }
        });

        createOrderButton.setOnClickListener(e -> {
            Cart cart = (Cart) Sharedable.get(Cart.ID);
            double totalPrice = cart.getPrice();
            double discount = discountAmount;
            String voucher = voucherView.getText().toString();
            String note = noteView.getText().toString();
            String storeId = ((UserInfo) Sharedable.get(UserInfo.ID)).getStoreId();
            OrderRequest request = new OrderRequest(
                    LocalDateTime.now(),
                    cart.getOrderItems(),
                    totalPrice,
                    discount,
                    voucher,
                    note,
                    storeId
            );
            String authToken = "Bearer " + sessionManager.fetchAuthToken();

            orderApi.createOrder(authToken, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() != 200) {

                        throw new RuntimeException("Failed to create order " + response.code());
                    }
                    ComfirmOrder comfirmOrderActivity = (ComfirmOrder) getActivity();

                    if (comfirmOrderActivity != null) {
                        comfirmOrderActivity.switchToCompleteOrder();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    throw new RuntimeException(t.getMessage());
                }
            });

        });
    }



    @Override
    public void onStart() {
        super.onStart();
        Cart cart = (Cart) Sharedable.get(Cart.ID);


        previewPrice.setText(NumberFormat.formatMoney(cart.getPrice()));
        totalPriceView.setText(NumberFormat.formatMoney(cart.getPrice()));
    }

    private void  updateTotalPrice() {
        Cart cart = (Cart) Sharedable.get(Cart.ID);
        double totalPrice = cart.getPrice();
        totalPrice -= discountAmount;
        totalPrice = Math.max(0, totalPrice);

        totalPriceView.setText(NumberFormat.formatMoney(totalPrice));
    }



}