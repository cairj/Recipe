package com.recipe.r.ui.activity.table;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 预定成功界面
 * 可跳转至点餐界面
 */
public class QuickSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView name_shop_success;
    private TextView number_table_success;
    private TextView person_success;
    private TextView time_success;
    private TextView price_success;
    private TextView nickname_success;
    private TextView phone_success;
    private TextView note_success;
    private Button advance_menu_success;
    private TextView other_menu_success;
    private String ORDERID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_success);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("order_id"))) {
            ORDERID = getIntent().getStringExtra("order_id");
        }
        initHead(R.mipmap.reset_back, "返回", "恭喜您预订成功", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        name_shop_success = (TextView) findViewById(R.id.name_shop_success);
        number_table_success = (TextView) findViewById(R.id.number_table_success);
        person_success = (TextView) findViewById(R.id.person_success);
        time_success = (TextView) findViewById(R.id.time_success);
        price_success = (TextView) findViewById(R.id.price_success);
        nickname_success = (TextView) findViewById(R.id.nickname_success);
        phone_success = (TextView) findViewById(R.id.phone_success);
        note_success = (TextView) findViewById(R.id.note_success);
        advance_menu_success = (Button) findViewById(R.id.advance_menu_success);
        other_menu_success = (TextView) findViewById(R.id.other_menu_success);
        advance_menu_success.setOnClickListener(this);
        other_menu_success.setOnClickListener(this);
    }

    private void initData() {
        getOrderInfo();
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
        params.put("device","andriod");
        if (!TextUtils.isEmpty(ORDERID)) {
            params.put("order_id", ORDERID);
        } else {
            params.put("order_id", "");
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
//                            String info = response.getString("info");
//                            ToastUtil.show(QuickSuccessActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                JSONObject shop_info = data.getJSONObject("shop_info");
                                name_shop_success.setText(shop_info.getString("shop_name"));
                                number_table_success.setText(shop_info.getString("table_name")/* + "  桌号:" + data.getString("table_id")*/);
                                person_success.setText(data.getString("people_num")+"人");
                                time_success.setText(DateUtil.getDateToString2(Long.parseLong(data.getString("book_time"))));
                                price_success.setText(/*"预订费用:¥"*/"¥" + data.getDouble("payment_cost"));
                                nickname_success.setText(/*"联系人" + */data.getString("consignee"));
                                phone_success.setText(/*"联系电话" +*/ data.getString("mobile"));
                                if (!TextUtils.isEmpty(data.getString("remark"))) {
                                    note_success.setText(data.getString("remark"));
                                } else {
                                    note_success.setText("暂无备注");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickSuccessActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advance_menu_success:
                //点菜
                Intent intent = new Intent(QuickSuccessActivity.this, MainActivity.class);
                intent.putExtra("type", "menu");
                startActivity(intent);
                QuickSuccessActivity.this.finish();
                break;
            case R.id.other_menu_success:
                //首页
                Intent intent_main = new Intent(QuickSuccessActivity.this, MainActivity.class);
                intent_main.putExtra("type", "main");
                startActivity(intent_main);
                QuickSuccessActivity.this.finish();
                break;
        }
    }
}
