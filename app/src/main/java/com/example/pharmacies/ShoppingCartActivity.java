package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity implements CartProductsAdapter.OnQuantityChangedListener {

    private RecyclerView recyclerViewCart;
    private EditText editTextCartCoupon;
    private ImageView imageViewCartCouponApply;
    private TextView textViewCartTotal;
    private TextView textViewCartShippingTotal;
    private TextView textViewCartVAT;
    private TextView textViewCartSubTotal;
    private ImageView imageViewCartButtonContinue;
    private  CartProductsAdapter productsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().hide();
        // Initialize views
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setHasFixedSize(true);
        editTextCartCoupon = findViewById(R.id.editTextCartCoupon);
        imageViewCartCouponApply = findViewById(R.id.imageViewCartCouponApply);
        textViewCartTotal = findViewById(R.id.textViewCartTotal);
        textViewCartShippingTotal = findViewById(R.id.textViewCartShippingTotal);
        textViewCartVAT = findViewById(R.id.textViewCartVAT);
        textViewCartSubTotal = findViewById(R.id.textViewCartSubTotal);
        imageViewCartButtonContinue = findViewById(R.id.imageViewCartButtonContinue);

        ImageView imageViewShoppingCartBack= findViewById(R.id.imageViewShoppingCartBack);
        imageViewShoppingCartBack.setOnClickListener(view -> finish());

        // Set onClickListener for apply coupon button
        imageViewCartCouponApply.setOnClickListener(v -> applyCoupon());


        productsAdapter = new CartProductsAdapter(ShoppingCartActivity.this, HomeActivity.cart.getProducts());
        productsAdapter.setOnQuantityChangedListener(ShoppingCartActivity.this);
        recyclerViewCart.setAdapter(productsAdapter);

        textViewCartSubTotal.setText(HomeActivity.cart.getSubtotal()+" SAR");
        textViewCartVAT.setText(HomeActivity.cart.getVAT()+ " SAR");
        textViewCartShippingTotal.setText(HomeActivity.cart.getShippingTotal()+ " SAR");
        textViewCartTotal.setText(HomeActivity.cart.getTotal()+ " SAR");

        imageViewCartButtonContinue.setOnClickListener(view -> startActivity(new Intent(ShoppingCartActivity.this, CheckoutActivity.class)));
    }

    // Method to apply coupon
    private void applyCoupon() {
        // Handle applying coupon here
    }

    @Override
    public void onQuantityChanged() {
        // Recalculate total, subtotal, etc.
        HomeActivity.cart.updateCart();
        textViewCartSubTotal.setText(HomeActivity.cart.getSubtotal() + " SAR");
        textViewCartVAT.setText(HomeActivity.cart.getVAT() + " SAR");
        textViewCartShippingTotal.setText(HomeActivity.cart.getShippingTotal() + " SAR");
        textViewCartTotal.setText(HomeActivity.cart.getTotal() + " SAR");
        productsAdapter = new CartProductsAdapter(ShoppingCartActivity.this, HomeActivity.cart.getProducts());
        productsAdapter.setOnQuantityChangedListener(ShoppingCartActivity.this);
        recyclerViewCart.setAdapter(productsAdapter);
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
    }

}
