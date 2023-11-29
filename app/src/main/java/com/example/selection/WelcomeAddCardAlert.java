package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.selection.databinding.ActivityAddCardAlertBinding;

public class WelcomeAddCardAlert extends AppCompatActivity {
    private ActivityAddCardAlertBinding binding;
    private FunctionUser functionUser;
    private final String TAG = "SMG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
        Log.d(TAG, functionUser.getName());


        binding.goToEnrollCardButton.setOnClickListener(view -> {
            startActivity(new Intent(WelcomeAddCardAlert.this, WelcomeAddCardChooseCompany.class).putExtra("functionUser", functionUser));
            finish();
        });

        binding.cancelGoToMainAcitivy.setOnClickListener(view -> {
            startActivity(new Intent(WelcomeAddCardAlert.this, MainActivity.class).putExtra("functionUser", functionUser));
            finish();
        });
    }
}