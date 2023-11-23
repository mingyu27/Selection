package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class WelcomeJoin extends AppCompatActivity {

    private ActivityWelcomeJoinBinding binding;
    private String userName, userId, userPassword, userPasswordAgain;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
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
                    startActivity(new Intent(WelcomeJoin.this, Welcome.class));
                }
            }
        });
    }
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //  updateUI(user);
                        } else {
                            Log.d(TAG, "createUserWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }
                    }
                });
    }
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            Intent intent = new Intent(this, Welcome.class);
//            //intent.putExtra("USER_PROFILE", "email: " + user.getEmail() + "\n" + "uid: " + user.getUid());
//            startActivity(intent);
//        }
//    }
}