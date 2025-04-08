package com.example.pharmacies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextSignupUsername, editTextSignupEmail, editTextSignupPassword, editTextSignupConfirmPassword;
    private ImageView imageViewButtonSignup, imageViewAlreadyHaveAccount, imageViewTerms, imageViewBack;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        // Initialize EditText fields
        editTextSignupUsername = findViewById(R.id.editTextSignupUsername);
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail); // Changed from phone to email
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextSignupConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);
        agree= findViewById(R.id.checkBoxSignup);

        // Initialize ImageView buttons
        imageViewButtonSignup = findViewById(R.id.imageViewButtonSignup);
        imageViewAlreadyHaveAccount = findViewById(R.id.imageViewAlreadyHaveAccount);
        imageViewTerms= findViewById(R.id.imageViewGoToTermsAndConditions);
        imageViewTerms.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });

        imageViewBack= findViewById(R.id.imageViewSignupBack);
        imageViewBack.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finishAffinity();
        });

        // Set onClickListeners for ImageView buttons
        imageViewButtonSignup.setOnClickListener(view -> signUp());

        imageViewAlreadyHaveAccount.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the sign-up activity
        });
    }

    private void signUp() {
        String username = editTextSignupUsername.getText().toString().trim();
        String email = editTextSignupEmail.getText().toString().trim(); // Changed from phone to email
        String password = editTextSignupPassword.getText().toString();
        String confirmPassword = editTextSignupConfirmPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the checkbox is not checked
        if (!agree.isChecked()) {
            Toast.makeText(SignUpActivity.this, "You have to agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform signup request
        String url = MyApplication.API_PORT + "signup.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("error")) {
                        String error = jsonResponse.getString("error");
                     //  Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else if (jsonResponse.has("message")) {
                        String message = jsonResponse.getString("message");
                       // Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                        if (jsonResponse.has("userId")) {
                            int userId = jsonResponse.getInt("userId");
                            Intent intent = new Intent(SignUpActivity.this, MedicalHistoryActivity.class);
                            intent.putExtra("user_id", userId);
                            startActivity(intent);
                            finish(); // Close the sign-up activity
                           // Toast.makeText(SignUpActivity.this, "User ID: " + userId, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // Unexpected response format
                        Toast.makeText(SignUpActivity.this, "Unexpected response format: " + response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email); // Changed from phone to email
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(SignUpActivity.this).add(request);
    }


    private void signUpWithGoogle() {
        // Implement signup with Google if needed
        Toast.makeText(this, "Sign up with Google is not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
