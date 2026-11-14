package android.support.v4.view;

import android.view.View;

/**
 * 兼容桩（S5）：refresh-layout-kernel 2.0.5 在 SmartUtil/RefreshContentWrapper 中以
 * instanceof 引用旧 support 的 NestedScrollingChild 接口，宿主 AGP 9 无 Jetifier、
 * 全图无构件提供此类，运行期解析失败即 NoClassDefFoundError。
 * 本接口按 androidx.core.view.NestedScrollingChild（support 原版的直系后继）逐法 1:1 镜像。
 */
public interface NestedScrollingChild {

    void setNestedScrollingEnabled(boolean enabled);

    boolean isNestedScrollingEnabled();

    boolean startNestedScroll(int axes);

    void stopNestedScroll();

    boolean hasNestedScrollingParent();

    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

    boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow);

    boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);

    boolean dispatchNestedPreFling(float velocityX, float velocityY);
}
