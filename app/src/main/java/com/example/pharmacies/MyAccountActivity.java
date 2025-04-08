package com.example.pharmacies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail,editTextMedicalHistory,
            editTextMedicationHistory, editTextCountry, editTextPostalCode,
            editTextBuildingHood;
    private TextView textViewCustomerInfo, textViewMedicalHistory, textViewDeliveryInfo;
    private ImageView imageViewProfilePicture, imageViewDeleteMyAccount, imageViewSaveMyAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().hide();

        // Find views by their IDs
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMedicalHistory = findViewById(R.id.editTextMedicalHistory);
        editTextMedicationHistory = findViewById(R.id.editTextMedicationHistory);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextPostalCode = findViewById(R.id.editTextPostalCode);
        editTextBuildingHood = findViewById(R.id.editTextBuildingHood);

        textViewCustomerInfo = findViewById(R.id.textViewCustomerInfo);
        textViewMedicalHistory = findViewById(R.id.textViewMedicalHistory);
        textViewDeliveryInfo = findViewById(R.id.textViewDeliveryInfo);
        imageViewProfilePicture= findViewById(R.id.imageViewMyAccountPic);

        imageViewDeleteMyAccount = findViewById(R.id.imageViewDeleteMyAccount);
        imageViewDeleteMyAccount.setOnClickListener(view -> {
            // Show a confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User confirmed, proceed with account deletion
                            deleteUser(HomeActivity.currentUser.getId());
                            Toast.makeText(MyAccountActivity.this, "User deleted!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MyAccountActivity.this, LoginActivity.class));
                            finishAffinity();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        imageViewSaveMyAccount = findViewById(R.id.imageViewSaveMyAccount);
        imageViewSaveMyAccount.setOnClickListener(view -> updateUserDetails());

        ImageView imageViewBack= findViewById(R.id.imageViewMyAccountBack);
        imageViewBack.setOnClickListener(view -> finish());

        editTextMedicalHistory.setOnClickListener(view -> startActivity(new Intent(MyAccountActivity.this, MedicalHistoryActivity.class)));
        editTextMedicationHistory.setOnClickListener(view -> startActivity(new Intent(MyAccountActivity.this, MedicalHistoryActivity.class)));

        editTextCountry.setOnClickListener(view -> {
            boolean myAccountRunning=true;
            Intent intent = new Intent(MyAccountActivity.this, UserAddressActivity.class);
            intent.putExtra("myAccountRunning", myAccountRunning);
            startActivity(intent);
            finish();
        });
        editTextBuildingHood.setOnClickListener(view -> {
            boolean myAccountRunning=true;
            Intent intent = new Intent(MyAccountActivity.this, UserAddressActivity.class);
            intent.putExtra("myAccountRunning", myAccountRunning);
            startActivity(intent);
            finish();
        });



        fillUserDetails();
    }

    private void fillUserDetails() {
        // Check if the current user is not null
        if (HomeActivity.currentUser != null) {
            // Fill the EditText fields with user details
            editTextUsername.setText(HomeActivity.currentUser.getUsername());
            editTextEmail.setText(HomeActivity.currentUser.getEmail());
            editTextMedicalHistory.setText("View Medical History");
            editTextMedicationHistory.setText("View Medication History");
            editTextCountry.setText(HomeActivity.currentUser.getAddress().getCountry());
            editTextPostalCode.setText(HomeActivity.currentUser.getAddress().getPostalCode());
            editTextBuildingHood.setText(HomeActivity.currentUser.getAddress().getRegion());
        }
    }

    private void updateUserDetails() {
        // Get user ID from HomeActivity.currentUser
        String userId = HomeActivity.currentUser.getId();

        // Get updated values
        String name = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Check if username or email is empty
        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Username and email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", userId);
        params.put("username", name);
        params.put("email", email);

        // API URL
        String url = MyApplication.API_PORT + "update_user.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the raw response
                        Log.d("RawResponse", response);

                        // Handle response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                // User details updated successfully
                                HomeActivity.currentUser.setUsername(name);
                                HomeActivity.currentUser.setEmail(email);
                                if (HomeActivity.currentUser.getAddress()!=null){
                                    HomeActivity.currentUser.getAddress().setPostalCode(editTextPostalCode.getText().toString().trim());
                                }

                                Toast.makeText(MyAccountActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                // Error updating user details
                                Toast.makeText(MyAccountActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Error parsing JSON response
                            Toast.makeText(MyAccountActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MyAccountActivity.this, "Error updating user details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void deleteUser(String userId) {
        // Check if userId is empty
        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", userId);

        // API URL for deleting user
        String url = MyApplication.API_PORT + "delete_user.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the raw response
                        Log.d("RawResponse", response);

                        // Handle response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                // User deleted successfully
                                Toast.makeText(MyAccountActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                // Error deleting user
                                Toast.makeText(MyAccountActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Error parsing JSON response
                            Toast.makeText(MyAccountActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MyAccountActivity.this, "Error deleting user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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


}
