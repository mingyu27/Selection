package com.example.selection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private String storeName = "";
    private String category = "";
    private String TAG = "SMG";
    private static final int REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                storeName = o.getData().getStringExtra("storeName");
                category = o.getData().getStringExtra("category");
            }
            Bundle bundle = new Bundle();
            bundle.putString("storeName", storeName);
            bundle.putString("category", category);
            bundle.putSerializable("functionUser", functionUser);
            MenuRecommendFragment menuRecommendFragment = new MenuRecommendFragment();
            menuRecommendFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), menuRecommendFragment).commit();
            Log.d(TAG, "bundle("+storeName +", "  + category + ") sent to MenuRecommendFragment");
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
        Log.d(TAG, functionUser.getName() + "at MainActivity");
        bottomNavigationView = binding.bottomNavigation;
        transferTo(MenuRecommendFragment.newInstance(functionUser));






        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();

                if(itemID == R.id.home){
                    transferTo(MenuRecommendFragment.newInstance(functionUser));
                    return true;
                }
                if(itemID == R.id.my_card){
                    transferTo(MenuSavedCardFragment.newInstance(functionUser));
                    return true;
                }
                if(itemID == R.id.liked_card){
                    transferTo(MenuLikedCardFragment.newInstance(functionUser));
                    return true;
                }

                if(itemID == R.id.information){
                    transferTo(MenuInformationFragment.newInstance("param1","param2"));
                    return true;
                }
                return false;
            }
        });


//        });
    }
    private void transferTo(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).addToBackStack(null).commit();
    }

    protected String getStoreName(){
        return storeName;
    }

    @Override
    protected void onResume() {
//        if(!(storeName.equals(""))){
//            Bundle bundle = new Bundle();
//            bundle.putString("storeName", storeName);
//            MenuRecommendFragment menuRecommendFragment = new MenuRecommendFragment();
//            menuRecommendFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), menuRecommendFragment);
//            Log.d(TAG, "sended");
//        }
        super.onResume();
    }

    protected void startDialog(){
        launcher.launch(new Intent(MainActivity.this, MainLocationChooseDialog.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
    }
}