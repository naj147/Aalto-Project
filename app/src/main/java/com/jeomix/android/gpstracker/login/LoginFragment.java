package com.jeomix.android.gpstracker.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.ErrorActivity;
import com.jeomix.android.gpstracker.files.Helper.UserHelper;
import com.jeomix.android.gpstracker.files.Main2Activity;
import com.jeomix.android.gpstracker.files.MainActivity;
import com.jeomix.android.gpstracker.files.User;
import com.jeomix.android.gpstracker.files.Utils;

import io.saeid.fabloading.LoadingView;

/**
 * Created by jeomix on 8/8/17.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setupLoginButton(view);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                if(UserHelper.getCurrentUser()==null){
                    myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Retrieving User From DB
                                User user = dataSnapshot.getValue(User.class);
                                UserHelper.setCurrentUser(user);
                                router(user.getIsAdmin());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            router(3);
                        }

                    });
                }
                else{
                    // User is signed in
                    router(UserHelper.getCurrentUser().getIsAdmin());
                }

                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {

                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        };
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    private void setupLoginButton(View view) {
        email = (EditText) view.findViewById(R.id.loginEmailInputText);
        password = (EditText) view.findViewById(R.id.loginPassInputText);
        //TODO : EMAIL & PASS Verification , Email & pass input design, First Login Screen Design
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email.setHint("");
//            }
//        });
//       email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(email.getText().toString().isEmpty()){
//                    if(hasFocus){
//                        email.setHint("");
//                    }else{
//                        email.setHint(R.string.email);
//                    }
//                }
//            }
//        });
//        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(password.getText().toString().isEmpty()){
//                    if(hasFocus){
//                        password.setHint("");
//                    }else{
//                        password.setHint(R.string.password);
//                    }
//                }
//            }
//        });
        LoadingView login = (LoadingView) view.findViewById(R.id.loginbuttonview);
        login.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.login, LoadingView.FROM_BOTTOM);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.startAnimation();
                //TODO Login
                if(Utils.formValidation(email,password,null).isEmpty()){
                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(getActivity(), task -> {
                                Log.d(TAG, "s ignInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());

                                    Toast.makeText(getContext(),task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            });
                }


            }
        });
    }
    void router ( int route){
        Intent intent=null;
            switch (route){
                case 0:
                    //Vehicle
                    intent = new Intent(getContext(), MainActivity.class);
                    break;
                case 1:
                    //Not yet Approved Admin
                    intent = new Intent(getContext(), ErrorActivity.class);
                    break;
                case 2:
                    //admin
                    intent = new Intent(getContext(), Main2Activity.class);
                    break;
                case 3:
                    //banned
                    intent = new Intent(getContext(), ErrorActivity.class);
                    break;
            }
            if(intent!=null)
            startActivity(intent);

    }

}
