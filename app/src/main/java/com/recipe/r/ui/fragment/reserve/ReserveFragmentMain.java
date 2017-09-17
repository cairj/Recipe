package com.recipe.r.ui.fragment.reserve;/**
 * 作者：Administrator on 2017/6/9 14:51
 * 功能:@描述
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.TableItem;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.adapter.head.GirdDropDownAdapter;
import com.recipe.r.ui.adapter.head.ListDropDownAdapter;
import com.recipe.r.ui.adapter.table.TableAdapter;
import com.recipe.r.ui.fragment.base.BaseFragment;
import com.recipe.r.ui.widget.SpaceItemDecoration;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.UserIsLogin;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017
 * 06
 * 2017/6/9
 * wangxiaoer
 * 功能描述：订桌界面
 **/
@SuppressLint("ValidFragment")
public class ReserveFragmentMain extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private Activity context;
    public static final String TAG = "ReserveFragmentMain";
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    ArrayList<TableItem.Table> TableItemList = null;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private TableAdapter table_adapter;
    private DropDownMenu mDropDownMenu;
    private String headers[] = {"所在地", "店铺", "包厢"};
    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter ageAdapter;
    private ListDropDownAdapter sexAdapter;
    private ArrayList<String> citys = new ArrayList<>();
    private ArrayList<String> shop = new ArrayList<>();
    private ArrayList<String> table = new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();
    private ListView cityView;
    private ListView shopView;
    private ListView tableView;
    private ImageView no_login_iv;


    public ReserveFragmentMain(Activity mainActivity) {
        this.context = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        View view = inflater.inflate(R.layout.fragment_reserve_main, container, false);
        initHead(view, 0, "", "快捷订桌", 0, "");
        initView(view);
        initData();
        initListener();
        return view;
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
        if (table_adapter == null) {
            table_adapter = new TableAdapter(context);
        }
        LayoutInflater inflater = context.getLayoutInflater();
        View view_recycler = inflater.inflate(R.layout.fragment_integral_detail, null);
        this.mRecyclerView = (RecyclerView) view_recycler.findViewById(R.id.swipe_target);
        this.swipeToLoadLayout = (SwipeToLoadLayout) view_recycler.findViewById(R.id.swipeToLoadLayout);
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu_table);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mRecyclerView.setAdapter(table_adapter);
        initHeadPop(view_recycler);
        initRefresh(swipeToLoadLayout, view);
    }

    /**
     * 初始化头部筛选器
     */
    private void initHeadPop(View view) {
//        citys.add("福建漳州市");
//        citys.add("芗城区");
//        citys.add("龙文区");
//        citys.add("龙海市");
//        citys.add("平和县");
//        citys.add("南靖县");
//        citys.add("诏安县");
//        citys.add("漳浦县");
//        citys.add("华安县");
//        citys.add("东山县");
//        citys.add("长泰县");
        citys.add("云霄县");
        //init city menu
        cityView = new ListView(context);
        cityAdapter = new GirdDropDownAdapter(context, citys);
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        //init age menu
        shopView = new ListView(context);
        shopView.setDividerHeight(0);
        ageAdapter = new ListDropDownAdapter(context, shop);
        shopView.setAdapter(ageAdapter);

        //init sex menu
        tableView = new ListView(context);
        tableView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(context, table);
        tableView.setAdapter(sexAdapter);
        //init popupViews
        popupViews.add(cityView);
        popupViews.add(shopView);
        popupViews.add(tableView);
        //init dropdownview
        headers[0]="所在地:"+ citys.get(0);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,view);
    }

    /**
     * 加载数据
     */
    private void initData() {
        getTableData(PAGE, limit, "0");
    }

    private void initListener() {
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        //add item click event
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(/*position == 0 ? headers[0] :*/"所在地:"+ citys.get(position));
                mDropDownMenu.closeMenu();
            }
        });

        shopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(/*position == 0 ? headers[1] :*/ shop.get(position));
                mDropDownMenu.closeMenu();
            }
        });

        tableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexAdapter.setCheckItem(position);
                getTableData(1, 10, "" + (position + 1));
                mDropDownMenu.setTabText(/*position == 0 ? headers[2] :*/ table.get(position));
                mDropDownMenu.closeMenu();
            }
        });
        no_login_iv.setOnClickListener(this);
    }

//    /**
//     * 调用接口获取餐桌信息
//     */
//    private void getTableInfo(String city, int page, int limit) {
//        showProgress();
//        String url = Config.URL + Config.GETTABLES;
//        Map<String, String> params = new HashMap<>();
//        params.put("page", "" + page);
//        params.put("limit", "" + limit);
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        super.onSuccess(statusCode, response);
//                        hideProgress();
//                        int status = 0;
//                        try {
//                            status = response.getInt("status");
//                            String info = response.getString("info");
//                            
//                            if (status == 1) {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onProgress(long currentBytes, long totalBytes) {
//                        super.onProgress(currentBytes, totalBytes);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        hideProgress();
//                        Logger.e(TAG, "statusCode" + statusCode + "error_msg" + error_msg);
//                    }
//                });
//    }

    /**
     * 获取订桌信息接口
     *
     * @param page
     * @param limit
     */
    private void getTableData(int page, int limit, String table_type) {
        showProgress();
        String url = Config.URL + Config.GETTABLES;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
        params.put("table_type", table_type);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<TableItem>() {


                    @Override
                    public void onSuccess(int statusCode, TableItem response) {
                        TableItemList = new ArrayList<>();
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            TableItemList = response.getData();
//                            for (int i = 0; i < TableItemList.size(); i++) {
                            if (TableItemList.size() > 0) {
                                shop.add(TableItemList.get(0).getShop_name());
                            }
//                            }
                            for (int i = 0; i < TableItemList.size(); i++) {
                                table.add(TableItemList.get(i).getTable_name());
                            }
                            loadTableItem(TableItemList);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e(TAG, "statusCode" + statusCode + "error_msg" + error_msg);
                    }
                });
    }

    private void loadTableItem(final ArrayList<TableItem.Table> listitem) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    table_adapter.updatelist(listitem);
                } else {
                    table_adapter.append(listitem);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getTableData(PAGE, limit, "0");
        swipeToLoadLayout.setRefreshing(false);

    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getTableData(PAGE, limit, "0");
        swipeToLoadLayout.setLoadingMore(false);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            System.out.println("不可见");
            if (mDropDownMenu.isShowing()) {
                mDropDownMenu.closeMenu();
            }
        } else {
            System.out.println("当前可见");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_login_iv:
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
        }
    }
}
