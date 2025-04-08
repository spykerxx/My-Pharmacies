package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class OrdersHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back= findViewById(R.id.imageViewBackOrderHistory);
        back.setOnClickListener(view -> finish());

        // Fetch orders from the server
        fetchOrdersAndSetupRecyclerView();
    }

    private void fetchOrdersAndSetupRecyclerView() {
        HomeActivity.fetchOrders(Integer.parseInt(HomeActivity.currentUser.getId()), this, new HomeActivity.OrdersCallback() {
            @Override
            public void onOrdersFetched(ArrayList<Order> orders) {

                // Create and set the adapter for the RecyclerView
                adapter = new OrdersAdapter(OrdersHistoryActivity.this, orders);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
