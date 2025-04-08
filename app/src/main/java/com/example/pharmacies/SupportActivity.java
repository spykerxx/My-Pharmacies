package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class SupportActivity extends AppCompatActivity {

    ImageView imageViewSupportChat;
    ImageView imageViewSupportEmail;
    ImageView imageViewSupportBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().hide();

        // Find ImageView elements by their IDs
        imageViewSupportChat = findViewById(R.id.imageViewSupportChat);
        imageViewSupportEmail = findViewById(R.id.imageViewSupportEmail);
        imageViewSupportBack = findViewById(R.id.imageViewSupportBack);

        imageViewSupportBack.setOnClickListener(view -> finish());
        imageViewSupportChat.setOnClickListener(view -> Toast.makeText(this, "Not available yet!", Toast.LENGTH_SHORT).show());
        imageViewSupportEmail.setOnClickListener(view -> Toast.makeText(this, "Not available yet!", Toast.LENGTH_SHORT).show());

        // Now you can use these ImageView objects as needed.
    }
}
