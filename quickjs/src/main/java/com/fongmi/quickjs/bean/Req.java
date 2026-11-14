package com.fongmi.quickjs.bean;

import android.text.TextUtils;

import com.github.catvod.utils.Json;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Req {

    @SerializedName("buffer")
    private Integer buffer;
    @SerializedName("redirect")
    private Integer redirect;
    @SerializedName("timeout")
    private Integer timeout;
    @SerializedName("postType")
    private String postType;
    @SerializedName("method")
    private String method;
    @SerializedName("body")
    private String body;
    @SerializedName("data")
    private JsonElement data;
    @SerializedName("headers")
    private JsonElement headers;
    // 海阔增量（照星辰 Req）：fetch 选项的 per-request dns 与 rejectCoding
    @SerializedName("rejectCoding")
    private Boolean rejectCoding;
    @SerializedName("dns")
    private String dns;

    public static Req objectFrom(String json) {
        return new Gson().fromJson(json, Req.class);
    }

    public int getBuffer() {
        return buffer == null ? 0 : buffer;
    }

    public Integer getRedirect() {
        return redirect == null ? 1 : redirect;
    }

    public Integer getTimeout() {
        return timeout == null ? 10000 : timeout;
    }

    public String getPostType() {
        return TextUtils.isEmpty(postType) ? "json" : postType;
    }

    public String getMethod() {
        return TextUtils.isEmpty(method) ? "get" : method;
    }

    public String getBody() {
        return body;
    }

    public JsonElement getData() {
        return data;
    }

    private JsonElement getHeaders() {
        return headers;
    }

    public boolean isRedirect() {
        return getRedirect() == 1;
    }

    public Map<String, String> getHeader() {
        return Json.toMap(getHeaders());
    }

    public String getCharset() {
        Map<String, String> header = getHeader();
        List<String> keys = Arrays.asList("Content-Type", "content-type");
        for (String key : keys) if (header.containsKey(key)) return getCharset(Objects.requireNonNull(header.get(key)));
        return "UTF-8";
    }

    private String getCharset(String value) {
        for (String text : value.split(";")) if (text.contains("charset=")) return text.split("=")[1];
        return "UTF-8";
    }

    // ---- 海阔增量（照星辰 Req 同名法）：HkHttp/HkJsRuntime 用 ----
    public String getDns() {
        return dns;
    }

    public boolean isRejectCoding() {
        return rejectCoding != null && rejectCoding;
    }

    /** 请求头 content-type 里显式声明的 charset（charset=/charst=），未声明回 null。 */
    public String getCharsetOrNull() {
        Map<String, String> header = getHeader();
        List<String> keys = Arrays.asList("content-type", "Content-Type", "Content-type");
        for (String key : keys) {
            String v = header.get(key);
            if (v != null && !v.isEmpty()) return charsetRemainder(v);
        }
        return null;
    }

    private static String charsetRemainder(String value) {
        int i = value.indexOf("charset=");
        if (i >= 0) return value.substring(i + "charset=".length());
        int j = value.indexOf("charst=");
        if (j >= 0) return value.substring(j + "charst=".length());
        return null;
    }
}
