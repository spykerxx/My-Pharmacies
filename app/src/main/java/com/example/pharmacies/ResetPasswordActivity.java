package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmNewPassword;
    private String email;
    private String userType= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();
        userType= getIntent().getStringExtra("userType");

        editTextNewPassword= findViewById(R.id.editTextResetNewPassword);
        editTextConfirmNewPassword= findViewById(R.id.editTextResetConfirmPassword);
        ImageView buttonResetPassword = findViewById(R.id.imageViewResetPasswordSave);
        ImageView back= findViewById(R.id.imageViewPasswordRecoveryBack);
        back.setOnClickListener(view -> finish());

        // Retrieve email address from the bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
        }

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new password entered by the user
                String newPassword = editTextNewPassword.getText().toString().trim();
                String confirmedPassword = editTextConfirmNewPassword.getText().toString().trim();

                // Validate the new password
                if (newPassword.isEmpty()) {
                    editTextNewPassword.setError("Please enter a new password");
                    editTextNewPassword.requestFocus();
                    return;
                }

                // Validate the confirmed password
                if (!newPassword.equals(confirmedPassword)) {
                    editTextConfirmNewPassword.setError("Passwords do not match");
                    editTextConfirmNewPassword.requestFocus();
                    return;
                }

                // Send the reset password request to the server
                resetPassword(email, newPassword);
            }
        });

    }

    private void resetPassword(final String email, final String newPassword) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "";
        if (userType==null){
            userType="customer";
        }
        if (userType.equals("delivery")){
            url = MyApplication.API_PORT+"reset_password_delivery.php";

        }

       else if (userType.equals("pharmacy")){
            url = MyApplication.API_PORT+"reset_password_pharmacy.php";
        }

       else {url = MyApplication.API_PORT+"reset_password.php";}


        // Create the POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");
                        if (success) {
                            // Password reset successfully
                            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            // Failed to reset password
                            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ResetPasswordActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPasswordActivity.this, "Error sending request", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Add the parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPassword);
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}
