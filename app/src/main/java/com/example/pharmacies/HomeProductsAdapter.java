package com.example.pharmacies;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.ProductViewHolder> implements Filterable {

    private List<Product> products;
    private static List<Product> filteredProducts;
    private final Context context;
    private static OnItemClickListener listener;
    List<Pharmacy> pharmacies;

    public HomeProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        filteredProducts = new ArrayList<>(products);
        pharmacies= HomeActivity.pharmacies;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        HomeProductsAdapter.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentItem = filteredProducts.get(position);

        // Set data to views
        holder.textViewProductName.setText(currentItem.getName());
        holder.textViewProductPrice.setText(currentItem.getPrice() + " SAR");
        if (CompareProductsActivity.running){
            holder.imageViewCompare.setVisibility(View.INVISIBLE);
        }


        holder.imageViewLike.setOnClickListener(view -> {
            if (HomeActivity.currentUser.getUsername().equals("guest")){
                Toast.makeText(context, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                return;
            }
            HomeActivity.addToWishlist(context, currentItem.getProductId(), Integer.parseInt(HomeActivity.currentUser.getId()));
            Log.d("HomeActivity", "Adding product ID " + currentItem.getProductId() + " to wishlist for user ID " + HomeActivity.currentUser.getId());

        });


        holder.imageViewCompare.setOnClickListener(view -> {
            if (HomeActivity.currentUser.getUsername().equals("guest")){
                Toast.makeText(context, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                return;
            }
            context.startActivity(new Intent(context, CompareProductsActivity.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CompareProductsActivity.productLeft = currentItem;
                    CompareProductsActivity.update();
                }
            }, 500);

        });

        Image imageBlob2= currentItem.getImageBlob();
        if (imageBlob2!=null){

            holder.imageViewProductImage.setImageBitmap(imageBlob2.getBitmap());
            holder.imageViewProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        Pharmacy currentPharmacy= findPharmacy(currentItem.getPharmacyId());
        Image imageBlob= currentPharmacy.getImageBlob();
        if (imageBlob!=null){
            holder.imageViewPharmacy.setImageBitmap(imageBlob.getBitmap());
        }

        if (currentItem.getStock().equals("out of stock")){
            holder.imageViewAdapterProductStock.setImageResource(R.drawable.outofstock);
        }

    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProductImage, imageViewLike, imageViewAddToCart, imageViewPharmacy, imageViewCompare, imageViewAdapterProductStock;
        public TextView textViewProductName, textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductImage = itemView.findViewById(R.id.imageViewLayoutProductImage);
            textViewProductName = itemView.findViewById(R.id.textViewLayoutProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewLayoutProductPrice);
            imageViewLike = itemView.findViewById(R.id.imageViewLayoutProductLike);
            imageViewAddToCart = itemView.findViewById(R.id.imageViewLayoutProductAddToCart);
            imageViewPharmacy= itemView.findViewById(R.id.imageViewProductLayoutPharmacy);
            imageViewCompare= itemView.findViewById(R.id.imageViewLayoutProductCompare);
            imageViewAdapterProductStock= itemView.findViewById(R.id.imageViewAdapterProductStock);

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


    private Pharmacy findPharmacy(String id){
        Pharmacy pharmacy = null;
        for (int i = 0; i < pharmacies.size(); i++) {
            if (pharmacies.get(i).getId().equals(id)){
                pharmacy=pharmacies.get(i);
            }
        }
        return pharmacy;
    }



}
