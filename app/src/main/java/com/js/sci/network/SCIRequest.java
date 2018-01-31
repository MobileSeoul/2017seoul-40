package com.js.sci.network;

import com.js.sci.constant.SCIConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jskim on 2017. 9. 11..
 */

public class SCIRequest implements SCIConstants {
    private int START_INDEX;
    private int END_INDEX;
    private String CULTCODE;

    public SCIRequest(int START_INDEX, int END_INDEX) {
        this(START_INDEX, END_INDEX, null);
    }

    public SCIRequest(int START_INDEX, int END_INDEX, String CULTCODE) {
        this.START_INDEX = START_INDEX;
        this.END_INDEX = END_INDEX;
        this.CULTCODE = CULTCODE;
    }

    protected String getDefaultUrl() {
        return URL + "/" + KEY + "/" + TYPE + "/" + SEARCH_CONCERT_DETAIL_SERVICE + "/" + START_INDEX + "/" + END_INDEX + "/";
    }

    public String getUrl() {
        return getDefaultUrl() + (CULTCODE != null ? CULTCODE + "/" : "");
    }

    public Map<String, String> getHeader() {
        return new HashMap<>();
    }

    public Map<String, String> getParam() {
        return new HashMap<>();
    }

    public int getMethod() {
        return 0;
    }

    public String getTag() {
        return SEARCH_CONCERT_DETAIL_SERVICE;
    }
}
