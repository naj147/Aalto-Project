package com.jeomix.android.gpstracker.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.MainActivity;

import io.saeid.fabloading.LoadingView;

/**
 * Created by jeomix on 8/8/17.
 */

public class LoginFragment extends Fragment{

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        setupLoginButton(view);
        return view;
    }
    private void setupLoginButton(View view){
        LoadingView login= (LoadingView) view.findViewById(R.id.loginbuttonview);
        login.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.login ,LoadingView.FROM_BOTTOM);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.startAnimation();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                //TODO Login
            }
        });

    }
}
