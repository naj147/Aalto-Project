package com.jeomix.android.gpstracker.login;

/**
 * Created by jeomix on 8/8/17.
 */

public class MyBounceInterpolator implements android.view.animation.Interpolator  {
    private double amplitude = 1;
    private double frequency = 10;

    MyBounceInterpolator(double amp, double freq) {
        amplitude = amp;
        frequency = freq;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ amplitude) * Math.cos(frequency * time) + 1);
    }
}