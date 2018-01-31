package com.js.sci.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.js.sci.dto.SCIData;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jskim on 2017. 9. 11..
 */

public class SCIDatabaseManager implements SCIDatabaseConstants {

    private static Object lock = new Object();

    private static SCIDatabaseManager instance;

    private SCISQLiteOpenHelper sQLiteOpenHelper;

    private SCIDatabaseManager() {
    }

    public static SCIDatabaseManager getInstance() {
        synchronized (lock) {
            if (null == instance) {
                instance = new SCIDatabaseManager();
            }
            return instance;
        }
    }

    public void init(@NonNull Context context) {
        sQLiteOpenHelper = new SCISQLiteOpenHelper(context);
    }

    public void clear() {
        sQLiteOpenHelper = null;
        instance = null;
    }

    private long insert(ContentValues values) {
        SQLiteDatabase db = sQLiteOpenHelper.getWritableDatabase();
        return db.insert(TABLE_CONCERT_DETAIL, null, values);
    }

    private void update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = sQLiteOpenHelper.getWritableDatabase();
        db.update(TABLE_CONCERT_DETAIL, values, whereClause, whereArgs);
    }

    private void delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = sQLiteOpenHelper.getWritableDatabase();
        db.delete(TABLE_CONCERT_DETAIL, whereClause, whereArgs);
    }

    private List<SCIData> select(String selection, String[] selectionArgs, String orderBy, String limit) {
        return select(null, selection, selectionArgs, orderBy, limit);
    }

    private List<SCIData> select(String[] columns, String selection,
                                 String[] selectionArgs, String orderBy, String limit) {

        ArrayList<SCIData> list = new ArrayList<SCIData>();
        SQLiteDatabase db = sQLiteOpenHelper.getWritableDatabase();

        Cursor c = db.query(TABLE_CONCERT_DETAIL, columns, selection, selectionArgs, null, null, orderBy, limit);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                list.add(convertSCIData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public long insertList(List<SCIData> list) {
        for (SCIData data : list) {
            if (insert(convertContentValues(data)) == -1) {
                SCILog.error("insert fail : " + data.toString());
                return -1;
            }
        }
        return list.size();
    }

    public long updateList(List<SCIData> list) {
        /*
        for (SCIData data : list) {
            if (insert(convertContentValues(data)) == -1) {
                SCILog.error("insert fail : " + data.toString());
                return -1;
            }
        }
        */
        return list.size();
    }

    public SCIData getData(String cultcode) {
        String selection = SCIUtils.getSelection(SELECT_TYPE.DATA);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.DATA, cultcode);
        String orderBy = SCIUtils.getOrderBy(SELECT_TYPE.DATA);
        String limit = SCIUtils.getLimit(SELECT_TYPE.DATA);
        List<SCIData> list = select(selection, selectionArgs, orderBy, limit);

        SCIData data = null;
        if (!list.isEmpty()) {
            data = list.get(0);
        }

        return data;
    }

    public List<SCIData> getList() {
        return getList(0);
    }

    public List<SCIData> getList(int offset) {
        String selection = SCIUtils.getSelection(SELECT_TYPE.LIST);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.LIST);
        String orderBy = SCIUtils.getOrderBy(SELECT_TYPE.LIST);
        String limit = SCIUtils.getLimit(SELECT_TYPE.LIST, offset);
        return select(selection, selectionArgs, orderBy, limit);
    }

    public int getCount() {
        String selection = SCIUtils.getSelection(SELECT_TYPE.LIST);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.LIST);

        List<SCIData> list = select(selection, selectionArgs, null, null);

        return list.size();
    }

    public List<SCIData> getSearchList(String keyword) {
        String selection = SCIUtils.getSelection(SELECT_TYPE.SEARCH, keyword);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.SEARCH);
        String orderBy = SCIUtils.getOrderBy(SELECT_TYPE.SEARCH);
        String limit = SCIUtils.getLimit(SELECT_TYPE.SEARCH);
        return select(selection, selectionArgs, orderBy, limit);
    }

    public List<SCIData> getBookmarkList() {
        String selection = SCIUtils.getSelection(SELECT_TYPE.BOOKMARKS);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.BOOKMARKS);
        String orderBy = SCIUtils.getOrderBy(SELECT_TYPE.BOOKMARKS);
        String limit = SCIUtils.getLimit(SELECT_TYPE.BOOKMARKS);
        return select(selection, selectionArgs, orderBy, limit);
    }

    public SCIData getBookmark() {
        String selection = SCIUtils.getSelection(SELECT_TYPE.BOOKMARK);
        String[] selectionArgs = SCIUtils.getSelectionArgs(SELECT_TYPE.BOOKMARK);
        String orderBy = SCIUtils.getOrderBy(SELECT_TYPE.BOOKMARK);
        String limit = SCIUtils.getLimit(SELECT_TYPE.BOOKMARK);
        List<SCIData> list = select(selection, selectionArgs, orderBy, limit);

        SCIData data = null;
        if (!list.isEmpty()) {
            data = list.get(0);
        }

        return data;
    }


    public void setBookmark(SCIData data) {
        String whereClause = CULTCODE + "=?";
        String[] whereArg = {data.getCULTCODE()};
        update(convertContentValues(data), whereClause, whereArg);
    }
    public void deleteAll() {
        delete(null, null);
    }

    private SCIData convertSCIData(Cursor c) {
        SCIData data = new SCIData();
        try {
            data.setCULTCODE(c.getString(0));
            data.setSUBJCODE(c.getString(1));
            data.setCODENAME(c.getString(2));
            data.setTITLE(c.getString(3));
            data.setSTRTDATE(c.getString(4));
            data.setEND_DATE(c.getString(5));
            data.setTIME(c.getString(6));
            data.setPLACE(c.getString(7));
            data.setORG_LINK(c.getString(8));
            data.setMAIN_IMG(c.getString(9));
            data.setHOMEPAGE(c.getString(10));
            data.setUSE_TRGT(c.getString(11));
            data.setUSE_FEE(c.getString(12));
            data.setSPONSOR(c.getString(13));
            data.setINQUIRY(c.getString(14));
            data.setSUPPORT(c.getString(15));
            data.setETC_DESC(c.getString(16));
            data.setAGELIMIT(c.getString(17));
            data.setIS_FREE(c.getString(18));
            data.setTICKET(c.getString(19));
            data.setPROGRAM(c.getString(20));
            data.setPLAYER(c.getString(21));
            data.setCONTENTS(c.getString(22));
            data.setGCODE(c.getString(23));
            data.setBOOKMARK(c.getString(24));
        } catch (Exception e) {

        }

        return data;
    }

    private ContentValues convertContentValues(SCIData data) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(CULTCODE, data.getCULTCODE());
            contentValues.put(SUBJCODE, data.getSUBJCODE());
            contentValues.put(CODENAME, data.getCODENAME());
            contentValues.put(TITLE, data.getTITLE());
            contentValues.put(STRTDATE, data.getSTRTDATE());
            contentValues.put(END_DATE, data.getEND_DATE());
            contentValues.put(TIME, data.getTIME());
            contentValues.put(PLACE, data.getPLACE());
            contentValues.put(ORG_LINK, data.getORG_LINK());
            contentValues.put(MAIN_IMG, data.getMAIN_IMG());
            contentValues.put(HOMEPAGE, data.getHOMEPAGE());
            contentValues.put(USE_TRGT, data.getUSE_TRGT());
            contentValues.put(USE_FEE, data.getUSE_FEE());
            contentValues.put(SPONSOR, data.getSPONSOR());
            contentValues.put(INQUIRY, data.getINQUIRY());
            contentValues.put(SUPPORT, data.getSUPPORT());
            contentValues.put(ETC_DESC, data.getETC_DESC());
            contentValues.put(AGELIMIT, data.getAGELIMIT());
            contentValues.put(IS_FREE, data.getIS_FREE());
            contentValues.put(TICKET, data.getTICKET());
            contentValues.put(PROGRAM, data.getPROGRAM());
            contentValues.put(PLAYER, data.getPLAYER());
            contentValues.put(CONTENTS, data.getCONTENTS());
            contentValues.put(GCODE, data.getGCODE());
            contentValues.put(BOOKMARK, data.getBOOKMARK());
        } catch (Exception e) {

        }

        return contentValues;
    }

    class SCISQLiteOpenHelper extends SQLiteOpenHelper {

        private SCISQLiteOpenHelper(@NonNull Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_CONCERT_DETAIL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_CONCERT_DETAIL);
            onCreate(db);
        }
    }
}
