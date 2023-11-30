package com.example.selection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selection.databinding.ActivityAddCardChooseCompanyBinding;

import java.util.ArrayList;


public class MenuAddCardChooseCompanyFragment extends Fragment implements View.OnClickListener {

    private ArrayList<String> companyToEnrollList = new ArrayList<>();
    private FunctionUser functionUser;
    private ActivityAddCardChooseCompanyBinding binding;
    private boolean isSelectedShinhan = false;
    private boolean isSelectedKookmin = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuAddCardChooseCompanyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuAddCardChooseCompanyFragment newInstance(String param1, String param2) {
        MenuAddCardChooseCompanyFragment fragment = new MenuAddCardChooseCompanyFragment();
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
        binding = ActivityAddCardChooseCompanyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_add__card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        functionUser = (FunctionUser) requireActivity().getIntent().getSerializableExtra("functionUser");

        binding.shinhanButton.setOnClickListener(this);
        binding.kookminButton.setOnClickListener(this);

        binding.goNextButton.setOnClickListener(this);
        binding.hyundaiButton.setOnClickListener(this);
        binding.nonghyupButton.setOnClickListener(this);
        binding.hanaButton.setOnClickListener(this);
        binding.lotteButton.setOnClickListener(this);
        binding.wooriButton.setOnClickListener(this);
        binding.bcButton.setOnClickListener(this);
        binding.samsungButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.goNextButton) {
            Log.d("SMG", "YES");
            startActivity(new Intent(requireActivity(), WelcomeAddCardChooseCard.class)
                    .putExtra("CompanyToEnrollList", companyToEnrollList)
                    .putExtra("functionUser", functionUser));
        } else if (v == binding.shinhanButton) {
            if (!isSelectedShinhan) {
                companyToEnrollList.add("신한");
                binding.shinhanButton.setBackgroundResource(R.drawable.round_bold);
                isSelectedShinhan = true;
            } else {
                companyToEnrollList.remove("신한");
                binding.shinhanButton.setBackgroundResource(R.drawable.round);
                isSelectedShinhan = false;
            }
        } else if (v == binding.kookminButton) {
            if (!isSelectedKookmin) {
                companyToEnrollList.add("KB국민");
                binding.kookminButton.setBackgroundResource(R.drawable.round_bold);
                isSelectedKookmin = true;
            } else {
                companyToEnrollList.remove("KB국민");
                binding.kookminButton.setBackgroundResource(R.drawable.round);
                isSelectedKookmin = false;
            }
        } else {
            Toast.makeText(requireContext(), "추후 지원될 예정입니다.", Toast.LENGTH_SHORT).show();
        }
    }
}