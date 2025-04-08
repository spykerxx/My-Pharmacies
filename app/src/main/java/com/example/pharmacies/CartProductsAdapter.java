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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.ProductViewHolder> {

    private static List<Product> products = new ArrayList<>();
    private final Context context;
    private static OnItemClickListener listener;

    public CartProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void setProducts(List<Product> products) {
        CartProductsAdapter.products = products;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnQuantityChangedListener quantityChangedListener;

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.quantityChangedListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentItem = products.get(position);

        // Set data to views
        holder.textViewProductName.setText(currentItem.getName());
        holder.textViewProductQuantity.setText(String.valueOf(currentItem.getQuantityInCart()));
        holder.textViewProductPrice.setText(String.valueOf(currentItem.getPrice() * currentItem.getQuantityInCart()) + " SAR");

        // Increase quantity when plus button is clicked
        holder.imageViewIncreaseQuantity.setOnClickListener(v -> {
            int updatedQuantity = currentItem.getQuantityInCart() + 1;
            currentItem.setQuantityInCart(updatedQuantity);
            holder.textViewProductQuantity.setText(String.valueOf(updatedQuantity));
            holder.textViewProductPrice.setText(String.valueOf(currentItem.getPrice() * updatedQuantity) + " SAR");
            if (quantityChangedListener != null) {
                quantityChangedListener.onQuantityChanged();
            }
        });

        // Decrease quantity when minus button is clicked
        holder.imageViewDecreaseQuantity.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantityInCart();
            if (currentQuantity > 1) {
                int updatedQuantity = currentQuantity - 1;
                currentItem.setQuantityInCart(updatedQuantity);
                holder.textViewProductQuantity.setText(String.valueOf(updatedQuantity));
                holder.textViewProductPrice.setText(currentItem.getPrice() * updatedQuantity + " SAR");
                if (quantityChangedListener != null) {
                    quantityChangedListener.onQuantityChanged();
                }
            }
        });

        holder.imageViewDelete.setOnClickListener(v -> {
                products.remove(currentItem);
                quantityChangedListener.onQuantityChanged();
                HomeActivity.cart.updateCart();

        });

        holder.imageViewLike.setOnClickListener(v -> {
            HomeActivity.addToWishlist(context, currentItem.getProductId(), Integer.parseInt(HomeActivity.currentUser.getId()));


        });



        Image imageBlob= currentItem.getImageBlob();
        if (imageBlob!=null){
            holder.imageViewProductImage.setImageBitmap(imageBlob.getBitmap());
           // holder.imageViewProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProductImage, imageViewLike, imageViewDelete, imageViewIncreaseQuantity, imageViewDecreaseQuantity;
        public TextView textViewProductName, textViewProductPrice, textViewProductQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductImage = itemView.findViewById(R.id.imageViewLayoutCartProductImage);
            textViewProductName = itemView.findViewById(R.id.textViewLayoutCartProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewLayoutCartProductPrice);
            imageViewLike = itemView.findViewById(R.id.imageViewLayoutCartProductLike);
            imageViewDelete = itemView.findViewById(R.id.imageViewLayoutCartProductDelete);
            textViewProductQuantity = itemView.findViewById(R.id.textViewCartProductQuantity);
            imageViewIncreaseQuantity= itemView.findViewById(R.id.imageViewCartIncreaseQuantity);
            imageViewDecreaseQuantity= itemView.findViewById(R.id.imageViewCartDecreaseQuantity);



            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(products.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }
}
