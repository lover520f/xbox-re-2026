package com.fongmi.android.tv.read;

import android.app.Activity;
import android.widget.Toast;

/**
 * oktvpro 一期桩：阅读器页面不移植，openReading 系列为空转并提示不可用。
 */
public class ReadingRouter {

    public static void openReading(Activity activity, ReadingSession session) {
        notifyUnavailable(activity);
    }

    public static void openReadingWithStartPage(Activity activity, ReadingSession session, int startPage) {
        notifyUnavailable(activity);
    }

    private static void notifyUnavailable(Activity activity) {
        try {
            if (activity != null) Toast.makeText(activity, "阅读功能暂不可用", Toast.LENGTH_SHORT).show();
        } catch (Throwable ignored) {
        }
    }
}
