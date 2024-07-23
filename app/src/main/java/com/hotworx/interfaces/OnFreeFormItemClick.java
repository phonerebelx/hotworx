package com.hotworx.interfaces;

public interface OnFreeFormItemClick {
    void onItemClick(String user_id, String product_id, Boolean is_fav);

    void onItemClick(String json);
}
