package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/8/20.
 * 确认订单实体类
 */

public class OrderConfirmModel {
    private String info;
    private int status;
    private ConfirmOrder data;

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

    public ConfirmOrder getData() {
        return data;
    }

    public void setData(ConfirmOrder data) {
        this.data = data;
    }

    public class ConfirmOrder {
        private int order_type;
        private int address_id;
        private GoodConfirm goods;
        private OrderAddress address;
        private BookInfo book_info;

        @Override
        public String toString() {
            return "ConfirmOrder{" +
                    "order_type=" + order_type +
                    ", address_id=" + address_id +
                    ", goods=" + goods +
                    ", address=" + address +
                    ", book_info=" + book_info +
                    '}';
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public GoodConfirm getGoods() {
            return goods;
        }

        public void setGoods(GoodConfirm goods) {
            this.goods = goods;
        }

        public OrderAddress getAddress() {
            return address;
        }

        public void setAddress(OrderAddress address) {
            this.address = address;
        }

        public BookInfo getBook_info() {
            return book_info;
        }

        public void setBook_info(BookInfo book_info) {
            this.book_info = book_info;
        }
    }

    /**
     * 订桌类
     */
    public class BookInfo {
        private String table_name;
        private int people_num;
        private String remark;
        private String book_time;

        @Override
        public String toString() {
            return "BookInfo{" +
                    "table_name='" + table_name + '\'' +
                    ", people_num=" + people_num +
                    ", remark='" + remark + '\'' +
                    ", book_time='" + book_time + '\'' +
                    '}';
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public int getPeople_num() {
            return people_num;
        }

        public void setPeople_num(int people_num) {
            this.people_num = people_num;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getBook_time() {
            return book_time;
        }

        public void setBook_time(String book_time) {
            this.book_time = book_time;
        }
    }

    /**
     * 商品类数量
     *
     */
    public class GoodConfirm {
        private ArrayList<OrderGoods> data;
        private int count;
        private double total_amount;

        @Override
        public String toString() {
            return "GoodConfirm{" +
                    "data=" + data +
                    ", count=" + count +
                    ", total_amount=" + total_amount +
                    '}';
        }

        public ArrayList<OrderGoods> getGoods() {
            return data;
        }

        public void setGoods(ArrayList<OrderGoods> goods) {
            this.data = goods;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }
    }
    /**
     * 商品类
     *
     */
    public class OrderGoods {
        private String cart_id;
        private String user_id;
        private String goods_id;
        private int goods_number;
        private long time;
        private int status;
        private double shop_price;
        private String goods_name;
        private String goods_image;

        @Override
        public String toString() {
            return "OrderGoods{" +
                    "cart_id='" + cart_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", goods_id='" + goods_id + '\'' +
                    ", goods_number=" + goods_number +
                    ", time=" + time +
                    ", status=" + status +
                    ", shop_price=" + shop_price +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_image='" + goods_image + '\'' +
                    '}';
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getShop_price() {
            return shop_price;
        }

        public void setShop_price(double shop_price) {
            this.shop_price = shop_price;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }
    }

    public class OrderAddress {
        private String consignee;
        private String full_address;
        private String mobile;
        private String remark;

        @Override
        public String toString() {
            return "OrderAddress{" +
                    "consignee='" + consignee + '\'' +
                    ", full_address='" + full_address + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getFull_address() {
            return full_address;
        }

        public void setFull_address(String full_address) {
            this.full_address = full_address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    @Override
    public String toString() {
        return "OrderConfirmModel{" +
                "info='" + info + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
