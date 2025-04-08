package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ImageView imageViewCustomer = findViewById(R.id.imageViewMainCustomer);
        imageViewCustomer.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        ImageView imageViewDelivery = findViewById(R.id.imageViewDelivery);
        imageViewDelivery.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(MainActivity.this, DeliveryLoginActivity.class);
            startActivity(intent);
        });

        ImageView imageViewPharmacy = findViewById(R.id.imageViewPharmacy);
        imageViewPharmacy.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(MainActivity.this, PharmacyLoginActivity.class);
            startActivity(intent);
        });

    }
}
