package com.recipe.r.ui.activity.search;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.db.SearchDbUtil;
import com.recipe.r.entity.MenuGoods;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.entity.SearchHistory;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.dialog.LogDialog;
import com.recipe.r.ui.dialog.MyAlertDialog;
import com.recipe.r.ui.widget.DividerItemDecoration;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 搜索界面
 * 搜索商品,热门商品，历史记录
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private SearchView searchView;
    private RecyclerView recyclerView1, recyclerView2, search_content_recyclerView;
    private ScrollView search_content_sv;
    private TextView search_cancelTv;
    private Button search_clear_button;
    private List<String> local_searchList;
    private SearchDbUtil searchDbUtil;
    private String Num = "4";
    private String HotToken;
    private MyAlertDialog alertDialog;
    private int PAGE = 1;
    private int limit = 10;
    private LogDialog dialog;
    private LinearLayout search_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search_return = (LinearLayout) findViewById(R.id.search_return);
        alertDialog = new MyAlertDialog(this, R.style.NormalAlertDialogStyle);
        searchView = (SearchView) findViewById(R.id.search_searchView);
        search_content_sv = (ScrollView) findViewById(R.id.search_content_sv);
        search_cancelTv = (TextView) findViewById(R.id.search_cancelTv);
        search_clear_button = (Button) findViewById(R.id.search_clear_button);
        setSearchView(searchView);
        recyclerView1 = (RecyclerView) findViewById(R.id.search_RecyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2 = (RecyclerView) findViewById(R.id.search_RecyclerView2);
        //recyclerView2不滚动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setNestedScrollingEnabled(false);
        search_content_recyclerView = (RecyclerView) findViewById(R.id.search_content_recyclerView);
        search_content_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        search_content_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {
        local_searchList = new ArrayList<>();
        //获取历史搜索数据
        setHistoryData();
    }

    private void setHistoryData() {
        searchDbUtil = new SearchDbUtil(this);
        ArrayList<SearchHistory> searchHistories = searchDbUtil.queryData("");
        local_searchList.clear();
        for (int i = 0; i < searchHistories.size(); i++) {
            String searchHistory = searchHistories.get(i).getInput();
            local_searchList.add(searchHistory);
        }
        final CommonAdapter commonAdapter = new CommonAdapter<String>(this, R.layout.item_searchview_recycle, local_searchList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.search_RecyclerView_item_tv, s);
            }
        };
        recyclerView2.setAdapter(commonAdapter);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //历史记录点击搜索
