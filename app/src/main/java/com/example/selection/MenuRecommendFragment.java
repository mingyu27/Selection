package com.example.selection;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.selection.databinding.FragmentMenuRecommendBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MenuRecommendFragment extends Fragment {

    private FragmentMenuRecommendBinding binding;
    private String TAG = "SMG";
    private String storeName = "위치를 선택해주세요";
    private String category = "카테고리를 선택해주세요";
    private String camelTypeCategory;

    private boolean isLocationSet = false;
    private MainActivity mainActivity;
    private FunctionUser functionUser;
    private FirebaseFirestore db;
    private List<Integer> savedShinhanCardIndexArrayList = new ArrayList<>();
    private List<Integer> savedKookminCardIndexArrayList = new ArrayList<>();
    private List<FunctionCard> discountCategoryAllSavedShinhanCard = new ArrayList<>();
    private List<FunctionCard> discountCategoryAllSavedKookminCard = new ArrayList<>();
    private List<FunctionCard> discountCategoryPartSavedShinhanCard = new ArrayList<>();
    private List<FunctionCard> discountCategoryPartSavedKookminCard = new ArrayList<>();
    private FunctionCard bestFunctionCard;
    private FunctionCard alterFunctionCard;
    private String bestFunctionCardCompany; private int bestFunctionCardIndex;
    private String alterFunctionCardCompany; private int alterFunctionCardIndex;
    private double bestDiscountRate = 0.0; private int bestDiscountAmount = 0; private double bestCashbackRate = 0.0; private int bestCashbackAmount = 0;
    private double alterDiscountRate = 0.0; private int alterDiscountAmount = 0; private double alterCashbackRate = 0.0; private int alterCashbackAmount = 0;
    public MenuRecommendFragment() {
        // Required empty public constructor
    }
    //MenuRecommendFragment생성할때,, functionUser매겨변수로 받아와서,, 유저포함한 fragment생성해서 return
    public static MenuRecommendFragment newInstance(FunctionUser functionUser) {
        MenuRecommendFragment fragment = new MenuRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionUser",functionUser);
        fragment.setArguments(bundle);
        return fragment;
    }
    //activity에 붙을때는 부모 액티비티 주소만 받아올것
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        db = FirebaseFirestore.getInstance();
    }
    //fragemnt 보여지기전에,,안에 들어있는것들 꺼내보기
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MenuRecommendFragment onCreate Started");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //매장이름 넣기
            try{
                String str;
                str = getArguments().getString("storeName");
                if(str != null){storeName = str; isLocationSet = true;}

            } catch(NullPointerException e){}
            //FunctionUser넣기
            try{
                FunctionUser tempUser;
                tempUser = (FunctionUser) getArguments().getSerializable("functionUser");
                if(tempUser != null){functionUser = tempUser; Log.d(TAG, functionUser.getName() + "at MenuRecommendFragment");}
            }catch (NullPointerException e){}
            try{
                String str;
                str = getArguments().getString("category");
                if(str != null){category = str; isLocationSet = true; }
            }catch (NullPointerException e){}
        }
        Log.d(TAG, "MenuRecommendFragment onCreate ended with " + functionUser.getName() + ", " + storeName + ", " + category );
    }
    //완성(리스너달아서,, 화면으로 보여지는 것은 이 과정에서 다 처리할것)해서,, 뷰를 보여지게할것
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "MenuRecommendFragment onCreateView");
        binding = FragmentMenuRecommendBinding.inflate(inflater, container, false);
        binding.pay1Card.setOnClickListener(view-> {startActivity(new Intent(getActivity(), MenuRecommendSavedCard.class).putExtra("payPriority", 1));});
        binding.pay2Card.setOnClickListener(view-> {startActivity(new Intent(getActivity(), MenuRecommendSavedCard.class).putExtra("payPriority", 2));});
        binding.recommendNewCardView.setOnClickListener(view-> {startActivity(new Intent(getActivity(), MenuRecommendNewCard.class).putExtra("payPriority", 2));});
        binding.selectedLocation.setOnClickListener(view -> {mainActivity.startDialog();});
        binding.selectedLocation.setText(storeName);
        //다이얼로그에서 장소 받아오면
        if(isLocationSet) {
            //해당카테고리 할인 가능한 functionUser
            if(isAvailableDiscountCategory(category)){
                Log.d(TAG, "MenuRecommendFragment onCreateView " + category + " is AvailableDiscountCategory");
                //보유중인 카드 인덱스로 카드 추출;
                savedShinhanCardIndexArrayList = functionUser.getSavedShinhan();
                savedKookminCardIndexArrayList = functionUser.getSavedKookmin();

                Log.d("SMG1", "보유중인 신한카드");
                for(Integer integer : savedShinhanCardIndexArrayList){
                    Log.d("SMG1", integer + " ");
                }

                Log.d("SMG1", "보유중인 국민카드");
                for(Integer integer : savedKookminCardIndexArrayList){
                    Log.d("SMG1", integer + " ");
                }

                //보유중인 Shinhan카드 중,,discountAll, discountPart
                for(int i = 0; i < savedShinhanCardIndexArrayList.size(); i++){
                    //해당카테고리 모두할인되는 카드 저장
                    //저장된 신한카드중,,i번째 카드가 해당카테고리 전부할인이면,,i번째 카드정보를,,FunctionCard tempCard에 저장,,상세카드혜택(amuse~theater)도 저장
                    db.collection("Shinhan")
                            .whereEqualTo("cardIndex", savedShinhanCardIndexArrayList.get(i))
                            .whereEqualTo("ifDiscount"+ camelTypeCategory+"All", true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            FunctionCard tempCard = document.toObject(FunctionCard.class);

                                            //상세카드 할인혜택필드도 완성할것,,이게좀 오래걸리는것같음,,functionLocationSpecificDiscountArrayList을 받아와서,,amuse~theater필드를 완성함
                                            try {
                                                tempCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }

                                            discountCategoryAllSavedShinhanCard.add(tempCard);
                                            Log.d("SMG1", "신한모두할인되는 "+document.toObject(FunctionCard.class).getCardIndex() + " 저장함");

                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    //해당카테고리 일부만할인되는 카드 저장
                    db.collection("Shinhan")
                            .whereEqualTo("cardIndex", savedShinhanCardIndexArrayList.get(i))
                            .whereEqualTo("ifDiscount"+ camelTypeCategory+"All", false)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            FunctionCard tempCard = document.toObject(FunctionCard.class);

                                            //상세카드 할인혜택필드도 완성할것
                                            try {
                                                tempCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }

                                            discountCategoryPartSavedShinhanCard.add(tempCard);
                                            Log.d("SMG1", "신한일부할인되는 "+document.toObject(FunctionCard.class).getCardIndex() + " 저장함");
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
                //보유중인 Kookmin카드 중,,discountAll, discountPart
                for(int i = 0; i < savedKookminCardIndexArrayList.size(); i++) {
                    db.collection("Kookmin")
                            .whereEqualTo("cardIndex", savedKookminCardIndexArrayList.get(i))
                            .whereEqualTo("ifDiscount" + camelTypeCategory + "All", true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            FunctionCard tempCard = document.toObject(FunctionCard.class);

                                            //상세카드 할인혜택필드도 완성할것
                                            try {
                                                tempCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }

                                            discountCategoryAllSavedKookminCard.add(tempCard);
                                        Log.d("SMG1", "국민전체할인되는"+document.toObject(FunctionCard.class).getCardIndex() + " 저장함");
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    db.collection("Kookmin")

                            .whereEqualTo("cardIndex", savedKookminCardIndexArrayList.get(i))
                            .whereEqualTo("ifDiscount" + camelTypeCategory + "All", false)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            FunctionCard tempCard = document.toObject(FunctionCard.class);

                                            //상세카드 할인혜택필드도 완성할것
                                            try {
                                                tempCard.setAmusementDiscount(getFunctionSpecificDiscountArrayList(document, "amusementDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBakeryDiscount(getFunctionSpecificDiscountArrayList(document, "bakeryDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setBookstoreDiscount(getFunctionSpecificDiscountArrayList(document, "bookStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setCafeDiscount(getFunctionSpecificDiscountArrayList(document, "cafeDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setConvenientStoreDiscount(getFunctionSpecificDiscountArrayList(document, "convenientStoreDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setFastFoodDiscount(getFunctionSpecificDiscountArrayList(document, "fastFoodDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setRestaurantDiscount(getFunctionSpecificDiscountArrayList(document, "restaurantDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                tempCard.setTheaterDiscount(getFunctionSpecificDiscountArrayList(document, "theaterDiscount"));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            discountCategoryPartSavedKookminCard.add(tempCard);
                                        Log.d("SMG1", "국민일부할인되는"+document.toObject(FunctionCard.class).getCardIndex() + " 저장함");
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
                //최적의 할인수단으로 화면 구성할것
                binding.recommendNewCardView.setVisibility(View.VISIBLE);
                binding.recommendSavedCardView.setVisibility(View.VISIBLE);



                //특수매장에 포함되는경우
                if(isSpecialStore(storeName)){
                    Log.d(TAG, "MenuRecommendFragment onCreateView " +storeName + " is SpecialStore");

                    //해당카테고리에서,,speicalStore가 몇번째 인덱스인지
                    int specialStoreIndex = firebaseTypeSpecialStoreIndex(storeName);


                    //할인part인 카드중에서,, 해당매장할인 가능한 카드,,중에서 최고 할인율 카드 찾기,,찾아서,,어떤카드사, 카드인덱스, 최대혜택 저장할것,,현재 최대혜택은 저장함
                    for(int i = 0; i < discountCategoryPartSavedShinhanCard.size(); i++){
                        //만약해당매장(specialStoreIndex) 할인이 0이 아니면??
                        //temp할인율보다 좋으면 최신화
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마

                                //이 카드가 최고혜택이면,, 인덱스랑, 카드사도 같이 저장해야지!
                                if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();

                                } else if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Shinhan"; bestFunctionCardIndex = i;
                    }
                    for(int i = 0; i < discountCategoryPartSavedKookminCard.size(); i++){
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마
                                if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryPartSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Kookmin"; bestFunctionCardIndex = i;
                    }



                    // 할인all인 카드,,중에서 최고 할인율 카드 찾기
                    for(int i = 0; i < discountCategoryAllSavedShinhanCard.size(); i++){
                        //만약해당매장(specialStoreIndex) 할인이 0이 아니면??
                        //temp할인율보다 좋으면 최신화
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마
                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Shinhan"; bestFunctionCardIndex = i;
                    }
                    for(int i = 0; i < discountCategoryAllSavedKookminCard.size(); i++){
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마
                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Kookmin"; bestFunctionCardIndex = i;
                    }



                }
                //특수매장이 아닌경우
                else{
                    Log.d(TAG, "MenuRecommendFragment onCreateView " +storeName + " is not SpecialStore");
                    //할인all인 카드,,중에서 최고 할인율 카드 찾기
                    int specialStoreIndex = 0;
                    for(int i = 0; i < discountCategoryAllSavedShinhanCard.size(); i++){
                        //만약해당매장(specialStoreIndex) 할인이 0이 아니면??
                        //temp할인율보다 좋으면 최신화
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마
                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedShinhanCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Shinhan"; bestFunctionCardIndex = i;
                    }
                    for(int i = 0; i < discountCategoryAllSavedKookminCard.size(); i++){
                        switch (camelTypeCategory){
                            case "Amusement":
                                //해당카드가 amusement>index번항목>할인액이 best보다 좋으면!!
                                //best보다 좋지는 않고 차선보다는 좋으면!!!
                                //그냥최악이면 건들지마
                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getAmusementDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Bakery":
                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBakeryDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "BookStore":
                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getBookstoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Cafe":
                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getCafeDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "ConvenientStore":
                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getConvenientStoreDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "FastFood":
                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getFastFoodDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Restaurant":
                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getRestaurantDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;
                            case "Theater":
                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > bestDiscountAmount){
                                    bestDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount() > alterDiscountAmount){
                                    alterDiscountAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountAmount();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > bestDiscountRate){
                                    bestDiscountRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate() > alterDiscountRate){
                                    alterDiscountRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getDiscountRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > bestCashbackRate){
                                    bestCashbackRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate() > alterCashbackRate){
                                    alterCashbackRate = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackRate();
                                }

                                if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > bestCashbackAmount){
                                    bestCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                } else if(discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount() > alterCashbackAmount){
                                    alterCashbackAmount = discountCategoryAllSavedKookminCard.get(i).getTheaterDiscount().get(specialStoreIndex).getCashbackAmount();
                                }
                                break;

                        }
                        bestFunctionCardCompany = "Kookmin"; bestFunctionCardIndex = i;
                    }


                }


                //pay1, pay2 정보 입력하기..이름은 어디서 가져올까나...저장된건 카드사, 카드인덱스, 최대헤택인데..
                //근데 pay1, pay2는 각각 최대할인액, 최대할인비율로 해야겠다..이런근본적인걸 잊고있었던 나는 죤나병신인가

                //최대할인/캐시백비율, 최대할인/캐시백액 산정하기
                if(bestDiscountAmount != 0) binding.pay1CardName.setText(String.valueOf(bestDiscountAmount));
                if(bestCashbackAmount != 0) binding.pay2CardName.setText(bestCashbackAmount);



            }
            //할인이 불가능한 functionUser임..기본화면제공
            else{}

            //추천카드제시




        }


        return binding.getRoot();
    }

    //이미 그려지고 난 이후,,여기서는 보여지는 것이 수정되면 안됨
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "MenuRecommendFragment onViewCreated");
    }


    @Override
    public void onPause() {
        Log.d(TAG, "MenuRecommendFragment onPause");
        super.onPause();
    }


    private boolean isAvailableDiscountCategory(String category){
        switch (category){
            case "커피": camelTypeCategory = "Cafe"; return functionUser.isAvailableDiscountCafe();
            case "편의점": camelTypeCategory = "ConvenientStore"; return functionUser.isAvailableDiscountConvenientStore();
            case "패스트푸드": camelTypeCategory = "FastFood"; return functionUser.isAvailableDiscountFastFood();
            case "음식점": camelTypeCategory = "Restaurant"; return functionUser.isAvailableDiscountRestaurant();
            case "영화관": camelTypeCategory = "Theater"; return functionUser.isAvailableDiscountTheater();
            case "베이커리": camelTypeCategory = "Bakery"; return functionUser.isAvailableDiscountBakery();
            case "서점": camelTypeCategory = "BookStore"; return functionUser.isAvailableDiscountBookStore();
            case "놀이공원": camelTypeCategory = "Amusement"; return functionUser.isAvailableDiscountAmusement();
            default: return false;
        }
    }

    private boolean isSpecialStore(String storeName){
        return (storeName.contains("캐리비안베이") ||storeName.contains("서울랜드") ||storeName.contains("롯데월드") ||storeName.contains("에버랜드") ||storeName.contains("레고랜드")

                ||storeName.contains("파리바게뜨") ||storeName.contains("파리크라상") ||storeName.contains("뚜레쥬르") ||storeName.contains("던킨")

                ||storeName.contains("카페베네") ||storeName.contains("스타벅스") ||storeName.contains("빽다방") ||storeName.contains("이디야") ||storeName.contains("투썸플레이스") ||storeName.contains("커피빈") ||storeName.contains("엔제리너스") ||storeName.contains("배스킨라빈스")

                ||storeName.contains("CU") ||storeName.contains("세븐일레븐") ||storeName.contains("이마트24") ||storeName.contains("GS25")

                ||storeName.contains("KFC") ||storeName.contains("버거킹") ||storeName.contains("롯데리아")

                ||storeName.contains("빕스") ||storeName.contains("아웃백") ||storeName.contains("계절밥상")

                ||storeName.contains("CGV") ||storeName.contains("메가박스") ||storeName.contains("롯데시네마")

                ||storeName.contains("yes24") ||storeName.contains("교보문고")

        );
    }

    //document, amuse~theater얻어와서,, functionLocationSpecificDiscount-ArrayList return
    private ArrayList<FunctionLocationSpecificDiscount> getFunctionSpecificDiscountArrayList(QueryDocumentSnapshot document, String specificDiscount) throws Exception {
        ArrayList<FunctionLocationSpecificDiscount> functionLocationSpecificDiscountArrayList = new ArrayList<>();
        switch (specificDiscount){
            case "amusementDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "레고랜드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "롯데월드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "서울랜드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "에버랜드"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "캐리비안베이"));
                break;
            case "bakeryDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "던킨"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "뚜레쥬르"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "파리바게뜨"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "파리크라상"));
                break;
            case "bookstoreDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "yes24"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "교보문고"));
                break;
            case "cafeDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "배스킨라빈스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "빽다방"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "스타벅스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "엔제리너스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "이디야"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "카페베네"));
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
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "롯데리아"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "버거킹"));
                break;
            case "restaurantDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "계절밥상"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "빕스"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "아웃백"));
                break;
            case "theaterDiscount":
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "CGV"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "롯데시네마"));
                functionLocationSpecificDiscountArrayList.add(getFunctionSpecificDiscount(document, specificDiscount, "메가박스"));
                break;

        }

        return functionLocationSpecificDiscountArrayList;
    }

    //amuse~theater 컬렉션에서,,"매장이름"의 FunctionLocationSpecificDiscount을 클래스형식으로 받아옴
    private FunctionLocationSpecificDiscount getFunctionSpecificDiscount(QueryDocumentSnapshot queryDocumentSnapshot, String specificDiscount, String locationName) throws Exception {

        Task<DocumentSnapshot> task = queryDocumentSnapshot.getReference().collection(specificDiscount).document(locationName).get();
        while(!task.isComplete()){Thread.sleep(1);}

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
    private int firebaseTypeSpecialStoreIndex(String storeName){
        switch (storeName){
            case "레고랜드": case "던킨": case "yes24": case "배스킨라빈스": case "CU": case "KFC": case "계절밥상": case "CGV": return 0;

            case "롯데월드": case "뚜레쥬르": case "교보문고": case "빽다방": case "GS25": case "롯데리아": case "빕스": case "롯데시네마": return 1;

            case "서울랜드": case "파리바게뜨": case "스타벅스": case "세븐일레븐": case "버거킹": case "아웃백": case "메가박스": return 2;

            case "에버랜드": case "파리크라상": case "엔제리너스": case "이마트24": return 3;

            case "캐리비안베이": case "이디야": return 4;

            case "카페베네": return 5;

            case "커피빈": return 6;

            case "투썸플레이스": return 7;

            default: return 0;
        }
    }
}