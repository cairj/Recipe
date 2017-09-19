package com.recipe.r.ui.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.CollectionItem;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.mine.CollectionAdapter;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的收藏
 */
public class CollectionActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private CollectionAdapter adapter;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ArrayList<CollectionItem.Collecyion> paymentList = null;
    private ImageView default_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initHead(R.mipmap.reset_back, "返回", "我的收藏", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        default_result = (ImageView) findViewById(R.id.default_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CollectionAdapter(CollectionActivity.this,mMyOkhttp);
        mRecyclerView.setAdapter(adapter);
        initRefresh(swipeToLoadLayout);
    }

    private void initData() {
        getcollection(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new CollectionAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, CollectionItem.Collecyion data) {
                if (data != null) {
                    ToastUtil.show(CollectionActivity.this, data.getGoods_name(), 500);
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
    private void getcollection(int page, int limit) {
        paymentList = new ArrayList<>();
        showProgress();
        String url = Config.URL + Config.MYCOLLEXTS;
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
                .enqueue(new GsonResponseHandler<CollectionItem>() {

                             @Override
                             public void onSuccess(int statusCode, CollectionItem response) {
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
                                 }
                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {
                                 hideProgress();
                                 default_result.setVisibility(View.VISIBLE);
                             }
                         }
                );

    }


    //获取展示数据
    private void loadPaymentItem(final ArrayList<CollectionItem.Collecyion> items) {
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
        getcollection(PAGE, limit);
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getcollection(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }
}


