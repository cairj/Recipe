package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/23.
 */
public class TableItem {
    private String info;
    private int status;
    private ArrayList<Table> data;

    public ArrayList<Table> getData() {
        return data;
    }

    public void setData(ArrayList<Table> data) {
        this.data = data;
    }

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

    public class Table {
        private String time;
        private String shop_id;
        private int status;
        private String table_name;
        private String capacity;
        private String table_id;
        private String table_type;
        private String table_image;
        private String shop_name;

        @Override
        public String toString() {
            return "Table{" +
                    "time='" + time + '\'' +
                    ", shop_id='" + shop_id + '\'' +
                    ", status=" + status +
                    ", table_name='" + table_name + '\'' +
                    ", capacity='" + capacity + '\'' +
                    ", table_id='" + table_id + '\'' +
                    ", table_type='" + table_type + '\'' +
                    ", table_image='" + table_image + '\'' +
                    ", shop_name='" + shop_name + '\'' +
                    '}';
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public String getCapacity() {
            return capacity;
        }

        public void setCapacity(String capacity) {
            this.capacity = capacity;
        }

        public String getTable_id() {
            return table_id;
        }

        public void setTable_id(String table_id) {
            this.table_id = table_id;
        }

        public String getTable_type() {
            return table_type;
        }

        public void setTable_type(String table_type) {
            this.table_type = table_type;
        }

        public String getTable_image() {
            return table_image;
        }

        public void setTable_image(String table_image) {
            this.table_image = table_image;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }
    }

    @Override
    public String toString() {
        return "TableItem{" +
                "info='" + info + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
