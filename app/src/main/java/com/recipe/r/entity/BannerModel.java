package com.recipe.r.entity;

/**
 * 作者：Administrator on 2017/6/9 17:46
 * 功能:@描述
 * 首页Banner数据
 */

public class BannerModel {
    private String Tips;
    private String ImageUrl;
    private String href;

    public String getTips() {
        return Tips;
    }

    public void setTips(String tips) {
        Tips = tips;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
