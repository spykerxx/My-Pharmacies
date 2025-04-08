package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

public class DeliverySignupActivity extends AppCompatActivity {

    private EditText editTextDeliveryEmail;
    private EditText editTextDeliveryUsername;
    private EditText editTextDeliveryFullName;
    private EditText editTextDeliveryConfirmPassword;
    private EditText editTextDeliveryPassword;
    private EditText editTextDeliveryPhone;
    private ImageView imageViewSignup;
    private ImageView imageViewAlreadyHaveAccount;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_signup);
        getSupportActionBar().hide();

        // Initialize EditText fields
        editTextDeliveryEmail = findViewById(R.id.editTextDeliveryEmail);
        editTextDeliveryUsername = findViewById(R.id.editTextDeliveryUsername);
        editTextDeliveryFullName = findViewById(R.id.editTextDeliveryFullName);
        editTextDeliveryConfirmPassword = findViewById(R.id.editTextDeliveryConfirmPassword);
        editTextDeliveryPassword = findViewById(R.id.editTextDeliveryPassword);
        editTextDeliveryPhone = findViewById(R.id.editTextDeliveryPhone);
        imageViewSignup = findViewById(R.id.imageViewButtonDeliverySignup);
        imageViewAlreadyHaveAccount = findViewById(R.id.imageViewDeliveryAlreadyHaveAccount);
        agree= findViewById(R.id.checkBoxSignup2);

        ImageView imageViewDeliverySignupBack= findViewById(R.id.imageViewDeliverySignupBack);
        imageViewDeliverySignupBack.setOnClickListener(view -> finish());

        // Set onClickListener for signup button
        imageViewSignup.setOnClickListener(view -> signUp());

        // Set onClickListener for already have account text
        imageViewAlreadyHaveAccount.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(DeliverySignupActivity.this, DeliveryLoginActivity.class);
            startActivity(intent);
            finish(); // Close the signup activity
        });

        ImageView imageViewTerms= findViewById(R.id.imageViewGoToTermsAndConditionsDelivery);
        imageViewTerms.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(DeliverySignupActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });
    }



    private void signUp() {
        String email = editTextDeliveryEmail.getText().toString().trim();
        String username = editTextDeliveryUsername.getText().toString().trim();
        String fullName = editTextDeliveryFullName.getText().toString().trim();
        String password = editTextDeliveryPassword.getText().toString();
        String confirmPassword = editTextDeliveryConfirmPassword.getText().toString();
        String phone = editTextDeliveryPhone.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(DeliverySignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(DeliverySignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the checkbox is not checked
        if (!agree.isChecked()) {
            Toast.makeText(DeliverySignupActivity.this, "You have to agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform signup request
        String url = MyApplication.API_PORT + "delivery_signup.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("error")) {
                        String error = jsonResponse.getString("error");
                        Toast.makeText(DeliverySignupActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else if (jsonResponse.has("message")) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(DeliverySignupActivity.this, message, Toast.LENGTH_SHORT).show();
                        // Navigate to login activity
                        Intent intent = new Intent(DeliverySignupActivity.this, DeliveryLoginActivity.class);
                        startActivity(intent);
                        finish(); // Close the signup activity
                    } else {
                        // Unexpected response format
                        Toast.makeText(DeliverySignupActivity.this, "Unexpected response format: " + response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DeliverySignupActivity.this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeliverySignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", username);
                params.put("fullName", fullName);
                params.put("password", password);
                params.put("phone", phone);
                return params;
            }
        };
        Volley.newRequestQueue(DeliverySignupActivity.this).add(request);
    }
}
