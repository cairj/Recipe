package com.recipe.r.ui.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.OrderGoods;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.order.OrderInfoAdapter;
import com.recipe.r.ui.widget.CustomDigitalClock;
import com.recipe.r.ui.widget.ListViewForScrollView;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单信息界面
 */
public class OrderInfoActivity extends BaseActivity {
    private String ORDERID = "";
    private TextView consignee_orderinfo;
    private TextView table_orderinfo;
    private TextView people_num_orderinfo;
    private TextView time_orderinfo;
    private TextView total_price_orderinfo;
    private TextView pay_price_orderinfo;
    private ListViewForScrollView shop_lv_orderinfo;
    private CustomDigitalClock remainTime_order_tv;
    private LinearLayout table_info_order;
    private ImageView isfinish_order;
    private LinearLayout outorder_info_order;
    private TextView consignee_outorder;
    private TextView address_outorder;
    private TextView address_status_outorder;
    private TextView time_outorder;
    private OrderInfoAdapter adapter;
    private ArrayList<OrderGoods> shopcarList = null;
    private TextView status_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        if (getIntent().getExtras() != null) {
            ORDERID = getIntent().getStringExtra("orderId");
        }
        initHead(R.mipmap.reset_back, "返回", "预订订单详情", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        shopcarList = new ArrayList<>();
        status_order= (TextView) findViewById(R.id.status_order);
        consignee_orderinfo = (TextView) findViewById(R.id.consignee_orderinfo);
        table_orderinfo = (TextView) findViewById(R.id.table_orderinfo);
        people_num_orderinfo = (TextView) findViewById(R.id.people_num_orderinfo);
        time_orderinfo = (TextView) findViewById(R.id.time_orderinfo);
        time_outorder = (TextView) findViewById(R.id.time_outorder);
        address_outorder = (TextView) findViewById(R.id.address_outorder);
        total_price_orderinfo = (TextView) findViewById(R.id.total_price_orderinfo);
        pay_price_orderinfo = (TextView) findViewById(R.id.pay_price_orderinfo);
        table_info_order = (LinearLayout) findViewById(R.id.table_info_order);
        isfinish_order = (ImageView) findViewById(R.id.isfinish_order);
        consignee_outorder = (TextView) findViewById(R.id.consignee_outorder);
        address_status_outorder = (TextView) findViewById(R.id.address_status_outorder);
        outorder_info_order = (LinearLayout) findViewById(R.id.outorder_info_order);
        shop_lv_orderinfo = (ListViewForScrollView) findViewById(R.id.shop_lv_orderinfo);
        remainTime_order_tv = (CustomDigitalClock) findViewById(R.id.remainTime_order_tv);
    }

    //TODO 获取订单详情界面展示
    private void initData() {
        getOrderInfo();
    }

    private void initListener() {

    }


    /**
     * 获取订单信息
     */
    private void getOrderInfo() {
        showProgress();
        String url = Config.URL + Config.GETORDERINFO;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("order_id", ORDERID);
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
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                status_order.setText(data.getString("status_name"));
                                remainTime_order_tv.setEndTime(data.getLong("confirm_time")+1200);//TODO 订单结束时间
                                //TODO 订单类型,1,菜品消费,2,菜品外送,3,食材外送
                                if (data.getInt("order_type") == 2) {
                                    //外卖订单
                                    outorder_info_order.setVisibility(View.VISIBLE);
                                    table_info_order.setVisibility(View.GONE);
                                    isfinish_order.setVisibility(View.VISIBLE);
                                    isfinish_order.setBackgroundResource(R.mipmap.out_order);
                                    consignee_outorder.setText(data.getString("consignee"));
                                    address_outorder.setText(data.getString("address"));
                                    time_outorder.setText(DateUtil.getDateToString2(Long.parseLong(data.getString("order_time"))));
                                    //TODO 订单状态,-3,已删除,-2,订单无效,-1,订单取消,0,待付款,1,已付款,2,已发货,3,已完成,4,申请退款,5,拒绝退款,6,退款成功
                                    if (data.getInt("status") == -3) {
                                        address_status_outorder.setText(R.string.order_delete);
                                    } else if (data.getInt("status") == -2) {
                                        address_status_outorder.setText(R.string.order_no);
                                    } else if (data.getInt("status") == -1) {
                                        address_status_outorder.setText(R.string.order_cancel);
                                    } else if (data.getInt("status") == 0) {
                                        address_status_outorder.setText(R.string.wait_order);
                                    } else if (data.getInt("status") == 1) {
                                        address_status_outorder.setText(R.string.finish_order);
                                    } else if (data.getInt("status") == 2) {
                                        address_status_outorder.setText(R.string.order_fahuo);
                                    } else if (data.getInt("status") == 3) {
                                        address_status_outorder.setText(R.string.order_finish);
                                    } else if (data.getInt("status") == 4) {
                                        address_status_outorder.setText(R.string.order_please);
                                    } else if (data.getInt("status") == 5) {
                                        address_status_outorder.setText(R.string.order_refuse);
                                    } else if (data.getInt("status") == 6) {
                                        address_status_outorder.setText(R.string.order_success);
                                    }
                                } else {
                                    table_info_order.setVisibility(View.VISIBLE);
                                    outorder_info_order.setVisibility(View.GONE);
                                    isfinish_order.setVisibility(View.VISIBLE);
                                    isfinish_order.setBackgroundResource(R.mipmap.order_sure);
                                    JSONObject shop_info = data.getJSONObject("shop_info");
                                    table_orderinfo.setText(shop_info.getString("table_name") + "  桌号:" + data.getString("table_id"));
                                    people_num_orderinfo.setText(data.getString("people_num") + "人");
                                    time_orderinfo.setText(DateUtil.getDateToString2(Long.parseLong(data.getString("book_time"))));
                                    consignee_orderinfo.setText(data.getString("consignee"));
                                }
                                JSONArray goods_info = data.getJSONArray("goods_info");
                                shopcarList.clear();
                                for (int i = 0; i < goods_info.length(); i++) {
                                    OrderGoods orderGoods = new OrderGoods();
                                    JSONObject object = goods_info.getJSONObject(i);
                                    orderGoods.setId(object.getString("id"));
                                    orderGoods.setOrder_id(object.getString("order_id"));
                                    orderGoods.setGoods_id(object.getString("goods_id"));
                                    orderGoods.setGoods_name(object.getString("goods_name"));
                                    orderGoods.setGoods_number(object.getInt("goods_number"));
                                    orderGoods.setGoods_price(object.getDouble("goods_price"));
                                    orderGoods.setShop_price(object.getDouble("deal_price"));
                                    orderGoods.setTime(object.getLong("time"));
                                    orderGoods.setGoods_image(object.getString("goods_image"));
                                    shopcarList.add(orderGoods);
                                }
                                adapter = new OrderInfoAdapter(OrderInfoActivity.this, shopcarList);
                                shop_lv_orderinfo.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("OrderInfoActivity", error_msg);
                    }
                });
    }

}
