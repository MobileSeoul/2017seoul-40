package com.js.sci.constant;

import com.js.sci.R;

/**
 * Created by a80099709 on 2017. 9. 13..
 */

public enum LIST_COUNT {
    COUNT_10(10, R.string.list_count_1),
    COUNT_20(20, R.string.list_count_2),
    COUNT_30(30, R.string.list_count_3);

    int count;
    int resId;

    LIST_COUNT(int count, int resId) {
        this.count = count;
        this.resId = resId;
    }

    public int getCount() {
        return count;
    }

    public int getResId() {
        return resId;
    }
}
