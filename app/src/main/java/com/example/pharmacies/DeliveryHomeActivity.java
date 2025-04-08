package com.example.pharmacies;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryHomeActivity extends AppCompatActivity {

    public static Delivery delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_home);
        getSupportActionBar().hide();


        String name = getIntent().getStringExtra("username");
        getUserDetails(name);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set listener for bottom navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Handle item selection based on item ID
            switch (item.getItemId()) {
                case R.id.menu_available_orders:
                    selectedFragment = new DeliveryHomeFragment();
                    break;
                case R.id.menu_my_orders:
                    selectedFragment = new DeliveryMyOrdersFragment();
                    break;

                case R.id.menu_my_notifications:
                    selectedFragment = new NotificationsFragment();
                    break;
            }

            // Replace the current fragment with the selected fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }

            return false;
        });

        // Set the default fragment when the activity is first created
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DeliveryHomeFragment()).commit();
    }

    public static void fetchDeliveryOrdersWithDetails(Context context, DeliveryOrdersWithDetailsCallback callback) {
        // API call to fetch delivery orders with user, address, and pharmacy details
        String URL = MyApplication.API_PORT + "delivery_orders.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<Order> orders = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract order number
                            int orderNumber = jsonObject.getInt("order_number");
                            Log.d("OrderNumberLog", "Order Number: " + orderNumber);
                            // Extract user object from the JSON response
                            JSONObject userObject = jsonObject.getJSONObject("user");
                            // Extract username from the user object
                            String username = userObject.getString("username");

                            // Extract pharmacy id
                            String pharmacyId = jsonObject.getString("pharmacy_id");
                            // Extract pharmacy name
                            String pharmacyName = jsonObject.getString("pharmacy_name");
                            // Extract pharmacy address
                            String pharmacyAddress = jsonObject.getString("pharmacy_address");
                            // Extract order time
                            String orderTime = jsonObject.getString("order_time");
                            // Extract pharmacy image
                            String pharmacyImage = jsonObject.getString("pharmacy_image");
                            // Extract order amount
                            double orderAmount = jsonObject.getDouble("order_amount");
                            // Extract customer name
                            String customerName = jsonObject.getString("customer_name");
                            // Extract customer phone
                            String customerPhone = jsonObject.getString("customer_phone");

                            // Create Order object with extracted details
                            Order order = new Order();
                            order.setNumber(orderNumber);
                            // Log the order number
                            Log.d("OrderNumber", "Order number: " + order.getNumber());
                            order.setOrderTime(orderTime);
                            order.setOrderAmount(orderAmount);

                            // Create User object with extracted details
                            User user = new User();
                            user.setUsername(username);

                            // Create Address object with extracted details
                            JSONObject addressObject = jsonObject.getJSONObject("address");
                            Address address = new Address(
                                    // Add more address details as needed
                            );
                            address.setShortAddress(addressObject.getString("address_line"));
                            address.setFullName(addressObject.getString("fullName"));
                            address.setPrimaryPhone(addressObject.getString("primaryPhone"));

                            // Set the Address object to the User
                            user.setAddress(address);

                            // Set User to Order
                            order.setUser(user);

                            // Create Pharmacy object with extracted details
                            Pharmacy pharmacy = new Pharmacy();
                            pharmacy.setId(pharmacyId);
                            pharmacy.setName(pharmacyName);
                            pharmacy.setAddress(pharmacyAddress);
                            pharmacy.setImage(pharmacyImage);
                            pharmacy.setPhone(jsonObject.getString("pharmacy_phone"));

                            // Add Pharmacy object to the Order
                            order.setPharmacy(pharmacy);

                            // Add order to the list
                            orders.add(order);
                        }
                        // Callback with fetched orders
                        callback.onSuccess(orders);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("JSON parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    callback.onError("Volley error: " + error.getMessage());
                }) {
        };
        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }


    // Define a callback interface
    interface DeliveryOrdersWithDetailsCallback {
        void onSuccess(ArrayList<Order> orders);
        void onError(String errorMessage);
    }

    private void getUserDetails(final String username) {
        String USER_DETAILS_URL = MyApplication.API_PORT + "get_delivery.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_DETAILS_URL,
                response -> {
                    Log.d("User Details Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject userObject = jsonArray.getJSONObject(0);
                            String fullName = userObject.getString("fullName");
                            String phone = userObject.getString("phone");
                            int id = userObject.getInt("id");


                            // Extract other user details as needed

                            delivery = new Delivery(username, phone, fullName);
                            delivery.setId(id);


                            // Proceed with the user details
                        } else {
                            // No user found
                            Log.e("User Details", "No user found for username: " + username);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DeliveryHomeActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(DeliveryHomeActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


}
