package com.js.sci.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.js.sci.R;

/**
 * Created by a80099709 on 2017. 9. 14..
 */

public class SCIProgressDialog extends Dialog {
    public SCIProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.view_progress);
        setCancelable(false);
    }
}
