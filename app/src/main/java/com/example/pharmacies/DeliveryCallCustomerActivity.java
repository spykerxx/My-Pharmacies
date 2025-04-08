package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class DeliveryCallCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_call_customer);
        getSupportActionBar().hide();

        ImageView back= findViewById(R.id.imageViewCallCustomerBack);
        back.setOnClickListener(view -> finish());

        ImageView call= findViewById(R.id.imageViewCallCustomerCall);
        ImageView chat= findViewById(R.id.imageViewCallCustomerChat);

        call.setOnClickListener(view -> Toast.makeText(this, "Not Available!", Toast.LENGTH_SHORT).show());
        chat.setOnClickListener(view -> Toast.makeText(this, "Not Available!", Toast.LENGTH_SHORT).show());
    }
}
