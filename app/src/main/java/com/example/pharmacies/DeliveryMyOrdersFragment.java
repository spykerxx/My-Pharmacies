package com.example.pharmacies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DeliveryMyOrdersFragment extends Fragment implements MyOrderDetailsCallback {

   ImageView imageViewDeliverOrder, imageViewOrdersHistory, imageViewProfile, imageViewSupport, imageViewFeedBack;

    Order order = new Order();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_my_orders, container, false);

          fetchOrderDetails(getContext(), String.valueOf(DeliveryHomeActivity.delivery.getUsername()), this);

        imageViewOrdersHistory= view.findViewById(R.id.imageViewMyDeliveryOrderHistory);
        imageViewProfile= view.findViewById(R.id.imageViewMyDevlieryProfile);
        imageViewSupport= view.findViewById(R.id.imageViewMyDeliverySupport);
        imageViewFeedBack= view.findViewById(R.id.imageViewMyDeliveryFeedBack);

        ImageView imageViewCallCustomer= view.findViewById(R.id.imageViewCallCustomer);
        imageViewCallCustomer.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), DeliveryCallCustomerActivity.class)));

        imageViewOrdersHistory.setOnClickListener(getView -> startActivity(new Intent(getActivity(), DeliveryOrderHistoryActivity.class)));
        imageViewProfile.setOnClickListener(getView -> startActivity(new Intent(getActivity(), DeliveryProfileActivity.class)));
        imageViewSupport.setOnClickListener(getView -> startActivity(new Intent(getActivity(), SupportActivity.class)));
        imageViewFeedBack.setOnClickListener(getView -> startActivity(new Intent(getActivity(), FeedbackActivity.class)));

        return view;
    }
    @Override
    public void onSuccess(Order order) {
        TextView orderNumberTextView = getView().findViewById(R.id.orderNumberTextView);
        orderNumberTextView.setText("Order number: " + order.getNumber());

        TextView orderAmountTextView = getView().findViewById(R.id.orderAmountTextView);
        orderAmountTextView.setText("Order amount: " + order.getOrderAmount());

        TextView pharmacyNameTextView = getView().findViewById(R.id.pharmacyNameTextView);
        pharmacyNameTextView.setText("Pharmacy name: " + order.getPharmacy().getName());

        TextView pharmacyAddressTextView = getView().findViewById(R.id.pharmacyAddressTextView);
        pharmacyAddressTextView.setText("Pharmacy address: " + order.getPharmacy().getAddress());

        TextView pharmacyPhoneTextView = getView().findViewById(R.id.pharmacyPhoneTextView);
        pharmacyPhoneTextView.setText("Pharmacy phone: " + order.getPharmacy().getPhone());

        TextView customerNameTextView = getView().findViewById(R.id.customerNameTextView);
        customerNameTextView.setText("Customer name: " + order.getUser().getUsername());

        TextView customerAddressTextView = getView().findViewById(R.id.customerAddressTextView);
        customerAddressTextView.setText("Customer short address: " + order.getUser().getAddress().getShortAddress());

        TextView customerPhoneTextView = getView().findViewById(R.id.customerPhoneTextView);
        customerPhoneTextView.setText("Customer phone: " + order.getUser().getAddress().getPrimaryPhone());

        TextView customerNeighborhoodTextView = getView().findViewById(R.id.customerNeighborhoodTextView);
        customerNeighborhoodTextView.setText("Neighborhood: " + order.getUser().getAddress().getNeighborhood());

        TextView customerStreetTextView = getView().findViewById(R.id.customerStreetTextView);
        customerStreetTextView.setText("Street: " + order.getUser().getAddress().getStreetNo());

        TextView customerApartmentNumberTextView = getView().findViewById(R.id.customerApartmentNumberTextView);
        customerApartmentNumberTextView.setText("Apartment Number: " + order.getUser().getAddress().getApartmentNo());

        TextView customerSecondaryPhone= getView().findViewById(R.id.customerCustomerBackupPhoneNumberTextView);
        customerSecondaryPhone.setText("Customer backup phone number: "+order.getUser().getAddress().getSecondaryPhone());

        TextView orderDetailTextView= getView().findViewById(R.id.orderDetailTextView);
        orderDetailTextView.setText("Order details: "+order.getOrder_detail());



        imageViewDeliverOrder= getView().findViewById(R.id.imageViewMyDeliveryDeliverOrder);
        imageViewDeliverOrder.setOnClickListener(view -> {
            // Get the current date and time
            String currentDate = getCurrentDate();
            String currentTime = getCurrentTime();

            // Call the deliverOrder() method with the current date and time
            deliverOrder(order.getNumber(), DeliveryHomeActivity.delivery.getPhone(), currentTime, currentDate);
        });
    }

    // Method to get the current date in the format "YYYY-MM-DD"
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // Method to get the current time in the format "HH:mm a" (e.g., "2:30 AM")
    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date currentTime = new Date();
        return timeFormat.format(currentTime);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle error
       // Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void fetchOrderDetails(Context context, String deliveryName, MyOrderDetailsCallback callback) {
        // API call to fetch order details based on delivery name
        String URL = MyApplication.API_PORT + "current_delivery_order.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(response);

                        // Check if the response contains an error
                        if (jsonResponse.has("error")) {
                            // Handle the error case
                            String errorMessage = jsonResponse.getString("error");
                            callback.onError(errorMessage);
                        } else {
                            // Extract order details
                            int orderNumber = jsonResponse.getInt("order_number");
                            double orderAmount = jsonResponse.getDouble("order_amount");
                            String customerName = jsonResponse.getString("customer_name");
                            String pharmacyName = jsonResponse.getString("pharmacy_name");
                            String pharmacyAddress = jsonResponse.getString("pharmacy_address");
                            String pharmacyPhone = jsonResponse.getString("pharmacy_phone");
                            String customerPhone = jsonResponse.getString("customer_phone");
                            String order_detail= jsonResponse.getString("order_detail");

                            // Create Address object with extracted details
                            JSONObject addressObject = jsonResponse.getJSONObject("address");
                            Address address = new Address(
                                    // Add more address details as needed
                            );
                            address.setShortAddress(addressObject.getString("address_line"));
                            address.setFullName(addressObject.getString("fullName"));
                            address.setPrimaryPhone(addressObject.getString("primaryPhone"));
                            address.setSecondaryPhone(addressObject.getString("secondaryPhone"));
                            address.setStreetNo(addressObject.getString("streetNo"));
                            address.setBuildingNo(addressObject.getString("buildingNo"));
                            address.setNeighborhood(addressObject.getString("neighborhood"));
                            address.setApartmentNo(addressObject.getString("apartmentNo"));



                            order = new Order();
                            order.setNumber(orderNumber);
                            order.setOrderAmount(orderAmount);
                            order.setOrder_detail(order_detail);

                            // Create User object with extracted details
                            User user = new User();
                            user.setUsername(customerName);


                           // address.setShortAddress(customerAddress);
                            address.setPrimaryPhone(customerPhone);
                            user.setAddress(address);
                            order.setUser(user);

                            // Create Pharmacy object with extracted details
                            Pharmacy pharmacy = new Pharmacy();
                            pharmacy.setName(pharmacyName);
                            pharmacy.setAddress(pharmacyAddress);
                            pharmacy.setPhone(pharmacyPhone);
                            order.setPharmacy(pharmacy);

                            // Callback with fetched order details
                            callback.onSuccess(order);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  callback.onError("JSON parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                  //  callback.onError("Volley error: " + error.getMessage());
                }) {
            // Pass delivery name as parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("delivery_name", deliveryName);
                return params;
            }
        };
        // Add the request to the request queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void deliverOrder(int orderNumber, String deliveryPhone, String deliveryTime, String deliveryDate) {
        String url = MyApplication.API_PORT + "deliver_order.php";

        // Get the username from DeliveryHomeActivity.delivery.getUsername()
        String username = DeliveryHomeActivity.delivery.getUsername();

        // Create a StringRequest to make a POST request to the PHP file
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response here if needed
                    Toast.makeText(getContext(), "Order status and delivery details updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle errors here if needed
                    Toast.makeText(getContext(), "Error updating order status and delivery details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            // Pass parameters to the PHP file
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_number", String.valueOf(orderNumber));
                params.put("delivery_phone", deliveryPhone);
                params.put("delivery_time", deliveryTime);
                params.put("delivery_date", deliveryDate);
                params.put("username", username); // Add the username parameter
                return params;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}

 interface MyOrderDetailsCallback {
    void onSuccess(Order order);
    void onError(String errorMessage);
}

