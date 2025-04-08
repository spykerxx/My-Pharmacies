package com.example.pharmacies;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeliveryOrdersAdapter extends RecyclerView.Adapter<DeliveryOrdersAdapter.ProductViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private final Context context;

    public DeliveryOrdersAdapter(Context context) {
        this.context = context;

        // Call the fetchDeliveryOrdersWithDetails method from DeliveryHomeActivity
        DeliveryHomeActivity.fetchDeliveryOrdersWithDetails(context, new DeliveryHomeActivity.DeliveryOrdersWithDetailsCallback() {
            @Override
            public void onSuccess(ArrayList<Order> orders) {
                // Update the adapter with fetched orders
                setOrders(orders);

                      }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged(); // Notify RecyclerView about the data change
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_order, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order currentItem = orders.get(position);

        // Set data to views
        holder.textViewCustomerName.setText(currentItem.getUser().getUsername());

        holder.imageViewPickup.setOnClickListener(v -> {
            // Step 1: Remove the item from the orders list
            orders.remove(position);

            // Step 2: Notify the adapter that the item has been removed
            notifyItemRemoved(position);

            // Step 3 (Optional): If you want to animate the removal
            notifyItemRangeChanged(position, orders.size());

            // Call updateOrderStatus method with the order number
            updateOrderStatus(currentItem.getNumber());
        });

        holder.imageViewCancel.setOnClickListener(v -> {
            // Step 1: Remove the item from the orders list
            orders.remove(position);

            // Step 2: Notify the adapter that the item has been removed
            notifyItemRemoved(position);

            // Step 3 (Optional): If you want to animate the removal
            notifyItemRangeChanged(position, orders.size());

        });
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCustomerName;
        ImageView imageViewCancel, imageViewPickup;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.textViewLayoutDeliveryCustomerName);
            imageViewPickup= itemView.findViewById(R.id.imageViewLayoutDeliveryPickup);
            imageViewCancel= itemView.findViewById(R.id.imageViewLayoutDeliveryCancel);

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
                    Toast.makeText(context, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors here if needed
                    Toast.makeText(context, "Error updating order status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(context).add(stringRequest);
    }


}
