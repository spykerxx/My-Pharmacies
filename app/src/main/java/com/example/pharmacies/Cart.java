package com.example.pharmacies;

import java.util.ArrayList;

public class Cart {
    private User user;
    private ArrayList<Product> products = new ArrayList<>();
    private double subtotal;
    private double VAT;
    private double shippingTotal;
    private double total;

    public Cart(User user) {
        this.user = user;
    }

    // Constructor
    public Cart(User user, ArrayList<Product> products) {
        this.user = user;
        this.products = products;
        calculateSubtotal();
        calculateTotal();
    }

    // Getter and setter methods
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
        calculateSubtotal();
        calculateTotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getVAT() {
        return VAT;
    }

    public double getShippingTotal() {
        return shippingTotal;
    }

    public double getTotal() {
        return total;
    }

    // Calculate subtotal based on product prices and quantity in cart
    private void calculateSubtotal() {
        subtotal = 0;
        for (Product product : products) {
            subtotal += (product.getPrice() * product.getQuantityInCart());
        }
    }

    // Calculate total price including VAT and shipping
    private void calculateTotal() {
        // Assuming VAT and shipping costs are predefined
        VAT = subtotal * 0.05; // VAT rate is 5%
        shippingTotal = 10.0; // Assuming fixed shipping cost
        total = subtotal + VAT + shippingTotal;
    }

    public void addProduct(Product product) {
        products.add(product);
        calculateSubtotal();
        calculateTotal();
    }

    // Method to remove a product from the cart by name
    public void removeProductByName(String productName) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getName().equals(productName)) {
                products.remove(i);
                // Recalculate subtotal, VAT, and total after removing the product
                calculateSubtotal();
                calculateTotal();
                return; // Exit the loop after removing the product
            }
        }
    }
    public void updateCart(){
        calculateSubtotal();
        calculateTotal();
    }
}
