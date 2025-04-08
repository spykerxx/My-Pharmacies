package com.example.pharmacies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomePharmaciesAdapter extends RecyclerView.Adapter<HomePharmaciesAdapter.NotificationViewHolder> {

    private final List<Pharmacy> pharmacies;
    private final Context context;
    private final List<String> addedImages;

    // Define interface for click listener
    public interface OnItemClickListener {
        void onItemClick(String pharmacyId);
        void onItemClick(Pharmacy pharmacy);
    }

    private final OnItemClickListener listener;

    public HomePharmaciesAdapter(Context context, List<Pharmacy> pharmacies, OnItemClickListener listener) {
        this.context = context;
        this.pharmacies = pharmacies;
        this.listener = listener;
        this.addedImages = new ArrayList<>();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_pharmacy, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Pharmacy currentItem = pharmacies.get(position);
        Image imageBlob= currentItem.getImageBlob();
        if (imageBlob!=null){
        String currentImage = currentItem.getImageBlob().getTag();

        // Check if the current image is already added
        if (!addedImages.contains(currentImage)) {
          holder.imageView.setImageBitmap(imageBlob.getBitmap());
          holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            // Skip adding the item if the image is already added
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the pharmacyId to the listener
                listener.onItemClick(currentItem.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewLayoutPharmacyImage);
        }
    }
}
