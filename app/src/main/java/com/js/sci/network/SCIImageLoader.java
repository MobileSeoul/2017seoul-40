package com.js.sci.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.LinkedList;

/**
 * Created by a80099709 on 2017. 9. 11..
 */

public class SCIImageLoader {

    private static final Object lock = new Object();
    private static SCIImageLoader instance;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private LinkedList<OnResponseListener> onResponseListener = new LinkedList<>();

    public interface OnResponseListener {
        void onResponseSuccess(Bitmap bitmap);
        void onResponseError(VolleyError error);
    }

    private SCIImageLoader() {
    }

    public static SCIImageLoader getInstance() {
        synchronized (lock) {
            if (null == instance) {
                instance = new SCIImageLoader();
            }
            return instance;
        }
    }

    public void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache());
        /*
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache(30);
            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        });
        */
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

    public ImageLoader getImageLoader() {
        if (imageLoader != null) {
            return imageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
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

    public Response.Listener<Bitmap> createSuccessListener() {
        return new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (onResponseListener != null) {
                    for (OnResponseListener listener : onResponseListener) {
                        listener.onResponseSuccess(bitmap);
                    }
                }
            }
        };
    }

    public Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (onResponseListener != null) {
                    for (OnResponseListener listener : onResponseListener) {
                        listener.onResponseError(error);
                    }
                }
            }
        };
    }


    public void executeRequest(String url) {
        getRequestQueue().add(getRequest(url));
    }

    private Request getRequest(String url) {
        return new ImageRequest(url, createSuccessListener(), 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, createErrorListener());
    }

    private static int getDefaultLruCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{

        public BitmapLruCache(){
            super( getDefaultLruCacheSize() );
        }

        public BitmapLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get( url );
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap ) {
            put( url, bitmap );
        }
    }
}
