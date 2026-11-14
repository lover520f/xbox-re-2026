package com.fongmi.android.tv.utils.download;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.utils.Download;

import java.io.File;
import java.util.Map;

/**
 * oktvpro 适配：海阔下载入口改接 oktv 自带 Download（不移植星辰下载子系统）。
 * 文件落到应用私有目录 hiker_downloads/，带 header 的一期先忽略 header
 * （oktv Download 无 header 面；海阔下载为附带功能，不影响播放链）。
 */
public class DownloadManager {

    private static final DownloadManager INSTANCE = new DownloadManager();

    public static DownloadManager get() {
        return INSTANCE;
    }

    public void add(String url, String name) {
        add(url, name, null);
    }

    public void add(String url, String name, Map<String, String> headers) {
        try {
            if (url == null || url.isEmpty()) return;
            File dir = new File(App.get().getExternalFilesDir(null), "hiker_downloads");
            if (!dir.exists()) dir.mkdirs();
            String safe = (name == null || name.isEmpty() ? "hiker_file" : name).replaceAll("[\\\\/:*?\"<>|]", "_");
            Download.create(url, new File(dir, safe)).start(new Download.Callback() {
                @Override
                public void progress(int progress) {
                }

                @Override
                public void error(String msg) {
                }

                @Override
                public void success(File file) {
                }
            });
        } catch (Throwable ignored) {
        }
    }
}
