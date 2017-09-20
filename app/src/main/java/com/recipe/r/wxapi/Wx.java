package com.recipe.r.wxapi;

import android.app.Activity;
import android.util.Log;

import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.MD5Util;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/**
 * Created by hht on 2016/11/23.
 * 微信支付
 */

public class Wx {
    private Activity context;
    private MyOkHttp mMyOkhttp;
    private String TAG = "Wx";
    // 微信支付参数
    private PayReq wxReq;
    private IWXAPI wxApi;
    private Map<String, String> wxPrepayId;// 微信预支付订单id
    public static String wxOutTradeNo = "";// 微信支付订单号
    public static String wxPayMoney = "0";// 微信支付支付金额(单位为分，所以需要转换)
    private String wxNotifyUrl = Config.ADVANCEPAY_BACK;// 微信支付成功回访服务器地址
    private String wxBody = "GuXin";// 微信支付支付商品名。必须为英文,否则签名会出错
    //支付结果返回
    private static String PAY_MONEY = "";
    private static String PAY_ORDER = "";
    private static String PAY_TIME = "";
    private static String PAY_TYPE = "";

    public Wx(Activity context) {
        this.context = context;
    }

    /**
     * 调用微信支付页面
     */
    public void sendPayReq(String order_sn, String type) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        if (type.equals("0")) {
            //预支付
            wxNotifyUrl = Config.ADVANCEPAY_BACK;
        } else {
            wxNotifyUrl = Config.PAY_BACK;
        }
        payOrder(order_sn);
    }


    /**
     * 生成签名参数
     *
     * @param prepayId
     */
    private PayReq genPayReq(String prepayId) {
        PayReq req = new PayReq();
        req.appId = ConfigApp.WX_PP_ID;// appid
        req.partnerId = ConfigApp.MCHID;// 商户号
        req.prepayId = prepayId;// 预支付交易会话ID
        req.packageValue = "Sign=WXPay";// 扩展字段
        req.nonceStr = genNonceStr();// 随机字符串
        req.timeStamp = String.valueOf(genTimeStamp());// 时间戳
//        req.timeStamp = String.valueOf(timeStamp / 1000);// 时间戳
        String appSign = "appid=" + req.appId + "&"
                + "noncestr=" + req.nonceStr + "&"
                + "package=" + req.packageValue + "&"
                + "partnerid=" + req.partnerId + "&"
                + "prepayid=" + req.prepayId + "&"
                + "timestamp=" + req.timeStamp + "&"
                + "key=" + ConfigApp.WXMY;
        Log.i("TAG", appSign);
        try {
            appSign = MD5Util.md5Encode(appSign).toUpperCase(Locale.CHINESE);
        } catch (Exception e) {
            // TODO Auto-generated ca
            // tch block
            e.printStackTrace();
        }
        Log.i("TAG", appSign);
        req.sign = appSign;
        return req;
    }

    /**
     * 时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 随机数
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        String md5 = "";
        try {
            md5 = MD5Util.md5Encode(String.valueOf(random.nextInt(10000)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 完成支付
     * *
     *
     * @param order_sn
     */
    private void payOrder(String order_sn) {
        String url = Config.URL + Config.ORDERPREPAY;
        Map<String, String> params = new HashMap<>();
        params.put("order_sn", order_sn);
        params.put("payment_id", "2");
        params.put("device", "android");
        params.put("pay_type", "prepay");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            JSONObject data = response.getJSONObject("data");
                            if (status == 1) {
                                IWXAPI api = WXAPIFactory.createWXAPI(context, ConfigApp.WX_PP_ID);// 获取实例
                                api.registerApp(ConfigApp.WX_PP_ID);
                                api.sendReq(genPayReq(data.getString("prepay_id")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("Failure", "statusCode" + statusCode + "error_msg" + error_msg);
                    }
                });
    }


}
