package com.recipe.r.bean;

import java.util.List;

/**
 * Created by hht on 2016/9/18.
 * 省
 */
public class Province {
    private String name;
    private List<City> cityList;

    public Province() {
        super();
    }

    public Province(String name, List<City> cityList) {
        super();
        this.name = name;
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
    }
}
