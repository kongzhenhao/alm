package com.xl.alm.app.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.filter.NameFilter;

import java.util.List;

public class JsonUtil {
    public JsonUtil() {
    }

    public static String toJSONString(Object object) {
        return toJSONString(object, true);
    }

    public static String toJSONString(Object object, boolean isSnakeCase) {
        return isSnakeCase ? JSON.toJSONString(object, NameFilter.of(PropertyNamingStrategy.SnakeCase), new JSONWriter.Feature[]{JSONWriter.Feature.LargeObject, JSONWriter.Feature.WriteBigDecimalAsPlain}) : JSON.toJSONString(object, new JSONWriter.Feature[]{JSONWriter.Feature.LargeObject, JSONWriter.Feature.WriteBigDecimalAsPlain});
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz, new JSONReader.Feature[]{com.alibaba.fastjson2.JSONReader.Feature.SupportSmartMatch});
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return (T)JSON.parseObject(text, clazz, new JSONReader.Feature[]{com.alibaba.fastjson2.JSONReader.Feature.SupportSmartMatch});
    }
}

