package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.selection.databinding.ActivitySettingsPwChangeBinding;

public class SettingsPwChange extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivitySettingsPwChangeBinding binding = ActivitySettingsPwChangeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsPwChange.this, SettingsInformation.class));
            }
        });
    }
}