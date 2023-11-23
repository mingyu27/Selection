package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class WelcomeLogin extends AppCompatActivity {
    private ActivityWelcomeLoginBinding binding;
    private String userId , userPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    public void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

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
                            // updateUI(user);
                            startActivity(new Intent(WelcomeLogin.this, AddCardChooseCompany.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.d(TAG, "signInWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
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