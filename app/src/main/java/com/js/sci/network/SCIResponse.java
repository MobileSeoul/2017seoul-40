package com.js.sci.network;

import com.google.gson.Gson;
import com.js.sci.constant.SCIConstants;
import com.js.sci.dto.ResultData;
import com.js.sci.dto.SCIData;
import com.js.sci.util.SCILog;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jskim on 2017. 9. 11..
 */

public class SCIResponse implements SCIConstants {
    private String list_total_count;
    private ResultData RESULT;

    private ArrayList<SCIData> row;

    public String getList_total_count() {
        return list_total_count;
    }

    public ResultData getRESULT() {
        return RESULT;
    }

    public ArrayList<SCIData> getRow() {
        return row;
    }

    public static SCIResponse getResponseData(Object object) {
        SCIResponse response = null;

        if (object instanceof JSONObject == false) {
            SCILog.warning("JSONObject is not!");
            return response;
        }

        JSONObject responseObject = (JSONObject) object;

        if (!responseObject.has(SEARCH_CONCERT_DETAIL_SERVICE)) {
            SCILog.warning("service is not!");
            return response;
        }

        try {
            response = new Gson().fromJson(responseObject.get(SEARCH_CONCERT_DETAIL_SERVICE).toString(), SCIResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
