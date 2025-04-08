package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryOrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DevlieryOrdersHistoryAdapter adapter;
    ImageView back;
    ArrayList<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_history);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewDeliveryHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back= findViewById(R.id.imageViewOrderHistoryBack);
                back.setOnClickListener(view -> finish());

        fetchOrdersAndSetupRecyclerView(DeliveryHomeActivity.delivery.getUsername());


    }

    private void fetchOrdersHistory(String deliveryName, Context context, OrdersCallback callback) {
        orders = new ArrayList<>();
        String ORDERS_URL = MyApplication.API_PORT + "orders_histroy_delivery.php"; // Update the URL if necessary
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDERS_URL,
                response -> {
                    Log.d("Orders Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        // Check if there are orders available
                        if (jsonArray.length() > 0) {
                            // Iterate through each order
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderObject = jsonArray.getJSONObject(i);
                                // Extract order details
                                int orderNumber = orderObject.getInt("number");
                                String orderDate = orderObject.getString("order_date");
                                String orderTime = orderObject.getString("order_time");
                                double orderAmount = orderObject.getDouble("order_amount");
                                int orderPoints = orderObject.getInt("order_points");
                                String deliveryDate = orderObject.getString("delivery_date");
                                String deliveryTime = orderObject.getString("delivery_time");
                                String deliveryNumber = orderObject.getString("delivery_number");
                                String status = orderObject.getString("status");
                                // Create Order object or process as needed
                                Order order = new Order(orderDate, orderTime, orderAmount);
                                order.setNumber(orderNumber);
                                order.setDeliveryDate(deliveryDate);
                                order.setDeliveryTime(deliveryTime);
                                order.setDeliveryNumber(deliveryNumber);
                                order.setDeliveryName(deliveryName);
                                order.setOrderPoints(orderPoints);
                                order.setStatus(status);
                                orders.add(order);

                            }

                        } else {
                            // No orders found
                            Log.e("Orders", "no orders found!");
                        }
                        // Invoke the callback with the fetched orders
                        callback.onOrdersFetched(orders);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        // Toast.makeText(context, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle Volley error
                    // Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("delivery_name", deliveryName); // Change the parameter name to delivery_name
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public interface OrdersCallback {
        void onOrdersFetched(ArrayList<Order> orders);
    }

    private void fetchOrdersAndSetupRecyclerView(String delivery_name) {
        fetchOrdersHistory(delivery_name, this, orders -> {

            // Create and set the adapter for the RecyclerView
            adapter = new DevlieryOrdersHistoryAdapter(DeliveryOrderHistoryActivity.this, orders);
            recyclerView.setAdapter(adapter);
        });
    }

}
