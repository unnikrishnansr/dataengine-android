package com.attinad.analyticsengine.core.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arun.chand on 23-02-2017.
 */
public class SharedPreferenceManager {
    private static volatile SharedPreferenceManager sInstance;
    private SharedPreferences sharedPreferences;


    /**
     * Provides the singleton instance of the {@link SharedPreferenceManager}.
     *
     * @param context The application context
     * @return The SharedPreferenceManager instance
     */
    public static SharedPreferenceManager getInstance(final Context context) {
        SharedPreferenceManager manager = sInstance;
        if (manager == null) {
            synchronized (SharedPreferenceManager.class) {
                manager = sInstance;
                if (manager == null) {
                    manager = new SharedPreferenceManager(context);
                    sInstance = manager;
                }
            }
        }
        return manager;
    }

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("aopandroid", Context.MODE_PRIVATE);
    }


    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public String getData(String key,String defvalue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defvalue);
        }
        return "";
    }

    public void saveBool(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public boolean getBool(String key, boolean defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public void clearPreferences() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        if (prefsEditor != null) {
            prefsEditor.clear();
            prefsEditor.commit();
        }
    }

}

