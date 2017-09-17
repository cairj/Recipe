package com.recipe.r.ui.activity.home;

import android.content.Intent;
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
import com.recipe.r.entity.ShareItem;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.home.HotAdapter;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 古辛热点界面
 */
public class HotActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private HotAdapter adapter;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ArrayList<ShareItem.Sharedata> recommendList = null;
    private ImageView hot_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        setTranslucentStatus(R.color.main_red);
        initHead(R.mipmap.reset_back, "返回", "古辛热点", 0, "");
        SetHeadBackground(getResources().getColor(R.color.main_red));
        initView();
        initData();
        initListener();
    }


    private void initView() {
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        hot_image = (ImageView) findViewById(R.id.hot_image);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HotAdapter(HotActivity.this);
        mRecyclerView.setAdapter(adapter);
        initRefresh(swipeToLoadLayout);
    }

    private void initData() {
        getHotGoods(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new HotAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final ShareItem.Sharedata data) {
                Intent intent_detail = new Intent(HotActivity.this, HotDetailsActivity.class);
                intent_detail.putExtra("news_id", data.getNews_id());
                startActivity(intent_detail);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getHotGoods(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getHotGoods(PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    private void getHotGoods(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETSHARELIST;
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");//0，分享，1，专题
        params.put("option", "hot");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)

                .enqueue(new GsonResponseHandler<ShareItem>() {

                    @Override
                    public void onSuccess(int statusCode, ShareItem response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        
                        if (status == 1) {
                            recommendList = response.getData();
                            loadHotItem(recommendList);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        hot_image.setVisibility(View.VISIBLE);
                    }
                });
    }

    //获取展示数据
    private void loadHotItem(final ArrayList<ShareItem.Sharedata> items) {
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
}
