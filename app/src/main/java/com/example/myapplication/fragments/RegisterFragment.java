package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.R;

import org.json.JSONObject;

public class RegisterFragment extends Fragment {

    //creating variables for ui components
    EditText registerIdET;
    EditText registerNameET;
    EditText registerPhoneET;
    EditText registerMailET;
    Spinner yearSpinner;
    Spinner branchSpinner;
    Spinner sectionSpinner;
    Button registerBtn;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JSONObject data;
    Context mContext;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing ui variables with corresponding components
        registerIdET = view.findViewById(R.id.registerIdET);
        registerNameET = view.findViewById(R.id.registerNameET);
        registerPhoneET = view.findViewById(R.id.registerPhoneET);
        registerMailET = view.findViewById(R.id.registerMailET);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        branchSpinner = view.findViewById(R.id.branchSpinner);
        sectionSpinner = view.findViewById(R.id.sectionSpinner);
        registerBtn = view.findViewById(R.id.registerBtn);

        //Intializing JSON object
        data = new JSONObject();

        //setting onClickListener to login Button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { register();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void register() {
        try {
            Long userId = Long.parseLong(registerIdET.getText().toString());
            String userName = registerNameET.getText().toString();
            Long userPhone = Long.parseLong(registerPhoneET.getText().toString());
            String userMail = registerMailET.getText().toString();
            String userYear = yearSpinner.getSelectedItem().toString();
            String userBranch = branchSpinner.getSelectedItem().toString();
            Integer userSection = Integer.parseInt(sectionSpinner.getSelectedItem().toString());
            if(checkDetails(userId, userName, userPhone, userMail)&&matchDetails(userId,userMail,userBranch)){
                Toast.makeText(mContext,"successfully registered",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(mContext,"please check your details",Toast.LENGTH_LONG).show();
        }
    }

    private boolean matchDetails(Long userId, String userMail,String userBranch) {
        String Id = userId.toString();
        if(!Id.substring(4,6).equals(userMail.substring(3,5))){
            Toast.makeText(mContext,"user ID and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else if(!Id.substring(9,12).equals(userMail.substring(5,8))){
            Toast.makeText(mContext,"user ID and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else if(!userMail.substring(9,9+userBranch.length()).equals(userBranch.toLowerCase())){
            Toast.makeText(mContext,"user branch and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private Boolean checkDetails(Long userId, String userName, Long userPhone, String userMail) {
        return true;
    }
}
