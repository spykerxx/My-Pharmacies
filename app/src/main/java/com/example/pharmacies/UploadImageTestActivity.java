package com.example.pharmacies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadImageTestActivity extends AppCompatActivity {

    private ImageView imageViewPicture;
    ArrayList<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_test);

        imageViewPicture = findViewById(R.id.imageViewTestImage);
        ImageView apply = findViewById(R.id.imageViewApply);

        // Call the method to upload the image when 'apply' is clicked
        apply.setOnClickListener(view -> uploadImage());
        downloadImages();
    }

    private void uploadImage() {
        // Get the Bitmap from the drawable resources
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);

        // Convert Bitmap to Base64 string
        String imageString = bitmapToBase64(bitmap);

        // API URL for uploading image
        String URL = MyApplication.API_PORT + "test_upload_image.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(UploadImageTestActivity.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors
                    Toast.makeText(UploadImageTestActivity.this, "Error uploading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString); // Add the image string to the parameters
                // Log the parameters
                Log.d("UploadImageTestActivity", "Params: " + params.toString());
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

// Convert Bitmap to Base64 string



    // Convert Bitmap to Base64 string
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void downloadImage() {
        // API URL for downloading image
        String URL = MyApplication.API_PORT + "test_download_image.php";

        // Make a GET request using Volley
        ImageRequest imageRequest = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Set the downloaded image to the ImageView
                        imageViewPicture.setImageBitmap(response);
                    }
                },
                0, // Image width (0 = automatic scaling based on device density)
                0, // Image height (0 = automatic scaling based on device density)
                ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                Bitmap.Config.RGB_565, // Bitmap configuration
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(UploadImageTestActivity.this, "Error downloading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(imageRequest);
    }

    private void downloadImages() {
        // API URL for fetching images
        String URL = MyApplication.API_PORT + "images.php";

        // Make a GET request using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    // Process the JSON response
                   imageList = new ArrayList<>();
                    try {
                        // Iterate through the JSON array
                        for (int i = 0; i < response.length(); i++) {
                            // Get the current JSON object
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Extract image data from the JSON object
                            int id = jsonObject.getInt("id");
                            String imageBase64 = jsonObject.getString("image_blob");
                            String tag = jsonObject.getString("image_tag");

                            // Convert Base64 string to Bitmap
                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            // Create an Image object and add it to the list
                            Image image = new Image(bitmap, tag);
                            image.setId(id);
                            imageList.add(image);
                        }

                        // Check if the imageList is not empty
                        if (!imageList.isEmpty()) {
                            // Get the first image from the list
                            Image firstImage = imageList.get(0);

                            // Check if the bitmap is not null
                            if (firstImage.getBitmap() != null) {
                                // Set the bitmap to the ImageView
                                imageViewPicture.setImageBitmap(firstImage.getBitmap());
                            } else {
                                // Handle the case where bitmap is null
                                // You can display a placeholder image or show an error message
                            }
                        } else {
                            // Handle the case where imageList is empty
                            // You can display a placeholder image or show an error message
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadImageTestActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("YourActivity", "Error fetching images: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


}