//                Intent intent = new Intent(SearchActivity.this, AllSerchActivity.class);
//                intent.putExtra("title", local_searchList.get(position));
//                intent.putExtra("CatId", 0);
//                startActivity(intent);
                PAGE = 1;
                limit = 10;
                getHotData(PAGE, limit, (String) commonAdapter.getDatas().get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 搜索热门商品接口数据
     */
    private void getHotData(int page, int limit, String keyword) {
        showProgress();
        String url = Config.URL + Config.SEARCHGOODS;
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<NewGoods>() {

                    @Override
                    public void onSuccess(int statusCode, final NewGoods response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            CommonAdapter commonAdapter = new CommonAdapter<NewGoods.Goods>(SearchActivity.this, R.layout.item_searchview_search_recycle, response.getData()) {

                                @Override
                                protected void convert(ViewHolder holder, NewGoods.Goods goods, int position) {
                                    holder.setText(R.id.search_RecyclerView_item_tv, goods.getGoods_name());
                                }
                            };
                            recyclerView1.setAdapter(commonAdapter);
                            commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    if (response.getData().get(position).getGoods_name() != null) {
                                        searchDbUtil.insertData("", response.getData().get(position).getGoods_name());
                                        getGoodsDetails(response.getData().get(position).getGoods_id());
                                    } else {
                                        ToastUtil.show(SearchActivity.this, "未搜索到相关数据", 100);
                                    }
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 搜索热门商品接口数据
     */
    private void getSearchData(int page, int limit, String keyword) {
        showProgress();
        String url = Config.URL + Config.SEARCHGOODS;
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<NewGoods>() {

                    @Override
                    public void onSuccess(int statusCode, final NewGoods response) {
                        hideProgress();
                        int status = response.getStatus();
                        if (status == 1) {

                            CommonAdapter commonAdapter = new CommonAdapter<NewGoods.Goods>(SearchActivity.this, R.layout.item_searchview_hot_recycle, response.getData()) {

                                @Override
                                protected void convert(ViewHolder holder, NewGoods.Goods goods, int position) {
                                    holder.setText(R.id.search_hot_RecyclerView_item_tv, goods.getGoods_name());
                                    if (TextUtils.isEmpty(goods.getGoods_brief())) {
                                        holder.setText(R.id.search_hot_descripttion_tv_menu, "商品简介:暂无简介");
                                    } else {
                                        holder.setText(R.id.search_hot_descripttion_tv_menu, goods.getGoods_brief());
                                    }
                                    holder.setText(R.id.search_hot_price_tv_menu, "价格:¥" + goods.getShop_price());
                                    holder.setText(R.id.search_hot_sellnumber_tv_menu, "已售出:" + goods.getSold_num() + "份");
                                    ShowImageUtils.showImageView(context, R.mipmap.logo, Config.IMAGE_URL + goods.getGoods_image(), WeakImageViewUtil.getImageView((ImageView) holder.getView(R.id.search_hot_food_iv_menu)));
                                }
                            };

                            search_content_recyclerView.setAdapter(commonAdapter);
                            commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    if (response.getData().get(position).getGoods_name() != null) {
                                        searchDbUtil.insertData("", response.getData().get(position).getGoods_name());
                                        getGoodsDetails(response.getData().get(position).getGoods_id());
                                    } else {
                                        ToastUtil.show(SearchActivity.this, "未搜索到相关数据", 100);
                                    }
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetails(final String GOODID) {
        showProgress();
        String url = Config.URL + Config.GETGOODSDETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", GOODID);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<MenuGoods>() {


                    @Override
                    public void onSuccess(int statusCode, final MenuGoods response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        
                        if (status == 1) {
                            dialog = new LogDialog(context);
                            dialog.setTitle(response.getData().getGoods_name());
                            dialog.setContent(response.getData().getGoods_brief());
                            dialog.setBannerViews(Config.IMAGE_URL + response.getData().getGoods_image());
                            dialog.show();
                            dialog.setOnItemClickListener(new LogDialog.OnItemClickListener() {
                                @Override
                                public void setOnItemClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.addcar_menu_dialog:
                                            //添加购物车
                                            addShopCart(GOODID, "1");
                                            dialog.dismiss();
                                            break;
                                        case R.id.rb_taste1:
                                            dialog.setClickRb(0);
                                            break;
                                        case R.id.rb_taste2:
                                            dialog.setClickRb(1);
                                            break;
                                        case R.id.rb_taste3:
                                            dialog.setClickRb(2);
                                            break;
                                        case R.id.collection_dialog:
                                            //先要判断一次是否为收藏
                                            if (response.getData().getColleted() == null) {
                                                //添加收藏
                                                setCollect(GOODID, "1");
                                                dialog.getCollectionText().setText("收藏");
                                                dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));

                                            } else {
                                                if (response.getData().getColleted().equals("0")) {
                                                    //添加收藏
                                                    setCollect(GOODID, "1");
                                                    dialog.getCollectionText().setText("收藏");
                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
                                                } else if (response.getData().getColleted().equals("1")) {
                                                    //取消收藏
                                                    setCollect(GOODID, "0");
                                                    dialog.getCollectionText().setText("取消收藏");
                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_bule));
                                                }
                                            }
                                            break;
                                    }
                                }
                            });
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

    /**
     * 设置产品收藏
     */
    private void setCollect(String goodId, String type) {
        showProgress();
        String url = Config.URL + Config.COLLECT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("goods_id", goodId);
        params.put("type", type);//0，取消收藏，1，收藏，默认为1
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            
                            if (status == 1) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 添加购物车方法
     */
    private void addShopCart(String goodId, String goods_number) {
        showProgress();
        String url = Config.URL + Config.ADDCART;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("goods_id", goodId);
        params.put("goods_number", goods_number);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            
                            if (status == 1) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    private void initListener() {
        search_cancelTv.setOnClickListener(this);
        search_clear_button.setOnClickListener(this);
        searchView.setOnQueryTextListener(this);
        search_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
    }

    /**
     * 单击搜索按钮时激发该方法
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals("")) {
            searchDbUtil.insertData("", query);
            searchView.setIconified(true);
//            Intent intent = new Intent(SearchActivity.this, AllSerchActivity.class);
//            intent.putExtra("title", query);
//            startActivity(intent);
//            getHotData(PAGE, limit, query);
            return true;
        }
        return false;
    }

    private String input = "";

    /**
     * 用户输入字符时激发该方法
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null && newText.length() > 0) {
            input = newText;
            search_content_recyclerView.setVisibility(View.VISIBLE);
            search_content_sv.setVisibility(View.GONE);
            //执行相应的查询动作
            try {
                schedule(new SearchTipThread(newText), 500);
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }
        } else {
            search_content_recyclerView.setAdapter(null);
            search_content_recyclerView.setVisibility(View.GONE);
            search_content_sv.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private InputMethodManager inputMethodManager;

    //隐藏软键盘
    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = this.getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }

    class SearchTipThread implements Runnable {

        private String newText;

        public SearchTipThread(String newText) {
            this.newText = newText;
        }

        public void run() {
            // keep only one thread to load current search tip, u can get data from network here
            if (newText != null && newText.equals(input)) {
                //获取热门搜索的数据
                getSearchData(PAGE, limit, newText);
            }
        }
    }

    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
        return scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    //设置searchview样式
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSearchView(final SearchView searchView) {
        if (searchView != null) {
            //获取最前面的搜索图标ImageView的id
            int imgId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            //获取ImageView
            ImageView searchButton = (ImageView) searchView.findViewById(imgId);
            //设置图片
            searchButton.setImageResource(R.mipmap.search_gray);
            //不使用默认
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("搜索商品");
            //设置嵌套有searchicon和searchtext的ll的id
            int search_edit_ll_id = searchView.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
            LinearLayout search_edit_ll = (LinearLayout) searchView.findViewById(search_edit_ll_id);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(0, 0, 0, 0);
            search_edit_ll.setLayoutParams(lp);
            //获取嵌套searchtext的ll的id
            int search_plate_ll_id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
            LinearLayout search_plate_ll = (LinearLayout) searchView.findViewById(search_plate_ll_id);
            search_plate_ll.setBackground(getResources().getDrawable(R.drawable.shape_search_ll_bg));
            //获取到TextView的ID
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            //获取到TextView的控件
            TextView textView = (TextView) searchView.findViewById(id);
            textView.setPadding(0, 20, 0, 0);
            //设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            //设置字体颜色
            textView.setTextColor(getResources().getColor(R.color.text_color));
            //设置提示文字颜色
            textView.setHintTextColor(getResources().getColor(R.color.gray));
            //获取到close的控件id...search_close_btn
            int clodeid = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            ImageView imageView = (ImageView) searchView.findViewById(clodeid);
            imageView.setImageResource(R.mipmap.close_search);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancelTv:
                this.finish();
                break;
            case R.id.search_clear_button:
                alertDialog.yesAndNo("", "是否确定删除历史记录?", getResources().getString(R.string.sure_str), getResources().getString(R.string.cancel))
                        .yes(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchDbUtil.deletaAll("");
                                setHistoryData();
                                alertDialog.dismiss();
                            }
                        })
                        .no(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideSoftInput();
        setHistoryData();
    }
}

