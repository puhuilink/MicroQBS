package com.phlink.core.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchVO implements Serializable {

    private String startDate;

    private String endDate;
}