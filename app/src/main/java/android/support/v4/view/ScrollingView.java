package android.support.v4.view;

/**
 * 兼容桩（S5）：SmartUtil 以 instanceof 引用。按 androidx.core.view.ScrollingView 1:1 镜像。
 */
public interface ScrollingView {

    int computeHorizontalScrollRange();

    int computeHorizontalScrollOffset();

    int computeHorizontalScrollExtent();

    int computeVerticalScrollRange();

    int computeVerticalScrollOffset();

    int computeVerticalScrollExtent();
}
