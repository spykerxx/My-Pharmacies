package com.example.pharmacies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PharmacyHomeActivity extends AppCompatActivity {

    public static Pharmacy currentPharmacy;
    public static ArrayList<Product> products;
    public static ArrayList<Order> orders;
    public static ArrayList<Image> imageList= new ArrayList<>();
    public static ArrayList<String> chatRequests= new ArrayList<>();

    public static String user_id= "";
    public static ArrayList<ChatMessage> messages;
    public static ArrayList<Offer> offers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_home);
        getSupportActionBar().hide();

        downloadImages();

        String name = getIntent().getStringExtra("username");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    getUserDetails(name);
                }
                , 1000);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    fetchOffersByPharmacyId(this, currentPharmacy.getId());
                    fetchProductsByPharmacyId(this, currentPharmacy.getId());
                    fetchOrdersByPharmacyId(this, currentPharmacy.getId());

                    fetchAllMessages(this);
                }
                , 1500);




        // Find views by their meaningful IDs
        ImageView imageViewPharmacyNotification = findViewById(R.id.imageViewPharmacyNotification);
        ImageView imageViewConsRequest = findViewById(R.id.imageViewPharmacyHomeConsRequest);
        ImageView imageViewAddProduct = findViewById(R.id.imageViewPharmacyHomeAddProduct);
        ImageView imageViewModifyProduct = findViewById(R.id.imageViewPharmacyHomeModifyProduct);
        ImageView imageViewPharmacySettingsIcon = findViewById(R.id.imageViewPharmacySettingsIcon);
        ImageView imageViewPharmacyHomeOffers= findViewById(R.id.imageViewPharmacyHomeOffers);
        ImageView imageViewPharmacyHomeMyProducts= findViewById(R.id.imageViewPharmacyHomeMyProducts);

        imageViewPharmacyNotification.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacyNotificationsActivity.class));
        });

        imageViewConsRequest.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacyChatRequestsActivity.class));
        });

        imageViewAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, AddProductActivity.class));
        });

        imageViewPharmacySettingsIcon.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacySettingsActivity.class));
        });

        imageViewModifyProduct.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacySearchProductActivity.class));
        });

        imageViewPharmacyHomeOffers.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacyOffersActivity.class));
        });

        imageViewPharmacyHomeMyProducts.setOnClickListener(v -> {
            startActivity(new Intent(PharmacyHomeActivity.this, PharmacyProductsActivity.class));
        });


    }

    private void getUserDetails(final String username) {
        String USER_DETAILS_URL = MyApplication.API_PORT + "get_pharmacy.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_DETAILS_URL,
                response -> {
                    Log.d("User Details Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject userObject = jsonArray.getJSONObject(0);
                            String id = userObject.getString("id");
                            String name= userObject.getString("name");
                            String address= userObject.getString("address");
                            String email= userObject.getString("email");
                            String phone= userObject.getString("phone");
                            String image= userObject.getString("image");
                            String image_tag= userObject.getString("image_tag");



                            // Extract other user details as needed

                            currentPharmacy = new Pharmacy();
                            currentPharmacy.setId(id);
                            currentPharmacy.setName(name);
                            currentPharmacy.setPhone(phone);
                            currentPharmacy.setAddress(address);
                            currentPharmacy.setImage(image);
                            currentPharmacy.setEmail(email);
                            Image imageBlob= findImageByTag(image_tag);
                            if (imageBlob!=null){
                            currentPharmacy.setImageBlob(imageBlob);
                            }


                            // Proceed with the user details
                        } else {
                            // No user found
                            Log.e("User Details", "No user found for username: " + username);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PharmacyHomeActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Toast.makeText(PharmacyHomeActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pharmacy_name", username);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public static void fetchProductsByPharmacyId(Context context, String pharmacyId) {
        String url = MyApplication.API_PORT + "pharmacy_products.php";

        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("pharmacy_id", pharmacyId);

        // Create a new request with POST method and parameters
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PharmacyHomeActivity", "Response: " + response); // Log the response
                        try {
                            JSONArray jsonArray = new JSONArray(response); // Parse response as a JSON array
                            products = new ArrayList<>(); // Initialize the products list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productObject = jsonArray.getJSONObject(i);
                                String productId = String.valueOf(productObject.getInt("productId"));
                                String name = productObject.getString("name");
                                double price = productObject.getDouble("price");
                                String image = productObject.getString("image");
                                String category = productObject.getString("category");
                                String description = productObject.getString("description");
                                String volume= productObject.getString("volume");
                                String barcode= productObject.getString("barcode");
                                String stock= productObject.getString("stock");
                                String image_tag= productObject.getString("image_tag");

                                Product product = new Product(pharmacyId, name, price, description, image, category);
                                product.setProductId(Integer.parseInt(productId));
                                product.setVolume(volume);
                                product.setBarcode(barcode);
                                product.setStock(stock);
                                Image imageBlob= findImageByTag(image_tag);
                                if (imageBlob!=null){
                                    product.setImageBlob(imageBlob);
                                }
                                products.add(product); // Add the product to the products list
                            }

                            // Now you can use the 'products' list as needed

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ProductsActivity", "Error fetching products: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request);
    }


    public static void fetchOffersByPharmacyId(Context context, String pharmacyId) {
        String url = MyApplication.API_PORT + "pharmacy_offers.php";

        // Create a map to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("pharmacy_id", pharmacyId);

        // Create a new request with POST method and parameters
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PharmacyHomeActivity", "Response: " + response); // Log the response
                        try {
                            JSONArray jsonArray = new JSONArray(response); // Parse response as a JSON array
                           offers = new ArrayList<>(); // Initialize the offers list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject offerObject = jsonArray.getJSONObject(i);
                                int offerId = offerObject.getInt("id");
                                String name = offerObject.getString("name");
                                String image = offerObject.getString("image");
                                String image_tag= offerObject.getString("image_tag");
                              //  String imageBlob = offerObject.getString("image_blob");


                                // Decode the Base64 image string into a byte array
                                //byte[] imageBytes = Base64.decode(imageBlob, Base64.DEFAULT);

                                // Create a Bitmap from the byte array
                               // Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                Offer offer = new Offer(name, image);
                               // offer.setImageBitMap(bitmap);
                                offer.setId(offerId);
                                offer.setImageBlob(findImageByTag(image_tag));
                                offers.add(offer); // Add the offer to the offers list
                            }

                            // Now you can use the 'offers' list as needed

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OffersActivity", "Error fetching offers: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request);
    }



    public static void fetchOrdersByPharmacyId(Context context, String pharmacyId) {
        String URL = MyApplication.API_PORT + "pharmacy_orders.php";

        // Create an ArrayList to hold the fetched orders
        orders = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract order details
                            int orderNumber = Integer.parseInt(jsonObject.getString("number"));
                            double orderAmount = jsonObject.getDouble("order_amount");
                            String orderTime = jsonObject.getString("order_time");
                            String order_detail = jsonObject.getString("order_detail");

                            Order order = new Order();
                            order.setNumber(orderNumber);
                            order.setOrderAmount(orderAmount);
                            order.setOrderTime(orderTime);
                            order.setOrder_detail(order_detail);
                            order.setPharmacy(currentPharmacy);

                            // Add the order to the fetched orders list
                            orders.add(order);
                        }

                        // Update the adapter or perform any other actions with the fetched orders
                        // For example:
                        // ordersAdapter.setOrders(orders);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                },
                error -> {
                    error.printStackTrace();
                    // Handle Volley error
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pharmacy_id", pharmacyId);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }


    public static void fetchAllMessages(Context context) {
        String MESSAGES_URL = MyApplication.API_PORT + "user_chat_all.php"; // Update with the PHP file that fetches all messages
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MESSAGES_URL,
                response -> {
                    Log.d("Messages Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            // Array to hold the messages
                            messages = new ArrayList<>();

                            // HashSet to store unique user_ids
                            HashSet<String> uniqueUserIds = new HashSet<>();

                            // Fetch and store each message in the list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject messageObject = jsonArray.getJSONObject(i);
                                int messageId = messageObject.getInt("id");
                                String text = messageObject.getString("text");
                                String pharmacyId = messageObject.getString("pharmacy_id");
                                String sender = messageObject.getString("sender");
                                String user_id = String.valueOf(messageObject.getInt("user_id"));

                                // Check if the user_id is unique
                                if (!uniqueUserIds.contains(user_id)) {
                                    // Add the user_id to the uniqueUserIds set
                                    uniqueUserIds.add(user_id);

                                    // Add the user_id to the chatRequests list
                                    chatRequests.add(user_id);
                                }

                                //getUserDetailsById(user_id, context);

                                ChatMessage message = new ChatMessage(text, user_id, pharmacyId);
                                message.setSender(sender);
                                messages.add(message);
                            }

                            // Now you can use the 'messages' list as needed
                        } else {
                            // No messages found
                            Log.e("Messages", "No messages found");
                            // Handle this case as needed
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                },
                error -> {
                    Log.e("Messages", "Volley error: " + error.getMessage());
                    // Handle Volley error
                });

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }


    public static void deleteProduct(int productId) {
        // Iterate through the ArrayList to find the product with the specified ID
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getProductId()==productId) {
                // Remove the product from the ArrayList
                products.remove(i);
                // Break the loop as we found and removed the product
                break;
            }
        }
    }

    private void downloadImages() {
        // API URL for fetching images
        String URL = MyApplication.API_PORT + "images.php";

        // Make a GET request using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    // Process the JSON response
                    try {
                        // Iterate through the JSON array
                        for (int i = 0; i < response.length(); i++) {
                            // Get the current JSON object
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Extract image data from the JSON object
                            int id = jsonObject.getInt("id");
                            String imageBase64 = jsonObject.getString("image_blob");
                            String tag = jsonObject.getString("image_tag");

                            // Convert Base64 string to Bitmap
                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            // Create an Image object and add it to the list
                            Image image = new Image(bitmap, tag);
                            image.setId(id);
                            imageList.add(image);
                        }

                        // Check if the imageList is not empty
                        if (!imageList.isEmpty()) {
                            // Get the first image from the list
                            Image firstImage = imageList.get(0);

                            // Check if the bitmap is not null

                        } else {
                            // Handle the case where imageList is empty
                            // You can display a placeholder image or show an error message
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PharmacyHomeActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("YourActivity", "Error fetching images: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public static Image findImageByTag(String imageTag) {
        for (Image image : imageList) {
            if (image.getTag().equals(imageTag)) {
                return image;
            }
        }
        // Return null if no image with the specified tag is found
        return null;
    }


}
