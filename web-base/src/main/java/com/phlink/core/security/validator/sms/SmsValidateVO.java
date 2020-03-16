package com.phlink.core.security.validator.sms;

import lombok.Data;

@Data
public class SmsValidateVO {
    private String mobile;
    private String code;
}
