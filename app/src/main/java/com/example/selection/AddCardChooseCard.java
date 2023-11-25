//package com.example.selection;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.content.res.TypedArray;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.widget.NumberPicker;
//import android.widget.Toast;
//
//import com.example.selection.databinding.ActivityAddCardChooseCardBinding;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//
//public class AddCardChooseCard extends AppCompatActivity {
//
//    private ActivityAddCardChooseCardBinding binding;
//    private ArrayList<String> companyToEnrollList;
//    private String companyNameToEnroll;
//    private String[] cardsToEnroll;
//    private TypedArray typedArray;
//    private FunctionUser functionUser;
//    private int selectedCardIndex;
//    private ArrayList<Integer> savedCardIndexArrayList;
//    private static final String TAG = "SMG";
//    private FirebaseFirestore db;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAddCardChooseCardBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//
//
//        db = FirebaseFirestore.getInstance();
//        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
//        Log.d(TAG, functionUser.getName());
//
//
////        카드명, 카드사진 초기화
//        companyToEnrollList = (ArrayList<String>) getIntent().getSerializableExtra("CompanyToEnrollList");
//        companyNameToEnroll = companyToEnrollList.remove(0);
//        binding.companyNameText.setText(companyNameToEnroll);
//        if(companyNameToEnroll.equals("신한")){
//            cardsToEnroll = getResources().getStringArray(R.array.shinHanCardNameList);
//            typedArray = getResources().obtainTypedArray(R.array.shinHanCardImageList);
//        }
//        else if (companyNameToEnroll.equals("KB국민")){
//            cardsToEnroll = getResources().getStringArray(R.array.kookMinCardNameList);
//            typedArray = getResources().obtainTypedArray(R.array.kookMinCardImageList);
//        }
//
//        //현재 등록예정인 리스트에..functionUser의 기존 리스트를 불러오기..카드사별로
//        if(companyNameToEnroll.equals("신한")){savedCardIndexArrayList = functionUser.getSavedShinhan();}
//        else if(companyNameToEnroll.equals("KB국민")){savedCardIndexArrayList = functionUser.getSavedKookmin();}
//
//
//        NumberPicker pickCard = binding.cardSelectList;
//        final String[] cardList = cardsToEnroll;
//        pickCard.setMinValue(0);
//        pickCard.setMaxValue(cardList.length-1);
//        pickCard.setDisplayedValues(cardList);
//        pickCard.setWrapSelectorWheel(false);
//        pickCard.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                binding.selectedCardImage.setImageResource(typedArray.getResourceId(newVal, -1));
//                selectedCardIndex = newVal;
//                Log.d(TAG, selectedCardIndex + "");
//            }
//        });
//
//
//        binding.CardChooseEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                filterNumberPicker(s.toString());
//            }
//        });
//        private void filterNumberPicker(String query) {
//            // 검색어에 따라 NumberPicker 필터링
//            // 여기서는 간단한 예제로 모든 숫자를 보여줍니다.
//            // 실제로는 필터링 로직을 추가하여 검색에 따라 적절한 숫자만 보여주어야 합니다.
//            numberPicker.setMinValue(0);
//            numberPicker.setMaxValue(100);
//        }
//        binding.goNextButton.setOnClickListener(view -> {
//            //카드번호저장된 arraylist를 functionUser의 필드에 저장
//            if(companyNameToEnroll.equals("신한")){functionUser.setSavedShinhan(savedCardIndexArrayList);}
//            else if(companyNameToEnroll.equals("KB국민")){functionUser.setSavedKookmin(savedCardIndexArrayList);}
//
//
//            Log.d("SMG", "CLIKE");
//
//            //만약 저장이 다됐다면
//            if(companyToEnrollList.isEmpty()){
//                CollectionReference userReference = db.collection("User");
//                userReference.document((functionUser.getUid().contains("kakao"))?"kakao"+functionUser.getName():"email"+functionUser.getName()).set(functionUser);
//
//                startActivity(new Intent(AddCardChooseCard.this, MainActivity.class).putExtra("functionUser", functionUser));
//                Toast.makeText(this, "카드가 모두 정상적으로 등록됐습니다.", Toast.LENGTH_SHORT);
//            }
//            else {
//                startActivity(new Intent(AddCardChooseCard.this, AddCardChooseCard.class).putExtra("CompanyToEnrollList", companyToEnrollList).putExtra("functionUser", functionUser));
//            }
//        });
//
//        //임시리스트에 카드번호 추가
//        binding.saveCard.setOnClickListener(view -> {
//            savedCardIndexArrayList.add(selectedCardIndex);
//        });
//
//
//    }
//}