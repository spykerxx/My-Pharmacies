package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {
    private TextView textViewProductQuantity, productPrice, tv_productName, tv_productDescription;
    private ImageView productImage, buttonAddToCart, addToWishlist, productCompare, imageViewPharmacy, imageViewProductInStock;

    private int quantity = 1; // Initial quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().hide();
        // Initialize views

        // Get the event name from the bundle
        String productName = getIntent().getStringExtra("productName");

        // Find the event in the eventList by name
        Product product = findProductByName(productName);
       ImageView imageViewProductBack= findViewById(R.id.imageViewProductBack);
       imageViewProductBack.setOnClickListener(view -> finish());

        if (product != null) {
            tv_productName = findViewById(R.id.textViewProductPageName);
            productPrice = findViewById(R.id.textViewProductPagePrice);
            productImage = findViewById(R.id.imageViewProductPageImage);
            buttonAddToCart = findViewById(R.id.imageViewProductPageAddToCartButton);
            productCompare = findViewById(R.id.imageViewProductPageCompare);
            addToWishlist = findViewById(R.id.imageViewProductPageLike);
            textViewProductQuantity = findViewById(R.id.textViewProductQuantity);
            tv_productDescription= findViewById(R.id.textViewProductPageDescription);
            ImageView imageViewProductPageIncreaseQuantity = findViewById(R.id.imageViewProductPageIncreaseQuantity);
            ImageView imageViewProductPageDecreaseQuantity = findViewById(R.id.imageViewProductPageDecreaseQuantity);
            ImageView imageViewProductInStock= findViewById(R.id.imageViewProductInStock);

            if (product.getStock().equals("out of stock")){
                imageViewProductInStock.setImageResource(R.drawable.outofstock);
            }


            productCompare.setOnClickListener(view -> {

                if (HomeActivity.currentUser.getUsername().equals("guest")){
                    Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                    return;
                }


                startActivity(new Intent(ProductActivity.this, CompareProductsActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CompareProductsActivity.productLeft = product;
                        CompareProductsActivity.update();
                    }
                }, 500);

            });


            ImageView imageViewCart= findViewById(R.id.imageViewProductPageViewCart);
            imageViewCart.setOnClickListener(view -> {
                if (HomeActivity.currentUser.getUsername().equals("guest")){
                    Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(ProductActivity.this, ShoppingCartActivity.class));
            });


            imageViewPharmacy= findViewById(R.id.imageViewPharmacyImageProduct);
            Pharmacy currentPharmacy= findPharmacy(product.getPharmacyId());
            Image imageBlob= currentPharmacy.getImageBlob();
            if (imageBlob!=null){
                imageViewPharmacy.setImageBitmap(imageBlob.getBitmap());
                imageViewPharmacy.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }


            tv_productName.setText(product.getName());
            productPrice.setText(product.getPrice()+" SAR");

            tv_productDescription.setText(product.getDescription());
             // Set the product image
            Image imageBlob2= product.getImageBlob();
            if (imageBlob2!=null){
                productImage.setImageBitmap(imageBlob2.getBitmap());
            }
            // Set initial quantity
            textViewProductQuantity.setText(String.valueOf(quantity));

            // Increase quantity when plus button is clicked
            imageViewProductPageIncreaseQuantity.setOnClickListener(v -> {
                quantity++;
                textViewProductQuantity.setText(String.valueOf(quantity));
            });

            // Decrease quantity when minus button is clicked
            imageViewProductPageDecreaseQuantity.setOnClickListener(v -> {
                if (quantity > 1) { // Ensure quantity doesn't go below 1
                    quantity--;
                    textViewProductQuantity.setText(String.valueOf(quantity));
                }
            });

            buttonAddToCart.setOnClickListener(v -> {

                if (HomeActivity.currentUser.getUsername().equals("guest")){
                    Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if the product already exists in the cart
                boolean productExists = false;
                for (Product p : HomeActivity.cart.getProducts()) {
                    if (p.getName().equals(product.getName())) {
                        // Increment the quantity in cart
                        p.setQuantityInCart(p.getQuantityInCart() + Integer.parseInt(textViewProductQuantity.getText().toString()));
                        productExists = true;
                        break;
                    }
                }
                // If the product doesn't exist, add it to the cart
                if (!productExists) {
                    product.setQuantityInCart(Integer.parseInt(textViewProductQuantity.getText().toString()));
                    HomeActivity.cart.addProduct(product);
                }
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
                HomeActivity.cart.updateCart();
            });

            // Add product to wishlist
            addToWishlist.setOnClickListener(v -> {
                if (HomeActivity.currentUser.getUsername().equals("guest")){
                    Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                    return;
                }

               HomeActivity.addToWishlist(this, product.getProductId(), Integer.parseInt(HomeActivity.currentUser.getId()));
            });

        }

    }

    private Product findProductByName(String productName) {
        if (HomeActivity.products != null) {
            for (Product product : HomeActivity.products) {
                if (product.getName().equals(productName)) {
                    return product;
                }
            }
        }
        return null;
    }

    private Pharmacy findPharmacy(String id){
        Pharmacy pharmacy = null;
        for (int i = 0; i < HomeActivity.pharmacies.size(); i++) {
            if (HomeActivity.pharmacies.get(i).getId().equals(id)){
                pharmacy=HomeActivity.pharmacies.get(i);
            }
        }
        return pharmacy;
    }
}
