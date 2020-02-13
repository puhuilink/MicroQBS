package com.phlink.bus.api.bus.domain.VO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CheckoutSpecialVO {

    private Long id;
    private Long userId;
    private String realname;
    private String mobile;
    private String numberPlate;
    private LocalDate checkDate;
    private LocalDateTime checkTime;
    private String[] imagePath;
    private String[] videoPath;
    private String description;
    private String rolename;

    private String dateStart;
    private String dateEnd;

}
