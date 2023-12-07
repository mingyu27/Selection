package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selection.databinding.ActivityWelcomeJoinBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class WelcomeJoin extends AppCompatActivity {

    private ActivityWelcomeJoinBinding binding;
    private String userName, userId, userPassword, userPasswordAgain;
    private static final String TAG = "SMG";
    private FirebaseAuth mAuth;
    private void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    private FunctionUser functionUser;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        EditText Id = findViewById(R.id.id_join);
        EditText Password = findViewById(R.id.password_join);

        binding.joinBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeJoin.this, Welcome.class));
            }
        });
        binding.next1Join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = binding.nameJoin.getText().toString();
                userId = binding.idJoin.getText().toString();
                userPassword = binding.passwordJoin.getText().toString();
                userPasswordAgain = binding.passwordOkJoin.getText().toString();
                if(userName.length() == 0 || userId.length() == 0 || userPassword.length() == 0 || userPasswordAgain.length() == 0){
                    showToast("빈칸 없이 채워주세요");
                }
                else if(!userPassword.equals(userPasswordAgain)){
                    binding.passwordcheckJoin.setVisibility(View.VISIBLE);
                }
                else if(userPassword.equals(userPasswordAgain)){
                    binding.passwordcheckJoin.setVisibility(View.INVISIBLE);
                    signUp(userId, userPassword);

                }
            }
        });
    }

    //신규가입,,이름,uid로 FunctionUser초기화해서,,firestore에 저장,, putExtra로 MainActivity로 넘김
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isComplete()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            functionUser = new FunctionUser(
                                    user.getUid(), userName, false, false, false, false,
                                    false, false, false, false,
                                    new ArrayList<Integer>(), new ArrayList<Integer>(),
                                    new ArrayList<Integer>(), new ArrayList<Integer>());
                            CollectionReference userReference = db.collection("User");
                            userReference.document("email"+userName).set(functionUser);

                            // 외부에서 functionUser 사용 가능
                            onSignUpSuccess(functionUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // 실패 시 처리
                            onSignUpFailure();
                        }
                    }
                });


    }


    // 사용자 생성 성공 시 호출되는 함수
    private void onSignUpSuccess(FunctionUser functionUser) {
        // 외부에서 functionUser 사용 가능
        Log.d(TAG, "User created successfully: " + functionUser.getName());
        startActivity(new Intent(this, WelcomeAddCardAlert.class).putExtra("functionUser", functionUser));
    }

    // 사용자 생성 실패 시 호출되는 함수
    private void onSignUpFailure() {
        Log.d(TAG, "User creation failed");
    }
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            Intent intent = new Intent(this, Welcome.class);
//            //intent.putExtra("USER_PROFILE", "email: " + user.getEmail() + "\n" + "uid: " + user.getUid());
//            startActivity(intent);
//        }
//    }
}