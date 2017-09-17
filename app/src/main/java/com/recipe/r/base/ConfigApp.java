package com.recipe.r.base;

import android.os.Environment;

/**
 * Created by hj on 2017/6/11.
 * 轻量级存储字段
 */

public class ConfigApp {
    public static final String USERNAME = "user_name";//用户名
    public static final String AVATER = "headimgurl";//头像
    public static final String USERID = "user_id";//用户ID
    public static final String ISLOGIN = "is_login";//是否登录
    public static final String POSINTS = "my_points";
    public static final String NICKNAME = "my_nickname";//我的昵称
    public static final String GENDER = "my_gender";//我的性别
    public static final String ADDRID = "AddrId";//地址id  默认
    public static final String EARNREC="earn_rec";//推荐获得提成
    public static final String EARNSUB="earn_sub";//分享获得提成
    public static final String TOKEN="token";//缓存token值
    /**
     * 快递日期
     */
    public static final String DELIVERY = "DELIVERY";
    //用户出生日期
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final int REQUEST_CAMERA_CODE = 11;
    public static final int REQUEST_PREVIEW_CODE = 22;
    public static final int MY_WALLET = 1;//支付方式
    public static final int ZHI_FU_BAO = 1;//支付宝
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    public static final String CACHE_PIC_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/recipe/pics/";
    /**
     * 动态申请权限
     */
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_CAMERA = 3;
    /**
     * 拍照
     */
    public static final int TAKEPHOTO = 1011;
    public static final int GALLARY = 1012;
    public static final int SAVEPIC = 1013;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017011905254817";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088521587688549";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC0FZDC67OtiigfgCelIcr/hk2MQ0MJyW1fdP61tTpSY/T5eFv6+BBse1+B/LTIyXKibgxNxvW6i4X1NcqHGAXenqELDbcDv0jlgv0w+ss8As6OKfBn661iU6duOO/Tru06wjQUva5KRJ2ZKMWORA9RnNDx11XWeyLqBgwaxkTYqBWLQmHvMiYPBA+UKw+5riapJxoE/sBfR9YWmgGnEJ+IVIIzhkz1Mb2vpJPJihdudZ1lshqoSKBQOMCrDHKWCnvDGR7CWMM7EtTNEeb9w4xM8Cm0eoHvRSvAXkyHhW59wkAsO8kpvbniSc7drpIDCFrOwDB+4UtY+096RJcfIByDAgMBAAECggEBAJXfNMDkBH8y8b11nIjznMwX68YF3G7yshG1fp2fG9Ch26kbIAG4HYMLEitOMu6/qKXBzH+WS79airvObqULuybRAdn/Kgb2VSadYzY2IpnpBBqQkMEMBRpF4Jv8yqtzc39pTIrOaZTpkfd7VR7XSaB0T/hVa9y6Ug5NxhkqVPWxV0ncsS6fxruIx1nGVXw2L5/tUlwnKat5IcUkQ99DsEZQoNFuJyzKcZd4b3chfaeeKf8sOfk/1gu2FKZ7JUyZrVxCWHLkQdzVrXjJcWYv3fxwRPFepfJf7QuNws34nvZtZ0i0LSOvpK1tguk99o3xFHbIjQEfWa53uWRmR6nInFECgYEA5RH/wEtolJkLS9DM7tNuSOt9kwYWHAUeTRT9amILUILm/v6lEr65YOoFRO7HpGv090DCOBD1P17wtuqD1hbG7Ho4XnQ016UdT1xNF8ULNB65/JWlna3SD5k2xBtVCRwqteT5PPXKxG5EkZtjcfyk3f2pcC/q5DvZyUgIfwqUFGcCgYEAyUFNizxRBR/fwyQYc0nxmJDLyhuUZswEPj6vXrt7C74gloZ4JL/Cufp9wOlrANPK2QRX6zOiRcS8/gZWmNZYTkTQi6MLdoCy9tFSZYqgnNZm6I69M43wCdJzCtdRtP/Blki+5F5lAydQ9vkWZdalkAYTQDFQ3TJu9dtsQoKohYUCgYA51wWg5GmPu+UiS6KzWyepueyHRy4l/RTdmcMzLXZ16dex41KDHS1pzy1tbm1ykt/T3sIrrfx+99YIGccYM8Aeze2Vy/nbyZFRDJlauyJNEsUbhh2/Zrh3v9QYSF58YmRWcW6NefKtDrq5mWbOzWBDvV0fOrowzvv62vlqOp2RWQKBgQCHZoQBNSddR0CITXSVvB3rAx/Ybg29y2zfY9Ug6wmxsMzD+NxD/S/JaBnK/D+dYuCKeT2dZDO8tMxiK2l/klX+8pVMHPnNVHETVWkWH9pHKog51i68zLW+JfR4Sr0SBS/9KBwmEMvs/zqdrp+/DFZ/O1cgTQ2WhCPbDTEc3kL68QKBgGtYSa0xSbVfDaGdXIEhnycT4QK/9Fb9O2ja/idfdLnAXP9xLGhLOd345ubDJTUchOXoUpQct9YoYH8fMBTfGZThVE+xqKcOMOvPdmSrLKKW8J64toQl2KSFvEufcToQRfJBsj/RYLGOW/efvQ0DRz487Z+hRQoQGzA4z3RBMRu/";
    public static final String RSA_PRIVATE = "";
    public static final String PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtBWQwuuzrYooH4AnpSHK/4ZNjENDCcltX3T+tbU6UmP0+Xhb+vgQbHtfgfy0yMlyom4MTcb1uouF9TXKhxgF3p6hCw23A79I5YL9MPrLPALOjinwZ+utYlOnbjjv067tOsI0FL2uSkSdmSjFjkQPUZzQ8ddV1nsi6gYMGsZE2KgVi0Jh7zImDwQPlCsPua4mqScaBP7AX0fWFpoBpxCfiFSCM4ZM9TG9r6STyYoXbnWdZbIaqEigUDjAqwxylgp7wxkewljDOxLUzRHm/cOMTPAptHqB70UrwF5Mh4VufcJALDvJKb254knO3a6SAwhazsAwfuFLWPtPekSXHyAcgwIDAQAB";
    public static final String WX_PP_ID = "wx8ec0ecd005c79108";
    public static final String MCHID = "1482768922";//商户ID
    public static final String WXMY = "ufjaxBOCRmdPjpO2lXlhe9EZa7laAPGj";//密钥
    public static final String HXUSERNAME = "guxinshishang";//环信用户设定
    public static final String HXPWD = "guxin147258";//环信用户密码设定

}
