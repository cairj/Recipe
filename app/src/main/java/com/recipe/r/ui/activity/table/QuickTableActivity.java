package com.recipe.r.ui.activity.table;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.TableItem;
import com.recipe.r.events.MessageEvent;
import com.recipe.r.payment.ZhiFuBaoPay;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.dialog.DatePickDialog;
import com.recipe.r.ui.dialog.PayWayDialog;
import com.recipe.r.ui.widget.SpinerPopWindow;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.wxapi.Wx;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 快捷预订桌子
 */
public class QuickTableActivity extends BaseActivity implements View.OnClickListener {
    private String TYPE = "";
    private String TABLNUMBER = "1";//桌号
    private String Person_Number = "";//就餐人数
    private String TABLENAME = "";//就餐环境
    private String TIME = "";//就餐时间
    private String DATE = "";
    private TextView name_shop;
    private TextView time_eat;
    private DatePickDialog datePickDialogTime1;// 时间选择器
    private int month;
    private int year;
    private int day_of_month;
    private Calendar calendar;
    private RelativeLayout rl_time_quick;
    private TextView tvGoodsSelectNum_quick;
    private ImageView ivGoodsMinus_quick;
    private ImageView ivGoodsAdd_quick;
    private int[] goodsNum;
    private TextView number_table;
    private String SHOPNAME = "";
    private EditText phone_quick;
    private EditText note_quick;
    private Button payment_booking;
    private RadioButton man_gener_quick;
    private RadioButton woman_gener_quick;
    private String Consignee = "先生";
    private PayWayDialog pay_dialog;
    private int hour;
    private int minute;
    private Date startTime;// 开始时间
    private String ORDER_SN = "";
    private String ORDER_ID = "";
    private ArrayList<String> restaurantlist;
    private ArrayList<String> tablelist;
    private ArrayList<String> timelist;
    private ArrayList<String> tablenumberlist;
    private SpinerPopWindow mSpinerPopWindow;
    private LinearLayout switch_restaurant;
    private LinearLayout switch_table;
    private SpinerPopWindow mSpinerPopWindow2;
    private EditText nickname_quick;
    private TextView hour_dining;
    private SpinerPopWindow spinerPopWindow_time;
    private RelativeLayout rl_hour_quick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_table);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("table_id"))) {
            TABLNUMBER = getIntent().getStringExtra("table_id");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("number"))) {
            Person_Number = getIntent().getStringExtra("number");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("shopname"))) {
            SHOPNAME = getIntent().getStringExtra("shopname");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("table_name"))) {
            TABLENAME = getIntent().getStringExtra("table_name");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("date"))) {
            DATE = getIntent().getStringExtra("date");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("time"))) {
            TIME = getIntent().getStringExtra("time");
        }
        initHead(R.mipmap.reset_back, "返回", "快捷预订", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        name_shop = (TextView) findViewById(R.id.name_shop);
        time_eat = (TextView) findViewById(R.id.time_dining);
        hour_dining = (TextView) findViewById(R.id.hour_dining);
        rl_hour_quick = (RelativeLayout) findViewById(R.id.rl_hour_quick);
        rl_time_quick = (RelativeLayout) findViewById(R.id.rl_time_quick);
        tvGoodsSelectNum_quick = (TextView) findViewById(R.id.tvGoodsSelectNum_quick);
        ivGoodsMinus_quick = (ImageView) findViewById(R.id.ivGoodsMinus_quick);
        ivGoodsAdd_quick = (ImageView) findViewById(R.id.ivGoodsAdd_quick);
        number_table = (TextView) findViewById(R.id.number_table);
        phone_quick = (EditText) findViewById(R.id.phone_quick);
        note_quick = (EditText) findViewById(R.id.note_quick);
        payment_booking = (Button) findViewById(R.id.payment_booking);
        man_gener_quick = (RadioButton) findViewById(R.id.man_gener_quick);
        woman_gener_quick = (RadioButton) findViewById(R.id.woman_gener_quick);
        switch_restaurant = (LinearLayout) findViewById(R.id.switch_restaurant);
        switch_table = (LinearLayout) findViewById(R.id.switch_table);
        nickname_quick = (EditText) findViewById(R.id.nickname_quick);
        if (!TextUtils.isEmpty(TABLENAME)) {
            number_table.setText(TABLENAME);
        } else {
            switch_table.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(SHOPNAME)) {
            name_shop.setText(SHOPNAME);
        } else {
            switch_restaurant.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(Person_Number)) {
            //tvGoodsSelectNum_quick.setText(Person_Number);
            tvGoodsSelectNum_quick.setVisibility(View.VISIBLE);
            ivGoodsAdd_quick.setVisibility(View.VISIBLE);
        }
    }

    //获取当前系统时间
    private void getDate() {
        if (TextUtils.isEmpty(DATE)) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            //当前月
            month = (calendar.get(Calendar.MONTH)) + 1;
            day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            startTime = new Date();
            time_eat.setText(DateUtil.formatDateTime(startTime.getTime(), "yyyy-MM-dd"));
        } else {
            time_eat.setText(DATE + " " + TIME);
        }
        getBookTimes(TABLNUMBER, DateUtil.getStringToTime3(time_eat.getText().toString()));
    }


    private void initData() {
        goodsNum = new int[1];
        /*if (!TextUtils.isEmpty(Person_Number)) {
            goodsNum[0] = Integer.parseInt(Person_Number);
        }*/
        goodsNum[0]=1;
        getDate();
        if (goodsNum[0] > 0) {
            tvGoodsSelectNum_quick.setVisibility(View.VISIBLE);
            ivGoodsMinus_quick.setVisibility(View.VISIBLE);
            tvGoodsSelectNum_quick.setText(goodsNum[0] + "");
        }
        getTableData();
    }

    private void initListener() {
        rl_time_quick.setOnClickListener(this);
        ivGoodsMinus_quick.setOnClickListener(this);
        ivGoodsAdd_quick.setOnClickListener(this);
        payment_booking.setOnClickListener(this);
        man_gener_quick.setOnClickListener(this);
        woman_gener_quick.setOnClickListener(this);
        switch_restaurant.setOnClickListener(this);
        switch_table.setOnClickListener(this);
        findViewById(R.id.rl_hour_quick).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_time_quick:
                //点击选择时间
                datePickerDialog();
                break;
            case R.id.ivGoodsAdd_quick:
                goodsNum[0]++;
                if (goodsNum[0] > 0) {
                    tvGoodsSelectNum_quick.setVisibility(View.VISIBLE);
                    ivGoodsMinus_quick.setVisibility(View.VISIBLE);
                    tvGoodsSelectNum_quick.setText(goodsNum[0] + "");
                }
                break;
            case R.id.ivGoodsMinus_quick:
                if (goodsNum[0] > 0) {
                    goodsNum[0]--;
                    tvGoodsSelectNum_quick.setText(goodsNum[0] + "");
                } else {
                    ivGoodsMinus_quick.setVisibility(View.GONE);
                    tvGoodsSelectNum_quick.setVisibility(View.GONE);
                }
                break;
            case R.id.payment_booking:
                if (!chkEditText(phone_quick)) {
                    ToastUtil.show(context, "请输入联系人联系方式", 500);
                    return;
                }
                if (!chkEditText(nickname_quick)) {
                    ToastUtil.show(context, "请输入您的姓名", 100);
                    return;
                }
                getTableInfo();
                break;
            case R.id.man_gener_quick:
                Consignee = "先生";
                break;
            case R.id.woman_gener_quick:
                Consignee = "女士";
                break;
            case R.id.switch_restaurant:
                //点击切换餐厅
                TYPE = "0";
                mSpinerPopWindow.setWidth(switch_restaurant.getWidth());
                mSpinerPopWindow.setHeight(320);
                mSpinerPopWindow.showAsDropDown(switch_restaurant);
                break;
            case R.id.switch_table:
                TYPE = "1";
                mSpinerPopWindow2.setWidth(switch_table.getWidth());
                mSpinerPopWindow2.setHeight(320);
                mSpinerPopWindow2.showAsDropDown(switch_table);
                break;
            case R.id.rl_hour_quick:
                //TODO 选择时间
                TYPE = "2";
                if (spinerPopWindow_time != null) {
                    spinerPopWindow_time.setWidth(rl_hour_quick.getWidth());
                    spinerPopWindow_time.setHeight(320);
                    spinerPopWindow_time.showAsDropDown(rl_hour_quick);
                }
                break;
        }
    }

    /**
     * 订桌
     */
    private void getTableInfo() {
        showProgress();
        String url = Config.URL + Config.BOOKTABLEINFO;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("table_id", TABLNUMBER);
        params.put("book_date", DateUtil.getStringToTime3(time_eat.getText().toString()));
        params.put("book_step", (hour_dining.getText().toString().split(":")[0]).replace("0", ""));
        params.put("people_num", tvGoodsSelectNum_quick.getText().toString());
        params.put("consignee", nickname_quick.getText().toString() + Consignee);
        params.put("mobile", phone_quick.getText().toString());
        if (chkEditText(note_quick)) {
            params.put("remark", note_quick.getText().toString());
        }
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
                            ToastUtil.show(QuickTableActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                initDialog(data.getString("order_id"));
                                ORDER_SN = data.getString("order_sn");
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

    /**
     * 时间选择
     */
    private void datePickerDialog() {
        datePickDialogTime1 = new DatePickDialog(QuickTableActivity.this);
        datePickDialogTime1.setMinTime(startTime);
        datePickDialogTime1.setShowTime(startTime);
        datePickDialogTime1.show();
        datePickDialogTime1.setListener(new DatePickDialog.DateListener() {
            @Override
            public void callback(Date dateBack) {
                time_eat.setText(DateUtil.formatDateTime(dateBack, "yyyy-MM-dd"));
                getBookTimes(TABLNUMBER, DateUtil.getStringToTime3(time_eat.getText().toString()));
            }
        });
    }

    /**
     * 初始化支付方式Dialog
     */
    private void initDialog(final String order_Id) {
        ORDER_ID = order_Id;
        // 隐藏输入法
        pay_dialog = new PayWayDialog(QuickTableActivity.this, R.style.recharge_pay_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay_dialog.payWay == 1) {
                    //支付宝支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        ZhiFuBaoPay aliPay = new ZhiFuBaoPay(QuickTableActivity.this);
                        aliPay.payAliBaba(1, "0", ORDER_SN);
                    } else {
                        ToastUtil.show(QuickTableActivity.this, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                } else {
                    //微信支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        Wx weixin_pay = new Wx(QuickTableActivity.this);
                        weixin_pay.sendPayReq(ORDER_SN, "0");
                    } else {
                        ToastUtil.show(QuickTableActivity.this, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                }

            }
        });
        pay_dialog.setPrice("100");
        pay_dialog.show();
    }


    /**
     * 订桌
     */
    public void payTableInfo(String type) {
        showProgress();
        String url = Config.URL + Config.GETORDERINFO;
        Map<String, String> params = new HashMap<>();
//        params.put("order_sn", ORDER_SN);
        params.put("order_id", ORDER_ID);
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
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
                            ToastUtil.show(QuickTableActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                Intent intent_success = new Intent(QuickTableActivity.this, QuickSuccessActivity.class);
                                intent_success.putExtra("order_id", data.getString("order_id"));
                                startActivity(intent_success);
                                QuickTableActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShowMessageEvent(MessageEvent messageEvent) {
        payTableInfo(messageEvent.getMessage());
    }


    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (TYPE.equals("0")) {
                mSpinerPopWindow.dismiss();
                name_shop.setText(restaurantlist.get(position));
                if (!TextUtils.isEmpty(name_shop.getText().toString())) {
                    switch_restaurant.setVisibility(View.GONE);
                }
            } else if (TYPE.equals("1")) {
                mSpinerPopWindow2.dismiss();
                number_table.setText(tablelist.get(position));
                TABLNUMBER = tablenumberlist.get(position);
                if (!TextUtils.isEmpty(number_table.getText().toString())) {
                    switch_table.setVisibility(View.GONE);
                }
            } else if (TYPE.equals("2")) {
                if (spinerPopWindow_time != null) {
                    spinerPopWindow_time.dismiss();
                    hour_dining.setText(timelist.get(position));
                }
            }
        }
    };


    /**
     * 获取订桌信息接口
     */
    private void getTableData() {
        showProgress();
        String url = Config.URL + Config.GETTABLES;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("option", "hot");
        params.put("page", "1");
        params.put("limit", "");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<TableItem>() {


                    @Override
                    public void onSuccess(int statusCode, TableItem response) {
                        restaurantlist = new ArrayList<>();
                        tablelist = new ArrayList<String>();
                        tablenumberlist = new ArrayList<String>();
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            for (int i = 0; i < response.getData().size(); i++) {
                                restaurantlist.add(response.getData().get(i).getShop_name());
                                tablelist.add(response.getData().get(i).getTable_name());
                                tablenumberlist.add(response.getData().get(i).getTable_id());
                            }
                            mSpinerPopWindow = new SpinerPopWindow<String>(QuickTableActivity.this, restaurantlist, itemClickListener);
                            mSpinerPopWindow2 = new SpinerPopWindow<String>(QuickTableActivity.this, tablelist, itemClickListener);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e(TAG, "statusCode" + statusCode + "error_msg" + error_msg);
                    }
                });
    }


    /**
     * 获取可以预定的订桌时间
     */
    private void getBookTimes(String table_id, String book_date) {
        String url = Config.URL + Config.GETBOOKTIMES;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("table_id", table_id);
        params.put("book_date", book_date);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.e("onSuccess",response.toString());
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            if (status == 1) {
                                timelist = new ArrayList<String>();
                                timelist.clear();
                                JSONObject data = response.getJSONObject("data");
                                JSONObject enable = data.getJSONObject("enable");
                                for (int i = 1; i < 9; i++) {
                                    try {
                                        if (enable.getString("" + i * 3) != null) {
                                            timelist.add(enable.getString("" + i * 3));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                spinerPopWindow_time = new SpinerPopWindow<String>(QuickTableActivity.this, timelist, itemClickListener);
                            } else {
                                ToastUtil.show(QuickTableActivity.this, info, 100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}
