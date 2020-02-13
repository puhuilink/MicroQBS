package com.phlink.bus.core.model;

import lombok.Data;

import java.util.List;

@Data
public class JiGuangPushBean {

    private PushBean pushBean;
    private List<String> registrationIds;

}
