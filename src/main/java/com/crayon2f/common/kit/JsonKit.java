package com.crayon2f.common.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by feifan.gou@gmail.com on 2018/7/31 15:03.
 */
public final class JsonKit {

    public static Map<String, Object> toMap(String source) {

        @SuppressWarnings("unchecked")
        Map<String, Object> result = parse(source, Map.class);
        return result;
    }

    public static <T> List<T> toList(String source, Class<T> clazz) {

        return JSONArray.parseArray(source, clazz);
    }

    public static <T> Set<T> toSet(String source, Class<T> clazz) {

        return new HashSet<>(toList(source, clazz));
    }

    public static String toJson(Object object) {

        return JSON.toJSONString(object);
    }

    public static <T> T parse(String source, Class<T> clazz) {

        try {
            return JSON.parseObject(source, clazz);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
