package com.example.pharmacies;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PharmacyOrdersFragment extends Fragment {

    PharmacyOrdersAdapter ordersAdapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPharmacyOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    ordersAdapter = new PharmacyOrdersAdapter(getContext(), PharmacyHomeActivity.orders);
                    recyclerView.setAdapter(ordersAdapter);
                }
                , 1000);

        // No need to fetch orders here as it's done inside the adapter constructor

        return view;
    }
}
