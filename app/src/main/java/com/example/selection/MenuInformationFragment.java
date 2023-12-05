package com.example.selection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class MenuInformationFragment extends Fragment{

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
        View imageView = view.findViewById(R.id.Password_change_button);
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