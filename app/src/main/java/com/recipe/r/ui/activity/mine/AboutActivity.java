package com.recipe.r.ui.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.dialog.AlertDialog;

/**
 * 关于古辛食尚
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private AlertDialog dialog;
    private TextView adrress_web;
    private WebView about_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initHead(R.mipmap.reset_back, "返回", "古辛食尚", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        adrress_web = (TextView) findViewById(R.id.adrress_web);
        about_web = (WebView) findViewById(R.id.about_web);
    }

    private void initData() {
        String url = Config.ABOUT_URL + Config.ABOUT_GUXIN;
        about_web.loadUrl(url);
        about_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutActivity.this.finish();
            }
        });
        setClickListener(R.id.customer_rl);
        setClickListener(R.id.web_rl);
    }


    /**
     * 设置点击效果
     */
    private void setClickListener(int id) {
        findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customer_rl:
                //联系客服
                dialog = new AlertDialog(context)
                        .builder()
                        .setTitle("提示").setMsg("是否要拨打客服电话").setCancelable(false).setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + "400-123-8888");
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                break;
            case R.id.web_rl:
                //跳转外网
                String URL = adrress_web.getText().toString();
                Uri uri = Uri.parse(URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}
