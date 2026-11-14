package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * 兼容桩（S5）：refresh-layout-kernel/footer 调用 getColor，LitePal 调用
 * checkSelfPermission。全部静态委托 androidx.core.content.ContextCompat
 * （support 原版的直系后继，行为等价）；附带补齐同类常用方法以防同 AAR 其他路径调用。
 */
public class ContextCompat {

    public static int getColor(Context context, int id) {
        return androidx.core.content.ContextCompat.getColor(context, id);
    }

    public static ColorStateList getColorStateList(Context context, int id) {
        return androidx.core.content.ContextCompat.getColorStateList(context, id);
    }

    public static Drawable getDrawable(Context context, int id) {
        return androidx.core.content.ContextCompat.getDrawable(context, id);
    }

    public static int checkSelfPermission(Context context, String permission) {
        return androidx.core.content.ContextCompat.checkSelfPermission(context, permission);
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        return androidx.core.content.ContextCompat.getExternalFilesDirs(context, type);
    }

    public static File[] getExternalCacheDirs(Context context) {
        return androidx.core.content.ContextCompat.getExternalCacheDirs(context);
    }

    public static File[] getObbDirs(Context context) {
        return androidx.core.content.ContextCompat.getObbDirs(context);
    }

    public static File getNoBackupFilesDir(Context context) {
        return androidx.core.content.ContextCompat.getNoBackupFilesDir(context);
    }

    public static File getCodeCacheDir(Context context) {
        return androidx.core.content.ContextCompat.getCodeCacheDir(context);
    }

    public static File getDataDir(Context context) {
        return androidx.core.content.ContextCompat.getDataDir(context);
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        return androidx.core.content.ContextCompat.isDeviceProtectedStorage(context);
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        return androidx.core.content.ContextCompat.createDeviceProtectedStorageContext(context);
    }

    public static void startActivity(Context context, Intent intent, Bundle options) {
        androidx.core.content.ContextCompat.startActivity(context, intent, options);
    }

    public static Executor getMainExecutor(Context context) {
        return androidx.core.content.ContextCompat.getMainExecutor(context);
    }

    public static <T> T getSystemService(Context context, Class<T> serviceClass) {
        return androidx.core.content.ContextCompat.getSystemService(context, serviceClass);
    }
}
