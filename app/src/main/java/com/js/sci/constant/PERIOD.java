package com.js.sci.constant;

import com.js.sci.R;

/**
 * Created by a80099709 on 2017. 9. 13..
 */

public enum PERIOD {
    DAYS_3(3, R.string.period_1),
    DAYS_7(7, R.string.period_2),
    DAYS_14(14, R.string.period_3),
    DAYS_30(30, R.string.period_4),

    NONE(-1, -1);

    int resId;
    int day;

    PERIOD(int day, int resId) {
        this.day = day;
        this.resId = resId;
    }


    public int getDay() { return day;}

    public int getResId() {
        return resId;
    }
}
