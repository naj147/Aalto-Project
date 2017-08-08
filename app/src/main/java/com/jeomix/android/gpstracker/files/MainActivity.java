/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeomix.android.gpstracker.files;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.Manifest;

import android.content.pm.PackageManager;

import android.net.Uri;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.jeomix.android.gpstracker.BuildConfig;
import com.jeomix.android.gpstracker.R;

import io.saeid.fabloading.LoadingView;


public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODEAFL = 34;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODEACL = 35;


    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // UI elements.
    private LoadingView mLoadViewMarker;
    private LoadingView mLoadViewVehicule;
    private EditText mTextLabel;
    private EditText mTextVin;
    private TextInputLayout til;

    //
    String Error="";

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
        myReceiver = new MyReceiver();
        mLoadViewMarker = (LoadingView) findViewById(R.id.tracker_view);
        mLoadViewVehicule=(LoadingView)findViewById(R.id.type_view);
        mTextLabel=(EditText)findViewById(R.id.labelField);
        mTextVin=(EditText)findViewById(R.id.vinField);
        til = (TextInputLayout)  findViewById(R.id.vinInputLayout);
        mTextVin.addTextChangedListener(new TextWatcher() {
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
                    til.setErrorEnabled(true);
                    Error="You need to enter a Vin";
                    til.setError(Error);
                }
                else{

                    Error="";
                    til.setErrorEnabled(false);
                }



            }
        });


        setupVehicules();

        }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        mLoadViewMarker.pauseAnimation();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) &&  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationaleAFL =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationaleACL =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationaleAFL) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODEAFL);
                        }
                    })
                    .show();
        } else if(shouldProvideRationaleACL){
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODEACL);
                        }
                    })
                    .show();
        }
                else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODEAFL);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODEACL);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODEAFL || requestCode == REQUEST_PERMISSIONS_REQUEST_CODEACL) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                setButtonsState(false);
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(MainActivity.this, Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        mLoadViewMarker.resumeAnimation();
        //Verify the state of the app to figure out if icon should show activated on deactivated marker drawable

        if(requestingLocationUpdates){
            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_LEFT);
            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_RIGHT);
            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_TOP);
            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_BOTTOM);
        }else
        {
            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_BOTTOM);
            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_LEFT);
            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_RIGHT);
            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_TOP);
        }
        mLoadViewMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Error.trim().length()<=0){
                    mLoadViewMarker.startAnimation();
                    mLoadViewMarker.addListener(new LoadingView.LoadingListener() {
                        @Override
                        public void onAnimationStart(int currentItemPosition) {

                        }

                        @Override
                        public void onAnimationRepeat(int nextItemPosition) {

                        }

                        @Override
                        public void onAnimationEnd(int nextItemPosition) {
                            int position=0;
                            if(!mLoadViewMarker.getDrawable().getConstantState().equals(ContextCompat.getDrawable(getApplicationContext(),R.drawable.if_marker_on).getConstantState())){
                                Toast.makeText(getApplicationContext(),"Marker ON",Toast.LENGTH_SHORT).show();
                                if (!checkPermissions()) {
                                    requestPermissions();
                                } else {
                                    mService.requestLocationUpdates();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Marker OFF",Toast.LENGTH_SHORT).show();
                                mService.removeLocationUpdates();
                            }

                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ERROR: "+ Error,Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
public void setupVehicules() {

    mLoadViewVehicule.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_motorcycle ,LoadingView.FROM_BOTTOM);
    mLoadViewVehicule.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_car, LoadingView.FROM_LEFT);
    mLoadViewVehicule.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_schooolbus, LoadingView.FROM_RIGHT);
    mLoadViewVehicule.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_truck, LoadingView.FROM_TOP);

    mLoadViewVehicule.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadViewVehicule.startAnimation();
            mLoadViewVehicule.addListener(new LoadingView.LoadingListener() {
                @Override
                public void onAnimationStart(int currentItemPosition) {

                }

                @Override
                public void onAnimationRepeat(int nextItemPosition) {

                }

                @Override
                public void onAnimationEnd(int nextItemPosition) {
                }
            });
        }
    });
}
//    private void loadingState(Boolean ON){
//        mLoadViewMarker.clearAnimation();
//        mLoadViewMarker.removeAnimation(0);
//        mLoadViewMarker.removeAnimation(1);
//        mLoadViewMarker.removeAnimation(2);
//        mLoadViewMarker.removeAnimation(3);
//
//        if(ON){
//            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_RIGHT);
//            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_TOP);
//            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_BOTTOM);
//            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_LEFT);
//            mLoadViewMarker.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mLoadViewMarker.startAnimation();
//                    mLoadViewMarker.addListener(new LoadingView.LoadingListener() {
//                        @Override
//                        public void onAnimationStart(int currentItemPosition) {
////                            if(currentItemPosition==3)
////                                mLoadViewMarker.setRepeat(1);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(int nextItemPosition) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(int nextItemPosition) {
//                            if(nextItemPosition== 1 || nextItemPosition== 3){
//                                Toast.makeText(getApplicationContext(),"Marker ON",Toast.LENGTH_SHORT).show();
//                                if (!checkPermissions()) {
//                                    requestPermissions();
//                                } else {
//                                    mService.requestLocationUpdates();
//                                }
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Marker OFF",Toast.LENGTH_SHORT).show();
//                                mService.removeLocationUpdates();
//                            }
//
//                        }
//                    });
//                }
//            });
//        }else{
//
//            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_RIGHT);
//            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_TOP);
//            mLoadViewMarker.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.if_marker_on, LoadingView.FROM_BOTTOM);
//            mLoadViewMarker.addAnimation(Color.parseColor("#FF4218"), R.drawable.if_marker_off, LoadingView.FROM_LEFT);
//            mLoadViewMarker.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mLoadViewMarker.startAnimation();
//                    mLoadViewMarker.addListener(new LoadingView.LoadingListener() {
//                        @Override
//                        public void onAnimationStart(int currentItemPosition) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(int nextItemPosition) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(int nextItemPosition) {
//                            if(nextItemPosition== 0 || nextItemPosition== 2){
//                                Toast.makeText(getApplicationContext(),"Marker ON",Toast.LENGTH_SHORT).show();
//                                if (!checkPermissions()) {
//                                    requestPermissions();
//                                } else {
//                                    mService.requestLocationUpdates();
//                                }
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Marker OFF",Toast.LENGTH_SHORT).show();
//                                mService.removeLocationUpdates();
//                            }
//
//                        }
//                    });
//                }
//            });
//        }
//
//    }
}
