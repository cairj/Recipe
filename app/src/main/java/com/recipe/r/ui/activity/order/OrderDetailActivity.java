package com.recipe.r.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.OrderConfirmModel;
import com.recipe.r.payment.ZhiFuBaoPay;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.address.AdressActivity;
import com.recipe.r.ui.activity.table.QuickTableActivity;
import com.recipe.r.ui.adapter.order.OrderDetailsAdapter;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.PayWayDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NetworkUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.UserIsLogin;
import com.recipe.r.wxapi.Wx;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 2017
 * 07
 * 2017/7/28
 * wangxiaoer
 * 功能描述：订单详情界面
 **/
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private double totalMoney;
    //下面的参数是立即购买过来的立即支付时的参数
    private int PayMode = 0;//2支付宝 1微信
    private PayWayDialog pay_dialog;
    private TextView sure_order_phoneTv;
    private TextView sure_order_silentTv;
    private TextView sure_order_addressTv;
    private TextView delivery_time;
    private TextView remark_order;
    private TextView num_Settlement_tv;
    private TextView totalMoney_Settlement_tv;
    private TextView order_time;
    private RecyclerView sure_order_Rv;
    private OrderDetailsAdapter adapter;
    private TextView Settlement_tv;
    private RadioButton shop_eat;
    private RadioButton out_eat;
    private LinearLayout sure_order_addressLL;
    private RelativeLayout shop_eat_ll;
    private ArrayList<OrderConfirmModel.OrderGoods> shopcarList = null;
    private RadioGroup eat_way;
    private int ORDER_TYPE = 1;
    private String ORDER_SN = "";
    private final int Address_Code = 1002;
    private final int Address_RESULT_CODE = 1003;
    private String Address_Id = "";
    private TextView pay_price_total;
    private TextView pay_price_actually;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        initHead(R.mipmap.reset_back, "返回", "确认订单", 0, "");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        shopcarList = new ArrayList<>();
        sure_order_phoneTv = (TextView) findViewById(R.id.sure_order_phoneTv);
        sure_order_silentTv = (TextView) findViewById(R.id.sure_order_silentTv);
        sure_order_addressTv = (TextView) findViewById(R.id.sure_order_addressTv);
        delivery_time = (TextView) findViewById(R.id.delivery_time);
        remark_order = (TextView) findViewById(R.id.remark_order);
        num_Settlement_tv = (TextView) findViewById(R.id.num_Settlement_tv);
        totalMoney_Settlement_tv = (TextView) findViewById(R.id.totalMoney_Settlement_tv);
        order_time = (TextView) findViewById(R.id.order_time);
        sure_order_Rv = (RecyclerView) findViewById(R.id.sure_order_Rv);
        shop_eat_ll = (RelativeLayout) findViewById(R.id.shop_eat_ll);
        sure_order_addressLL = (LinearLayout) findViewById(R.id.sure_order_addressLL);
        shop_eat = (RadioButton) findViewById(R.id.shop_eat);
        out_eat = (RadioButton) findViewById(R.id.out_eat);
        eat_way = (RadioGroup) findViewById(R.id.eat_way);
        pay_price_actually= (TextView) findViewById(R.id.pay_price_actually);
        pay_price_total= (TextView) findViewById(R.id.pay_price_total);
        sure_order_Rv.setLayoutManager(new LinearLayoutManager(context));
        Settlement_tv = (TextView) findViewById(R.id.Settlement_tv);
    }

    private void initListener() {
        Settlement_tv.setOnClickListener(this);
        eat_way.setOnCheckedChangeListener(this);
        sure_order_silentTv.setOnClickListener(this);
        findViewById(R.id.booking_orderdetails).setOnClickListener(this);
    }


    /**
     * 获取购物车信息
     */
    private void initData() {
        if (NetworkUtils.isAvailable(this)) {
            getOrderInfo();
        } else {
            Toast.makeText(this, getResources().getString(R.string.NetWork_error), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取订单详情接口
     */
    private void getOrderInfo() {
        showProgress();
        String url = Config.URL + Config.ORDERCONFIRM;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
        if (!TextUtils.isEmpty(Address_Id)) {
            //TODO 外卖回调地址
            params.put("address_id", Address_Id);
        }
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<OrderConfirmModel>() {
                    @Override
                    public void onSuccess(int statusCode, OrderConfirmModel response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        Logger.e("onSuccess",response.toString());
                        if (status == 1) {
                            OrderConfirmModel.ConfirmOrder data = response.getData();
                            ORDER_TYPE = data.getOrder_type();
                            if (ORDER_TYPE == 1) {
                                //TODO 店内消费
                                shop_eat.setChecked(true);
                                out_eat.setChecked(false);
                                sure_order_addressLL.setVisibility(View.GONE);
                                shop_eat_ll.setVisibility(View.GONE);
                                Settlement_tv.setEnabled(true);
                                Settlement_tv.setBackgroundColor(getResources().getColor(R.color.main_red));
                            } else if (ORDER_TYPE == 2) {
                                //TODO 外卖
                                shop_eat.setChecked(false);
                                out_eat.setChecked(true);
                                sure_order_addressLL.setVisibility(View.VISIBLE);
                                shop_eat_ll.setVisibility(View.GONE);
                                Settlement_tv.setEnabled(true);
                                Settlement_tv.setBackgroundColor(getResources().getColor(R.color.main_red));
                                //TODO 外卖订单地址信息
//                                if (response.getData().getAddress_id() == 0) {
                                if (response.getData().getAddress() == null) {
                                    //TODO 用户无地址情况下
                                    Intent intent = new Intent(OrderDetailActivity.this, AdressActivity.class);
                                    intent.putExtra("type", "order");
                                    startActivityForResult(intent, Address_Code);
                                    return;
                                }
                                OrderConfirmModel.OrderAddress address_list = response.getData().getAddress();
                                sure_order_phoneTv.setText(address_list.getConsignee() + "电话号" + address_list.getMobile());
                                sure_order_addressTv.setText(address_list.getFull_address());
                                if (!TextUtils.isEmpty(address_list.getRemark())) {
                                    remark_order.setText(address_list.getRemark());
                                }
                            }
                            shopcarList = response.getData().getGoods().getGoods();
                            num_Settlement_tv.setText("" + response.getData().getGoods().getCount());
                            totalMoney_Settlement_tv.setText("￥" + response.getData().getGoods().getTotal_amount());
                            pay_price_actually.setText("￥" + response.getData().getGoods().getTotal_amount());
                            pay_price_total.setText("￥" + response.getData().getGoods().getTotal_amount());
                            adapter = new OrderDetailsAdapter(OrderDetailActivity.this, shopcarList);
                            sure_order_Rv.setAdapter(adapter);
                        } else {
                            ToastUtil.show(OrderDetailActivity.this, info, 100);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        if (!TextUtils.isEmpty(error_msg)) {
                            Logger.e("OrderDetailsActivity", error_msg);
                        }
                    }


                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_order_silentTv:
                Intent intent_address = new Intent(OrderDetailActivity.this, AdressActivity.class);
                startActivity(intent_address);
                break;
            case R.id.Settlement_tv:
                //付款
                if (ORDER_TYPE == 1) {
                    //店内消费
                    if (shop_eat.isChecked()) {
                        //TODO 正常到店吃流程
                        getSubmitOrder();
//                        getBookTableOrder();
                    } else {
                        //TODO 到店吃切换到外卖
                        showOrderDialog();
                    }
                } else if (ORDER_TYPE == 2) {
                    //外卖
                    if (out_eat.isChecked()) {
                        //TODO 正常外卖
                        getSubmitOrder();
//                        getOuterOrder();
                    } else {
                        //TODO 外卖转到店吃
                        ToastUtil.show(context, getString(R.string.subscribe_table), 100);
                        sure_order_addressLL.setVisibility(View.GONE);
                        shop_eat_ll.setVisibility(View.VISIBLE);
                        Settlement_tv.setEnabled(false);
                        Settlement_tv.setBackgroundColor(getResources().getColor(R.color.gray_6c6c6c));
                    }

                }
                break;
            case R.id.booking_orderdetails:
                //预订订桌
                Intent intent = new Intent(OrderDetailActivity.this, QuickTableActivity.class);
                intent.putExtra("mark", "order");
                startActivity(intent);
                break;
        }
    }

    /**
     * 跳转至订单界面，删除订单后在来点菜
     */
    private void showOrderDialog() {
        AlertDialog dialog = new AlertDialog(OrderDetailActivity.this).builder()
                .setTitle(getString(R.string.str_tip))
                .setMsg(getString(R.string.table_to_out))
                .setPositiveButton("确认", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (UserIsLogin.IsLogn(context)) {
                            Intent intent_payment = new Intent(context, PaymentActivity.class);
                            intent_payment.putExtra("type", "0");
                            startActivity(intent_payment);
                        }
                        OrderDetailActivity.this.finish();
                    }
                }).setNegativeButton("返回", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        OrderDetailActivity.this.finish();
                    }
                });
        dialog.show();
        dialog.setCancelable(false);
    }

    /**
     * 初始化支付方式Dialog
     */
    private void initDialog() {
        // 隐藏输入法
        pay_dialog = new PayWayDialog(OrderDetailActivity.this, R.style.recharge_pay_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay_dialog.payWay == 1) {
                    //支付宝支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        ZhiFuBaoPay aliPay = new ZhiFuBaoPay(OrderDetailActivity.this);
                        aliPay.payAliBaba(1, "0", ORDER_SN);
                    } else {
                        ToastUtil.show(OrderDetailActivity.this, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                } else {
                    //微信支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        Wx weixin_pay = new Wx(OrderDetailActivity.this);
                        weixin_pay.sendPayReq(ORDER_SN, "0");
                    } else {
                        ToastUtil.show(OrderDetailActivity.this, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                }

            }
        });
        pay_dialog.setPrice(totalMoney_Settlement_tv.getText().toString());
        pay_dialog.show();
    }


    /**
     * 结算外卖订单
     */
    private void getSubmitOrder() {
        showProgress();
        String url = Config.URL + Config.ORDERSUBMIT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
        params.put("order_type", "" + ORDER_TYPE);
        params.put("address_id", Address_Id);
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
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            if (status == 1) {
                                Intent intent = new Intent(OrderDetailActivity.this, PaymentActivity.class);
                                startActivity(intent);
                                OrderDetailActivity.this.finish();
                            } else {
                                //TODO 错误信息
                                ToastUtil.show(OrderDetailActivity.this, info, 100);
                                OrderDetailActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        if (!TextUtils.isEmpty(error_msg)) {
                            Logger.e("LoginActivity", error_msg);
                        }
                    }
                });

    }

