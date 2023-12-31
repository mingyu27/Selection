package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selection.databinding.ActivityWelcomeLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WelcomeLogin extends AppCompatActivity {
    private ActivityWelcomeLoginBinding binding;
    private String userId , userPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "SMG";
    public void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    private FunctionUser functionUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText ID = findViewById(R.id.ID);
        EditText PASSWORD = findViewById(R.id.PASSWORD);

        binding.loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeLogin.this, Welcome.class));
            }
        });

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = binding.ID.getText().toString();
                userPassword = binding.PASSWORD.getText().toString();
                if((userId.length() == 0) || (userPassword.length() == 0)){
                    showToast("아이디와 비밀번호를 정학히 입력하세요");
                }
                else{
                    signIn(userId, userPassword);

                }

            }
        });

    }
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //user.getUid()로,,firestore에 일치하는uid의 functionuser를 받아오기

                            db.collection("User")
                                    .whereEqualTo("uid", user.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                functionUser = document.toObject(FunctionUser.class);
                                                startActivity(new Intent(WelcomeLogin.this,MainActivity.class).putExtra("functionUser", functionUser));
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task1.getException());
                                        }
                                    });

                        } else {
                            Log.d(TAG, "signInWithEmail:failure");
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            showLoginFailureDialog();

                        }
                    }
                });
    }
    private void showLoginFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패")
                .setMessage("아이디 또는 비밀번호가 올바르지 않습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자가 확인을 누르면 아무 작업도 하지 않고 다이얼로그를 닫습니다.
                        dialog.dismiss();
                    }
                })
                .show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));// 예 버튼 글자색 변경
    }
}
