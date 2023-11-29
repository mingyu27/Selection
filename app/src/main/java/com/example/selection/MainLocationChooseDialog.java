package com.example.selection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.selection.databinding.ActivityMainLocationChooseDialogBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainLocationChooseDialog extends AppCompatActivity {


    private NumberPicker categoryPicker;
    private NumberPicker storePicker;

    private FusedLocationProviderClient fusedLocationClient;
    private ActivityMainLocationChooseDialogBinding binding;
    private List<String> storeNameList;
    private String selectedStoreName;
    private String searchedStoreName;



    private static final String REST_API_KEY = "8a4f1750a33a01a17d50933a8336b64a";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String TAG = "SMG";
    private double latitude, longitude;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        binding = ActivityMainLocationChooseDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위치 권한 요청
        requestLocationPermission();

        // 현재 위치 얻어오기
        getCurrentLocation();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        categoryPicker = binding.categoryPicker;
        storePicker = binding.storePicker;
        searchedStoreName = binding.searchStoreName.getText().toString();


        final String[] categoryList = {"커피", "편의점", "패스트푸드", "음식점", "영화관", "베이커리", "서점", "놀이공원"};
        categoryPicker.setMinValue(0);
        categoryPicker.setMaxValue(categoryList.length-1);
        categoryPicker.setDisplayedValues(categoryList);
        categoryPicker.setWrapSelectorWheel(false);


        //거리순으로 매장이름만 넣을 String배열
        storePicker.setMinValue(0);
        storePicker.setMaxValue(10);
        storePicker.setWrapSelectorWheel(false);
        // 초기값("카페")으로 fetchStores 실행
        if (ContextCompat.checkSelfPermission(MainLocationChooseDialog.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchStores(urlEncode(categoryList[0]));
            Log.d("SMG", "GOOD");
        } else {
            Log.d("SMG", "BAD");
        }



        //카테고리 선택시
        categoryPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                //위치허용시 선택된카테고리로 매장fetch할것
                if (ContextCompat.checkSelfPermission(MainLocationChooseDialog.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fetchStores(urlEncode(categoryList[newVal]));
                    Log.d("SMG", "GOOD");
                } else Log.d("SMG", "BAD");


            }
        });

        //매장 선택시
        storePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedStoreName = storeNameList.get(newVal);
            }
        });


        //직접검색시

        binding.cancelToMainAcitivity.setOnClickListener(view -> {
            setResult(RESULT_OK, new Intent().putExtra("storeName", ""));
            finish();

        });

        binding.saveLocationToMainActivity.setOnClickListener(view -> {
            setResult(RESULT_OK, new Intent().putExtra("storeName", selectedStoreName));
            finish();
        });







    }




    //권한 없으면 권한 받아오기
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //권한받아왔으니,, 현재 위치 받아오기
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location lastLocation = task.getResult();
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                        Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
                    } else {
                        Log.e(TAG, "Failed to get location.");
                    }
                }
            });
        } else {
            Log.e(TAG, "Location permission not granted.");
        }
    }


    //쿼리에서 키워드>문자인코딩값
    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //현위치를 가지고 fetchsoretask 실행
    private void fetchStores(String category) {
        // 위치 권한이 허용되어 있을 때만 위치 기반 매장 검색 수행
        if (ContextCompat.checkSelfPermission(MainLocationChooseDialog.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            //GOTO execute(카테고리명, 위도, 경도)
                            new FetchStoresTask().execute(category, String.valueOf(latitude), String.valueOf(longitude));
                            Log.d("SMG", ""+latitude);
                            Log.d("SMG", ""+longitude);
                        }
                    });
        } else {
            // 위치 권한이 허용되어 있지 않은 경우
            Log.d("SMG", "Location permission not granted.");
        }
    }



    //asyntask를 기반으로 background에서 인자로 받아온,,카테고리코드, 위도, 경도로,,,restapi요청실행,,,response가지고 parseStoreResponse실행
    private class FetchStoresTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // 백그라운드에서 네트워크 요청 수행
            String encodedCategoryName = params[0];
            String latitude = params[1];
            String longitude = params[2];
            //URL생성


            String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?sort=distance&x=" + longitude + "&y=" + latitude
                    + "&radius=20000&query=" + encodedCategoryName;

            //URL로 RESTAPI요청실행
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                //response 문자열 return
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        //요청후 response 받아왔을 때
        @Override
        protected void onPostExecute(String response) {
            // 네트워크 요청 완료 후 매장 목록 표시
            if (response != null) {
                //response문자열로 parseStoreResponse실행
                parseStoreResponse(response);
            } else {
                Toast.makeText(MainLocationChooseDialog.this, "Error fetching stores", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //response받아와서,,Store형식으로 파싱후,,거리순정렬,,storePicker에 리스트표시
    private void parseStoreResponse(String response) {
        // 응답에서 매장 정보를 추출하여 리스트뷰에 표시

        try {
            // JSON 파싱 작업
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray storesArray = jsonResponse.getJSONArray("documents");

            // 매장 정보를 담을 리스트
            List<FunctionStore> storeList = new ArrayList<>();
            storeNameList = new ArrayList<>();

            for (int i = 0; i < storesArray.length(); i++) {
                //매장명, 거리 정보 파싱하기
                JSONObject storeObject = storesArray.getJSONObject(i);
                String storeName = storeObject.getString("place_name");
//                String storeDistance = storeObject.getString("distance");


                // Store 객체 생성 및 리스트에 추가
                FunctionStore store = new FunctionStore(storeName);
                storeList.add(store);
            }

            // 거리순으로 정렬
//            Collections.sort(storeList, new Comparator<Store>() {
//                @Override
//                public int compare(Store s1, Store s2) {
//                    return Double.compare(s1.getDistance(), s2.getDistance());
//                }
//            });

            // storeList에 매장명만 추가
            for (FunctionStore store : storeList) {
                storeNameList.add(store.getName());
            }

            storePicker.setValue(0);
            storePicker.setDisplayedValues(storeNameList.toArray(new String[0]));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







}
