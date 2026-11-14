package com.fongmi.android.tv.read;

import java.util.List;

/**
 * oktvpro 一期桩：阅读子系统不移植。isHkNovelRule 恒 false，
 * 海阔小说/漫画规则不分流到阅读器（入口置不可用），视频/图文规则不受影响。
 */
public class ReadingGate {

    public static boolean isHkNovelRule(String ruleTitle) {
        return false;
    }

    /** 一期不分流漫画阅读：恒 null，调用方走 pics 兜底会话分支（播放层不消费会话即无-op）。 */
    public static ReadingSession fromHkComic(String ruleTitle, String bookTitle, String chapterName,
                                             String chapterUrl, com.fongmi.android.tv.api.hk.HkPlay play,
                                             java.util.Map<String, String> headers) {
        return null;
    }

    public static ReadingSession fromHkNovel(String ruleTitle, String bookTitle,
                                             List<ReadingSession.Chapter> chapters, int chapterIndex) {
        ReadingSession s = new ReadingSession();
        s.kind = ReadingSession.Kind.NOVEL;
        s.sourceName = ruleTitle == null ? "" : ruleTitle;
        s.ruleTitle = s.sourceName;
        s.bookTitle = bookTitle == null ? "" : bookTitle;
        if (chapters != null) s.chapters = chapters;
        s.chapterIndex = Math.max(0, chapterIndex);
        s.chapterUrl = s.currentChapter().url;
        s.referer = "";
        return s;
    }
}
