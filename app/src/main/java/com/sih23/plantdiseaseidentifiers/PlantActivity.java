package com.sih23.plantdiseaseidentifiers;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sih23.plantdiseaseidentifiers.databinding.ActivityPlantBinding;

public class PlantActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String ARG_PARAM_COORDINATE = "lastLocationCoordinateOfDevice";

    private String LOG_TAG = PlantActivity.class.getSimpleName();

    private ActivityPlantBinding binding;

    private FusedLocationProviderClient fusedLocationClient;

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

        // TODO: Fix this code in future
        /*Intent intent = getIntent();
        if (intent.hasExtra(CameraActivity.OPEN_MY_PLANT_FRAGMENT_PRAM)) {
            if (intent.getIntExtra(CameraActivity.OPEN_MY_PLANT_FRAGMENT_PRAM, -1) == 3) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, MyPlantFragment.class, null)
                        .commit();
                bottomNavigationView.setSelectedItemId(R.id.my_plants);
            }
        }*/

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    LocationViewModel viewModel = ViewModelProvider
                            .AndroidViewModelFactory
                            .getInstance(this.getApplication())
                            .create(LocationViewModel.class);
                    viewModel.getCoordinate().observe(this, new Observer<String[]>() {
                        @Override
                        public void onChanged(String[] strings) {
                            Bundle coordinateBundle = new Bundle();
                            coordinateBundle.putStringArray(ARG_PARAM_COORDINATE, strings);
                            // Passing coordinate value to home fragment
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, HomeFragment.class, coordinateBundle)
                                    .commit();
                        }
                    });
                });
        permissionLauncher.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
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