package com.phlink.bus.api.serviceorg.domain;

import com.wuwenze.poi.config.Options;

public class StudentRelationOptions implements Options {

  @Override
  public String[] get() {
    return new String[]{"父子", "父女", "母子", "母女", "祖孙", "其他"};
  }
}