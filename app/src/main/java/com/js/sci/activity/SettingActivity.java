package com.js.sci.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.js.sci.R;
import com.js.sci.constant.DISTRICT;
import com.js.sci.constant.GENRE;
import com.js.sci.constant.LIST_COUNT;
import com.js.sci.constant.PERIOD;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.network.SCINetworkManager;
import com.js.sci.network.SCIResponse;
import com.js.sci.ui.SCIProgressDialog;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCIPreferences;

import java.util.Calendar;
import java.util.List;

/**
 * Created by a80099709 on 2017. 9. 12..
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener, SCINetworkManager.OnResponseListener {

    final GENRE[] genres = GENRE.values();
    final DISTRICT[] districts = DISTRICT.values();
    final PERIOD[] periods = PERIOD.values();
    final LIST_COUNT[] listCounts = LIST_COUNT.values();

    private Dialog progressDialog;

    private TextView tvGenre;
    private TextView tvDistrict;
    private TextView tvPeriod;
    private TextView tvListCount;

    private int checkGenre = 0;
    private int checkDistrict = 0;
    private int checkPeriod = 0;
    private int checkListCount = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        tvGenre = (TextView) findViewById(R.id.tv_genre);
        tvDistrict = (TextView) findViewById(R.id.tv_district);
        tvPeriod = (TextView) findViewById(R.id.tv_period);
        tvListCount = (TextView) findViewById(R.id.tv_list_count);

        progressDialog = new SCIProgressDialog(this);

        setActionbar(R.string.setting_title, true);
    }

    @Override
    protected void initEvent() {
        SCINetworkManager.getInstance().addResponseListener(this);

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

        LIST_COUNT listCount = LIST_COUNT.valueOf(SCIPreferences.getInstance().getListCount());
        tvListCount.setText(listCount.getResId());
        for (int i = 0; i < listCounts.length; i++) {
            if (listCounts[i] == listCount) {
                checkListCount = i;
                break;
            }
        }

        findViewById(R.id.v_genre).setOnClickListener(this);
        findViewById(R.id.v_district).setOnClickListener(this);
        findViewById(R.id.v_period).setOnClickListener(this);
        findViewById(R.id.v_list_count).setOnClickListener(this);
        findViewById(R.id.v_update).setOnClickListener(this);
        findViewById(R.id.v_reset).setOnClickListener(this);
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
            case R.id.v_list_count:
                showListCountDialog();
                break;
            case R.id.v_update:
                showUpdateDialog();
                break;
            case R.id.v_reset:
                showResetDialog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }


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
            updateBookmark();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResponseError(String tag, VolleyError error) {
        progressDialog.dismiss();
        SCILog.error("onResponseError : " + tag + " " + error.getMessage());
        error.printStackTrace();

        showMsgDialog(getString(R.string.update_fail));
    }

    private int offset = 1;
    private List<SCIData> bookMarkList;

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
    }

    private void showGenreDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.genre, checkGenre, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkGenre = which;
                        SCIPreferences.getInstance().setGenre(genres[which].name());
                        tvGenre.setText(genres[which].getResId());
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
                        tvDistrict.setText(districts[which].getResId());
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
                        tvPeriod.setText(periods[which].getResId());
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showListCountDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.list_count, checkListCount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkListCount = which;
                        SCIPreferences.getInstance().setListCount(listCounts[which].name());
                        tvListCount.setText(listCounts[which].getResId());
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showUpdateDialog() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        progressDialog.show();

                        SCILog.debug("update start");
                        bookMarkList = SCIDatabaseManager.getInstance().getBookmarkList();
                        SCIDatabaseManager.getInstance().deleteAll();
                        SCINetworkManager.getInstance().requestList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setMessage(R.string.update_msg)
                .setPositiveButton(R.string.confirm, onClickListener)
                .setNegativeButton(R.string.cancel, onClickListener)
                .show();
    }

    private void showResetDialog() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SCIDatabaseManager.getInstance().deleteAll();
                        SCIPreferences.getInstance().reset();
                        setResult(RESULT_RESET);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setMessage(R.string.reset_msg)
                .setPositiveButton(R.string.confirm, onClickListener)
                .setNegativeButton(R.string.cancel, onClickListener)
                .show();
    }

    private void showMsgDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                        setResult(RESULT_DATABASE_FAIL);
                        finish();
                    }
                }).show();
    }


}
