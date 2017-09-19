package com.recipe.r.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 新的登录界面
 */
public class NewLoginActivity extends BaseActivity implements View.OnClickListener {
    private Context context = NewLoginActivity.this;
    private ToggleButton according_userPwd_tb;
    private EditText userPwd_et;
    private TextView reset_passworld, user_code_login;
    private Button loginBtn;
    private ImageView avater_new_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        initHead(R.mipmap.reset_back, "返回", "登录", 0, "注册");
        initView();
        initListener();
    }

    private void initView() {
        avater_new_login = (ImageView) findViewById(R.id.avater_new_login);
        according_userPwd_tb = (ToggleButton) findViewById(R.id.according_userPwd_tb);
        userPwd_et = (EditText) findViewById(R.id.userPwd_et);
        user_code_login = (TextView) findViewById(R.id.user_code_login);
        reset_passworld = (TextView) findViewById(R.id.reset_passworld);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        according_userPwd_tb.setOnClickListener(this);
//        reset_passworld.setOnClickListener(this);
        user_code_login.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, AppSettings.getPrefString(NewLoginActivity.this, ConfigApp.AVATER, ""), WeakImageViewUtil.getImageView(avater_new_login));

    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewLoginActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        userPwd_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!arg0.toString().isEmpty()) {
                    according_userPwd_tb.setVisibility(View.VISIBLE);
                } else {
                    according_userPwd_tb.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.according_userPwd_tb:
                //密码是否明文显示
                if (!according_userPwd_tb.isChecked()) {
                    userPwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    userPwd_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.reset_passworld:
                Intent intent_reset = new Intent(NewLoginActivity.this, ResetPassworldActivity.class);
                startActivity(intent_reset);
                break;
            case R.id.user_code_login:
                Intent intent_verification = new Intent(NewLoginActivity.this, VerificationActivity.class);
                startActivity(intent_verification);
                break;
            case R.id.loginBtn:
                if (!chkEditText(userPwd_et)) {
                    ToastUtil.show(NewLoginActivity.this, getString(R.string.passworld_write), 500);
                    return;
                }
                UserLogin(AppSettings.getPrefString(NewLoginActivity.this, ConfigApp.USERNAME, ""), userPwd_et.getText().toString());
                break;
            default:
                break;
        }
    }

    //用户登录
    private void UserLogin(String phone, String userPwd_et) {
        showProgress();
        String url = Config.URL + Config.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("account", phone);
        params.put("pwd", userPwd_et);
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
                            ToastUtil.show(NewLoginActivity.this, info, 500);
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                String user_name = data.getString("user_name");
                                String user_id = data.getString("user_id");
                                String avater = data.getString("headimgurl");
                                String points = data.getString("point");
                                String address_id = data.getString("address_id");
                                String token = data.getString("token");
                                AppSettings.setPrefString(context, ConfigApp.USERNAME, user_name);
                                AppSettings.setPrefString(context, ConfigApp.USERID, user_id);
                                AppSettings.setPrefString(context, ConfigApp.AVATER, Config.IMAGE_URL + avater);
                                AppSettings.setPrefString(context, ConfigApp.POSINTS, points);
                                AppSettings.setPrefString(context, ConfigApp.ADDRID, address_id);
                                AppSettings.setPrefString(context, ConfigApp.ISLOGIN, "0");
                                AppSettings.setPrefString(context, ConfigApp.TOKEN, token);
//                                LoginHyp(ConfigApp.HXUSERNAME + user_id);
                                Intent intent_main = new Intent(NewLoginActivity.this, MainActivity.class);
                                startActivity(intent_main);
                                AppManager.getInstance().killActivity(MainActivity.class);
                                NewLoginActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("NewLoginActivity", error_msg);
                    }
                });
    }

//    /**
//     * 环信登录方法
//     */
//    private void LoginHyp(String userName) {
//        ChatClient.getInstance().login(userName, ConfigApp.HXPWD, new Callback() {
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
    }
}
