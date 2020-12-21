/*
 * @Author: sevncz.wen
 * @Date: 2020-08-21 11:01
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-21 11:01
 */
package com.puhuilink.qbs.core.base.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.puhuilink.qbs.core.base.gson.annotation.Exclude;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-21 11:01
 **/
public class ExcludeExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
