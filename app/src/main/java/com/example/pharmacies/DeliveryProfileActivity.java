package com.example.pharmacies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DeliveryProfileActivity extends AppCompatActivity {

    private EditText editTextPhoneDelivery;
    private EditText editTextUsernameDelivery;
    private ImageView imageViewProfileFeedbackDelivery;
    private ImageView imageViewProfileCarInformation;
    private ImageView imageViewProfileSupportDelivery;
    private ImageView imageViewSaveMyAccountDelivery;
    private ImageView imageViewDeleteMyAccountDelivery;
    private ImageView imageViewLogoutDelivery;
    private ImageView imageViewBackProfileDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_profile);
        getSupportActionBar().hide();

        // Find views by their IDs
        editTextPhoneDelivery = findViewById(R.id.editTextPhoneDelivery);
        editTextUsernameDelivery = findViewById(R.id.editTextUsernameDelivery);
        imageViewProfileFeedbackDelivery = findViewById(R.id.imageViewProfileFeedbackDelivery);
        imageViewProfileCarInformation = findViewById(R.id.imageViewProfileCarInformation);
        imageViewProfileSupportDelivery = findViewById(R.id.imageViewProfileSupportDelivery);
        imageViewSaveMyAccountDelivery = findViewById(R.id.imageViewSaveMyAccountDelivery);
        imageViewDeleteMyAccountDelivery = findViewById(R.id.imageViewDeleteMyAccountDelivery);
        imageViewLogoutDelivery = findViewById(R.id.imageViewLogoutDelivery);
        imageViewBackProfileDelivery = findViewById(R.id.imageViewBackProfileDelivery);

        imageViewProfileFeedbackDelivery.setOnClickListener(view -> startActivity(new Intent(DeliveryProfileActivity.this, FeedbackActivity.class)));
        imageViewProfileSupportDelivery.setOnClickListener(view -> startActivity(new Intent(DeliveryProfileActivity.this, SupportActivity.class)));
        imageViewBackProfileDelivery.setOnClickListener(view -> finish());
        imageViewLogoutDelivery.setOnClickListener(view -> {
            startActivity(new Intent(DeliveryProfileActivity.this, DeliveryLoginActivity.class));
            finishAffinity();
        });

        imageViewProfileCarInformation.setOnClickListener(view -> {
            startActivity(new Intent(DeliveryProfileActivity.this, CarInformationActivity.class));
        });

        imageViewSaveMyAccountDelivery.setOnClickListener(view -> updateDeliveryDetails());

        imageViewDeleteMyAccountDelivery.setOnClickListener(view -> {
            // Show confirmation dialog
            showConfirmationDialog();
        });

        editTextUsernameDelivery.setText(DeliveryHomeActivity.delivery.getUsername());
        editTextPhoneDelivery.setText(DeliveryHomeActivity.delivery.getPhone()+"");
    }



    // Method to show confirmation dialog
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button
                deleteDelivery(); // Call method to delete delivery
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, do nothing
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updateDeliveryDetails() {
        // Get updated values
        String name = editTextUsernameDelivery.getText().toString().trim();
        String phone = editTextPhoneDelivery.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(DeliveryHomeActivity.delivery.getId()));
        params.put("name", name);
        params.put("phone", phone);

        // API URL
        String url = MyApplication.API_PORT + "update_delivery.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    handleUpdateResponse(response);
                    DeliveryHomeActivity.delivery.setPhone(phone);
                    DeliveryHomeActivity.delivery.setUsername(name);
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error updating delivery details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void deleteDelivery() {
        // Get the current delivery ID
        String deliveryId = String.valueOf(DeliveryHomeActivity.delivery.getId());

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("id", deliveryId);

        // API URL for deleting delivery
        String url = MyApplication.API_PORT + "delete_delivery.php";

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
                        Toast.makeText(DeliveryProfileActivity.this, "Error deleting delivery: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Method to handle delete delivery response
    private void handleDeleteResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            String message = jsonObject.getString("message");

            if (success) {
                // Delivery deleted successfully
                Toast.makeText(DeliveryProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DeliveryProfileActivity.this, DeliveryLoginActivity.class));
                finishAffinity();
                // Perform any additional actions upon successful deletion
            } else {
                // Failed to delete delivery
                Toast.makeText(DeliveryProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(DeliveryProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }


}
