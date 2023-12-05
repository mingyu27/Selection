package com.example.selection;

import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuPasswordChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuPasswordChangeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuPasswordChangeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuPasswordChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuPasswordChangeFragment newInstance(String param1, String param2) {
        MenuPasswordChangeFragment fragment = new MenuPasswordChangeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
                                MenuInformationFragment.newInstance("param1", "param2"))
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