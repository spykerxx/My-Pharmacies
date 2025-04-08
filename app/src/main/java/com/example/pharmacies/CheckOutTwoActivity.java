package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class CheckOutTwoActivity extends AppCompatActivity {

    private ImageView imageViewCash;
    private ImageView imageViewApplePay;
    private ImageView imageViewCreditCard;
    private ImageView lastClickedPaymentMethod;
    private TextView textViewCheckoutTwoTotal;
    private TextView textViewCheckoutTwoShippingTotal;
    private TextView textViewCheckoutTwoSubtotal;
    private String chosenPaymentMethod="";
    private ImageView imageViewContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_two);
        getSupportActionBar().hide();

        // Find views
        imageViewCash = findViewById(R.id.imageViewCash);
        imageViewApplePay = findViewById(R.id.imageViewApplePay);
        imageViewCreditCard = findViewById(R.id.imageViewCreditCard);

        textViewCheckoutTwoTotal = findViewById(R.id.textViewCheckoutTwoTotal);
        textViewCheckoutTwoShippingTotal = findViewById(R.id.textViewCheckoutTwoShippingTotal);
        textViewCheckoutTwoSubtotal = findViewById(R.id.textViewCheckoutTwoSubtotal);
        setTextViewValues();

        ImageView imageViewCheckOutTwoBack= findViewById(R.id.imageViewCheckOutTwoBack);
        imageViewCheckOutTwoBack.setOnClickListener(view -> finish());

        imageViewContinue= findViewById(R.id.imageViewCheckoutTwoContinue);
        imageViewContinue.setOnClickListener(view -> {
            if (chosenPaymentMethod.equals("Credit Card")){
            startActivity(new Intent(CheckOutTwoActivity.this, CreditCardActivity.class));
            }
            else if (chosenPaymentMethod.isEmpty()){
                Toast.makeText(this, "Choose Payment Method!", Toast.LENGTH_SHORT).show();
            }
            else{
                addOrderToDatabase(this);
            }
        });

        // Set click listeners
        imageViewCash.setOnClickListener(v -> highlightPaymentMethod(imageViewCash, "Cash"));

        imageViewApplePay.setOnClickListener(v -> highlightPaymentMethod(imageViewApplePay, "Apple Pay"));

        imageViewCreditCard.setOnClickListener(v -> highlightPaymentMethod(imageViewCreditCard, "Credit Card"));
    }

    // Method to highlight the selected payment method and reset the previously selected one
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

    private void setTextViewValues() {
        if (HomeActivity.cart != null) {
            // Get values from HomeActivity.cart
            double total = HomeActivity.cart.getTotal();
            double subtotal = HomeActivity.cart.getSubtotal();
            double shippingTotal = HomeActivity.cart.getShippingTotal();

            // Set values to TextViews
            textViewCheckoutTwoTotal.setText(String.valueOf(total));
            textViewCheckoutTwoSubtotal.setText(String.valueOf(subtotal));
            textViewCheckoutTwoShippingTotal.setText(String.valueOf(shippingTotal));
        }
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

                                Intent intent = new Intent(CheckOutTwoActivity.this, HomeActivity.class);
                                intent.putExtra("username", HomeActivity.currentUser.getUsername());
                                intent.putExtra("userId", HomeActivity.currentUser.getId());
                                startActivity(intent);
                                startActivity(new Intent(CheckOutTwoActivity.this, OrderDoneActivity.class));
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
