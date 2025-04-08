package com.example.pharmacies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private EditText editTextTextLoginUsername, editTextTextLoginPassword;
    private ImageView imageViewEye;
    // Declare GoogleSignInClient as a member variable
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Inside onCreate method
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize EditText fields
        editTextTextLoginUsername = findViewById(R.id.editTextTextLoginUsername);
        editTextTextLoginPassword = findViewById(R.id.editTextTextLoginPassword);

        back= findViewById(R.id.imageViewLoginBack);
        back.setOnClickListener(view -> finish());

        // Set click listeners for login button and other actions
        ImageView imageViewLoginButtonSignIn = findViewById(R.id.imageViewLoginButtonSignIn);
        ImageView imageViewLoginAsGuest = findViewById(R.id.imageViewLoginAsGuest);
        ImageView imageViewLoginForgotPassword = findViewById(R.id.imageViewLoginForgotPassword);
        ImageView imageViewDontHaveAccont = findViewById(R.id.imageViewDontHaveAccont);

        imageViewEye= findViewById(R.id.imageViewLoginEye);
        imageViewEye.setOnClickListener(v -> {
            // Toggle password visibility
            int selectionStart = editTextTextLoginPassword.getSelectionStart();
            int selectionEnd = editTextTextLoginPassword.getSelectionEnd();

            if (editTextTextLoginPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                // Password is currently hidden, show it
                editTextTextLoginPassword.setTransformationMethod(null);
                imageViewEye.setImageResource(R.drawable.eyenocross);
            } else {
                // Password is currently shown, hide it
                editTextTextLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewEye.setImageResource(R.drawable.eye); // Change back to your original image resource

            }

            // Restore cursor position
            editTextTextLoginPassword.setSelection(selectionStart, selectionEnd);
        });


        imageViewLoginButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextTextLoginUsername.getText().toString().trim();
                String password = editTextTextLoginPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                performLogin(username, password);
            }
        });


        // Set click listeners for other actions
        imageViewLoginAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLoginAsGuest();
            }
        });

        imageViewLoginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
            }
        });

        imageViewDontHaveAccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                String email = account.getEmail();
                String displayName = account.getDisplayName();
                Toast.makeText(this, "Signed in as " + displayName + " (" + email + ")", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("Google Sign-In", "Sign-in failed: " + result.getStatus());
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void performLogin(final String username, final String password) {
        String LOGIN_URL = MyApplication.API_PORT + "login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    Log.d("Login Response", response); // Log the response received from the server
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (success) {
                            JSONObject userObject = jsonObject.getJSONObject("user");
                            String userId = userObject.getString("id"); // Parse user ID directly from user object
                            String email = userObject.getString("email");

                            Log.d("Login Response userid", userId); // Log the response received from the server

                            User currentUser = new User(userId, username, email);
                            Toast.makeText(LoginActivity.this, "Success: " + message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("userId", userId); // Pass the user ID with the intent
                            startActivity(intent);
                            finishAffinity();
                            // Proceed to the next activity or perform further actions
                        } else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void performLoginAsGuest() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("username", "guest");
        startActivity(intent);
        finishAffinity();
    }


}
