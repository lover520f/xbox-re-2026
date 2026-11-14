package android.support.v4.view;

import android.view.View;

/**
 * 兼容桩（S5）：仅补 refresh-layout-kernel 实际调用到的方法，静态委托
 * androidx.core.view.ViewCompat（support 原版的直系后继，算法等价）。
 * 盘点依据：kernel 全部类中对本类的调用只有 isNestedScrollingEnabled 一处。
 */
public class ViewCompat {

    public static boolean isNestedScrollingEnabled(View view) {
        return androidx.core.view.ViewCompat.isNestedScrollingEnabled(view);
    }
}
