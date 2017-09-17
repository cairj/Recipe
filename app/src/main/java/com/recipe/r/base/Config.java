package com.recipe.r.base;

/**
 * Created by hj on 2017/6/10.
 * 接口类
 */

public class Config {
    /**
     * 全局变量
     */
    public static final String Bugly_ID = "a81ee1990e";
    /**
     * 基本接口
     */
    public static String WXURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//微信支付预订单URL

    public static String URL = "http://v20.api.gussin.cn";//正式接口
    //    public static String IMAGE_URL = "http://www.gussin.cn/";//正式发布
//    public static String URL = "http://testapi.gussin.cn";//测试接口
    public static String IMAGE_URL = "";//测试接口发布  public static String IMAGE_URL = "http://test.gussin.cn/";
    public static String AGREEMENT = "http://test.gussin.cn/pad/agreement";//用户协议
    public static String ABOUT_URL = "http://test.gussin.cn";//关于我们
    public static String REGISTER = "/Members/registerHandle";//注册
    public static String LOGIN = "/Members/loginHandle";//登录
    public static String CODE = "/Common/getCode";//获取验证码
    public static String RESETPWD = "/Members/resetPwd";//重置密码
    public static String MEMBERS = "/Members";//获取个人中心数据
    public static String GETREC = "/Recommend/getRec";//获取推荐
    public static String GETBANNER = "/Common/getBanner";//获取Banner
    public static String GETLISTS = "/Goods/getLists";//获取产品列表
    public static String GETGOODSDETAILS = "/Goods/getDetail";//获取产品详情
    public static String GETUSERADRESS = "/Order/getUserAddress";//获取用户地址
    public static String ADDUSERADDRESS = "/Order/editUserAddress";//添加新的地址
    public static String SETUSERADDRESSTODEFAULT = "/Order/setUserAddress";//设置默认地址
    public static String PRAISE = "/Goods/digg";//产品点赞
    public static String SETORDERSTATUS = "/Order/setOrderStatus";//删除订单
    public static String COLLECT = "/Goods/collect";//收藏产品
    public static String GETSHARELIST = "/News/getLists";//获取资讯
    public static String GETINFORMATIONDETAILS = "/News/getDetail";//获取资讯详情
    public static String INFORMATIONPRAISE = "/News/digg";//资讯点赞
    public static String BOOKTABLEINFO = "/Order/bookTableInfo";//预订桌子
    public static String APPPREPAYTODB = "/Pay/appPrepayToDb";//订桌预付
    public static String UPDATEACATAR = "/Members/updateAvatar";//更新头像
    public static String GETORDERINFO = "/Order/getOrderInfo";//获取订单信息
    public static String UPLOADIMAGE = "/Upload";//上传图片
    public static String GETTABLES = "/Order/getTables";//获取餐桌信息
    //    public static String GETORDERS = "/Members/myOrders";//我的订单
//    public static String GETBOOKS = "/Members/myBooks";//我的预订
    public static String GETSHOPCAR = "/Goods/getCart";//获取购物车
    public static String MYRECOMMENDS = "/Members/myRecommends";//我的推荐
    public static String MYCOLLEXTS = "/Members/myCollects";//我的收藏
    public static String MYSHARE = "/Members/myShares";//我的分享接口
    public static String ADDSHARE = "/News/addShare";//添加分享接口
    public static String SEARCHGOODS = "/Search/goods";//搜索商品
    public static String MYLUCKYS = "/Members/myLuckys";//中奖纪录
    public static String ADDCOMMENT = "/Comment/addComment";//添加评论
    public static String GETCOMMENTLISTS = "/Comment/getLists";//评论列表
    public static String SETUP = "/Members/setup";//用户信息修改
    public static String ADDCART = "/Goods/addCart";//添加购物车
    public static String GETSERVICELISTS = "/Common/getServiceLists";//获取客服列表
    public static String GETPOINTRECORD = "/Members/getPointRecord";//获取用户积分
    public static String GOODSIMPORT = "/Goods/goodsImport";//产品推荐
    public static String MYCOUPONS = "/Members/myCoupons";//我的现金卷
    public static String ORDERPREPAY = "/Pay/orderPay";//预支付接口
    public static String OUTERGOODS = "/Order/outerGoods";//点菜外卖
    public static String GETORDERGOODS = "/Order/getOrderGoods";//获取订单产品
    public static String GETSHAREINFO = "/Common/getShareInfo";//获取分享发布信息
    //    public static String GETORDERTYPE = "/Order/getOrderType";//获取订单类型
    public static String ORDERCONFIRM = "/Auto/orderConfirm";//购物车下单-订单确认(APP端)
    public static String ADVANCEPAY_BACK = " http://api.gussin.cn/pay/notifyPrepayWx";//微信成功支付回访地址
    public static String PAY_BACK = "http://api.gussin.cn/pay/notifyFinalpayWx";
    public static String BOOKTABLEGOODS = "/Order/bookTableGoods";//预订桌子-点菜添加
    public static String CHECKLUCKY = "/Mall/checkLucky";//查询是否可以抽奖
    public static String GETLUCKY = "/Mall/getLucky";//正式抽奖
    public static String APPLYLUCKY = "/Mall/applyLucky";//领奖
    public static String GETINTEGRALLISTS = "/Exchange/getLists";//获取兑换产品列表
    public static String POINTEXCHANGE = "/Exchange/pointExchange";//积分兑换
    public static String DELETESHOP = "/Goods/updateCart";//删除商品
    public static String ORDERREC = "/Order/orderRec";//获取消费推荐
    public static String GETORDERLISTS = "/Order/getOrderLists";//订单列表
    public static String ADDREPLY = "/Comment/addReply";//评论的回复
    public static String ABOUT_GUXIN = "/pad/about";//关于古辛食尚(APP端)
    public static String GETBOOKTIMES = "/Order/getBookTimes";//可选订桌时间
    public static String GETVERSION = "/common/getVersion";//获取版本号
    public static String TAKEMONEY = "/Members/takeMoney";//用户提现
    public static String ORDERSUBMIT = "/Auto/orderSubmit";//购物车下单-订单提交(APP端)
    public static String PAYCONFIRM = "/Auto/payConfirm";//订单支付-支付确认(APP端)
}
