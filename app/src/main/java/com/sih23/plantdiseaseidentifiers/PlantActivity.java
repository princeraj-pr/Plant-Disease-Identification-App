package com.sih23.plantdiseaseidentifiers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
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
        bottomNavigationView.getMenu().getItem(4).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Setup Toolbar
        MaterialToolbar mainToolbar = binding.mainToolbar;
        setSupportActionBar(mainToolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.feedback) {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.recommendApp) {
            Toast.makeText(this, "Recommend App", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.contact) {
            Toast.makeText(this, "Contact & Social", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}