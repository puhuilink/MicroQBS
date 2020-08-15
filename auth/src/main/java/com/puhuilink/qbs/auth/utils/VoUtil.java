/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:51
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:14:23
 */
package com.puhuilink.qbs.auth.utils;

import com.puhuilink.qbs.auth.controller.vo.MenuVO;
import com.puhuilink.qbs.auth.entity.Permission;
import org.springframework.beans.BeanUtils;


public class VoUtil {

    public static MenuVO permissionToMenuVO(Permission p) {
        MenuVO menuVo = new MenuVO();
        BeanUtils.copyProperties(p, menuVo);
        return menuVo;
    }
}
