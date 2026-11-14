package com.fongmi.android.tv.legado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * oktvpro 一期桩：Legado 有声子系统不移植。本类只保留海阔核心集
 * （HkRadioRouter）引用的数据面；听书页路由已在 HkRadioRouter 置不可用，
 * put/get 仅作进程内暂存，不产生任何播放行为。
 */
public class LegadoAudio {

    public static class Chapter {
        public String name = "";
        public String url = "";

        public Chapter(String name, String url) {
            this.name = name == null ? "" : name;
            this.url = url == null ? "" : url;
        }
    }

    public static class AudioAlbum {
        public String legadoKey = "";
        public String hkRule = "";
        public String hkLine = "";
        public String sourceName = "";
        public String title = "";
        public String coverUrl = "";
        public final List<Chapter> chapters = new ArrayList<>();
        public int index = 0;

        public Chapter current() {
            if (chapters.isEmpty()) return null;
            int i = Math.max(0, Math.min(index, chapters.size() - 1));
            return chapters.get(i);
        }
    }

    private static final Map<String, AudioAlbum> CACHE = new HashMap<>();

    public static synchronized String put(AudioAlbum a) {
        String id = UUID.randomUUID().toString();
        if (a != null) CACHE.put(id, a);
        return id;
    }

    public static synchronized AudioAlbum get(String id) {
        return id == null ? null : CACHE.get(id);
    }

    public static synchronized void remove(String id) {
        if (id != null) CACHE.remove(id);
    }
}
