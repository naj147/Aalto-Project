package com.jeomix.android.gpstracker.files.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.github.florent37.materialtextfield.MaterialTextField;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.UI.ErrorActivity;
import com.jeomix.android.gpstracker.files.UI.FragmentPagerSupport;
import com.jeomix.android.gpstracker.files.Helper.UserHelper;
import com.jeomix.android.gpstracker.files.MainActivity;
import com.jeomix.android.gpstracker.files.Objects.User;
import com.jeomix.android.gpstracker.files.UI.Main2Activity;
import com.jeomix.android.gpstracker.files.Utils;

import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity;
import io.saeid.fabloading.LoadingView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by jeomix on 8/8/17.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;
    private String Error="";
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Error="";
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.findViewById(R.id.loginParent).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            return true;
        });
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
                            assert user != null;
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
        MaterialTextField emailTextField= (MaterialTextField) view.findViewById(R.id.loginEmailExpand);
        MaterialTextField passTextField=(MaterialTextField) view.findViewById(R.id.loginPassExpand);
        TextInputLayout emailTextInputLayout= (TextInputLayout) view.findViewById(R.id.loginEmailInputLayout);
        TextInputLayout passTextInputLayout=(TextInputLayout) view.findViewById(R.id.loginPassInputLayout);
        emailTextField.expand();
        passTextField.expand();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string= s.toString();

                if(string.trim().length()<=0){
                    emailTextInputLayout.setErrorEnabled(true);
                    Error="You need to enter an email";
                    emailTextInputLayout.setError(Error);
                }
                else{
                        Error=Utils.emailError(string);
                        emailTextInputLayout.setError(Error);

                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string= s.toString();

                if(string.trim().length()<=0){
                    passTextInputLayout.setErrorEnabled(true);
                    Error="You need to enter an password";
                    passTextInputLayout.setError(Error);
                }
                else{
                    Error=Utils.passError(string);
                    passTextInputLayout.setError(Error);

                }
            }
        });

        LoadingView login = (LoadingView) view.findViewById(R.id.loginbuttonview);
        login.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.login, LoadingView.FROM_BOTTOM);
        FABProgressCircle fabProgressCircle = (FABProgressCircle) view.findViewById(R.id.loginFabProgressCircle);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircle.show();
                login.startAnimation();
                Error=Utils.formValidation(email,password,null);
                if(Error.isEmpty()){
                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(getActivity(), task -> {
                                Log.d(TAG, "s ignInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    fabProgressCircle.hide();
                                    Toast.makeText(getContext(),task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                }else{
                                    fabProgressCircle.beginFinalAnimation();
                                }

                                // ...
                            });
                }
                else{
                    fabProgressCircle.hide();
                        Snackbar.make(getView(),Error,Snackbar.LENGTH_SHORT).show();
                    Error="";
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
                case 3:
                    //Not yet Approved Admin
                    intent = new Intent(getContext(), ErrorActivity.class);
                    break;
                case 2:
                    //admin
                    intent = new Intent(getContext(), FragmentPagerSupport.class);
                    break;
                case 1:
                    //banned
                    intent = new Intent(getContext(), Main2Activity.class);
                    break;
            }
            if(intent!=null)
            startActivity(intent);

    }

}
