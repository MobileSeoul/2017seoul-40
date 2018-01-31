package com.js.sci.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.widget.Toast;

import com.js.sci.R;
import com.js.sci.constant.SCIConstants;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.tsengvn.typekit.TypekitSpan;

/**
 * Created by jskim on 2017. 9. 12..
 */

public abstract class BaseActivity extends AppCompatActivity implements SCIConstants{
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(getLayoutResource());

        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clear();
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    /**
     * Activity에서 사용될 layout resource id 반환
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutResource();

    /**
     * onCreate()에서 실행 : 뷰를 생성하고 리스너를 등록함
     */
    protected abstract void initView();

    /**
     * onCreate()에서 실행 : 초기 이벤트를 실행함
     */
    protected abstract void initEvent();

    /**
     * onDestroy()에서 실행 : 뷰를 해제하고 데이터를 클리어함
     */
    protected abstract void clear();

    protected void showToast(@NonNull String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(@StringRes int msgId) {
        showToast(msgId, Toast.LENGTH_SHORT);
    }

    protected void showToast(@NonNull String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }

    protected void showToast(@StringRes int msgId, int duration) {
        Toast.makeText(this, msgId, duration).show();
    }

    protected void setActionbar(int resId, boolean showHomeAsUp) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (resId > 0) {
                SpannableString s = new SpannableString(getString(resId));
                s.setSpan(new TypekitSpan(Typeface.BOLD, Typekit.getInstance()), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(s);
            }
            if (showHomeAsUp) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white);
                actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            }
        }
    }
}
