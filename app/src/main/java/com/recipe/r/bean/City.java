package com.recipe.r.bean;

import java.util.List;

/**
 * Created by hht on 2016/9/18.
 * 市
 */
public class City {
    private String name;
    private List<District> districtList;

    public City() {
        super();
    }

    public City(String name, List<District> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList
                + "]";
    }
}
