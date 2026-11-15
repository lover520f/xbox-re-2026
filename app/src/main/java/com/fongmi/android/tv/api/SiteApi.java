package com.fongmi.android.tv.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.api.config.VodConfig;
import com.fongmi.android.tv.api.hk.HkDetailBridge;
import com.fongmi.android.tv.api.hk.HkPlay;
import com.fongmi.android.tv.api.hk.HkRouter;
import com.fongmi.android.tv.api.hk.HkRuleManager;
import com.fongmi.android.tv.read.ReadingGate;
import com.fongmi.android.tv.read.ReadingSession;
import com.fongmi.android.tv.bean.Class;
import com.fongmi.android.tv.bean.Result;
import com.fongmi.android.tv.bean.Site;
import com.fongmi.android.tv.bean.Vod;
import com.fongmi.android.tv.player.Source;
import com.fongmi.android.tv.setting.PlayerSetting;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.Sniffer;
import com.fongmi.android.tv.web.WebHomeInlineVodStore;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Prefers;
import com.github.catvod.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;

public class SiteApi {

    public static final String PUSH = "push_agent";

    public static String call(@NonNull Site site, @NonNull ArrayMap<String, String> params) throws IOException {
        if (!site.getExt().isEmpty()) params.put("extend", site.getExt());
        Call call = site.getExt().length() <= 1000 ? OkHttp.newCall(site.getApi(), site.getHeader(), params) : OkHttp.newCall(site.getApi(), site.getHeader(), OkHttp.toBody(params));
        try (Response response = call.execute()) {
            return response.body().string();
        }
    }

    private static boolean isSpider(@NonNull Site site) {
        return site.getType() == 3;
    }

    private static String ac(int type) {
        return type == 0 ? "videolist" : "detail";
    }

    @NonNull
    public static Result homeContent(@NonNull Site site) throws Exception {
        if (isSpider(site)) {
            Spider spider = site.recent().spider();
            boolean crash = Prefers.getBoolean("crash");
            String home = crash ? "" : spider.homeContent(true);
            String video = crash ? "" : spider.homeVideoContent();
            Prefers.put("crash", false);
            SpiderDebug.log("home", home);
            SpiderDebug.log("homeVideo", video);
            Result result = Result.fromJson(home);
            List<Vod> list = Result.fromJson(video).getList();
            if (!list.isEmpty()) result.setList(list);
            setTypes(site, result);
            return result;
        } else if (site.getType() == 4) {
            ArrayMap<String, String> params = new ArrayMap<>();
            params.put("filter", "true");
            String homeContent = call(site.fetchExt(), params);
            SpiderDebug.log("home", homeContent);
            Result result = Result.fromJson(homeContent);
            setTypes(site, result);
            return result;
        } else {
            try (Response response = OkHttp.newCall(site.getApi(), site.getHeader()).execute()) {
                String homeContent = response.body().string();
                SpiderDebug.log("home", homeContent);
                Result result = Result.fromType(site.getType(), homeContent);
                fetchPic(site, result);
                setTypes(site, result);
                return result;
            }
        }
    }

    @NonNull
    public static Result categoryContent(@NonNull String key, @NonNull String tid, @NonNull String page, boolean filter, @NonNull HashMap<String, String> extend) throws Exception {
        SpiderDebug.log("category", "key=%s,tid=%s,page=%s,filter=%s,extend=%s", key, tid, page, filter, extend);
        Site site = VodConfig.get().getSite(key);
        if (isSpider(site)) {
            String categoryContent = site.recent().spider().categoryContent(tid, page, filter, extend);
            SpiderDebug.log("category", categoryContent);
            return Result.fromJson(categoryContent);
        } else {
            ArrayMap<String, String> params = new ArrayMap<>();
            if (site.getType() == 1 && !extend.isEmpty()) params.put("f", App.gson().toJson(extend));
            if (site.getType() == 4) params.put("ext", Util.base64(App.gson().toJson(extend), Util.URL_SAFE));
            params.put("ac", ac(site.getType()));
            params.put("t", tid);
            params.put("pg", page);
            String categoryContent = call(site, params);
            SpiderDebug.log("category", categoryContent);
            return Result.fromType(site.getType(), categoryContent);
        }
    }

