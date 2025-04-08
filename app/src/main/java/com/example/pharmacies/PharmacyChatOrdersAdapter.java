package com.example.pharmacies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyChatOrdersAdapter extends RecyclerView.Adapter<PharmacyChatOrdersAdapter.ChatRequestViewHolder> {

    private ArrayList<String> chatRequests;
    private final Context context;

    public PharmacyChatOrdersAdapter(Context context, ArrayList<String> chatRequests) {
        this.context = context;
        this.chatRequests = chatRequests;
    }

    @NonNull
    @Override
    public ChatRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_order, parent, false);
        return new ChatRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRequestViewHolder holder, int position) {
        String currentItem = chatRequests.get(position);

        // Set data to views
        holder.textViewCustomerName.setText(currentItem);
        holder.imageViewLayoutDeliveryCancel.setOnClickListener(v -> {
            // Remove the item from the dataset
            chatRequests.remove(position);
            // Notify the adapter that the dataset has changed
            notifyDataSetChanged(); // Or use notifyItemRemoved(position) for smoother animation
        });

        holder.imageViewLayoutDeliveryPickup.setOnClickListener(v -> {
            PharmacyHomeActivity.user_id=currentItem;
           context.startActivity(new Intent(context, PharmacyChatActivity.class));


        });
    }

    @Override
    public int getItemCount() {
        return chatRequests.size();
    }

    public static class ChatRequestViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCustomerName;
        private ImageView imageViewLayoutDeliveryPickup, imageViewLayoutDeliveryCancel;


        public ChatRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.textViewLayoutDeliveryCustomerName);
            imageViewLayoutDeliveryPickup= itemView.findViewById(R.id.imageViewLayoutDeliveryPickup);
            imageViewLayoutDeliveryCancel= itemView.findViewById(R.id.imageViewLayoutDeliveryCancel);
        }
    }
}
