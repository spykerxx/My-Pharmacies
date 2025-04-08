package com.example.pharmacies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {

    private boolean pharmacySearch;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        getSupportActionBar().hide();

        pharmacySearch= getIntent().getBooleanExtra("pharmacySearch", false);

        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, start barcode scanning
            startBarcodeScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start barcode scanning
                startBarcodeScanning();
            } else {
                // Camera permission denied, show a message or handle accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startBarcodeScanning() {
        // Start barcode scanning
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle barcode scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Barcode detected

                String barcode = result.getContents();
                if (pharmacySearch){
              PharmacySearchProductActivity.editTextSearch.setText(barcode);
              finish();
              return;
                }
                Product product= findProductByBarcode(barcode);
                if (product!=null){
                String productName= product.getName();
                Intent intent= new Intent(BarcodeActivity.this, ProductActivity.class);
                intent.putExtra("productName", productName);
                startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Product not found! ", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                // Barcode scanning cancelled
                Toast.makeText(this, "Barcode scanning cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Product findProductByBarcode(String barcode) {
        if (HomeActivity.products != null) {
            for (Product product : HomeActivity.products) {
                if (product.getBarcode().equals(barcode)) {
                    return product;
                }
            }
        }
        return null;
    }
}
