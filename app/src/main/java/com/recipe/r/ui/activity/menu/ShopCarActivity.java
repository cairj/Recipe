package com.recipe.r.ui.activity.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.address.AdressActivity;
import com.recipe.r.ui.activity.order.OrderDetailActivity;
import com.recipe.r.ui.adapter.menu.ShopCarAdapter;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车界面
 */
public class ShopCarActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, ShopCarAdapter.OnShopCartGoodsChangeListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ShopCarAdapter adapter;
    private TextView shopping_totalMoney_Tv;//总价
    private TextView Settlement_tv;//支付
    private LinearLayout gwc_jiesuan;
    private ArrayList<NewGoods.Goods> shopcarList = null;
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        initHead(R.mipmap.reset_back, "返回", "购物车", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        gwc_jiesuan = (LinearLayout) findViewById(R.id.gwc_jiesuan);
        shopping_totalMoney_Tv = (TextView) findViewById(R.id.shopping_totalMoney_Tv);
        Settlement_tv = (TextView) findViewById(R.id.Settlement_tv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopCarAdapter(ShopCarActivity.this);
        mRecyclerView.setAdapter(adapter);
        adapter.setShopCart(gwc_jiesuan, shopping_totalMoney_Tv);
        adapter.setOnShopCartGoodsChangeListener(this);
        initRefresh(swipeToLoadLayout);
    }

    private void initData() {
        shopcarList = new ArrayList<>();
        getShop(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopCarActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        Settlement_tv.setOnClickListener(this);
    }


    //获取购物车数据
    private void getShop(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETSHOPCAR;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<NewGoods>() {


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e("ShopCarActivity", "statusCode" + statusCode + "error_msg" + error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, NewGoods response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        
                        if (status == 1) {
                            shopcarList = response.getData();
                            if (isFirst) {
                                double total_price = 0;
                                for (int i = 0; i < shopcarList.size(); i++) {
                                    total_price = total_price + Double.parseDouble(shopcarList.get(i).getShop_price()) * shopcarList.get(i).getGoods_number();
                                }
                                adapter.setFirstShop(total_price);
                                loadShareItem(shopcarList);
                                adapter.changeShopCart();
                                isFirst = false;
                            } else {
                                loadShareItem(shopcarList);
                            }
                        }
                    }

                });
    }


    //获取展示数据,加载分享圈数据
    private void loadShareItem(final ArrayList<NewGoods.Goods> items) {
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
        getShop(PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getShop(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onNumChange() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Settlement_tv:
                //点击付款
                if (TextUtils.isEmpty(AppSettings.getPrefString(context, ConfigApp.ADDRID, "")) || AppSettings.getPrefString(context, ConfigApp.ADDRID, "").equals("0")) {
                    new AlertDialog(ShopCarActivity.this).builder()
                            .setTitle("提示").setMsg("您未设置默认地址,是否去设置默认?")
                            .setPositiveButton("确认", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ShopCarActivity.this, AdressActivity.class);
                                    startActivity(intent);

                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.show(context, "未设置地址，无法生成订单", 100);
                            return;
                        }
                    }).show();
                } else {
                    //TODO 跳转确认下单
                    Intent intent = new Intent(ShopCarActivity.this, OrderDetailActivity.class);
                    startActivity(intent);
                    ShopCarActivity.this.finish();
                }
                break;
        }
    }

}
