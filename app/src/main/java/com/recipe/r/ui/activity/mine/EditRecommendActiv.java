package com.recipe.r.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 编辑推荐菜单界面
 */
public class EditRecommendActiv extends BaseActivity implements View.OnClickListener {
    private String goods_image;
    private String goods_name;
    private String goods_summary;
    private String main_material;
    private String sub_material;
    private String taste;
    private String method;
    private String make_time;
    ArrayList<Map<String, String>> step_list;
    private LinearLayout add_recommend_ll1;
    private LinearLayout add_recommend_ll2;
    private LinearLayout add_recommend_ll3;
    private LinearLayout add_recommend_ll4;
    private LinearLayout add_recommend_ll5;
    private final int requestCode_ll1 = 1001;
    private final int requestCode_ll2 = 1002;
    private final int requestCode_ll3 = 1003;
    private final int requestCode_ll4 = 1004;
    private final int requestCode_ll5 = 1005;
    private TextView add_recommend_tv1;
    private TextView add_recommend_tv2;
    private TextView add_recommend_tv3;
    private TextView add_recommend_tv4;
    private TextView add_recommend_tv5;
    private ImageView add_recommend_iv1;
    private ImageView add_recommend_iv2;
    private ImageView add_recommend_iv3;
    private ImageView add_recommend_iv4;
    private ImageView add_recommend_iv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recommend);
        if (getIntent().getExtras() != null) {
            goods_image = getIntent().getStringExtra("goods_image");
            goods_name = getIntent().getStringExtra("goods_name");
            goods_summary = getIntent().getStringExtra("goods_summary");
            main_material = getIntent().getStringExtra("main_material");
            sub_material = getIntent().getStringExtra("sub_material");
            taste = getIntent().getStringExtra("taste");
            method = getIntent().getStringExtra("method");
            make_time = getIntent().getStringExtra("make_time");
        }
        initHead(R.mipmap.reset_back, "返回", "编辑菜谱", 0, "保存");
        initView();
        initListener();
    }

    private void initView() {
        step_list = new ArrayList<>();
        add_recommend_ll1 = (LinearLayout) findViewById(R.id.add_recommend_ll1);
        add_recommend_ll2 = (LinearLayout) findViewById(R.id.add_recommend_ll2);
        add_recommend_ll3 = (LinearLayout) findViewById(R.id.add_recommend_ll3);
        add_recommend_ll4 = (LinearLayout) findViewById(R.id.add_recommend_ll4);
        add_recommend_ll5 = (LinearLayout) findViewById(R.id.add_recommend_ll5);
        add_recommend_tv1 = (TextView) findViewById(R.id.add_recommend_tv1);
        add_recommend_tv2 = (TextView) findViewById(R.id.add_recommend_tv2);
        add_recommend_tv3 = (TextView) findViewById(R.id.add_recommend_tv3);
        add_recommend_tv4 = (TextView) findViewById(R.id.add_recommend_tv4);
        add_recommend_tv5 = (TextView) findViewById(R.id.add_recommend_tv5);
        add_recommend_iv1 = (ImageView) findViewById(R.id.add_recommend_iv1);
        add_recommend_iv2 = (ImageView) findViewById(R.id.add_recommend_iv2);
        add_recommend_iv3 = (ImageView) findViewById(R.id.add_recommend_iv3);
        add_recommend_iv4 = (ImageView) findViewById(R.id.add_recommend_iv4);
        add_recommend_iv5 = (ImageView) findViewById(R.id.add_recommend_iv5);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditRecommendActiv.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRecommend(step_list);
            }
        });
        add_recommend_ll1.setOnClickListener(this);
        add_recommend_ll2.setOnClickListener(this);
        add_recommend_ll3.setOnClickListener(this);
        add_recommend_ll4.setOnClickListener(this);
        add_recommend_ll5.setOnClickListener(this);
    }

    /**
     * 发布上传推荐菜方法
     */
    private void AddRecommend(ArrayList<Map<String, String>> file_img) {
        showProgress();
        String url = Config.URL + Config.GOODSIMPORT;
        Map<String, String> params = new HashMap<>();
        Map<String, ArrayList<Map<String, String>>> step = new HashMap<>();
//        Map<String, String> goods_info = new HashMap<>();
        Gson g = new Gson();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
        //拼接上传数组
        params.put("goods_name", goods_name);
        params.put("goods_summary", goods_summary);
        params.put("main_material", main_material);
        params.put("sub_material", sub_material);
        params.put("taste", taste);
        params.put("method", method);
        params.put("make_time", make_time);//
        params.put("goods_image", goods_image);
        if (file_img.size() > 0) {
            step.put("step", file_img);
        }
//        String jsonString = g.toJson(goods_info);
//        try {
//            params.put("goods_info", URLEncoder.encode(jsonString, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        mMyOkhttp.upload()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                EditRecommendActiv.this.finish();
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
                        Logger.e("EditRecommendActiv", error_msg);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_recommend_ll1:
                Intent mIntent_recomend1 = new Intent();
                mIntent_recomend1.setClass(EditRecommendActiv.this,
                        WriteRecommendActivity.class);
                startActivityForResult(mIntent_recomend1, requestCode_ll1);
                break;
            case R.id.add_recommend_ll2:
                Intent mIntent_recomend2 = new Intent();
                mIntent_recomend2.setClass(EditRecommendActiv.this,
                        WriteRecommendActivity.class);
                startActivityForResult(mIntent_recomend2, requestCode_ll2);
                break;
            case R.id.add_recommend_ll3:
                Intent mIntent_recomend3 = new Intent();
                mIntent_recomend3.setClass(EditRecommendActiv.this,
                        WriteRecommendActivity.class);
                startActivityForResult(mIntent_recomend3, requestCode_ll3);
                break;
            case R.id.add_recommend_ll4:
                Intent mIntent_recomend4 = new Intent();
                mIntent_recomend4.setClass(EditRecommendActiv.this,
                        WriteRecommendActivity.class);
                startActivityForResult(mIntent_recomend4, requestCode_ll4);
                break;
            case R.id.add_recommend_ll5:
                Intent mIntent_recomend5 = new Intent();
                mIntent_recomend5.setClass(EditRecommendActiv.this,
                        WriteRecommendActivity.class);
                startActivityForResult(mIntent_recomend5, requestCode_ll5);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String file_img = data.getStringExtra("file_img");
        String recommend_tv = data.getStringExtra("recommend_tv");
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case requestCode_ll1:
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + file_img, WeakImageViewUtil.getImageView(add_recommend_iv1));
                add_recommend_tv1.setText(recommend_tv);
                break;
            case requestCode_ll2:
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + file_img, WeakImageViewUtil.getImageView(add_recommend_iv2));
                add_recommend_tv2.setText(recommend_tv);
                break;
            case requestCode_ll3:
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + file_img, WeakImageViewUtil.getImageView(add_recommend_iv3));
                add_recommend_tv3.setText(recommend_tv);
                break;
            case requestCode_ll4:
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + file_img, WeakImageViewUtil.getImageView(add_recommend_iv4));
                add_recommend_tv4.setText(recommend_tv);
                break;
            case requestCode_ll5:
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + file_img, WeakImageViewUtil.getImageView(add_recommend_iv5));
                add_recommend_tv5.setText(recommend_tv);
                break;
            default:
                break;
        }
    }
}
