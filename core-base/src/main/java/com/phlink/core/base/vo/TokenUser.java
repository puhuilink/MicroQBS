package com.phlink.core.base.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4234898740994855314L;

    private String username;

    private List<String> permissions;

    private Boolean saveLogin;
}
