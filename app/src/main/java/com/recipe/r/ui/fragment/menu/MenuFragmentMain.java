package com.recipe.r.ui.fragment.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.menu.ShopCarActivity;
import com.recipe.r.ui.adapter.menu.MenuRecyclerViewAdapter;
import com.recipe.r.ui.dialog.LogDialog;
import com.recipe.r.ui.fragment.base.BaseFragment;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.UserIsLogin;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 2017/6/9
 * wangxiaoer
 * 功能描述：菜单界面
 **/
@SuppressLint("ValidFragment")
public class MenuFragmentMain extends BaseFragment implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, MenuRecyclerViewAdapter.OnShopCartGoodsChangeListener {
    private MainActivity context;
    private TextView comprehensive_ranking, new_product_attempt, popular_selling;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private MenuRecyclerViewAdapter adapter;
    //控制侧滑菜单
    private DrawerLayout drawer;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ArrayList<NewGoods.Goods> recommendList = null;
    private ImageView no_login_iv;
    //搜索栏
    private EditText toolbar_et_search_menu;

    public MenuFragmentMain(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    private Button rb1, rb2, rb3, rb4, rb5;

    private String TYPE = "new";
    private LogDialog dialog;
    private LinearLayout menu_search_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        View view = inflater.inflate(R.layout.fragment_menu_main, container, false);
        initHead(view, 0, "", "精选菜单", R.mipmap.shop_car, "点菜单");
        initView(view);
        initData();
        initListener(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            System.out.println("不可见");
            if (drawer != null) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    //关闭菜单;
                    drawer.closeDrawer(Gravity.RIGHT);
                }
            }
        }
    }

    private void initView(View view) {
        no_login_iv = (ImageView) view.findViewById(R.id.no_login_iv);
        if (UserIsLogin.isShowLogin(context)) {
            //显示
            no_login_iv.setVisibility(View.VISIBLE);
        } else {
            //不显示
            no_login_iv.setVisibility(View.GONE);
        }
        rb1 = (Button) view.findViewById(R.id.rb1);
        rb2 = (Button) view.findViewById(R.id.rb2);
        rb3 = (Button) view.findViewById(R.id.rb3);
        rb4 = (Button) view.findViewById(R.id.rb4);
        rb5 = (Button) view.findViewById(R.id.rb5);
        comprehensive_ranking = (TextView) view.findViewById(R.id.comprehensive_ranking);
        new_product_attempt = (TextView) view.findViewById(R.id.new_product_attempt);
        popular_selling = (TextView) view.findViewById(R.id.popular_selling);
        toolbar_et_search_menu = (EditText) view.findViewById(R.id.toolbar_et_search_menu);
        menu_search_ll = (LinearLayout) view.findViewById(R.id.menu_search_ll);
        this.swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MenuRecyclerViewAdapter(context);
        mRecyclerView.setAdapter(adapter);
        adapter.setShopCart(getShopView());
        adapter.setOnShopCartGoodsChangeListener(this);
        initRefresh(swipeToLoadLayout, view);
    }


    private void initData() {
        //默认初始化加载数据
        getMenuGoods(TYPE, PAGE, limit);
    }

    private void initListener(View view) {
        getRightLinearlayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopCarActivity.class);
                startActivity(intent);
                adapter.setBuyNum();//TODO 清空购物车
                adapter.initGoodsNum();//TODO 初始化购物数量
                adapter.notifyDataSetChanged();
            }
        });
        comprehensive_ranking.setOnClickListener(this);
        new_product_attempt.setOnClickListener(this);
        popular_selling.setOnClickListener(this);
        menu_search_ll.setOnClickListener(this);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
