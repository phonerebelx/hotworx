package com.hotworx.interfaces;

public interface BasketClickListener {
    void onItemClick(String value, float totalCal);
    void onUpdateQuantity(float quantity, int position);
}
