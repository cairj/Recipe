package com.recipe.r.ui.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.UserInfo;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.mine.ChatListAdapter;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 聊天界面
 * 随后添加聊天的好友列表再跳转至本页面，目前版本就只支持同客服聊天
 */
public class ChatListActivity extends BaseActivity implements OnRefreshListener {
    //    private Fragment conversationListFragment;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ChatListAdapter adapter;
    private int PAGE = 1;
    private int limit = 3;
    private boolean isRefresh = true;
    private ArrayList<UserInfo.User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initHead(R.mipmap.reset_back, "返回", "客服列表", 0, "");
        initView();
        initData();
        initListener();
    }


    private void initView() {
//        conversationListFragment = new ConversationListFragment();
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        transaction.add(R.id.chat_fragment, conversationListFragment).show(conversationListFragment).commit();
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        adapter = new ChatListAdapter(ChatListActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        initRefresh(swipeToLoadLayout);
    }

    private void initData() {
        //默认初始化加载数据
        userList = new ArrayList<>();
        getUserInfo(PAGE, limit);
    }

    private void initListener() {
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatListActivity.this.finish();
            }
        });
        adapter.setOnItemClickListener(new ChatListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, UserInfo.User data) {
                //点击进入客服聊天
                Intent intent = new IntentBuilder(ChatListActivity.this)
                        .setServiceIMNumber(ConfigApp.HXUSERNAME + data.getUser_id())
                        .setTitleName(data.getUser_name())
                        .build();
                startActivity(intent);
            }
        });
    }

    /**
     * 获取
     *
     * @param page
     * @param limit
     */
    private void getUserInfo(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETSERVICELISTS;
        Map<String, String> params = new HashMap<>();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<UserInfo>() {

                    @Override
                    public void onSuccess(int statusCode, UserInfo response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        
                        if (status == 1) {
                            userList = response.getData();
                            loadMenuItem(userList);
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
    private void loadMenuItem(final ArrayList<UserInfo.User> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    adapter.updatelist(userList);
                } else {
                    adapter.append(userList);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getUserInfo(PAGE, limit);
        swipeToLoadLayout.setRefreshing(false);
    }
}