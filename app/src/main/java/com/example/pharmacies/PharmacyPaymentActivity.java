package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class PharmacyPaymentActivity extends AppCompatActivity {

    private ImageView lastClickedPaymentMethod, imageViewApplePay, imageViewCreditCard;
    private String chosenPaymentMethod="";
    private ImageView imageViewContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_payment);
        getSupportActionBar().hide();

        imageViewApplePay = findViewById(R.id.imageViewApplePay2);
        imageViewCreditCard = findViewById(R.id.imageViewCreditCard2);

        ImageView back;
        back= findViewById(R.id.imageViewPharmacyPaymentBack);
        back.setOnClickListener(view -> finish());

        imageViewContinue= findViewById(R.id.imageViewPharmacyPaymentNext);
        imageViewContinue.setOnClickListener(view -> {
            if (chosenPaymentMethod.equals("Credit Card")){
                startActivity(new Intent(PharmacyPaymentActivity.this, PharmacyCreditCardActivity.class));
            }
            else if (chosenPaymentMethod.isEmpty()){
                Toast.makeText(this, "Choose Payment Method!", Toast.LENGTH_SHORT).show();
            }
            else{
                startActivity(new Intent(PharmacyPaymentActivity.this, PharmacyLoginActivity.class));
                Toast.makeText(this, "Success! Login please", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewApplePay.setOnClickListener(v -> highlightPaymentMethod(imageViewApplePay, "Apple Pay"));

        imageViewCreditCard.setOnClickListener(v -> highlightPaymentMethod(imageViewCreditCard, "Credit Card"));


    }

    private void highlightPaymentMethod(ImageView clickedPaymentMethod, String paymentMethod) {
        if (lastClickedPaymentMethod != null) {
            // Reset the previously selected payment method
            lastClickedPaymentMethod.setBackgroundResource(0); // Reset background
            lastClickedPaymentMethod.setScaleX(1.0f); // Reset scale
            lastClickedPaymentMethod.setScaleY(1.0f); // Reset scale
        }

        // Highlight the clicked payment method
        clickedPaymentMethod.setBackgroundResource(R.drawable.highlight_border); // Add border
        clickedPaymentMethod.setScaleX(1.1f); // Increase scale
        clickedPaymentMethod.setScaleY(1.1f); // Increase scale

        // Update last clicked payment method
        lastClickedPaymentMethod = clickedPaymentMethod;

        // Update chosen payment method
        chosenPaymentMethod = paymentMethod;

    }
}
