package com.example.pharmacies;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    public static Cart cart;
    public static  User currentUser;
    public static ArrayList<Product> products;
    public static  ArrayList<ChatMessage> messages;
    public static ArrayList<Image> imageList= new ArrayList<>();
    public static Order activeOrder;
    // Define a Handler
    private Handler handler = new Handler();
    public static ArrayList<Pharmacy> pharmacies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        downloadImages();

        messages = new ArrayList<>();

        String username = getIntent().getStringExtra("username");
        String user_id = getIntent().getStringExtra("userId");


        if (!username.equals("guest")){
        getUserDetails(username);
        initiateDelayedTasks(user_id);

        }
        else {
            currentUser= new User();
            currentUser.setId("0");
            currentUser.setUsername("guest");
            currentUser.setAddress(new Address());
            currentUser.setMedicalHistory(new MedicalHistory());
        }
        cart = new Cart(currentUser);

        products= new ArrayList<>();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    fetchPharmacies();
                    fetchProducts("All");
                }
                , 1000);


       // fetchOrders(Integer.parseInt(currentUser.getId()));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the default selected item as home fragment if savedInstanceState is null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // Manually set the selected item to the home fragment
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.action_services:
                    if (username.equals("guest")){
                    Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                    break;
                    }
                    selectedFragment = new ServicesFragment();
                    break;
                case R.id.action_category:
                    // Handle like icon click
                    selectedFragment = new CategoryFragment();
                    break;
                case R.id.action_home:
                    // Handle like icon click
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.action_notifications:
                    // Handle home icon click
                    if (username.equals("guest")){
                        Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    selectedFragment = new NotificationsFragment();
                    break;
                case R.id.action_profile:
                    if (username.equals("guest")){
                        Toast.makeText(this, "This is not available for guest users! Sign up please", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });
    }
    private void getUserDetails(final String username) {
        String USER_DETAILS_URL = MyApplication.API_PORT + "get_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_DETAILS_URL,
                response -> {
                    Log.d("User Details Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject userObject = jsonArray.getJSONObject(0);
                            String userId = String.valueOf(userObject.getInt("id"));
                            String email = userObject.getString("email");
                            int points = userObject.getInt("points");
                            // Extract other user details as needed

                            currentUser = new User(userId, username, email);
                            currentUser.setPoints(points);
                            // Proceed with the user details
                        } else {
                            // No user found
                            Log.e("User Details", "No user found for username: " + username);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  Toast.makeText(HomeActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    public static void fetchOrders(int user_id, Context context, OrdersCallback callback) {
        ArrayList<Order> orders = new ArrayList<>();
        currentUser.setPoints(0);
        String ORDERS_URL = MyApplication.API_PORT + "orders.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDERS_URL,
                response -> {
                    Log.d("Orders Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        // Check if there are orders available
                        if (jsonArray.length() > 0) {
                            // Iterate through each order
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderObject = jsonArray.getJSONObject(i);
                                // Extract order details
                                int orderNumber = orderObject.getInt("number");
                                String orderDate = orderObject.getString("order_date");
                                String orderTime = orderObject.getString("order_time");
                                double orderAmount = orderObject.getDouble("order_amount");
                                int orderPoints= orderObject.getInt("order_points");
                                currentUser.setPoints(currentUser.getPoints()+orderPoints);
                                String deliveryDate = orderObject.getString("delivery_date");
                                String deliveryTime = orderObject.getString("delivery_time");
                                String deliveryName = orderObject.getString("delivery_name");
                                String deliveryNumber = orderObject.getString("delivery_number");
                                String status= orderObject.getString("status");
                                // Create Order object or process as needed
                                Order order = new Order(orderDate, orderTime, orderAmount);
                                order.setNumber(orderNumber);
                                order.setDeliveryDate(deliveryDate);
                                order.setDeliveryTime(deliveryTime);
                                order.setDeliveryNumber(deliveryNumber);
                                order.setDeliveryName(deliveryName);
                                order.setOrderPoints(orderPoints);
                                order.setStatus(status);
                                order.setUser_id(user_id);
                                orders.add(order);
                                if (!order.getStatus().equals("phase4")) {
                                   activeOrder=order;
                                    }

                            }

                            if (activeOrder==null && orders.size()>0){
                                activeOrder= orders.get(orders.size() - 1);
                            }
                        } else {
                            // No orders found
                            Log.e("Orders", "no orders found! " + orders.size() + user_id);
                        }
                        // Invoke the callback with the fetched orders
                        callback.onOrdersFetched(orders);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        // Toast.makeText(context, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle Volley error
                    // Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user_id));
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);

    }


    private void fetchProducts(String productCategory) {
        String url = MyApplication.API_PORT+"products.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    products = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject eventObject = response.getJSONObject(i);
                            int productId =eventObject.getInt("productId");
                            String pharmacyId = eventObject.getString("pharmacyId");
                            String name = eventObject.getString("name");
                            double price = eventObject.getDouble("price");
                            String image = eventObject.getString("image");
                            String category = eventObject.getString("category");
                            String description = eventObject.getString("description");
                            String barcode= eventObject.getString("barcode");
                            String volume= eventObject.getString("volume");
                            String image_tag= eventObject.getString("image_tag");
                            String stock= eventObject.getString("stock");


                            Product product = new Product(pharmacyId, name, price, description, image, category);
                            product.setProductId(productId);
                            product.setBarcode(barcode);
                            product.setVolume(volume);
                            product.setStock(stock);
                            Image imageBlob= findImageByTag(image_tag);
                            if (imageBlob!=null){
                                product.setImageBlob(imageBlob);
                            }


                            // Only add products of the selected category, or fetch all events if category is "null"
                            if (productCategory.equals("All") || productCategory.equals(category)) {
                                products.add(product);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ProductsActivity", "Error fetching products: " + error.getMessage()));

        Volley.newRequestQueue(this).add(request);
    }

    public static void addToWishlist(Context context, int productId, int userId) {
        // Create a request to add the product to the wishlist

        String url = MyApplication.API_PORT + "add_to_wishlist.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                    } catch (JSONException e) {
                        // Error parsing JSON response
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                   // Toast.makeText(context, "Error adding product to wishlist: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (product_id and user_id)
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request);
    }

    // Define the OrdersCallback interface
    public interface OrdersCallback {
        void onOrdersFetched(ArrayList<Order> orders);
    }

    private void getAddressDetails(final String userId) {
        String ADDRESS_DETAILS_URL = MyApplication.API_PORT + "address.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDRESS_DETAILS_URL,
                response -> {
                    Log.d("Address Details Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject addressObject = jsonArray.getJSONObject(0);
                            String country = addressObject.getString("country");
                            String region = addressObject.getString("region");
                            String city = addressObject.getString("city");
                            String neighborhood = addressObject.getString("neighborhood");
                            String streetNo = addressObject.getString("streetNo");
                            String shortAddress = addressObject.getString("shortAddress");
                            String buildingNo = addressObject.getString("buildingNo");
                            String apartmentNo = addressObject.getString("apartmentNo");
                            String fullName = addressObject.getString("fullName");
                            String primaryPhone = addressObject.getString("primaryPhone");
                            String secondaryPhone = addressObject.getString("secondaryPhone");
                            String postalCode = addressObject.getString("postalCode");

                            Address address = new Address(country, region, city, neighborhood, streetNo, shortAddress, buildingNo, apartmentNo, fullName, primaryPhone, secondaryPhone);
                            address.setPostalCode(postalCode);
                            currentUser.setAddress(address);
                            // Proceed with the address details
                        } else {
                            // No address found
                            Log.e("Address Details", "No address found for userId: " + userId);
                            // Create a new Address object with blank strings
                            Address address = new Address("", "", "", "", "", "", "", "", "", "", "");
                            currentUser.setAddress(address);
                            // Proceed with the address details
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  Toast.makeText(HomeActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                   // Toast.makeText(HomeActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void getMedicalHistoryDetails(final String userId) {
        String MEDICAL_HISTORY_DETAILS_URL = MyApplication.API_PORT + "medical_record.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MEDICAL_HISTORY_DETAILS_URL,
                response -> {
                    Log.d("Medical History Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject medicalHistoryObject = jsonArray.getJSONObject(0);
                            String birthday = medicalHistoryObject.getString("birthday");
                            String gender = medicalHistoryObject.getString("gender");

                            String diseases = medicalHistoryObject.getString("diseases");
                            String medicines = medicalHistoryObject.getString("medicines");
                            String complicationMedicines = medicalHistoryObject.getString("complication_medicines");
                            String foodAllergies = medicalHistoryObject.getString("food_allergies");
                            String medicineAllergies = medicalHistoryObject.getString("medicine_allergies");

                            MedicalHistory medicalHistory = new MedicalHistory(birthday, gender);
                            medicalHistory.setDiseases(diseases);
                            medicalHistory.setMedicines(medicines);
                            medicalHistory.setComplicationMedicines(complicationMedicines);
                            medicalHistory.setFoodAllergies(foodAllergies);
                            medicalHistory.setMedicineAllergies(medicineAllergies);

                            currentUser.setMedicalHistory(medicalHistory);

                        } else {
                            // No medical history found
                            Log.e("Medical History", "No medical history found for userId: " + userId);
                            // Create a new MedicalHistory object with default values
                            MedicalHistory medicalHistory = new MedicalHistory("", "");
                            currentUser.setMedicalHistory(medicalHistory);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                     //   Toast.makeText(HomeActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                 //   Toast.makeText(HomeActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }




    // Method to initiate the tasks after a delay
    private void initiateDelayedTasks(String userId) {
        // Execute the tasks after a delay of 1 second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Execute getMedicalHistoryDetails
                getMedicalHistoryDetails(userId);

                // Execute getAddressDetails
                getAddressDetails(userId);

                fetchMessages(userId,getApplicationContext());

                // Call fetchOrders here
                fetchOrders(Integer.parseInt(currentUser.getId()), getApplicationContext(), orders -> {
                    // Handle the fetched orders here
                });
            }
        }, 1000); // Delay in milliseconds (1 second = 1000 milliseconds)
    }

    private void fetchPharmacies() {
        String url = MyApplication.API_PORT+"home_pharmacies.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    pharmacies = new ArrayList<>();
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



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("NotificationsActivity", "Error fetching notifications: " + error.getMessage()));

        Volley.newRequestQueue(this).add(request);
    }

    public static void fetchMessages(final String userId, Context context) {
        String MESSAGES_URL = MyApplication.API_PORT + "user_chat.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MESSAGES_URL,
                response -> {
                    Log.d("Messages Response", response); // Log the response received from the server
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            // Array to hold the messages

                            // Fetch and store each message in the list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject messageObject = jsonArray.getJSONObject(i);
                                int messageId = messageObject.getInt("id");
                                String text = messageObject.getString("text");
                                String pharmacyId = messageObject.getString("pharmacy_id");
                                String sender= messageObject.getString("sender");

                                ChatMessage message = new ChatMessage(text, currentUser.getId(), pharmacyId);
                                message.setSender(sender);
                                messages.add(message);
                            }

                            // Now you can use the 'messages' list as needed
                        } else {
                            // No messages found for the user
                            Log.e("Messages", "No messages found for userId: " + userId);
                            // Handle this case as needed
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Messages", "Volley error: " + error.getMessage());
                        // Handle Volley error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
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

    private void downloadImages() {
        // API URL for fetching images
        String URL = MyApplication.API_PORT + "images.php";

        // Make a GET request using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {


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
                        Toast.makeText(HomeActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Method to search for a pharmacy by ID
    public static Pharmacy searchPharmacy(String id) {
        // Iterate through the list of pharmacies
        for (Pharmacy pharmacy : pharmacies) {
            // Check if the current pharmacy's ID matches the target ID
            if (pharmacy.getId().equals(id)) {
                // Return the pharmacy if found
                return pharmacy;
            }
        }
        // Return null if pharmacy with the specified ID is not found
        return null;
    }

}
