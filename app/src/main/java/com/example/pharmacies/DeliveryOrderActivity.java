package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DeliveryOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        getSupportActionBar().hide();

        // Retrieve extras from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int orderNumber = extras.getInt("order_number");
            double orderAmount = extras.getDouble("order_amount");
            String customerName = extras.getString("customer_name");
            String customerShortAddress = extras.getString("customer_short_address");
            String pharmacyName = extras.getString("pharmacy_name");
            String pharmacyAddress = extras.getString("pharmacy_address");
            String pharmacyPhone = extras.getString("pharmacy_phone");
            String customerPhone = extras.getString("customer_phone");


            ImageView imageViewBack= findViewById(R.id.imageViewDeliveryBack);
            imageViewBack.setOnClickListener(view -> finish());

            ImageView imageViewTakeOrder= findViewById(R.id.imageViewDeliveryTakeOrder);
            imageViewTakeOrder.setOnClickListener(view -> {
                // Update order status to phase3 when imageViewTakeOrder is clicked
                updateOrderStatus(orderNumber);
            });


            TextView textViewOrderNumber = findViewById(R.id.orderNumberTextView);
            textViewOrderNumber.setText("Order Number: " + orderNumber);

            TextView textViewOrderAmount = findViewById(R.id.orderAmountTextView);
            textViewOrderAmount.setText("Order Amount: " + orderAmount+ " SAR");

            TextView textViewCustomerName = findViewById(R.id.customerNameTextView);
            textViewCustomerName.setText("Customer Name: " + customerName);

            TextView textViewCustomerShortAddress = findViewById(R.id.customerAddressTextView);
            textViewCustomerShortAddress.setText("Customer Address: " + customerShortAddress);

            TextView textViewPharmacyName = findViewById(R.id.pharmacyNameTextView);
            textViewPharmacyName.setText("Pharmacy Name: " + pharmacyName);

            TextView textViewPharmacyAddress = findViewById(R.id.pharmacyAddressTextView);
            textViewPharmacyAddress.setText("Pharmacy Address: " + pharmacyAddress);

            TextView textViewPharmacyPhone = findViewById(R.id.pharmacyPhoneTextView);
            textViewPharmacyPhone.setText("Pharmacy Phone: " + pharmacyPhone);

            TextView textViewCustomerPhone = findViewById(R.id.customerPhoneTextView);
            textViewCustomerPhone.setText("Customer Phone: " + customerPhone);
        }
    }

    private void updateOrderStatus(int orderNumber) {
        String url = MyApplication.API_PORT + "pickup_order.php";

        // Get the username from DeliveryHomeActivity.delivery.getUsername()
        String username = DeliveryHomeActivity.delivery.getUsername();

        // Create a StringRequest to make a POST request to the PHP file
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response here if needed
                    Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors here if needed
                    Toast.makeText(this, "Error updating order status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            // Pass parameters to the PHP file
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_number", String.valueOf(orderNumber));
                params.put("name", username); // Add the username parameter
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
