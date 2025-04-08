package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ConsultationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        getSupportActionBar().hide();

        ImageView imageViewBack = findViewById(R.id.imageViewConultationBack);
        imageViewBack.setOnClickListener(view -> finish());
        ImageView imageViewChatAI = findViewById(R.id.imageViewChatAI);
        imageViewChatAI.setOnClickListener(view -> Toast.makeText(this, "Not available now!", Toast.LENGTH_SHORT).show());

        ImageView imageViewChatDoctor = findViewById(R.id.imageViewChatDoctor);
        imageViewChatDoctor.setOnClickListener(view -> startActivity(new Intent(ConsultationActivity.this, CustomerChatActivity.class)));

    }
}
