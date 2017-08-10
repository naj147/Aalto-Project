package com.jeomix.android.gpstracker.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.Utils;


import io.saeid.fabloading.LoadingView;

/**
 * Created by jeomix on 8/8/17.
 */

public class SignUpFragment extends Fragment {
    private static final String TAG ="SignUpFragment" ;
    int userType=0;
    static int choice;
    EditText email,pass,passConf;
    private FirebaseAuth mAuth;
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mAuth = FirebaseAuth.getInstance();
        setupSignUpButtons(view);
        return view;
    }

    private void setupSignUpButtons(View view) {
        LoadingView type = (LoadingView) view.findViewById(R.id.choicebuttonview);
        LoadingView signupButton = (LoadingView) view.findViewById(R.id.signupbuttonview);
        email=(EditText) view.findViewById(R.id.signupEmailInputText);
        pass=(EditText) view.findViewById(R.id.signupPassInputText);
        passConf=(EditText) view.findViewById(R.id.signupPassConfInputText);
        type.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.vehicule, LoadingView.FROM_BOTTOM);
        type.addAnimation(Color.parseColor("#FF4218"), R.drawable.admin, LoadingView.FROM_LEFT);

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.startAnimation();
                type.addListener(new LoadingView.LoadingListener() {
                    @Override
                    public void onAnimationStart(int currentItemPosition) {

                    }

                    @Override
                    public void onAnimationRepeat(int nextItemPosition) {

                    }

                    @Override
                    public void onAnimationEnd(int nextItemPosition) {
                        choice = nextItemPosition;
                        if (choice == 0) {
                            userType=0;
                            Toast.makeText(getContext(), "Admin", Toast.LENGTH_SHORT).show();
                        } else {
                            userType=1;
                            Toast.makeText(getContext(), "Vehicle", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        FABProgressCircle fabProgressCircle = (FABProgressCircle) view.findViewById(R.id.fabProgressCircle);
        signupButton.addAnimation(Color.parseColor("#FF4218"), R.drawable.save, LoadingView.FROM_LEFT);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Load the data in DB
                fabProgressCircle.show();
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                if(Utils.formValidation(email,pass,passConf).isEmpty()){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signIUpWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signUpWithEmail:failed", task.getException());
                                        Toast.makeText(getContext(), "SignUpFailed",Toast.LENGTH_SHORT).show();
                                    }else{
                                        fabProgressCircle.beginFinalAnimation();
                                        Intent intent = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intent);
                                    }

                                    // ...
                                }
                            });
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
