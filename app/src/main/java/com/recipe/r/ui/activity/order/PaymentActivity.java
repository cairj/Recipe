package com.recipe.r.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.PaymentItem;
import com.recipe.r.events.MessageEvent;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.order.PaymentAdapter;
import com.recipe.r.utils.AppSettings;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 待付款界面
 */
public class PaymentActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private PaymentAdapter adapter;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ArrayList<PaymentItem.OrderInfo> paymentList = null;
    private ImageView default_result;
    private String TYPE = "0";//默认为待付款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View content = LayoutInflater.from(this).inflate(R.layout.activity_payment, null);
        setContentView(content);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("type"))) {
            TYPE = getIntent().getStringExtra("type");
        }
        if (TYPE.equals("0")) {
            initHead(R.mipmap.reset_back, "返回", "待付款", 0, "");
        } else {
            initHead(R.mipmap.reset_back, "返回", "待收货", 0, "");
        }
        initView(content);
        initData();
        initListener();
    }

    private void initView(View content) {
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        default_result = (ImageView) findViewById(R.id.default_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PaymentAdapter(PaymentActivity.this, content, TYPE, mMyOkhttp);
        mRecyclerView.setAdapter(adapter);
        initRefresh(swipeToLoadLayout);
    }

    private void initData() {
        getpayment(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new PaymentAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, PaymentItem.OrderInfo data) {
                if (data != null) {
                    Intent intent = new Intent(PaymentActivity.this, OrderInfoActivity.class);
                    intent.putExtra("orderId", data.getOrder_id());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 获取待付款界面数据
     *
     * @param page
     * @param limit
     */
    private void getpayment(int page, int limit) {
        paymentList = new ArrayList<>();
        showProgress();
        String url = Config.URL + Config.GETORDERLISTS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("status", "0");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<PaymentItem>() {
                    @Override
                    public void onSuccess(int statusCode, PaymentItem response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            default_result.setVisibility(View.GONE);
                            paymentList = response.getData();
                            loadPaymentItem(paymentList);
                            if(paymentList.size()==0){
                                default_result.setVisibility(View.VISIBLE);
                            }
                        }else{
                            default_result.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        default_result.setVisibility(View.VISIBLE);
                    }
                });

    }


    //获取展示数据
    private void loadPaymentItem(final ArrayList<PaymentItem.OrderInfo> items) {
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
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getpayment(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(MessageEvent messageEvent) {
        //TODO 付款成功  刷新当前界面
        PAGE=1;
        getpayment(PAGE, limit);
    }
}
