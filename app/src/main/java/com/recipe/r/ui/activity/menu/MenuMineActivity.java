package com.recipe.r.ui.activity.menu;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.PaymentItem;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.order.OrderInfoActivity;
import com.recipe.r.ui.adapter.mine.FoodMenuAdapter;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.recipe.r.R.id.swipeToLoadLayout;

/**
 * 菜单选择界面
 */
public class MenuMineActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView nopay_menu;
    private TextView history_menu;
    // 滚动条图片
    private ImageView scrollbar;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    private SwipeToLoadLayout swipeToLoadLayout_nopay;
    private SwipeToLoadLayout swipeToLoadLayout_history;
    private RecyclerView mRecyclerView_nopay;
    private RecyclerView mRecyclerView_history;
    private ArrayList<PaymentItem.OrderInfo> HistorymenuItemList = null;
    private ArrayList<PaymentItem.OrderInfo> shopcarList = null;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private FoodMenuAdapter adapter;
    private RelativeLayout default_result_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_mine);
        initHead(R.mipmap.reset_back, "返回", "我的外卖", 0, "");
        initView();
        initData();
        initListener();
    }


    private void initView() {
        if (adapter == null) {
            adapter = new FoodMenuAdapter(MenuMineActivity.this);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager_menu);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.fragment_menu_nopay, null);
        View view2 = inflater.inflate(R.layout.fragment_menu_history, null);
        default_result_rl= (RelativeLayout) findViewById(R.id.default_result_rl);
        this.swipeToLoadLayout_nopay = (SwipeToLoadLayout) view1.findViewById(swipeToLoadLayout);
        this.mRecyclerView_nopay = (RecyclerView) view1.findViewById(R.id.swipe_target);
        this.swipeToLoadLayout_history = (SwipeToLoadLayout) view2.findViewById(swipeToLoadLayout);
        this.mRecyclerView_history = (RecyclerView) view2.findViewById(R.id.swipe_target);
        mRecyclerView_nopay.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView_history.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView_nopay.setAdapter(adapter);
        mRecyclerView_history.setAdapter(adapter);
        nopay_menu = (TextView) findViewById(R.id.nopay_menu);
        history_menu = (TextView) findViewById(R.id.history_menu);
        scrollbar = (ImageView) findViewById(R.id.scrollbar_menu);
        nopay_menu.setOnClickListener(this);
        history_menu.setOnClickListener(this);
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
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
            if (viewPager.getCurrentItem() == 0) {
                nopay_menu.setTextColor(getResources().getColor(R.color.main_red));
                history_menu.setTextColor(getResources().getColor(R.color.text_color));
                getMenu(PAGE, limit, "0");
            } else if (viewPager.getCurrentItem() == 1) {
                nopay_menu.setTextColor(getResources().getColor(R.color.text_color));
                history_menu.setTextColor(getResources().getColor(R.color.main_red));
                getMenu(PAGE, limit, "99");
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
        HistorymenuItemList = new ArrayList<>();
        shopcarList = new ArrayList<>();
        getMenu(PAGE, limit, "0");
    }

    /**
     * 订单外卖获取
     */
    private void getMenu(int page, int limit, final String type) {
        showProgress();
        String url = Config.URL + Config.GETORDERLISTS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("order_type", "2");
        params.put("status", "" + type);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<PaymentItem>() {


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        default_result_rl.setVisibility(View.VISIBLE);
                        hideProgress();
                        Logger.e("ShopCarActivity", "statusCode" + statusCode + "error_msg" + error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, PaymentItem response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            if (type.equals("0")) {
                                default_result_rl.setVisibility(View.GONE);
                                shopcarList = response.getData();
                                loadMenuNoPayItem(shopcarList);
                                if(shopcarList.size()==0){
                                    default_result_rl.setVisibility(View.VISIBLE);
                                }
                            } else if (type.equals("3")) {
                                HistorymenuItemList = response.getData();
                                loadMenuHistoryItem(HistorymenuItemList);
                                if(HistorymenuItemList.size()==0){
                                    default_result_rl.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            default_result_rl.setVisibility(View.VISIBLE);
                        }
                    }

                });
    }


    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuMineActivity.this.finish();
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
        adapter.setOnItemClickListener(new FoodMenuAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, PaymentItem.OrderInfo data) {
                if (data != null) {
                    Intent intent = new Intent(MenuMineActivity.this, OrderInfoActivity.class);
                    intent.putExtra("orderId", data.getOrder_id());
                    startActivity(intent);
                }
            }
        });
    }

    //获取展示数据
    private void loadMenuNoPayItem(final ArrayList<PaymentItem.OrderInfo> items) {
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

    //获取展示数据
    private void loadMenuHistoryItem(final ArrayList<PaymentItem.OrderInfo> items) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nopay_menu:
                viewPager.setCurrentItem(0);
                nopay_menu.setTextColor(getResources().getColor(R.color.main_red));
                history_menu.setTextColor(getResources().getColor(R.color.text_color));
                getMenu(PAGE, limit, "0");
                break;
            case R.id.history_menu:
                viewPager.setCurrentItem(1);
                nopay_menu.setTextColor(getResources().getColor(R.color.text_color));
                history_menu.setTextColor(getResources().getColor(R.color.main_red));
                getMenu(PAGE, limit, "99");
                break;
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        if (viewPager.getCurrentItem() == 0) {
            getMenu(PAGE, limit, "0");
            swipeToLoadLayout_nopay.setRefreshing(false);
        } else if (viewPager.getCurrentItem() == 1) {
            getMenu(PAGE, limit, "99");
            swipeToLoadLayout_history.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        if (viewPager.getCurrentItem() == 0) {
            getMenu(PAGE, limit, "0");
            swipeToLoadLayout_nopay.setLoadingMore(false);
        } else if (viewPager.getCurrentItem() == 1) {
            getMenu(PAGE, limit, "99");
            swipeToLoadLayout_history.setLoadingMore(false);
        }
    }

}
