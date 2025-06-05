package com.example.barista.utils;

import java.text.DecimalFormat;

public class NumberFormat {
    public static String formatMoney(double money) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(money) + " đ";
    }
}
