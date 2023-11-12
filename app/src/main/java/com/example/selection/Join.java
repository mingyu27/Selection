package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.selection.databinding.ActivityJoinBinding;

public class Join extends AppCompatActivity {

    private ActivityJoinBinding binding;
    private String userName, userId, userPassword, userPasswordAgain;
    private void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.next1Join.setOnClickListener(view -> {
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
            }


        });
    }
}