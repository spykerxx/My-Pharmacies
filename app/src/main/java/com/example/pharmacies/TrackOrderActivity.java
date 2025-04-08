package com.example.pharmacies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrackOrderActivity extends AppCompatActivity {

    // Order information views
    private TextView textViewTrackOrderNumberValue;
    private TextView textViewTrackOrderDateValue;
    private TextView textViewTrackOrderTimeValue;
    private TextView textViewTrackOrderAmountValue;
    private TextView textViewEarnedPointsValue;

    // Delivery information views
    private TextView textViewDeliveryTrackDateValue;
    private TextView textViewRepresentativeNumberValue;
    private TextView textViewRepresentativeNameValue;
    private TextView textViewDeliveryTrackTimeValue;
    // ImageView elements related to delivery phases
    private ImageView imageViewPhaseTwo;
    private ImageView imageViewLinePhaseTwo;
    private ImageView imageViewPhaseThree;
    private ImageView imageViewLinePhaseThree;
    private ImageView imageViewPhaseFour;
    private ImageView imageViewLinePhaseFour;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        getSupportActionBar().hide();

        Order currentOrder = HomeActivity.activeOrder;
        // Find views for order information
        textViewTrackOrderNumberValue = findViewById(R.id.textViewTrackOrderNumberValue);
        textViewTrackOrderDateValue = findViewById(R.id.textViewTrackOrderDateValue);
        textViewTrackOrderTimeValue = findViewById(R.id.textViewTrackOrderTimeValue);
        textViewTrackOrderAmountValue = findViewById(R.id.textViewTrackOrderAmountValue);
        textViewEarnedPointsValue = findViewById(R.id.textViewEarnedPointsValue);

        // Find views for delivery information
        textViewDeliveryTrackDateValue = findViewById(R.id.textViewDeliveryTrackDateValue);
        textViewRepresentativeNumberValue = findViewById(R.id.textViewRepresentativeNumberValue);
        textViewRepresentativeNameValue = findViewById(R.id.textViewRepresentativeNameValue);
        textViewDeliveryTrackTimeValue= findViewById(R.id.textViewDeliveryTrackTimeValue);

        // Find views for ImageView elements related to delivery phases
        imageViewPhaseTwo = findViewById(R.id.imageViewPhaseTwo);
        imageViewLinePhaseTwo = findViewById(R.id.imageViewLinePhaseTwo);
        imageViewPhaseThree = findViewById(R.id.imageViewPhaseThree);
        imageViewLinePhaseThree = findViewById(R.id.imageViewLinePhaseThree);
        imageViewPhaseFour = findViewById(R.id.imageViewPhaseFour);
        imageViewLinePhaseFour = findViewById(R.id.imageViewLinePhaseFour);
        ImageView imageViewOrderHistory = findViewById(R.id.imageViewTrackYourOrderHistory);
        imageViewOrderHistory.setOnClickListener(view -> startActivity(new Intent(TrackOrderActivity.this, OrdersHistoryActivity.class)));

        back= findViewById(R.id.imageViewTrackOrderBack);
        back.setOnClickListener(view -> finish());

        if (currentOrder !=null){  setDataIntoViews(currentOrder);}
        else Toast.makeText(this, "current order is null!", Toast.LENGTH_SHORT).show();

    }
    // Method to update views based on the order phase
    private void updateOrderStatus(String phase) {
        switch (phase) {
            case "phase2":
                imageViewLinePhaseTwo.setImageResource(R.drawable.dotscomplete);
                imageViewPhaseTwo.setImageResource(R.drawable.bigdotscomplete);
                imageViewLinePhaseThree.setImageResource(R.drawable.dotsnotyetcomplete);
                break;
            case "phase3":
                imageViewLinePhaseThree.setImageResource(R.drawable.dotsnotyetcomplete);
                imageViewPhaseTwo.setImageResource(R.drawable.bigdotscomplete);
                imageViewPhaseThree.setImageResource(R.drawable.bigdotscomplete);
                imageViewLinePhaseThree.setImageResource(R.drawable.dotscomplete);
                imageViewLinePhaseTwo.setImageResource(R.drawable.dotscomplete);
                imageViewLinePhaseFour.setImageResource(R.drawable.dotsnotyetcomplete);
                break;
            case "phase4":
                imageViewLinePhaseThree.setImageResource(R.drawable.dotsnotyetcomplete);
                imageViewPhaseTwo.setImageResource(R.drawable.bigdotscomplete);
                imageViewPhaseThree.setImageResource(R.drawable.bigdotscomplete);
                imageViewLinePhaseThree.setImageResource(R.drawable.dotscomplete);
                imageViewLinePhaseTwo.setImageResource(R.drawable.dotscomplete);
                imageViewLinePhaseFour.setImageResource(R.drawable.dotscomplete);
                imageViewPhaseFour.setImageResource(R.drawable.bigdotscomplete);
                break;
            default:
                // Default behavior for phase1, do nothing
                break;
        }
    }

    private void setDataIntoViews(Order currentOrder) {
        // Set order information
        textViewTrackOrderNumberValue.setText(String.valueOf(currentOrder.getNumber()));
        textViewTrackOrderDateValue.setText(currentOrder.getOrderDate());
        textViewTrackOrderTimeValue.setText(currentOrder.getOrderTime());
        textViewTrackOrderAmountValue.setText(String.valueOf(currentOrder.getOrderAmount()));
        textViewEarnedPointsValue.setText(String.valueOf(currentOrder.getOrderPoints()));

        // Set delivery information
        textViewDeliveryTrackDateValue.setText(currentOrder.getDeliveryDate());
        textViewRepresentativeNumberValue.setText(currentOrder.getDeliveryNumber());
        textViewRepresentativeNameValue.setText(currentOrder.getDeliveryName());
        textViewDeliveryTrackTimeValue.setText(currentOrder.getDeliveryTime());

        // Set status
        updateOrderStatus(currentOrder.getStatus());
    }


}
