package com.fongmi.android.tv.read;

/**
 * oktvpro 一期桩：阅读进度存储不移植，find 恒返回 null（无历史进度可续）。
 * Record 仅保留海阔核心集读取的字段面。
 */
public class ReadingStore {

    public static class Record {
        public String kind = "";
        public String sourceName = "";
        public String bookTitle = "";
        public String coverUrl = "";
        public String chapterName = "";
        public int chapterIndex = 0;
        public String chapterUrl = "";
        public String siteKey = "";
        public String detailId = "";
        public String playFlag = "";
        public String legadoKey = "";
        public String origin = "";
        public String localId = "";
        public String content = "";
        public int position = 0;
        public int segIndex = 0;
        public String bookUrl = "";
        public String ruleTitle = "";
    }

    public static Record find(String kind, String sourceName, String bookTitle) {
        return null;
    }
}
