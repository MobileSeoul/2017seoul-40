package com.js.sci.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.js.sci.SCIApplication;
import com.js.sci.constant.DISTRICT;
import com.js.sci.constant.GENRE;
import com.js.sci.constant.LIST_COUNT;
import com.js.sci.constant.PERIOD;
import com.js.sci.constant.SCIConstants;
import com.js.sci.database.SCIDatabaseConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by a80099709 on 2017. 9. 14..
 */

public class SCIUtils implements SCIConstants, SCIDatabaseConstants {

    public static String getToadyString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static String getNextString() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, PERIOD.valueOf(SCIPreferences.getInstance().getPeriod()).getDay());
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getSelection(SELECT_TYPE type, String... obj) {
        String selection = new String();
        switch (type) {
            case LIST:
                selection += STRTDATE + " " + LE + " " + AND + " " + END_DATE + " " + GE;
                selection += GENRE.ALL.equals(GENRE.valueOf(SCIPreferences.getInstance().getGenre())) ? "" : " " + AND + " " + SUBJCODE + " " + EQ;
                selection += DISTRICT.All.equals(DISTRICT.valueOf(SCIPreferences.getInstance().getDistrict())) ? "" : " " + AND + " " + GCODE + " " + EQ;
                break;
            case BOOKMARK:
            case BOOKMARKS:
                selection += END_DATE + " " + GE;
                selection += " " + AND + " " + BOOKMARK + " " + EQ;
                break;
            case SEARCH:
                selection += "(" + CODENAME + " || " + TITLE + " || " + PROGRAM + " || " + CONTENTS + " || " + PLACE + ") " + LIKE + " '%" + obj[0] + "%'";
                break;
            case DATA:
                selection += CULTCODE + " " + EQ;
                break;
        }

        SCILog.debug("getSelection : " + selection);

        return selection;
    }

    public static String[] getSelectionArgs(SELECT_TYPE type, String... obj) {
        ArrayList<String> selectionArgs = new ArrayList<>();
        switch (type) {
            case LIST:
                selectionArgs.add(getNextString());
                selectionArgs.add(getToadyString());
                if (!GENRE.ALL.equals(GENRE.valueOf(SCIPreferences.getInstance().getGenre()))) {
                    selectionArgs.add(GENRE.valueOf(SCIPreferences.getInstance().getGenre()).getCode());
                }

                if (!DISTRICT.All.equals(DISTRICT.valueOf(SCIPreferences.getInstance().getDistrict()))) {
                    selectionArgs.add(SCIApplication.getContext().getString(DISTRICT.valueOf(SCIPreferences.getInstance().getDistrict()).getResId()));
                }
                break;
            case BOOKMARK:
            case BOOKMARKS:
                selectionArgs.add(getToadyString());
                selectionArgs.add(IS_EXIST);
                break;
            case SEARCH:
                break;
            case DATA:
                selectionArgs.add(obj[0]);
                break;
        }

        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    public static String getOrderBy(SELECT_TYPE type) {
        String orderBy = null;
        switch (type) {
            case LIST:
            case BOOKMARK:
            case BOOKMARKS:
            case SEARCH:
                orderBy = END_DATE + " " + ASC + " , " + STRTDATE + " " + DESC;
                break;
            case DATA:
                break;
        }
        return orderBy;
    }

    public static String getLimit(SELECT_TYPE type) {
        return getLimit(type, 0);
    }

    public static String getLimit(SELECT_TYPE type, int offset) {
        String limit = null;
        switch (type) {
            case LIST:
                limit = offset + " , " + String.valueOf(LIST_COUNT.valueOf(SCIPreferences.getInstance().getListCount()).getCount());
                break;
            case BOOKMARK:
                limit = "1";
                break;
            case BOOKMARKS:
            case SEARCH:
                break;
            case DATA:
                limit = "1";
                break;
        }

        return limit;
    }

    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                isConnected = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                isConnected = true;
            } else {
                // not connected to the internet
                isConnected = false;
            }
        } else {
            isConnected = false;
        }
        return isConnected;
    }
}
