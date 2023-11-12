package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.selection.databinding.ActivityAddCardBinding;

import java.util.ArrayList;

public class AddCardSelectCompany extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddCardBinding binding;
    private ArrayList<String> companyToEnrollList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.shinhanButton.setOnClickListener(this);
        binding.kookminButton.setOnClickListener(this);

        binding.goNextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == binding.goNextButton){
            Log.d("SMG", "YES");
            startActivity(new Intent(AddCardSelectCompany.this, AddCardSelectCard.class).putExtra("CompanyToEnrollList", companyToEnrollList));}
        else if(v == binding.shinhanButton){companyToEnrollList.add("신한");Log.d("SMG", "YES1");}
        else if(v == binding.kookminButton){companyToEnrollList.add(("KB국민"));Log.d("SMG", "YES2");}

    }


}