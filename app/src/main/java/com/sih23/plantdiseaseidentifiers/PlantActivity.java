package com.sih23.plantdiseaseidentifiers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sih23.plantdiseaseidentifiers.databinding.ActivityPlantBinding;

public class PlantActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityPlantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setBackground(null);
        // Disable placeholder button in bottom navigation app bar
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Open camera activity
        binding.cameraFab.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(getBaseContext(), CameraActivity.class);
            startActivity(cameraIntent);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, HomeFragment.class, null)
                    .commit();
            return true;
        } else if (id == R.id.diagnose) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, DiagnoseFragment.class, null)
                    .commit();
            return true;
        } else if (id == R.id.my_plants) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, MyPlantFragment.class, null)
                    .commit();
            return true;
        } else if (id == R.id.account) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, AccountFragment.class, null)
                    .commit();
            return true;
        }
        return false;
    }
}