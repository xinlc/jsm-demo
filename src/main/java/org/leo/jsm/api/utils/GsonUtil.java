package org.leo.jsm.api.utils;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static String toJson(Object obj, Type type) {
        try {
            return URLEncoder.encode(gson.toJson(obj, type), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return gson.toJson(obj, type);
        }

    }

    public static Object fromJson(String str, Type type) {
        String str1 = "";
        try {
            str1 = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            str1 = str;
            e.printStackTrace();
        }
        return gson.fromJson(str1, type);
    }

    public static Object fromJson(Reader reader, Type type) {
        return gson.fromJson(reader, type);
    }
}
