package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 兼容桩（S5）：DesignUtil 以 instanceof 判定后调 addOnOffsetChangedListener，
 * 其 DesignUtil$1 实现本类的 OnOffsetChangedListener。本应用不存在 design 版
 * AppBarLayout 实例，监听永远不会被触发；监听列表的登记/移除按真实语义维护，
 * 仅滚动偏移事件无来源。
 */
public class AppBarLayout extends LinearLayout {

    private final List<OnOffsetChangedListener> listeners = new ArrayList<>();

    public AppBarLayout(Context context) {
        super(context);
    }

    public AppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener listener) {
        if (listener != null && !listeners.contains(listener)) listeners.add(listener);
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener listener) {
        if (listener != null) listeners.remove(listener);
    }

    public int getTotalScrollRange() {
        return 0;
    }

    public interface OnOffsetChangedListener {
        void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset);
    }
}
