package com.example.selection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting_InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting_InformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Setting_InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting_Information.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting_InformationFragment newInstance(String param1, String param2) {
        Setting_InformationFragment fragment = new Setting_InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("YJH", "OnCreated");
        return inflater.inflate(R.layout.fragment_setting__information, container, false);
    }
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//
//        Button BackButton = view.findViewById(R.id.back_button);
//        Button PasswordChangeButton = view.findViewById(R.id.Password_change_button);
//
//    }
}