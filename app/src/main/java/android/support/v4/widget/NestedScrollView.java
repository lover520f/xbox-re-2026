package android.support.v4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 兼容桩（S5）：SmartUtil 仅以 instanceof 判定后调 fling(int)。本应用不存在
 * support 版 NestedScrollView 实例（宿主用 androidx/原生滚动视图），分支恒不
 * 成立；本类只为类型解析存在，fling 无实例可作用，保持空实现即正确语义。
 */
public class NestedScrollView extends FrameLayout {

    public NestedScrollView(Context context) {
        super(context);
    }

    public NestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void fling(int velocityY) {
        // 无实例，永不执行
    }
}