    @NonNull
    public static Result detailContent(@NonNull String key, @NonNull String id) throws Exception {
        SpiderDebug.log("detail", "key=%s,id=%s", key, id);
        if (WebHomeInlineVodStore.KEY.equals(key)) return WebHomeInlineVodStore.detail(id);
        Site site = VodConfig.get().getSite(key);
        if (site.isEmpty() && PUSH.equals(key)) {
            Vod vod = new Vod();
            vod.setId(id);
            vod.setName(id);
            vod.setPlayUrl(id);
            vod.setPlayFrom(ResUtil.getString(R.string.push));
            vod.setPic(ResUtil.getString(R.string.push_image));
            Source.get().parse(vod.setFlags());
            return Result.vod(vod);
        } else if (isSpider(site)) {
            String detailContent = site.recent().spider().detailContent(Arrays.asList(id));
            SpiderDebug.log("detail", detailContent);
            Result result = Result.fromJson(detailContent);
            Source.get().parse(result.getVod().setFlags());
            return result;
        } else {
            ArrayMap<String, String> params = new ArrayMap<>();
            params.put("ac", ac(site.getType()));
            params.put("ids", id);
            String detailContent = call(site, params);
            SpiderDebug.log("detail", detailContent);
            Result result = Result.fromType(site.getType(), detailContent);
            Source.get().parse(result.getVod().setFlags());
            return result;
        }
    }

    @NonNull
    public static Result playerContent(@NonNull String key, @NonNull String flag, @NonNull String id) throws Exception {
        return playerContent(key, flag, id, PlayerSetting.getPlayer());
    }

    @NonNull
    public static Result playerContent(@NonNull String key, @NonNull String flag, @NonNull String id, int playerType) throws Exception {
        SpiderDebug.log("player", "key=%s,flag=%s,id=%s", key, flag, id);
        Source.get().stop();
        if (WebHomeInlineVodStore.KEY.equals(key)) return WebHomeInlineVodStore.player(flag, id);
        if (HkDetailBridge.isHkKey(key)) return hkPlayerContent(key, flag, id, playerType);
        Site site = VodConfig.get().getSite(key);
        if (site.getType() == 3) {
            String playerContent = site.recent().spider().playerContent(flag, id, VodConfig.get().getFlags());
            SpiderDebug.log("player", playerContent);
            Result result = Result.fromJson(playerContent);
            if (result.getFlag().isEmpty()) result.setFlag(flag);
            result.setUrl(Source.get().fetch(result, playerType));
            result.setHeader(site.getHeader());
            result.setKey(key);
            return result;
        } else if (site.getType() == 4) {
            ArrayMap<String, String> params = new ArrayMap<>();
            params.put("play", id);
            params.put("flag", flag);
            String playerContent = call(site, params);
            SpiderDebug.log("player", playerContent);
            Result result = Result.fromJson(playerContent);
            if (result.getFlag().isEmpty()) result.setFlag(flag);
            result.setUrl(Source.get().fetch(result, playerType));
            result.setHeader(site.getHeader());
            return result;
        } else if (site.isEmpty() && "push_agent".equals(key)) {
            Result result = new Result();
            result.setUrl(id);
            result.setParse(0);
            result.setFlag(flag);
            result.setUrl(Source.get().fetch(result, playerType));
            SpiderDebug.log("player", result.toString());
            return result;
        } else {
            Result result = new Result();
            result.setUrl(id);
            result.setFlag(flag);
            result.setHeader(site.getHeader());
            result.setPlayUrl(site.getPlayUrl());
            result.setParse(Sniffer.isVideoFormat(id) && result.getPlayUrl().isEmpty() ? 0 : 1);
            result.setUrl(Source.get().fetch(result, playerType));
            SpiderDebug.log("player", result.toString());
            return result;
        }
    }

