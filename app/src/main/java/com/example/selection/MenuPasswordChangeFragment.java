package com.example.selection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MenuPasswordChangeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private FunctionUser functionUser;
    private String TAG = "SMG";

    public MenuPasswordChangeFragment() {
        // Required empty public constructor
    }




    public static MenuPasswordChangeFragment newInstance(FunctionUser functionUser) {
        MenuPasswordChangeFragment fragment = new MenuPasswordChangeFragment();
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
            if(tempUser != null){functionUser = tempUser; Log.d(TAG, functionUser.getName() + "at MenuPasswordChangeFragment");}

        }catch (NullPointerException e){}

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_password_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.back_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                MenuInformationFragment.newInstance(functionUser))
                        .commit();
            }
        });

        // EditText 참조 가져오기
        EditText currentPasswordEditText = view.findViewById(R.id.currentPassword);
        EditText newPasswordEditText = view.findViewById(R.id.newPassword);
        EditText confirmNewPasswordEditText = view.findViewById(R.id.confirmPassword);

        // 비밀번호 변경 버튼 참조 가져오기
        Button changePasswordButton = view.findViewById(R.id.password_changeButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

                // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
                if (!newPassword.equals(confirmNewPassword)) {
                    TextView passwordMismatch = (TextView) view.findViewById(R.id.passwordMismatch);
                    passwordMismatch.setVisibility(View.VISIBLE);
                    return;
                }

                // 현재 로그인한 사용자 가져오기
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // 비밀번호 변경
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}