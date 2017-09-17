package com.recipe.r.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.entity.CodeModel;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.base.Config;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册界面
 * wangxiaoer
 * 主要处理注册逻辑
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_deal_content;
    private Button reg_btnCode;
    private EditText reg_Etphone, reg_Etcode;
    private CountDowm countDowm;
    private Button next_register;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initHead(R.mipmap.reset_back, "返回", "注册", 0, "");
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reg_Etcode != null) {
            reg_Etcode.setText("");
        }
    }

    private void initView() {
        tv_deal_content = (TextView) findViewById(R.id.tv_deal_content);
        reg_btnCode = (Button) findViewById(R.id.reg_btnCode);
        reg_Etphone = (EditText) findViewById(R.id.reg_Etphone);
        next_register = (Button) findViewById(R.id.next_register);
        reg_Etcode = (EditText) findViewById(R.id.reg_Etcode);
        reg_btnCode.setOnClickListener(this);
        next_register.setOnClickListener(this);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
        tv_deal_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("协议内容");
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_btnCode:// 获取验证码
                if ((reg_Etphone.getText().toString()).isEmpty()) { //手机号为空
                    showToast(getResources().getString(R.string.phone_tip));
                    break;
                }
                getCode(reg_Etphone.getText().toString());

                break;
            case R.id.next_register:
                //下一步
                if (!TextUtils.isEmpty(reg_Etcode.getText().toString())) {
                    Intent intent = new Intent(RegisterActivity.this, SetUserActivity.class);
                    intent.putExtra("Phone", reg_Etphone.getText().toString());
                    intent.putExtra("Code", reg_Etcode.getText().toString());
                    startActivity(intent);
                } else {
                    showToast(R.string.code_write);
                }
                break;
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
                        Logger.d(TAG, "doUpload onSuccess:" + response);
                        hideProgress();
                        if (response.getStatus() == 1) {
                            //开始计时
                            countDowm = new CountDowm(60000, 1000);
                            countDowm.start();
                            ToastUtil.show(RegisterActivity.this, response.getInfo(), 500);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        Logger.d(TAG, "doUpload onProgress:" + currentBytes + "/" + totalBytes);

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtil.show(RegisterActivity.this, "" + error_msg, 0);
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
            reg_btnCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            reg_btnCode.setText(getResources().getString(R.string.obtain_repeat));
            reg_btnCode.setClickable(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyOkhttp.cancel(this);
    }
}
