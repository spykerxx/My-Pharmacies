package com.example.pharmacies;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomePharmaciesAdapter.OnItemClickListener {


    private ImageView imageViewCart, imageViewOffers, imageViewSkin, imageViewMomAndBaby;
    private RecyclerView recyclerViewOffers, recyclerViewPharmacies;
    private EditText searchBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize ImageViews
        imageViewCart = view.findViewById(R.id.imageViewHomeCart);
        imageViewOffers = view.findViewById(R.id.imageViewHomeOffers);
        imageViewSkin = view.findViewById(R.id.imageViewHomeSkin);
        imageViewMomAndBaby = view.findViewById(R.id.imageViewHomeMomAndBaby);
        recyclerViewOffers= view.findViewById(R.id.reyclerViewHomeOffers);
        recyclerViewOffers.setHasFixedSize(true);
        recyclerViewOffers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));




        ImageView imageViewSearchBarcode= view.findViewById(R.id.imageViewSearchBarcode2);
        imageViewSearchBarcode.setOnClickListener(v -> startActivity(new Intent(getActivity(), BarcodeActivity.class)));


        recyclerViewPharmacies= view.findViewById(R.id.reyclerViewHomePharmacies);
        recyclerViewPharmacies.setHasFixedSize(true);
        recyclerViewPharmacies.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.HORIZONTAL, false));

       // recyclerViewPharmacies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    fetchPharmacies();
                    fetchOffers();
                }
                , 1000);

        searchBar= view.findViewById(R.id.editTextTextHomeSearch);
        searchBar = view.findViewById(R.id.editTextTextHomeSearch);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                // Start the ProductsActivity here
                Intent intent = new Intent(getActivity(), ProductsActivity.class);
                String search= searchBar.getText().toString();
                intent.putExtra("search", search);
                startActivity(intent);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
            }
        });

        searchBar.setOnClickListener(v -> {
             Intent intent = new Intent(getActivity(), ProductsActivity.class);
             startActivity(intent);
        });

        // Set click listeners for ImageViews
        imageViewCart.setOnClickListener(v -> {

            if (HomeActivity.currentUser.getUsername().equals("guest")){
                Toast.makeText(getContext(), "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                return;
            }


            Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
            startActivity(intent);
        });

        imageViewOffers.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProductsActivity.class);
            intent.putExtra("category", "Hair Care");
            startActivity(intent);
        });

        imageViewSkin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProductsActivity.class);
            intent.putExtra("category", "Skin Care");
            startActivity(intent);
        });

        imageViewMomAndBaby.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProductsActivity.class);
            intent.putExtra("category", "Mom & Baby");
            startActivity(intent);
        });


        return view;
    }
    private void fetchOffers() {
        String url = MyApplication.API_PORT+"home_offers.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Offer> offers = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject notificationObject = response.getJSONObject(i);
                            String name = notificationObject.getString("name");
                            String image = notificationObject.getString("image");
                            String image_tag= notificationObject.getString("image_tag");
                            Image imageBlob= findImageByTag(image_tag);




                            Offer offer = new Offer(name, image);
                            if (imageBlob!=null){
                                offer.setImageBlob(imageBlob);
                            }
                            offers.add(offer);
                        }

                        HomeOffersAdapter adapter = new HomeOffersAdapter(getActivity(), offers);
                        recyclerViewOffers.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("NotificationsActivity", "Error fetching notifications: " + error.getMessage()));

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void fetchPharmacies() {
        String url = MyApplication.API_PORT+"home_pharmacies.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Pharmacy> pharmacies = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject notificationObject = response.getJSONObject(i);
                            String image = notificationObject.getString("image");
                            String id= notificationObject.getString("id");
                            String image_tag= notificationObject.getString("image_tag");



                            Pharmacy pharmacy = new Pharmacy(image);
                            pharmacy.setId(id);
                            Image imageBlob= findImageByTag(image_tag);
                            if (imageBlob!=null){
                                pharmacy.setImageBlob(imageBlob);
                            }

                            pharmacies.add(pharmacy);


                        }

                        HomePharmaciesAdapter adapter = new HomePharmaciesAdapter(requireContext(), pharmacies, this);


                        recyclerViewPharmacies.setAdapter(adapter);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("NotificationsActivity", "Error fetching notifications: " + error.getMessage()));

        Volley.newRequestQueue(getContext()).add(request);
    }


    @Override
    public void onItemClick(String pharmacyId) {
        // Start the ProductsActivity and pass the selected category as an extra in a bundle
        Intent intent = new Intent(requireContext(), ProductsActivity.class);
        intent.putExtra("pharmacyId", pharmacyId);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Pharmacy pharmacy) {
        // Start the ProductsActivity and pass the selected category as an extra in a bundle
        Intent intent = new Intent(requireContext(), ProductsActivity.class);
        intent.putExtra("pharmacyId", pharmacy.getId());
        startActivity(intent);
    }
    public static Image findImageByTag(String imageTag) {
        for (Image image : HomeActivity.imageList) {
            if (image.getTag().equals(imageTag)) {
                return image;
            }
        }
        // Return null if no image with the specified tag is found
        return null;
    }

}
