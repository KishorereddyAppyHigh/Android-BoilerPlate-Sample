package com.appyhigh.sampleboilerplateapplication.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
    private Context context;
    SharedPreferences sharedPreferences;

    public SharedPreferenceUtil(Context context) {
        this.context = context;
    }


    public void saveString(String key, String value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.apply();
    }

    public void saveInt(String key , int value){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key , value);
        editor.apply();
    }

    public void saveBoolean(String key , boolean value){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key , value);
        editor.apply();
    }

    public String getStringValue(String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,null);
    }

    public int getIntValue(String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key,1);
    }

    public boolean getBooleanValue(String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key,true);
    }

    public void clearSharedPreferenceFile(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
    }

}
