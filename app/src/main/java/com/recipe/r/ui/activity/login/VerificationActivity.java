package com.recipe.r.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.CodeModel;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机验证码登录界面
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener {
    private TextView user_login_verification;
    private Button loginBtn_verification, btnCode_verification;
    private EditText Etphone_verification, Etcode_verification;
    private CountDowm countDowm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initHead(R.mipmap.reset_back, "返回", "登录", 0, "注册");
        initView();
        initListener();
    }

    private void initView() {
        user_login_verification = (TextView) findViewById(R.id.user_login_verification);
        loginBtn_verification = (Button) findViewById(R.id.loginBtn_verification);
        Etphone_verification = (EditText) findViewById(R.id.Etphone_verification);
        Etcode_verification = (EditText) findViewById(R.id.Etcode_verification);
        btnCode_verification = (Button) findViewById(R.id.btnCode_verification);
        user_login_verification.setOnClickListener(this);
        loginBtn_verification.setOnClickListener(this);
        btnCode_verification.setOnClickListener(this);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificationActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerificationActivity.this, VerificationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_verification:
                Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                startActivity(intent);
                VerificationActivity.this.finish();
                break;
            case R.id.loginBtn_verification:
                //使用验证码登录
                if (!chkEditText(Etphone_verification)) {
                    ToastUtil.show(context, getResources().getString(R.string.phone_write), 500);
                    return;
                }
                if (!chkEditText(Etcode_verification)) {
                    ToastUtil.show(context, getResources().getString(R.string.code_write), 500);
                    return;
                }
                VerificationLogin(Etphone_verification.getText().toString(), Etcode_verification.getText().toString());
                break;
            case R.id.btnCode_verification:
                if (!chkEditText(Etphone_verification)) {
                    ToastUtil.show(context, getResources().getString(R.string.phone_write), 500);
                    return;
                }
                getCode(Etphone_verification.getText().toString());
                break;
        }

    }

    //验证码登录
    private void VerificationLogin(String phone, String code) {
        showProgress();
        String url = Config.URL + Config.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("account", phone);
        params.put("pwd", code);
        params.put("type", "code");
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
                            ToastUtil.show(VerificationActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                String user_name = data.getString("user_name");
                                String user_id = data.getString("user_id");
                                String avater = data.getString("headimgurl");
                                String points = data.getString("point");
                                AppSettings.setPrefString(context, ConfigApp.USERNAME, user_name);
                                AppSettings.setPrefString(context, ConfigApp.USERID, user_id);
                                AppSettings.setPrefString(context, ConfigApp.AVATER, Config.IMAGE_URL + avater);
                                AppSettings.setPrefString(context, ConfigApp.POSINTS, points);
                                AppSettings.setPrefString(context, ConfigApp.ISLOGIN, "0");
//                                LoginHyp(ConfigApp.HXUSERNAME + user_id);
                                Intent intent_main = new Intent(VerificationActivity.this, MainActivity.class);
                                startActivity(intent_main);
                                VerificationActivity.this.finish();
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

    //获取验证码
    private void getCode(String phone) {
        String url = Config.URL + Config.CODE;
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<CodeModel>() {

                    @Override
                    public void onSuccess(int statusCode, CodeModel response) {
                        Logger.d(TAG, "doUpload onSuccess:" + response);
                        if (response.getStatus() == 1) {
                            //开始计时
                            countDowm = new VerificationActivity.CountDowm(60000, 1000);
                            countDowm.start();
                            ToastUtil.show(VerificationActivity.this, response.getInfo(), 500);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        Logger.d(TAG, "doUpload onProgress:" + currentBytes + "/" + totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtil.show(VerificationActivity.this, "" + error_msg, 0);
                        Logger.d(TAG, "doUpload onFailure:" + error_msg);
                    }
                });
    }

    //验证码显示效果
    class CountDowm extends CountDownTimer {

        public CountDowm(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            btnCode_verification.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            btnCode_verification.setText(getResources().getString(R.string.obtain_repeat));
            btnCode_verification.setClickable(true);
        }

    }
//    /**
//     * 环信登录方法
//     */
//    private void LoginHyp(String userName) {
//        ChatClient.getInstance().login("username", "password", new Callback(){
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
