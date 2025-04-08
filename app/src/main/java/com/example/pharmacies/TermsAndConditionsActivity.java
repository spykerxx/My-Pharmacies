package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        getSupportActionBar().hide();

        ImageView imageViewTermsAndConditions= findViewById(R.id.imageViewTermsAndConditions);
        imageViewTermsAndConditions.setOnClickListener(view -> finish());
    }
}
