package com.fongmi.android.tv.ui.hk;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * leanback 一期桩：与 mobile 同名类的接口面对齐（extends AlertDialog.Builder），
 * TV 端用系统 AlertDialog 呈现（授权确认/错误详情等 main 源集调用点可正常工作），
 * 不带 mobile 的毛玻璃样式。mobile 源集有同名真实现。
 */
public class HkDialogs extends AlertDialog.Builder {

    public HkDialogs(@NonNull Context context) {
        super(context);
    }
}
