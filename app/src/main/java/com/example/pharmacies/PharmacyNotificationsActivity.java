package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class PharmacyNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_notifications);
        getSupportActionBar().hide();

        ImageView back= findViewById(R.id.imageViewNotificationsBack);
        back.setOnClickListener(view -> finish());
    }
}
