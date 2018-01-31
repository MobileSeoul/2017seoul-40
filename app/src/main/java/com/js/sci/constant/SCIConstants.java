package com.js.sci.constant;

/**
 * Created by jskim on 2017. 9. 11..
 */

public interface SCIConstants  {
    String URL = "http://openAPI.seoul.go.kr:8088";
    String TYPE = "json";
    String KEY = "735a66695a72656434376c77784847";

    int COUNT = 200;
    String TAG_COUNT = "count";

    String KEY_FIRST = "key_first";
    String KEY_LAST_UPDATE = "key_last_update";
    String KEY_GENRE = "key_genre";
    String KEY_DISTRICT = "key_district";
    String KEY_PERIOD = "key_period";
    String KEY_LIST_COUNT = "key_list_count";

    String ALL = "0";

    String TYPE_STRING = "string";

    String IDENTIFIER_GENRE = "genre_";
    String IDENTIFIER_DISTRICT = "district_";
    String IDENTIFIER_PERIOD = "period_";
    String IDENTIFIER_LIST_COUNT = "list_count_";

    int REQUEST_CDDE_SETTING = 1000;
    int REQUEST_CDDE_SEARCH = 1001;
    int REQUEST_CDDE_BOOKMARK = 1002;
    int REQUEST_CDDE_DETAIL = 1003;

    int RESULT_DATABASE_FAIL = -2;
    int RESULT_RESET = -3;

    String EXTRA_DETAIL_DATA = "extra_detail_data";
    String EXTRA_CULTCODE = "extra_cultcode";
    String EXTRA_TITLE = "extra_title";

    String SEARCH_CONCERT_DETAIL_SERVICE = "SearchConcertDetailService";

    String FONT_PATTERN = "<font color=\"#FF4081\">%s</font>";

    String HASHTAG = "#";

    String IS_EXIST = "1";

    String[] CALL_REG_EX = {
            "\\d{2,3}[\\),-]\\d{3,4}-\\d{4}",
            "\\d{3,4}-\\d{4}"
    };
}
