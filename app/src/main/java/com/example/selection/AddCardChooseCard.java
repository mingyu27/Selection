package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

import com.example.selection.databinding.ActivityAddCardChooseCardBinding;

import java.util.ArrayList;

public class AddCardChooseCard extends AppCompatActivity {

    private ActivityAddCardChooseCardBinding binding;
    private ArrayList<String> companyToEnrollList;
    private String companyNameToEnroll;
    private String[] cardsToEnroll;
    private TypedArray typedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardChooseCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //카드명, 카드사진 초기화
        companyToEnrollList = (ArrayList<String>) getIntent().getSerializableExtra("CompanyToEnrollList");
        companyNameToEnroll = companyToEnrollList.remove(0);
        binding.companyNameText.setText(companyNameToEnroll);
        if(companyNameToEnroll.equals("신한")){
            cardsToEnroll = getResources().getStringArray(R.array.shinHanCardNameList);
            typedArray = getResources().obtainTypedArray(R.array.shinHanCardImageList);
        }
        else if (companyNameToEnroll.equals("KB국민")){
            cardsToEnroll = getResources().getStringArray(R.array.kookMinCardNameList);
            typedArray = getResources().obtainTypedArray(R.array.kookMinCardImageList);
        }


        NumberPicker pickCard = binding.cardSelectList;
        final String[] cardList = cardsToEnroll;
        pickCard.setMinValue(0);
        pickCard.setMaxValue(cardList.length-1);
        pickCard.setDisplayedValues(cardList);
        pickCard.setWrapSelectorWheel(false);


        pickCard.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("SMG", "" + newVal);
                binding.selectedCardImage.setImageResource(typedArray.getResourceId(newVal, -1));
            }
        });

        binding.goNextButton.setOnClickListener(view -> {
            Log.d("SMG", "CLIKE");
            if(companyToEnrollList.isEmpty()){
                startActivity(new Intent(AddCardChooseCard.this, MainActivity.class));
            }
            else {
                startActivity(new Intent(AddCardChooseCard.this, AddCardChooseCard.class).putExtra("CompanyToEnrollList", companyToEnrollList));
            }
        });


    }
}