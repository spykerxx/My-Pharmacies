package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextverificationCode;
    private String randomCode;
    private int code;
    private String userEmail;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().hide();

        userType= getIntent().getStringExtra("userType");
        if (userType==null){
            userType="customer";
        }
        ImageView buttonResetPassword = findViewById(R.id.imageViewForgotPassButtonSend);
        ImageView buttonSubmit = findViewById(R.id.imageViewForgotPassButtonSubmit);

        editTextEmail= findViewById(R.id.editTextForgotPassEmail);
        editTextverificationCode= findViewById(R.id.editTextForgotPassCode);

        ImageView back= findViewById(R.id.imageViewPasswordForgotBack);
        back.setOnClickListener(view -> finish());

        ImageView resend= findViewById(R.id.imageViewForgotPassResend);


        ImageView signup= findViewById(R.id.imageViewForgotPassSignup);
        signup.setOnClickListener(view -> {
            if (userType.equals("pharmacy")){
                startActivity(new Intent(PasswordRecoveryActivity.this, PharmacySignupActivity.class));
                finish();
                return;
            }

           else if (userType.equals("delivery")){
                startActivity(new Intent(PasswordRecoveryActivity.this, DeliverySignupActivity.class));
                finish();
                return;
            }


            startActivity(new Intent(PasswordRecoveryActivity.this, SignUpActivity.class));
            finishAffinity();
        });


        editTextverificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove the listener to avoid infinite recursion
                editTextverificationCode.removeTextChangedListener(this);

                // Clear all previous formatting
                String text = s.toString().replaceAll("\\s", "");

                // Apply the formatting (add space between each character)
                StringBuilder formattedText = new StringBuilder();
                int length = text.length();
                for (int i = 0; i < length; i++) {
                    formattedText.append(text.charAt(i));
                    // Add a space after each character except for the last one
                    if (i < length - 1) {
                        formattedText.append("       ");
                    }
                }

                // Set the formatted text to the EditText
                editTextverificationCode.setText(formattedText.toString());
                // Move the cursor to the end of the text
                editTextverificationCode.setSelection(formattedText.length());

                // Restore the listener
                editTextverificationCode.addTextChangedListener(this);
            }
        });

        buttonSubmit.setOnClickListener(v -> {
            String enteredCode = editTextverificationCode.getText().toString().replaceAll("\\s", "");
            enteredCode = enteredCode.trim();
            if (enteredCode.isEmpty()) {
                editTextverificationCode.setError("Verification code is required");
                editTextverificationCode.requestFocus();
                return;
            }

            // Check if the entered code matches the generated code
            if (enteredCode.equals(String.valueOf(code))) {
                // Codes match, navigate to ResetPasswordActivity
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                intent.putExtra("email", userEmail); // Pass the email address in the intent bundle
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish(); // Optional, if you want to close the current activity
                // Display success message
                Toast.makeText(getApplicationContext(), "Verification code matched!", Toast.LENGTH_SHORT).show();
            } else {
                // Display error message
                Toast.makeText(getApplicationContext(), "Verification code does not match!", Toast.LENGTH_SHORT).show();
            }
        });


        buttonResetPassword.setOnClickListener(v -> {
            userEmail = editTextEmail.getText().toString().trim();
            if (userEmail.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }
            randomCode = generateRandomCode();

            // Send the verification code via email
            sendVerificationCode(userEmail, randomCode);
        });

        resend. setOnClickListener(v -> {
            userEmail = editTextEmail.getText().toString().trim();
            if (userEmail.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }
            randomCode = generateRandomCode();

            // Send the verification code via email
            sendVerificationCode(userEmail, randomCode);
        });
    }

    private String generateRandomCode() {
        Random random = new Random();
        code = random.nextInt(900000) + 100000; // Generate 6-digit random code
        return String.valueOf(code);
    }

    private void sendVerificationCode(String recipientEmail, String code) {
        new Thread(() -> {
            final String senderEmail = "my.ph4rmacies@gmail.com"; // Your Gmail email address
            final String appSpecificPassword = "usbk sdsx uhcx ueif"; // App-specific password

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, appSpecificPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Subject of Email");
                message.setText("Body of the Email: Your verification code is " + code);

                Transport.send(message);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Verification code sent successfully!", Toast.LENGTH_SHORT).show();

                });
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error sending verification code. Please try again later.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
