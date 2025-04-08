package com.example.pharmacies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PharmacyProfileActivity extends AppCompatActivity {

    private EditText editTextPharmacyUsernameProfile;
    private EditText editTextPharmacyEmailProfile;
    private EditText editTextPharmacyPhoneProfile, editTextPharmacyAddressProfile;
    private ImageView imageViewButtonPharmacyUpdate, delete;
    private ImageView imageViewPharmacyProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);
        getSupportActionBar().hide();

        ImageView back= findViewById(R.id.imageViewPharmacyProfileBack);
        back.setOnClickListener(view -> finish());

        editTextPharmacyUsernameProfile = findViewById(R.id.editTextPharmacyUsernameProfile);
        editTextPharmacyEmailProfile = findViewById(R.id.editTextPharmacyEmailProfile);
        editTextPharmacyPhoneProfile = findViewById(R.id.editTextPharmacyPhoneProfile);
        imageViewButtonPharmacyUpdate = findViewById(R.id.imageViewButtonPharmacyUpdate);
        imageViewPharmacyProfileImage = findViewById(R.id.imageViewPharmacyProfileImage);


        editTextPharmacyAddressProfile= findViewById(R.id.editTextPharmacyAddressProfile);
        delete= findViewById(R.id.imageViewButtonPharmacyDeleteMyAccount);
        delete.setOnClickListener(view -> {
            // Show a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(PharmacyProfileActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete your pharmacy account? This action cannot be undone.");

            // Add buttons for confirmation
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User confirmed, proceed with deleting the pharmacy
                    deletePharmacy();
                }
            });

            // Add button to cancel the operation
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User canceled the operation, do nothing
                    dialogInterface.dismiss();
                }
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });



        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    // Set values to EditText fields
                    editTextPharmacyUsernameProfile.setText(PharmacyHomeActivity.currentPharmacy.getName());
                    editTextPharmacyEmailProfile.setText(PharmacyHomeActivity.currentPharmacy.getEmail());
                    editTextPharmacyPhoneProfile.setText(PharmacyHomeActivity.currentPharmacy.getImage());
                    editTextPharmacyAddressProfile.setText(PharmacyHomeActivity.currentPharmacy.getAddress());

                   // fetchImageByTag(PharmacyHomeActivity.currentPharmacy.getImageBlob().getTag());

                    if (PharmacyHomeActivity.currentPharmacy.getImageBlob()!=null){
                        imageViewPharmacyProfileImage.setImageBitmap(PharmacyHomeActivity.currentPharmacy.getImageBlob().getBitmap());
                    }


                }
                , 700);

        imageViewButtonPharmacyUpdate.setOnClickListener(v -> updatePharmacyDetails());

    }

    private void updatePharmacyDetails() {
        // Get updated values
        String name = editTextPharmacyUsernameProfile.getText().toString().trim();
        String email = editTextPharmacyEmailProfile.getText().toString().trim();
        String phone = editTextPharmacyPhoneProfile.getText().toString().trim();
        String address = editTextPharmacyAddressProfile.getText().toString().trim();
        String image= editTextPharmacyPhoneProfile.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", PharmacyHomeActivity.currentPharmacy.getId());
        params.put("name", name);
        params.put("email", email);
        params.put("phone", image);
        params.put("address", address);

        // API URL
        String url = MyApplication.API_PORT + "update_pharmacy.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    handleUpdateResponse(response);
                    PharmacyHomeActivity.currentPharmacy.setEmail(email);
                    PharmacyHomeActivity.currentPharmacy.setAddress(address);
                    PharmacyHomeActivity.currentPharmacy.setPhone(phone);
                    PharmacyHomeActivity.currentPharmacy.setName(name);
                    PharmacyHomeActivity.currentPharmacy.setImage(image);
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error updating pharmacy details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    // Handle response from API call
    private void handleUpdateResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");
            if (success) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                // Optionally update UI or take any other action upon successful update
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }


    private void deletePharmacy() {
        // Get the current pharmacy ID
        String pharmacyId = PharmacyHomeActivity.currentPharmacy.getId();

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", pharmacyId);

        // API URL for deleting pharmacy
        String url = MyApplication.API_PORT + "delete_pharmacy.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        handleDeleteResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(PharmacyProfileActivity.this, "Error deleting pharmacy: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    // Method to handle delete pharmacy response
    private void handleDeleteResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            String message = jsonObject.getString("message");

            if (success) {
                // Pharmacy deleted successfully
                Toast.makeText(PharmacyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PharmacyProfileActivity.this, PharmacyLoginActivity.class));
                finishAffinity();
                // Navigate back to previous screen or perform any other action
            } else {
                // Failed to delete pharmacy
                Toast.makeText(PharmacyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(PharmacyProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchImageByTag(String imageTag) {
        // API URL for fetching image by tag
        String URL = MyApplication.API_PORT + "get_image.php";

        // Create parameters for the POST request
        Map<String, String> params = new HashMap<>();
        params.put("image_tag", imageTag);
        // Add logging to display the parameters
        Log.d("YourActivity", "Image Tag: " + imageTag);


        // Make a POST request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON response
                        try {
                            // Check if the response contains an image
                                   // Extract image data from the JSON object

                                String imageBase64 = response.getString("image_blob");

                                // Convert Base64 string to Bitmap
                                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                // Create an Image object
                                Image image = new Image(bitmap, imageTag);

                                // Process the fetched image (e.g., display it)
                                processFetchedImage(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PharmacyProfileActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("YourActivity", "Error fetching image by tag: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Method to process the fetched image (e.g., display it)
    private void processFetchedImage(Image image) {
        if (image!=null){
        imageViewPharmacyProfileImage.setImageBitmap(image.getBitmap());
        }
    }



}
