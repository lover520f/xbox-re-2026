package android.support.v4.view;

import android.view.View;

/**
 * 兼容桩（海阔移植一期）：io.github.scwang90:refresh-layout-kernel:2.0.5 的
 * SmartRefreshLayout 实现了旧 support 的 NestedScrollingParent 接口，该 AAR
 * 在星辰侧靠 Jetifier 转写为 androidx 接口；宿主 AGP 9 已移除 Jetifier，
 * 且全图无任何构件提供此类，javac 加载 SmartRefreshLayout 层级时报
 * 「class file for android.support.v4.view.NestedScrollingParent not found」。
 * 本桩按 support 原版接口逐法补齐（仅供类型层级完整，运行时无调用方经此
 * 接口分发——宿主自身的 SmartRefreshLayout 布局在无此桩时亦可运行可证）。
 */
public interface NestedScrollingParent {

    boolean onStartNestedScroll(View child, View target, int nestedScrollAxes);

    void onNestedScrollAccepted(View child, View target, int nestedScrollAxes);

    void onStopNestedScroll(View target);

    void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed);

    void onNestedPreScroll(View target, int dx, int dy, int[] consumed);

    boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed);

    boolean onNestedPreFling(View target, float velocityX, float velocityY);

    int getNestedScrollAxes();
}
