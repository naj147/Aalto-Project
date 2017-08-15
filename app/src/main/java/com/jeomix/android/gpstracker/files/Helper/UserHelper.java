package com.jeomix.android.gpstracker.files.Helper;

import com.jeomix.android.gpstracker.files.Objects.User;

/**
 * Created by jeomix on 8/10/17.
 */

public class UserHelper {
    static User currentUser=null;
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserHelper.currentUser = currentUser;
    }
}
