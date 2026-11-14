package com.fongmi.android.tv.ui.hk;

import android.app.Activity;
import android.content.Intent;

/**
 * leanback 一期桩：海阔列表页是 mobile UI，TV 端无此页（未注册进 leanback
 * Manifest，不会被启动）。仅满足 main 源集 PageParser 的类引用与两个静态口：
 * putTempRule 照小规则形态把数据放进 Intent extra，clearTempParentData 空操作。
 * mobile 源集有同名真实现。
 */
public class HkListActivity extends Activity {

    public static void putTempRule(Intent intent, String ruleJson) {
        if (intent != null) intent.putExtra("data", ruleJson);
    }

    public static void clearTempParentData() {
    }
}
