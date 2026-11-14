package com.fongmi.android.tv.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.github.catvod.net.OkHttp;

import java.io.InputStream;

@GlideModule
public class OkGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        // 海阔图片链（transplant-set §5.4，照星辰 OkGlideModule 同位）：GlideUrl→InputStream
        // 改走 HkGlideUrlLoader（HkHttp.imageClient：DoH/CookieJar/Brotli + @js= 流式解密）。
        // 其 handles() 恒 true、fetcher 对普通 URL 即标准 OkHttp 抓取，星辰线上全量图片均走此链。
        registry.replace(GlideUrl.class, InputStream.class, new com.fongmi.android.tv.api.hk.HkGlideUrlLoader.Factory(com.fongmi.android.tv.api.hk.HkHttp.imageClient()));
    }
}