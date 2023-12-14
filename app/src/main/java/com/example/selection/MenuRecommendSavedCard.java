package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;

import com.example.selection.databinding.ActivityMenuRecommendSavedCardBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuRecommendSavedCard extends AppCompatActivity {
    private FunctionUser functionUser;
    private FirebaseFirestore db;
    private FunctionCard bestBenefitAmountFunctionCard;
    private FunctionCard bestBenefitRateFunctionCard;
    private TypedArray typedArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMenuRecommendSavedCardBinding binding = ActivityMenuRecommendSavedCardBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        bestBenefitAmountFunctionCard = (FunctionCard) getIntent().getSerializableExtra("bestAmount");
        bestBenefitRateFunctionCard = (FunctionCard) getIntent().getSerializableExtra("bestRate");

        if(bestBenefitAmountFunctionCard!=null) {
            binding.CardImage.setImageResource(getIntent().getIntExtra("cardImageResourceID", -1)); //카드 이미지 가져오기
            binding.CardName.setText(getIntent().getStringExtra("cardName")); //카드 이름 가져오기
        }

        else if(bestBenefitRateFunctionCard!=null){
            binding.CardImage.setImageResource(getIntent().getIntExtra("cardImageResourceID", -1)); //카드 이미지 가져오기
            binding.CardName.setText(getIntent().getStringExtra("cardName")); //카드 이름 가져오기
        }
    }
}