package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PharmacyChatActivity extends AppCompatActivity {

    ImageView imageViewCustomerChatBack, imageViewCallAndVideo, imageViewCallerProfileImage, imageViewChatLine, imageViewSendMessage;
    TextView textViewCustomerChatCallerName;
    EditText editTextEnteredMessage;
    ChatMessagePharmacyAdapter adapter;
    RecyclerView recyclerViewChatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_chat);
        getSupportActionBar().hide();

        // Find views by their IDs
        imageViewCustomerChatBack = findViewById(R.id.imageViewCustomerChatBack2);
        imageViewCustomerChatBack.setOnClickListener(view -> finish());
        imageViewCallAndVideo= findViewById(R.id.imageView131);
        imageViewCallAndVideo.setOnClickListener(view -> Toast.makeText(this, "Calls are not available now!", Toast.LENGTH_SHORT).show());
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage2);
        textViewCustomerChatCallerName = findViewById(R.id.textViewCustomerChatCallerName2);
        editTextEnteredMessage = findViewById(R.id.editTextEnteredMessage2);
        imageViewSendMessage.setOnClickListener(v -> sendMessage());
        ImageView imageViewCustomerMedicalHistory= findViewById(R.id.imageViewCustomerMedicalHistory);
        imageViewCustomerMedicalHistory.setOnClickListener(view -> startActivity(new Intent(PharmacyChatActivity.this, CustomerMedicalHistoryActivity.class)));


        textViewCustomerChatCallerName.setText("Customer Id: "+PharmacyHomeActivity.user_id);

        // Inside CustomerChatActivity.java
        recyclerViewChatMessages = findViewById(R.id.recyclerViewCustomerChat2);
        // Set layout manager
        recyclerViewChatMessages.setLayoutManager(new LinearLayoutManager(this));

// Add space between items
        int spaceHeight = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the spacing dimension in resources
        recyclerViewChatMessages.addItemDecoration(new SpaceItemDecoration(spaceHeight));




        adapter = new ChatMessagePharmacyAdapter(PharmacyHomeActivity.messages, PharmacyHomeActivity.currentPharmacy.getId());

// Set the adapter to the RecyclerView
        recyclerViewChatMessages.setAdapter(adapter);

    }

    private void sendMessage() {
        // Get the entered message from the EditText
        String message = editTextEnteredMessage.getText().toString().trim();

        // Check if the message is not empty or blank
        if (!message.isEmpty()) {
            // Create a ChatMessage object with the user's ID as the sender
            ChatMessage chatMessage = new ChatMessage(message, PharmacyHomeActivity.user_id, PharmacyHomeActivity.currentPharmacy.getId());
            chatMessage.setSender(PharmacyHomeActivity.currentPharmacy.getId());

            // Add the message to the list of chat messages
            adapter.addMessage(chatMessage);

            // Scroll the RecyclerView to the bottom to show the latest message
            recyclerViewChatMessages.scrollToPosition(adapter.getItemCount() - 1);

            // Clear the EditText after sending the message
            editTextEnteredMessage.setText("");

            sendMessage(PharmacyHomeActivity.user_id, message, PharmacyHomeActivity.currentPharmacy.getId());
        } else {
            // Show a toast message if the message is empty or blank
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage( String userId, String text, String sender) {
        String MESSAGE_SEND_URL = MyApplication.API_PORT + "user_chat_send.php";

        // Create a StringRequest with POST method to send the message
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MESSAGE_SEND_URL,
                response -> {
                    // Handle response from the server
                    Log.d("MessageSender", "Response: " + response);
                    // You can handle the response as needed
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("MessageSender", "Error sending message: " + error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("text", text);
                params.put("sender", sender);
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void update(){
        HomeActivity.fetchMessages(HomeActivity.currentUser.getId(), getApplicationContext());
        adapter = new ChatMessagePharmacyAdapter(HomeActivity.messages, HomeActivity.currentUser.getId());

// Set the adapter to the RecyclerView
        recyclerViewChatMessages.setAdapter(adapter);
    }

}
