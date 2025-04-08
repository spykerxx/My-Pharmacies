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

public class WishlistProductsAdapter extends RecyclerView.Adapter<WishlistProductsAdapter.ProductViewHolder> {

    private static List<Product> products = new ArrayList<>();
    private final Context context;


    public WishlistProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void setProducts(List<Product> products) {
        WishlistProductsAdapter.products = products;
    }





    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wishlist_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentItem = products.get(position);

        // Set data to views
        holder.textViewProductName.setText(currentItem.getName());

        holder.textViewProductPrice.setText(String.valueOf(currentItem.getPrice() + " SAR"));
        holder.imageViewAddToCart.setOnClickListener(v -> {
            // Start EventActivity and pass the event name in the bundle
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("productName", currentItem.getName());
            context.startActivity(intent);

        });


        // Remove item from RecyclerView and wishlist when delete button is clicked
        holder.imageViewDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                // Remove item from RecyclerView
                products.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                removeFromWishlist(currentItem.getProductId(), Integer.parseInt(HomeActivity.currentUser.getId()));
            }
        });


        Image imageBlob= currentItem.getImageBlob();
        if (imageBlob!=null){
            holder.imageViewProductImage.setImageBitmap(imageBlob.getBitmap());
            //holder.imageViewProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProductImage, imageViewDelete, imageViewAddToCart;
        public TextView textViewProductName, textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductImage = itemView.findViewById(R.id.imageViewWishlistLayoutProductImage);
            textViewProductName = itemView.findViewById(R.id.textViewWishlistLayoutProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewWishlistLayoutProductPrice);
            imageViewAddToCart = itemView.findViewById(R.id.imageViewLayoutWishlistAddToCart);
            imageViewDelete = itemView.findViewById(R.id.imageViewLayoutWishlistDelete);

        }
    }

    public void removeFromWishlist(int productId, int userId) {
        // Create a request to remove the product from the wishlist

        String url = MyApplication.API_PORT+"remove_wishlist.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle success response, if needed
                    Log.d("WishlistAdapter", "Product removed from wishlist: " + response);
                },
                error -> {
                    // Handle error, if needed
                    Log.e("WishlistAdapter", "Error removing product from wishlist: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (product_id and user_id)
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request);
    }


}
