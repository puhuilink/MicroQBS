package com.phlink.bus.api.bus.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DvrRtmpRequest {
    private Integer channel;
    private Integer dataType;
    private String simcard;
    private Integer streamType;
    private String type;
}
