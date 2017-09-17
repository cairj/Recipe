package com.recipe.r.ui.fragment.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.ui.activity.integral.IntegralMineActivity;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.login.NewLoginActivity;
import com.recipe.r.ui.activity.menu.MenuMineActivity;
import com.recipe.r.ui.activity.menu.ShopCarActivity;
import com.recipe.r.ui.activity.mine.CollectionActivity;
import com.recipe.r.ui.activity.mine.RecommendedMineActivity;
import com.recipe.r.ui.activity.mine.UpRecommendationActivity;
import com.recipe.r.ui.activity.order.PaymentActivity;
import com.recipe.r.ui.activity.prize.PrizeActivity;
import com.recipe.r.ui.activity.reservation.ReservationActivity;
import com.recipe.r.ui.activity.setting.SettingActivity;
import com.recipe.r.ui.activity.share.ReleaseShareActivity;
import com.recipe.r.ui.activity.share.ShareActivity;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.fragment.base.BaseFragment;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.UserIsLogin;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 2017
 * 06
 * 2017/6/9
 * wangxiaoer
 * 功能描述：我的界面
 **/
@SuppressLint("ValidFragment")
public class MineFragmentMain extends BaseFragment implements View.OnClickListener {
    private MainActivity context;
    private TextView nickname_mine;
    private ImageView gift_icon;

    public MineFragmentMain(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    private TextView integral_tv;
    private AlertDialog dialog;
    private ImageView avater_mine;
    private String LUCKY_ID = "";
    private boolean isLuCky = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        View view = inflater.inflate(R.layout.fragment_mine_main, container, false);
        initHead(view, R.mipmap.setting, "", "个人中心", /*R.mipmap.shop_cart_white*/0, "");
        setTitleColor(getResources().getColor(R.color.white));
        SetHeadBackground(getResources().getColor(R.color.main_red));
        initView(view);
//        initData();
        initDoClick(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            System.out.println("不可见");
        } else {
            initData();
            System.out.println("当前可见");
        }
    }

    private void initView(View view) {
        if (!UserIsLogin.IsLogn(context)) {
            getRightView().setVisibility(View.GONE);
            return;
        }
        avater_mine = (ImageView) view.findViewById(R.id.avater_mine);
        integral_tv = (TextView) view.findViewById(R.id.integral_tv);
        nickname_mine = (TextView) view.findViewById(R.id.nickname_mine);
        gift_icon = (ImageView) view.findViewById(R.id.gift_icon);
        getTitleView().setTextColor(context.getResources().getColor(R.color.white));
    }

    private void initData() {
        //初始化数据
        getUserInfo();
    }


    /**
     * 获取用户个人数据
     */
    private void getUserInfo() {
        showProgress();
        String url = Config.URL + Config.MEMBERS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
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
                            if (status == 1) {
                                if (response.getJSONObject("data") != null) {
                                    ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, Config.IMAGE_URL + response.getJSONObject("data").getString("headimgurl"), WeakImageViewUtil.getImageView(avater_mine));
                                    if (!TextUtils.isEmpty(response.getJSONObject("data").getString("point"))) {
                                        integral_tv.setText(response.getJSONObject("data").getString("point"));
                                    }
                                    if (!TextUtils.isEmpty(response.getJSONObject("data").getString("user_name"))) {
                                        nickname_mine.setText(response.getJSONObject("data").getString("user_name"));
                                    }
                                    if (!TextUtils.isEmpty(response.getJSONObject("data").getString("earn_rec"))) {
                                        AppSettings.setPrefString(context, ConfigApp.EARNREC, response.getJSONObject("data").getString("earn_rec"));
                                    }
                                    if (!TextUtils.isEmpty(response.getJSONObject("data").getString("earn_agent"))) {
                                        AppSettings.setPrefString(context, ConfigApp.EARNSUB, response.getJSONObject("data").getString("earn_agent"));
                                    }
                                } else {
                                    ToastUtil.show(context, "暂未获取到用户信息", 100);
                                }
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
                        Logger.e("LoginActivity", error_msg);
                    }
                });

    }

    /**
     * 设置每一个人资料的点击事件
     */
    public void initDoClick(View myView) {
        setClickListener(myView, R.id.avater_mine);
        setClickListener(myView, R.id.payment_mine);
        setClickListener(myView, R.id.ll_integral_mine);
//        setClickListener(myView, R.id.charge_mine);
        setClickListener(myView, R.id.menu_mine_rl);
        setClickListener(myView, R.id.reservation_mine_rl);
        setClickListener(myView, R.id.integral_mine_ll);
        setClickListener(myView, R.id.share_mine_rl);
        setClickListener(myView, R.id.collection_mine_rl);
        setClickListener(myView, R.id.recommended_mine_rl);
        setClickListener(myView, R.id.integral_mine_rl);
        setClickListener(myView, R.id.prize_mine_rl);
        setClickListener(myView, R.id.recommended_app_mine_rl);
        setClickListener(myView, R.id.setting_mine_rl);
        setClickListener(myView, R.id.customer_mine_rl);
        setClickListener(myView, R.id.release_share);
        setClickListener(myView, R.id.upload_recommendation_ll);
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置
                Intent intent_set = new Intent(context, SettingActivity.class);
                startActivity(intent_set);
            }
        });
        getRightView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *消息中心,获取我们发起聊天的者的username
                 *获取当前登录用户的 username
                 */
