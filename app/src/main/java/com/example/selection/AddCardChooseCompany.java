package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.selection.databinding.ActivityAddCardChooseCompanyBinding;

import java.util.ArrayList;

public class AddCardChooseCompany extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddCardChooseCompanyBinding binding;
    private ArrayList<String> companyToEnrollList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardChooseCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.shinhanButton.setOnClickListener(this);
        binding.kookminButton.setOnClickListener(this);

        binding.goNextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == binding.goNextButton){
            Log.d("SMG", "YES");
            startActivity(new Intent(AddCardChooseCompany.this, AddCardChooseCard.class).putExtra("CompanyToEnrollList", companyToEnrollList));}
        else if(v == binding.shinhanButton){companyToEnrollList.add("신한");binding.shinhanButton.setBackgroundColor(R.color.black);}
        else if(v == binding.kookminButton){companyToEnrollList.add(("KB국민"));binding.kookminButton.setBackgroundColor(R.color.black);}

    }


}