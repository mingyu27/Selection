package com.example.selection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.selection.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FunctionUser functionUser;
    private BottomNavigationView bottomNavigationView;
    private String storeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
        bottomNavigationView = binding.bottomNavigation;
        transferTo(MenuRecommendCardFragment.newInstance("param1", "param2"));

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK){
                            storeName = o.getData().getStringExtra("storeName");
                        }

                    }
                });

        launcher.launch((new Intent(this, MainLocationChooseDialog.class)));

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();

                if(itemID == R.id.home){
                    transferTo(MenuRecommendCardFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.my_card){
                    transferTo(MenuSavedCardFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.liked_card){
                    transferTo(MenuLikedCardFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.add_card){
                    transferTo(MenuAddCardChooseCompanyFragment.newInstance("param1","param2"));
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }
    private void transferTo(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}