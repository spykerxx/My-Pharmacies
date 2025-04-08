package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckoutActivity extends AppCompatActivity {

    // Views related to address information
    private TextView textViewCheckoutFullName;
    private TextView textViewCheckOutPrimaryPhone;
    private TextView textViewCheckoutShortAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().hide();

        // Find views for address information
        textViewCheckoutFullName = findViewById(R.id.textViewCheckoutFullName);
        textViewCheckOutPrimaryPhone = findViewById(R.id.textViewCheckOutPrimaryPhone);
        textViewCheckoutShortAddress = findViewById(R.id.textViewCheckoutShortAddress);
        ImageView imageViewAddNewAddress = findViewById(R.id.imageViewAddNewAddress);
        ImageView imageViewContinue = findViewById(R.id.imageViewCheckOutContinue);

        ImageView imageViewCheckOutBack= findViewById(R.id.imageViewCheckOutBack);
        imageViewCheckOutBack.setOnClickListener(view -> finish());

        imageViewContinue.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(CheckoutActivity.this, CheckOutTwoActivity.class);
            startActivity(intent);
            finish();
        });


        imageViewAddNewAddress.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(CheckoutActivity.this, UserAddressActivity.class);
            boolean checkoutRunning=true;
            intent.putExtra("checkoutRunning", checkoutRunning);
            startActivity(intent);
            finish();
        });


        // Set user data
        setUserData();
    }

    private void setUserData() {
        // Check if currentUser is not null
        if (HomeActivity.currentUser != null) {
            // Get the address object from currentUser
            Address address = HomeActivity.currentUser.getAddress();

            // Check if address is not null
            if (address != null) {
                // Set full name
                textViewCheckoutFullName.setText(address.getFullName());
                // Set primary phone
                textViewCheckOutPrimaryPhone.setText(address.getPrimaryPhone());
                // Set short address
                textViewCheckoutShortAddress.setText(address.getShortAddress());
            }
        }
    }

}
