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


import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.jeomix.android.gpstracker.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPhone(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }

    public static String emailError(CharSequence emailSeq){
        String emailString = emailSeq.toString().trim();
        if(!Utils.isValidEmail(emailString))
            return "Email is not well formated ";
        return "";
    }
    public static  String passError(CharSequence passSeq){
        String passString=passSeq.toString();
        if(passString.length()<6)
            return "Password has less than 6 Character ";
        return "";
    }
    public static String passVer(CharSequence passSeq,CharSequence passConfSeq){
        if(!passSeq.toString().equals(passConfSeq.toString()))
            return "Password Confirmation Doesn't Match The First One Entered ";
        return "";
    }
    public static String formValidation(EditText email, EditText pass , EditText passConf ){
        String error = "";
        error += emailError(email.getText());
        error+= passError(pass.getText());
        if(passConf != null){
            error+= passError(passConf.getText());
            error+= passVer(pass.getText(),passConf.getText());
        }

        return error;
    }

    public static Long convertStringToTimestamp(String str_date) {
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            // you can change format of date
            Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            Long time = timeStampDate.getTime();
            return time;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }
}
