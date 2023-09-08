package com.sih23.plantdiseaseidentifiers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sih23.plantdiseaseidentifiers.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setBackground(null);
        // Disable placeholder button in bottom navigation app bar
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }
}