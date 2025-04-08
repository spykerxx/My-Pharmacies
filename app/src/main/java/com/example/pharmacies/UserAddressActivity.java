package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserAddressActivity extends AppCompatActivity {

    private EditText editTextStreetName;
    private EditText editTextShortAddress;
    private EditText editTextBuildingNo;
    private EditText editTextApartmentNo;
    private EditText editTextFullName;
    private EditText editTextPrimaryPhoneNumber;
    private EditText editTextBackupPhoneNumber;

    private Spinner spinnerCountry;
    private Spinner spinnerCity;
    private EditText editTextRegion;
    private EditText editTextNeighborhood;
    private ImageView imageViewSave;
    boolean checkoutRunning = false, myAccountRunning = false;
    String[] countries = {
            "Afghanistan",
            "Bahrain",
            "Egypt",
            "Iran",
            "Iraq",
            "Jordan",
            "Kuwait",
            "Lebanon",
            "Oman",
            "Palestine",
            "Qatar",
            "Saudi Arabia",
            "Syria",
            "United Arab Emirates",
            "United States"
    };

    // Cities arrays for each country
    String[][] cities = {
            // Afghanistan
            {"Kabul", "Kandahar", "Herat", "Mazar-i-Sharif"},

            // Bahrain
            {"Manama", "Muharraq", "Riffa", "Isa Town"},

            // Egypt
            {"Cairo", "Alexandria", "Giza", "Shubra El-Kheima"},

            // Iran
            {"Tehran", "Mashhad", "Isfahan", "Tabriz"},

            // Iraq
            {"Baghdad", "Basra", "Mosul", "Erbil"},

            // Jordan
            {"Amman", "Zarqa", "Irbid", "Salt"},

            // Kuwait
            {"Kuwait City", "Farwaniya", "Hawalli", "Mubarak Al-Kabeer"},

            // Lebanon
            {"Beirut", "Tripoli", "Sidon", "Tyre"},

            // Oman
            {"Muscat", "Salalah", "Sohar", "Nizwa"},

            // Palestine
            {"Gaza", "Jerusalem", "Hebron", "Nablus"},

            // Qatar
            {"Doha", "Al Wakrah", "Al Khor", "Umm Salal Muhammad"},

            // Saudi Arabia
            {"Riyadh", "Jeddah", "Mecca", "Medina", "Dammam", "Tabuk", "Taif", "Buraidah"},

            // Syria
            {"Damascus", "Aleppo", "Homs", "Hama"},

            // United Arab Emirates
            {"Dubai", "Abu Dhabi", "Sharjah", "Al Ain"},

            // United States
            {"New York", "Los Angeles", "Chicago", "Houston"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        getSupportActionBar().hide();
        checkoutRunning = getIntent().getBooleanExtra("checkoutRunning", false);
        myAccountRunning = getIntent().getBooleanExtra("myAccountRunning", false);
        // Initialize EditTexts
        editTextStreetName = findViewById(R.id.editTextStreetName);
        editTextShortAddress = findViewById(R.id.editTextShortAddress);
        editTextBuildingNo = findViewById(R.id.editTextBuildingNo);
        editTextApartmentNo = findViewById(R.id.editTextApartmentNo);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPrimaryPhoneNumber = findViewById(R.id.editTextPrimaryPhoneNumber);
        editTextBackupPhoneNumber = findViewById(R.id.editTextBackupPhoneNumber);
        editTextRegion= findViewById(R.id.editTextRegion);
        editTextNeighborhood= findViewById(R.id.editTextNeighborhood);

        ImageView back = findViewById(R.id.imageViewDeliveryInformationBack);
        back.setOnClickListener(v -> onBackPressed());


        // Initialize Spinners
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);

        // Set up ArrayAdapter for the country spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
        // Set default choice to "Saudi Arabia"
        spinnerCountry.setSelection(getPositionFromArray(countries, "Saudi Arabia"));

        // Set up listener for country spinner
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // When a country is selected, update the city spinner with the cities for that country
                updateCitySpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Get the address details and populate the fields
        Address address = HomeActivity.currentUser.getAddress();
        if (address != null) {
            setAddressDetails(address);
        }
        imageViewSave= findViewById(R.id.imageViewSaveNewAddress);
        imageViewSave.setOnClickListener(view -> updateAddressDetails(HomeActivity.currentUser.getId()));
    }

    // Method to update the city spinner based on the selected country
    private void updateCitySpinner(int countryPosition) {
        // Get the cities for the selected country
        String[] selectedCities = cities[countryPosition];

        // Set up ArrayAdapter for the city spinner
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedCities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
    }

    // Method to get the position of an item in an array
    private int getPositionFromArray(String[] array, String item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(item)) {
                return i;
            }
        }
        return -1; // Return -1 if item is not found in the array
    }

    private void setAddressDetails(Address address) {
        // Set country
        int countryPosition = getPositionFromArray(countries, address.getCountry());
        if (countryPosition != -1) {
            spinnerCountry.setSelection(countryPosition);

            // Set city
            updateCitySpinner(countryPosition);
            String[] selectedCities = cities[countryPosition];
            int cityPosition = getPositionFromArray(selectedCities, address.getCity());
            if (cityPosition != -1) {
                spinnerCity.setSelection(cityPosition);
            }
        }

        // Set region
        editTextRegion.setText(address.getRegion());

        // Set neighborhood
        editTextNeighborhood.setText(address.getNeighborhood());

        // Set street name
        editTextStreetName.setText(address.getStreetNo());

        // Set short address
        editTextShortAddress.setText(address.getShortAddress());

        // Set building number
        editTextBuildingNo.setText(address.getBuildingNo());

        // Set apartment number
        editTextApartmentNo.setText(address.getApartmentNo());

        // Set full name
        editTextFullName.setText(address.getFullName());

        // Set primary phone number
        editTextPrimaryPhoneNumber.setText(address.getPrimaryPhone());

        // Set backup phone number
        editTextBackupPhoneNumber.setText(address.getSecondaryPhone());
    }


    // Method to update the address details in the database
    private void updateAddressDetails(String userId) {
        // Get address details from the EditText fields
        final String country = spinnerCountry.getSelectedItem().toString();
        final String city = spinnerCity.getSelectedItem().toString();
        final String region = editTextRegion.getText().toString().trim();
        final String neighborhood = editTextNeighborhood.getText().toString().trim();
        final String streetNo = editTextStreetName.getText().toString().trim();
        final String shortAddress = editTextShortAddress.getText().toString().trim();
        final String buildingNo = editTextBuildingNo.getText().toString().trim();
        final String apartmentNo = editTextApartmentNo.getText().toString().trim();
        final String fullName = editTextFullName.getText().toString().trim();
        final String primaryPhone = editTextPrimaryPhoneNumber.getText().toString().trim();
        final String secondaryPhone = editTextBackupPhoneNumber.getText().toString().trim();

        // Create HTTP request to update address details in the database
        String url = MyApplication.API_PORT + "update_address.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success")) {
                            // Address details updated successfully

                            // Create a new Address object with the extracted details
                            Address newAddress = new Address(country, region, city, neighborhood, streetNo, shortAddress,
                                    buildingNo, apartmentNo, fullName, primaryPhone, secondaryPhone);

                            // Set the new Address object to the currentUser
                            HomeActivity.currentUser.setAddress(newAddress);

                            Toast.makeText(UserAddressActivity.this, jsonResponse.getString("success"), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else if (jsonResponse.has("error")) {
                            // Error updating address details
                            Toast.makeText(UserAddressActivity.this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            // You can perform any additional action here
                        }
                    } catch (JSONException e) {
                        // Error parsing JSON response
                        e.printStackTrace();
                        // You can display an error message or perform any necessary action
                    }
                },
                error -> {
                    // Error occurred while updating address details
                    error.printStackTrace();
                    Toast.makeText(UserAddressActivity.this, "Error updating address details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    // You can perform any additional action here
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (userId, country, city, region, etc.)
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("country", country);
                params.put("region", region);
                params.put("city", city);
                params.put("neighborhood", neighborhood);
                params.put("streetNo", streetNo);
                params.put("shortAddress", shortAddress);
                params.put("buildingNo", buildingNo);
                params.put("apartmentNo", apartmentNo);
                params.put("fullName", fullName);
                params.put("primaryPhone", primaryPhone);
                params.put("secondaryPhone", secondaryPhone);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onDestroy() {
        if (checkoutRunning){
        Intent intent = new Intent(UserAddressActivity.this, CheckoutActivity.class);
        startActivity(intent);
        }
        if (myAccountRunning){
            Intent intent = new Intent(UserAddressActivity.this, MyAccountActivity.class);
            startActivity(intent);
        }

        finish();
        super.onDestroy();
    }

}
