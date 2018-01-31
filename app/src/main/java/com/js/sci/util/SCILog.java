package com.js.sci.util;

import android.util.Log;

import com.js.sci.BuildConfig;

/**
 * Created by jskim on 2017. 9. 11..
 */

public class SCILog {
    // ======================================================
    public static final String LOG_TAG = "SCI";
    private static final int STACK_TRACE_INDEX = 4;
    // ======================================================

    public static final int LEVEL_DEBUG = 0;
    public static final int LEVEL_INFO = 1;
    public static final int LEVEL_WARNING = 2;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_VERBOSE = 5;


    protected static boolean isRelease() {
        return !BuildConfig.DEBUG;
    }

    public static void debug(String... args) {
        if (isRelease()) return;
        printLog(LEVEL_DEBUG, args);
    }

    public static void error(String... args) {
        if (isRelease()) return;
        printLog(LEVEL_ERROR, args);
    }

    public static void info(String... args) {
        if (isRelease()) return;
        printLog(LEVEL_INFO, args);
    }

    public static void verbose(String... args) {
        if (isRelease()) return;
        printLog(LEVEL_VERBOSE, args);
    }

    public static void warning(String... args) {
        if (isRelease()) return;
        printLog(LEVEL_WARNING, args);
    }

    private static void printLog(int level, String[] args) {
        String tag = "";
        String message = "";
        switch (args.length) {
            case 1:
                tag = LOG_TAG;
                message = args[0];
                break;
            case 2:
                tag = args[0];
                message = args[1];
                break;
        }

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];

        String fileName = ste.getFileName();
        int lineNum = ste.getLineNumber();
        String strLog = String.format("[%-20s:%5d] %s\n", fileName, lineNum, message);

        switch (level) {
            case LEVEL_DEBUG:
                Log.d(tag, strLog);
                break;
            case LEVEL_INFO:
                Log.i(tag, strLog);
                break;
            case LEVEL_WARNING:
                Log.w(tag, strLog);
                break;
            case LEVEL_ERROR:
                Log.e(tag, strLog);
                break;
            case LEVEL_VERBOSE:
                Log.v(tag, strLog);
                break;
        }
    }
}
