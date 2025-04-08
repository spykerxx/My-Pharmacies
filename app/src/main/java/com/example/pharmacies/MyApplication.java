package com.example.pharmacies;

import android.app.Application;


public class MyApplication extends Application {
    public static String API_PORT="http://192.168.1.101/Pharmacies/";
    public static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024; // 3MB

    @Override
    public void onCreate() {
        super.onCreate();
        // Add any necessary initialization code here
    }
}
            