    @NonNull
    public static Result searchContent(@NonNull Site site, @NonNull String keyword, boolean quick, @NonNull String page) throws Exception {
        SpiderDebug.log("search", "site=%s,keyword=%s,quick=%s,page=%s", site.getName(), keyword, quick, page);
        boolean hasPage = !page.equals("1");
        if (isSpider(site)) {
            String searchContent = hasPage ? site.spider().searchContent(keyword, quick, page) : site.spider().searchContent(keyword, quick);
            SpiderDebug.log("search", searchContent);
            Result result = Result.fromJson(searchContent);
            for (Vod vod : result.getList()) vod.setSite(site);
            return result;
        } else {
            ArrayMap<String, String> params = new ArrayMap<>();
            params.put("wd", keyword);
            params.put("quick", String.valueOf(quick));
            params.put("extend", "");
            if (hasPage) params.put("pg", page);
            String searchContent = call(site, params);
            SpiderDebug.log("search", searchContent);
            Result result = fetchPic(site, Result.fromType(site.getType(), searchContent));
            for (Vod vod : result.getList()) vod.setSite(site);
            return result;
        }
    }

    @NonNull
    public static Result action(@NonNull String key, @NonNull String action) throws Exception {
        Site site = VodConfig.get().getSite(key);
        SpiderDebug.log("action", "key=%s,action=%s", key, action);
        if (site.getType() == 3) return Result.fromJson(site.recent().spider().action(action));
        if (site.getType() == 4) return Result.fromJson(OkHttp.string(action));
        return Result.empty();
    }

    @NonNull
    public static Result fetchPic(@NonNull Site site, @NonNull Result result) throws Exception {
        if (site.getType() > 2 || result.getList().isEmpty() || !result.getVod().getPic().isEmpty()) return result;
        ArrayList<String> ids = new ArrayList<>();
        boolean empty = site.getCategories().isEmpty();
        for (Vod item : result.getList()) if (empty || site.getCategories().contains(item.getTypeName())) ids.add(item.getId());
        if (ids.isEmpty()) return result.clear();
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("ac", ac(site.getType()));
        params.put("ids", TextUtils.join(",", ids));
        try (Response response = OkHttp.newCall(site.getApi(), site.getHeader(), params).execute()) {
            result.setList(Result.fromType(site.getType(), response.body().string()).getList());
            return result;
        }
    }

    private static void setTypes(@NonNull Site site, @NonNull Result result) {
        result.getTypes().stream().filter(type -> result.getFilters().containsKey(type.getTypeId())).forEach(type -> type.setFilters(result.getFilters().get(type.getTypeId())));
        if (site.getCategories().isEmpty()) return;
        Map<String, Class> typeByName = new HashMap<>();
        result.getTypes().forEach(type -> typeByName.put(type.getTypeName(), type));
        List<Class> types = site.getCategories().stream().map(typeByName::get).filter(Objects::nonNull).toList();
        if (!types.isEmpty()) result.setTypes(types);
    }

    /**
     * 海阔播放路由缓存。
     *
     * <p>根因：之前每次播放都 {@code HkRouter.open(title)} 全新初始化——新建 QuickJSContext、
     * 注册上百个 API、读 kv/config 文件、跑 preRule（很多规则的 preRule 会发网络请求）。
     * 点一次封面要等好几秒，大头是这里重复初始化，而不是真正的地址解析。</p>
     *
     * <p>缓存按规则名复用已初始化的 HkRouter：HkJsRuntime 的 JS 调用全部走单线程 executor
     * 串行，跨播放复用线程安全；每次取用前校验规则文件 mtime（用户改规则立即失效）
     * 与 15 分钟 TTL（preRule 里的登录态/cookie 定期刷新）。最多缓存 3 个规则，
     * 淘汰时 destroy 释放 QuickJS 上下文。</p>
     */
    private static final int HK_ROUTER_CACHE_MAX = 3;
    private static final long HK_ROUTER_CACHE_TTL_MS = 15 * 60 * 1000L;
    private static final ExecutorService HK_INIT_EXEC = Executors.newSingleThreadExecutor();
    private static final ConcurrentHashMap<String, HkRouterCacheEntry> hkRouterCache = new ConcurrentHashMap<>();

    private static class HkRouterCacheEntry {
        volatile Future<HkRouter> future;
        volatile long ruleModified;
        volatile long createdAt;
    }

