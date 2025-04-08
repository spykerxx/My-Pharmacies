package com.example.pharmacies;

public class ChatMessage {
    private String message;
    private String user_id;
    private String pharmacy_id;
    private String username;
    private String sender;

    public ChatMessage(String message, String user_id, String pharmacy_id) {
        this.message = message;
        this.user_id = user_id;
        this.pharmacy_id = pharmacy_id;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return user_id;
    }

    public String getPharmacyId() {
        return pharmacy_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(String pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
