package com.js.sci;

import android.app.Application;
import android.content.Context;

import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.network.SCIImageLoader;
import com.js.sci.network.SCINetworkManager;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCIPreferences;
import com.tsengvn.typekit.Typekit;

/**
 * Created by jskim on 2017. 9. 11..
 */

public class SCIApplication extends Application {

    private static final String TAG = SCIApplication.class.getSimpleName();

    private static Context context;

    @Override
    public void onCreate() {

        SCILog.debug(TAG, "onCreate");
        super.onCreate();

        this.context = getApplicationContext();

        SCIDatabaseManager.getInstance().init(getApplicationContext());
        SCINetworkManager.getInstance().init(getApplicationContext());
        SCIImageLoader.getInstance().init(getApplicationContext());
        SCIPreferences.getInstance().init(getApplicationContext());

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/SeoulHangangM.ttf" ))
                .addBold(Typekit.createFromAsset(this, "fonts/SeoulHangangB.ttf"));

    }

    @Override
    public void onTerminate() {
        SCILog.debug(TAG, "onTerminate");

        SCIDatabaseManager.getInstance().clear();
        SCINetworkManager.getInstance().clear();
        SCIImageLoader.getInstance().clear();
        SCIPreferences.getInstance().clear();


        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }
}