    /**
     * 预热某规则的播放路由（点封面预检出直接播放地址后调用）：后台提前做
     * QuickJS 初始化 + preRule，等播放器真正调 playerContent 时缓存已就绪。
     * UI 线程调用也安全，只提交任务立即返回。
     */
    public static void warmHkRouter(String title) {
        if (title == null || title.isEmpty()) return;
        try {
            acquireHkRouterFuture(title);
        } catch (Throwable ignored) {
        }
    }

    /** 取已初始化的 HkRouter（缓存命中直接返回，未命中则同步初始化）。 */
    private static HkRouter acquireHkRouter(String title) throws Exception {
        Future<HkRouter> f = acquireHkRouterFuture(title);
        try {
            HkRouter router = f.get(30, TimeUnit.SECONDS);
            // 口令流程的一次性 input 覆盖只属于详情页那次求值，播放路由复用时清掉，
            // 防止上次残留污染本次 lazyRule 的 input。
            try {
                router.getEngine().getJsRuntime().setNextInputOverride(null);
            } catch (Throwable ignored) {
            }
            return router;
        } catch (ExecutionException ee) {
            // 初始化失败：清掉坏条目，下次重建；把原始异常抛给调用方（行为与之前一致）
            hkRouterCache.remove(title);
            Throwable c = ee.getCause();
            if (c instanceof Exception) throw (Exception) c;
            throw new RuntimeException(c);
        }
    }

    private static synchronized Future<HkRouter> acquireHkRouterFuture(String title) {
        long mtime = HkRuleManager.get().ruleModified(title);
        HkRouterCacheEntry e = hkRouterCache.get(title);
        if (e != null && e.ruleModified == mtime && !hkRouterExpired(e) && !hkRouterFailed(e)) {
            return e.future;
        }
        if (e != null) {
            hkRouterCache.remove(title);
            destroyHkRouterFuture(e.future);
        }
        // LRU 淘汰：按创建时间踢掉最老的
        while (hkRouterCache.size() >= HK_ROUTER_CACHE_MAX) {
            String oldest = null;
            long oldestAt = Long.MAX_VALUE;
            for (Map.Entry<String, HkRouterCacheEntry> en : hkRouterCache.entrySet()) {
                if (en.getValue().createdAt < oldestAt) {
                    oldestAt = en.getValue().createdAt;
                    oldest = en.getKey();
                }
            }
            if (oldest == null) break;
            HkRouterCacheEntry out = hkRouterCache.remove(oldest);
            if (out != null) destroyHkRouterFuture(out.future);
        }
        HkRouterCacheEntry ne = new HkRouterCacheEntry();
        ne.ruleModified = mtime;
        ne.createdAt = System.currentTimeMillis();
        ne.future = HK_INIT_EXEC.submit(() -> HkRouter.open(title));
        hkRouterCache.put(title, ne);
        return ne.future;
    }

    private static boolean hkRouterExpired(HkRouterCacheEntry e) {
        return System.currentTimeMillis() - e.createdAt > HK_ROUTER_CACHE_TTL_MS;
    }

