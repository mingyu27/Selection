package com.example.selection;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selection.databinding.ActivityFunctionAddCardBenefitBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FunctionIOKookmin extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityFunctionAddCardBenefitBinding binding;
    private final String TAG = "SMG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFunctionAddCardBenefitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        addKookminCardData(
                0,
                "AK KB국민 체크카드",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=01736",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                false, false,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05
        );

        addKookminCardData(
                1,
                "H.Point 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=04906",
                false, false,
                true, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05
        );
        addKookminCardData(
                2,
                "kb pay 머니백카드",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=09308",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                3,
                "kb국민 국민행복체크",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=02066&solicitorcode=7030084002",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                4,
                "kb국민 우리동네 체크카드",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=09652&solicitorcode=7030084002",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                5,
                "kb국민 티머니노리 학생증 체크카드(그린)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                6,
                "kb국민 티머니노리 학생증체크카드(블루)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                7,
                "kb국민 티머니노리 학생증체크카드(세로형-그린)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                8,
                "kb국민 티머니노리 학생증체크카드(세로형-블루)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                9,
                "kb국민 티머니노리 학생증체크카드(세로형-퍼플)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                10,
                "kb국민 티머니노리 학생증체크카드(퍼플)",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                11,
                "lg u+체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01990",
                false, false,
                false, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1
        );
        addKookminCardData(
                12,
                "liiv M 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01772",
                false, false,
                false, false,
                false, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.03,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                13,
                "ONE 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01946",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                true, true,
                true, true,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.002,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.002,
                0, 0.0, 0, 0.002,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                14,
                "TMAP&LOGI행복체크카드",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                15,
                "Young Youth 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01936",
                false, false,
                false, false,
                true, true,
                false, false,
                true, false,
                true, true,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05
        );
        addKookminCardData(
                16,
                "가온 올포인트 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01998",
                false, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                17,
                "가온체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01720",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                true, true,
                true, true,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                18,
                "그린재킷 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09613",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                19,
                "나라사랑체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=04120",
                true, false,
                false, false,
                false, false,
                true, false,
                false, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                20,
                "노리2 체크카드(Global)",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=07972&solicitorcode=7030084002",
                true, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                15000, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.1, 0, 0.0,
                0, 0.05, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0
        );
        addKookminCardData(
                21,
                "노리2 체크카드(KB pay)",
                "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=07964&solicitorcode=7030084002",
                true, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                15000, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.1, 0, 0.0,
                0, 0.05, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0
        );
        addKookminCardData(
                22,
                "노리2 체크카드(Play)",
                "",
                true, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                15000, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.1, 0, 0.0,
                0, 0.05, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0
        );
        addKookminCardData(
                23,
                "노리체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01664",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                24,
                "누리체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09166",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                true, true,
                true, true,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                25,
                "락스타 체크카드",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                26,
                "레고랜드 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=02230",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                27,
                "리브 NEXT카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09608",
                false, false,
                false, false,
                false, false,
                true, true,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                28,
                "민 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01560",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                29,
                "비트윈 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01680",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.3,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.03,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.15,
                0, 0.0, 0, 0.2
        );
        addKookminCardData(
                30,
                "새로이 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01774",
                false, false,
                true, false,
                true, false,
                true, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                31,
                "샘쏘영 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01574",
                false, false,
                false, false,
                false, false,
                false, false,
                true, false,
                true, true,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                32,
                "스타체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01552",
                true, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.03,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 3000, 0.0
        );
        addKookminCardData(
                33,
                "스타플러스 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09487",
                true, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                150000, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 4000, 0.0
        );
        addKookminCardData(
                34,
                "쏘영 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01570",
                false, false,
                false, false,
                false, false,
                false, false,
                true, false,
                true, true,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                35,
                "아모레퍼시픽 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01996",
                false, false,
                false, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.1, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                36,
                "아시아나 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01974",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                37,
                "알뜰교통플러스 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09322",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                38,
                "위글위글 첵첵 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01918",
                false, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0,
                4000, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 4000, 0.0
        );
        addKookminCardData(
                39,
                "위메프페이 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09620",
                false, false,
                true, true,
                false, false,
                true, true,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                40,
                "음 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01932",
                false, false,
                true, true,
                false, false,
                true, false,
                false, false,
                false, false,
                true, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                41,
                "정 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01928",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                42,
                "직장인 보너스체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01690",
                true, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                true, false,
                false, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                43,
                "청춘대로 싱글 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01988",
                false, false,
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                44,
                "총무 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=07981",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                false, false,
                true, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                45,
                "카카오페이kb국민체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01782",
                false, false,
                true, true,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.01,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.01,
                0, 0.0, 0, 0.01,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                46,
                "탐나는전 체크카드",
                "",
                false, false,
                false, false,
                false, false,
                true, true,
                false, false,
                true, true,
                true, true,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.004,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                47,
                "토심이 첵첵 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=07960",
                false, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                false, false,
                true, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 4000, 0.0,
                0, 0.0, 4000, 0.0,
                0, 0.0, 4000, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 4000, 0.0
        );
        addKookminCardData(
                48,
                "티머니체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01699",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.3,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.03,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 0, 0.2
        );
        addKookminCardData(
                49,
                "펭수노리 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01908",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                50,
                "포인트리체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01556",
                true, false,
                false, false,
                true, false,
                false, false,
                false, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.03,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.1,
                0, 0.0, 3000, 0.0
        );
        addKookminCardData(
                51,
                "해외에선 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01582",
                false, false,
                false, false,
                false, false,
                true, false,
                true, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                52,
                "해피cu포인트 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01754",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                53,
                "해피노리체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01670",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                54,
                "해피락스타 체크카드",
                "",
                true, false,
                false, false,
                true, false,
                true, false,
                true, false,
                false, false,
                true, false,
                true, false,
                0, 0.0, 0, 0.5,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.2,
                0, 0.0, 0, 0.35
        );
        addKookminCardData(
                55,
                "해피리워드 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=09636",
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
        addKookminCardData(
                56,
                "훈 체크카드",
                "https://m.kbcard.com/CRD/DVIEW/MCAMCXHIACRC0002?mainCC=b&allianceCode=01930",
                false, false,
                false, false,
                true, true,
                false, false,
                false, false,
                false, false,
                false, false,
                false, false,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.05,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0,
                0, 0.0, 0, 0.0
        );
//        getKookminCardDataToLog(0);
        getKookminCardDataToFunctionCard(0);
    }


    private void getKookminCardDataToLog(int index){
        db.collection("Kookmin")
                .whereEqualTo("cardIndex", index)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
    private void getKookminCardDataToFunctionCard(int index){

        db.collection("Kookmin")
                .whereEqualTo("cardIndex", index)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FunctionCard functionCard = document.toObject(FunctionCard.class);

//                            functionCard.setAmusementDiscount(getFunctionSpecificDiscount(document, "amusementDiscount"));
//                            functionCard.setBakeryDiscount(getFunctionSpecificDiscount(document, "bakeryDiscount"));
//                            functionCard.setBookstoreDiscount(getFunctionSpecificDiscount(document, "bookstoreDiscount"));
//                            functionCard.setCafeDiscount(getFunctionSpecificDiscount(document, "cafeDiscount"));
//                            functionCard.setConvenientStoreDiscount(getFunctionSpecificDiscount(document, "convenientStoreDiscount"));
//                            functionCard.setFastFoodDiscount(getFunctionSpecificDiscount(document, "fastFoodDiscount"));
//                            functionCard.setRestaurantDiscount(getFunctionSpecificDiscount(document, "restaurantDiscount"));
                            try {
                                functionCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                            Log.d(TAG, "cardName = " + functionCard.getCardApplicationLink());
                            Log.d(TAG, "cgv캐시백 = " + functionCard.getTheaterDiscount().get(0).getCashbackRate());

                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


    }



    private ArrayList<FunctionLocationSpecificDiscount> getFunctionSpecificDiscountArrayList(QueryDocumentSnapshot document, String specificDiscount) throws Exception {
        ArrayList<FunctionLocationSpecificDiscount> functionLocationSpecificDiscountArrayList = new ArrayList<>();
        switch (specificDiscount){
//            case "amusementDiscount":
//                break;
//            case "bakeryDiscount":
//                break;
//            case "bookstoreDiscount":
//                break;
//            case "cafeDiscount":
//                break;
//            case "convenientStoreDiscount":
//                break;
//            case "fastFoodDiscount":
//                break;
//            case "restaurantDiscount":
//                break;
            case "theaterDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "CGV"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "메가박스"));
                break;

        }

        return functionLocationSpecificDiscountArrayList;
    }
    private FunctionLocationSpecificDiscount getFunctionSpecificDiscount(QueryDocumentSnapshot queryDocumentSnapshot, String specificDiscount, String locationName) throws Exception {

        Task<DocumentSnapshot> task = queryDocumentSnapshot.getReference().collection(specificDiscount).document(locationName).get();
        while(!task.isComplete()){Thread.sleep(100);}

        // 작업이 성공적으로 완료됐는지 확인
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                return document.toObject(FunctionLocationSpecificDiscount.class);
            } else {
                throw new Exception("Document not found");
            }
        } else {
            throw new Exception("Error getting document", task.getException());
        }











    }

    private void addKookminCardData(
            int cardIndex, String cardName, String cardApplicationLink,
            boolean ifDiscountAmusement,boolean ifDiscountAmusementAll,
            boolean ifDiscountBakery, boolean ifDiscountBakeryAll,
            boolean ifDiscountBookStore, boolean ifDiscountBookStoreAll,
            boolean ifDiscountCafe, boolean ifDiscountCafeAll,
            boolean ifDiscountConvenientStore, boolean ifDiscountConvenientStoreAll,
            boolean ifDiscountFastFood, boolean ifDiscountFastFoodAll,
            boolean ifDiscountRestaurant, boolean ifDiscountRestaurantAll,
            boolean ifDiscountTheater, boolean ifDiscountTheaterAll,
            int amusementDiscountAmount, double amusementDiscountRate, int amusementCashbackAmount, double amusementCashbackRate,
            int bakeryDiscountAmount, double bakeryDiscountRate, int bakeryCashbackAmount, double bakeryCashbackRate,
            int bookStoreDiscountAmount, double bookStoreDiscountRate, int bookStoreCashbackAmount, double bookStoreCashbackRate,
            int cafeDiscountAmount, double cafeDiscountRate, int cafeCashbackAmount, double cafeCashbackRate,
            int convenientStoreDiscountAmount, double convenientStoreDiscountRate, int convenientStoreCashbackAmount, double convenientStoreCashbackRate,
            int fastFoodDiscountAmount, double fastFoodDiscountRate, int fastFoodCashbackAmount, double fastFoodCashbackRate,
            int restaurantDiscountAmount, double restaurantDiscountRate, int restaurantCashbackAmount, double restaurantCashbackRate,
            int theaterDiscountAmount, double theaterDiscountRate, int theaterCashbackAmount, double theaterCashbackRate) {

        //신한collection >> cardName문서 저장
        CollectionReference cardCompanyRef = db.collection("Kookmin");
        FunctionCard functionCard = new FunctionCard(
                cardIndex, cardName, cardApplicationLink,
                ifDiscountAmusement, ifDiscountAmusementAll,
                ifDiscountBakery, ifDiscountBakeryAll,
                ifDiscountBookStore, ifDiscountBookStoreAll,
                ifDiscountCafe, ifDiscountCafeAll,
                ifDiscountConvenientStore, ifDiscountConvenientStoreAll,
                ifDiscountFastFood, ifDiscountFastFoodAll,
                ifDiscountRestaurant, ifDiscountRestaurantAll,
                ifDiscountTheater, ifDiscountTheaterAll);
        cardCompanyRef.document(String.valueOf(cardIndex)).set(functionCard);
        setSubCollections(cardCompanyRef, cardIndex,
                ifDiscountAmusementAll, ifDiscountBakeryAll,
                ifDiscountBookStoreAll, ifDiscountCafeAll,
                ifDiscountConvenientStoreAll, ifDiscountFastFoodAll,
                ifDiscountRestaurantAll, ifDiscountTheaterAll,
                amusementDiscountAmount, amusementDiscountRate, amusementCashbackAmount, amusementCashbackRate,
                bakeryDiscountAmount, bakeryDiscountRate, bakeryCashbackAmount, bakeryCashbackRate,
                bookStoreDiscountAmount, bookStoreDiscountRate, bookStoreCashbackAmount, bookStoreCashbackRate,
                cafeDiscountAmount, cafeDiscountRate, cafeCashbackAmount, cafeCashbackRate,
                convenientStoreDiscountAmount, convenientStoreDiscountRate, convenientStoreCashbackAmount, convenientStoreCashbackRate,
                fastFoodDiscountAmount, fastFoodDiscountRate, fastFoodCashbackAmount,fastFoodCashbackRate,
                restaurantDiscountAmount, restaurantDiscountRate, restaurantCashbackAmount, restaurantCashbackRate,
                theaterDiscountAmount, theaterDiscountRate, theaterCashbackAmount, theaterCashbackRate
        );


    }

    private Map<String, Object> makeSpecificDiscountInfo(
            int discountAmount, double discountRate,
            int cashbackAmount, double cashbackRate){
        Map<String, Object> specificDiscountInfo = new HashMap<>();
        specificDiscountInfo.put("discountAmount", discountAmount);
        specificDiscountInfo.put("discountRate", discountRate);
        specificDiscountInfo.put("cashbackAmount", cashbackAmount);
        specificDiscountInfo.put("cashbackRate", cashbackRate);
        specificDiscountInfo.put("ifLimitDayOfWeek", false);
        specificDiscountInfo.put("ifLimitTime", false);
        specificDiscountInfo.put("validDayOfWeek", Arrays.asList(""));
        specificDiscountInfo.put("validTime", Arrays.asList(""));
        specificDiscountInfo.put("details", "");



        return specificDiscountInfo;
    }


    private void setSubDocuments(CollectionReference cardCompanyRef, String cardIndex, String discountCategory, int discountAmount, double discountRate, int cashbackAmount, double cashbackRate){
        switch(discountCategory){
            case "amusementDiscount":
                cardCompanyRef.document(cardIndex).collection("amusementDiscount").document("캐리비안베이").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("amusementDiscount").document("서울랜드").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("amusementDiscount").document("롯데월드").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("amusementDiscount").document("에버랜드").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("amusementDiscount").document("레고랜드").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "bakeryDiscount":
                cardCompanyRef.document(cardIndex).collection("bakeryDiscount").document("파리바게뜨").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("bakeryDiscount").document("파리크라상").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("bakeryDiscount").document("뚜레쥬르").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("bakeryDiscount").document("던킨").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "bookStoreDiscount":
                cardCompanyRef.document(cardIndex).collection("bookStoreDiscount").document("교보문고").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("bookStoreDiscount").document("yes24").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "cafeDiscount" :
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("스타벅스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("빽다방").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("이디야").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("투썸플레이스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("커피빈").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("엔제리너스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("배스킨라빈스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("cafeDiscount").document("카페베네").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));


            case "convenientStoreDiscount":
                cardCompanyRef.document(cardIndex).collection("convenientStoreDiscount").document("CU").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("convenientStoreDiscount").document("세븐일레븐").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("convenientStoreDiscount").document("이마트24").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("convenientStoreDiscount").document("GS25").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "fastFoodDiscount":
                cardCompanyRef.document(cardIndex).collection("fastFoodDiscount").document("KFC").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("fastFoodDiscount").document("버거킹").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("fastFoodDiscount").document("롯데리아").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "restaurantDiscount":
                cardCompanyRef.document(cardIndex).collection("restaurantDiscount").document("빕스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("restaurantDiscount").document("아웃백").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("restaurantDiscount").document("계절밥상").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;
            case "theaterDiscount":
                cardCompanyRef.document(cardIndex).collection("theaterDiscount").document("CGV").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("theaterDiscount").document("메가박스").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                cardCompanyRef.document(cardIndex).collection("theaterDiscount").document("롯데시네마").set(makeSpecificDiscountInfo(discountAmount, discountRate, cashbackAmount, cashbackRate));
                break;







        }
    }
    private void setSubCollections(
            CollectionReference cardCompanyRef, int num,
            boolean ifDiscountAmusementAll, boolean ifDiscountBakeryAll,
            boolean ifDiscountBookStoreAll, boolean ifDiscountCafeAll,
            boolean ifDiscountConvenientStoreAll, boolean ifDiscountFastFoodAll,
            boolean ifDiscountRestaurantAll, boolean ifDiscountTheaterAll,
            int amusementDiscountAmount, double amusementDiscountRate, int amusementCashbackAmount, double amusementCashbackRate,
            int bakeryDiscountAmount, double bakeryDiscountRate, int bakeryCashbackAmount, double bakeryCashbackRate,
            int bookStoreDiscountAmount, double bookStoreDiscountRate, int bookStoreCashbackAmount, double bookStoreCashbackRate,
            int cafeDiscountAmount, double cafeDiscountRate, int cafeCashbackAmount, double cafeCashbackRate,
            int convenientStoreDiscountAmount, double convenientStoreDiscountRate, int convenientStoreCashbackAmount, double convenientStoreCashbackRate,
            int fastFoodDiscountAmount, double fastFoodDiscountRate, int fastFoodCashbackAmount, double fastFoodCashbackRate,
            int restaurantDiscountAmount, double restaurantDiscountRate, int restaurantCashbackAmount, double restaurantCashbackRate,
            int theaterDiscountAmount, double theaterDiscountRate, int theaterCashbackAmount, double theaterCashbackRate) {


        String cardIndex = String.valueOf(num);
        if(ifDiscountAmusementAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"amusementDiscount", amusementDiscountAmount, amusementDiscountRate, amusementCashbackAmount, amusementCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"amusementDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountBakeryAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"bakeryDiscount", bakeryDiscountAmount, bakeryDiscountRate, bakeryCashbackAmount, bakeryCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"bakeryDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountBookStoreAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"bookStoreDiscount", bookStoreDiscountAmount, bookStoreDiscountRate, bookStoreCashbackAmount, bookStoreCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"bookStoreDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountCafeAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"cafeDiscount", cafeDiscountAmount, cafeDiscountRate, cafeCashbackAmount, cafeCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"cafeDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountConvenientStoreAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"convenientStoreDiscount", convenientStoreDiscountAmount, convenientStoreDiscountRate, convenientStoreCashbackAmount, convenientStoreCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"convenientStoreDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountFastFoodAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"fastFoodDiscount", fastFoodDiscountAmount, fastFoodDiscountRate, fastFoodCashbackAmount, fastFoodCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"fastFoodDiscount", 0, 0.0, 0, 0.0);}

        if(ifDiscountRestaurantAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"restaurantDiscount", restaurantDiscountAmount, restaurantDiscountRate, restaurantCashbackAmount, restaurantCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"restaurantDiscount", 0, 0.0, 0, 0.0);}


        if(ifDiscountTheaterAll == true) {setSubDocuments(cardCompanyRef, cardIndex,"theaterDiscount", theaterDiscountAmount, theaterDiscountRate, theaterCashbackAmount, theaterCashbackRate);}
        else{setSubDocuments(cardCompanyRef, cardIndex,"theaterDiscount", 0, 0.0, 0, 0.0);}


    }




}