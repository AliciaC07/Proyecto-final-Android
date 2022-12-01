package com.aip.commerce_e.drawerFragments.cart;

public interface CartRCVInterface {
    void decreaseQuantity(Integer pos);
    void increaseQuantity(Integer pos);
    void deleteFromCart(Integer pos);
    void navigateOnClick(Integer pos);
}
