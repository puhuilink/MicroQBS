/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:39
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:23
 */
package com.phlink.core.web.controller.common;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.phlink.core.web.entity.City;
import com.phlink.core.web.entity.Country;
import com.phlink.core.web.entity.Province;
import com.phlink.core.web.service.CityService;
import com.phlink.core.web.service.CountryService;
import com.phlink.core.web.service.ProvinceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "行政区域接口")
@RequestMapping("/api/common/region")
@RestController
@Transactional
public class RegionController {
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CountryService countryService;

    @ApiOperation(value = "省级行政地区", httpMethod = "GET")
    @GetMapping("/province")
    public List<Province> provinces(@RequestParam(required = false) String name) {
        return this.provinceService.listByCondition(name);
    }

    @ApiOperation(value = "市级行政地区", httpMethod = "GET")
    @GetMapping("/city")
    public List<City> cities(@Valid @RequestParam @NotBlank(message = "{required}") String provinceId) {
        return this.cityService.listByProvinceId(provinceId);
    }

    @ApiOperation(value = "县区级行政地区", httpMethod = "GET")
    @GetMapping("/country")
    public List<Country> countries(@Valid @RequestParam @NotBlank(message = "{required}") String cityId) {
        return this.countryService.listByCityId(cityId);
    }

}
