package com.example.pharmacies;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PharmacyProfileFragment extends Fragment {

    private EditText editTextPharmacyConfirmPasswordProfile;
    private EditText editTextPharmacyUsernameProfile;
    private EditText editTextPharmacyEmailProfile;
    private EditText editTextPharmacyPhoneProfile, editTextPharmacyAddressProfile;
    private EditText editTextPharmacyPasswordProfile;
    private ImageView imageViewButtonPharmacySignup2;
    private ImageView imageViewPharmacyProfileImage, imageViewSettings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy_profile, container, false);

        // Find views by their IDs
        editTextPharmacyConfirmPasswordProfile = view.findViewById(R.id.editTextPharmacyConfirmPasswordProfile);
        editTextPharmacyUsernameProfile = view.findViewById(R.id.editTextPharmacyUsernameProfile);
        editTextPharmacyEmailProfile = view.findViewById(R.id.editTextPharmacyEmailProfile);
        editTextPharmacyPhoneProfile = view.findViewById(R.id.editTextPharmacyPhoneProfile);
        editTextPharmacyPasswordProfile = view.findViewById(R.id.editTextPharmacyPasswordProfile);
        imageViewButtonPharmacySignup2 = view.findViewById(R.id.imageViewButtonPharmacySignup2);
        imageViewPharmacyProfileImage = view.findViewById(R.id.imageViewPharmacyProfileImage);
        editTextPharmacyAddressProfile= view.findViewById(R.id.editTextPharmacyAddressProfile);
        imageViewSettings= view.findViewById(R.id.imageViewPharmacySettings);

        imageViewSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PharmacySettingsActivity.class);
            startActivity(intent);
        });


        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    // Set values to EditText fields
                    editTextPharmacyUsernameProfile.setText(PharmacyHomeActivity.currentPharmacy.getName());
                    editTextPharmacyEmailProfile.setText(PharmacyHomeActivity.currentPharmacy.getEmail());
                    editTextPharmacyPhoneProfile.setText(PharmacyHomeActivity.currentPharmacy.getPhone());
                    editTextPharmacyAddressProfile.setText(PharmacyHomeActivity.currentPharmacy.getAddress());

                    int resourceId = getActivity().getApplicationContext().getResources().getIdentifier(PharmacyHomeActivity.currentPharmacy.getImage(), "drawable", getActivity().getApplicationContext().getPackageName());
                    if (resourceId != 0) {
                        imageViewPharmacyProfileImage.setImageResource(resourceId);
                    }
                    // Set any other values if needed
                }
                , 700);

        imageViewButtonPharmacySignup2.setOnClickListener(v -> updatePharmacyDetails());

        return view;
    }

    private void updatePharmacyDetails() {
        // Get updated values
        String name = editTextPharmacyUsernameProfile.getText().toString().trim();
        String email = editTextPharmacyEmailProfile.getText().toString().trim();
        String phone = editTextPharmacyPhoneProfile.getText().toString().trim();
        String address = editTextPharmacyAddressProfile.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("pharmacy_id", PharmacyHomeActivity.currentPharmacy.getId());
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("address", address);

        // API URL
        String url = MyApplication.API_PORT + "update_pharmacy.php";

        // Make a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    handleUpdateResponse(response);
                },
                error -> {
                    // Handle error
                    Toast.makeText(getActivity(), "Error updating pharmacy details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    // Handle response from API call
    private void handleUpdateResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");
            if (success) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                // Optionally update UI or take any other action upon successful update
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

}