//        adapter.setOnItemClickListener(new MenuRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, final NewGoods.Goods data) {
//                getGoodsDetails(data.getGoods_id());
//            }
//        });
        no_login_iv.setOnClickListener(this);
        initDoClick(view);
        drawer.addDrawerListener(drawerListener);
    }

    /**
     * 监听侧滑菜单隐藏
     */
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            context.hideMainBottom(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            context.hideMainBottom(false);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    /**
     * 设置每一个人资料的点击事件
     */
    public void initDoClick(View myView) {
        setClickListener(myView, R.id.toolbar_et_search_menu);
        setClickListener(myView, R.id.rb1);
        setClickListener(myView, R.id.rb2);
        setClickListener(myView, R.id.rb3);
        setClickListener(myView, R.id.rb4);
        setClickListener(myView, R.id.rb5);
        setClickListener(myView, R.id.rb6);
        setClickListener(myView, R.id.rb7);
        setClickListener(myView, R.id.rb8);
        setClickListener(myView, R.id.reset_menu_btn);
        setClickListener(myView, R.id.finish_menu_btn);
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
            case R.id.comprehensive_ranking:
                //综合排序
                PAGE = 1;
                TYPE = "new";
                comprehensive_ranking.setTextColor(context.getResources().getColor(R.color.main_red));
                new_product_attempt.setTextColor(context.getResources().getColor(R.color.text_color));
                popular_selling.setTextColor(context.getResources().getColor(R.color.text_color));
                getMenuGoods(TYPE, PAGE, limit);
                break;
            case R.id.new_product_attempt:
                //最新
                PAGE = 1;
                TYPE = "new";
                comprehensive_ranking.setTextColor(context.getResources().getColor(R.color.text_color));
                new_product_attempt.setTextColor(context.getResources().getColor(R.color.main_red));
                popular_selling.setTextColor(context.getResources().getColor(R.color.text_color));
                getMenuGoods(TYPE, PAGE, limit);
                break;
            case R.id.popular_selling:
                //热销
                PAGE = 1;
                TYPE = "hot";
                comprehensive_ranking.setTextColor(context.getResources().getColor(R.color.text_color));
                new_product_attempt.setTextColor(context.getResources().getColor(R.color.text_color));
                popular_selling.setTextColor(context.getResources().getColor(R.color.main_red));
                getMenuGoods("hot", PAGE, limit);
                break;
            case R.id.menu_search_ll:
                context.hideMainBottom(true);
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.no_login_iv:
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
            //关于搜索
            case R.id.rb1:
                resetColor();
                toolbar_et_search_menu.setText(R.string.chinese_food);
                rb1.setBackgroundColor(getResources().getColor(R.color.main_red));
                rb1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.rb2:
                resetColor();
                toolbar_et_search_menu.setText(R.string.soup);
                rb2.setBackgroundColor(getResources().getColor(R.color.main_red));
                rb2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.rb3:
                resetColor();
                toolbar_et_search_menu.setText(R.string.sashimi);
                rb3.setBackgroundColor(getResources().getColor(R.color.main_red));
                rb3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.rb4:
                resetColor();
                toolbar_et_search_menu.setText(R.string.iron_plate);
                rb4.setBackgroundColor(getResources().getColor(R.color.main_red));
                rb4.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.rb5:
                resetColor();
                rb5.setBackgroundColor(getResources().getColor(R.color.main_red));
                rb5.setTextColor(getResources().getColor(R.color.white));
                toolbar_et_search_menu.setText(context.getResources().getString(R.string.wine));
                break;
//            case R.id.rb6:
//                toolbar_et_search_menu.setText(context.getResources().getString(R.string.japan));
//
//                break;
//            case R.id.rb7:
//                toolbar_et_search_menu.setText(context.getResources().getString(R.string.vegetables));
//
//                break;
//            case R.id.rb8:
//                toolbar_et_search_menu.setText(context.getResources().getString(R.string.other));
//
//                break;
            case R.id.reset_menu_btn:
                //重置
                resetColor();
                toolbar_et_search_menu.setText("");
                break;
            case R.id.finish_menu_btn:
                //完成
                if (TextUtils.isEmpty(toolbar_et_search_menu.getText().toString())) {
                    ToastUtil.show(context, "请输入搜索内容", 500);
                    return;
                }
                //搜索
                isRefresh = true;
                PAGE = 1;
                resetColor();
                getHotData(toolbar_et_search_menu.getText().toString());
                break;
            case R.id.toolbar_et_search_menu:
                resetColor();
                toolbar_et_search_menu.setText("");
                break;
        }
    }

    /**
     * 重置颜色
     */
    private void resetColor() {
        rb1.setBackgroundResource(R.mipmap.classification_disclick);
        rb1.setTextColor(getResources().getColor(R.color.text_color));
        rb2.setBackgroundResource(R.mipmap.classification_disclick);
        rb2.setTextColor(getResources().getColor(R.color.text_color));
        rb3.setBackgroundResource(R.mipmap.classification_disclick);
        rb3.setTextColor(getResources().getColor(R.color.text_color));
        rb4.setBackgroundResource(R.mipmap.classification_disclick);
        rb4.setTextColor(getResources().getColor(R.color.text_color));
        rb5.setBackgroundResource(R.mipmap.classification_disclick);
        rb5.setTextColor(getResources().getColor(R.color.text_color));

    }

    /**
     * 搜索热门商品接口数据
     */
    private void getHotData(String keyword) {
        showProgress();
        String url = Config.URL + Config.SEARCHGOODS;
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", "" + PAGE);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<NewGoods>() {

                    @Override
                    public void onSuccess(int statusCode, NewGoods response) {
                        hideProgress();
                        //关闭菜单
                        resetColor();
                        drawer.closeDrawer(Gravity.RIGHT);
                        toolbar_et_search_menu.setText("");
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {

                            recommendList = response.getData();
                            loadMenuItem(1, recommendList);
                        } else {
                            ToastUtil.show(context, "无相关搜索信息，搜索失败", 500);
                            recommendList = null;
                            loadMenuItem(1, recommendList);
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

    //获取展示数据
    private void loadMenuItem(int type, final ArrayList<NewGoods.Goods> items) {
        context.runOnUiThread(new Runnable() {
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
        getMenuGoods(TYPE, PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getMenuGoods(TYPE, PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }
//
//    /**
//     * 获取商品详情
//     */
//    private void getGoodsDetails(final String GOODID) {
//        showProgress();
//        String url = Config.URL + Config.GETGOODSDETAILS;
//        Map<String, String> params = new HashMap<>();
//        params.put("goods_id", GOODID);
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new GsonResponseHandler<MenuGoods>() {
//
//
//                    @Override
//                    public void onSuccess(int statusCode, final MenuGoods response) {
//                        hideProgress();
//                        int status = response.getStatus();
//                        String info = response.getInfo();
//
//                        if (status == 1) {
//                            dialog = new LogDialog(context);
//                            dialog.setTitle(response.getData().getGoods_name());
//                            dialog.setContent(response.getData().getGoods_brief());
//                            dialog.setBannerViews(Config.IMAGE_URL + response.getData().getGoods_image());
//                            dialog.show();
//                            /**
//                             * 设置收藏，暂时不用
//                             */
////                            if(response.getData().getStatus().equals("0")){
////                                //未收藏
////                                dialog.setLikeViews(R.mipmap.love);
////                            }else{
////                                //已收藏
////                                dialog.setLikeViews(R.mipmap.love);
////                            }
////
////                            if (dialog.LikeView() != null) {
////                                dialog.LikeView().setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View view) {
////                                        setCollect(response.getData().getGoods_id());
////                                    }
////                                });
////                            }
//                            dialog.setOnItemClickListener(new LogDialog.OnItemClickListener() {
//                                @Override
//                                public void setOnItemClick(View v) {
//                                    switch (v.getId()) {
//                                        case R.id.addcar_menu_dialog:
//                                            //添加购物车
//                                            addShopCart(GOODID, "1");
//                                            dialog.dismiss();
//                                            break;
//                                        case R.id.rb_taste1:
//                                            dialog.setClickRb(0);
//                                            break;
//                                        case R.id.rb_taste2:
//                                            dialog.setClickRb(1);
//                                            break;
//                                        case R.id.rb_taste3:
//                                            dialog.setClickRb(2);
//                                            break;
//                                        case R.id.collection_dialog:
//                                            //先要判断一次是否为收藏
//                                            if (response.getData().getColleted() == null) {
//                                                //添加收藏
//                                                setCollect(GOODID, "1");
//                                                dialog.getCollectionText().setText("取消收藏");
//                                                dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
//
//                                            } else {
//                                                if (response.getData().getColleted().equals("0")) {
//                                                    //添加收藏
//                                                    setCollect(GOODID, "1");
//                                                    dialog.getCollectionText().setText("取消收藏");
//                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
//                                                } else if (response.getData().getColleted().equals("1")) {
//                                                    //取消收藏
//                                                    setCollect(GOODID, "0");
//                                                    dialog.getCollectionText().setText("收藏");
//                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_bule));
//                                                }
//                                            }
//                                            break;
//                                    }
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//
//                    }
//                });
//    }


//    /**
//     * 添加购物车方法
//     */
//    private void addShopCart(String goodId, String goods_number) {
//        showProgress();
//        String url = Config.URL + Config.ADDCART;
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
//        params.put("device", "android");
//        params.put("goods_id", goodId);
//        params.put("goods_number", goods_number);
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        hideProgress();
//                        try {
//                            int status = response.getInt("status");
//                            String info = response.getString("info");
//
//                            if (status == 1) {
//                                if (dialog != null) {
//                                    dialog.dismiss();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        Logger.e("LoginActivity", error_msg);
//                    }
//                });
//    }

    /**
     * 获取菜谱列表
     *
     * @param type
     * @param page
     * @param limit
     */
    private void getMenuGoods(String type, int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETLISTS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("option", type);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)

                .enqueue(new GsonResponseHandler<NewGoods>() {

                    @Override
                    public void onSuccess(int statusCode, NewGoods response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            recommendList = response.getData();
                            loadMenuItem(1, recommendList);
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

//    /**
//     * 设置产品收藏
//     */
//    private void setCollect(String goodId, final String type) {
//        showProgress();
//        String url = Config.URL + Config.COLLECT;
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
//        params.put("device", "android");
//        params.put("goods_id", goodId);
//        params.put("type", type);//0，取消收藏，1，收藏，默认为1
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        hideProgress();
//                        try {
//                            int status = response.getInt("status");
//                            String info = response.getString("info");
//
//                            if (status == 1) {
//                                if (dialog != null) {
//                                    if (type.equals("1")) {
//                                        dialog.getCollectionText().setText("取消收藏");
//                                    } else {
//                                        dialog.getCollectionText().setText("收藏");
//                                    }
//                                    dialog.dismiss();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        Logger.e("LoginActivity", error_msg);
//                    }
//                });
//    }


    @Override
    public void onNumChange() {

    }
}
