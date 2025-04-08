package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GiftingActivity extends AppCompatActivity {

    private EditText editTextGiftPersonName;
    private EditText editTextGiftPersonEmail;
    private EditText editTextGiftMessage;
    private EditText editTextGiftOtherValue;
    private ImageView buttonCompleteGift, gift500, gift200, gift100, back;
    private ImageView lastClickedGift;
    private TextView textViewGiftValue;
    private int giftValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifting);
        getSupportActionBar().hide();

        // Initialize EditText and Button
        editTextGiftPersonName = findViewById(R.id.editTextGiftPersonName);
        editTextGiftPersonEmail = findViewById(R.id.editTextGiftPersonEmail);
        editTextGiftMessage = findViewById(R.id.editTextGiftMessage);
        editTextGiftOtherValue = findViewById(R.id.editTextGiftOtherValue);
        buttonCompleteGift = findViewById(R.id.imageViewGiftButtonComplete);
        textViewGiftValue= findViewById(R.id.textViewGiftMoney);
        textViewGiftValue.setText(giftValue+" SAR");
        gift500= findViewById(R.id.imageViewGift500);
        gift200= findViewById(R.id.imageViewGift200);
        gift100= findViewById(R.id.imageViewGift100);

        // Set click listeners for each gift
        gift500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGiftOtherValue.getText().toString().isEmpty()) {
                    highlightGift(gift500);
                    giftValue = 500;
                    textViewGiftValue.setText(giftValue+" SAR");
                }
            }
        });

        gift200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGiftOtherValue.getText().toString().isEmpty()) {
                    highlightGift(gift200);
                    giftValue = 200;
                    textViewGiftValue.setText(giftValue+" SAR");
                }
            }
        });

        gift100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGiftOtherValue.getText().toString().isEmpty()) {
                    highlightGift(gift100);
                    giftValue = 100;
                    textViewGiftValue.setText(giftValue+" SAR");
                }
            }
        });

        back= findViewById(R.id.imageViewGiftingBack);
        back.setOnClickListener(view -> finish());

// TextChangedListener for editTextOtherValue
        editTextGiftOtherValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Clear highlighting and set giftValue based on editTextOtherValue
                if (!s.toString().isEmpty()) {
                    resetGiftHighlight();
                    try {
                        giftValue = Integer.parseInt(s.toString());
                        textViewGiftValue.setText(giftValue+" SAR");
                    } catch (NumberFormatException e) {
                        // Handle invalid input
                        giftValue = 0;
                        textViewGiftValue.setText(giftValue+" SAR");
                    }
                }
                else {
                    giftValue = 0;
                    textViewGiftValue.setText(giftValue+" SAR");
                }
            }
        });


        // Set click listener for the button
        buttonCompleteGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from EditText fields
                String personName = editTextGiftPersonName.getText().toString();
                String personEmail = editTextGiftPersonEmail.getText().toString();
                String message = editTextGiftMessage.getText().toString();
                String otherValue = editTextGiftOtherValue.getText().toString();

                // Perform your gift sending action here
                // For example, you can display a toast message
                Toast.makeText(GiftingActivity.this, "Gift sent to " + personName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to highlight the selected gift and reset the previously selected gift
    private void highlightGift(ImageView clickedGift) {
        if (lastClickedGift != null) {
            // Reset the previously selected gift
            lastClickedGift.setBackgroundResource(0); // Reset background
            lastClickedGift.setScaleX(1.0f); // Reset scale
            lastClickedGift.setScaleY(1.0f); // Reset scale
        }

        // Highlight the clicked gift
        clickedGift.setBackgroundResource(R.drawable.highlight_border); // Add border
        clickedGift.setScaleX(1.1f); // Increase scale
        clickedGift.setScaleY(1.1f); // Increase scale

        // Update last clicked gift
        lastClickedGift = clickedGift;
    }
    // Method to reset highlighting of all gifts
    private void resetGiftHighlight() {
        if (lastClickedGift != null) {
            lastClickedGift.setBackgroundResource(0); // Reset background
            lastClickedGift.setScaleX(1.0f); // Reset scale
            lastClickedGift.setScaleY(1.0f); // Reset scale
            lastClickedGift = null;
        }
    }

}
