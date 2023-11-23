//package com.example.selection;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.selection.databinding.ActivityFunctionAddCardBenefitBinding;
//import com.example.selection.databinding.ActivityFunctionIouserBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.RuntimeExecutionException;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.functions.FirebaseFunctions;
//import com.google.firebase.functions.HttpsCallableResult;
//import com.kakao.sdk.auth.model.OAuthToken;
//import com.kakao.sdk.user.UserApiClient;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.function.Function;
//
//import kotlin.Unit;
//import kotlin.jvm.functions.Function2;
//
//public class FunctionIOUser extends AppCompatActivity {
//
//    private FirebaseFirestore db;
//    private FirebaseUser currentUser;
//    private ActivityFunctionIouserBinding binding;
//    private final String TAG = "SMG";
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityFunctionIouserBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        db = FirebaseFirestore.getInstance();
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        addUser(currentUser);
//
//
//
////        FunctionUser functionUser = getUser("kakao:3143790213");
//
//
//
//    }
//
//
//
//    private void addUser(FirebaseUser firebaseUser){
//        FunctionUser functionUser = new FunctionUser(
//                firebaseUser.getUid(), firebaseUser.getEmail(),
//                false, false,
//                false, false,
//                false, false,
//                false, false,
//                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        CollectionReference userRef = db.collection("User");
//        userRef.document(firebaseUser.getUid()).set(functionUser);
//    }
//
//    private FunctionUser getUser(String uid){
//        db.collection("User")
//                .whereEqualTo("uid", uid)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                    }
//                })
//    }
//
//
//}