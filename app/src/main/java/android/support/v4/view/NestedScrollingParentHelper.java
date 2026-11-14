package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * 兼容桩（S5）：SmartRefreshLayout 持有本类字段并调用 onNestedScrollAccepted/
 * onStopNestedScroll/getNestedScrollAxes。内部委托 androidx.core.view
 * .NestedScrollingParentHelper，与 support 原版算法等价（axes 的 OR 累积与清零）。
 */
public class NestedScrollingParentHelper {

    private final androidx.core.view.NestedScrollingParentHelper delegate;

    public NestedScrollingParentHelper(ViewGroup viewGroup) {
        delegate = new androidx.core.view.NestedScrollingParentHelper(viewGroup);
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        delegate.onNestedScrollAccepted(child, target, axes);
    }

    public int getNestedScrollAxes() {
        return delegate.getNestedScrollAxes();
    }

    public void onStopNestedScroll(View target) {
        delegate.onStopNestedScroll(target);
    }
}
