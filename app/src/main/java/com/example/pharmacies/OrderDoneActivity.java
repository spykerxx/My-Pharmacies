package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class OrderDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);
        getSupportActionBar().hide();

        ImageView imageViewTrackOrder = findViewById(R.id.imageViewOrderDoneTrackMyOrder);

        imageViewTrackOrder.setOnClickListener(v -> {
            finish();
        });
    }
}
