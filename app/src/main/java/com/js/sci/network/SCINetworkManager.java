package com.js.sci.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.js.sci.constant.SCIConstants;
import com.js.sci.util.SCILog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by jskim on 2016. 9. 16..
 */
public class SCINetworkManager {

    private static final int SERVER_TIMEOUT_MS = 30 * 1000;
    private static final int SERVER_MAX_RETRIES = 3;

    private static final Object lock = new Object();
    private static SCINetworkManager instance;

    private RequestQueue requestQueue;
    private LinkedList<OnResponseListener> onResponseListener = new LinkedList<>();

    public interface OnResponseListener {
        void onResponseSuccess(String tag, Object response);

        void onResponseError(String tag, VolleyError error);
    }

    /**
     * Get Singleton instance
     *
     * @return
     */
    public static SCINetworkManager getInstance() {
        synchronized (lock) {
            if (null == instance) {
                instance = new SCINetworkManager();
            }
            return instance;
        }
    }

    private SCINetworkManager() {
    }

    public void init(Context context) {
        requestQueue = Volley.newRequestQueue(context); // volley default queue
    }

    public void clear() {
        cancelRequestAll();
        onResponseListener.clear();
        instance = null;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue != null) {
            return requestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * 모든 request 취소
     */
    private void cancelRequestAll() {
        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {

            @Override
            public boolean apply(Request<?> arg0) {
                return true;
            }
        });
    }

    public void addResponseListener(OnResponseListener responseLstener) {
        onResponseListener.add(responseLstener);
    }

    public void removeResponseListener(OnResponseListener responseLstener) {
        onResponseListener.remove(responseLstener);
    }

    public Response.Listener<JSONObject> createSuccessListener(final String tag) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseListener != null) {
                    for (OnResponseListener listener : onResponseListener) {
                        listener.onResponseSuccess(tag, response);
                    }
                }
            }
        };
    }

    public Response.ErrorListener createErrorListener(final String tag) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (onResponseListener != null) {
                    for (OnResponseListener listener : onResponseListener) {
                        listener.onResponseError(tag, error);
                    }
                }
            }
        };
    }

    public static DefaultRetryPolicy getDefaultPolicy() {
        return new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static DefaultRetryPolicy getAppServerPolicy() {
        return new DefaultRetryPolicy(
                SERVER_TIMEOUT_MS,
                SERVER_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


    /**
     * Http Request 를 생성하고 network queue를 실행한다.
     */
    public void executeRequest(SCIRequest request) {
        getRequestQueue().add(getRequest(request, createSuccessListener(request.getTag()), createErrorListener(request.getTag())));
    }

    /**
     * 요청된 큐를 취소
     *
     * @param tag
     */
    public void cancel(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    /**
     * 커스텀 이미지뷰 사용
     **/
    private Request getRequest(SCIRequest request, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        SCILog.info("[HTTP Request] \nURL : " + request.getUrl()
                + "\nMethod : " + request.getMethod()
                + "\nHeader : " + request.getHeader()
                + "\nBody : " + request.getParam());

        return new CustomRequest(request, responseListener, errorListener);
    }

    class CustomRequest extends Request<JSONObject> {
        private Response.Listener<JSONObject> listener;
        private SCIRequest request;

        public CustomRequest(SCIRequest request, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(request.getMethod(), request.getUrl(), errorListener);
            this.request = request;
            this.listener = listener;
            setRequestPolicy(getAppServerPolicy());
            setRequestTag(request.getTag());
        }

        @Override
        protected void deliverResponse(JSONObject response) {
            if (listener != null) {
                listener.onResponse(response);
            }
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            String data = "";
            try {
                data = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            } catch (Exception e) {
                SCILog.error(e.getMessage());
            }
            SCILog.info("[HTTP Response] \nStatusCode : " + response.statusCode
                            + "\nHeaders : " + response.headers
                            + "\nNotModified : " + response.notModified
                            + "\nNetworkTimeMs : " + response.networkTimeMs
//                    + "\ndata : " + data
            );
            try {
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return request.getHeader();
        }

        @Override
        public Map<String, String> getParams() throws AuthFailureError {
            return request.getParam();
        }

        private void setRequestPolicy(DefaultRetryPolicy policy) {
            if (policy != null) {
                setRetryPolicy(policy);
            }
        }

        private void setRequestTag(String tag) {
            if (!TextUtils.isEmpty(tag)) {
                setTag(tag);
            }
        }
    }

    private static final int START_OFFSET = 1;

    public void requestList() {
        requestList(START_OFFSET);
    }

    public void requestList(int offset) {
        executeRequest(new SCIRequest(offset, offset + SCIConstants.COUNT - 1, null));
    }
}
