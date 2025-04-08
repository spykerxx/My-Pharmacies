package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PointsActivity extends AppCompatActivity {

    TextView textViewMyPoints;
    RecyclerView recyclerViewPoints;
    PointsAdapter adapter;
    private ImageView imageViewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        getSupportActionBar().hide();

        // Initialize views
        textViewMyPoints = findViewById(R.id.textViewMyPoints);
        recyclerViewPoints = findViewById(R.id.recyclerViewPoints);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPoints.setHasFixedSize(true);
        imageViewBack=findViewById(R.id.imageViewPointsback);
        imageViewBack.setOnClickListener(view -> finish());

        // Fetch orders from the server
        fetchOrdersAndSetupRecyclerView();
    }

    private void fetchOrdersAndSetupRecyclerView() {
        HomeActivity.fetchOrders(Integer.parseInt(HomeActivity.currentUser.getId()), this, new HomeActivity.OrdersCallback() {
            @Override
            public void onOrdersFetched(ArrayList<Order> orders) {

                // Create and set the adapter for the RecyclerView
                adapter = new PointsAdapter(PointsActivity.this, orders);
                recyclerViewPoints.setAdapter(adapter);
                textViewMyPoints.setText(""+HomeActivity.currentUser.getPoints());
            }
        });
    }
}
