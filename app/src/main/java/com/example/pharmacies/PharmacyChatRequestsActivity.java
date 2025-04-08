package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class PharmacyChatRequestsActivity extends AppCompatActivity {

    PharmacyChatOrdersAdapter ordersAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_chat_requests);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewPharmacyRequestsChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ImageView back= findViewById(R.id.imageViewConsBack2);
        back.setOnClickListener(view -> finish());


        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    ordersAdapter = new PharmacyChatOrdersAdapter(this, PharmacyHomeActivity.chatRequests);
                    recyclerView.setAdapter(ordersAdapter);
                    Log.d("ChatRequests", "Size: " + PharmacyHomeActivity.chatRequests.size());
                }
                , 1000);
    }


}
