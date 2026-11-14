package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 兼容桩（S5）：SmartUtil/RefreshContentWrapper 仅以 instanceof 判定内容类型。
 * 本应用不存在 support 版 ViewPager 实例（宿主用 androidx 版），instanceof 恒为
 * false 即正确语义；本类只为类型解析存在，不承载行为。
 */
public class ViewPager extends ViewGroup {

    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
