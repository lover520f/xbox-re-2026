package com.fongmi.android.tv.localbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * oktvpro 一期桩：本地 EPUB 解析不移植。parse 直接抛异常，
 * HkEpubBridge 的调用点均在 try/catch 内，表现为电子书不可用。
 */
public class LocalEpub {

    public static class Book {
        public String title = "";
        public String charset = "UTF-8";
        public String opfDir = "";
        public List<SpineItem> spine = new ArrayList<>();
        public String coverEntry = "";
        public long textChars = 0;
        public int imageCount = 0;
    }

    public static class SpineItem {
        public String id = "";
        public String path = "";
        public String title = "";
    }

    public static class DrmException extends Exception {
        public DrmException() {
            super("epub unavailable");
        }
    }

    public static boolean hasDrm(ZipFile zip) {
        return false;
    }

    public static Book parse(File epubFile) throws Exception {
        throw new Exception("LocalEpub not available in oktvpro phase 1");
    }

    public static void analyze(File epubFile, Book book) throws Exception {
        throw new Exception("LocalEpub not available in oktvpro phase 1");
    }

    public static String textOf(File epubFile, SpineItem item) {
        return "";
    }

    public static List<String> imagesOf(File epubFile, SpineItem item, String opfDir) {
        return new ArrayList<>();
    }

    public static byte[] rawOf(ZipFile zip, String entryPath) throws Exception {
        return null;
    }

    public static String normalize(String path) {
        return path == null ? "" : path.replace('\\', '/');
    }
}
