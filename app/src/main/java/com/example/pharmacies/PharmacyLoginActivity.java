package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import java.util.HashMap;
import java.util.Map;

public class PharmacyLoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private ImageView imageViewSignIn;
    private ImageView imageViewForgotPassword;
    private ImageView imageViewEye;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_login);
        getSupportActionBar().hide();

        // Initialize EditText fields
        editTextUsername = findViewById(R.id.editTextPharmacyLoginUsername);
        editTextPassword = findViewById(R.id.editTextPharmacyLoginPassword);

        // Initialize ImageView buttons
        imageViewSignIn = findViewById(R.id.imageViewLoginPharmacyButtonSignIn);
        imageViewForgotPassword = findViewById(R.id.imageViewLoginForgotPassword);

        ImageView back= findViewById(R.id.imageViewLoginBackPharmacy);
        back.setOnClickListener(view -> finish());

        imageViewEye= findViewById(R.id.imageViewPharmacyLoginEye);
        imageViewEye.setOnClickListener(v -> {
            // Toggle password visibility
            int selectionStart = editTextPassword.getSelectionStart();
            int selectionEnd = editTextPassword.getSelectionEnd();

            if (editTextPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                // Password is currently hidden, show it
                editTextPassword.setTransformationMethod(null);
                imageViewEye.setImageResource(R.drawable.eyenocross);
            } else {
                // Password is currently shown, hide it
                editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewEye.setImageResource(R.drawable.eye);
            }

            // Restore cursor position
            editTextPassword.setSelection(selectionStart, selectionEnd);
        });

        // Set onClickListener for sign in button
        imageViewSignIn.setOnClickListener(view -> signIn());

        // Set onClickListener for forgot password button
        imageViewForgotPassword.setOnClickListener(view -> {
            // Handle forgot password action
            Intent intent = new Intent(PharmacyLoginActivity.this, PasswordRecoveryActivity.class);
            String userType = "pharmacy";
            intent.putExtra("userType", userType);
            startActivity(intent);

        });

        // Set onClickListener for forgot password button
        ImageView imageViewDonthaveaccount= findViewById(R.id.imageViewPharmacyDontHaveAccont);
        imageViewDonthaveaccount.setOnClickListener(view -> {
            startActivity(new Intent(PharmacyLoginActivity.this, PharmacySignupActivity.class));
            finish();
        });
    }

    private void signIn() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(PharmacyLoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform sign-in request
        String url = MyApplication.API_PORT + "pharmacy_login.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success")) {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            // Login successful
                            Toast.makeText(PharmacyLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // Navigate to home activity or any other activity
                            Intent intent = new Intent(PharmacyLoginActivity.this, PharmacyHomeActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // Login failed
                            Toast.makeText(PharmacyLoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Unexpected response format
                        Toast.makeText(PharmacyLoginActivity.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PharmacyLoginActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PharmacyLoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(PharmacyLoginActivity.this).add(request);
    }
}
