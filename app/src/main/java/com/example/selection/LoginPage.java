package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.selection.databinding.ActivityLoginBinding;
import com.example.selection.databinding.ActivityLoginPageBinding;

public class LoginPage extends AppCompatActivity {
    private ActivityLoginPageBinding binding;
    private String userId , userPassword;
    public void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.LoginButton.setOnClickListener(view -> {
            userId = binding.ID.getText().toString();
            userPassword = binding.PASSWORD.getText().toString();

            if((userId.length() == 0) || (userPassword.length() == 0)){
                showToast("아이디와 비밀번호를 정학히 입력하세요");
            }
            else{
                Log.d("SMG", "" +userId + " " + userPassword);
            }
        });


    }
}