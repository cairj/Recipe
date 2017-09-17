package com.recipe.r.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageBean implements Serializable{
	private Integer sort;
	private String imageUrl;
	private String  thumbUrl;;
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

}
