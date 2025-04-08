package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class PharmacyOffersActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static HomeOffersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_offers);
        getSupportActionBar().hide();
        recyclerView= findViewById(R.id.recyclerViewPharmacyOffers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ImageView back= findViewById(R.id.imageViewPharmacyBack);
        back.setOnClickListener(view -> finish());

        ImageView addNewOffer= findViewById(R.id.imageViewPharmacyAddNewOffer);
        adapter= new HomeOffersAdapter(this, PharmacyHomeActivity.offers);
        recyclerView.setAdapter(adapter);

        addNewOffer.setOnClickListener(view -> startActivity(new Intent(PharmacyOffersActivity.this, AddOfferActivity.class)));


    }

    public static void update(){
        adapter= new HomeOffersAdapter(recyclerView.getContext(), PharmacyHomeActivity.offers);
        recyclerView.setAdapter(adapter);
    }
}
