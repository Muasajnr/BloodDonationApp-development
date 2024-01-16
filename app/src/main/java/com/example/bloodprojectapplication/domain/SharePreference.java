package com.example.bloodprojectapplication.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePreference {
    private static final String BLOODGROUP = "com.example.bloodprojectapplication.bloodgroup";
    private static final String EMAIL = "com.example.bloodprojectapplication.email";
    private static final String NAME = "com.example.bloodprojectapplication.name";
    private static final String PHONENUMBER = "com.example.bloodprojectapplication.phoneNumber";
    private static final String TYPE = "com.example.bloodprojectapplication.type";


    private static SharePreference INSTANCE;
    private final SharedPreferences sharedPreferences;


    private SharePreference(SharedPreferences sharedPreferences) {
        //this.sharedPreferences = context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        this.sharedPreferences = sharedPreferences;

    }

    public static synchronized SharePreference getINSTANCE(Context context) {
        if (INSTANCE == null) {
            //noinspection deprecation
            INSTANCE = new SharePreference(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return INSTANCE;
    }

    public String getName() {
        return sharedPreferences.getString(NAME, "000000");
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(NAME, name).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "null");
    }

    public void setEmail(String email) {

        sharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public void setBloodgroup(String bloodgroup) {
        sharedPreferences.edit().putString(BLOODGROUP, bloodgroup).apply();
    }

    public String getBLOODGROUP() {
        return sharedPreferences.getString(BLOODGROUP, "null");
    }

    public String getPhonenumber() {
        return sharedPreferences.getString(PHONENUMBER, "null");
    }

    public void setPhonenumber(String phonenumber) {
        sharedPreferences.edit().putString(PHONENUMBER, phonenumber).apply();
    }

    public String getType() {
        return sharedPreferences.getString(TYPE, "null");
    }

    public void setType(String type) {
        sharedPreferences.edit().putString(TYPE, type).apply();
    }

}
