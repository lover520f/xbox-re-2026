package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 兼容桩（S5）：DesignUtil 仅以 instanceof 判定内容是否处于 CoordinatorLayout。
 * 本应用不存在 design 版 CoordinatorLayout（宿主用 androidx.coordinatorlayout），
 * instanceof 恒为 false 即正确语义；本类只为类型解析存在。
 */
public class CoordinatorLayout extends ViewGroup {

    public CoordinatorLayout(Context context) {
        super(context);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
