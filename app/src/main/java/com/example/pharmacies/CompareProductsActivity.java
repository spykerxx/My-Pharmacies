package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CompareProductsActivity extends AppCompatActivity {

    public static ImageView imageViewComparePharmacyLeft;
    public static  ImageView imageViewComparePharmacyMid;
    public static  ImageView imageViewCompareAddLeft;
    public static  ImageView imageViewCompareAddRight;
    public static  ImageView imageViewCompareInStockRight;
    public static  ImageView imageViewCompareInStockLeft;
    public static  ImageView imageViewCompareInStockMid;
    public static  ImageView imageViewCompareAddMid;
    public static  ImageView imageViewComparePharmacyRight;
    public static  TextView textViewCompareBrandRight;
    public static  TextView textViewCompareBrandMid;
    public static  TextView textViewCompareBrandLeft;
    public static  TextView textViewComparePriceRight;
    public static  TextView textViewComparePriceMid;
    public static  TextView textViewComparePriceLeft;
    public static  TextView textViewCompareVolumeLeft, textViewCompareVolumeMid, textViewCompareVolumeRight;
    public static TextView textViewCompareNameLeft, textViewCompareNameMid, textViewCompareNameRight;

    public static boolean running= false;

    public static Product productLeft, productRight, productMid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_products);
        getSupportActionBar().hide();

        running=true;
        // Initialize views
        imageViewComparePharmacyLeft = findViewById(R.id.imageViewComparePharmacyLeft);
        imageViewCompareAddLeft = findViewById(R.id.imageViewCompareAddLeft);
        imageViewCompareInStockLeft = findViewById(R.id.imageViewCompareInStockLeft);
        imageViewCompareInStockMid = findViewById(R.id.imageViewCompareInStockMid);
        imageViewCompareAddMid = findViewById(R.id.imageViewCompareAddMid);
        imageViewComparePharmacyRight = findViewById(R.id.imageViewComparePharmacyRight);
        textViewCompareBrandRight = findViewById(R.id.textViewCompareBrandRight);
        textViewCompareBrandMid = findViewById(R.id.textViewCompareBrandMid);
        textViewCompareBrandLeft = findViewById(R.id.textViewCompareBrandLeft);
        textViewComparePriceRight = findViewById(R.id.textViewComparePriceRight);
        textViewComparePriceMid = findViewById(R.id.textViewComparePriceMid);
        textViewComparePriceLeft = findViewById(R.id.textViewComparePriceLeft);
        imageViewComparePharmacyMid = findViewById(R.id.imageViewComparePharmacyMid);
        imageViewCompareAddRight = findViewById(R.id.imageViewCompareAddRight);
        imageViewCompareInStockRight = findViewById(R.id.imageViewCompareInStockRight);
        textViewCompareNameLeft= findViewById(R.id.textViewCompareNameLeft);
        textViewCompareNameRight= findViewById(R.id.textViewCompareNameRight);
        textViewCompareNameMid= findViewById(R.id.textViewCompareNameMid);
        textViewCompareVolumeLeft= findViewById(R.id.textViewCompareVolumeLeft);
        textViewCompareVolumeRight= findViewById(R.id.textViewCompareVolumeRight);
        textViewCompareVolumeMid= findViewById(R.id.textViewCompareVolumeMid);


        ImageView back= findViewById(R.id.imageViewCompareBack);
        back.setOnClickListener(view -> finish());

        imageViewCompareAddLeft.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(CompareProductsActivity.this, ProductsActivity.class);
            String compareProduct = "left";
            boolean compareRunning=true;
            intent.putExtra("compareRunning", compareRunning);
            intent.putExtra("compareProduct", compareProduct);
            startActivity(intent);
        });

        imageViewCompareAddMid.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(CompareProductsActivity.this, ProductsActivity.class);
            String compareProduct = "mid";
            boolean compareRunning=true;
            intent.putExtra("compareRunning", compareRunning);
            intent.putExtra("compareProduct", compareProduct);
            startActivity(intent);
        });

        imageViewCompareAddRight.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(CompareProductsActivity.this, ProductsActivity.class);
            String compareProduct = "right";
            boolean compareRunning=true;
            intent.putExtra("compareRunning", compareRunning);
            intent.putExtra("compareProduct", compareProduct);
            startActivity(intent);
        });


    }

    public static void update(){
        if (productLeft!=null){
            textViewComparePriceLeft.setText(productLeft.getPrice()+" SAR");
            textViewCompareBrandLeft.setText(productLeft.getCategory());
            Pharmacy pharmacy= HomeActivity.searchPharmacy(productLeft.getPharmacyId());
            Image imageBlob=pharmacy.getImageBlob();
            if (imageBlob!=null){
                imageViewComparePharmacyLeft.setImageBitmap(imageBlob.getBitmap());
                imageViewComparePharmacyLeft.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else{
                imageViewComparePharmacyLeft.setImageResource(R.drawable.pharmacy1);
            }
            imageViewCompareInStockLeft.setVisibility(View.VISIBLE);
            if (productLeft.getStock().equals("out of stock")){
                imageViewCompareInStockLeft.setImageResource(R.drawable.outofstock);
            }
            textViewCompareNameLeft.setText(productLeft.getName());
            textViewCompareVolumeLeft.setText(productLeft.getVolume());
            setImageForCompare(imageViewCompareAddLeft, productLeft.getImageBlob());

        }

        if (productMid!=null){
            textViewComparePriceMid.setText(productMid.getPrice()+" SAR");
            textViewCompareBrandMid.setText(productMid.getCategory());
            Pharmacy pharmacy= HomeActivity.searchPharmacy(productMid.getPharmacyId());
            Image imageBlob=pharmacy.getImageBlob();
            if (imageBlob!=null){
                imageViewComparePharmacyMid.setImageBitmap(imageBlob.getBitmap());
                imageViewComparePharmacyMid.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else{
                imageViewComparePharmacyMid.setImageResource(R.drawable.pharmacy2);
            }
            imageViewCompareInStockMid.setVisibility(View.VISIBLE);
            if (productMid.getStock().equals("out of stock")){
                imageViewCompareInStockMid.setImageResource(R.drawable.outofstock);
            }
            textViewCompareNameMid.setText(productMid.getName());
            textViewCompareVolumeMid.setText(productMid.getVolume());
            setImageForCompare(imageViewCompareAddMid, productMid.getImageBlob());

        }

        if (productRight!=null){
            textViewComparePriceRight.setText(productRight.getPrice()+" SAR");
            textViewCompareBrandRight.setText(productRight.getCategory());
            Pharmacy pharmacy= HomeActivity.searchPharmacy(productRight.getPharmacyId());
            Image imageBlob=pharmacy.getImageBlob();
            if (imageBlob!=null){
                imageViewComparePharmacyRight.setImageBitmap(imageBlob.getBitmap());
                imageViewComparePharmacyRight.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else{
            imageViewComparePharmacyRight.setImageResource(R.drawable.pharmacy3);
            }
            imageViewCompareInStockRight.setVisibility(View.VISIBLE);
            if (productRight.getStock().equals("out of stock")){
                imageViewCompareInStockRight.setImageResource(R.drawable.outofstock);
            }
            textViewCompareNameRight.setText(productRight.getName());
            textViewCompareVolumeRight.setText(productRight.getVolume());
            setImageForCompare(imageViewCompareAddRight, productRight.getImageBlob());

        }
    }

    public static void setImageForCompare(ImageView imageView, Image imageBlob) {
      if (imageBlob!=null){
          imageView.setImageBitmap(imageBlob.getBitmap());
          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

      }
    }

    @Override
    protected void onDestroy() {
       running=false;
       productLeft=null; productRight= null; productMid=null;
        finish();
        super.onDestroy();
    }
}
