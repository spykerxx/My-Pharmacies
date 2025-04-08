package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PharmacySearchProductActivity extends AppCompatActivity {

    public static EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_search_product);
        getSupportActionBar().hide();

        ImageView back= findViewById(R.id.imageViewSearchBack);
        back.setOnClickListener(view -> finish());
        editTextSearch= findViewById(R.id.editTextSearchProduct);
        ImageView next= findViewById(R.id.imageViewSearchProductNext);
        next.setOnClickListener(view -> {

            String productid = editTextSearch.getText().toString().trim();
            Product product= findProduct(productid);
            if (product!=null){
                // Declare the Intent
                Intent intent = new Intent(PharmacySearchProductActivity.this, ModifiyProductActivity.class);
                intent.putExtra("product_id", productid);
                startActivity(intent);

            }
            else {
                Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();

            }
        });

        ImageView imageViewPharmacySearchBarcode = findViewById(R.id.imageViewPharmacySearchBarcode);
        imageViewPharmacySearchBarcode.setOnClickListener(view -> {
            Intent intent = new Intent(PharmacySearchProductActivity.this, BarcodeActivity.class);
            boolean pharmacySearch = true;
            intent.putExtra("pharmacySearch", pharmacySearch);
            startActivity(intent);
        });


    }

    // Method to find a product by ID
    public Product findProduct(String barcode) {
        for (Product product : PharmacyHomeActivity.products) {
            if (product.getBarcode() .equals(barcode)) {
                return product; // Return the product if found
            }
        }
        return null; // Return null if no product with the specified ID is found
    }
}
