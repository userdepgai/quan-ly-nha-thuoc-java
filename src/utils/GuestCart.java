package utils;

import java.util.HashMap;

public class GuestCart {

    private static HashMap<String, Integer> cart = new HashMap<>();

    public static void addProduct(String maSP, int soLuong) {
        if(cart.containsKey(maSP)) {
            cart.put(maSP, cart.get(maSP) + soLuong);
        } else {
            cart.put(maSP, soLuong);
        }

    }

    public static HashMap<String, Integer> getCart(){
        return cart;
    }

    public static void clear(){
        cart.clear();
    }

    public static boolean isEmpty(){
        return cart.isEmpty();
    }

}