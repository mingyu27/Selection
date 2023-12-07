package com.example.selection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MenuInformationFragment extends Fragment{
    public TextView CardCountView;
    private FunctionUser functionUser;
    private String TAG = "SMG";
    private List<Integer> savedShinhanCardIndexArrayList = new ArrayList<>();
    private List<Integer> savedKookminCardIndexArrayList = new ArrayList<>();
    public MenuInformationFragment() {
        // Required empty public constructor
    }
    public static MenuInformationFragment newInstance(FunctionUser functionUser) {
        MenuInformationFragment fragment = new MenuInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionUser",functionUser);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            FunctionUser tempUser;
            tempUser = (FunctionUser) getArguments().getSerializable("functionUser");
            if(tempUser != null){
                functionUser = tempUser;

                savedKookminCardIndexArrayList = functionUser.getSavedKookmin();
                savedShinhanCardIndexArrayList = functionUser.getSavedShinhan();

                Log.d(TAG, functionUser.getName() + "at MenuInformationFragment");
            }

        }catch (NullPointerException e){}
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_information, container, false);
        CardCountView = rootView.findViewById(R.id.Card_Count);

        // savedKookminCardIndexArrayList와 savedShinhanCardIndexArrayList의 크기 합 구하기
        int totalCardCount = savedKookminCardIndexArrayList.size() + savedShinhanCardIndexArrayList.size();

        // CardCountView에 값을 설정
        updateCardCount(totalCardCount);

        return rootView;
    }
    public void updateCardCount(int totalCardCount) {
        if (CardCountView != null) {
            CardCountView.setText(String.valueOf(totalCardCount));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View imageView = view.findViewById(R.id.Password_change_button);
        CardCountView = view.findViewById(R.id.Card_Count);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        MenuPasswordChangeFragment.newInstance(functionUser)).addToBackStack(null).commit();
            }
        });

        ImageView logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 대화상자 생성
                new AlertDialog.Builder(getContext())
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 로그아웃 처리
                                FirebaseAuth.getInstance().signOut();
                                // 로그아웃 후 로그인 화면으로 이동
                                Intent intent = new Intent(getActivity(),Welcome.class);
                                startActivity(intent);
                                getActivity().finish();

                                // 로그아웃 완료 Toast 메시지 띄우기
                                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}