package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/11.
 * 菜单实体类
 */

public class MenuItem {
    private int status;
    private String info;
    private ArrayList<MenuInfo> data;

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

    public ArrayList<MenuInfo> getData() {
        return data;
    }

    public void setData(ArrayList<MenuInfo> data) {
        this.data = data;
    }

    public class MenuInfo {
        private String goods_id;//产品id
        private String parent_id;//父类id
        private String brand_id;//品牌id
        private String goods_name;//产品名称
        private String alias_name;//别名
        private String goods_sn;//产品编号
        private String goods_image;//产品封面图
        private String shop_price;//售价
        private String market_price;//市场价
        private String promote_price;//促销价
        private String seckill_price;//秒杀价
        private String start_time;//促销起始时间
        private String end_time;//促销结束时间
        private String shop_id;//餐厅id
        private String user_id;//推荐用户
        private String cate_id;//分类id
        private String taste_id;//口味id
        private String view_num;//浏览数
        private String digg_num;//喜欢数
        private String collect_num;//收藏数
        private int sold_num;//总销量
        private String sold_month;//月销量
        private int status;//状态
        private String add_time;//添加时间
        private String make_time;//制作时长(分钟)

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getAlias_name() {
            return alias_name;
        }

        public void setAlias_name(String alias_name) {
            this.alias_name = alias_name;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getPromote_price() {
            return promote_price;
        }

        public void setPromote_price(String promote_price) {
            this.promote_price = promote_price;
        }

        public String getSeckill_price() {
            return seckill_price;
        }

        public void setSeckill_price(String seckill_price) {
            this.seckill_price = seckill_price;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCate_id() {
            return cate_id;
        }

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public String getTaste_id() {
            return taste_id;
        }

        public void setTaste_id(String taste_id) {
            this.taste_id = taste_id;
        }

        public String getView_num() {
            return view_num;
        }

        public void setView_num(String view_num) {
            this.view_num = view_num;
        }

        public String getDigg_num() {
            return digg_num;
        }

        public void setDigg_num(String digg_num) {
            this.digg_num = digg_num;
        }

        public String getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(String collect_num) {
            this.collect_num = collect_num;
        }

        public int getSold_num() {
            return sold_num;
        }

        public void setSold_num(int sold_num) {
            this.sold_num = sold_num;
        }

        public String getSold_month() {
            return sold_month;
        }

        public void setSold_month(String sold_month) {
            this.sold_month = sold_month;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getMake_time() {
            return make_time;
        }

        public void setMake_time(String make_time) {
            this.make_time = make_time;
        }
    }
}
