package com.puhuilink.qbs.core.base.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6123930305866632922L;

    private String startDate;

    private String endDate;
}
