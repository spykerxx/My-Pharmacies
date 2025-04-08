package com.example.pharmacies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DevlieryOrdersHistoryAdapter extends RecyclerView.Adapter<DevlieryOrdersHistoryAdapter.ProductViewHolder> {

    private static List<Order> orders = new ArrayList<>();
    private final Context context;


    public DevlieryOrdersHistoryAdapter(Context context, List<Order> orders) {
        this.context = context;
        DevlieryOrdersHistoryAdapter.orders = orders;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_history_delivery, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order currentItem = orders.get(position);

        // Set data to views
        holder.textViewOrderNumberValue.setText(String.valueOf(currentItem.getNumber()));
        holder.textViewOrderDateValue.setText(currentItem.getOrderDate());
        holder.textViewOrderTimeValue.setText(currentItem.getOrderTime());
        holder.textViewOrderAmountValue.setText(currentItem.getOrderAmount() + " SAR");
        holder.textViewDeliveryDateValue.setText(currentItem.getDeliveryDate());
        holder.textViewDeliveryTimeValue.setText(currentItem.getDeliveryTime());


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderNumberValue, textViewOrderDateValue, textViewOrderTimeValue, textViewOrderAmountValue,
                 textViewDeliveryDateValue, textViewDeliveryTimeValue;



        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderNumberValue = itemView.findViewById(R.id.textViewDeliveryOrderNumberValue);
            textViewOrderDateValue = itemView.findViewById(R.id.textViewDeliveryOrderDateValue);
            textViewOrderTimeValue = itemView.findViewById(R.id.textViewDeliveryOrderTimeValue);
            textViewOrderAmountValue = itemView.findViewById(R.id.textViewDeliveryOrderAmountValue);
            textViewDeliveryDateValue = itemView.findViewById(R.id.textViewDeliveryDateValue);
            textViewDeliveryTimeValue = itemView.findViewById(R.id.textViewDeliveryTimeValue);

        }
    }

}