//    /**
//     * 结算外卖订单
//     */
//    private void getOuterOrder() {
//        showProgress();
//        String url = Config.URL + Config.OUTERGOODS;
//        Map<String, String> params = new HashMap<>();
//        ArrayList<Map<String, String>> menu_list = new ArrayList<>();
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
//                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//                .setPrettyPrinting()
//                .setVersion(1.0)
//                .create();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
//        params.put("device", "andriod");
//        for (int i = 0; i < shopcarList.size(); i++) {
//            Map<String, String> menu_map = new HashMap<>();
//            menu_map.put("goods_id", shopcarList.get(i).getGoods_id());
//            menu_map.put("num", "" + shopcarList.get(i).getGoods_number());
//            menu_list.add(menu_map);
//        }
//        params.put("address_id", AppSettings.getPrefString(context, ConfigApp.ADDRID, ""));
//        String jsonString = gson.toJson(menu_list);
//        try {
//            params.put("goods_menu", URLEncoder.encode(jsonString, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            ToastUtil.show(OrderDetailActivity.this, "订单信息有误", 100);
//        }
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        hideProgress();
//                        try {
//                            int status = response.getInt("status");
//                            String info = response.getString("info");
//                            ToastUtil.show(OrderDetailActivity.this, info, 500);
//                            if (status == 1) {
//                                JSONObject data = response.getJSONObject("data");
//                                initDialog();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        hideProgress();
//                        if (!TextUtils.isEmpty(error_msg)) {
//                            Logger.e("LoginActivity", error_msg);
//                        }
//                    }
//                });
//
//    }


