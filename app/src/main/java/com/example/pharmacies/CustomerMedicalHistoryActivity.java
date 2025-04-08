package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerMedicalHistoryActivity extends AppCompatActivity {

    TextView textViewBirthdayValue, textViewGenderValue, textViewDiseasesValue, textViewMedicinesValue, textViewComplicationsValue, textViewAllergiesValue, textViewAllergicReactionsValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_medical_history);
        getSupportActionBar().hide();

        ImageView imageViewCustomerHistoryBack= findViewById(R.id.imageViewCustomerHistoryBack);
        imageViewCustomerHistoryBack.setOnClickListener(view -> finish());

        // Find the TextViews in the layout
        textViewBirthdayValue = findViewById(R.id.textViewBirthDayValue);
        textViewGenderValue = findViewById(R.id.textViewGenderValue);
        textViewDiseasesValue = findViewById(R.id.textViewDiseasesValue);
        textViewMedicinesValue = findViewById(R.id.textViewMedicinesValue);
        textViewComplicationsValue = findViewById(R.id.textViewComplicationsValue);
        textViewAllergiesValue = findViewById(R.id.textViewAllergiesValue);
        textViewAllergicReactionsValue = findViewById(R.id.textViewAllergicReactionValue);

        if (PharmacyHomeActivity.user_id != null){
        getMedicalHistoryDetails(PharmacyHomeActivity.user_id);
        }
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

                            textViewBirthdayValue.setText(birthday);
                            textViewGenderValue.setText(gender);
                            textViewDiseasesValue.setText(diseases);
                            textViewMedicinesValue.setText(medicines);
                            textViewComplicationsValue.setText(complicationMedicines);
                            textViewAllergiesValue.setText(foodAllergies);
                            textViewAllergicReactionsValue.setText(medicineAllergies);



                        } else {
                            // No medical history found
                            Log.e("Medical History", "No medical history found for userId: " + userId);
                            // Create a new MedicalHistory object with default values
                            MedicalHistory medicalHistory = new MedicalHistory("", "");
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
}
