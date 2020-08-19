package com.puhuilink.qbs.graphapi.resolver;

import com.puhuilink.qbs.auth.entity.City;
import com.puhuilink.qbs.auth.entity.Country;
import com.puhuilink.qbs.auth.entity.Province;
import com.puhuilink.qbs.auth.service.CityService;
import com.puhuilink.qbs.auth.service.CountryService;
import com.puhuilink.qbs.auth.service.ProvinceService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionQuery implements GraphQLQueryResolver {
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CountryService countryService;

    public List<Province> provinces() {
        return provinceService.list();
    }

    public List<City> cities(String provinceId) {
        List<City> cities = this.cityService.listByProvinceId(provinceId);
        return cities;
    }

    public List<Country> countries(String cityId) {
        List<Country> countries = this.countryService.listByCityId(cityId);
        return countries;
    }
}