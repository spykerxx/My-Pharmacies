package com.example.pharmacies;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PharmacyOrdersAdapter extends RecyclerView.Adapter<PharmacyOrdersAdapter.ProductViewHolder> {

    private List<Order> orders;
    private final Context context;

    public PharmacyOrdersAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders; // Initialize with the orders list passed from the activity
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_order, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyOrdersAdapter.ProductViewHolder holder, int position) {
        Order currentItem = orders.get(position);

        // Set data to views
        holder.textViewCustomerName.setText(currentItem.getNumber());

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
        String url = MyApplication.API_PORT + "ready_order.php";

        // Create a StringRequest to make a POST request to the PHP file
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response here if needed

                },
                error -> {
                    // Handle errors here if needed

                }) {

            // Pass parameters to the PHP file
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_number", String.valueOf(orderNumber));
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context.getApplicationContext()).add(stringRequest);
    }

}
