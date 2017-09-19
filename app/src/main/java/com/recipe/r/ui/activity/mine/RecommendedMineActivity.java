package com.recipe.r.ui.activity.mine;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.MenuItem;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.mine.RecommendedMineAdapter;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的推荐菜单界面
 */
public class RecommendedMineActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView detailed_recommend;
    private TextView withdrawals_recommend;
    // 滚动条图片
    private ImageView scrollbar;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号,默认为零
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    ArrayList<MenuItem.MenuInfo> menuItemList = null;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private RecommendedMineAdapter adapter;
    private RelativeLayout default_result_rl;
    private ImageView avater_recommend;
    private TextView nickname_recommend;
    private TextView percentage_recommend;
    private TextView number_recommend;
    private TextView share_recommend;
    private TextView menu_recommend;
    private EditText money_recommend_percentage;
    private RadioButton zhifubao_percentage;
    private RadioButton weixin_percentage;
    private EditText account_percentage;
    private EditText passworld_percentage;
    private Button commit_percentage;
    private int payment_id = 2;//默认支付宝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_mine);
        initHead(R.mipmap.reset_back, "返回", "我的推荐菜", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        if (adapter == null) {
            adapter = new RecommendedMineAdapter(context);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager_recommend);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_recommend_my, null);
        View view2 = inflater.inflate(R.layout.fragment_recommend_percentage, null);
        pageview = new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
        this.swipeToLoadLayout = (SwipeToLoadLayout) view1.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) view1.findViewById(R.id.swipe_target);
        default_result_rl = (RelativeLayout) findViewById(R.id.default_result_rl);
        detailed_recommend = (TextView) findViewById(R.id.detailed_recommend);
        withdrawals_recommend = (TextView) findViewById(R.id.withdrawals_recommend);
        scrollbar = (ImageView) findViewById(R.id.scrollbar_recommend);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        //TODO 初始化提现控件
        avater_recommend = (ImageView) view2.findViewById(R.id.avater_recommend);
        nickname_recommend = (TextView) view2.findViewById(R.id.nickname_recommend);
        percentage_recommend = (TextView) view2.findViewById(R.id.percentage_recommend);
        number_recommend = (TextView) view2.findViewById(R.id.number_recommend);
        share_recommend = (TextView) view2.findViewById(R.id.share_recommend);
        menu_recommend = (TextView) view2.findViewById(R.id.menu_recommend);
        money_recommend_percentage = (EditText) view2.findViewById(R.id.money_recommend_percentage);
        zhifubao_percentage = (RadioButton) view2.findViewById(R.id.zhifubao_percentage);
        weixin_percentage = (RadioButton) view2.findViewById(R.id.weixin_percentage);
        account_percentage = (EditText) view2.findViewById(R.id.account_percentage);
        passworld_percentage = (EditText) view2.findViewById(R.id.passworld_percentage);
        commit_percentage = (Button) view2.findViewById(R.id.commit_percentage);
        initRefresh(swipeToLoadLayout);
        initViewPager();
        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.AVATER, ""), WeakImageViewUtil.getImageView(avater_recommend));
        nickname_recommend.setText(AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.NICKNAME, ""));
        number_recommend.setText(AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.POSINTS, ""));
        percentage_recommend.setText(AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.EARNSUB, ""));
        share_recommend.setText(AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.EARNSUB, ""));
        menu_recommend.setText(AppSettings.getPrefString(RecommendedMineActivity.this, ConfigApp.EARNREC, ""));
    }

    /**
     * 初始化滑动
     */
    private void initViewPager() {
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.scrollbar).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset = (screenW / 2 - bmpW) / 2;
        //计算出切换一个界面时，滚动条的位移量
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);
        if (currIndex == 0) {
            viewPager.setCurrentItem(0);
        } else if (currIndex == 1) {
            viewPager.setCurrentItem(1);
        }
    }

    private void initData() {
        menuItemList = new ArrayList<>();
        getpayment(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecommendedMineActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        detailed_recommend.setOnClickListener(this);
        withdrawals_recommend.setOnClickListener(this);
        zhifubao_percentage.setOnClickListener(this);
        weixin_percentage.setOnClickListener(this);
        commit_percentage.setOnClickListener(this);
    }

    /**
     * 获取我的推荐菜
     *
     * @param page
     * @param limit
     */
    private void getpayment(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.MYRECOMMENDS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)

                .enqueue(new GsonResponseHandler<MenuItem>() {
                             @Override
                             public void onSuccess(int statusCode, MenuItem response) {
                                 hideProgress();
                                 int status = response.getStatus();
                                 String info = response.getInfo();
                                 if (status == 1) {
                                     default_result_rl.setVisibility(View.GONE);
                                     menuItemList = response.getData();
                                     loadRecommendedItem(menuItemList);
                                     if(menuItemList.size()==0){
                                         default_result_rl.setVisibility(View.VISIBLE);
                                     }
                                 }else{
                                     default_result_rl.setVisibility(View.VISIBLE);
                                 }
                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {
                                 hideProgress();
                                 default_result_rl.setVisibility(View.VISIBLE);
                             }
                         }
                );

    }


    //获取展示数据
    private void loadRecommendedItem(final ArrayList<MenuItem.MenuInfo> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    adapter.updatelist(items);
                } else {
                    adapter.append(items);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getpayment(PAGE, limit);
        swipeToLoadLayout.setRefreshing(false);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getpayment(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detailed_recommend:
                PAGE = 1;
                viewPager.setCurrentItem(0);
                detailed_recommend.setTextColor(getResources().getColor(R.color.main_red));
                withdrawals_recommend.setTextColor(getResources().getColor(R.color.text_color));
                getpayment(PAGE, limit);
                break;
            case R.id.withdrawals_recommend:
                PAGE = 1;
                viewPager.setCurrentItem(1);
                detailed_recommend.setTextColor(getResources().getColor(R.color.text_color));
                withdrawals_recommend.setTextColor(getResources().getColor(R.color.main_red));

                break;
            case R.id.zhifubao_percentage:
                //TODO 支付宝提现
                payment_id = 2;
                zhifubao_percentage.setTextColor(getResources().getColor(R.color.white));
                zhifubao_percentage.setChecked(true);
                weixin_percentage.setTextColor(getResources().getColor(R.color.text_color));
                weixin_percentage.setChecked(false);
                break;
            case R.id.weixin_percentage:
                //TODO 微信提现
                payment_id = 1;
                zhifubao_percentage.setTextColor(getResources().getColor(R.color.text_color));
                zhifubao_percentage.setChecked(false);
                weixin_percentage.setTextColor(getResources().getColor(R.color.white));
                weixin_percentage.setChecked(true);
                break;
            case R.id.commit_percentage:
                //TODO 提现
                if (!chkEditText(account_percentage)) {
                    ToastUtil.show(RecommendedMineActivity.this, "未填写提现账号", 100);
                    return;
                }
                if (!chkEditText(money_recommend_percentage)) {
                    ToastUtil.show(RecommendedMineActivity.this, "未填写提现金额", 100);
                    return;
                }
                CommitMoney();
                break;
        }
    }

    /**
     * 提现接口实现
     */
    private void CommitMoney() {
        showProgress();
        String url = Config.URL + Config.TAKEMONEY;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("payment_id", "" + payment_id);
        params.put("account", account_percentage.getText().toString());
        params.put("value", money_recommend_percentage.getText().toString());
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                String info = response.getString("info");
                                ToastUtil.show(RecommendedMineActivity.this, "提现" + info, 100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
//            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
            if (viewPager.getCurrentItem() == 0) {
                //我的提成
                PAGE = 1;
                detailed_recommend.setTextColor(getResources().getColor(R.color.main_red));
                withdrawals_recommend.setTextColor(getResources().getColor(R.color.text_color));
                getpayment(PAGE, limit);
            } else if (viewPager.getCurrentItem() == 1) {
                //提成积分
                PAGE = 1;
                detailed_recommend.setTextColor(getResources().getColor(R.color.text_color));
                withdrawals_recommend.setTextColor(getResources().getColor(R.color.main_red));

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
