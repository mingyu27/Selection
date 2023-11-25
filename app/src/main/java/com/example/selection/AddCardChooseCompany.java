package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.selection.databinding.ActivityAddCardChooseCompanyBinding;

import java.util.ArrayList;

public class AddCardChooseCompany extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddCardChooseCompanyBinding binding;
    private ArrayList<String> companyToEnrollList = new ArrayList<>();
    private FunctionUser functionUser;
    private boolean isSelectedShinhan= false;
    private boolean isSelectedKookmin= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardChooseCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");

        binding.shinhanButton.setOnClickListener(this);
        binding.kookminButton.setOnClickListener(this);

        binding.goNextButton.setOnClickListener(this);
        binding.hyundaiButton.setOnClickListener(this);
        binding.nonghyupButton.setOnClickListener(this);
        binding.hanaButton.setOnClickListener(this);
        binding.lotteButton.setOnClickListener(this);
        binding.wooriButton.setOnClickListener(this);
        binding.bcButton.setOnClickListener(this);
        binding.samsungButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == binding.goNextButton){
            Log.d("SMG", "YES");
           // startActivity(new Intent(AddCardChooseCompany.this, AddCardChooseCard.class).putExtra("CompanyToEnrollList", companyToEnrollList).putExtra("functionUser", functionUser));
             }
        else if(v == binding.shinhanButton){
            if(isSelectedShinhan == false){companyToEnrollList.add("신한");binding.shinhanButton.setBackgroundResource(R.drawable.round_bold); isSelectedShinhan = true;}
            else{companyToEnrollList.remove("신한"); binding.shinhanButton.setBackgroundResource(R.drawable.round); isSelectedShinhan = false;}
        }
        else if(v == binding.kookminButton){
            if(isSelectedKookmin == false){companyToEnrollList.add("KB국민");binding.kookminButton.setBackgroundResource(R.drawable.round_bold); isSelectedKookmin = true;}
            else{companyToEnrollList.remove("KB국민"); binding.kookminButton.setBackgroundResource(R.drawable.round); isSelectedKookmin = false;}
        }
        else {
            Toast.makeText(this, "추후 지원될 예정입니다.", Toast.LENGTH_SHORT).show();
        }
    }


}