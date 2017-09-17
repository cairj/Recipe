package com.recipe.r.entity;/**
 * 作者：Administrator on 2017/6/9 18:39
 * 功能:@描述
 */

/**
 * 2017
 * 06
 * 2017/6/9
 * wangxiaoer
 * 功能描述：
 **/
public class HomeItem {
    private String Title;
    private String IImageUrl;
    private String content;
    private String Price;
    private String soldnumber;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIImageUrl() {
        return IImageUrl;
    }

    public void setIImageUrl(String IImageUrl) {
        this.IImageUrl = IImageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSoldnumber() {
        return soldnumber;
    }

    public void setSoldnumber(String soldnumber) {
        this.soldnumber = soldnumber;
    }
}
