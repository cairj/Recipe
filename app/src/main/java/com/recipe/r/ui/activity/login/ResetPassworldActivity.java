package com.recipe.r.ui.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.CodeModel;
import com.recipe.r.ui.activity.BaseActivity;
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
 * wangxiaoer
 * 重置密码界面
 */
public class ResetPassworldActivity extends BaseActivity implements View.OnClickListener {
    private Button reg_btnCode_reset, sure_reset;
    private CountDowm countDowm;
    private EditText reg_Etphone_reset, passworld_reset, reg_Etcode_reset;
    private String TAG = "ResetPassworldActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passworld);
        initHead(R.mipmap.reset_back, "返回", "修改密码", 0, "");
        initView();
        initListener();
    }

    private void initView() {
        reg_btnCode_reset = (Button) findViewById(R.id.reg_btnCode_reset);
        reg_Etphone_reset = (EditText) findViewById(R.id.reg_Etphone_reset);
        sure_reset = (Button) findViewById(R.id.sure_reset);
        passworld_reset = (EditText) findViewById(R.id.passworld_reset);
        reg_Etcode_reset = (EditText) findViewById(R.id.reg_Etcode_reset);
        reg_btnCode_reset.setOnClickListener(this);
        sure_reset.setOnClickListener(this);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPassworldActivity.this.finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_btnCode_reset:// 获取验证码
                if (TextUtils.isEmpty(reg_Etphone_reset.getText().toString())) { //手机号为空
                    showToast(getResources().getString(R.string.phone_tip));
                    break;
                }
                getCode(reg_Etphone_reset.getText().toString());
                break;
            case R.id.sure_reset:
                if (!chkEditText(passworld_reset)) {
                    ToastUtil.show(ResetPassworldActivity.this, getString(R.string.new_passworld), 500);
                    return;
                }
                if (!chkEditText(reg_Etphone_reset)) {
                    ToastUtil.show(ResetPassworldActivity.this, getString(R.string.new_phone), 500);
                    return;
                }
                if (!chkEditText(reg_Etcode_reset)) {
                    ToastUtil.show(ResetPassworldActivity.this, "请输入验证码再重置", 500);
                    return;
                }
                SetNewPassworld(passworld_reset.getText().toString());
                break;
        }
    }

    //重置密码
    private void SetNewPassworld(String pwd) {
        String url = Config.URL + Config.RESETPWD;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("pwd", pwd);
        params.put("device", "andriod");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            ToastUtil.show(ResetPassworldActivity.this, info, 500);
                            if (status == 1) {
                                ResetPassworldActivity.this.finish();
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

    //验证码显示效果
    class CountDowm extends CountDownTimer {

        public CountDowm(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            reg_btnCode_reset.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            reg_btnCode_reset.setText(getResources().getString(R.string.obtain_repeat));
            reg_btnCode_reset.setClickable(true);
        }

    }

    //获取验证码
    private void getCode(String phone) {
        showProgress();
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
                        hideProgress();
                        Logger.d(TAG, "doUpload onSuccess:" + response);
                        if (response.getStatus() == 1) {
                            //开始计时
                            countDowm = new ResetPassworldActivity.CountDowm(60000, 1000);
                            countDowm.start();
                            ToastUtil.show(ResetPassworldActivity.this, response.getInfo(), 500);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        Logger.d(TAG, "doUpload onProgress:" + currentBytes + "/" + totalBytes);

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtil.show(ResetPassworldActivity.this, "" + error_msg, 0);
                        Logger.d(TAG, "doUpload onFailure:" + error_msg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyOkhttp.cancel(this);
    }
}
