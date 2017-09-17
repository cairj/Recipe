package com.recipe.r.ui.activity.search;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/21
 * wangxiaoer
 * 功能描述：
 **/
public class GoodsSearch {
    private int status;
    private String info;
    private ArrayList<GoodsSearch.Goods> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<Goods> getData() {
        return data;
    }

    public void setData(ArrayList<Goods> data) {
        this.data = data;
    }

    class Goods {
        private String Title;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }
    }
}
