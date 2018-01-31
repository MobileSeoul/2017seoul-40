package com.js.sci.constant;

import com.js.sci.R;

/**
 * Created by jskim on 2017. 9. 11..
 */

public enum DISTRICT {
    All         ("0",          R.string.all),
    Jongno      ("1111000000", R.string.district_1111000000),
    Jung        ("1114000000", R.string.district_1114000000),
    Yongsan     ("1117000000", R.string.district_1117000000),
    Seongdong   ("1120000000", R.string.district_1120000000),
    Gwangjin    ("1121500000", R.string.district_1121500000),
    Dongdaemun  ("1123000000", R.string.district_1123000000),
    Jungnang    ("1126000000", R.string.district_1126000000),
    Seongbuk    ("1129000000", R.string.district_1129000000),
    Gangbuk     ("1130500000", R.string.district_1130500000),
    Dobong      ("1132000000", R.string.district_1132000000),
    Nowon       ("1135000000", R.string.district_1135000000),
    Eunpyeong   ("1138000000", R.string.district_1138000000),
    Seodaemun   ("1141000000", R.string.district_1141000000),
    Mapo        ("1144000000", R.string.district_1144000000),
    Yangcheon   ("1147000000", R.string.district_1147000000),
    Gangseo     ("1150000000", R.string.district_1150000000),
    Guro        ("1153000000", R.string.district_1153000000),
    Geumcheon   ("1154500000", R.string.district_1154500000),
    Yeongdeungpo("1156000000", R.string.district_1156000000),
    Dongjak     ("1159000000", R.string.district_1159000000),
    Gwanak      ("1162000000", R.string.district_1162000000),
    Seocho      ("1165000000", R.string.district_1165000000),
    Gangnam     ("1168000000", R.string.district_1168000000),
    Songpa      ("1171000000", R.string.district_1171000000),
    Gangdong    ("1174000000", R.string.district_1174000000),
    Default     ("1100000000", R.string.district_1100000000);


    String code;
    int resId;

    DISTRICT(String code, int resId) {
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
