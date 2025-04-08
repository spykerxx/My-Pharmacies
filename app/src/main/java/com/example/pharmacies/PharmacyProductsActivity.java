package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public class PharmacyProductsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PharmacyProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_products);
        getSupportActionBar().hide();

        ImageView imageViewPharmacyMyProductsBack= findViewById(R.id.imageViewPharmacyMyProductsBack);
        imageViewPharmacyMyProductsBack.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerViewPharmacyProducts);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        // Update the RecyclerView with the fetched products

        // Execute fetchProductsByPharmacyId method after 1.5 seconds

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    productsAdapter = new PharmacyProductsAdapter(this, PharmacyHomeActivity.products);
                    recyclerView.setAdapter(productsAdapter);
                }
                , 700);


    }
}
