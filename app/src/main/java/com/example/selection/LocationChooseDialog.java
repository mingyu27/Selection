package com.example.selection;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.example.selection.databinding.ActivityLocationChooseDialogBinding;

public class LocationChooseDialog extends Activity {
    private ActivityLocationChooseDialogBinding binding;
    String selectedBusinessType;
    String selectedStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationChooseDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NumberPicker pickBusinessType = binding.pickBusinessTypeList;
        NumberPicker pickStore = binding.pickStoreList;


        final String[] businessTypeList = {"카페", "일반음식점", "패스트푸드"};
        final String[] storeList = {"스타벅스 숭실대점", "이디야 숭실대점", "매머드커피 숭실대점"};

        pickBusinessType.setMinValue(0);
        pickBusinessType.setMaxValue(businessTypeList.length-1);
        pickStore.setMinValue(0);
        pickStore.setMaxValue(storeList.length-1);

        pickBusinessType.setDisplayedValues(businessTypeList);
        pickStore.setDisplayedValues(storeList);

        pickBusinessType.setWrapSelectorWheel(true);
        pickStore.setWrapSelectorWheel(true);


        pickBusinessType.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                binding.selectedResultText.setText(businessTypeList[newVal]);
                selectedBusinessType = businessTypeList[newVal];
            }
        });

        pickStore.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedStore = storeList[newVal];
                binding.selectedResultText.setText(selectedBusinessType + " " + selectedStore);
            }
        });



    }




}