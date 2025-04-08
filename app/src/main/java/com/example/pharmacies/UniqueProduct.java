package com.example.pharmacies;

public class UniqueProduct {
    private Product product;
    private int quantity;

    public UniqueProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
