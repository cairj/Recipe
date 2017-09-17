package com.recipe.r.bean;

/**
 * Created by hht on 2016/9/18.
 * 区
 */
public class District {
    private String name;
    private String zipcode;

    public District() {
        super();
    }

    public District(String name, String zipcode) {
        super();
        this.name = name;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
    }
}