//    /**
//     * 结算店内吃
//     */
//    private void getBookTableOrder() {
//        showProgress();
//        String url = Config.URL + Config.BOOKTABLEGOODS;
//        Map<String, String> params = new HashMap<>();
//        ArrayList<Map<String, String>> menu_list = new ArrayList<>();
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
//                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//                .setPrettyPrinting()
//                .setVersion(1.0)
//                .create();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
//        for (int i = 0; i < shopcarList.size(); i++) {
//            Map<String, String> menu_map = new HashMap<>();
//            menu_map.put("goods_id", shopcarList.get(i).getGoods_id());
//            menu_map.put("num", "" + shopcarList.get(i).getGoods_number());
//            menu_list.add(menu_map);
//        }
//        params.put("order_sn", ORDER_SN);
//        String jsonString = gson.toJson(menu_list);
//        try {
//            params.put("goods_menu", URLEncoder.encode(jsonString, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            ToastUtil.show(OrderDetailActivity.this, "订单信息有误", 100);
//        }
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        hideProgress();
//                        try {
//                            int status = response.getInt("status");
//                            String info = response.getString("info");
//                            ToastUtil.show(OrderDetailActivity.this, info, 500);
//                            if (status == 1) {
//                                JSONObject data = response.getJSONObject("data");
//                                initDialog();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        hideProgress();
//                        if (!TextUtils.isEmpty(error_msg)) {
//                            Logger.e("LoginActivity", error_msg);
//                        }
//                    }
//                });
//
//    }
//

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.shop_eat:
                sure_order_addressLL.setVisibility(View.GONE);
                if (ORDER_TYPE == 1) {
                    shop_eat_ll.setVisibility(View.GONE);
                } else if (ORDER_TYPE == 2) {
                    shop_eat_ll.setVisibility(View.VISIBLE);
                    Settlement_tv.setEnabled(false);
                    Settlement_tv.setBackgroundColor(getResources().getColor(R.color.gray_6c6c6c));
                }
                Settlement_tv.setClickable(false);
                Settlement_tv.setBackgroundColor(getResources().getColor(R.color.gray_6c6c6c));
                break;
            case R.id.out_eat:
                sure_order_addressLL.setVisibility(View.VISIBLE);
                shop_eat_ll.setVisibility(View.GONE);
                Settlement_tv.setClickable(true);
                Settlement_tv.setBackgroundColor(getResources().getColor(R.color.main_red));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Address_Code:
                // TODO
                if (resultCode == Address_RESULT_CODE) {
                    //地址id
                    Address_Id = data.getStringExtra("address_id");
                    initData();
                }
                break;
            default:
                break;
        }
    }
}
