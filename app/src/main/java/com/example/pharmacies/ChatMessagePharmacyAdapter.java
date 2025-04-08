package com.example.pharmacies;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagePharmacyAdapter extends RecyclerView.Adapter<ChatMessagePharmacyAdapter.ViewHolder> {
    private List<ChatMessage> messages;
    private String currentUser; // Assuming you have the ID of the current user

    public ChatMessagePharmacyAdapter(List<ChatMessage> messages, String currentUser) {
        this.messages = filterMessages(messages);
        this.currentUser = currentUser;
    }

    private List<ChatMessage> filterMessages(List<ChatMessage> messages) {
        List<ChatMessage> filteredMessages = new ArrayList<>();
        for (ChatMessage message : messages) {
            if (message.getUserId().equals(PharmacyHomeActivity.user_id)) {
                filteredMessages.add(message);
            }
        }
        return filteredMessages;
    }

    public void addMessage(ChatMessage message) {
        if (message.getSender().equals(currentUser)) {
            messages.add(message);
            PharmacyHomeActivity.messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textViewMessage);
        }

        public void bind(ChatMessage message) {
            // Set the message text
            messageTextView.setText(message.getMessage());

            // Set layout params based on whether the message is from the current user or not
            if (message.getSender().equals(currentUser)) {
                // Align message to the right if it's from the current user
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginEnd(30); // Adjust as needed
                itemView.setLayoutParams(params);
            } else {
                // Align message to the left if it's from the other party
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginStart(100); // Adjust as needed
                itemView.setLayoutParams(params);
                messageTextView.setBackgroundResource(R.drawable.messageframe2);
                messageTextView.setTextColor(Color.BLACK);
            }
        }
    }
}
