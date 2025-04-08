package com.example.pharmacies;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProductsActivity extends AppCompatActivity implements HomeProductsAdapter.OnItemClickListener {


    public static ArrayList<Product> products;
    private HomeProductsAdapter productsAdapter;
    private RecyclerView recyclerView;
    private ImageView imageViewCart, imageViewRecentView, imageViewSearchBarcode;
    private EditText searchBar;
    private Spinner spinnerProductsFilter;
    private boolean compareRunning=false;
    private boolean recentView= false;
    private String compareProduct="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        ImageView imageViewProductsBack= findViewById(R.id.imageViewProductsBack);
        imageViewProductsBack.setOnClickListener(view -> finish());
        compareRunning = getIntent().getBooleanExtra("compareRunning", false);
        compareProduct = getIntent().getStringExtra("compareProduct");
        String category = getIntent().getStringExtra("category");
        String PharmacyId = getIntent().getStringExtra("pharmacyId");

        recentView= getIntent().getBooleanExtra("recentView", false);


        imageViewSearchBarcode= findViewById(R.id.imageViewSearchBarcode);
        imageViewSearchBarcode.setOnClickListener(view -> startActivity(new Intent(ProductsActivity.this, BarcodeActivity.class)));

        imageViewRecentView= findViewById(R.id.imageViewProductsRecentView);
        spinnerProductsFilter = findViewById(R.id.spinnerProductsFilter);

        spinnerProductsFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Call the fetchProducts method with the selected item and PharmacyId
                if (PharmacyId!=null){
                fetchProducts(selectedItem, PharmacyId);
                }
                else{
                    fetchProducts(selectedItem, "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (PharmacyId!=null){
                    fetchProducts(category, PharmacyId);
                }

                else{
                    fetchProducts(category, "");
                }

            }
        });


// Get the array of items from resources
        String[] categoryItems = getResources().getStringArray(R.array.filter_items);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryItems);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinnerProductsFilter.setAdapter(adapter);

        if (category != null && !category.isEmpty()) {
            ArrayAdapter<String> adapter2 = (ArrayAdapter<String>) spinnerProductsFilter.getAdapter();
            if (adapter2 != null) {
                int position = adapter2.getPosition(category);
                spinnerProductsFilter.setSelection(position);
                   }
        }


        recyclerView= findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set GridLayoutManager with 2 columns
        recyclerView.setHasFixedSize(true);



        if (category != null) {
            fetchProducts(category, "");
        }

        if (PharmacyId != null) {
            fetchProducts("All", PharmacyId);
        }

        else {
            fetchProducts("All", "");
        }



        imageViewCart= findViewById(R.id.imageViewProductsGoToCart);
        imageViewCart.setOnClickListener(view -> {
            if (HomeActivity.currentUser.getUsername().equals("guest")){
                Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                return;
            }

            startActivity(new Intent(ProductsActivity.this, ShoppingCartActivity.class));
        });

        searchBar= findViewById(R.id.editTextTextProductsSearch);

        String search = getIntent().getStringExtra("search");
        if (search!=null){
        searchBar.setText(search);
        searchBar.requestFocus();
        }


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but required for implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Apply the filter to the adapter
                productsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used, but required for implementation
            }
        });

        if (recentView){
            runRecentView();
        }
    }
    private void fetchProducts(String productCategory, String pharmacy_id) {
        String url = MyApplication.API_PORT+"products.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    products = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject eventObject = response.getJSONObject(i);
                            String productId = String.valueOf(eventObject.getInt("productId"));
                            String pharmacyId = eventObject.getString("pharmacyId");
                            String name = eventObject.getString("name");
                            double price = eventObject.getDouble("price");
                            String image = eventObject.getString("image");
                            String category = eventObject.getString("category");
                            String description = eventObject.getString("description");
                            String volume= eventObject.getString("volume");
                            String image_tag= eventObject.getString("image_tag");
                            String stock= eventObject.getString("stock");

                            Product product = new Product(pharmacyId, name, price, description, image, category);
                            product.setProductId(Integer.parseInt(productId));
                            product.setStock(stock);
                            Image imageBlob= HomeActivity.findImageByTag(image_tag);
                            if (imageBlob!=null){
                                product.setImageBlob(imageBlob);
                            }
                            product.setVolume(volume);


                            // Only add products of the selected category, or fetch all events if category is "null"
                            if ("All".equalsIgnoreCase(productCategory) || productCategory.equalsIgnoreCase(category) || productCategory.isEmpty()) {
                                if ("".equalsIgnoreCase(pharmacy_id) || pharmacyId.equalsIgnoreCase(pharmacy_id) || pharmacy_id.isEmpty()) {
                                    products.add(product);
                                }

                            }

                            // Only add products of the selected category, or fetch all events if category is "null"


                        }

                        productsAdapter = new HomeProductsAdapter(ProductsActivity.this, products);
                        productsAdapter.setOnItemClickListener(ProductsActivity.this); // Set click listener
                        recyclerView.setAdapter(productsAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ProductsActivity", "Error fetching products: " + error.getMessage()));

        Volley.newRequestQueue(this).add(request);
    }
    @Override
    public void onItemClick(Product product) {
        // Handle item click
        String productName = product.getName();

        if (compareRunning){
            // Use switch statement
            switch (compareProduct) {
                case "left":
                    // Handle left comparison
                    CompareProductsActivity.productLeft = getProductByName(productName);
                    // Do something with leftProduct
                    break;
                case "right":
                    // Handle right comparison
                    CompareProductsActivity.productRight = getProductByName(productName);
                    // Do something with rightProduct
                    break;
                case "mid":
                    // Handle mid comparison
                    CompareProductsActivity.productMid = getProductByName(productName);
                    // Do something with midProduct
                    break;
                default:
                    // Handle default case
            }

            CompareProductsActivity.update();
            finish();

        }

        else {
        // Start EventActivity and pass the event name in the bundle
        Intent intent = new Intent(ProductsActivity.this, ProductActivity.class);
        intent.putExtra("productName", productName);
        //intent.putExtra("choosenEventId", event.getId());
        startActivity(intent);
        }
    }

    private Product getProductByName(String productName) {
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    return product;
                }
            }
        }
        return null; // Product not found
    }

    private void runRecentView(){

        imageViewRecentView.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.INVISIBLE);
        spinnerProductsFilter.setVisibility(View.INVISIBLE);
        imageViewCart.setVisibility(View.INVISIBLE);
        imageViewSearchBarcode.setVisibility(View.INVISIBLE);

    }

}
