package com.example.pharmacies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ModifiyProductActivity extends AppCompatActivity {

    EditText editTextModifiyProductName, editTextModifiyProductPrice, editTextModifiyProductDescription, editTextModifiyProductVolume;
    ImageView imageViewButtonPharmacyAdd, imageViewModifiyProductHeader, back, imageViewProductImage, imageViewButtonDelete;
    Spinner spinnerModifiyProduct, stockSpinner;
    private String profilePicturePath = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiy_product);
        getSupportActionBar().hide();

        String product_id = getIntent().getStringExtra("product_id");
        currentProduct= findProduct(product_id);

        // Find views by their respective IDs
        editTextModifiyProductName = findViewById(R.id.editTextModifiyProductName);
        editTextModifiyProductPrice = findViewById(R.id.editTextModifiyProductPrice);
        editTextModifiyProductDescription = findViewById(R.id.editTextModifiyProductDescription);
        imageViewButtonPharmacyAdd = findViewById(R.id.imageViewButtonPharmacyModifySave);
        imageViewButtonDelete = findViewById(R.id.imageViewButtonPharmacyDeleteProduct);
        imageViewModifiyProductHeader = findViewById(R.id.imageViewModifiyProductHeader);
        spinnerModifiyProduct = findViewById(R.id.spinnerModifiyProduct);
        stockSpinner = findViewById(R.id.spinnerModifiyProductStock);
        imageViewProductImage= findViewById(R.id.imageViewButtonModifiyProductPic);
        editTextModifiyProductVolume= findViewById(R.id.editTextModifiyProductVolume);

        editTextModifiyProductName.setText(currentProduct.getName());
        editTextModifiyProductDescription.setText(currentProduct.getDescription());
        editTextModifiyProductPrice.setText(currentProduct.getPrice()+" SAR");
        editTextModifiyProductVolume.setText(currentProduct.getVolume());

        Image imageBlob= currentProduct.getImageBlob();
        if (imageBlob!=null){
            imageViewProductImage.setImageBitmap(imageBlob.getBitmap());
        }

        imageViewProductImage.setOnClickListener(v -> {
            requestStoragePermission();
            openGallery();
        });

        imageViewButtonDelete.setOnClickListener(v -> {
            // Show a confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked Yes button
                            deleteProduct(currentProduct.getProductId()); // Call deleteProduct method here
                            Toast.makeText(ModifiyProductActivity.this, "Deleted!.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null) // Do nothing if user clicks No
                    .show();
        });

        back= findViewById(R.id.imageViewModifiyProductBack);
        back.setOnClickListener(view -> finish());

        String category = currentProduct.getCategory();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModifiyProduct.setAdapter(adapter);

// Iterate through the adapter to find the index of the category
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(category)) {
                spinnerModifiyProduct.setSelection(i);
                break;
            }
        }




        // Populate the Spinner with options from the category_items array resource
        ArrayAdapter<CharSequence> stockAdatper = ArrayAdapter.createFromResource(this,
                R.array.stock_items, android.R.layout.simple_spinner_item);

        String stockValue = currentProduct.getStock();
        stockAdatper = ArrayAdapter.createFromResource(this,
                R.array.stock_items, android.R.layout.simple_spinner_item);
        stockAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stockSpinner.setAdapter(stockAdatper);

// Iterate through the adapter to find the index of the stock value
        for (int i = 0; i < stockAdatper.getCount(); i++) {
            if (stockAdatper.getItem(i).toString().equals(stockValue)) {
                stockSpinner.setSelection(i);
                break;
            }
        }


        // Set click listener for the add product button
        imageViewButtonPharmacyAdd.setOnClickListener(view -> {


            String ModifiyName = editTextModifiyProductName.getText().toString().trim();
            String ModifiyPrice = editTextModifiyProductPrice.getText().toString().trim();
            String ModifiyDescription = editTextModifiyProductDescription.getText().toString().trim();
            String ModifiyCategory = spinnerModifiyProduct.getSelectedItem().toString();
            String stock = stockSpinner.getSelectedItem().toString();

            updateProduct(currentProduct.getProductId(), ModifiyName, ModifiyPrice, ModifiyDescription, ModifiyCategory, stock);

        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    imageViewProductImage.setImageBitmap(bitmap);
                    profilePicturePath = getPathFromUri(selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ModifiyProductActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private void updateProduct(int productId, String name, String price, String description, String category, String stock) {
        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("productId", String.valueOf(productId)); // Convert productId to String
        params.put("name", name);
        String numericPart = price.replaceAll("[^0-9.]", "");
        double  numericPrice = Double.parseDouble(numericPart);
        params.put("price", numericPart);
        params.put("description", description);
        params.put("category", category);
        params.put("stock", stock);

        // Create a StringRequest to make a POST request with parameters
        String url = MyApplication.API_PORT + "update_product.php"; // Replace with the actual URL
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(ModifiyProductActivity.this, response, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < PharmacyHomeActivity.products.size(); i++) {
                            if (productId == PharmacyHomeActivity.products.get(i).getProductId()) {
                                PharmacyHomeActivity.products.get(i).setName(name);
                                PharmacyHomeActivity.products.get(i).setStock(stock);
                                PharmacyHomeActivity.products.get(i).setDescription(description);
                                PharmacyHomeActivity.products.get(i).setPrice(numericPrice);
                                PharmacyHomeActivity.products.get(i).setCategory(category);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        String errorMessage = "Error updating product: " + error.getMessage();
                        Log.e("YourActivity", errorMessage);
                        Toast.makeText(ModifiyProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ModifiyProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform necessary operations
                Log.d("Permission", "READ_EXTERNAL_STORAGE granted");
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable functionality)
                Log.d("Permission", "READ_EXTERNAL_STORAGE denied");
            }
        }
    }

    public Product findProduct(String barcode) {
        for (Product product : PharmacyHomeActivity.products) {
            if (product.getBarcode() .equals(barcode)) {
                return product; // Return the product if found
            }
        }
        return null; // Return null if no product with the specified ID is found
    }

    private void deleteProduct(int productId) {
        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("productId", String.valueOf(productId)); // Convert productId to String

        // Create a StringRequest to make a POST request with parameters
        String url = MyApplication.API_PORT + "delete_product.php"; // Replace with the actual URL
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(ModifiyProductActivity.this, response, Toast.LENGTH_SHORT).show();
                        PharmacyHomeActivity.deleteProduct(currentProduct.getProductId());
                        // Optionally handle further actions after successful deletion
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        String errorMessage = "Error deleting product: " + error.getMessage();
                        Log.e("ModifyProductActivity", errorMessage);
                        Toast.makeText(ModifiyProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void updateProduct(int productId, String name, String description, String stock, String category, double price) {

            for (int i = 0; i < PharmacyHomeActivity.products.size(); i++) {
                if (productId == PharmacyHomeActivity.products.get(i).getProductId()) {
                    PharmacyHomeActivity.products.get(i).setName(name);
                    PharmacyHomeActivity.products.get(i).setStock(stock);
                    PharmacyHomeActivity.products.get(i).setDescription(description);
                    PharmacyHomeActivity.products.get(i).setPrice(price);
                    PharmacyHomeActivity.products.get(i).setCategory(category);
                }
            }
    }




}
