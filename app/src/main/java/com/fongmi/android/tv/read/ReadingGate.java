package com.fongmi.android.tv.read;

import android.text.TextUtils;

import com.fongmi.android.tv.api.hk.HkItem;
import com.fongmi.android.tv.api.hk.HkRule;

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

    /** 海阔规则阅读面分类（星辰 1.4.137 同名 API）：一期不分流，恒 NONE。 */
    public enum HkReadKind {NONE, NOVEL, AUDIO}

    public static HkReadKind classifyHkRule(HkRule rule) {
        return HkReadKind.NONE;
    }

    public static HkReadKind classifyHkTitle(String ruleTitle) {
        return HkReadKind.NONE;
    }

    /**
     * 容器劫持页的有效规则（按星辰 1.4.137 原样）：聚合容器把被点子规则全文
     * 以 base64 放在条目 extra.RULE 里、随开页透传进页规则的 params；在此
     * 解码取子规则本体。无 RULE/解码失败回 null，调用方回落页规则自身。
     */
    public static HkRule effectiveRule(HkRule pageRule) {
        String json = effectiveRuleJson(pageRule);
        if (json == null) return null;
        try {
            return new com.google.gson.Gson().fromJson(json, HkRule.class);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 有效规则全文（解码后的子规则 JSON；无则 null）。 */
    public static String effectiveRuleJson(HkRule pageRule) {
        if (pageRule == null) return null;
        String params = pageRule.getParams();
        if (TextUtils.isEmpty(params)) return null;
        try {
            com.google.gson.JsonElement root = com.google.gson.JsonParser.parseString(params);
            if (root == null || !root.isJsonObject()) return null;
            com.google.gson.JsonElement r = root.getAsJsonObject().get("RULE");
            if (r == null || !r.isJsonPrimitive()) return null;
            String b64 = r.getAsString();
            if (b64 == null || b64.isEmpty()) return null;
            String json = new String(android.util.Base64.decode(b64, android.util.Base64.NO_WRAP),
                    java.nio.charset.StandardCharsets.UTF_8);
            return json.startsWith("{") ? json : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 聚阅短剧分流：一期不跟（属阅读面），恒 null，调用方走原详情链。 */
    public static ReadingSession fromHkJuChapterClick(List<HkItem> pageItems, int clickedIndex,
                                                      String hostRuleTitle, String pageUrl) {
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
