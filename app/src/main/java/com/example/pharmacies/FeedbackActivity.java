package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();
        ImageView imageViewBack= findViewById(R.id.imageViewFeedbackBack);
        imageViewBack.setOnClickListener(view -> finish());
        ImageView imageViewSave= findViewById(R.id.imageViewFeedBackSave);
        imageViewSave.setOnClickListener(view -> {
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

}
