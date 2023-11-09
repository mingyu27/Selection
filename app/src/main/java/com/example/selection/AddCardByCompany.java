package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

import com.example.selection.databinding.ActivityAddCardByCompanyBinding;

public class AddCardByCompany extends AppCompatActivity {

    private ActivityAddCardByCompanyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardByCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NumberPicker pickCard = binding.cardSelectList;

        final String[] cardList = getResources().getStringArray(R.array.shinHanCardNameList);
        pickCard.setMinValue(0);
        pickCard.setMaxValue(cardList.length-1);
        pickCard.setDisplayedValues(cardList);
        pickCard.setWrapSelectorWheel(true);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.shinHanCardImageList);

        pickCard.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("SMG", "" + newVal);
                binding.selectedCardImage.setImageResource(typedArray.getResourceId(newVal, -1));
            }
        });


    }
}