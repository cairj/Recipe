package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/14.
 * 积分实体类
 */

public class Integral {
    private int status;
    private String info;
    private ArrayList<IntegralData> data;

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

    public ArrayList<IntegralData> getData() {
        return data;
    }

    public void setData(ArrayList<IntegralData> data) {
        this.data = data;
    }

    public class IntegralData {
        private String goods_name;
        private String point_id;//记录id
        private String user_id;//用户id
        private String value;//变动值
        private String point;//最新积分
        private String remark;//备注
        private String type;//账变类型,1,获取,2,兑换
        private String time;//时间
        private String status;//状态,-1,兑换无效,0,兑换申请中,1,正常
        private String goods_image;
        private String goods_id;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getPoint_id() {
            return point_id;
        }

        public void setPoint_id(String point_id) {
            this.point_id = point_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }
    }
}
