package com.atoi.touchlock.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionSharedPreferances {
    private SharedPreferences prefs;

    public SessionSharedPreferances(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserId(long userId) {
        prefs.edit().putLong("userId", userId).apply();
    }

    public long getUserId() {
        long userId = prefs.getLong("userId", 0);
        return userId;
    }

    public void setEmailSession(String email) {
        prefs.edit().putString("email", email).apply();
    }

    public String getEmail() {
        String email = prefs.getString("email","ozan@gmail.com");
        return email;
    }
}
