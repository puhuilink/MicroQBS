package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.system.dao.CityMapper;
import com.phlink.bus.api.system.dao.CountryMapper;
import com.phlink.bus.api.system.dao.ProvinceMapper;
import com.phlink.bus.api.system.domain.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("region")
@Api(tags ="行政区域")
public class RegionController extends BaseController {
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CountryMapper countryMapper;

    @ApiOperation(value = "省级行政地区", notes = "省级行政地区列表", httpMethod = "GET")
    @GetMapping("province")
    public List<Province> provinces() {
        LambdaQueryWrapper<Province> queryWrapper = new LambdaQueryWrapper<>();
        return this.provinceMapper.selectList(queryWrapper);
    }

    @ApiOperation(value = "市级行政地区", notes = "市级行政地区列表", httpMethod = "GET")
    @GetMapping("city")
    public List<City> cities(@Valid @RequestParam @NotBlank(message = "{required}") String provinceId) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getProvinceId, provinceId);
        return this.cityMapper.selectList(queryWrapper);
    }

    @ApiOperation(value = "县区级行政地区", notes = "县区级行政地区列表", httpMethod = "GET")
    @GetMapping("country")
    public List<Country> countries(@Valid @RequestParam @NotBlank(message = "{required}") String cityId) {
        LambdaQueryWrapper<Country> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Country::getCityId, cityId);
        return this.countryMapper.selectList(queryWrapper);
    }

}
