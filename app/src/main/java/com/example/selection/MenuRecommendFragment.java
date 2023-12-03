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

import java.util.Objects;


public class MenuRecommendFragment extends Fragment {

    private FragmentMenuRecommendBinding binding;
    private String TAG = "SMG";
    private ImageView InfoButton;
    private String storeName = "위치를 선택해주세요";
    private String category = "";

    private boolean isLocationSet = false;
    private MainActivity mainActivity;
    private FunctionUser functionUser;
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
    }

    //fragemnt 보여지기전에,,안에 들어있는것들 꺼내보기
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MenuRecommendFragment onCreate");
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
        if(isLocationSet){
            binding.recommendNewCardView.setVisibility(View.VISIBLE);
            binding.recommendSavedCardView.setVisibility(View.VISIBLE);

            //해당매장정보..추천카드1,2순위 보여주기


        }


        return binding.getRoot();
    }

    //이미 그려지고 난 이후,,여기서는 보여지는 것이 수정되면 안됨
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "MenuRecommendFragment onViewCreated");
        InfoButton = view.findViewById(R.id.information_button);
        InfoButton.setOnClickListener(v -> {startActivity(new Intent(getActivity(), SettingsInformation.class));});



    }


    @Override
    public void onPause() {
        Log.d(TAG, "MenuRecommendFragment onPause");
        super.onPause();
    }
}