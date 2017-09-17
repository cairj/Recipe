package com.recipe.r.ui.activity.address;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.bean.JsonBean;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.GetJsonDataUtil;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.PhoneUtil;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加编辑地址
 */
public class EditAddressActivity extends BaseActivity {
    private EditText nickname_editaddress;
    private EditText phone_editaddress;
    private RelativeLayout area_editaddress;
    private EditText address_editaddress;
    private EditText code_editaddress;
    private Context context = EditAddressActivity.this;
    private String TAG = "EditAddressActivity";
    private TextView region_editaddress;
    private CheckBox default_address;
    private String CODE = "";
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private String provice = "";
    private String city = "";
    private String district = "";
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initHead(R.mipmap.reset_back, "返回", "添加地址", 0, "保存");
        initView();
        initData();
        initListener();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        Logger.e("EditAddressActivity", "开始解析数据");
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Logger.e("EditAddressActivity", "解析数据成功");
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Logger.e("EditAddressActivity", "解析数据失败");
                    break;

            }
        }
    };

    private void initView() {
        nickname_editaddress = (EditText) findViewById(R.id.nickname_editaddress);
        phone_editaddress = (EditText) findViewById(R.id.phone_editaddress);
        area_editaddress = (RelativeLayout) findViewById(R.id.area_editaddress);
        address_editaddress = (EditText) findViewById(R.id.address_editaddress);
        code_editaddress = (EditText) findViewById(R.id.code_editaddress);
        region_editaddress = (TextView) findViewById(R.id.region_editaddress);
        default_address = (CheckBox) findViewById(R.id.default_address);

        area_editaddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    Toast.makeText(EditAddressActivity.this, "暂未获取到城市数据，请稍等", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initData() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAddressActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkEditText(nickname_editaddress)) {
                    Toast.makeText(context, "请输入收货人", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String str = stringFilter(nickname_editaddress.getText()
                        .toString());
                if (!(nickname_editaddress.getText().toString()).equals(str)) {
                    Toast.makeText(context, "收货人携带特殊字符", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!PhoneUtil.isGloblePhoneNumber(phone_editaddress.getText()
                        .toString())) {
                    Toast.makeText(context, "无效的手机号码！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String str2 = stringFilter(phone_editaddress.getText()
                        .toString());
                if (!(phone_editaddress.getText().toString()).equals(str2)) {
                    Toast.makeText(context, "手机号码携带特殊字符", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!chkEditText(phone_editaddress)) {
                    Toast.makeText(context, "请输入手机号码", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(region_editaddress.getText().toString())) {
                    Toast.makeText(context, "请选择地区", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!chkEditText(address_editaddress)) {
                    Toast.makeText(context, "请输入详细地址", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String str3 = stringFilter(address_editaddress.getText()
                        .toString());
                if (!(address_editaddress.getText().toString()).equals(str3)) {
                    Toast.makeText(context, "详细地址携带特殊字符", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                // if (!chkEditText(code_editaddress)) {
                // Toast.makeText(context, "请输入邮编", Toast.LENGTH_SHORT).show();
                // return;
                // }
                String str4 = stringFilter(code_editaddress.getText()
                        .toString());
                if (!(code_editaddress.getText().toString()).equals(str4)) {
                    Toast.makeText(context, "邮编携带特殊字符", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                CommitAddress();
            }
        });
    }


    //提交新的地址
    private void CommitAddress() {
        showProgress();
        String url = Config.URL + Config.ADDUSERADDRESS;
        Map<String, String> params = new HashMap<>();
        String detailAddr = address_editaddress.getText().toString();
//        String code = code_editaddress.getText().toString();
        String receiverName = nickname_editaddress.getText().toString();
        String receiverPhone = phone_editaddress.getText().toString();
        String isDefault;
        if (default_address.isChecked()) {
            isDefault = "1";
        } else {
            isDefault = "0";
        }
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","andriod");
        params.put("provice", provice);
        params.put("city", city);
        params.put("district", district);
        params.put("address", detailAddr);
        params.put("consignee", receiverName);
        params.put("mobile", receiverPhone);
        params.put("default", isDefault);
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
                            ToastUtil.show(EditAddressActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data=response.getJSONObject("data");
                                AppSettings.setPrefString(context, ConfigApp.ADDRID, data.getString("address_id"));
                                EditAddressActivity.this.finish();
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

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                provice = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                district = options3Items.get(options1).get(options2).get(options3);
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                region_editaddress.setText(tx);
            }
        })

                .setTitleText("地址选择")
                .setTitleColor(Color.BLUE)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
