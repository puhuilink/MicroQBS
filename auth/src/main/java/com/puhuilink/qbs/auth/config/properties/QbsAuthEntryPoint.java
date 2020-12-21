package com.puhuilink.qbs.auth.config.properties;

import lombok.Data;

@Data
public class QbsAuthEntryPoint {
    private String base = "/auth";
    private String usernameLogin = "/auth/login";
    private String mobileLogin = "/auth/login/mobile";
    private String imageLogin = "/auth/login/image";
    private String tokenProtect = "/api/**";
}