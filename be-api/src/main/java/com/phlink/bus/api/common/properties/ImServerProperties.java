package com.phlink.bus.api.common.properties;

import lombok.Data;

@Data
public class ImServerProperties {

    private String url;
    private String username;
    private String password;
//    private String companyId;
    private String busOrgId;
    private String routeOrgId;
    private String guardianOrgId;
    private String teacherOrgId;
    private String cron;

}
