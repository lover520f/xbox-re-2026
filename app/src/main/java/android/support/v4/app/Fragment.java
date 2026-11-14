package android.support.v4.app;

/**
 * 兼容桩（S5）：仅作为 FragmentManager.getFragments() 的元素类型被
 * ClassicsHeader 引用（且该引用点在 instanceof 守卫之后，恒不执行）。
 * 本应用不存在 support 版 Fragment 实例，本类只为类型解析存在。
 */
public class Fragment {
}
