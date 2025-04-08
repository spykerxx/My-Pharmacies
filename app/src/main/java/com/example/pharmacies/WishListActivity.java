package com.example.pharmacies;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WishlistProductsAdapter adapter;
    private List<Product> wishlistProducts;
    private ImageView wishlistBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewWishList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        wishlistProducts = new ArrayList<>();
        adapter = new WishlistProductsAdapter(this, wishlistProducts);
        recyclerView.setAdapter(adapter);

        wishlistBack= findViewById(R.id.imageViewWishListBack);
        wishlistBack.setOnClickListener(view -> finish());

        // Fetch wishlist for the current user
        fetchWishlist(HomeActivity.currentUser.getUsername()); // Replace "username" with the actual username
    }

    private void fetchWishlist(String username) {
        String url = MyApplication.API_PORT+"wishlist.php";

        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        // Create a new request with POST method and parameters
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WishListActivity", "Response: " + response); // Log the response
                        try {
                            JSONArray jsonArray = new JSONArray(response); // Parse response as a JSON array
                            wishlistProducts.clear(); // Clear existing wishlist products
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject wishlistItem = jsonArray.getJSONObject(i);
                                int productId = wishlistItem.getInt("productId");

                                // Find the product with the matching productId in HomeActivity.products
                                for (Product product : HomeActivity.products) {
                                    if (product.getProductId() == productId) {
                                        wishlistProducts.add(product); // Add the product to the wishlist
                                        break; // Exit the loop once the product is found
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged(); // Notify the adapter of the data change
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WishListActivity", "Error fetching wishlist: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }


}
