package com.sih23.plantdiseaseidentifiers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sih23.plantdiseaseidentifiers.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button signUpButton = binding.createAccountButton;
        Button loginButton = binding.loginBtn;
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start home activity after user login successfully
                Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(homeIntent);
                // Destroy current activity after user login successfully
                finish();
            }
        });
    }
}