//                if (UserIsLogin.IsLogn(context)) {
//                    // 跳转到聊天界面，开始聊天
//                    Intent intent = new Intent(context, ChatListActivity.class);
//                    startActivity(intent);
//                }
                /*Intent intent = new Intent(context, ShopCarActivity.class);
                startActivity(intent);*/
            }
        });
    }

    /**
     * 设置点击效果
     */
    private void setClickListener(View parentView, int id) {
        parentView.findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avater_mine:
                //点击头像登录
                if (UserIsLogin.isShowLogin(context)) {
                    if (TextUtils.isEmpty(AppSettings.getPrefString(context, ConfigApp.USERNAME, ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, NewLoginActivity.class);
                        context.startActivity(intent);
                    }
                }
                break;
            case R.id.ll_integral_mine:
                //我的积分界面
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_integral2 = new Intent(context, IntegralMineActivity.class);
                    intent_integral2.putExtra("currIndex", "0");
                    startActivity(intent_integral2);
                }
                break;
            case R.id.payment_mine:
                //待付款
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_payment = new Intent(context, PaymentActivity.class);
                    intent_payment.putExtra("type", "0");
                    startActivity(intent_payment);
                }
                break;
//            case R.id.charge_mine:
//                //待收货
//                Intent intent_charge = new Intent(context, PaymentActivity.class);
//                intent_charge.putExtra("type", "1");
//                startActivity(intent_charge);
//                break;
            case R.id.release_share:
                //发布分享
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent = new Intent(context, ReleaseShareActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.collection_mine_rl:
                //我的收藏
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_collection = new Intent(context, CollectionActivity.class);
                    startActivity(intent_collection);
                }
                break;
            case R.id.upload_recommendation_ll:
                //上传推荐
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_upload = new Intent(context, UpRecommendationActivity.class);
                    startActivity(intent_upload);
                }
                break;
            case R.id.integral_mine_ll:
                //我的积分兑换界面
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_integral_ll = new Intent(context, IntegralMineActivity.class);
                    intent_integral_ll.putExtra("currIndex", "1");
                    startActivity(intent_integral_ll);
                }
                break;
            case R.id.reservation_mine_rl:
                //我的预订
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_reservation = new Intent(context, ReservationActivity.class);
                    startActivity(intent_reservation);
                }
                break;
            case R.id.menu_mine_rl:
                //我的外卖界面
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_menu = new Intent(context, MenuMineActivity.class);
                    startActivity(intent_menu);
                }
                break;
            case R.id.share_mine_rl:
                //我的分享界面
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_share = new Intent(context, ShareActivity.class);
                    startActivity(intent_share);
                }
                break;
            case R.id.recommended_mine_rl:
                //我的推荐菜
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_recommended = new Intent(context, RecommendedMineActivity.class);
                    startActivity(intent_recommended);
                }
                break;
            case R.id.integral_mine_rl:
                //我的积分
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_integral = new Intent(context, IntegralMineActivity.class);
                    intent_integral.putExtra("currIndex", "0");
                    startActivity(intent_integral);
                }
                break;
            case R.id.prize_mine_rl:
                //我的奖品
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_prize = new Intent(context, PrizeActivity.class);
//                intent_prize.putExtra("isLucky", isLuCky);
//                intent_prize.putExtra("lucky_id", LUCKY_ID);
                    startActivity(intent_prize);
                }
                break;
            case R.id.recommended_app_mine_rl:
                //推荐App
//                openShareDialog();//自定义分享弹窗
                if (UserIsLogin.IsLogn(context)) {
                    getOtherShare();//先获取需要分享的内容
                }
                break;
            case R.id.setting_mine_rl:
                //设置
                if (UserIsLogin.IsLogn(context)) {
                    Intent intent_set = new Intent(context, SettingActivity.class);
                    startActivity(intent_set);
                }
                break;
            case R.id.customer_mine_rl:
                //联系客服
                dialog = new AlertDialog(context)
                        .builder()
                        .setTitle("提示").setMsg("是否要拨打客服电话").setCancelable(false).setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + "400-606-7866");
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

        }
    }

    //TODO 获取第三方分享内容
    private void getOtherShare() {
        showProgress();
        String url = Config.URL + Config.GETSHAREINFO;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
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
                                         String content = data.getString("content");
                                         String ImageUrl = Config.IMAGE_URL + data.getString("image");
                                         String title = data.getString("title");
                                         String link = data.getString("link");
                                         showShare(title, content, link, ImageUrl);//sharesdk弹窗
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

    /**
     * 分享
     *
     * @param title    标题
     * @param content  内容
     * @param Url      了解
     * @param imageUrl 图片链接
     */
    private void showShare(String title, String content, String Url, String imageUrl) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(Url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url);
        //设置分享的网络图片,图片内容影响微信发布（必须是正确的图片地址）
        oks.setImageUrl(imageUrl);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(Url);
        // 启动分享GUI
        oks.show(context);
    }

}
