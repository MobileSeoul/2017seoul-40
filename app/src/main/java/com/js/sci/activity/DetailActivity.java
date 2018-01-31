package com.js.sci.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.js.sci.R;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.network.SCIImageLoader;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCINotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jskim on 2017. 9. 12..
 */

public class DetailActivity extends BaseActivity {

    private NetworkImageView ivImage;

    private TextView tvTitle;
    private TextView tvDesc;

    private LinearLayout llPeriod;
    private TextView tvPeriod;

    private LinearLayout llTime;
    private TextView tvTime;

    private LinearLayout llPlace;
    private TextView tvPlace;

    private LinearLayout llTarget;
    private TextView tvTarget;

    private LinearLayout llFee;
    private TextView tvFee;

    private LinearLayout llSponser;
    private TextView tvSponser;

    private FloatingActionButton fabAdd;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabInquiry;
    private FloatingActionButton fabHomepage;

    private Animation enterFab;
    private Animation enterFabMenu;
    private Animation exitFab;
    private Animation exitFabMenu;

    private boolean isMenuOpen = false;
    private boolean isInquiry = true;

    private String callNum = "";

    private SCIData data;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        setActionbar(R.string.detail, true);

        ivImage = (NetworkImageView) findViewById(R.id.iv_image);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);

        llPeriod = (LinearLayout) findViewById(R.id.ll_period);
        tvPeriod = (TextView) findViewById(R.id.tv_period);
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        tvTime = (TextView) findViewById(R.id.tv_time);
        llPlace = (LinearLayout) findViewById(R.id.ll_place);
        tvPlace = (TextView) findViewById(R.id.tv_place);
        llTarget = (LinearLayout) findViewById(R.id.ll_target);
        tvTarget = (TextView) findViewById(R.id.tv_target);
        llFee = (LinearLayout) findViewById(R.id.ll_fee);
        tvFee = (TextView) findViewById(R.id.tv_fee);
        llSponser = (LinearLayout) findViewById(R.id.ll_sponsor);
        tvSponser = (TextView) findViewById(R.id.tv_sponsor);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabInquiry = (FloatingActionButton) findViewById(R.id.fab_inquiry);
        fabHomepage = (FloatingActionButton) findViewById(R.id.fab_homepage);

        enterFab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_fab);
        enterFabMenu = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_fab_menu);
        exitFab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_fab);
        exitFabMenu = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_fab_menu);

    }

    @Override
    protected void initEvent() {
        String cultcode = getIntent().getStringExtra(EXTRA_CULTCODE);
        SCILog.debug("cultcode : " + cultcode);
        data = SCIDatabaseManager.getInstance().getData(cultcode);
        SCILog.debug("data : " + data.toString());

        if (data == null) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_msg_03)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    }).show();
            return;
        }

        tvTitle.setText(data.getTITLE());
        String desc = data.getPROGRAM();
        if (TextUtils.isEmpty(desc)) {
            desc = data.getCONTENTS();
        }
        tvDesc.setText(Html.fromHtml(desc));

        tvPeriod.setText(String.format("%s ~ %s", data.getSTRTDATE(), data.getEND_DATE()));
        tvTime.setText(data.getTIME());
        tvPlace.setText(data.getPLACE());

        if (TextUtils.isEmpty(data.getUSE_TRGT())) {
            llTarget.setVisibility(View.GONE);
        } else {
            llTarget.setVisibility(View.VISIBLE);
            tvTarget.setText(data.getUSE_TRGT());
        }

        if (TextUtils.isEmpty(data.getUSE_FEE())) {
            llFee.setVisibility(View.GONE);
        } else {
            llFee.setVisibility(View.VISIBLE);
            tvFee.setText(data.getUSE_FEE());
        }

        if (TextUtils.isEmpty(data.getSPONSOR())) {
            llSponser.setVisibility(View.GONE);
        } else {
            llSponser.setVisibility(View.VISIBLE);
            tvSponser.setText(data.getSPONSOR());
        }

        ivImage.setImageUrl(data.getMAIN_IMG(), SCIImageLoader.getInstance().getImageLoader());

        callNum = getCallNum(data.getINQUIRY());
        isInquiry = !TextUtils.isEmpty(callNum);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingActionButton();
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goShare();
            }
        });

        fabInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCall();
            }
        });


        fabHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHomepage();
            }
        });

    }

    @Override
    protected void clear() {
    }

    private void setFloatingActionButton() {
        if (isMenuOpen) {
            fabShare.setVisibility(View.GONE);
            fabShare.startAnimation(exitFabMenu);
            fabHomepage.setVisibility(View.GONE);
            fabHomepage.startAnimation(exitFabMenu);
            if (isInquiry) {
                fabInquiry.setVisibility(View.GONE);
                fabInquiry.startAnimation(exitFabMenu);
            }
            fabAdd.startAnimation(exitFab);
            isMenuOpen = false;

        } else {
            fabShare.setVisibility(View.VISIBLE);
            fabShare.startAnimation(enterFabMenu);
            fabHomepage.setVisibility(View.VISIBLE);
            fabHomepage.startAnimation(enterFabMenu);
            if (isInquiry) {
                fabInquiry.setVisibility(View.VISIBLE);
                fabInquiry.startAnimation(enterFabMenu);
            }
            fabAdd.startAnimation(enterFab);
            isMenuOpen = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        boolean bookmark = IS_EXIST.equals(data.getBOOKMARK());
        menu.findItem(R.id.action_bookmark).setIcon(bookmark ? R.drawable.ic_bookmark_white : R.drawable.ic_bookmark_border_white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_bookmark:
                boolean bookmark = IS_EXIST.equals(data.getBOOKMARK());
                data.setBOOKMARK(bookmark ? "0" : "1");
                SCIDatabaseManager.getInstance().setBookmark(data);
                invalidateOptionsMenu();
                setNotification(bookmark);
                showToast(bookmark ? R.string.bookmark_unregister : R.string.bookmark_register);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void setNotification(boolean bookmark) {
        if(!bookmark) {
            long time = 0;
            // 1. 시작하는 당일 보다 이전이면 당일 9시에 알림
            // 2-1. 진행 중이면서 마감 전이면 다음날 9시 알림
            // 2-2  진행 중이면서 마감 당일이면 바로 알림
            try {
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(getDate(data.getSTRTDATE()).getTime());
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(getDate(data.getEND_DATE()).getTime());
                end.add(Calendar.DAY_OF_MONTH, 1);
                Calendar c = Calendar.getInstance();
                if (c.compareTo(start) < 0) {
                    start.add(Calendar.HOUR_OF_DAY, 9);
                    time = start.getTimeInMillis();
                    SCINotification.setAlarm(this, time, Integer.parseInt(data.getCULTCODE()), data.getTITLE());
                } else {
                    Calendar nextDay = Calendar.getInstance();
                    nextDay.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    nextDay.add(Calendar.DAY_OF_MONTH, 1);
                    if (nextDay.compareTo(end) < 0) {
                        nextDay.add(Calendar.HOUR_OF_DAY, 9);
                        time = nextDay.getTimeInMillis();
                        SCINotification.setAlarm(this, time, Integer.parseInt(data.getCULTCODE()), data.getTITLE());
                    } else {
                        SCINotification.notification(this, Integer.parseInt(data.getCULTCODE()), data.getTITLE(), data.getPROGRAM());
                    }
                }
            } catch (Exception e) {

            }
        } else {
            SCINotification.resetAlarm(this, Integer.parseInt(data.getCULTCODE()), data.getTITLE());
        }
    }

    private String getCallNum(String inquiry) {
        String callNum = null;
        boolean found = false;

        Pattern pattern = Pattern.compile(CALL_REG_EX[0]);
        Matcher matcher = pattern.matcher(inquiry);

        while (!found && matcher.find()) {
            callNum = matcher.group();
            found = true;
        }

        if (!found) {
            pattern = Pattern.compile(CALL_REG_EX[1]);
            matcher = pattern.matcher(inquiry);

            while (matcher.find()) {
                callNum = "02-" + matcher.group();
            }
        }
        return callNum;
    }

    private void goShare() {
        String text = "[" + getString(R.string.app_name) + "]" + data.getTITLE() + " - " + data.getORG_LINK();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooser = Intent.createChooser(intent, getString(R.string.share));
        startActivity(chooser);
    }


    private void goCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + callNum));
        startActivity(intent);

    }

    private void goHomepage() {
        String url = data.getORG_LINK();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private Date getDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

}
