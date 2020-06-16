package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    //creating objects for UI components
    EditText loginIdET;
    EditText loginPasswordET;
    CheckBox loginCaptainCB;
    Button loginBtn;
    CheckBox keepLoggedInCB;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JSONObject data;
    Context mContext;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing UI component variables with corresponding components
        loginIdET = view.findViewById(R.id.loginIdET);
        loginPasswordET = view.findViewById(R.id.loginPasswordET);
        loginCaptainCB = view.findViewById(R.id.loginCaptainCB);
        loginBtn = view.findViewById(R.id.loginBtn);
        keepLoggedInCB = view.findViewById(R.id.keepLoggedInCB);

        //Intializing JSON object
        data = new JSONObject();

        //setting onClickListener to login Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid = isValid();
            }
        });
    }

    //function to validate the details given by the user
    private Boolean isValid() {
        //inserting user given values into JSON object
        try{
            String username=loginIdET.getText().toString();
            String password=loginPasswordET.getText().toString();
            Boolean captain=loginCaptainCB.isChecked();
            data.put("username",username);
            data.put("password",password);
        }catch(Exception e){
            e.printStackTrace();
        }

        String url="http://ec2-3-7-131-60.ap-south-1.compute.amazonaws.com/login";
        queue = Volley.newRequestQueue(mContext);
        objectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accessTkn = null;
                        try {
                            accessTkn = response.getString("access_token");
                            if(keepLoggedInCB.isChecked()){
                                SharedPreferences sharedPref = mContext.getSharedPreferences(
                                        getString(R.string.user_id), Context.MODE_PRIVATE);
                                String uid = sharedPref.getString("user_id","");
                                if(uid==""){
                                    Toast.makeText(mContext,"uid is empty",Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    String newuid = "160117733167";
                                    editor.putInt(getString(R.string.user_id), Integer.parseInt(newuid));
                                    editor.commit();
                                }else{
                                    Toast.makeText(mContext,uid,Toast.LENGTH_LONG).show();
                                }

                            }
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginIdET.setText("");
                loginPasswordET.setText("");
                if(keepLoggedInCB.isChecked()){
                    SharedPreferences sharedPref = mContext.getSharedPreferences(
                            getString(R.string.user_id), Context.MODE_PRIVATE);
                    String uid = sharedPref.getString("user_id","");
                    if(uid==""){
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_id), "kk"+loginIdET.getText().toString()).apply();
                        editor.commit();
                        uid = sharedPref.getString("user_id","111");
                        Toast.makeText(mContext,"hello"+uid,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(mContext,uid,Toast.LENGTH_LONG).show();
                    }

                }
                //Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(mContext,"Please check your credentials and try again",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(objectRequest);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}
