package com.jeomix.android.gpstracker.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.Helper.UserHelper;
import com.jeomix.android.gpstracker.files.User;
import com.jeomix.android.gpstracker.files.Utils;


import io.saeid.fabloading.LoadingView;

/**
 * Created by jeomix on 8/8/17.
 */

public class SignUpFragment extends Fragment {
    private static final String TAG ="SignUpFragment" ;
     int choice=1;
    EditText email,pass,passConf;
    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private String Error="";
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        setupSignUpButtons(view);
        return view;
    }

    private void setupSignUpButtons(View view) {
        Error="";
        LoadingView type = (LoadingView) view.findViewById(R.id.choicebuttonview);
        LoadingView signupButton = (LoadingView) view.findViewById(R.id.signupbuttonview);
        TextView textlabel=(TextView) view.findViewById(R.id.textlabelsignup);
        email=(EditText) view.findViewById(R.id.signupEmailInputText);
        pass=(EditText) view.findViewById(R.id.signupPassInputText);
        passConf=(EditText) view.findViewById(R.id.signupPassConfInputText);
        type.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.vehicule, LoadingView.FROM_BOTTOM);
        type.addAnimation(Color.parseColor("#FF4218"), R.drawable.admin, LoadingView.FROM_LEFT);
        MaterialTextField emailTextField= (MaterialTextField) view.findViewById(R.id.signupEmailExpand);
        MaterialTextField passTextField=(MaterialTextField) view.findViewById(R.id.signupPassExpand);
        TextInputLayout emailTextInputLayout= (TextInputLayout) view.findViewById(R.id.signupEmailInputLayout);
        TextInputLayout passTextInputLayout=(TextInputLayout) view.findViewById(R.id.signupPassInputLayout);
        MaterialTextField passConfTextField=(MaterialTextField) view.findViewById(R.id.signupPassConfExpand);
        TextInputLayout passConfTextInputLayout= (TextInputLayout) view.findViewById(R.id.signupPassConfInputLayout);

        emailTextField.expand();
        passTextField.expand();
        passConfTextField.expand();

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
        pass.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            String string = s.toString();

                                            if (string.trim().length() <= 0) {
                                                passTextInputLayout.setErrorEnabled(true);
                                                Error = "You need to enter an password";
                                                passTextInputLayout.setError(Error);
                                            } else {
                                                Error = Utils.passError(string);
                                                passTextInputLayout.setError(Error);

                                            }
                                        }
                                    });
        passConf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();

                if (string.trim().length() <= 0) {
                    passConfTextInputLayout.setErrorEnabled(true);
                    Error = "You need to enter an password";
                    passConfTextInputLayout.setError(Error);
                } else {
                    Error = Utils.passError(string);
                    passConfTextInputLayout.setError(Error);

                }
            }
        });

        type.setOnClickListener(v -> {
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
                        Toast.makeText(getContext(), "Admin", Toast.LENGTH_SHORT).show();
                        textlabel.setText("Admin");
                    } else {
                        Toast.makeText(getContext(), "Vehicle", Toast.LENGTH_SHORT).show();
                        textlabel.setText("Vehicle");
                    }
                }
            });
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
                                        User user = new User(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
                                        user.setIsAdmin(1-choice);
                                        DatabaseReference myRef = mDatabase.getReference("users");
                                        myRef.child(user.getId()).setValue(user, (databaseError, databaseReference) -> {
                                          if(databaseError==null){
                                              UserHelper.setCurrentUser(user);
                                              fabProgressCircle.beginFinalAnimation();
                                              Intent intent = getActivity().getIntent();
                                              getActivity().finish();
                                              startActivity(intent);
                                          }else{
                                              //TODO : SOMETHING WENT WRONG PAGE REFRESH YOUR CONNECTION MESSAGE :)
                                          }

                                        });

                                    }

                                    // ...
                                }
                            });
                }else{
                    Snackbar.make(getView(),Error,Snackbar.LENGTH_SHORT).show();
                    Error="";
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
