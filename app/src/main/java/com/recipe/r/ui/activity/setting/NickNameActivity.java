package com.recipe.r.ui.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置昵称界面
 */
public class NickNameActivity extends BaseActivity {
    private EditText et_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        initHead(R.mipmap.reset_back, "返回", "昵称设置", 0, "保存");
        initView();
        initListener();
    }


    private void initView() {
        et_nickname = (EditText) findViewById(R.id.et_nickname);
    }


    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NickNameActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkEditText(et_nickname)) {
                    ToastUtil.show(context, "请输入昵称", 500);
                    return;
                }
                setUserInfo();
            }

        });
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo() {
        showProgress();
        String url = Config.URL + Config.SETUP;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","andriod");
        params.put("user_name", et_nickname.getText().toString());
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                             @Override
                             public void onSuccess(int statusCode, JSONObject response) {
                                 hideProgress();
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");
                                     
                                     if (status == 1) {
                                         JSONObject data=response.getJSONObject("data");
                                         if(!TextUtils.isEmpty(data.getString("user_name"))){
                                             AppSettings.setPrefString(NickNameActivity.this, ConfigApp.NICKNAME, data.getString("user_name"));
                                         }
                                         NickNameActivity.this.finish();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }

                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {
                                 hideProgress();
                             }
                         }
                );

    }
}
