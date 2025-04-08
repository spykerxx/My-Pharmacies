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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PharmacySignupActivity extends AppCompatActivity {

    private EditText editTextPharmacyEmail;
    private EditText editTextPharmacyUsername;
    private EditText editTextPharmacyConfirmPassword;
    private EditText editTextPharmacyPassword;
    private EditText editTextPharmacyPhone;
    private EditText editTextPharmacyId, editTextPharmacyBranchNumber;
    private ImageView imageViewSignup;
    private ImageView imageViewAlreadyHaveAccount;
    private ImageView imageViewButtonPharmacyUploadLogo, back;
    private CheckBox agree;
    private Image imageBlob;

    private String profilePicturePath = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_signup);
        getSupportActionBar().hide();

        // Initialize EditText fields
        editTextPharmacyEmail = findViewById(R.id.editTextPharmacyEmail);
        editTextPharmacyUsername = findViewById(R.id.editTextPharmacyUsername);
        editTextPharmacyConfirmPassword = findViewById(R.id.editTextPharmacyConfirmPassword);
        editTextPharmacyPassword = findViewById(R.id.editTextPharmacyPassword);
        editTextPharmacyPhone = findViewById(R.id.editTextPharmacyPhone);
        imageViewSignup = findViewById(R.id.imageViewButtonPharmacySignup);
        imageViewAlreadyHaveAccount = findViewById(R.id.imageViewPharmacyAlreadyHaveAccount);
        editTextPharmacyId= findViewById(R.id.editTextPharmacyId);
        imageViewButtonPharmacyUploadLogo= findViewById(R.id.imageViewButtonPharmacyUploadLogo);
        editTextPharmacyBranchNumber= findViewById(R.id.editTextPharmacyBranchNumber);

        agree= findViewById(R.id.checkBoxSignupPharmacy);

        back= findViewById(R.id.imageViewPharmacySignupBack);
        back.setOnClickListener(view -> finish());



        // Set onClickListener for signup button
        imageViewSignup.setOnClickListener(view -> signUp());

        // Set onClickListener for already have account text
        imageViewAlreadyHaveAccount.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(PharmacySignupActivity.this, PharmacyLoginActivity.class);
            startActivity(intent);
            finish(); // Close the signup activity
        });

        imageViewButtonPharmacyUploadLogo.setOnClickListener(v -> {
            requestStoragePermission();
            openGallery();
        });

        ImageView imageViewTerms= findViewById(R.id.imageViewGoToTermsAndConditionsPharmacy);
        imageViewTerms.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(PharmacySignupActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
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
                        imageViewButtonPharmacyUploadLogo.setImageBitmap(bitmap);
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
        Cursor cursor = PharmacySignupActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private void signUp() {
        String id = editTextPharmacyId.getText().toString().trim();
        String email = editTextPharmacyEmail.getText().toString().trim();
        String name = editTextPharmacyUsername.getText().toString().trim();
        String password = editTextPharmacyPassword.getText().toString();
        String confirmPassword = editTextPharmacyConfirmPassword.getText().toString();
        String phone = editTextPharmacyPhone.getText().toString().trim();
        String image = editTextPharmacyBranchNumber.getText().toString().trim();

        if (email.isEmpty() || id.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() || imageBlob==null) {
            Toast.makeText(PharmacySignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(PharmacySignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the checkbox is not checked
        if (!agree.isChecked()) {
            Toast.makeText(PharmacySignupActivity.this, "You have to agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform signup request
        String url = MyApplication.API_PORT + "pharmacy_signup.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("error")) {
                        String error = jsonResponse.getString("error");
                        Toast.makeText(PharmacySignupActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else if (jsonResponse.has("message")) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(PharmacySignupActivity.this, message, Toast.LENGTH_SHORT).show();
                        // Navigate to login activity
                        Intent intent = new Intent(PharmacySignupActivity.this, PharmacyLoginActivity.class);
                        Intent intent2 = new Intent(PharmacySignupActivity.this, PharmacyPaymentActivity.class);
                        startActivity(intent);
                        startActivity(intent2);
                        finish(); // Close the signup activity
                    } else {
                        // Unexpected response format
                        Toast.makeText(PharmacySignupActivity.this, "Unexpected response format: " + response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PharmacySignupActivity.this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PharmacySignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);
                params.put("phone", phone);
                params.put("image", image); // Add image parameter if needed
                params.put("image_tag", name);

                if (imageBlob!=null){
                    imageBlob.setTag(name);

                    uploadImageWithTags(imageBlob);
                }
                return params;
            }
        };
        Volley.newRequestQueue(PharmacySignupActivity.this).add(request);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PharmacySignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
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
                    Toast.makeText(PharmacySignupActivity.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors
                    Toast.makeText(PharmacySignupActivity.this, "Error uploading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString); // Add the image string to the parameters
                params.put("image_tag", imageTag); // Add the image tag to the parameters
                // Log the parameters
                Log.d("PharmacySignupActivity", "Params: " + params.toString());
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
