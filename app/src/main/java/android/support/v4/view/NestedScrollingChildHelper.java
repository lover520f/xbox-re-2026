package android.support.v4.view;

import android.view.View;

/**
 * 兼容桩（S5）：SmartRefreshLayout 持有本类字段并真实调用其全部派发方法，
 * 故不做空实现——内部委托 androidx.core.view.NestedScrollingChildHelper，
 * 与 support 原版算法等价（同一套嵌套滚动父查找/派发逻辑），签名与 support 版一致。
 */
public class NestedScrollingChildHelper {

    private final androidx.core.view.NestedScrollingChildHelper delegate;

    public NestedScrollingChildHelper(View view) {
        delegate = new androidx.core.view.NestedScrollingChildHelper(view);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        delegate.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return delegate.isNestedScrollingEnabled();
    }

    public boolean hasNestedScrollingParent() {
        return delegate.hasNestedScrollingParent();
    }

    public boolean startNestedScroll(int axes) {
        return delegate.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        delegate.stopNestedScroll();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return delegate.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return delegate.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return delegate.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return delegate.dispatchNestedPreFling(velocityX, velocityY);
    }

    public void onDetachedFromWindow() {
        delegate.onDetachedFromWindow();
    }

    public void onStopNestedScroll(View child) {
        delegate.onStopNestedScroll(child);
    }
}
