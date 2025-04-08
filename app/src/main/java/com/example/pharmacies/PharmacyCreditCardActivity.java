package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PharmacyCreditCardActivity extends AppCompatActivity {

    private EditText editTextCreditCardName;
    private EditText editTextCVV;
    private EditText editTextExpDate;
    private EditText editTextCreditCardNumber;



    // TextWatcher for credit card number
    private TextWatcher cardNumberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 16) {
                editTextExpDate.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // TextWatcher for expiration date
    private TextWatcher expDateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 2 && before == 0) {
                editTextExpDate.setText(editTextExpDate.getText().toString() + "/");
                editTextExpDate.setSelection(editTextExpDate.getText().toString().length());
            }
            if (s.length() == 5) {
                editTextCVV.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // TextWatcher for CVV
    private TextWatcher cvvWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 3) {
                // Remove the extra characters beyond 3 digits
                editTextCVV.setText(s.subSequence(0, 3));
                editTextCVV.setSelection(3); // Move cursor to the end
                editTextCreditCardName.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_credit_card);
        getSupportActionBar().hide();

        // Initialize EditText and ImageView
        editTextCreditCardName = findViewById(R.id.editTextCreditCardName2);
        editTextCVV = findViewById(R.id.editTextCVV2);
        editTextExpDate = findViewById(R.id.editTextExpDate2);
        editTextCreditCardNumber = findViewById(R.id.editTextCreditCardNumber2);

        // Add TextWatchers to EditTexts
        editTextCreditCardNumber.addTextChangedListener(cardNumberWatcher);
        editTextExpDate.addTextChangedListener(expDateWatcher);
        editTextCVV.addTextChangedListener(cvvWatcher);


        ImageView proceed= findViewById(R.id.imageViewCreditCardProceedPharmacy);
        proceed.setOnClickListener(view -> {
            startActivity(new Intent(PharmacyCreditCardActivity.this, PharmacyLoginActivity.class));
            Toast.makeText(this, "Subscription complete!", Toast.LENGTH_SHORT).show();
            finishAffinity();
        });

        ImageView cancel= findViewById(R.id.imageViewCreditCardCancelPharmacy);
        ImageView back;
        back= findViewById(R.id.imageViewPharmacyCreditBack);
        back.setOnClickListener(view -> finish());

        cancel.setOnClickListener(view -> {
            startActivity(new Intent(PharmacyCreditCardActivity.this, PharmacyLoginActivity.class));
            finishAffinity();
        });


    }

}
