package com.recipe.r.ui.activity.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Address;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.adress.AddressAdapter;
import com.recipe.r.ui.widget.ListViewForScrollView;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 地址列表
 */
public class AdressActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener {
    private ListViewForScrollView lv;
    private Button sure_address;
    private AddressAdapter adapter;
    private Context context = AdressActivity.this;
    private ArrayList<Address.Adress> list;
    private String TAG = "AddAddressActivity";
    private boolean isRefresh = true;
    private String ORDER = "";
    private final int resultCode = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);
        if (null != getIntent().getExtras()) {
            ORDER = getIntent().getExtras().getString("type");
        }
        initHead(R.mipmap.reset_back, "返回", "管理地址", 0, "");
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    private void initView() {
        if (adapter == null) {
            adapter = new AddressAdapter(context);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        lv = (ListViewForScrollView) findViewById(R.id.lv_address);
        sure_address = (Button) findViewById(R.id.sure_address);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sure_address.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        getData();
    }

    private void initListener() {
        // 初始化头部信息
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdressActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存修改信息
                for (int i = 0; i < list.size(); i++) {
                    Logger.e(TAG, "" + adapter.states.get(String.valueOf(i)));
                    if (adapter.states.get(String.valueOf(i)) == true) {
                        ModifyAddress(i);
                    }
                }
            }
        });
    }

    /**
     * 获取地址数据
     */
    private void getData() {
        showProgress();
        String url = Config.URL + Config.GETUSERADRESS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<Address>() {

                    @Override
                    public void onSuccess(int statusCode, Address response) {
                        hideProgress();
                        int status = response.getStatus();
//                        String info = response.getInfo();
//                        ToastUtil.show(AdressActivity.this, info, 500);
                        if (status == 1) {
                            list = response.getData();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getStatus() == 1) {
                                    //默认地址
//                                    AppSettings.setPrefString(context, ConfigApp.ADDRID, list.get(i).getAddress_id());
                                }
                            }
                            loadAdress(list);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    private void loadAdress(final ArrayList<Address.Adress> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    adapter.updatelist(items);
                } else {
                    adapter.append(items);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_address:
                //添加新的地址
//                new AlertDialog(AdressActivity.this).builder().setTitle("")
//                        .setMsg("是否添加新的地址")
//                        .setPositiveButton("确认", new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
                Intent intent = new Intent(
                        AdressActivity.this,
                        EditAddressActivity.class);
                startActivity(intent);
//                            }
//                        }).setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                }).show();
                break;

            default:
                break;
        }

    }

    /**
     * 编辑地址界面，修改默认地址接口数据
     */
    private void ModifyAddress(final int position) {
        showProgress();
        String url = Config.URL + Config.SETUSERADDRESSTODEFAULT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("address_id", adapter.getItem(position).getAddress_id());
        params.put("status", "1");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        int status = 0;
                        try {
                            status = response.getInt("status");
//                            String info = response.getString("info");
//                            ToastUtil.show(AdressActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                AppSettings.setPrefString(context, ConfigApp.ADDRID, data.getString("address_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 设置默认
     */
    @Override
    public void onItemClick(AdapterView<?> Adapter, View view, int position,
                            long arg3) {
        adapter.SetRB(position);
        for (int i = 0; i < list.size(); i++) {
            Logger.e(TAG, "" + adapter.states.get(String.valueOf(i)));
            if (adapter.states.get(String.valueOf(i)) == true) {
                if (ORDER.equals("order")) {
                    //TODO 购物地址
                    Intent mIntent = new Intent();
                    mIntent.putExtra("address_id", adapter.getItem(position).getAddress_id());
                    mIntent.putExtra("address", adapter.getItem(position));
                    setResult(resultCode, mIntent);
                    finish();
                } else {
                    ModifyAddress(i);
                }
            }
        }
    }
}

