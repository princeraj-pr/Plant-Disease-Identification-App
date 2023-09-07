package com.sih23.plantdiseaseidentifiers;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.sih23.plantdiseaseidentifiers.databinding.ActivityLoginBinding;
import com.sih23.plantdiseaseidentifiers.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup toolbar
        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AppBarLayout appBarLayout = binding.appBarLayout;
        appBarLayout.setStatusBarForeground(
                MaterialShapeDrawable.createWithElevationOverlay(this));

        Button switchAccButton = binding.switchCreateAccBtn;
        switchAccButton.setOnClickListener(new View.OnClickListener() {
            boolean fragmentState = false;
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentState) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, UserSignUpFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // Name can be null
                            .commit();
                    fragmentState = false;
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ExpertSignUpFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // Name can be null
                            .commit();
                    fragmentState = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Back to Login activity (finish activity)
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}