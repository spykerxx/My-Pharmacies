package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public class PharmacyConsRequestsActivity extends AppCompatActivity {

    PharmacyOrdersAdapter ordersAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_cons_requests);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewPharmacyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ImageView back= findViewById(R.id.imageViewConsBack);
        back.setOnClickListener(view -> finish());


        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    ordersAdapter = new PharmacyOrdersAdapter(this, PharmacyHomeActivity.orders);
                    recyclerView.setAdapter(ordersAdapter);
                }
                , 1000);
    }


}
