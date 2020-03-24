package com.phlink.core.web.util;

import cn.hutool.core.bean.BeanUtil;
import com.phlink.core.web.controller.vo.MenuVO;
import com.phlink.core.web.entity.Permission;

public class VoUtil {

    public static MenuVO permissionToMenuVO(Permission p){

        MenuVO menuVo = new MenuVO();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}