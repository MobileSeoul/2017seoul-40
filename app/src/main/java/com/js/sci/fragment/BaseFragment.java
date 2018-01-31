package com.js.sci.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.js.sci.activity.ActivityCallback;

/**
 * Created by jskim on 2016. 8. 4..
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected ActivityCallback callback;
    protected View rootView;

    private final int SHORTTIME_CLICK_INTERVAL = 1000;
    private long lastClickTime = 0;

    public BaseFragment() {}

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (Context) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (ActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Context must implement ActivityCallback.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResource(), container, false);
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        clear();
        callback = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected boolean isClickable() {
        if (lastClickTime > 0 && SystemClock.elapsedRealtime() - lastClickTime < SHORTTIME_CLICK_INTERVAL) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    /**
     * fragment 에서 사용되는 layout resource id 반환
     * @return layout resource id
     */
    @LayoutRes
    protected abstract int getLayoutResource();

    /**
     * 뷰를 생성하고 리스너를 등록
     */
    protected abstract void initView();

    /**
     * 초기 이벤트를 실행
     */
    protected abstract void initEvent();

    /**
     * 뷰를 해제하고 데이터를 클리어
     */
    protected abstract void clear();

    protected void onRefresh() {}

    protected boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    protected boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    protected boolean onBackPressed() {
        return false;
    }

    protected void finish() {

    }

}
