package android.support.v7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 兼容桩（S5）：SmartUtil 仅以 instanceof 判定后调 fling(int,int)。本应用
 * 列表全部是 androidx.recyclerview.widget.RecyclerView，不存在 support 版
 * 实例，分支恒不成立；本类只为类型解析存在。
 */
public class RecyclerView extends ViewGroup {

    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public boolean fling(int velocityX, int velocityY) {
        // 无实例，永不执行
        return false;
    }
}
