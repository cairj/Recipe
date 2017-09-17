package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/20
 * wangxiaoer
 * 功能描述：我的预订
 **/
public class ReservationItem {
    private int status;
    private String info;
    private ArrayList<OrderInfo> data;

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

    public ArrayList<OrderInfo> getData() {
        return data;
    }

    public void setData(ArrayList<OrderInfo> data) {
        this.data = data;
    }

    public class OrderInfo {
        private String order_id;//订单id
        private String order_sn;//订单号
        private String table_id;//餐桌id
        private String user_id;//用户id
        private String consignee;//收货人
        private String province;//省份
        private String city;//城市
        private String district;//市区
        private String address;//详细地址
        private String code;//邮编
        private String mobile;//手机号
        private String telephone;//座机
        private String remark;//订单备注
        private String ship_id;//快递公司id
        private String ship_sn;//快递编号
        private String ship_way;//快递方式
        private String ship_cost;//快递费用
        private String discount;//折扣
        private String origin_total;//原总价
        private String discount_total;//折扣后总价
        private String final_total;//最终总价
        private String paid_total;//已付款
        private String remain_total;//剩余付款
        private String payment_id;//支付id,1,支付宝,2,微信
        private String payment_cost;//支付手续费
        private String book_time;//预订时间
        private String order_time;//订单时间
        private String confirm_time;//确认时间
        private String payment_time;//支付时间
        private String ship_time;//发货时间
        private String refund_time;//退货时间
        private String admin_id;//操作管理员id
        private String order_ip;//订单ip
        private String order_type;//订单类型,1,餐厅消费,2,菜品外送,3,食材外送
        private String people_num;//消费人数
        private int status;//订单状态,-2,订单无效,-1,订单取消,0,待付款,1,已付款,2,已发货,3,已完成,4,申请退款,5,拒绝退款,6,退款成功
        private String table_image;
        private ShopInfo shop_info;
        private ArrayList<GoodsInfo> goods_info;
        private String table_name;
        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getTable_id() {
            return table_id;
        }

        public void setTable_id(String table_id) {
            this.table_id = table_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getShip_id() {
            return ship_id;
        }

        public void setShip_id(String ship_id) {
            this.ship_id = ship_id;
        }

        public String getShip_sn() {
            return ship_sn;
        }

        public void setShip_sn(String ship_sn) {
            this.ship_sn = ship_sn;
        }

        public String getShip_way() {
            return ship_way;
        }

        public void setShip_way(String ship_way) {
            this.ship_way = ship_way;
        }

        public String getShip_cost() {
            return ship_cost;
        }

        public void setShip_cost(String ship_cost) {
            this.ship_cost = ship_cost;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getOrigin_total() {
            return origin_total;
        }

        public void setOrigin_total(String origin_total) {
            this.origin_total = origin_total;
        }

        public String getDiscount_total() {
            return discount_total;
        }

        public void setDiscount_total(String discount_total) {
            this.discount_total = discount_total;
        }

        public String getFinal_total() {
            return final_total;
        }

        public void setFinal_total(String final_total) {
            this.final_total = final_total;
        }

        public String getPaid_total() {
            return paid_total;
        }

        public void setPaid_total(String paid_total) {
            this.paid_total = paid_total;
        }

        public String getRemain_total() {
            return remain_total;
        }

        public void setRemain_total(String remain_total) {
            this.remain_total = remain_total;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getPayment_cost() {
            return payment_cost;
        }

        public void setPayment_cost(String payment_cost) {
            this.payment_cost = payment_cost;
        }

        public String getBook_time() {
            return book_time;
        }

        public void setBook_time(String book_time) {
            this.book_time = book_time;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getConfirm_time() {
            return confirm_time;
        }

        public void setConfirm_time(String confirm_time) {
            this.confirm_time = confirm_time;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getShip_time() {
            return ship_time;
        }

        public void setShip_time(String ship_time) {
            this.ship_time = ship_time;
        }

        public String getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(String refund_time) {
            this.refund_time = refund_time;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public String getOrder_ip() {
            return order_ip;
        }

        public void setOrder_ip(String order_ip) {
            this.order_ip = order_ip;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getPeople_num() {
            return people_num;
        }

        public void setPeople_num(String people_num) {
            this.people_num = people_num;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTable_image() {
            return table_image;
        }

        public void setTable_image(String table_image) {
            this.table_image = table_image;
        }

        public ShopInfo getShop_info() {
            return shop_info;
        }

        public void setShop_info(ShopInfo shop_info) {
            this.shop_info = shop_info;
        }

        public ArrayList<GoodsInfo> getGoods_info() {
            return goods_info;
        }

        public void setGoods_info(ArrayList<GoodsInfo> goods_info) {
            this.goods_info = goods_info;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }
    }

    public class ShopInfo {
        private String shop_name;
        private String shop_address;
        private String table_name;

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }
    }

    public class GoodsInfo {
        private String order_id;
        private String goods_id;
        private String goods_name;
        private int goods_number;
        private double goods_price;
        private double deal_price;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }

        public double getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(double goods_price) {
            this.goods_price = goods_price;
        }

        public double getDeal_price() {
            return deal_price;
        }

        public void setDeal_price(double deal_price) {
            this.deal_price = deal_price;
        }
    }
}
