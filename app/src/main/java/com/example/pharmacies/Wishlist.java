package com.example.pharmacies;

import java.util.ArrayList;

public class Wishlist {
    private User user;
    private ArrayList<Product> products;

    public Wishlist(User user, ArrayList<Product> products) {
        this.user = user;
        this.products = products;
    }

    // Getters and setters for User and products
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
