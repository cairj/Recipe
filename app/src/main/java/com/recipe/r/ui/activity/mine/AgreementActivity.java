package com.recipe.r.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.ui.activity.BaseActivity;

/**
 * 用户协议界面
 */
public class AgreementActivity extends BaseActivity {
    private WebView agreement_wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        initHead(R.mipmap.reset_back, "返回", "古辛食尚", 0, "");
        initView();
        initListener();
    }


    private void initView() {
        agreement_wb = (WebView) findViewById(R.id.agreement_wb);
        String url = Config.AGREEMENT;
        agreement_wb.loadUrl(url);
        agreement_wb.setWebViewClient(new WebViewClient() {
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
                AgreementActivity.this.finish();
            }
        });
    }
}

