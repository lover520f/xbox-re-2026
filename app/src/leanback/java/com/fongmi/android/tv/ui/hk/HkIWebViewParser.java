package com.fongmi.android.tv.ui.hk;

/**
 * leanback 一期桩：WebView 嗅探解析（x5Rule/webRule）依赖 mobile 网页页与全桥，
 * TV 端不提供；parseSync 恒回空串，HkRouter 按解析失败走既有回退。
 * mobile 源集有同名真实现，两 flavor 互不干扰。
 */
public abstract class HkIWebViewParser {

    public static String parseSync(String url, String extraJson) {
        return "";
    }
}
