/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:39
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:23
 */
package com.puhuilink.qbs.web.controller.common;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.web.entity.City;
import com.puhuilink.qbs.web.entity.Country;
import com.puhuilink.qbs.web.entity.Province;
import com.puhuilink.qbs.web.service.CityService;
import com.puhuilink.qbs.web.service.CountryService;
import com.puhuilink.qbs.web.service.ProvinceService;

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
    public Result provinces(@RequestParam(required = false) String name) {
        List<Province> provinces = this.provinceService.listByCondition(name);
        return Result.ok().data(provinces);
    }

    @ApiOperation(value = "市级行政地区", httpMethod = "GET")
    @GetMapping("/city")
    public Result cities(@Valid @RequestParam @NotBlank(message = "{required}") String provinceId) {
        List<City> cities = this.cityService.listByProvinceId(provinceId);
        return Result.ok().data(cities);
    }

    @ApiOperation(value = "县区级行政地区", httpMethod = "GET")
    @GetMapping("/country")
    public Result countries(@Valid @RequestParam @NotBlank(message = "{required}") String cityId) {
        List<Country> countries = this.countryService.listByCityId(cityId);
        return Result.ok().data(countries);
    }

}
