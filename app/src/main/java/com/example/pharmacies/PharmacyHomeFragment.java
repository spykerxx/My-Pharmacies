package com.example.pharmacies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class PharmacyHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PharmacyProductsAdapter productsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPharmacyHome);
        ImageView imageViewAddProduct = view.findViewById(R.id.imageViewAddProduct);
        imageViewAddProduct.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AddProductActivity.class)));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        // Update the RecyclerView with the fetched products

        // Execute fetchProductsByPharmacyId method after 1.5 seconds

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    productsAdapter = new PharmacyProductsAdapter(getActivity(), PharmacyHomeActivity.products);
                    recyclerView.setAdapter(productsAdapter);
                }
                , 700);

        return view;
    }


}

