package com.example.pharmacies;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ProductViewHolder> {

    private static List<Order> orders = new ArrayList<>();
    private final Context context;


    public PointsAdapter(Context context, List<Order> orders) {
        this.context = context;
        PointsAdapter.orders = orders;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_points, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order currentItem = orders.get(position);

        // Set data to views
        holder.textViewPoints.setText(currentItem.getOrderPoints()+" points");
        holder.textViewOrderNumber.setText("#"+ currentItem.getNumber());
        holder.textViewDate.setText(currentItem.getOrderDate());


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPoints, textViewDate, textViewOrderNumber;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPoints = itemView.findViewById(R.id.textViewLayoutPointsPoints);
            textViewDate = itemView.findViewById(R.id.textViewLayoutPointsDate);
            textViewOrderNumber = itemView.findViewById(R.id.textViewLayoutPointsOrderNumber);

        }
    }

}
