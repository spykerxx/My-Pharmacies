package com.example.pharmacies;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeOffersAdapter extends RecyclerView.Adapter<HomeOffersAdapter.NotificationViewHolder> {

    private  List<Offer> offers= new ArrayList<>();
    private final Context context;



    public HomeOffersAdapter(Context context, List<Offer> notificationList) {
        this.context = context;
        if (notificationList!=null){
        this.offers = notificationList;
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_offer, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Offer currentItem = offers.get(position);

        if (PharmacyHomeActivity.currentPharmacy!=null){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }

        if (HomeActivity.currentUser!=null){
            holder.imageViewDelete.setVisibility(View.INVISIBLE);
        }
        // Set data to views
        holder.textView.setText(currentItem.getName());
        holder.imageViewDelete.setOnClickListener(view -> showDeleteConfirmationDialog(currentItem.getName()));


        if (currentItem.getImageBlob()!=null){
        holder.imageView.setImageBitmap(currentItem.getImageBlob().getBitmap());
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        ImageView imageViewDelete;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewLayoutOfferImage);
            textView = itemView.findViewById(R.id.textViewLayoutOfferName);
            imageViewDelete= itemView.findViewById(R.id.imageViewOfferDelete);
        }
    }

    private void deleteOffer(String offerName) {

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("name", offerName);

        // API URL for deleting an offer
        String url = MyApplication.API_PORT + "delete_offer.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        handleDeleteResponse(response, offerName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error deleting offer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    // Method to handle delete offer response
    private void handleDeleteResponse(String response, String offerName) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            String message = jsonObject.getString("message");

            if (success) {
                // Offer deleted successfully
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // Remove the deleted offer from the list
                removeDeletedOffer(offerName);
            } else {
                // Failed to delete offer
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeDeletedOffer(String offerName) {
        // Find the position of the deleted offer in the list
        int deletedPosition = -1;
        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).getName().equals(offerName)) {
                deletedPosition = i;
                break;
            }
        }

        // Remove the deleted offer from the list
        if (deletedPosition != -1) {
            offers.remove(deletedPosition);
            // Notify the adapter of the data change
            notifyItemRemoved(deletedPosition);
            notifyItemRangeChanged(deletedPosition, offers.size());
        }
    }


    private void showDeleteConfirmationDialog(String offerName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this offer?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, delete the offer
                        deleteOffer(offerName);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing
                        dialog.dismiss(); // Dismiss the dialog
                    }
                })
                .show();
    }

}
