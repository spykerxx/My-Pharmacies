package com.example.pharmacies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    private ImageView imageViewServicesGiftCard, imageViewServicesConsultation, imageViewServicesCompare;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        imageViewServicesGiftCard = view.findViewById(R.id.imageViewServicesGiftCard);
        imageViewServicesConsultation = view.findViewById(R.id.imageViewServicesConsultation);
        imageViewServicesCompare = view.findViewById(R.id.imageViewServicesCompare);

        // Set click listeners for ImageViews
        imageViewServicesGiftCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GiftingActivity.class);
            startActivity(intent);
        });

        imageViewServicesConsultation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConsultationActivity.class);
            startActivity(intent);
        });

        imageViewServicesCompare.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CompareProductsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
