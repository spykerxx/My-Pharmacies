package com.example.pharmacies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AddOfferActivity extends AppCompatActivity {

    private String profilePicturePath = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewAddOfferImage;
    private Image imageBlob;

    EditText editTextOfferName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        getSupportActionBar().hide();

        ImageView imageViewAddOfferBack= findViewById(R.id.imageViewAddOfferBack);
        imageViewAddOfferBack.setOnClickListener(view -> finish());

        ImageView addOffer= findViewById(R.id.imageViewAddOfferAdd);
        addOffer.setOnClickListener(view -> addOffer());

        editTextOfferName= findViewById(R.id.editTextAddOfferName);

        imageViewAddOfferImage= findViewById(R.id.imageViewAddOfferImage);
        imageViewAddOfferImage.setOnClickListener(v -> {
            requestStoragePermission();
            openGallery();
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Accept all image types
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
                        imageViewAddOfferImage.setImageBitmap(bitmap);
                        profilePicturePath = getPathFromUri(selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select an image smaller than 3MB", Toast.LENGTH_SHORT).show();
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


    private void addOffer() {
        // Get the input values from the EditText fields
        String name = editTextOfferName.getText().toString().trim();
        String image = "offeritem1";

        // Validate input
        if (name.isEmpty() || image.isEmpty()) {
            Toast.makeText(AddOfferActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("pharmacy_id", PharmacyHomeActivity.currentPharmacy.getId()); // Replace with the actual pharmacy ID
        params.put("name", name);
        params.put("image", image);
        params.put("image_tag", name);

        if (imageBlob!=null){
            imageBlob.setTag(name);

            uploadImageWithTags(imageBlob);
        }

        // Create a StringRequest to make a POST request with parameters
        String url = MyApplication.API_PORT + "add_offer.php"; // Replace with the actual URL
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(AddOfferActivity.this, response, Toast.LENGTH_SHORT).show();
                        Offer offer= new Offer(name, image);
                        offer.setImageBlob(imageBlob);
                        PharmacyHomeActivity.offers.add(offer);
                        PharmacyOffersActivity.update();
                        // Clear the input fields after successful addition
                        clearInputFields();
                        finish();
                    }
                },
                error -> {
                    // Handle errors
                    String errorMessage = "Error adding offer: " + error.getMessage();
                    Log.e("AddOfferActivity", errorMessage);
                    Toast.makeText(AddOfferActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
        editTextOfferName.getText().clear();
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddOfferActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
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
                    Toast.makeText(AddOfferActivity.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors
                    Toast.makeText(AddOfferActivity.this, "Error uploading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
