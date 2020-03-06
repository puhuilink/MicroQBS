package com.phlink.core.util;

import cn.hutool.core.bean.BeanUtil;
import com.phlink.core.controller.vo.MenuVO;
import com.phlink.core.entity.Permission;

public class VoUtil {

    public static MenuVO permissionToMenuVO(Permission p){

        MenuVO menuVo = new MenuVO();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}