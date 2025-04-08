package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CustomerChatActivity extends AppCompatActivity {

    ImageView imageViewCustomerChatBack, imageViewCallAndVideo, imageViewCallerProfileImage, imageViewChatLine, imageViewSendMessage;
    TextView textViewCustomerChatCallerName;
    EditText editTextEnteredMessage;
    ChatMessageAdapter adapter;
    RecyclerView recyclerViewChatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat);
        getSupportActionBar().hide();

        // Find views by their IDs
        imageViewCustomerChatBack = findViewById(R.id.imageViewCustomerChatBack);
        imageViewCustomerChatBack.setOnClickListener(view -> finish());
        imageViewCallAndVideo = findViewById(R.id.imageView58);
        imageViewCallAndVideo.setOnClickListener(view -> Toast.makeText(this, "Calls are not available now!", Toast.LENGTH_SHORT).show());
        imageViewCallerProfileImage = findViewById(R.id.imageView18);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        textViewCustomerChatCallerName = findViewById(R.id.textViewCustomerChatCallerName);
        editTextEnteredMessage = findViewById(R.id.editTextEnteredMessage);
        imageViewSendMessage.setOnClickListener(v -> sendMessage());


        // Inside CustomerChatActivity.java
        recyclerViewChatMessages = findViewById(R.id.recyclerViewCustomerChat);
        // Set layout manager
        recyclerViewChatMessages.setLayoutManager(new LinearLayoutManager(this));

// Add space between items
        int spaceHeight = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the spacing dimension in resources
        recyclerViewChatMessages.addItemDecoration(new SpaceItemDecoration(spaceHeight));




        adapter = new ChatMessageAdapter(HomeActivity.messages, HomeActivity.currentUser.getId());

// Set the adapter to the RecyclerView
        recyclerViewChatMessages.setAdapter(adapter);

    }

    private void sendMessage() {
        // Get the entered message from the EditText
        String message = editTextEnteredMessage.getText().toString().trim();

        // Check if the message is not empty or blank
        if (!message.isEmpty()) {
            // Create a ChatMessage object with the user's ID as the sender
            ChatMessage chatMessage = new ChatMessage(message, HomeActivity.currentUser.getId(), "");
            chatMessage.setSender(HomeActivity.currentUser.getId());

            // Add the message to the list of chat messages
            adapter.addMessage(chatMessage);

            // Scroll the RecyclerView to the bottom to show the latest message
            recyclerViewChatMessages.scrollToPosition(adapter.getItemCount() - 1);

            // Clear the EditText after sending the message
            editTextEnteredMessage.setText("");

            sendMessage(HomeActivity.currentUser.getId(), message, HomeActivity.currentUser.getId());
        } else {
            // Show a toast message if the message is empty or blank
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage( String userId, String text, String sender) {
        String MESSAGE_SEND_URL = MyApplication.API_PORT + "user_chat_send.php";

        // Create a StringRequest with POST method to send the message
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MESSAGE_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        Log.d("MessageSender", "Response: " + response);
                        // You can handle the response as needed
                    }
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
        adapter = new ChatMessageAdapter(HomeActivity.messages, HomeActivity.currentUser.getId());

// Set the adapter to the RecyclerView
        recyclerViewChatMessages.setAdapter(adapter);
    }

}
