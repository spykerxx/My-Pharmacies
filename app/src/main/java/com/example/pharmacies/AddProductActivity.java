package com.example.pharmacies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    EditText editTextAddProductName, editTextAddProductPrice, editTextAddProductDescription, editTextAddProductBarcode, editTextAddProductVolume;
    ImageView imageViewButtonPharmacyAdd, imageViewAddProductHeader, back, imageViewProductImage;
    Spinner spinnerAddProduct;
    private String profilePicturePath = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Image imageBlob;
    private Spinner spinnerAddProductStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();

        // Find views by their respective IDs
        editTextAddProductName = findViewById(R.id.editTextAddProductName);
        editTextAddProductPrice = findViewById(R.id.editTextAddProductPrice);
        editTextAddProductDescription = findViewById(R.id.editTextAddProductDescription);
        imageViewButtonPharmacyAdd = findViewById(R.id.imageViewButtonPharmacyAdd);
        imageViewAddProductHeader = findViewById(R.id.imageViewAddProductHeader);
        spinnerAddProduct = findViewById(R.id.spinnerAddProduct);
        imageViewProductImage= findViewById(R.id.imageViewButtonAddProductPic);
        editTextAddProductVolume= findViewById(R.id.editTextAddProductVolume);
        editTextAddProductBarcode= findViewById(R.id.editTextAddProductBarcode);
        spinnerAddProductStock= findViewById(R.id.spinnerAddProductStock);

        // Populate the Spinner with options from the category_items array resource
        ArrayAdapter<CharSequence> stockAdatper = ArrayAdapter.createFromResource(this,
                R.array.stock_items, android.R.layout.simple_spinner_item);

        stockAdatper = ArrayAdapter.createFromResource(this,
                R.array.stock_items, android.R.layout.simple_spinner_item);
        stockAdatper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddProductStock.setAdapter(stockAdatper);

        imageViewProductImage.setOnClickListener(v -> {
            requestStoragePermission();
            openGallery();
        });


        back= findViewById(R.id.imageViewAddProductBack);
        back.setOnClickListener(view -> finish());

        // Populate the Spinner with options from the category_items array resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddProduct.setAdapter(adapter);

        // Set click listener for the add product button
        imageViewButtonPharmacyAdd.setOnClickListener(view -> addProduct());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Check if the selected image size is within the limit
                if (isImageSizeAcceptable(selectedImageUri)) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        imageBlob = new Image();
                        imageBlob.setBitmap(bitmap);
                        imageViewProductImage.setImageBitmap(bitmap);
                        profilePicturePath = getPathFromUri(selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select an image smaller than 2MB", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isImageSizeAcceptable(Uri uri) {
        try {
            // Open an input stream to read the image data
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // Get the size of the image file
                int fileSize = inputStream.available();
                // Check if the file size is within the limit
                return fileSize <= MyApplication.MAX_IMAGE_SIZE_BYTES;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    // Method to add a product
    private void addProduct() {
        // Get the input values from the EditText fields
        String name = editTextAddProductName.getText().toString().trim();
        String price = editTextAddProductPrice.getText().toString().trim();
        String description = editTextAddProductDescription.getText().toString().trim();
        String category = spinnerAddProduct.getSelectedItem().toString();
        String volume = editTextAddProductVolume.getText().toString().trim();
        String barcode = editTextAddProductBarcode.getText().toString().trim();
        String stock = spinnerAddProductStock.getSelectedItem().toString();

        // Validate input
        if (name.isEmpty() || price.isEmpty() || description.isEmpty() || category.isEmpty() || volume.isEmpty() || barcode.isEmpty()) {
            Toast.makeText(AddProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(PharmacyHomeActivity.currentPharmacy.getId(), name, Double.parseDouble(price), description, "productimage2", category);
        product.setVolume(volume);
        product.setBarcode(barcode);
        product.setStock(stock);
        product.setImageBlob(imageBlob);

        // Add the product to the local list
        PharmacyHomeActivity.products.add(product);

        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("pharmacyId", PharmacyHomeActivity.currentPharmacy.getId()); // Replace with the actual pharmacy ID
        params.put("name", name);
        params.put("price", price);
        params.put("description", description);
        params.put("category", category);
        params.put("volume", volume);
        params.put("barcode", barcode);
        params.put("stock", stock);
        params.put("image_tag", name);

        if (imageBlob!=null){
            imageBlob.setTag(name);

            uploadImageWithTags(imageBlob);
        }


        // Create a StringRequest to make a POST request with parameters
        String url = MyApplication.API_PORT + "add_product.php"; // Replace with the actual URL
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(AddProductActivity.this, response, Toast.LENGTH_SHORT).show();
                        // Clear the input fields after successful addition
                        clearInputFields();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        String errorMessage = "Error adding product: " + error.getMessage();
                        Log.e("AddProductActivity", errorMessage);
                        Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

    // Method to clear input fields
    private void clearInputFields() {
        editTextAddProductName.getText().clear();
        editTextAddProductPrice.getText().clear();
        editTextAddProductDescription.getText().clear();
        editTextAddProductVolume.getText().clear();
        editTextAddProductBarcode.getText().clear();
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
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

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void uploadImageWithTags(Image image) {
        // Get the Bitmap from the drawable resources
        Bitmap bitmap =image.getBitmap();

        // Convert Bitmap to Base64 string
        String imageString = bitmapToBase64(bitmap);

        // Define the image tag
        String imageTag = image.getTag();

        // API URL for uploading image with tags
        String URL = MyApplication.API_PORT + "upload_image.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(AddProductActivity.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors
                    Toast.makeText(AddProductActivity.this, "Error uploading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString); // Add the image string to the parameters
                params.put("image_tag", imageTag); // Add the image tag to the parameters
                // Log the parameters
                Log.d("UploadImageTestActivity", "Params: " + params.toString());
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
