package com.js.sci.constant;

import com.js.sci.R;

/**
 * Created by jskim on 2017. 9. 11..
 */

public enum GENRE {
    ALL("0", R.string.all),
    CODE01("1", R.string.genre_1),
    CODE02("2", R.string.genre_2),
    CODE03("3", R.string.genre_3),
    CODE05("5", R.string.genre_5),
    CODE06("6", R.string.genre_6),
    CODE07("7", R.string.genre_7),
    CODE10("10", R.string.genre_10),
    CODE11("11", R.string.genre_11),
    CODE12("12", R.string.genre_12),
    CODE17("17", R.string.genre_17),
    CODE18("18", R.string.genre_18),
    CODE19("19", R.string.genre_19),

    NONE("0", R.string.all);

    String code;
    int resId;

    GENRE(String code, int resId) {
        this.code = code;
        this.resId = resId;
    }

    public String getCode() {
        return code;
    }

    public int getResId() {
        return resId;
    }
}
