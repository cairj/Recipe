package com.recipe.r.ui.activity.integral;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Integral;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.integral.ExchangeAdapter;
import com.recipe.r.ui.adapter.integral.IntegralAdapter;
import com.recipe.r.ui.widget.GridDivider;
import com.recipe.r.ui.widget.SpaceItemDecoration;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 我的积分界面
 * 包含积分兑换和积分明细
 */
public class IntegralMineActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView detailed_integral;
    private TextView withdrawals_integral;
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
    private SwipeToLoadLayout swipeToLoadLayout_nopay;
    private SwipeToLoadLayout swipeToLoadLayout_history;
    private RecyclerView mRecyclerView_nopay;
    private RecyclerView mRecyclerView_history;
    ArrayList<Integral.IntegralData> detailed_integralItemList = null;
    ArrayList<Integral.IntegralData> exchange_integralItemList = null;
    private ImageView avater_integral;
    private IntegralAdapter it_adapter;
    private ExchangeAdapter ex_adapter;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private TextView nickname_integral;
    private TextView number_integral;
    private RelativeLayout default_result_rl;
    private TextView share_integral;
    private TextView menu_integral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_mine);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("currIndex"))) {
            currIndex = Integer.parseInt(getIntent().getStringExtra("currIndex"));
        }
        initHead(R.mipmap.reset_back, "返回", "我的积分", 0, "");
        initView();
        initData();
        initListener();
    }


    private void initView() {
        if (it_adapter == null) {
            it_adapter = new IntegralAdapter(context);
        }
        if (ex_adapter == null) {
            ex_adapter = new ExchangeAdapter(context);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager_integral);
        share_integral = (TextView) findViewById(R.id.share_integral);
        menu_integral= (TextView) findViewById(R.id.menu_integral);
        avater_integral = (ImageView) findViewById(R.id.avater_integral);
        nickname_integral = (TextView) findViewById(R.id.nickname_integral);
        number_integral = (TextView) findViewById(R.id.number_integral);
        default_result_rl = (RelativeLayout) findViewById(R.id.default_result_rl);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_integral_detail, null);
        View view2 = inflater.inflate(R.layout.fragment_integral_withdrawals, null);
        this.swipeToLoadLayout_nopay = (SwipeToLoadLayout) view1.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView_nopay = (RecyclerView) view1.findViewById(R.id.swipe_target);
        this.swipeToLoadLayout_history = (SwipeToLoadLayout) view2.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView_history = (RecyclerView) view2.findViewById(R.id.swipe_target);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView_nopay.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView_history.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView_history.addItemDecoration(new GridDivider(IntegralMineActivity.this, 2, this.getResources().getColor(R.color.light_gray)));

        mRecyclerView_history.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        detailed_integral = (TextView) findViewById(R.id.detailed_integral);
        withdrawals_integral = (TextView) findViewById(R.id.withdrawals_integral);
        scrollbar = (ImageView) findViewById(R.id.scrollbar_integral);

        mRecyclerView_nopay.setAdapter(it_adapter);

        mRecyclerView_history.setAdapter(ex_adapter);
        detailed_integral.setOnClickListener(this);

        withdrawals_integral.setOnClickListener(this);
        pageview = new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
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
        initRefresh(swipeToLoadLayout_history);
        number_integral.setText("积分   " + AppSettings.getPrefString(IntegralMineActivity.this, ConfigApp.POSINTS, ""));
        nickname_integral.setText(AppSettings.getPrefString(IntegralMineActivity.this, ConfigApp.USERNAME, ""));
        share_integral.setText("分享提成：" + AppSettings.getPrefString(IntegralMineActivity.this, ConfigApp.EARNSUB, ""));
        menu_integral.setText("推荐提成："+ AppSettings.getPrefString(IntegralMineActivity.this, ConfigApp.EARNREC, ""));
        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_photo, AppSettings.getPrefString(IntegralMineActivity.this, ConfigApp.AVATER, ""), WeakImageViewUtil.getImageView(avater_integral));
        if (currIndex == 0) {
            viewPager.setCurrentItem(0);
        } else if (currIndex == 1) {
            viewPager.setCurrentItem(1);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

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
                PAGE = 1;
                detailed_integral.setTextColor(getResources().getColor(R.color.main_red));
                withdrawals_integral.setTextColor(getResources().getColor(R.color.text_color));
                getIntegralData(PAGE, limit, "1");
            } else if (viewPager.getCurrentItem() == 1) {
                PAGE = 1;
                detailed_integral.setTextColor(getResources().getColor(R.color.text_color));
                withdrawals_integral.setTextColor(getResources().getColor(R.color.main_red));
                getExchangeData(PAGE, limit, "2");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void initData() {
        //默认初始化加载数据
        detailed_integralItemList = new ArrayList<>();
        exchange_integralItemList = new ArrayList<>();
        getIntegralData(PAGE, limit, "1");
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntegralMineActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout_nopay.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout_nopay.setOnLoadMoreListener(this);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout_history.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout_history.setOnLoadMoreListener(this);
    }

    /**
     * 获取个人奖品数据
     *
     * @param page
     * @param limit
     */
    private void getIntegralData(int page, int limit, final String type) {
        showProgress();
        String url = Config.URL + Config.GETPOINTRECORD;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
        params.put("type", type);//1，获取，2，兑换
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)

                .enqueue(new GsonResponseHandler<Integral>() {

                    @Override
                    public void onSuccess(int statusCode, Integral response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            default_result_rl.setVisibility(View.GONE);
                            detailed_integralItemList = response.getData();
                            loadDetailedItem(detailed_integralItemList);
                            if(detailed_integralItemList.size()==0){
                                default_result_rl.setVisibility(View.VISIBLE);
                            }
                        } else {
                            default_result_rl.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        default_result_rl.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 获取兑换奖品列表
     *
     * @param page
     * @param limit
     */
    private void getExchangeData(int page, int limit, final String type) {
        showProgress();
        String url = Config.URL + Config.GETINTEGRALLISTS;
        Map<String, String> params = new HashMap<>();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("type", type);//1，获取，2，兑换
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<Integral>() {

                    @Override
                    public void onSuccess(int statusCode, Integral response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            default_result_rl.setVisibility(View.GONE);
                            exchange_integralItemList = response.getData();
                            loadExchangeItem(exchange_integralItemList);
                        }else {
                            default_result_rl.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        default_result_rl.setVisibility(View.VISIBLE);
                    }
                });
    }

    //获取明细展示数据
    private void loadDetailedItem(final ArrayList<Integral.IntegralData> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    it_adapter.updatelist(items);
                } else {
                    it_adapter.append(items);
                }

            }
        });
    }

    //获取兑换商品展示数据
    private void loadExchangeItem(final ArrayList<Integral.IntegralData> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    ex_adapter.updatelist(items);
                } else {
                    ex_adapter.append(items);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detailed_integral:
                PAGE = 1;
                viewPager.setCurrentItem(0);
                detailed_integral.setTextColor(getResources().getColor(R.color.main_red));
                withdrawals_integral.setTextColor(getResources().getColor(R.color.text_color));
                getIntegralData(PAGE, limit, "1");
                break;
            case R.id.withdrawals_integral:
                PAGE = 1;
                viewPager.setCurrentItem(1);
                detailed_integral.setTextColor(getResources().getColor(R.color.text_color));
                withdrawals_integral.setTextColor(getResources().getColor(R.color.main_red));
                getExchangeData(PAGE, limit, "2");
                break;

        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        if (viewPager.getCurrentItem() == 0) {
            getIntegralData(PAGE, limit, "1");
            swipeToLoadLayout_nopay.setRefreshing(false);
        } else if (viewPager.getCurrentItem() == 1) {
            getExchangeData(PAGE, limit, "2");
            swipeToLoadLayout_history.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        if (viewPager.getCurrentItem() == 0) {
            getIntegralData(PAGE, limit, "1");
            swipeToLoadLayout_nopay.setLoadingMore(false);
        } else if (viewPager.getCurrentItem() == 1) {
            getExchangeData(PAGE, limit, "2");
            swipeToLoadLayout_history.setLoadingMore(false);
        }
    }
}
