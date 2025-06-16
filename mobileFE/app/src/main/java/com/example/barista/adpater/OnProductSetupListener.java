package com.example.barista.adpater;

import com.example.barista.data.ProductItem;

public interface OnProductSetupListener {
    void onEditClick(ProductItem product);
    void onDeleteClick(ProductItem product, int position);
}