    private static boolean hkRouterFailed(HkRouterCacheEntry e) {
        Future<HkRouter> f = e.future;
        if (f == null || !f.isDone() || f.isCancelled()) return false;
        try {
            f.get(1, TimeUnit.MILLISECONDS);
            return false;
        } catch (ExecutionException ee) {
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void destroyHkRouterFuture(Future<HkRouter> f) {
        if (f == null) return;
        if (!f.isDone()) {
            // 初始化还在排队/执行：cancel(false) 不中断，只防未开始的任务启动；
            // 若已在执行，完成后顺手 destroy，避免泄漏 QuickJS 上下文。
            f.cancel(false);
            HK_INIT_EXEC.submit(() -> {
                try {
                    HkRouter r = f.get(60, TimeUnit.SECONDS);
                    if (r != null) r.destroy();
                } catch (Throwable ignored) {
                }
            });
            return;
        }
        try {
            HkRouter r = f.get(1, TimeUnit.MILLISECONDS);
            if (r != null) r.destroy();
        } catch (Throwable ignored) {
        }
    }

    /**
     * 取已初始化的 HkRouter（给详情页用）：与播放链共用 hkRouterCache，
     * 同规则同会话只初始化一次（preRule 只在首次建立时跑），不再每次进详情
     * new HkRouter + destroy。key 维度=规则名，与缓存现有设计一致，不会串规则。
     */
    public static HkRouter getHkRouter(String title) throws Exception {
        return acquireHkRouter(title);
    }

    /**
     * 海阔小程序播放（M4）：{@code hk_<规则名>} key 拦截 → {@link HkRouter#play(String)}
     * 分流链解析出真地址 → 直接组装 Result，不走 spider/嗅探。
     *
     * <p>详情页点选集（VideoActivity → SiteViewModel.playerContent → 本方法）统一走这里，
     * 因此选集、下一集、历史续播都会先经过海阔分流链。</p>
     */
    @NonNull
    private static Result hkPlayerContent(@NonNull String key, @NonNull String flag, @NonNull String id, int playerType) throws Exception {
        String title = key.substring(HkDetailBridge.KEY_PREFIX.length());
        // 路由缓存复用：不再每次播放重建 QuickJS 上下文+跑 preRule（之前点封面等几秒的主因）
        HkRouter router = acquireHkRouter(title);
        // 阅读分流（小说规则）：直接返回阅读会话，正文在阅读器里懒取，不跑 play 分流链。
        // 视频规则不受影响（type 不是"小说"）。
        if (ReadingGate.isHkNovelRule(title)) {
            Result novelResult = new Result();
            novelResult.setReadingSession(ReadingGate.fromHkNovel(title, "", null, 0));
            novelResult.setKey(key);
            novelResult.setFlag(flag);
            SpiderDebug.log("player", "hk reading diverted kind=NOVEL rule=%s", title);
            return novelResult;
        }
        // S6：页内切集只有 (key, flag, id) 三元组，条目 extra 由组装方登记在
        // HkDetailBridge 注册表里，按 (规则名, 地址) 查表补传——与 HkPageActivity
        // 点集时 play(url, extra) 的上下文对齐；未登记时保持无 extra 旧路。
        String hkExtra = HkDetailBridge.episodeExtra(key, id);
        HkPlay play = hkExtra == null ? router.play(id) : router.play(id, hkExtra);
        // 阅读分流（漫画 pics://）：命中则直接返回带会话的 Result，不再组装视频地址。
        // pics 只由 pics:// 分支产出，视频规则永远不走该分支。
        // 整套 play 请求头带进会话（旧口径只摘 Referer 一个值，其余头在此全丢——漫画没画面断点）
        ReadingSession comic = ReadingGate.fromHkComic(title, "", "", id, play, play.getHeaders());
        if (comic != null) {
            Result comicResult = new Result();
            comicResult.setReadingSession(comic);
            comicResult.setKey(key);
            comicResult.setFlag(flag);
            SpiderDebug.log("player", "hk reading diverted kind=COMIC rule=%s pics=%d", title, play.getPics().size());
            return comicResult;
        }
        if (play.isPicsAttempted()) {
            // 是 pics:// 漫画但分流门没接（图空/条目不合门的判据）：不许抛错
            // 拦详情（fix4——1.4.19 真机「漫画详情页都进不去」的根），也
            // 不许落进下面的视频装配。组最小漫画会话交阅读器：会话图先行，
            // 空时阅读器自带 router.play(章地址) 回退重取，不在此处判死。
            SpiderDebug.log("hk", "pics no-divert fallback-session rule=%s pics=%d page=%s",
                    title, play.getPics().size(), play.getPageUrl());
            ReadingSession fallback = new ReadingSession();
            fallback.kind = ReadingSession.Kind.COMIC;
            fallback.sourceName = title;
            fallback.ruleTitle = title;
            fallback.bookTitle = "";
            fallback.chapterUrl = id;
            ReadingSession.Chapter fc = new ReadingSession.Chapter("", id);
            fallback.chapters.add(fc);
            fallback.chapterIndex = 0;
            fallback.images = new java.util.ArrayList<>(play.getPics());
            String producedPage = play.getPageUrl();
            if (producedPage != null && (producedPage.startsWith("http://")
                    || producedPage.startsWith("https://"))) {
                fallback.pageUrl = producedPage;
            }
            java.util.Map<String, String> playHeaders = play.getHeaders();
            if (playHeaders != null) {
                fallback.playHeaders.putAll(playHeaders);
                String ref = playHeaders.get("Referer");
                if (ref != null) fallback.referer = ref;
            }
            Result fallbackResult = new Result();
            fallbackResult.setReadingSession(fallback);
            fallbackResult.setKey(key);
            fallbackResult.setFlag(flag);
            return fallbackResult;
        }
        // P2 多线路：默认首线。该线路的地址+请求头按下标取、不跨线路合并
        // （8.83 getPlayData/getHeaders 语义）。原 HkRouter.lineIndex 记忆表
        // 全树无写方（rememberLine 无调用），星辰 1.4.137 已删除该死机制，
        // 本处随上游收口为恒首线，行为与此前一致。
        int line = 0;
        Result result = new Result();
        result.setUrl(play.getUrl(line));
        result.setParse(0);
        result.setFlag(flag);
        java.util.Map<String, String> lineHeaders = play.getHeaders(line);
        if (!lineHeaders.isEmpty()) result.setHeader(lineHeaders);
        // 规则解析出的字幕此前在 HkPlay 里带着、到这一步被丢掉（审计 P7）：
        // 照 8.83 PlayerChooser 把 subtitle 传进播放器的做法接到 Result 上。
        if (!play.getSubtitle().isEmpty()) {
            result.setSubs(java.util.List.of(com.fongmi.android.tv.bean.Sub.from(play.getSubtitle())));
        }
        // 海阔播放 JSON 的 danmu 附带字段同 subtitle 一路接进 Result（G49/G59）：
        // 官侧靠 PlayData 整串传递到播放器取 danmu，咱家经 HkPlay 承载至此入
        // Result 弹幕面（PlaySpec.fromResult 消费 getDanmaku）。
        if (!play.getDanmu().isEmpty()) {
            result.setDanmaku(java.util.List.of(com.fongmi.android.tv.bean.Danmaku.from(play.getDanmu())));
        }
        // 环34：PlayData 另两字段 audioUrls/lyric 此前零消费（解析对齐、终端
        // 断点）——照 subtitle/danmu 同路入 Result（transient），PlaySpec.from
        // 携带进播放层；DLNA 面的 DlanUrlDTO 在此（PlayData 在手点）写入
        // audioUrl 首条＋danmu/subtitle（官由播放器起播时写 PlayData 全份）。
        if (play.getAudioUrls() != null && !play.getAudioUrls().isEmpty()) {
            // 复核订正 B2：audioUrls 与 urls 按下标平行配对（官 DLNA 取
            // audioUrls[switchIndex]、越界回首条）——本线伴随只入本线一条，
            // PlaySpec 装配处再与主址合流；DLNA 首条口径在下方 updateMediaInfo
            // 保持不变。
            result.setHkAudioUrls(com.fongmi.android.tv.player.engine.HkPlayCompanion.companionForLine(play.getAudioUrls(), line));
        }
        if (play.getLyric() != null && !play.getLyric().isEmpty()) {
            result.setHkLyric(play.getLyric());
        }
        com.fongmi.android.tv.api.hk.HkDlanState.updateMediaInfo(
                play.getAudioUrl(), play.getDanmu(), play.getSubtitle());
        result.setKey(key);
        // 磁力/电驴/thunder/ftp：播放器不认这些协议，直塞就是黑屏 0 KB/s。
        // 只拦这四类（HkRouter.isP2pPlayLink），走壳子已有的迅雷引擎边下边播；
        // 其余地址（http/m3u8 等）原样不动。失败时迅雷抛中文真错，不再黑屏无下文。
        // 门按实际要播的线路地址判定（play.getUrl(line)，即上面 setUrl 的同一串），
        // 不按默认线路地址判定：多线路时默认线与选中线 scheme 可能不同，旧写法会
        // 把选中线是 ftp 的漏过去（直塞黑屏），或把选中线是 http 的误送进 fetch。
        if (HkRouter.isP2pPlayLink(play.getUrl(line))) {
            result.setUrl(Source.get().fetch(result, playerType));
        }
        SpiderDebug.log("player", "hk play resolved: %s", result.getUrl());
        return result;
    }
}
