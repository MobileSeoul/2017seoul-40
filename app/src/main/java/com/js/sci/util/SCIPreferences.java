package com.js.sci.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.js.sci.constant.DISTRICT;
import com.js.sci.constant.GENRE;
import com.js.sci.constant.LIST_COUNT;
import com.js.sci.constant.PERIOD;
import com.js.sci.constant.SCIConstants;

/**
 * Created by a80099709 on 2017. 9. 13..
 */

public class SCIPreferences implements SCIConstants{

    private static final Object lock = new Object();
    private static SCIPreferences instance;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public static SCIPreferences getInstance() {
        synchronized (lock) {
            if (null == instance) {
                instance = new SCIPreferences();
            }
            return instance;
        }
    }

    private SCIPreferences() {
    }

    public void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public void clear() {
        editor = null;
        preferences = null;
        instance = null;
    }

    private synchronized boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    private synchronized void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    private synchronized long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    private synchronized void setLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    private synchronized String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    private synchronized void setString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public boolean getFirst(){
        return getBoolean(KEY_FIRST, true);
    }
    public void setFirst(boolean value) {
        setBoolean(KEY_FIRST, value);
    }

    public long getLastUpdate() {
        return getLong(KEY_LAST_UPDATE, 0);
    }

    public void setLastUpdate(long value) {
        editor.putLong(KEY_LAST_UPDATE, value).commit();
    }
    public String getGenre(){
        return getString(KEY_GENRE, GENRE.ALL.name());
    }
    public void setGenre(String value) {
        setString(KEY_GENRE, value);
    }
    public String getDistrict(){
        return getString(KEY_DISTRICT, DISTRICT.All.name());
    }
    public void setDistrict(String value) {
        setString(KEY_DISTRICT, value);
    }
    public String getPeriod(){
        return getString(KEY_PERIOD, PERIOD.DAYS_7.name());
    }
    public void setPeriod(String value) {
        setString(KEY_PERIOD, value);
    }
    public String getListCount(){
        return getString(KEY_LIST_COUNT, LIST_COUNT.COUNT_20.name());
    }
    public void setListCount(String value) {
        setString(KEY_LIST_COUNT, value);
    }

    public void reset() {
        editor.clear().commit();
    }
}
