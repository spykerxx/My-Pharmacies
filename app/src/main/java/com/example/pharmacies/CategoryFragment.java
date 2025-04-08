package com.example.pharmacies;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    private ListView listViewCategories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ListView
        listViewCategories = view.findViewById(R.id.listViewCategories);

        // Create an ArrayAdapter to populate the ListView with the provided items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category_items));

        // Set the ArrayAdapter to the ListView
        listViewCategories.setAdapter(adapter);

        // Set onItemClickListner to the ListView items
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item text
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Start the ProductsActivity and pass the selected category as an extra in a bundle
                Intent intent = new Intent(requireContext(), ProductsActivity.class);
                intent.putExtra("category", selectedItem);
                startActivity(intent);
            }
        });


    }
}
