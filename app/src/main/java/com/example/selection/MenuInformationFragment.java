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
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MenuInformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FunctionUser functionUser;
    private String TAG = "SMG";
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
            if(tempUser != null){functionUser = tempUser; Log.d(TAG, functionUser.getName() + "at MenuInformationFragment");}

        }catch (NullPointerException e){}
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // userName과 userId 정보 가져오기
        String user_Name = functionUser.getName();

        TextView userNameTextView = view.findViewById(R.id.user_name);

        userNameTextView.setText(user_Name);

        //카드 갯수 표시
        TextView cardCountTextView = view.findViewById(R.id.Card_Count);
        ArrayList<Integer> savedKookmin = (ArrayList<Integer>) functionUser.getSavedKookminIndexList();
        ArrayList<Integer> savedShinhan = (ArrayList<Integer>) functionUser.getSavedShinhanIndexList();

        int totalCardCount = savedKookmin.size() + savedShinhan.size();

        cardCountTextView.setText(String.valueOf(totalCardCount));

        View imageView = view.findViewById(R.id.Password_change_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        MenuPasswordChangeFragment.newInstance(functionUser)).addToBackStack(null).commit();
            }
        });

        View logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 대화상자 생성
                // AlertDialog.Builder를 통해 AlertDialog 생성
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 로그아웃 처리
                                FirebaseAuth.getInstance().signOut();
                                // 로그아웃 후 로그인 화면으로 이동
                                Intent intent = new Intent(getActivity(), Welcome.class);
                                startActivity(intent);
                                getActivity().finish();

                                // 로그아웃 완료 Toast 메시지 띄우기
                                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 아니오 버튼이 클릭된 경우의 처리
                                // 여기에 추가적인 코드 작성
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

// 예 버튼 글자색 변경
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.black));

// 아니오 버튼 글자색 변경
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.black));

            }
        });
    }
}

