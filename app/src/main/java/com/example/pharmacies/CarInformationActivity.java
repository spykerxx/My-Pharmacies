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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CarInformationActivity extends AppCompatActivity {

    private ImageView imageViewSaveCarInfo;
    private EditText editTextCarDriverLicense;
    private EditText editTextCarPicture;
    private EditText editTextCarPlate;
    private EditText editTextCarType;
    private EditText editTextCarVersion;
    private EditText editTextCarName;
    private ImageView imageViewCarInfoBack;
    private EditText editText;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);
        getSupportActionBar().hide();

        // Find views by their IDs
        imageViewSaveCarInfo = findViewById(R.id.imageViewSaveCarInfo);
        editTextCarDriverLicense = findViewById(R.id.editTextCarDriverLicense);
        editTextCarPicture = findViewById(R.id.editTextCarPicture);
        editTextCarPlate = findViewById(R.id.editTextCarPlate);
        editTextCarType = findViewById(R.id.editTextCarType);
        editTextCarVersion = findViewById(R.id.editTextCarVersion);
        editTextCarName = findViewById(R.id.editTextCarName);
        imageViewCarInfoBack = findViewById(R.id.imageViewCarInfoBack);
        imageViewCarInfoBack.setOnClickListener(view -> finish());

        imageViewSaveCarInfo.setOnClickListener(view -> updateOrInsertCarRecord(String.valueOf(DeliveryHomeActivity.delivery.getId())));


        editTextCarPicture.setOnClickListener(v -> {
            requestStoragePermission();
            editText= editTextCarPicture;
            openGallery();
        });

        editTextCarDriverLicense.setOnClickListener(v -> {
            requestStoragePermission();
            editText= editTextCarDriverLicense;
            openGallery();
        });

        fetchCarInfo(String.valueOf(DeliveryHomeActivity.delivery.getId()));


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
                 String profilePicturePath = getPathFromUri(selectedImageUri);
                    editText.setText(profilePicturePath);
                    editText=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = CarInformationActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CarInformationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
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

    private void updateOrInsertCarRecord(String deliveryId) {
        // Get car details from your fields or wherever you have them
        final String name = editTextCarName.getText().toString().trim();
        final String version = editTextCarVersion.getText().toString().trim();
        final String type = editTextCarType.getText().toString().trim();
        final String plate = editTextCarPlate.getText().toString().trim();
        final String carImage = editTextCarPicture.getText().toString().trim();
        final String plateImage = editTextCarDriverLicense.getText().toString().trim();

        // Create HTTP request to update or insert car record in the database
        String url = MyApplication.API_PORT + "update_car.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Check the response from the server
                    if (response.contains("saved!")) {
                        // Car record updated or inserted successfully
                        // You can perform any additional action here
                        Toast.makeText(CarInformationActivity.this, "Car saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error updating or inserting car record
                        Toast.makeText(CarInformationActivity.this, "Error: " + response, Toast.LENGTH_SHORT).show();
                        // You can perform any additional action here
                    }
                },
                error -> {
                    // Error occurred while updating or inserting car record
                    error.printStackTrace();
                    Toast.makeText(CarInformationActivity.this, "Error updating or inserting car record: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    // You can perform any additional action here
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (deliveryId, name, version, type, etc.)
                Map<String, String> params = new HashMap<>();
                params.put("delivery_id", deliveryId);
                params.put("name", name);
                params.put("version", version);
                params.put("type", type);
                params.put("plate", plate);
                params.put("car_image", carImage);
                params.put("plate_image", plateImage);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }


    private void fetchCarInfo(String deliveryId) {
        // Create HTTP request to fetch car information from the server
        String url = MyApplication.API_PORT + "get_car.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        // Parse the JSON response
                        JSONArray jsonArray = new JSONArray(response);

                        // Check if any cars were found
                        if(jsonArray.length() > 0) {
                            // Loop through each car object
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject carJson = jsonArray.getJSONObject(i);

                                // Extract car details from JSON
                                String name = carJson.getString("name");
                                String version = carJson.getString("version");
                                String type = carJson.getString("type");
                                String plate = carJson.getString("plate");
                                String carImage = carJson.getString("car_image");
                                String plateImage = carJson.getString("plate_image");

                                editTextCarName.setText(name);
                                editTextCarType.setText(type);
                                editTextCarVersion.setText(version);
                                editTextCarPicture.setText(carImage);
                                editTextCarPlate.setText(plate);
                                editTextCarDriverLicense.setText(plateImage);
                            }
                        } else {
                            // No cars found for the provided deliveryId
                            Toast.makeText(getApplicationContext(), "No cars found for the provided delivery ID", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Error parsing JSON response
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing car information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Error occurred while fetching car information
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error fetching car information: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                })
        {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (deliveryId)
                Map<String, String> params = new HashMap<>();
                params.put("delivery_id", deliveryId);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }



}
