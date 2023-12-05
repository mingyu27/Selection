package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.selection.databinding.ActivityAddCardChooseCardBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomeAddCardChooseCard extends AppCompatActivity {

    private ActivityAddCardChooseCardBinding binding;
    private ArrayList<String> companyToEnrollList;
    private String companyNameToEnroll;
    private String[] cardsToEnroll;
    private TypedArray typedArray;
    private FunctionUser functionUser;
    private int selectedCardIndex;
    private ArrayList<Integer> savedCardIndexArrayList;
    private static final String TAG = "SMG";
    private FirebaseFirestore db;
    private LinearLayout linearLayout;
    private EditText cardChooseEditText;
    private NumberPicker cardSelectList;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardChooseCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        linearLayout = binding.linearLayoutForSavedCard;
        db = FirebaseFirestore.getInstance();
        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
        cardChooseEditText = findViewById(R.id.Card_Choose_EditText);
        cardSelectList = findViewById(R.id.card_select_list);
        Log.d(TAG, functionUser.getName() + "at WelcomeAddCardChooseCard");


//        카드명, 카드사진 초기화
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

        //현재 등록예정인 리스트에..functionUser의 기존 리스트를 불러오기..카드사별로
        if(companyNameToEnroll.equals("신한")){savedCardIndexArrayList = functionUser.getSavedShinhan(); savedCardIndexArrayList = new ArrayList<>(new HashSet<>(savedCardIndexArrayList));}
        else if(companyNameToEnroll.equals("KB국민")){savedCardIndexArrayList = functionUser.getSavedKookmin(); savedCardIndexArrayList = new ArrayList<>(new HashSet<>(savedCardIndexArrayList));}


        NumberPicker pickCard = binding.cardSelectList;
        final String[] cardList = cardsToEnroll;
        pickCard.setMinValue(0);
        pickCard.setMaxValue(cardList.length-1);
        pickCard.setDisplayedValues(cardList);
        pickCard.setWrapSelectorWheel(false);


        pickCard.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                binding.selectedCardImage.setImageResource(typedArray.getResourceId(newVal, -1));
                selectedCardIndex = newVal;
            }
        });

        binding.goNextButton.setOnClickListener(view -> {
            //카드번호저장된 arraylist를 functionUser의 필드에 저장
            if(companyNameToEnroll.equals("신한")){functionUser.setSavedShinhan(savedCardIndexArrayList);}
            else if(companyNameToEnroll.equals("KB국민")){functionUser.setSavedKookmin(savedCardIndexArrayList);}
            //만약 저장이 다됐다면
            if(companyToEnrollList.isEmpty()){
                CollectionReference userReference = db.collection("User");
                startActivity(new Intent(WelcomeAddCardChooseCard.this, MainActivity.class).putExtra("functionUser", functionUser).putExtra("isJustStarted", true));
                Toast.makeText(this, "카드가 모두 정상적으로 등록됐습니다.", Toast.LENGTH_SHORT).show();
                userReference.document((functionUser.getUid().contains("kakao"))?"kakao"+functionUser.getName():"email"+functionUser.getName()).set(functionUser);
            }
            else {
                startActivity(new Intent(WelcomeAddCardChooseCard.this, WelcomeAddCardChooseCard.class).putExtra("CompanyToEnrollList", companyToEnrollList).putExtra("functionUser", functionUser));
            }
        });

        //카드추가 버튼 누를시
        binding.saveCard.setOnClickListener(view -> {
            //임시리스트에 카드번호 추가
            if (savedCardIndexArrayList.contains(selectedCardIndex)) {
                // 이미 추가된 카드라면 사용자에게 알림
                Toast.makeText(WelcomeAddCardChooseCard.this, "이미 추가된 카드입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                savedCardIndexArrayList.add(selectedCardIndex);
                //하단에 카드사진 추가
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(5, 0, 0, 0);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageResource(typedArray.getResourceId(selectedCardIndex, -1));
                imageView.setAdjustViewBounds(true);
                linearLayout.addView(imageView);

//              db가져오는작업..rxjava로 빼기
//              추가된카드의 혜택정보얻어오기위해
//              firestore에서 해당 인덱스에 해당하는 카드 찾아서
//                Observable.fromCallable(() -> {
//                            // Firestore에서 데이터 가져오기
//                            return db.collection(companyNameToEnroll.equals("신한") ? "Shinhan" : "Kookmin")
//                                    .whereEqualTo("cardIndex", selectedCardIndex)
//                                    .get()
//                                    .getResult();
//                        })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .subscribe(
//                                result -> {
//                                    // 비동기 작업이 성공적으로 완료된 경우
//                                    for (QueryDocumentSnapshot document : result) {
//                                        FunctionCard functionCard = document.toObject(FunctionCard.class);
//                                        Log.d(TAG, "추가된카드 = " + functionCard.getCardName());
//                                        try {
//                                            functionCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
//                                            functionCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
//                                            functionCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
//                                            functionCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
//                                            functionCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
//                                            functionCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
//                                            functionCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
//                                            functionCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
//                                        } catch (Exception e) {
//                                            throw new RuntimeException(e);
//                                        }
//
//                                        // 작업 결과를 처리
//                                        if(functionCard.isIfDiscountAmusement()){functionUser.setAvailableDiscountAmusement(true);}
//                                        if(functionCard.isIfDiscountBakery()){functionUser.setAvailableDiscountBakery(true);}
//                                        if(functionCard.isIfDiscountBookStore()){functionUser.setAvailableDiscountBookStore(true);}
//                                        if(functionCard.isIfDiscountCafe()){functionUser.setAvailableDiscountCafe(true);}
//                                        if(functionCard.isIfDiscountConvenientStore()){functionUser.setAvailableDiscountConvenientStore(true);}
//                                        if(functionCard.isIfDiscountFastFood()){functionUser.setAvailableDiscountFastFood(true);}
//                                        if(functionCard.isIfDiscountRestaurant()){functionUser.setAvailableDiscountRestaurant(true);}
//                                        if(functionCard.isIfDiscountTheater()){functionUser.setAvailableDiscountTheater(true);}
//
//                                    }
//                                },
//                                throwable -> {
//                                    // 비동기 작업이 실패한 경우
//                                    Log.d(TAG, "Error getting documents: ", throwable);
//                                }
//                        );

                new MyAsyncTask().execute();
            }
        });
        cardChooseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCardList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void filterCardList(String filterText) {
        String[] cardList = cardsToEnroll;

        ArrayList<String> filteredList = new ArrayList<>();
        for (String card : cardList) {
            if (card.toLowerCase().contains(filterText.toLowerCase())) {
                filteredList.add(card);
            }
        }

        if (!filteredList.isEmpty()) {
            cardSelectList.setMinValue(0);
            cardSelectList.setMaxValue(filteredList.size() - 1);
            cardSelectList.setDisplayedValues(filteredList.toArray(new String[0]));
            cardSelectList.setWrapSelectorWheel(false);
        } else {
            // If filteredList is empty, reset the NumberPicker
            cardSelectList.setMinValue(0);
            cardSelectList.setMaxValue(0);
            cardSelectList.setDisplayedValues(null);
        }
    }
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // 백그라운드에서 실행할 작업을 수행합니다.
            db.collection(companyNameToEnroll.equals("신한") ? "Shinhan" : "Kookmin")
                    .whereEqualTo("cardIndex", selectedCardIndex)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FunctionCard functionCard = document.toObject(FunctionCard.class);
                                Log.d(TAG, "추가된카드 = " + functionCard.getCardName());
                                try {
                                    functionCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
                                    functionCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
                                    functionCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
                                    functionCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
                                    functionCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
                                    functionCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
                                    functionCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
                                    functionCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                                if (functionCard.isIfDiscountAmusement()) {
                                    functionUser.setAvailableDiscountAmusement(true);
                                }
                                if (functionCard.isIfDiscountBakery()) {
                                    functionUser.setAvailableDiscountBakery(true);
                                }
                                if (functionCard.isIfDiscountBookStore()) {
                                    functionUser.setAvailableDiscountBookStore(true);
                                }
                                if (functionCard.isIfDiscountCafe()) {
                                    functionUser.setAvailableDiscountCafe(true);
                                }
                                if (functionCard.isIfDiscountConvenientStore()) {
                                    functionUser.setAvailableDiscountConvenientStore(true);
                                }
                                if (functionCard.isIfDiscountFastFood()) {
                                    functionUser.setAvailableDiscountFastFood(true);
                                }
                                if (functionCard.isIfDiscountRestaurant()) {
                                    functionUser.setAvailableDiscountRestaurant(true);
                                }
                                if (functionCard.isIfDiscountTheater()) {
                                    functionUser.setAvailableDiscountTheater(true);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
            return null;
        }
    }
    private ArrayList<FunctionLocationSpecificDiscount> getFunctionSpecificDiscountArrayList(QueryDocumentSnapshot document, String specificDiscount) throws Exception {
        ArrayList<FunctionLocationSpecificDiscount> functionLocationSpecificDiscountArrayList = new ArrayList<>();
        switch (specificDiscount){
            case "amusementDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "롯데월드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "서울랜드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "캐리비안베이"));
                break;
            case "bakeryDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "뚜레쥬르"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "파리바게뜨"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "파리크라상"));
                break;
            case "bookstoreDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "yes24"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "교보문고"));
                break;
            case "cafeDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "빽다방"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "스타벅스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "이디야"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "커피빈"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "투썸플레이스"));
                break;
            case "convenientStoreDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "CU"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "GS25"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "세븐일레븐"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "이마트24"));
                break;
            case "fastFoodDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "KFC"));
                break;
            case "restaurantDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "계절밥상"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "빕스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "아웃백"));
                break;
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

        // 작업이 성공적으로 완료됐으면
        // FunctionLocationSpecificDiscount 객체로 return
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                return document.toObject(FunctionLocationSpecificDiscount.class);
            } else {
                Log.d(TAG, "Document not found" + locationName);
                throw new Exception("Document not found");

            }
        } else {
            Log.d(TAG, "Error getting document" + locationName);
            throw new Exception("Error getting document", task.getException());
        }
    }
}