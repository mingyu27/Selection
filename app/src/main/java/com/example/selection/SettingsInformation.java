package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.selection.databinding.ActivitySettingsInformationBinding;

public class SettingsInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivitySettingsInformationBinding binding = ActivitySettingsInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsInformation.this, MainActivity.class));
            }
        });
        binding.PasswordChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsInformation.this, SettingsPwChange.class));
            }
        });
    }
}