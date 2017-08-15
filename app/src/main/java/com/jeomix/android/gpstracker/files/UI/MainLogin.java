package com.jeomix.android.gpstracker.files.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.databinding.ActivityMainLoginBinding;
import com.jeomix.android.gpstracker.files.login.LoginFragment;
import com.jeomix.android.gpstracker.files.login.SignUpFragment;
import android.databinding.DataBindingUtil;

/**
 * Created by jeomix on 8/8/17.
 */

public class MainLogin extends AppCompatActivity {
    private ActivityMainLoginBinding binding;
    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_login, new LoginFragment())
                .replace(R.id.top_login, new LoginFragment())
                .replace(R.id.bottom_sign_up, new SignUpFragment())
                .replace(R.id.top_sign_up, new SignUpFragment())
                .commit();

        binding.topLogin.setRotation(-90);
        binding.topSignUp.setRotation(90);

        binding.button.setOnButtonSwitched(isLogin -> {
            binding.getRoot()
                    .setBackgroundColor(ContextCompat.getColor(
                            this,
                            isLogin ? R.color.colorPrimary : R.color.colorAccent));
        });

        binding.bottomLogin.setAlpha(0f);
        rotate(getCurrentFocus());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.topLogin.setPivotX(binding.topLogin.getWidth() / 2);
        binding.topLogin.setPivotY(binding.topLogin.getHeight());
        binding.topSignUp.setPivotX(binding.topLogin.getWidth() / 2);
        binding.topSignUp.setPivotY(binding.topLogin.getHeight());
    }

    public void rotate(View view) {
        if (isLogin) {
            binding.topLogin.setAlpha(1f);
            binding.topLogin.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.bottomLogin.setAlpha(1f);
                    binding.topLogin.setRotation(-90);
                }
            });
            binding.bottomSignUp.animate().alpha(0f);
            binding.bottomSignUp.setVisibility(View.GONE);
            binding.topSignUp.setVisibility(View.GONE);
        } else {
            binding.bottomSignUp.setVisibility(View.VISIBLE);
            binding.topSignUp.setVisibility(View.VISIBLE);
            binding.topSignUp.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.bottomSignUp.setAlpha(1f);
                    binding.topSignUp.setRotation(90);
                }
            });
            binding.bottomLogin.animate().alpha(0f);
        }

        isLogin = !isLogin;
        binding.button.startAnimation();
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }
}
