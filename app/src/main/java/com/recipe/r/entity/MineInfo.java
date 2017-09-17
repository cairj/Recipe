package com.recipe.r.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017/7/14
 * wangxiaoer
 * 功能描述：个人中心
 **/
public class MineInfo {
    private int status;
    private String info;
    private ArrayList<Data> data;

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {
        private String memlevel;
        private String birthday;
        private String status;
        private String reg_time;
        private String sub_num;
        private String type;
        private String login_ip;
        private String reg_ip;
        private String reg_way;
        private String point;
        private String balance;
        private String user_name;
        private String earn_total;
        private String login_time;
        private String email;
        private String rec_num;
        private String rec;
        private List<Chart> chart;

        private String gender;
        private String user_id;//用户id
        private String is_login;
        private String headimgurl;
        private String mobile;
        private String parent_id;

        public String getMemlevel() {
            return memlevel;
        }

        public void setMemlevel(String memlevel) {
            this.memlevel = memlevel;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getSub_num() {
            return sub_num;
        }

        public void setSub_num(String sub_num) {
            this.sub_num = sub_num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLogin_ip() {
            return login_ip;
        }

        public void setLogin_ip(String login_ip) {
            this.login_ip = login_ip;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getReg_way() {
            return reg_way;
        }

        public void setReg_way(String reg_way) {
            this.reg_way = reg_way;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getEarn_total() {
            return earn_total;
        }

        public void setEarn_total(String earn_total) {
            this.earn_total = earn_total;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRec_num() {
            return rec_num;
        }

        public void setRec_num(String rec_num) {
            this.rec_num = rec_num;
        }

        public String getRec() {
            return rec;
        }

        public void setRec(String rec) {
            this.rec = rec;
        }

        public List<Chart> getChart() {
            return chart;
        }

        public void setChart(List<Chart> chart) {
            this.chart = chart;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getIs_login() {
            return is_login;
        }

        public void setIs_login(String is_login) {
            this.is_login = is_login;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }

    public class Chart {
        private List<Agent> agent;
        private List<Lucky> lucky;

        public List<Agent> getAgent() {
            return agent;
        }

        public void setAgent(List<Agent> agent) {
            this.agent = agent;
        }

        public List<Lucky> getLucky() {
            return lucky;
        }

        public void setLucky(List<Lucky> lucky) {
            this.lucky = lucky;
        }
    }

    public class Agent {
        private String user_name;
        private String user_id;//用户id
        private String earn_total;
        private String headimgurl;
        private String user_num;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getEarn_total() {
            return earn_total;
        }

        public void setEarn_total(String earn_total) {
            this.earn_total = earn_total;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUser_num() {
            return user_num;
        }

        public void setUser_num(String user_num) {
            this.user_num = user_num;
        }
    }


    public class Lucky {
        private String user_name;
        private String user_id;//用户id
        private String lucky_total;
        private String headimgurl;
        private String lucky_num;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getLucky_total() {
            return lucky_total;
        }

        public void setLucky_total(String lucky_total) {
            this.lucky_total = lucky_total;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getLucky_num() {
            return lucky_num;
        }

        public void setLucky_num(String lucky_num) {
            this.lucky_num = lucky_num;
        }
    }


}
