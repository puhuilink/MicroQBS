package com.phlink.core.web.base.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchVO implements Serializable {

    private String startDate;

    private String endDate;
}