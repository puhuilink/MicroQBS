package com.phlink.qbs.core.base.utils;

import java.util.HashMap;
import java.util.Map;

public class InheritableThreadLocalUtil {
    private static final ThreadLocal<Map<Class<?>, Object>> CONTEXT = new InheritableThreadLocal<>();

    /**
     * 把参数设置到上下文的Map中
     */
    public static void put(Object obj) {
        Map<Class<?>, Object> map = CONTEXT.get();
        if (map == null) {
            map = new HashMap<>();
            CONTEXT.set(map);
        }
        if (obj instanceof Enum) {
            map.put(obj.getClass().getSuperclass(), obj);
        } else {
            map.put(obj.getClass(), obj);
        }
    }

    /**
     * 从上下文中，根据类名取出参数
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> c) {
        Map<Class<?>, Object> map = CONTEXT.get();
        if (map == null) {
            return null;
        }
        return (T) map.get(c);
    }

    /**
     * 清空ThreadLocal的数据
     */
    public static void clean() {
        CONTEXT.remove();
    }
}
