package com.phlink.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgLog {

    private Integer id;
    private String content;
    private Long timestamp;

}
