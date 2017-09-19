package com.recipe.r.ui.activity.prize;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Prize;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.mine.CashActivity;
import com.recipe.r.ui.adapter.mine.PrizeAdapter;
import com.recipe.r.ui.dialog.MyAlertDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的奖品界面
 * <p>
 * 抽奖
 */
public class PrizeActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private ImageView avater_prize;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private PrizeAdapter adapter;
    private ArrayList<Prize.Gift> prizeList = null;
    private ItemTouchHelper itemTouchHelper;
    //限制ImageView长度所能增加的最大值
    private double ICON_MAX_SIZE = 50;
    private TextView share_prize;
    private ImageView redpackage_prize;
    //    private boolean isLucky = false;
//    private String Lucky_Id = "";
    private TextView number_prize;
    private TextView nickname_prize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
//        if (!TextUtils.isEmpty(getIntent().getExtras().getString("isLucky"))) {
//            isLucky = getIntent().getExtras().getBoolean("isLucky");
//            Lucky_Id = getIntent().getExtras().getString("lucky_id");
//        }
        initHead(R.mipmap.reset_back, "返回", "我的奖品", 0, "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        if (adapter == null) {
            adapter = new PrizeAdapter(PrizeActivity.this);
        }
        avater_prize = (ImageView) findViewById(R.id.avater_prize);
        nickname_prize= (TextView) findViewById(R.id.nickname_prize);
        share_prize = (TextView) findViewById(R.id.share_prize);
        number_prize = (TextView) findViewById(R.id.number_prize);
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        redpackage_prize = (ImageView) findViewById(R.id.redpackage_prize);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
//        if (isLucky) {
//            redpackage_prize.setVisibility(View.VISIBLE);
//        } else {
//            redpackage_prize.setVisibility(View.GONE);
//        }
        initRefresh(swipeToLoadLayout);
        number_prize.setText("积分:" + AppSettings.getPrefString(context, ConfigApp.POSINTS, ""));
        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, AppSettings.getPrefString(PrizeActivity.this, ConfigApp.AVATER, ""), WeakImageViewUtil.getImageView(avater_prize));
        nickname_prize.setText(AppSettings.getPrefString(PrizeActivity.this,ConfigApp.USERNAME,""));
    }

    private void initData() {
        prizeList = new ArrayList<>();
        getPrizeData(PAGE, limit);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrizeActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new PrizeAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, Prize.Gift data) {
                if (data != null) {
                    ToastUtil.show(PrizeActivity.this, data.getGoods_name(), 500);
                }
            }
        });
        share_prize.setOnClickListener(this);
        redpackage_prize.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getPrizeData(PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getPrizeData(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    /**
     * 获取个人奖品数据
     *
     * @param page
     * @param limit
     */
    private void getPrizeData(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.MYLUCKYS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)

                .enqueue(new GsonResponseHandler<Prize>() {

                    @Override
                    public void onSuccess(int statusCode, Prize response) {
                        hideProgress();
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            prizeList = response.getData();
                            loadPrizeItem(prizeList);
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
    private void loadPrizeItem(final ArrayList<Prize.Gift> items) {
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


    /**
     * RecyclerView滑动和拖动监听
     */
    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.removeItem(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            //仅对侧滑状态下的效果做出改变
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
                if (Math.abs(dX) <= getSlideLimitation(viewHolder)) {
                    viewHolder.itemView.scrollTo(-(int) dX, 0);
                }
                //如果dX还未达到能删除的距离，此时慢慢增加“眼睛”的大小，增加的最大值为ICON_MAX_SIZE
                else if (Math.abs(dX) <= recyclerView.getWidth() / 2) {
                    double distance = (recyclerView.getWidth() / 2 - getSlideLimitation(viewHolder));
                    double factor = ICON_MAX_SIZE / distance;
                    double diff = (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
                    if (diff >= ICON_MAX_SIZE)
                        diff = ICON_MAX_SIZE;
                    ((PrizeAdapter.ItemViewHolder) viewHolder).tv_text_prize.setText("");   //把文字去掉
                }
            } else {
                //拖拽状态下不做改变，需要调用父类的方法
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //重置改变，防止由于复用而导致的显示问题
            viewHolder.itemView.setScrollX(0);
            ((PrizeAdapter.ItemViewHolder) viewHolder).tv_text_prize.setText("左滑删除");

        }

    };

    /**
     * 获取删除方块的宽度
     */
    public int getSlideLimitation(RecyclerView.ViewHolder viewHolder) {
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
        return viewGroup.getChildAt(1).getLayoutParams().width;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_prize:
                //现金卷界面
                Intent intent = new Intent(PrizeActivity.this, CashActivity.class);
                startActivity(intent);
                break;
            case R.id.redpackage_prize:
                //红包
                getIsGift();
                break;
        }
    }

    /**
     * 查询是否可以抽取奖品
     */
    private void getIsGift() {
        String url = Config.URL + Config.CHECKLUCKY;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
                                if (null != data) {
                                    final MyAlertDialog dialog = new MyAlertDialog(context, R.style.NormalAlertDialogStyle);
                                    dialog.prizeGoods()
                                            .yes(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    getLucky(dialog);
                                                }
                                            })
                                            .no(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    dialog.show();
                                }
                            } else {
                                ToastUtil.show(context, info, 100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
     * 抽奖
     */
    private void getLucky(final MyAlertDialog dialog) {
        String url = Config.URL + Config.GETLUCKY;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            ToastUtil.show(context, response.getString("info"), 100);
                            if (status == 0) {
                                JSONObject data = response.getJSONObject("data");
                                int lucky = data.getInt("lucky");
                                //0,未中奖,1,中奖
                                if (lucky == 0) {
                                    dialog.dismiss();
                                } else if (lucky == 1) {
                                    dialog.setPrizeBackground(R.mipmap.integral_prize);
                                    dialog.setPrizeBtn(R.mipmap.see_prize);
                                    dialog.yes(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ApplyLucky(dialog);
                                        }
                                    });
                                }
                                //        if (TYPE_PRIZE == 0) {
//            //抽到积分
//            dialog.setPrizeBackground(R.mipmap.integral_prize);
//        } else {
//            //抽到奖卷
//            dialog.setPrizeBackground(R.mipmap.volume_prize);
//        }
//        dialog.setPrizeBtn(R.mipmap.see_prize);
                            } else {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 领奖
     */
    private void ApplyLucky(final MyAlertDialog dialog) {
//        String url = Config.URL + Config.APPLYLUCKY;
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
//        params.put("lucky_id", Lucky_Id);
//        mMyOkhttp.post()
//                .url(url)
//                .params(params)
//                .tag(this)
//                .enqueue(new JsonResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, JSONObject response) {
//                        try {
//                            int status = response.getInt("status");
//                            if (status == 0) {
        dialog.dismiss();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }
}
