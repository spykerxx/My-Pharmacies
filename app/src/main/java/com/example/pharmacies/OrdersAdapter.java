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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductViewHolder> {

    private static List<Order> orders = new ArrayList<>();
    private final Context context;


    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        OrdersAdapter.orders = orders;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_history, parent, false);
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
        holder.textViewEarnedPointsValue.setText(currentItem.getOrderPoints() + " points");
        holder.textViewDeliveryDateValue.setText(currentItem.getDeliveryDate());
        holder.textViewDeliveryTimeValue.setText(currentItem.getDeliveryTime());
        holder.textViewDeliveryRepresentativeNumberValue.setText(currentItem.getDeliveryNumber());
        holder.textViewDeliveryRepresentativeNameValue.setText(currentItem.getDeliveryName());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderNumberValue, textViewOrderDateValue, textViewOrderTimeValue, textViewOrderAmountValue,
                textViewEarnedPointsValue, textViewDeliveryDateValue, textViewDeliveryTimeValue,
                textViewDeliveryRepresentativeNumberValue, textViewDeliveryRepresentativeNameValue;



        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderNumberValue = itemView.findViewById(R.id.textViewOrderNumberValue);
            textViewOrderDateValue = itemView.findViewById(R.id.textViewOrderDateValue);
            textViewOrderTimeValue = itemView.findViewById(R.id.textViewOrderTimeValue);
            textViewOrderAmountValue = itemView.findViewById(R.id.textViewOrderAmountValue);
            textViewEarnedPointsValue = itemView.findViewById(R.id.textViewEarnedPointsValue);
            textViewDeliveryDateValue = itemView.findViewById(R.id.textViewDeliveryDateValue);
            textViewDeliveryTimeValue = itemView.findViewById(R.id.textViewDeliveryTimeValue);
            textViewDeliveryRepresentativeNumberValue = itemView.findViewById(R.id.textViewRepresentativeNumberValue);
            textViewDeliveryRepresentativeNameValue = itemView.findViewById(R.id.textViewRepresentativeNameValue);

        }
    }

}
