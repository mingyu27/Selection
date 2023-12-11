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

    private MainActivity mainActivity;
    private FunctionUser functionUser;
    private FirebaseFirestore db;
    private boolean isLocationSet = false;


    List<FunctionCard> savedKookmin;
    List<FunctionCard> savedShinhan;

    List<Integer> savedShinhanBenfitAmount = new ArrayList<>();
    List<Double> savedShinhanBenfitRate = new ArrayList<>();

    List<Integer> savedKookminBenfitAmount = new ArrayList<>();
    List<Double> savedKookminBenfitRate = new ArrayList<>();




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
                if(tempUser != null){functionUser = tempUser; Log.d(TAG, functionUser.getName() + "at MenuRecommendFragment"); savedShinhan = functionUser.getSavedShinhanFunctionCardList(); savedKookmin = functionUser.getSavedKookminFunctionCardList();}


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

        binding.selectedLocation.setText(storeName);



        //카테고리 할인 가능한 user이면!!
        if(isFuctionUserBenefitAvailable(category)){
            Log.d(TAG, functionUser.getName() + " is available for benefit " + category);


            int categoryNum = categoryToNum(category);
            //0번째는 amusement
            //1번째 bakery
            //2번째 bookstore
            //..
            //7번째 theater


            //저장된 각 신한카드가 amusement true인지
            for(int i = 0; i < savedShinhan.size(); i++){
                 //검사중인카드가 amusement할인 가능이면
                if(isIfDiscountCategory(savedShinhan.get(i), categoryNum)){

                    //검사중인 카드가 discountAll true이면
                    if(isIfDiscountCategoryAll(savedShinhan.get(i), categoryNum) == true){
                        savedShinhanBenfitAmount.add((int) max(getCategoryDiscount(savedShinhan.get(i), categoryNum).get(0).getCashbackAmount(), getCategoryDiscount(savedShinhan.get(i), categoryNum).get(0).getDiscountAmount() ));
                        savedShinhanBenfitRate.add( max(getCategoryDiscount(savedShinhan.get(i), categoryNum).get(0).getCashbackRate(), getCategoryDiscount(savedShinhan.get(i), categoryNum).get(0).getDiscountRate() ));
                        Log.d(TAG, "검사중 카드: " + savedShinhan.get(i).getCardName() + " discountAll  = true");
                    }
                    //검사중인 카드가 discountAll false이면
                    if(isIfDiscountCategoryAll(savedShinhan.get(i), categoryNum) == false){
                        //방문한 매장이 에버랜드, 롯데월드, 서울랜드, 캐리, 서울랜드 중에 하나이면
                        if( isStoreSpecial(storeName) ){
                            savedShinhanBenfitAmount.add((int) max(getCategoryDiscount(savedShinhan.get(i), categoryNum).get(getStoreNameIndex(storeName)).getCashbackAmount(), getCategoryDiscount(savedShinhan.get(i), categoryNum).get(getStoreNameIndex(storeName)).getDiscountAmount() ));
                            savedShinhanBenfitRate.add( max(getCategoryDiscount(savedShinhan.get(i), categoryNum).get(getStoreNameIndex(storeName)).getCashbackRate(), getCategoryDiscount(savedShinhan.get(i), categoryNum).get(getStoreNameIndex(storeName)).getDiscountRate() ));
                            Log.d(TAG, "검사중 카드: " + savedShinhan.get(i).getCardName() + " discountAll  = false, special");
                        }
                        //방문한 매장이 에버랜드, 롯데월드, 서울랜드, 캐리, 서울랜드 중에 없으면,,,,혜택적용불가지만,,나중에 비교를 위해 0, 0.0넣음
                        else{
                            savedShinhanBenfitAmount.add(0); savedShinhanBenfitRate.add(0.0);
                            Log.d(TAG, "검사중 카드: " + savedShinhan.get(i).getCardName() + " discountAll  = false, not special");
                        }
                    }
                }
            }

            //저장된 각 국민카드가 amusement true인지
            for(int i = 0; i < savedKookmin.size(); i++){
                 //검사중인카드가 amusement할인 가능이면
                if(isIfDiscountCategory(savedKookmin.get(i), categoryNum)){
                    //검사중인 카드가 discountAll true이면
                    if(isIfDiscountCategoryAll(savedKookmin.get(i), categoryNum) == true){
                        savedKookminBenfitAmount.add((int) max(getCategoryDiscount(savedKookmin.get(i), categoryNum).get(0).getCashbackAmount(), getCategoryDiscount(savedKookmin.get(i), categoryNum).get(0).getDiscountAmount() ));
                        savedKookminBenfitRate.add( max(getCategoryDiscount(savedKookmin.get(i), categoryNum).get(0).getCashbackRate(), getCategoryDiscount(savedKookmin.get(i), categoryNum).get(0).getDiscountRate() ));
                        Log.d(TAG, "검사중 카드: " + savedKookmin.get(i).getCardName() + " discountAll  = true");
                    }
                    //검사중인 카드가 discountAll false이면
                    if(isIfDiscountCategoryAll(savedKookmin.get(i), categoryNum) == false){
                        //방문한 매장이 에버랜드, 롯데월드, 서울랜드, 캐리, 서울랜드 중에 하나이면
                        if( isStoreSpecial(storeName) ){
                            savedKookminBenfitAmount.add((int) max(getCategoryDiscount(savedKookmin.get(i), categoryNum).get(getStoreNameIndex(storeName)).getCashbackAmount(), getCategoryDiscount(savedKookmin.get(i), categoryNum).get(getStoreNameIndex(storeName)).getDiscountAmount() ));
                            savedKookminBenfitRate.add( max(getCategoryDiscount(savedKookmin.get(i), categoryNum).get(getStoreNameIndex(storeName)).getCashbackRate(), getCategoryDiscount(savedKookmin.get(i), categoryNum).get(getStoreNameIndex(storeName)).getDiscountRate() ));
                            Log.d(TAG, "검사중 카드: " + savedKookmin.get(i).getCardName() + " discountAll  = false, special");
                        }
                        //방문한 매장이 에버랜드, 롯데월드, 서울랜드, 캐리, 서울랜드 중에 없으면,,,,혜택적용불가지만,,나중에 비교를 위해 0, 0.0넣음
                        else{
                            savedKookminBenfitAmount.add(0); savedKookminBenfitRate.add(0.0);
                            Log.d(TAG, "검사중 카드: " + savedKookmin.get(i).getCardName() + " discountAll  = false, not special");
                        }
                    }
                }
            }


//            Log.d(TAG, "검사완료된 신한카드: " + savedKookminBenfitAmount);
            Log.d(TAG, "검사완료된 국민카드: " + "(최고할인율 : " + savedKookminBenfitRate.get(0).toString() + ")" + "(최고할인액 : " + savedKookminBenfitAmount.get(0).toString() + ")");


        }








        return binding.getRoot();
    }




    //이미 그려지고 난 이후,,여기서는 보여지는 것이 수정되면 안됨
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(functionUser!= null) {
//            Log.d(TAG, "MenuRecommendFragment onViewCreated");
//            Log.d(TAG, "" + savedKookminBenfitAmount.get(0));
//            Log.d(TAG, "" + savedKookminBenfitRate.get(0));
//            Log.d(TAG, "" + savedShinhanBenfitAmount.get(0));
//            Log.d(TAG, "" + savedShinhanBenfitRate.get(0));
        }

        binding.pay1Card.setOnClickListener(v-> {startActivity(new Intent(getActivity(), MenuRecommendSavedCard.class).putExtra("payPriority", 1));});
        binding.pay2Card.setOnClickListener(v-> {startActivity(new Intent(getActivity(), MenuRecommendSavedCard.class).putExtra("payPriority", 2));});
        binding.recommendNewCardView.setOnClickListener(v-> {startActivity(new Intent(getActivity(), MenuRecommendNewCard.class).putExtra("payPriority", 2));});
        binding.selectedLocation.setOnClickListener(v -> {mainActivity.startDialog();});


    }


    @Override
    public void onPause() {
        Log.d(TAG, "MenuRecommendFragment onPause");
        super.onPause();
    }



















    //검사중인 카드가 해당카테고리 할인 가능인지!
    private boolean isIfDiscountCategory(FunctionCard functionCard, int categoryNum){
        switch (categoryNum){
            case 0: return functionCard.isIfDiscountAmusement();
            case 1: return functionCard.isIfDiscountBakery();
            case 2: return functionCard.isIfDiscountBookStore();
            case 3: return functionCard.isIfDiscountCafe();
            case 4: return functionCard.isIfDiscountConvenientStore();
            case 5: return functionCard.isIfDiscountFastFood();
            case 6: return functionCard.isIfDiscountRestaurant();
            case 7: return functionCard.isIfDiscountTheater();
            default: return false;
        }
    }

    private boolean isIfDiscountCategoryAll(FunctionCard functionCard, int categoryNum){
        switch (categoryNum){
            case 0: return functionCard.isIfDiscountAmusementAll();
            case 1: return functionCard.isIfDiscountBakeryAll();
            case 2: return functionCard.isIfDiscountBookStoreAll();
            case 3: return functionCard.isIfDiscountCafeAll();
            case 4: return functionCard.isIfDiscountConvenientStoreAll();
            case 5: return functionCard.isIfDiscountFastFoodAll();
            case 6: return functionCard.isIfDiscountRestaurantAll();
            case 7: return functionCard.isIfDiscountTheaterAll();
            default: return false;
        }
    }

    private ArrayList<FunctionLocationSpecificDiscount> getCategoryDiscount(FunctionCard functionCard, int categoryNum){
        switch (categoryNum){
            case 0: return functionCard.getAmusementDiscount();
            case 1: return functionCard.getBakeryDiscount();
            case 2: return functionCard.getBookstoreDiscount();
            case 3: return functionCard.getCafeDiscount();
            case 4: return functionCard.getConvenientStoreDiscount();
            case 5: return functionCard.getFastFoodDiscount();
            case 6: return functionCard.getRestaurantDiscount();
            case 7: return functionCard.getTheaterDiscount();
            default: return null;
        }

    }

    //functionUser가 해당 카테고리 할인가능인지
    private boolean isFuctionUserBenefitAvailable(String category){
       switch (category){
           case "커피": return functionUser.isAvailableDiscountCafe();
           case "편의점": return functionUser.isAvailableDiscountConvenientStore();
           case "패스트푸드": return functionUser.isAvailableDiscountFastFood();
           case "음식점": return functionUser.isAvailableDiscountRestaurant();
           case "영화관": return functionUser.isAvailableDiscountTheater();
           case "베이커리": return functionUser.isAvailableDiscountBakery();
           case "서점": return functionUser.isAvailableDiscountBakery();
           case "놀이공원": return functionUser.isAvailableDiscountAmusement();
           default: return false;
       }
    }

    //그냥 비교
    private double max(double cashBack, double discount){
        return ((cashBack >= discount) ? cashBack : discount);
    }

    // storeName.contains(“” ,,,,,,,,)
    boolean isStoreSpecial(String storeName) {
        return (
                        storeName.contains("레고랜드")||storeName.contains("롯데월드")||storeName.contains("서울랜드")||storeName.contains("에버랜드")||storeName.contains("캐리비안베이")||
                        storeName.contains("던킨")||storeName.contains("뚜레쥬르")||storeName.contains("파리바게뜨")||storeName.contains("파리크라상")||
                        storeName.contains("yes24")||storeName.contains("교보문고")||
                        storeName.contains("배스킨라빈스")||storeName.contains("빽다방")||storeName.contains("스타벅스")||storeName.contains("엔제리너스")||storeName.contains("이디야")||storeName.contains("카페베네")||storeName.contains("커피빈")||storeName.contains("투썸플레이스")||
                        storeName.contains("CU")||storeName.contains("GS25")||storeName.contains("세븐일레븐")||storeName.contains("이마트24")||
                        storeName.contains("KFC")||storeName.contains("롯데리아")||storeName.contains("버거킹")||
                        storeName.contains("계절밥상")||storeName.contains("빕스")||storeName.contains("아웃백")||
                        storeName.contains("CGV")||storeName.contains("롯데시네마")||storeName.contains("메가박스")
        );

    }

    // storeName에서 핵심매장명만 빼고, 해당매장의 인덱스 번호 return
    int getStoreNameIndex(String storeName){
        if(storeName.contains("레고랜드")||storeName.contains("던킨")||storeName.contains("yes24")||storeName.contains("배스킨라빈스")|| storeName.contains("CU")||storeName.contains("KFC")||storeName.contains("계절밥상")||storeName.contains("CGV")) return 0;
        if(storeName.contains("롯데월드")||storeName.contains("뚜레쥬르")||storeName.contains("교보문고")||storeName.contains("빽다방")||storeName.contains("GS25")||storeName.contains("롯데리아")||storeName.contains("빕스")||storeName.contains("롯데시네마")) return 1;
        if(storeName.contains("서울랜드")|| storeName.contains("파리바게뜨")||storeName.contains("스타벅스")||storeName.contains("세븐일레븐")||storeName.contains("버거킹")||storeName.contains("아웃백")|| storeName.contains("메가박스")) return 2;
        if(storeName.contains("에버랜드")||storeName.contains("파리크라상")||storeName.contains("엔제리너스")|| storeName.contains("이마트24")) return 3;
        if(storeName.contains("이디야")) return 4;
        if(storeName.contains("카페베네")) return 5;
        if(storeName.contains("커피빈")) return 6;
        if(storeName.contains("투썸플레이스")) return 7;
        else return 0;
    }

    private int categoryToNum(String category){
        switch (category){
            case "놀이공원": return 0;
            case "베이커리": return 1;
            case "서점": return 2;
            case "커피": return 3;
            case "편의점": return 4;
            case "패스트푸드": return 5;
            case "음식점": return 6;
            case "영화관": return 7;
            default: return 0;
        }

    }

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



}