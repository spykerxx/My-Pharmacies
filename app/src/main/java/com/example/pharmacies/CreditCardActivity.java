package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreditCardActivity extends AppCompatActivity {

    private EditText editTextCreditCardName;
    private EditText editTextCVV;
    private EditText editTextExpDate;
    private EditText editTextCreditCardNumber;
    private ImageView imageViewCreditCardProceed;
    private ImageView imageViewCreditCardCancel;



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
        setContentView(R.layout.activity_credit_card);
        getSupportActionBar().hide();

        // Initialize EditText and ImageView
        editTextCreditCardName = findViewById(R.id.editTextCreditCardName);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextExpDate = findViewById(R.id.editTextExpDate);
        editTextCreditCardNumber = findViewById(R.id.editTextCreditCardNumber);
        imageViewCreditCardProceed = findViewById(R.id.imageViewCreditCardProceed);
        imageViewCreditCardCancel = findViewById(R.id.imageViewCreditCardCancel);

        ImageView imageViewCreditCardBack= findViewById(R.id.imageViewCreditCardBack);
        imageViewCreditCardBack.setOnClickListener(view -> finish());

        // Add TextWatchers to EditTexts
        editTextCreditCardNumber.addTextChangedListener(cardNumberWatcher);
        editTextExpDate.addTextChangedListener(expDateWatcher);
        editTextCVV.addTextChangedListener(cvvWatcher);

        // Set onClickListener for imageViewCreditCardProceed
        imageViewCreditCardProceed.setOnClickListener(v -> processPayment());

        // Set onClickListener for imageViewCreditCardCancel
        imageViewCreditCardCancel.setOnClickListener(v -> {
           finish();
        });
    }

    private void processPayment() {
        // Validate payment details
        String cardNumber = editTextCreditCardNumber.getText().toString().trim();
        String expiryDate = editTextExpDate.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();
        String cardName = editTextCreditCardName.getText().toString().trim();

        if (isValidPaymentDetails(cardName, cardNumber, expiryDate, cvv)) {
            // Process payment
            // Call the method to add order to the database
            addOrderToDatabase(this);
        } else {
            Toast.makeText(this, "Invalid payment details", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPaymentDetails(String cardName, String cardNumber, String expiryDate, String cvv) {
        return !TextUtils.isEmpty(cardName) && !TextUtils.isEmpty(cardNumber) &&
                !TextUtils.isEmpty(expiryDate) && !TextUtils.isEmpty(cvv);
    }

    private void addOrderToDatabase(Context context) {
        // Check if currentUser is not null
        if (HomeActivity.currentUser != null) {
            // Get current user ID, pharmacy ID, and cart details
            int userId = Integer.parseInt(HomeActivity.currentUser.getId());
            double orderAmount = HomeActivity.cart.getTotal();
            String pharmacyId = HomeActivity.cart.getProducts().get(0).getPharmacyId();

            // Calculate order points
            int orderPoints = (int) (0.2 * orderAmount);

            // Get current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String orderDate = dateFormat.format(new Date());
            String orderTime = timeFormat.format(new Date());

            // Initialize order_detail string
            StringBuilder orderDetailBuilder = new StringBuilder();

            // Iterate through products in the cart and append product name and quantity
            for (Product product : HomeActivity.cart.getProducts()) {
                orderDetailBuilder.append("Product: ").append(product.getName()).append("\n");
                orderDetailBuilder.append("Quantity: ").append(product.getQuantityInCart()).append("\n\n");
            }

            // Convert StringBuilder to String
            String orderDetail = orderDetailBuilder.toString().trim();

            // Create a request to add the order to the database
            String url = MyApplication.API_PORT + "add_order.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Handle success response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("success")) {
                                // Order added successfully
                                Toast.makeText(context, jsonResponse.getString("success"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CreditCardActivity.this, HomeActivity.class);
                                intent.putExtra("username", HomeActivity.currentUser.getUsername());
                                startActivity(intent);
                                startActivity(new Intent(CreditCardActivity.this, OrderDoneActivity.class));
                                finishAffinity();
                            } else if (jsonResponse.has("error")) {
                                // Error adding order
                                Toast.makeText(context, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Error parsing JSON response
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Handle error
                        Toast.makeText(context, "Error adding order: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    // Add parameters for the request
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(userId));
                    params.put("pharmacy_id", pharmacyId); // Added pharmacy_id parameter
                    params.put("order_amount", String.valueOf(orderAmount));
                    params.put("order_points", String.valueOf(orderPoints));
                    params.put("order_date", orderDate);
                    params.put("order_time", orderTime);
                    params.put("order_detail", orderDetail); // Added order_detail parameter
                    return params;
                }
            };

            // Add the request to the request queue
            Volley.newRequestQueue(context).add(request);
        } else {
            // Handle the case where currentUser is null
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

}
