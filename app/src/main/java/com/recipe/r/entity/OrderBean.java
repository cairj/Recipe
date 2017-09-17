package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * 2017/8/1
 * wangxiaoer
 * 功能描述：订单实体类
 **/
public class OrderBean {
    private String info;
    private int status;
    private OrderData data;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public class OrderData {
        private double payment_cost;
        private double final_total;
        private String remark;
        private String consignee;
        private String order_id;
        private long payment_time;
        private int city;
        private String status_name;
        private long order_time;
        private double ship_cost;
        private int province;
        private String order_ip;
        private String people_num;
        private double paid_total;
        private double origin_total;
        private String user_id;
        private String district;
        private String ship_sn;
        private long ship_time;
        private String send_id;
        private String shop_id;
        private int status;
        private long book_time;
        private String ship_way;
        private long refund_time;
        private String table_id;
        private String code;
        private double discount_total;
        private double discount;
        private String ship_id;
        private String address;
        private String consignee_address;
        private String admin_id;
        private String order_sn;
        private String payment_id;
        private ArrayList<GoodsInfo> goods_info;
        private String  telephone;
        private long  confirm_time;
        private String order_type;
        private double  remain_total;
        private String mobile;
        public double getPayment_cost() {
            return payment_cost;
        }

        public void setPayment_cost(double payment_cost) {
            this.payment_cost = payment_cost;
        }

        public double getFinal_total() {
            return final_total;
        }

        public void setFinal_total(double final_total) {
            this.final_total = final_total;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public long getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(long payment_time) {
            this.payment_time = payment_time;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public long getOrder_time() {
            return order_time;
        }

        public void setOrder_time(long order_time) {
            this.order_time = order_time;
        }

        public double getShip_cost() {
            return ship_cost;
        }

        public void setShip_cost(double ship_cost) {
            this.ship_cost = ship_cost;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public String getOrder_ip() {
            return order_ip;
        }

        public void setOrder_ip(String order_ip) {
            this.order_ip = order_ip;
        }

        public String getPeople_num() {
            return people_num;
        }

        public void setPeople_num(String people_num) {
            this.people_num = people_num;
        }

        public double getPaid_total() {
            return paid_total;
        }

        public void setPaid_total(double paid_total) {
            this.paid_total = paid_total;
        }

        public double getOrigin_total() {
            return origin_total;
        }

        public void setOrigin_total(double origin_total) {
            this.origin_total = origin_total;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getShip_sn() {
            return ship_sn;
        }

        public void setShip_sn(String ship_sn) {
            this.ship_sn = ship_sn;
        }

        public long getShip_time() {
            return ship_time;
        }

        public void setShip_time(long ship_time) {
            this.ship_time = ship_time;
        }

        public String getSend_id() {
            return send_id;
        }

        public void setSend_id(String send_id) {
            this.send_id = send_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getBook_time() {
            return book_time;
        }

        public void setBook_time(long book_time) {
            this.book_time = book_time;
        }

        public String getShip_way() {
            return ship_way;
        }

        public void setShip_way(String ship_way) {
            this.ship_way = ship_way;
        }

        public long getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(long refund_time) {
            this.refund_time = refund_time;
        }

        public String getTable_id() {
            return table_id;
        }

        public void setTable_id(String table_id) {
            this.table_id = table_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getDiscount_total() {
            return discount_total;
        }

        public void setDiscount_total(double discount_total) {
            this.discount_total = discount_total;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getShip_id() {
            return ship_id;
        }

        public void setShip_id(String ship_id) {
            this.ship_id = ship_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getConsignee_address() {
            return consignee_address;
        }

        public void setConsignee_address(String consignee_address) {
            this.consignee_address = consignee_address;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public ArrayList<GoodsInfo> getGoods_info() {
            return goods_info;
        }

        public void setGoods_info(ArrayList<GoodsInfo> goods_info) {
            this.goods_info = goods_info;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public long getConfirm_time() {
            return confirm_time;
        }

        public void setConfirm_time(long confirm_time) {
            this.confirm_time = confirm_time;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public double getRemain_total() {
            return remain_total;
        }

        public void setRemain_total(double remain_total) {
            this.remain_total = remain_total;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        /**
         * 购买商品信息
         */
        public class GoodsInfo {
            private String id;
            private String goods_name;
            private double goods_price;
            private String order_id;
            private int goods_number;
            private double deal_price;
            private String goods_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public double getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(double goods_price) {
                this.goods_price = goods_price;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public int getGoods_number() {
                return goods_number;
            }

            public void setGoods_number(int goods_number) {
                this.goods_number = goods_number;
            }

            public double getDeal_price() {
                return deal_price;
            }

            public void setDeal_price(double deal_price) {
                this.deal_price = deal_price;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }
        }
    }
}
