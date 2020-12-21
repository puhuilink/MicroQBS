/*
 * @Author: sevncz.wen
 * @Date: 2020-08-21 11:00
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-21 11:00
 */
package com.puhuilink.qbs.core.base.gson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-21 11:00
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Exclude {
}
