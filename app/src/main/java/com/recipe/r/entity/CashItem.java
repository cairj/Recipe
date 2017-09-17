package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/22
 * wangxiaoer
 * 功能描述：
 **/
public class CashItem {
    private int status;
    private String info;
    private ArrayList<Cash> data;

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

    public ArrayList<Cash> getData() {
        return data;
    }

    public void setData(ArrayList<Cash> data) {
        this.data = data;
    }

    public class Cash {
        private String send_id;//发送id
        private String send_type;//发送类型
        private String coupons_id;//优惠券id
        private String face_value;//优惠券面值
        private String user_id;//用户id
        private String order_id_from;//获取优惠券的订单id
        private String order_id_to;//使用优惠券的订单id
        private String order_amount;//使用优惠券的最低消费额
        private String coupons_sn;//优惠券序列号
        private String coupons_pw;//优惠券密码
        private String send_time;//发送时间
        private String expire_time;//过期时间
        private String use_time;//使用时间
        private String status;//状态,1,可以使用,2,已使用,3,无效,4,已过期
        private String coupons_name;//优惠券名称

        public String getSend_id() {
            return send_id;
        }

        public void setSend_id(String send_id) {
            this.send_id = send_id;
        }

        public String getSend_type() {
            return send_type;
        }

        public void setSend_type(String send_type) {
            this.send_type = send_type;
        }

        public String getCoupons_id() {
            return coupons_id;
        }

        public void setCoupons_id(String coupons_id) {
            this.coupons_id = coupons_id;
        }

        public String getFace_value() {
            return face_value;
        }

        public void setFace_value(String face_value) {
            this.face_value = face_value;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getOrder_id_from() {
            return order_id_from;
        }

        public void setOrder_id_from(String order_id_from) {
            this.order_id_from = order_id_from;
        }

        public String getOrder_id_to() {
            return order_id_to;
        }

        public void setOrder_id_to(String order_id_to) {
            this.order_id_to = order_id_to;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public String getCoupons_sn() {
            return coupons_sn;
        }

        public void setCoupons_sn(String coupons_sn) {
            this.coupons_sn = coupons_sn;
        }

        public String getCoupons_pw() {
            return coupons_pw;
        }

        public void setCoupons_pw(String coupons_pw) {
            this.coupons_pw = coupons_pw;
        }

        public String getSend_time() {
            return send_time;
        }

        public void setSend_time(String send_time) {
            this.send_time = send_time;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public String getUse_time() {
            return use_time;
        }

        public void setUse_time(String use_time) {
            this.use_time = use_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCoupons_name() {
            return coupons_name;
        }

        public void setCoupons_name(String coupons_name) {
            this.coupons_name = coupons_name;
        }
    }
}
