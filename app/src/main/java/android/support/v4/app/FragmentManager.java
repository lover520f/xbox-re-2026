package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

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

    /**
     * 按 support 原版签名补齐的生命周期回调基类（空实现即原版语义）。
     * leanback 包内有库类在常量池引用本类型（dex 闭环扫描检出），故一并补定义。
     */
    public abstract static class FragmentLifecycleCallbacks {

        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        }

        public void onFragmentStarted(FragmentManager fm, Fragment f) {
        }

        public void onFragmentResumed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentPaused(FragmentManager fm, Fragment f) {
        }

        public void onFragmentStopped(FragmentManager fm, Fragment f) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        }

        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentDetached(FragmentManager fm, Fragment f) {
        }
    }
}
