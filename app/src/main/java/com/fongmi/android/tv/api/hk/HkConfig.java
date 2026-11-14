package com.fongmi.android.tv.api.hk;

import android.content.SharedPreferences;

import com.fongmi.android.tv.App;

/**
 * 海阔模块自有配置（照 LabConfig 模式：独立 SharedPreferences，不进全局 Setting）。
 * 一期只有底部导航入口开关；默认关，关时仅从增强设置进入。
 */
public class HkConfig {

    private static final String PREF = "hiker";
    private static final String KEY_NAV_ENTRY = "hk_nav_entry";

    private static class Loader {
        static final HkConfig INSTANCE = new HkConfig();
    }

    public static HkConfig get() {
        return Loader.INSTANCE;
    }

    private SharedPreferences sp() {
        return App.get().getSharedPreferences(PREF, android.content.Context.MODE_PRIVATE);
    }

    public boolean getNavEntry() {
        return sp().getBoolean(KEY_NAV_ENTRY, false);
    }

    public void setNavEntry(boolean value) {
        sp().edit().putBoolean(KEY_NAV_ENTRY, value).apply();
    }
}
