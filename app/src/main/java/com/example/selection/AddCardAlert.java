package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.selection.databinding.ActivityAddCardAlertBinding;

public class AddCardAlert extends AppCompatActivity {
    private ActivityAddCardAlertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goToEnrollCardButton.setOnClickListener(view -> {
            startActivity(new Intent(AddCardAlert.this, AddCardSelectCompany.class));
            finish();
        });
    }
}