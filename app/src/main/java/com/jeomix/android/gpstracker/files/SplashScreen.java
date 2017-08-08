package com.jeomix.android.gpstracker.files;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jeomix.android.gpstracker.MainLogin;
import com.jeomix.android.gpstracker.R;

public class SplashScreen extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fn_permission();
    }

    private  void forward(){
        Intent intent = new Intent(SplashScreen.this, MainLogin.class);
        startActivity(intent);
    }

    private void fn_permission() {
        final Context context=getApplicationContext();
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity)context, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
                    }
                });

            } else {
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
            forward();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;
                    forward();

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
