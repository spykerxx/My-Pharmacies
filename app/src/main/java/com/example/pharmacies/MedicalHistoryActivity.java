package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MedicalHistoryActivity extends AppCompatActivity {

    EditText editTextBirthday;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale;
    RadioButton radioButtonFemale;
    EditText editTextDiseases;
    EditText editTextMedicinesUse;
    EditText editTextMedicinesComplications;
    EditText editTextFoodsAllergic;
    EditText editTextMedicinesAllergic;
    ImageView imageViewSave, imageViewBack;

    int currentUserId=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        getSupportActionBar().hide();

        int user_id = getIntent().getIntExtra("user_id",0);
        if (user_id!=0){
            currentUserId=user_id;
        }


        // Finding views by their IDs
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(MedicalHistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update EditText with the selected date
                                String dateString = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                editTextBirthday.setText(dateString);
                            }
                        }, year, month, dayOfMonth);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        editTextDiseases = findViewById(R.id.editTextDiseases);
        editTextMedicinesUse = findViewById(R.id.editTextMedicinesUse);
        editTextMedicinesComplications = findViewById(R.id.editTextMedicinesComplications);
        editTextFoodsAllergic = findViewById(R.id.editTextFoodsAllergic);
        editTextMedicinesAllergic = findViewById(R.id.editTextMedicinesAllergic);
        imageViewSave = findViewById(R.id.imageViewSave);
        imageViewBack= findViewById(R.id.imageViewMedicalHistoryBack);
        imageViewBack.setOnClickListener(view -> finish());


        TextView textViewSkip= findViewById(R.id.textViewSkip);
        if (currentUserId==0){
            textViewSkip.setVisibility(View.INVISIBLE);
        }
        textViewSkip.setOnClickListener(view -> {
            if (currentUserId!=0){
            startActivity(new Intent(MedicalHistoryActivity.this, LoginActivity.class));
            finishAffinity();
            }
            else finish();
        });


        // Set text based on MedicalHistory data
        if (HomeActivity.currentUser!=null){
        setMedicalHistoryData(HomeActivity.currentUser.getMedicalHistory());
        }

        imageViewSave.setOnClickListener(v -> {
            if (HomeActivity.currentUser!= null){
                updateMedicalRecord(HomeActivity.currentUser.getId());
            }
            else {
                updateMedicalRecord(String.valueOf(currentUserId));
                Toast.makeText(this, "Medical Record Saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MedicalHistoryActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
    }

    private void setMedicalHistoryData(MedicalHistory medicalHistory) {
        // Set birthday
        editTextBirthday.setText(medicalHistory.getBirthday());

        // Set gender
        if (medicalHistory.getGender().equals("Male")) {
            radioButtonMale.setChecked(true);
        } else if (medicalHistory.getGender().equals("Female")) {
            radioButtonFemale.setChecked(true);
        }

        // Set other fields
        editTextDiseases.setText(medicalHistory.getDiseases());
        editTextMedicinesUse.setText(medicalHistory.getMedicines());
        editTextMedicinesComplications.setText(medicalHistory.getComplicationMedicines());
        editTextFoodsAllergic.setText(medicalHistory.getFoodAllergies());
        editTextMedicinesAllergic.setText(medicalHistory.getMedicineAllergies());
    }

    private void updateMedicalRecord(String userId) {
        // Get medical record details from the EditText fields
        final String birthday = editTextBirthday.getText().toString().trim();
        final String gender;
        if (radioButtonMale.isChecked()) {
            gender = "Male";
        } else if (radioButtonFemale.isChecked()) {
            gender = "Female";
        } else {
            // Default to empty string or handle this case based on your requirements
            gender = "";
        }
        final String diseases = editTextDiseases.getText().toString().trim();
        final String medicines = editTextMedicinesUse.getText().toString().trim();
        final String complicationMedicines = editTextMedicinesComplications.getText().toString().trim();
        final String foodAllergies = editTextFoodsAllergic.getText().toString().trim();
        final String medicineAllergies = editTextMedicinesAllergic.getText().toString().trim();

        // Create HTTP request to update medical record in the database
        String url = MyApplication.API_PORT + "update_medical_record.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success")) {
                            // Medical record updated successfully
                           // Toast.makeText(MedicalHistoryActivity.this, jsonResponse.getString("success"), Toast.LENGTH_SHORT).show();
                            // You can perform any additional action here
                           if (HomeActivity.currentUser!=null){
                            MedicalHistory newRecord= new MedicalHistory(birthday, gender);
                            newRecord.setDiseases(diseases);
                            newRecord.setMedicines(medicines);
                            newRecord.setComplicationMedicines(complicationMedicines);
                            newRecord.setFoodAllergies(foodAllergies);
                            newRecord.setMedicineAllergies(medicineAllergies);
                            HomeActivity.currentUser.setMedicalHistory(newRecord);
                           }
                        } else if (jsonResponse.has("error")) {
                            // Error updating medical record
                            Toast.makeText(MedicalHistoryActivity.this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            // You can perform any additional action here
                        }
                    } catch (JSONException e) {
                        // Error parsing JSON response
                        e.printStackTrace();
                        // You can display an error message or perform any necessary action
                    }
                },
                error -> {
                    // Error occurred while updating medical record
                    error.printStackTrace();
                    Toast.makeText(MedicalHistoryActivity.this, "Error updating medical record: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    // You can perform any additional action here
                })
        {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters for the request (userId, birthday, gender, diseases, etc.)
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("birthday", birthday);
                params.put("gender", gender);
                params.put("diseases", diseases);
                params.put("medicines", medicines);
                params.put("complicationMedicines", complicationMedicines);
                params.put("foodAllergies", foodAllergies);
                params.put("medicineAllergies", medicineAllergies);
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }


}
