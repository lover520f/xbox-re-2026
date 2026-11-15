package com.fongmi.android.tv.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * oktvpro 一期桩：阅读子系统不移植（见 transplant-set §6 决策 1）。
 * 本类只保留海阔核心集引用的数据面（会话载体），无阅读器行为；
 * ReadingGate.isHkNovelRule 恒 false，海阔阅读分支永不触发。
 */
public class ReadingSession {

    public enum Kind {COMIC, NOVEL}

    public static class Chapter {
        public String name = "";
        public String url = "";
        public String pic = "";

        public Chapter() {
        }

        public Chapter(String name, String url) {
            this.name = name == null ? "" : name;
            this.url = url == null ? "" : url;
        }
    }

    public Kind kind = Kind.COMIC;
    public String sourceName = "";
    public String bookTitle = "";
    public String coverUrl = "";
    public int chapterIndex = 0;
    public List<Chapter> chapters = new ArrayList<>();
    public List<String> images = new ArrayList<>();
    public String text = "";
    public String ruleTitle = "";
    public String hkRuleJson = "";
    public String hkHistoryKey = "";
    public String chapterUrl = "";
    public String referer = "";
    public String pageUrl = "";
    public Map<String, String> playHeaders = new HashMap<>();
    public String siteKey = "";
    public String detailId = "";
    public String playFlag = "";
    public String legadoKey = "";
    public String localId = "";
    public int startPage = 0;
    public int startFraction = -1;
    public boolean shelfSuperseded = false;

    private static final Map<String, ReadingSession> CACHE = new HashMap<>();

    public static synchronized String put(ReadingSession s) {
        String id = UUID.randomUUID().toString();
        if (s != null) CACHE.put(id, s);
        return id;
    }

    public static synchronized ReadingSession get(String id) {
        return id == null ? null : CACHE.get(id);
    }

    public static synchronized ReadingSession peek(String id) {
        return get(id);
    }

    public static synchronized void remove(String id) {
        if (id != null) CACHE.remove(id);
    }

    public Chapter currentChapter() {
        if (chapters == null || chapters.isEmpty()) return new Chapter();
        int i = Math.max(0, Math.min(chapterIndex, chapters.size() - 1));
        Chapter c = chapters.get(i);
        return c == null ? new Chapter() : c;
    }
}
