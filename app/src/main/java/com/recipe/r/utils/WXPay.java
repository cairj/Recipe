package com.recipe.r.utils;

import android.util.Log;
import android.util.Xml;

import com.recipe.r.base.ConfigApp;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @功能：微信支付需要的方法类
 * @作者：yux
 * @时间：2015年6月30日 上午10:00:54
 */
public class WXPay {

    /**
     * 生成微信支付相关参数
     *
     * @param wxOutTradeNo 微信支付订单号
     * @param wxPayMoney   微信支付金额(微信支付默认金额以"分"为单位，需要转化为"元")
     * @param notifyUrl    微信支付成功回调服务器地址
     */
    public static String genProductArgs(String wxOutTradeNo, String wxBody,
                                        String wxPayMoney, String notifyUrl) {
        StringBuffer xml = new StringBuffer();
        try {
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", ConfigApp.WX_PP_ID));
            packageParams.add(new BasicNameValuePair("body", wxBody));
            packageParams.add(new BasicNameValuePair("mch_id",
                    ConfigApp.MCHID));
            packageParams
                    .add(new BasicNameValuePair("nonce_str", genNonceStr()));
            packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
            packageParams.add(new BasicNameValuePair("out_trade_no",
                    wxOutTradeNo));
            packageParams.add(new BasicNameValuePair("spbill_create_ip",
                    "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", Integer
                    .valueOf(wxPayMoney) * 100 + ""));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);
            return xmlstring;
        } catch (Exception e) {
            Log.e("WXPay", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

	/*
     * public static String genOutTradNo() { Random random = new Random();
	 * return
	 * MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()); }
	 */

    public static String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(ConfigApp.WXMY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion赋值", packageSign);
        return packageSign;
    }

    public static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e("orion转换", sb.toString());
        return sb.toString();
    }

    /**
     * 解析xml返回值；
     */
    public static Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Log.e("orion解析", e.toString());
        }
        return null;
    }

    /**
     * 生成PayRequest
     */
    public static PayReq genPayReq(Map<String, String> wxPrepayId) {
        PayReq wxReq = new PayReq();
        wxReq.appId = ConfigApp.WX_PP_ID;
        wxReq.partnerId = ConfigApp.MCHID;
        wxReq.prepayId = wxPrepayId.get("prepay_id");
        wxReq.packageValue = "Sign=WXPay";

        wxReq.nonceStr = WXPay.genNonceStr();
        wxReq.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", wxReq.appId));
        signParams.add(new BasicNameValuePair("noncestr", wxReq.nonceStr));
        signParams.add(new BasicNameValuePair("package", wxReq.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", wxReq.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", wxReq.prepayId));
//		signParams.add(new BasicNameValuePair("pay_sign_key", WXConstant.API_KEY));
        signParams.add(new BasicNameValuePair("timestamp", wxReq.timeStamp));
        wxReq.sign = genAppSign(signParams);
        Log.e("WXPay---", signParams.toString());

        return wxReq;
    }

    // 生成签名参数
    public static String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(ConfigApp.WXMY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        return appSign;
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}
