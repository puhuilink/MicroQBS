/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:51
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:14:23
 */
package com.phlink.core.web.utils;

import com.phlink.core.web.controller.vo.MenuVO;
import com.phlink.core.web.entity.Permission;

import cn.hutool.core.bean.BeanUtil;

public class VoUtil {

    public static MenuVO permissionToMenuVO(Permission p) {

        MenuVO menuVo = new MenuVO();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
