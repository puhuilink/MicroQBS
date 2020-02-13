package com.phlink.bus.core.model;

import lombok.Data;

import java.util.Map;

@Data
public class PushBean {
    // 必填, 通知内容, 内容可以为空字符串，则表示不展示到通知栏。
    private String alert;

    // 可选, 附加信息, 供业务使用。
    private Map<String, String> extras;

    //android专用
    // 可选, 通知标题,如果指定了，则通知里原来展示 App名称的地方，将展示成这个字段。
    private String title;

}