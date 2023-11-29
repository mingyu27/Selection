package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.selection.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FunctionUser functionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
//
//        binding.username.setText(functionUser.getName());

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Setting_InformationFragment()).commit();
        transferTo(MainFragment.newInstance("param1", "param2"));
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();

                if(itemID == R.id.home){
                    transferTo(MainFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.my_card){
                  //  Log.d("YJH","my_card_menu");
                    transferTo(menu_possess_cardFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.dips_card){
                    transferTo(Dips_CardFragment.newInstance("param1","param2"));
                    return true;
                }
                if(itemID == R.id.add_card){

                }
                return false;
            }
        });

        navigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }
    private void transferTo(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}