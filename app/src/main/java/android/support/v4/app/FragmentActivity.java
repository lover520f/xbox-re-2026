package android.support.v4.app;

import android.app.Activity;

/**
 * 兼容桩（S5）：ClassicsHeader 以 instanceof 判定宿主 Activity 类型。
 * 本应用 Activity 均为 androidx.fragment.app.FragmentActivity，不存在
 * support 版实例，instanceof 恒为 false 即正确语义（星辰侧同代码在
 * Jetifier 转写后判定的是 androidx 类，海阔页面的宿主是 AppCompatActivity
 * 而非 FragmentActivity，判定结果同样为 false，行为一致）。
 */
public class FragmentActivity extends Activity {

    public FragmentManager getSupportFragmentManager() {
        return null;
    }
}
