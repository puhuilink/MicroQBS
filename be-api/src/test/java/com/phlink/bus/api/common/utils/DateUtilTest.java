package com.phlink.bus.api.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = ApiApplication.class)
//@ActiveProfiles("test")
public class DateUtilTest {

    @Test
    public void testDefineWeekEndToday() {
        boolean isWeekend = DateUtil.defineWeekEndToday();

        LocalDate today = LocalDate.of(2020, 1, 12);
        DayOfWeek week = today.getDayOfWeek();
        if (DayOfWeek.SATURDAY.equals(week) || DayOfWeek.SUNDAY.equals(week) ) {
            log.info("周末++++{}", week);
        }else{
            log.info("周末==={}", week);
        }

    }
}