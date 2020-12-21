package com.puhuilink.qbs.core.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class QbsBeanUtils extends BeanUtils {
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, QbsBeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack!=null) {
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }


}
