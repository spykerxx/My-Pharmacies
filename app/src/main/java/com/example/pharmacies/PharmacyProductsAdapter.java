package com.example.pharmacies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PharmacyProductsAdapter extends RecyclerView.Adapter<PharmacyProductsAdapter.ProductViewHolder> implements Filterable {

    private List<Product> products;
    private static List<Product> filteredProducts;
    private final Context context;
    private static OnItemClickListener listener;

    public PharmacyProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        filteredProducts = new ArrayList<>(products);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        PharmacyProductsAdapter.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pharmacy_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentItem = filteredProducts.get(position);

        // Set data to views
        if (currentItem!=null){
        holder.textViewProductName.setText(currentItem.getName());
        holder.textViewProductPrice.setText(String.valueOf(currentItem.getPrice()) + " SAR");

            Image imageBlob= currentItem.getImageBlob();
            if (imageBlob!=null){
                holder.imageViewProductImage.setImageBitmap(imageBlob.getBitmap());
                holder.imageViewProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
    }
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProductImage;
        public TextView textViewProductName, textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductImage = itemView.findViewById(R.id.imageViewLayoutPharmacyProductImage);
            textViewProductName = itemView.findViewById(R.id.textViewLayoutPharmacyProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewLayoutPharmacyProductPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(filteredProducts.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search query is empty, return the original list
                filteredList.addAll(products);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Iterate through the original list and add matching items to the filtered list
                for (Product product : products) {
                    if (product.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredProducts.clear();
            filteredProducts.addAll((List) results.values);
            notifyDataSetChanged();  // Notify RecyclerView of data changes
        }
    };
}
