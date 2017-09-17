package com.recipe.r.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置用户名界面
 */
public class SetUserActivity extends BaseActivity implements View.OnClickListener {
    private EditText name_setuser, passworld_setuser;
    private Button sure_setuser;
    private String TAG = "SetUserActivity";
    private String PHONE = "";
    private String CODE = "";
    private Context context = SetUserActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user);
        initHead(R.mipmap.reset_back, "返回", "设置用户名", 0, "");
        initView();
        initData();
        initListener();
    }

    //获取数据
    private void initData() {
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("Phone"))) {
            PHONE = getIntent().getExtras().getString("Phone");
        }
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("Code"))) {
            CODE = getIntent().getExtras().getString("Code");
        }
    }

    private void initView() {
        name_setuser = (EditText) findViewById(R.id.name_setuser);
        passworld_setuser = (EditText) findViewById(R.id.passworld_setuser);
        sure_setuser = (Button) findViewById(R.id.sure_setuser);
        sure_setuser.setOnClickListener(this);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUserActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_setuser:
                //注册
                if (!chkEditText(name_setuser)) {
                    showToast(R.string.set_username);
                    return;
                }
                if (!chkEditText(passworld_setuser)) {
                    showToast(R.string.set_passworld);
                    return;
                }
                setRegister(passworld_setuser.getText().toString());
                break;
        }
    }

    //注册账户
    private void setRegister(String passworld) {
        showProgress();
        String url = Config.URL + Config.REGISTER;
        Map<String, String> params = new HashMap<>();
        params.put("mobile", PHONE);
        params.put("pwd", passworld);
        params.put("code", CODE);
        params.put("device", "andriod");
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
                        Log.e("onSuccess",response.toString());
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            ToastUtil.show(SetUserActivity.this, info, 500);
                            if (status == 1) {
                                String user_name = response.getString("user_name");
                                String user_id = response.getString("user_id");
                                AppSettings.setPrefString(context, ConfigApp.USERNAME, user_name);
                                AppSettings.setPrefString(context, ConfigApp.USERID, user_id);
                                //注册失败会抛出HyphenateException
//                                EMClient.getInstance().createAccount(ConfigApp.HXUSERNAME + user_id, ConfigApp.HXPWD);//同步方法
//                                registerHYP();
                                Intent intent = new Intent(SetUserActivity.this, LoginActivity.class);
                                startActivity(intent);
                                SetUserActivity.this.finish();
                                AppManager.getInstance().killActivity(RegisterActivity.class);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

//    /**
//     * 环信注册
//     */
//    private void registerHYP() {
//        ChatClient.getInstance().createAccount(ConfigApp.HXUSERNAME + AppSettings.getPrefString(SetUserActivity.this, ConfigApp.USERID, ""), ConfigApp.HXPWD, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyOkhttp.cancel(this);
    }
}
