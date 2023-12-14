package com.example.selection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.selection.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FunctionUser functionUser;
    private BottomNavigationView bottomNavigationView;
    private String storeName = "";
    private String category = "";
    private String TAG = "SMG";
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private static final int REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                storeName = o.getData().getStringExtra("storeName");
                category = o.getData().getStringExtra("category");
            }

            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), MenuRecommendFragment.newInstance(functionUser, storeName, category)).commit();
//            Log.d(TAG, "bundle("+storeName +", "  + category + ") sent to MenuRecommendFragment1");
        }
    });

    public void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (currentUser != null) {
            Log.d("SMG!", currentUser.getUid());
            db.collection("User")
                    .whereEqualTo("uid", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                functionUser = document.toObject(FunctionUser.class);
                                Log.d(TAG, "저장된 신한카드: " + functionUser.getSavedKookminIndexList().size() + "개, 국민카드: " + functionUser.getSavedKookminIndexList().size() + "개");
                                Log.d(TAG, functionUser.getName() + "at MainActivity");
                                bottomNavigationView = binding.bottomNavigation;
                                transferTo(MenuRecommendFragment.newInstance(functionUser, "매장을 선택하세요", ""));

                                bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                    @Override
                                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                        int itemID = item.getItemId();

                                        if(itemID == R.id.home){
                                            transferTo(MenuRecommendFragment.newInstance(functionUser, "매장을 선택하세요", ""));
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
                                            transferTo(MenuInformationFragment.newInstance(functionUser));
                                            return true;
                                        }
                                        return false;
                                    }
                                });


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task1.getException());
                        }
                    });
        } else startActivity(new Intent(this, Welcome.class));


//        functionUser = (FunctionUser) getIntent().getSerializableExtra("functionUser");
//        Log.d(TAG, "functionuser에  있는카드 = " + functionUser.getSavedKookminFunctionCardList().get(0).getCardName());










//        });
    }
    private void transferTo(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).addToBackStack(null).commit();
    }

    protected String getStoreName(){
        return storeName;
    }



    protected void startDialog(){
        launcher.launch(new Intent(MainActivity.this, MainLocationChooseDialog.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
    }
}