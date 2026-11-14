package com.fongmi.android.tv.ui.hk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * leanback 一期桩：只保留 main 源集 HkJsRuntime 的 showSelectOptions 桥用到的
 * 两个纯数据嵌套类（SelectExtra/SelectOption，与 mobile 真实现的解析语义逐行
 * 对齐）；mobile 版的弹窗分发 UI 不在 TV 端提供，事件在 leanback 无订阅者。
 * mobile 源集有同名真实现，两 flavor 互不干扰。
 */
public class HkDetailBridge {

    /** 照抄 detail/model/SelectOption（字段 icon/title）。 */
    public static final class SelectOption {
        public String icon;
        public String title;
    }

    /** 照抄 detail/model/SelectExtra：默认值 col=3、selectedIndex=-1、attachView/bottom=false；
     * options 数组首项为对象时整组转入 options2。 */
    public static final class SelectExtra {
        public boolean attachView;
        public boolean bottom;
        public int col = 3;
        public String js;
        public List<String> options;
        public List<SelectOption> options2;
        public int selectedIndex = -1;
        public String title;

        public static SelectExtra parse(String json) {
            SelectExtra extra = new SelectExtra();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            JsonArray optionsArr = obj.has("options") && obj.get("options").isJsonArray()
                    ? obj.getAsJsonArray("options") : null;
            if (optionsArr != null && optionsArr.size() > 0 && optionsArr.get(0).isJsonObject()) {
                extra.options2 = parseOptions2(optionsArr);
            } else if (optionsArr != null) {
                extra.options = new ArrayList<>();
                for (JsonElement el : optionsArr) {
                    extra.options.add(el.isJsonPrimitive() ? el.getAsString() : String.valueOf(el));
                }
            }
            if (obj.has("options2") && obj.get("options2").isJsonArray()) {
                extra.options2 = parseOptions2(obj.getAsJsonArray("options2"));
            }
            if (obj.has("col") && obj.get("col").isJsonPrimitive()) extra.col = obj.get("col").getAsInt();
            if (obj.has("selectedIndex") && obj.get("selectedIndex").isJsonPrimitive()) {
                extra.selectedIndex = obj.get("selectedIndex").getAsInt();
            }
            if (obj.has("attachView") && obj.get("attachView").isJsonPrimitive()) {
                extra.attachView = obj.get("attachView").getAsBoolean();
            }
            if (obj.has("bottom") && obj.get("bottom").isJsonPrimitive()) {
                extra.bottom = obj.get("bottom").getAsBoolean();
            }
            extra.title = jsonStr(obj, "title");
            extra.js = jsonStr(obj, "js");
            return extra;
        }

        private static List<SelectOption> parseOptions2(JsonArray arr) {
            List<SelectOption> list = new ArrayList<>();
            for (JsonElement el : arr) {
                SelectOption option = new SelectOption();
                if (el.isJsonObject()) {
                    option.title = jsonStr(el.getAsJsonObject(), "title");
                    option.icon = jsonStr(el.getAsJsonObject(), "icon");
                }
                list.add(option);
            }
            return list;
        }
    }

    private static String jsonStr(JsonObject obj, String key) {
        if (obj == null || !obj.has(key) || obj.get(key).isJsonNull()) return null;
        JsonElement el = obj.get(key);
        return el.isJsonPrimitive() ? el.getAsString() : el.toString();
    }
}
