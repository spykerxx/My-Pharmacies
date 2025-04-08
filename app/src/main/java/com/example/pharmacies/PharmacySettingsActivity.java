package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class PharmacySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_settings);
        getSupportActionBar().hide();

        // Find views by their IDs
        ImageView imageViewPharmacyAboutUs = findViewById(R.id.imageViewPharmacyAboutUs);
        ImageView imageViewPharmacySupport = findViewById(R.id.imageViewPharmacySupport);
        ImageView imageViewPharmacyLogout = findViewById(R.id.imageViewPharmacyLogout);
        ImageView imageViewPharmacyFeedBack = findViewById(R.id.imageViewPharmacyFeedBack);
        ImageView imageViewPharmacySettingsBack = findViewById(R.id.imageViewPharmacySettingsBack);
        ImageView imageViewPharmacyAccount = findViewById(R.id.imageViewPharmacyAccount);
        ImageView imageViewPharmacyNotification = findViewById(R.id.imageViewPharmacyNotificationsSettings);

        imageViewPharmacyAboutUs.setOnClickListener(view -> startActivity(new Intent(PharmacySettingsActivity.this, AboutUsActivity.class)));
        imageViewPharmacySupport.setOnClickListener(view -> startActivity(new Intent(PharmacySettingsActivity.this, SupportActivity.class)));
        imageViewPharmacyFeedBack.setOnClickListener(view -> startActivity(new Intent(PharmacySettingsActivity.this, FeedbackActivity.class)));
        imageViewPharmacyAccount.setOnClickListener(view -> startActivity(new Intent(PharmacySettingsActivity.this, PharmacyProfileActivity.class)));
        imageViewPharmacyNotification.setOnClickListener(view -> startActivity(new Intent(PharmacySettingsActivity.this, PharmacyNotificationsActivity.class)));

        imageViewPharmacySettingsBack.setOnClickListener(view -> finish());
        imageViewPharmacyLogout.setOnClickListener(view -> {
            startActivity(new Intent(PharmacySettingsActivity.this, PharmacyLoginActivity.class));
            finishAffinity();
        });


    }
}
