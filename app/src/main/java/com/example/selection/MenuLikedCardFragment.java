package com.example.selection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuLikedCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuLikedCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FunctionUser functionUser;
    private String TAG = "SMG";

    public MenuLikedCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dips_CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuLikedCardFragment newInstance(FunctionUser functionUser) {
        MenuLikedCardFragment fragment = new MenuLikedCardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionUser",functionUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            FunctionUser tempUser;
            tempUser = (FunctionUser) getArguments().getSerializable("functionUser");
            if(tempUser != null){functionUser = tempUser; Log.d(TAG, functionUser.getName() + "at MenuLikedCardFragment");}

        }catch (NullPointerException e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_liked_card, container, false);
    }
}