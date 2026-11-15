# TV
-keep class androidx.leanback.widget.** { *; }
-keep class com.fongmi.quickjs.method.** { *; }
-keep class com.fongmi.android.tv.bean.** { *; }

# MPV JNI bridge
-keep class is.xyz.mpv.MPVLib { *; }
-keep class is.xyz.mpv.MPVLib$* { *; }

# libplayer.so resolves this class and these methods by their literal JNI names.
-keep class com.fongmi.android.tv.player.iso.IsoSessionManager {
    public static long length(long);
    public static int readAt(long, long, java.nio.ByteBuffer, int);
    public static void close(long);
    public static void prepareTrackMetadata(long, int);
}

# MPV owns one process-wide native context. Keep its lifecycle code intact so
# release inlining does not amplify timing-sensitive create/destroy transitions.
-keep,allowobfuscation class androidx.media3.mpvplayer.MpvPlayer { *; }
-keep,allowobfuscation class androidx.media3.mpvplayer.MpvPlayer$* { *; }
-keep,allowobfuscation class com.fongmi.android.tv.player.engine.MpvPlayerEngine { *; }

# Gson

# Gson 反射模型：release 开启 minifyEnabled，字段名必须固定且保留泛型签名，
# 否则 R8 会把 LabRoot.lists 混淆成 a，Gson 反序列化后 lists 恒为 null。
-keepattributes Signature
-keep class com.fongmi.android.tv.lab.LabModels { *; }
-keep class com.fongmi.android.tv.lab.LabModels$* { *; }
-keep class com.google.gson.** { *; }
-keep class com.fongmi.android.tv.remote.** { *; }
-keep class com.fongmi.android.tv.gitcloud.** { *; }

# SimpleXML
-keep interface org.simpleframework.xml.core.Label { public *; }
-keep class * implements org.simpleframework.xml.core.Label { public *; }
-keep interface org.simpleframework.xml.core.Parameter { public *; }
-keep class * implements org.simpleframework.xml.core.Parameter { public *; }
-keep interface org.simpleframework.xml.core.Extractor { public *; }
-keep class * implements org.simpleframework.xml.core.Extractor { public *; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.Path <fields>; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.Root <fields>; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.Text <fields>; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.Element <fields>; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.Attribute <fields>; }
-keepclassmembers,allowobfuscation class * { @org.simpleframework.xml.ElementList <fields>; }

# OkHttp
-dontwarn okhttp3.**
-keep class okio.** { *; }
-keep class okhttp3.** { *; }

# Kotlin
-keeppackagenames kotlin.**
-keep class kotlin.** { *; }

# JGit
-dontwarn java.lang.ProcessHandle
-dontwarn java.lang.management.ManagementFactory
-dontwarn org.eclipse.jgit.**
-keep class org.eclipse.jgit.** { *; }
-keeppackagenames org.slf4j.**
-keep class org.slf4j.** { *; }

# CatVod
-keep class com.github.catvod.Proxy { *; }
-keep class com.github.catvod.crawler.** { *; }
-keep class * extends com.github.catvod.crawler.Spider

# Jianpian
-keep class com.p2p.** { *; }

# JUPnP
-dontwarn org.jupnp.**
-keep class org.jupnp.** { *; }
-keep class javax.xml.** { *; }

# Nano
-keep class fi.iki.elonen.** { *; }

# NewPipeExtractor
-keep class javax.script.** { *; }
-keep class jdk.dynalink.** { *; }
-keep class org.mozilla.javascript.* { *; }
-keep class org.mozilla.javascript.** { *; }
-keep class org.mozilla.javascript.engine.** { *; }
-keep class org.mozilla.classfile.ClassFileWriter
-keep class org.schabi.newpipe.extractor.timeago.patterns.** { *; }
-keep class org.schabi.newpipe.extractor.services.youtube.protos.** { *; }
-dontwarn org.mozilla.javascript.JavaToJSONConverters
-dontwarn org.mozilla.javascript.tools.**
-dontwarn com.google.re2j.**
-dontwarn javax.script.**
-dontwarn jdk.dynalink.**

# Sardine
-keep class com.thegrizzlylabs.sardineandroid.** { *; }

# TVBus
-keep class com.tvbus.engine.** { *; }

# XunLei
-keep class com.xunlei.downloadlib.** { *; }

# Zxing
-keep class com.google.zxing.** { *; }

# 猫源 Node 运行时：JNI 静态注册要求类名/方法名不被混淆
-keep class com.fongmi.android.tv.nodejs.NodeBridge { *; }
-keep class com.fongmi.android.tv.node.** { *; }
-keep class com.fongmi.android.tv.api.CatSource { *; }
-keep class com.fongmi.android.tv.api.loader.CatSpider { *; }

# ===== 海阔（HK）规则（S7 自星辰真树 app/proguard-rules.pro 摘录并入，逐条注明来源；上方 oktv 自有规则未动）=====

# 海阔引擎 JS 桥（星辰原文）：HkJsRuntime 靠反射扫描 @JSAnnotation 方法生成同名 JS 函数
# （getAllFunctions），方法名即规则调用的 API 名；R8 改名后真机上 getUaObject
# 等全部 ReferenceError（本地未混淆测不出）。整类保名，含内部注解类型。
-keep class com.fongmi.android.tv.api.hk.HkJsRuntime { *; }
-keep class com.fongmi.android.tv.api.hk.HkJsRuntime$* { *; }

