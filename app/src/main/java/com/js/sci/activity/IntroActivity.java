package com.js.sci.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.js.sci.R;
import com.js.sci.constant.DISTRICT;
import com.js.sci.constant.GENRE;
import com.js.sci.constant.PERIOD;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.network.SCINetworkManager;
import com.js.sci.network.SCIResponse;
import com.js.sci.ui.SCIProgressDialog;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCIPreferences;
import com.js.sci.util.SCIUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

/**
 * Created by a80099709 on 2017. 9. 14..
 */

public class IntroActivity extends BaseActivity implements View.OnClickListener, SCINetworkManager.OnResponseListener{

    private Dialog progressDialog;

    private TextView tvGenre;
    private TextView tvDistrict;
    private TextView tvPeriod;

    private int checkGenre = 0;
    private int checkDistrict = 0;
    private int checkPeriod = 0;

    final GENRE[] genres = GENRE.values();
    final DISTRICT[] districts = DISTRICT.values();
    final PERIOD[] periods = PERIOD.values();

    private List<SCIData> bookMarkList;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_intro;
    }

    @Override
    protected void initView() {
        progressDialog = new SCIProgressDialog(this);

        if (SCIPreferences.getInstance().getFirst()) {
            findViewById(R.id.ll_first).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_start).setVisibility(View.GONE);
            tvGenre = (TextView) findViewById(R.id.tv_genre);
            tvDistrict = (TextView) findViewById(R.id.tv_district);
            tvPeriod = (TextView) findViewById(R.id.tv_period);
        } else {
            findViewById(R.id.ll_first).setVisibility(View.GONE);
            findViewById(R.id.ll_start).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {
        if (!SCIUtils.isConnected(getApplicationContext())) {
            showErrorDialog(getString(R.string.error_msg_01));
            return;
        }

        SCINetworkManager.getInstance().addResponseListener(this);

        if (SCIPreferences.getInstance().getFirst()) {
            GENRE genre = GENRE.valueOf(SCIPreferences.getInstance().getGenre());
            tvGenre.setText(genre.getResId());
            for (int i = 0; i < genres.length; i++) {
                if (genres[i] == genre) {
                    checkGenre = i;
                    break;
                }
            }

            DISTRICT district = DISTRICT.valueOf(SCIPreferences.getInstance().getDistrict());
            tvDistrict.setText(district.getResId());
            for (int i = 0; i < districts.length; i++) {
                if (districts[i] == district) {
                    checkDistrict = i;
                    break;
                }
            }

            PERIOD period = PERIOD.valueOf(SCIPreferences.getInstance().getPeriod());
            tvPeriod.setText(period.getResId());
            for (int i = 0; i < periods.length; i++) {
                if (periods[i] == period) {
                    checkPeriod = i;
                    break;
                }
            }

            findViewById(R.id.v_genre).setOnClickListener(this);
            findViewById(R.id.v_district).setOnClickListener(this);
            findViewById(R.id.v_period).setOnClickListener(this);
            findViewById(R.id.v_start).setOnClickListener(this);
        } else {
            progressDialog.show();
            long lastUpdate = SCIPreferences.getInstance().getLastUpdate();
            Calendar cLastUpdate = Calendar.getInstance();
            cLastUpdate.setTimeInMillis(lastUpdate);
            cLastUpdate.add(Calendar.DAY_OF_MONTH, PERIOD.valueOf(SCIPreferences.getInstance().getPeriod()).getDay());

            Calendar today = Calendar.getInstance();
            if (today.compareTo(cLastUpdate) < 1) {
                SCILog.debug("update skip");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        goMain();
                    }
                }, 1000);
            } else {
                SCILog.debug("update start");
                bookMarkList = SCIDatabaseManager.getInstance().getBookmarkList();
                SCIDatabaseManager.getInstance().deleteAll();
                SCINetworkManager.getInstance().requestList();
            }
        }
    }

    @Override
    protected void clear() {
        SCINetworkManager.getInstance().removeResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_genre:
                showGenreDialog();
                break;
            case R.id.v_district:
                showDistrictDialog();
                break;
            case R.id.v_period:
                showPeoridDialog();
                break;
            case R.id.v_start:
                progressDialog.show();
                SCINetworkManager.getInstance().requestList();
                break;
        }
    }

    private int offset = 1;

    @Override
    public void onResponseSuccess(String tag, Object response) {
        SCILog.debug("onResponseSuccess : " + tag + " " + response);

        final SCIResponse r = SCIResponse.getResponseData(response);

        long result = SCIDatabaseManager.getInstance().insertList(r.getRow());
        if (result == -1) {
            SCILog.debug("insert fail");
            SCIDatabaseManager.getInstance().deleteAll();
            showErrorDialog(getString(R.string.error_msg_02));
            return;
        }

        offset += COUNT;
        int totalCount = Integer.parseInt(r.getList_total_count());
        if (offset < totalCount) {
            SCINetworkManager.getInstance().requestList(offset);
        } else {
            SCILog.debug("insert success");
            SCIPreferences.getInstance().setLastUpdate(Calendar.getInstance().getTimeInMillis());

            if (SCIPreferences.getInstance().getFirst()) {
                SCIPreferences.getInstance().setFirst(false);
                goMain();
            } else {
                updateBookmark();
            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResponseError(String tag, VolleyError error) {
        progressDialog.dismiss();
        SCILog.error("onResponseError : " + tag + " " + error.getMessage());
        error.printStackTrace();

        showErrorDialog(getString(R.string.error_msg_01));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        SCILog.debug("onNewIntent");
        super.onNewIntent(intent);
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        SCILog.debug("id : " + getIntent().getStringExtra(EXTRA_CULTCODE));
        intent.putExtra(EXTRA_CULTCODE , getIntent().getStringExtra(EXTRA_CULTCODE));
        startActivity(intent);
        finish();
    }

    private void updateBookmark() {
        if (!bookMarkList.isEmpty()) {
            for (SCIData bookMark : bookMarkList) {
                SCIData data = SCIDatabaseManager.getInstance().getData(bookMark.getCULTCODE());
                if (data != null) {
                    data.setBOOKMARK("1");
                    SCIDatabaseManager.getInstance().setBookmark(data);
                }
            }
        }
        goMain();
    }

    private void showGenreDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.genre, checkGenre, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkGenre = which;
                        SCIPreferences.getInstance().setGenre(genres[which].name());
                        setText(R.id.tv_genre, genres[which].getResId());
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showDistrictDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.district, checkDistrict, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkDistrict = which;
                        SCIPreferences.getInstance().setDistrict(districts[which].name());
                        setText(R.id.tv_district, districts[which].getResId());
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showPeoridDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.period, checkPeriod, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPeriod = which;
                        SCIPreferences.getInstance().setPeriod(periods[which].name());
                        setText(R.id.tv_period, periods[which].getResId());
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showErrorDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    private void setText(int id, int resId) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(resId);
    }
}
