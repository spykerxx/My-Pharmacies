package com.example.pharmacies;

import android.content.Intent;
import android.media.Image;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView imageViewMyAccount, imageViewLogout, imageViewWishlist, imageViewPoints, imageViewRecentView;
    private ImageView imageViewAddressInfo, imageViewOrdersHistory, imageViewTrackOrder;
    private TextView textViewPofileName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewMyAccount= view.findViewById(R.id.imageViewProfileMyAccount);
        imageViewMyAccount.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), MyAccountActivity.class)));

        textViewPofileName= view.findViewById(R.id.textViewProfileName);
        textViewPofileName.setText(HomeActivity.currentUser.getUsername());

        imageViewLogout= view.findViewById(R.id.imageViewProfileLogout);
        imageViewLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finishAffinity();
        });

        imageViewWishlist= view.findViewById(R.id.imageViewProfileWishList);
        imageViewWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WishListActivity.class);
            startActivity(intent);
        });

        imageViewPoints= view.findViewById(R.id.imageViewProfileMyPoints);
        imageViewPoints.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PointsActivity.class);
            startActivity(intent);
        });

        imageViewRecentView= view.findViewById(R.id.imageViewProfileRecentView);
        imageViewRecentView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            boolean recentView= true;
            intent.putExtra("recentView", recentView);
            startActivity(intent);
        });

        imageViewAddressInfo= view.findViewById(R.id.imageViewProfileAddressInformation);
        imageViewAddressInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserAddressActivity.class);
            startActivity(intent);
        });

        imageViewOrdersHistory= view.findViewById(R.id.imageViewProfileOrderHistory);
        imageViewOrdersHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrdersHistoryActivity.class);
            startActivity(intent);
        });

        imageViewTrackOrder= view.findViewById(R.id.imageViewProfileTrackOrder);
        imageViewTrackOrder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
            startActivity(intent);
        });

        ImageView imageViewSupport= view.findViewById(R.id.imageViewProfileSupport);
        imageViewSupport.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SupportActivity.class);
            startActivity(intent);
        });

        ImageView imageViewFeedback= view.findViewById(R.id.imageViewProfileFeedback);
        imageViewFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            startActivity(intent);
        });

        ImageView imageViewAboutus= view.findViewById(R.id.imageViewProfileAboutUs);
        imageViewAboutus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