# JavaAdapter 抽象类桥（星辰原文）：JS 侧经反射按名调桥的覆写方法与
# super$ 化身（HkJsAdapterBridges），改名即断，整类保名。
-keep class com.fongmi.android.tv.api.hk.HkJsAdapterBridges { *; }
-keep class com.fongmi.android.tv.api.hk.HkJsAdapterBridges$* { *; }

# 图片解密桥（星辰原文）：crypto-java.js 按名调 FileUtil.toBytes（即本类），R8 改名后真机
# TypeError 找不到函数、封面空流。整类保名。
-keep class com.fongmi.android.tv.api.hk.HkCryptoFileUtil { *; }

# hikerview 本体（星辰原文）：规则 JS 在任意作用域按全名直调
# com.example.hikerview.** 的弹窗/工具/模型/枚举，无 Java 侧静态引用，
# R8 不保名 JS 侧类路径解析落空。整域保名（FileUtil 单条与星辰同为双保险）。
-keep class com.example.hikerview.utils.FileUtil { *; }
-keep class com.example.hikerview.** { *; }
-keep class com.annimon.stream.function.Consumer { *; }

# LitePal（星辰原文，官方 ProGuard 规矩）：其 AAR 不带 consumer 规则；
# 库自家记账类 org.litepal.model.Table_Schema 以字符串字面量塞进建表名单，
# 再由 LitePalBase 逐名 Class.forName 建表，改名即抛 ClassNotFound。
# 模型行覆盖 LitePalSupport/DataSupport 两代。
-keep class org.litepal.** { *; }
-keep class * extends org.litepal.crud.LitePalSupport { *; }
-keep class * extends org.litepal.crud.DataSupport { *; }

# 按名直调库整包保名（星辰原文）：海阔规则 JS 经 Rhino Packages 按全限定名直调库类
# （HkJsRuntime 无白名单），官方包不混淆、类名全原样，规则生态按原名写死。
# 星辰成品 dex 实证五库原名在位率个位数百分比，整包保名与 org.litepal 同口径。
-keep class com.lxj.xpopup.** { *; }
-keep class com.alibaba.fastjson.** { *; }
-keep class com.bumptech.glide.** { *; }
-keep class org.jsoup.** { *; }
-keep class org.greenrobot.eventbus.** { *; }

# 按名面同口径（星辰原文）：xtooltip 由 hikerPop 经 Packages 按名直调（Tooltip/ClosePolicy），
# okhttp3.brotli.BrotliInterceptor 由宿主 Class.forName 按名探入并反射读 INSTANCE 字段。
-keep class it.sephiroth.android.library.xtooltip.** { *; }
-keep class okhttp3.brotli.** { *; }

# 海阔模型（星辰原文）：Gson 按字段名序列化/反序列化（MY_RULE、hiker://page 规则体、
# 页面缓存与落盘结构），字段名不能改。HkItem/PageCache 另有 @SerializedName 钉死，保名作双保险。
-keep class com.fongmi.android.tv.api.hk.HkRule { *; }
-keep class com.fongmi.android.tv.api.hk.HkPageParser$HkPage { *; }
-keep class com.fongmi.android.tv.api.hk.HkSubscribeRecord { *; }
-keep class com.fongmi.android.tv.api.hk.HkPlayData { *; }
-keep class com.fongmi.android.tv.api.hk.HkItem { *; }
-keep class com.fongmi.android.tv.ui.hk.HkAutoCacheUtil$PageCache { *; }
# 同口径加固（星辰原文）：含泛型集合字段、走 Gson .class 读回的落盘/缓存结构。
-keep class com.fongmi.android.tv.api.hk.HkProxyServer$ProxyResult { *; }
-keep class com.fongmi.android.tv.api.hk.browser.HkBrowserProxyRule { *; }
-keep class com.fongmi.android.tv.ui.hk.HkX5Extra { *; }

# EventBus（星辰原文，官方 ProGuard 规矩）：本应用未注册 EventIndex、靠反射扫
# @Subscribe 方法，订阅方法被裁/改名即事件静默不达。
# （-keepattributes *Annotation* 已由 proguard-rules-media.pro 覆盖。）
-keepclassmembers class ** { @org.greenrobot.eventbus.Subscribe <methods>; }
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent { <init>(java.lang.Throwable); }

# fastjson 1.2.83（星辰原文）桌面/可选编解码器引用了安卓不存在的类，
# 官方安卓接法即以 dontwarn 放行这四系（这些编解码器在安卓上永不触发）。逐组列明：
-dontwarn java.awt.**
-dontwarn org.joda.**
-dontwarn org.javamoney.**
-dontwarn springfox.**
# fastjson 全包 keep 后补三系（星辰原文）：support.spring/jaxrs/retrofit 集成类随全包 keep 成根，
# 其可选框架本体不在安卓依赖内（服务端适配、安卓永不触发），同口径 dontwarn 放行（keep 本体不动）。
-dontwarn org.springframework.**
-dontwarn org.glassfish.jersey.**
-dontwarn retrofit2.**
