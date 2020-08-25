package com.puhuilink.qbs.core.base.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNumber = 1;

    private int pageSize = Integer.MAX_VALUE;

    private String sort;

    private String order;
}
