package android.support.v4.app;

import java.util.Collections;
import java.util.List;

/**
 * 兼容桩（S5）：ClassicsHeader 在 instanceof FragmentActivity 守卫后调用
 * getFragments()；本应用 Activity 均为 androidx 版，守卫恒为 false，
 * 本方法永不执行，返回空表仅为签名完整。
 */
public class FragmentManager {

    public List<Fragment> getFragments() {
        return Collections.emptyList();
    }
}
