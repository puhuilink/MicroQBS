package com.phlink.bus.api.system.domain;

import com.wuwenze.poi.config.Options;

public class SexOptions implements Options {

  @Override
  public String[] get() {
    return new String[]{"男", "女", "保密"};
  }
}