package com.jeomix.android.gpstracker.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.jeomix.android.gpstracker.R;


import io.saeid.fabloading.LoadingView;

/**
 * Created by jeomix on 8/8/17.
 */

public class SignUpFragment extends Fragment {

    static int choice;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        setupSignUpButtons(view);
        return view;
    }

    private void setupSignUpButtons(View view) {
        LoadingView type = (LoadingView) view.findViewById(R.id.choicebuttonview);
        LoadingView signupButton = (LoadingView) view.findViewById(R.id.signupbuttonview);

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
                            Toast.makeText(getContext(), "Admin", Toast.LENGTH_SHORT).show();
                        } else {
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

            }
        });

    }
}
