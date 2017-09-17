package com.recipe.r.ui.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.utils.NoHtml;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 热点商品详情
 */
public class HotDetailsActivity extends BaseActivity {
    private WebView content_hotdeatils;
    private TextView title_hotdeatils;
    private String news_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_details);
        setTranslucentStatus(R.color.main_red);
        initHead(R.mipmap.reset_back, "返回", "", R.mipmap.share_icon, "");
        SetHeadBackground(getResources().getColor(R.color.main_red));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("news_id"))) {
            news_id = getIntent().getStringExtra("news_id");
        }
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_hotdeatils = (TextView) findViewById(R.id.title_hotdeatils);
        content_hotdeatils = (WebView) findViewById(R.id.content_hotdeatils);
    }

    private void initData() {
        getHotDetails();
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotDetailsActivity.this.finish();
            }
        });
//        getRightView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showShare();
//            }
//        });
    }

    private void getHotDetails() {
        showProgress();
        String url = Config.URL + Config.GETINFORMATIONDETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("news_id", news_id);
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
                                JSONObject data = response.getJSONObject("data");
                                title_hotdeatils.setText(NoHtml.toNoHtml(data.getString("summary")));
                                SetHtml(data.getString("content"));
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

                    }
                });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 设置了html数据的头部文件，
     * 拼接content,防止图文混排的时候，
     * 图片无法计算正确高度
     */
    private void SetHtml(String content) {
        String HtmlHead = "<!DOCTYPE html><html><head><meta charset="
                + "utf-8"
                + ">"
                + "<meta name="
                + "viewport"
                + "content="
                + "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
                + "/>" + "<title></title>" + "<style>"
                + "img{max-width:100% !important;}</style>" + "</head>"
                + "<body  bgcolor=" + "white" + ">" + "<p align=" + "center"
                + ">";
        String HtmlBottom = "</p></body></html>";
        String newContent = HtmlHead + content + HtmlBottom;
        content_hotdeatils.loadDataWithBaseURL(null, newContent, "text/html",
                "utf-8", null);
        content_hotdeatils.getSettings().setJavaScriptEnabled(true);
        content_hotdeatils.setWebChromeClient(new WebChromeClient());
    }
}
