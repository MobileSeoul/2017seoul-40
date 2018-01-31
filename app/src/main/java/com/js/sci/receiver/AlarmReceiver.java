package com.js.sci.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.js.sci.constant.SCIConstants;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCINotification;

/**
 * Created by Jisu on 2016. 6. 2..
 */
public class AlarmReceiver extends BroadcastReceiver implements SCIConstants{
    @Override
    public void onReceive(Context context, Intent intent) {
        SCILog.debug("onReceive");
        int id = intent.getIntExtra(EXTRA_CULTCODE, -1);
        String text = intent.getStringExtra(EXTRA_TITLE);
        if (id > -1) {
            SCIData data = SCIDatabaseManager.getInstance().getData(String.valueOf(id));
            SCINotification.notification(context, id, data.getTITLE(), data.getPROGRAM());
        }
    }
}
