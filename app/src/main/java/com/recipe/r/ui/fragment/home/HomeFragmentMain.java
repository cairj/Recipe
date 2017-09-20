package com.recipe.r.ui.fragment.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.BannerModel;
import com.recipe.r.entity.MenuGoods;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.entity.ShareItem;
import com.recipe.r.events.MainEvent;
import com.recipe.r.ui.activity.BaseH5Activity;
import com.recipe.r.ui.activity.home.HotActivity;
import com.recipe.r.ui.activity.home.HotDetailsActivity;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.menu.ShopCarActivity;
import com.recipe.r.ui.activity.search.SearchActivity;
import com.recipe.r.ui.activity.table.QuickTableActivity;
import com.recipe.r.ui.activity.zxing.QrScanActivity;
import com.recipe.r.ui.adapter.home.HomeLoopAdapter;
import com.recipe.r.ui.adapter.home.HomeRecyclerViewAdapter;
import com.recipe.r.ui.dialog.BannerDialog;
import com.recipe.r.ui.dialog.LogDialog;
import com.recipe.r.ui.dialog.MyAlertDialog;
import com.recipe.r.ui.fragment.base.BaseFragment;
import com.recipe.r.ui.widget.SpaceItemDecoration;
import com.recipe.r.ui.widget.TranslucentScrollView;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.recipe.r.R.id.tv_value_person;
import static com.recipe.r.R.id.tv_value_time;


/**
 * 2017/6/9
 * wangxiaoer
 * 功能描述：首页界面
 **/
@SuppressLint("ValidFragment")
public class HomeFragmentMain extends BaseFragment implements TranslucentScrollView.OnScrollChangedListener, View.OnClickListener {
    private MainActivity context;
    private float headerHeight;//顶部高度
    private float minHeaderHeight;//顶部最低高度，即Bar的高度
    private TranslucentScrollView mScrollView;
    private float offset = 0.0f;
    private TextView toolbar_tv_search;
    private RollPagerView mRollPagerView;
    private List<BannerModel> banner = null;
    //    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<NewGoods.Goods> recommendList = null;//推荐
    private ImageView toolbar_iv_logo;
    private TextView tvValuedate, tvValuetime, tvValueperson;
    private int PAGE = 1;
    private int limit = 10;
    HomeRecyclerViewAdapter adapter = null;
    private boolean isRefresh = true;
    private ImageView hot_iv;
    private LinearLayout mToolbar;
    private int year;
    private int month;
    private int day_of_month;
    private int hour;
    private int minute;
    private MyAlertDialog DiningDialog;
    private DatePickerDialog dateDialog;
    private Calendar calendar;
    private ImageView no_login_iv;
    private ImageView message_iv;
    private ArrayList<ImageView> rec_iv_list;
    private ArrayList<TextView> rec_title_list;
    private ArrayList<TextView> rec_content_list;
    private ArrayList<ImageView> appetizer_iv_list;
    private ArrayList<TextView> appetizer_title_list;
    private ArrayList<ImageView> foodrecommend_iv_list;
    private ArrayList<TextView> foodrecommend_title_list;
    private ArrayList<TextView> foodrecommend_content_list;
    private LinearLayout share_home;
    private LinearLayout recipe_home;
    private Button reserve_bt;
    private LinearLayout meal_home;
    private String DATE = "";
    private String TIME = "";
    private String NUMBER = "";
    private LinearLayout hot_details_ll2;
    private LinearLayout hot_details_ll;
    private TextView hot_details_title;
    private TextView hot_details_title2;
    private String New_Id = "";
    private String New_Id2 = "";
    private LogDialog dialog;
    private TextView scan_tv_home;
    private TextView message_tv_home;
    private ImageView home_search_line;

    public HomeFragmentMain(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }


    // 设置渐变高度
    private void initMeasure() {
        headerHeight = getResources().getDimension(R.dimen.dimen_300);
        minHeaderHeight = getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
    }

    private void initView(View view) {
        no_login_iv = (ImageView) view.findViewById(R.id.no_login_iv);
//        if (UserIsLogin.isShowLogin(context)) {
//            //显示
//            no_login_iv.setVisibility(View.VISIBLE);
//        } else {
//            //不显示
        no_login_iv.setVisibility(View.GONE);
//        }
        mToolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        message_tv_home = (TextView) view.findViewById(R.id.message_tv_home);
        scan_tv_home = (TextView) view.findViewById(R.id.scan_tv_home);
        message_iv = (ImageView) view.findViewById(R.id.message_iv);
        meal_home = (LinearLayout) view.findViewById(R.id.meal_home);
        home_search_line = (ImageView) view.findViewById(R.id.home_search_line);
        share_home = (LinearLayout) view.findViewById(R.id.share_home);
        recipe_home = (LinearLayout) view.findViewById(R.id.recipe_home);
        toolbar_iv_logo = (ImageView) view.findViewById(R.id.toolbar_iv_logo);
        mScrollView = (TranslucentScrollView) view.findViewById(R.id.scrollview);
        toolbar_tv_search = (TextView) view.findViewById(R.id.toolbar_et_search);
        mRollPagerView = (RollPagerView) view.findViewById(R.id.rl_home_banner);
        hot_details_ll2 = (LinearLayout) view.findViewById(R.id.hot_details_ll2);
        hot_details_ll = (LinearLayout) view.findViewById(R.id.hot_details_ll);
        hot_details_title = (TextView) view.findViewById(R.id.hot_details_title);
        hot_details_title2 = (TextView) view.findViewById(R.id.hot_details_title2);
        reserve_bt = (Button) view.findViewById(R.id.reserve_bt);
        hot_iv = (ImageView) view.findViewById(R.id.hot_iv);
//        this.swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        tvValuedate = (TextView) view.findViewById(R.id.tv_value_date);
        tvValuetime = (TextView) view.findViewById(tv_value_time);
        tvValueperson = (TextView) view.findViewById(tv_value_person);
        mRollPagerView.setVisibility(View.GONE);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mRollPagerView.setHintView(new ColorPointHintView(context, Color.RED, Color.GRAY));// 设置圆点指示器颜色
        mRecyclerView.setNestedScrollingEnabled(false);//在ScrollView中嵌套了recyclerView，禁止recycler滑动防止冲突
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        adapter = new HomeRecyclerViewAdapter(context);
        mRecyclerView.setAdapter(adapter);
        //为您推荐数组
        setRecommendList(view);
        //开胃新品数组
        setAppetizerList(view);
        //美食推荐
//        setFoodRecommendList(view);
        initMeasure();
    }

    /**
     * 添加美食推荐数组
     *
     * @param view
     */
    private void setFoodRecommendList(View view) {
        foodrecommend_iv_list = new ArrayList<>();
        foodrecommend_title_list = new ArrayList<>();
        foodrecommend_content_list = new ArrayList<>();
        TextView foodrecommend_title1 = (TextView) view.findViewById(R.id.foodrecommend_title1);
        TextView foodrecommend_title2 = (TextView) view.findViewById(R.id.foodrecommend_title2);
        TextView foodrecommend_title3 = (TextView) view.findViewById(R.id.foodrecommend_title3);
        TextView foodrecommend_title4 = (TextView) view.findViewById(R.id.foodrecommend_title4);
        foodrecommend_title_list.add(foodrecommend_title1);
        foodrecommend_title_list.add(foodrecommend_title2);
        foodrecommend_title_list.add(foodrecommend_title3);
        foodrecommend_title_list.add(foodrecommend_title4);


        TextView foodrecommend_content1 = (TextView) view.findViewById(R.id.foodrecommend_content1);
        TextView foodrecommend_content2 = (TextView) view.findViewById(R.id.foodrecommend_content2);
        TextView foodrecommend_content3 = (TextView) view.findViewById(R.id.foodrecommend_content3);
        TextView foodrecommend_content4 = (TextView) view.findViewById(R.id.foodrecommend_content4);
        foodrecommend_content_list.add(foodrecommend_content1);
        foodrecommend_content_list.add(foodrecommend_content2);
        foodrecommend_content_list.add(foodrecommend_content3);
        foodrecommend_content_list.add(foodrecommend_content4);

        ImageView foodrecommend_image1 = (ImageView) view.findViewById(R.id.foodrecommend_image1);
        ImageView foodrecommend_image2 = (ImageView) view.findViewById(R.id.foodrecommend_image2);
        ImageView foodrecommend_image3 = (ImageView) view.findViewById(R.id.foodrecommend_image3);
        ImageView foodrecommend_image4 = (ImageView) view.findViewById(R.id.foodrecommend_image4);

        foodrecommend_iv_list.add(foodrecommend_image1);
        foodrecommend_iv_list.add(foodrecommend_image2);
        foodrecommend_iv_list.add(foodrecommend_image3);
        foodrecommend_iv_list.add(foodrecommend_image4);
    }


    /**
     * 添加为您推荐数组
     *
     * @param view
     */
    private void setRecommendList(View view) {
        rec_iv_list = new ArrayList<>();
        rec_title_list = new ArrayList<>();
        rec_content_list = new ArrayList<>();
        TextView recommend_title1 = (TextView) view.findViewById(R.id.recommend_title1);
        TextView recommend_title2 = (TextView) view.findViewById(R.id.recommend_title2);
        TextView recommend_title3 = (TextView) view.findViewById(R.id.recommend_title3);
        TextView recommend_title4 = (TextView) view.findViewById(R.id.recommend_title4);
        TextView recommend_title5 = (TextView) view.findViewById(R.id.recommend_title5);
        TextView recommend_title6 = (TextView) view.findViewById(R.id.recommend_title6);
        TextView recommend_title7 = (TextView) view.findViewById(R.id.recommend_title7);
        rec_title_list.add(recommend_title1);
        rec_title_list.add(recommend_title2);
        rec_title_list.add(recommend_title3);
        rec_title_list.add(recommend_title4);
        rec_title_list.add(recommend_title5);
        rec_title_list.add(recommend_title6);
        rec_title_list.add(recommend_title7);

        TextView recommend_content1 = (TextView) view.findViewById(R.id.recommend_content1);
        TextView recommend_content2 = (TextView) view.findViewById(R.id.recommend_content2);
        TextView recommend_content3 = (TextView) view.findViewById(R.id.recommend_content3);
        rec_content_list.add(recommend_content1);
        rec_content_list.add(recommend_content2);
        rec_content_list.add(recommend_content3);

        ImageView recommend_image1 = (ImageView) view.findViewById(R.id.recommend_image1);
        ImageView recommend_image2 = (ImageView) view.findViewById(R.id.recommend_image2);
        ImageView recommend_image3 = (ImageView) view.findViewById(R.id.recommend_image3);
        ImageView recommend_image4 = (ImageView) view.findViewById(R.id.recommend_image4);
        ImageView recommend_image5 = (ImageView) view.findViewById(R.id.recommend_image5);
        ImageView recommend_image6 = (ImageView) view.findViewById(R.id.recommend_image6);
        ImageView recommend_image7 = (ImageView) view.findViewById(R.id.recommend_image7);

        rec_iv_list.add(recommend_image1);
        rec_iv_list.add(recommend_image2);
        rec_iv_list.add(recommend_image3);
        rec_iv_list.add(recommend_image4);
        rec_iv_list.add(recommend_image5);
        rec_iv_list.add(recommend_image6);
        rec_iv_list.add(recommend_image7);

    }

    /**
     * 开胃数组
     *
     * @param view
     */
    private void setAppetizerList(View view) {
        appetizer_iv_list = new ArrayList<>();
        appetizer_title_list = new ArrayList<>();
        TextView appetizer_title1 = (TextView) view.findViewById(R.id.appetizer_title1);
        TextView appetizer_title2 = (TextView) view.findViewById(R.id.appetizer_title2);
        TextView appetizer_title3 = (TextView) view.findViewById(R.id.appetizer_title3);
        TextView appetizer_title4 = (TextView) view.findViewById(R.id.appetizer_title4);
        TextView appetizer_title5 = (TextView) view.findViewById(R.id.appetizer_title5);
        TextView appetizer_title6 = (TextView) view.findViewById(R.id.appetizer_title6);
        TextView appetizer_title7 = (TextView) view.findViewById(R.id.appetizer_title7);
        TextView appetizer_title8 = (TextView) view.findViewById(R.id.appetizer_title8);
        appetizer_title_list.add(appetizer_title1);
        appetizer_title_list.add(appetizer_title2);
        appetizer_title_list.add(appetizer_title3);
        appetizer_title_list.add(appetizer_title4);
        appetizer_title_list.add(appetizer_title5);
        appetizer_title_list.add(appetizer_title6);
        appetizer_title_list.add(appetizer_title7);
        appetizer_title_list.add(appetizer_title8);
        ImageView appetizer_image1 = (ImageView) view.findViewById(R.id.appetizer_image1);
        ImageView appetizer_image2 = (ImageView) view.findViewById(R.id.appetizer_image2);
        ImageView appetizer_image3 = (ImageView) view.findViewById(R.id.appetizer_image3);
        ImageView appetizer_image4 = (ImageView) view.findViewById(R.id.appetizer_image4);
        ImageView appetizer_image5 = (ImageView) view.findViewById(R.id.appetizer_image5);
        ImageView appetizer_image6 = (ImageView) view.findViewById(R.id.appetizer_image6);
        ImageView appetizer_image7 = (ImageView) view.findViewById(R.id.appetizer_image7);
        ImageView appetizer_image8 = (ImageView) view.findViewById(R.id.appetizer_image8);
        appetizer_iv_list.add(appetizer_image1);
        appetizer_iv_list.add(appetizer_image2);
        appetizer_iv_list.add(appetizer_image3);
        appetizer_iv_list.add(appetizer_image4);
        appetizer_iv_list.add(appetizer_image5);
        appetizer_iv_list.add(appetizer_image6);
        appetizer_iv_list.add(appetizer_image7);
        appetizer_iv_list.add(appetizer_image8);
    }

    private void initData() {
        mToolbar.bringToFront();
        banner = new ArrayList<>();
        recommendList = new ArrayList<>();
        getDate();
        getTime();
        //Banner数据
        getBanner();
        if (offset == 1) {
            mToolbar.setBackgroundColor(Color.argb(255, 255, 255, 255));
//            toolbar_tv_search.setBackgroundResource(R.drawable.shape_textview_red);
        } else {
            mToolbar.setBackgroundColor(Color.argb(0, 247, 97, 20));
        }
        getHotGoods(1, 2);
        //列表数据
        getMenuGoods("", PAGE, limit);
        //热卖数据
        getMenuGoods("hot", PAGE, limit);
        getMenuGoods("new", PAGE, limit);
    }

    /**
     * 获取Banner接口
     */
    private void getBanner() {
        String url = Config.URL + Config.GETBANNER;
        Map<String, String> params = new HashMap<>();
        params.put("itype", "index");
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("error", "statusCode" + statusCode + "error_msg" + error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.e("onSuccess",response.toString());
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONArray data = response.getJSONArray("data");
                                //JSONArray banner_Images = data.getJSONArray("images");
                                for (int i = 0; i < data.length(); i++) {
                                    BannerModel bannerModel = new BannerModel();
                                    bannerModel.setTips("" + i);
                                    bannerModel.setImageUrl(Config.IMAGE_URL + data.getJSONObject(i).getString("thumb"));
                                    bannerModel.setHref(Config.IMAGE_URL + data.getJSONObject(i).getString("href"));
                                    banner.add(bannerModel);
                                }
                                loadHomeBanner(banner);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //获取当前系统时间
    private void getDate() {
        calendar = Calendar.getInstance();
        //当前年
        year = calendar.get(Calendar.YEAR);
        month = (calendar.get(Calendar.MONTH)) + 1;
        day_of_month = calendar.get(Calendar.DAY_OF_MONTH);

    }

    //获取当前系统时间
    private void getTime() {
        Calendar c = Calendar.getInstance();
        //时
        hour = c.get(Calendar.HOUR_OF_DAY);
        //分
        minute = c.get(Calendar.MINUTE);
    }

    private void initListener() {
        mScrollView.setOnScrollChangedListener(this);
        toolbar_tv_search.setOnClickListener(this);
        meal_home.setOnClickListener(clickListener);
        share_home.setOnClickListener(clickListener);
        recipe_home.setOnClickListener(clickListener);
        toolbar_iv_logo.setOnClickListener(clickListener);
        reserve_bt.setOnClickListener(clickListener);
        hot_details_ll2.setOnClickListener(clickListener);
        hot_details_ll.setOnClickListener(clickListener);

        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (banner != null && banner.size() != 0) {
                    String URL = banner.get(position).getImageUrl();
                    BannerDialog dialog = new BannerDialog(context);
                    dialog.setImageViews(URL);
                    dialog.show();
                }
            }
        });
        tvValuedate.setOnClickListener(clickListener);
        tvValuetime.setOnClickListener(clickListener);
        tvValueperson.setOnClickListener(clickListener);
        hot_iv.setOnClickListener(clickListener);
        no_login_iv.setOnClickListener(clickListener);
//        //为swipeToLoadLayout设置下拉刷新监听者
//        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
//        swipeToLoadLayout.setOnLoadMoreListener(this);
        message_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *消息中心,获取我们发起聊天的者的username
                 *获取当前登录用户的 username
                 */
//                if (UserIsLogin.IsLogn(context)) {
//                    // 跳转到聊天界面，开始聊天
//                    Intent intent = new Intent(context, ChatListActivity.class);
//                    startActivity(intent);
//                }
                Intent intent = new Intent(context, ShopCarActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NewGoods.Goods data) {
                getGoodsDetails(data.getGoods_id());
            }
        });

    }

    //加载Banner数据
    private void loadHomeBanner(final List<BannerModel> banner) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRollPagerView.setAdapter(new HomeLoopAdapter(mRollPagerView, context, banner));
                mRollPagerView.setVisibility(View.VISIBLE);
                mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int i) {
                        BaseH5Activity.open(getActivity(),banner.get(i).getHref());
                    }
                });
            }
        });
    }

    //加载首页列表数据
    private void loadHomeShopItem(final ArrayList<NewGoods.Goods> items) {
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


    /**
     * 显示PopupWindow
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_value_date:
                    datePickerDialog();
                    setTextImage(R.mipmap.icon_down);
                    break;
                case tv_value_time:
                    timePickerDialog();
                    setTextImage(R.mipmap.icon_down);
                    break;
                case tv_value_person:
                    DiningDialog = new MyAlertDialog(context)
                            .oneEdit("请您选择就餐人数", "2", getResources().getString(R.string.sure_str), getResources().getString(R.string.cancel), 4).edit_yes(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String result = view.getTag().toString();
                                    NUMBER = result;
                                    tvValueperson.setText(result);
                                    DiningDialog.dismiss();
                                }
                            }).no(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DiningDialog.dismiss();
                                }
                            });
                    DiningDialog.show();
                    setTextImage(R.mipmap.icon_down);
                    break;
                case R.id.hot_iv:
                    //热点
                    Intent intent_hot = new Intent(context, HotActivity.class);
                    startActivity(intent_hot);
                    break;
                case R.id.no_login_iv:
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.toolbar_iv_logo:
                    Intent intent_scan = new Intent(context, QrScanActivity.class);
                    context.startActivity(intent_scan);
                    break;
                case R.id.share_home:
                    //分享
                    //食谱
                    EventBus.getDefault().post(new MainEvent("share"));
                    break;
                case R.id.recipe_home:
                    //食谱
                    EventBus.getDefault().post(new MainEvent("menu"));
                    break;
                case R.id.reserve_bt:
                    //预订订桌
                    if (tvValuedate.equals(getResources().getString(R.string.selector_date))) {
                        ToastUtil.show(context, "请选择日期", 100);
                        return;
                    }
                    if (tvValuetime.equals(getResources().getString(R.string.selector_time))) {
                        ToastUtil.show(context, "请选择时间", 100);
                        return;
                    }
                    if (tvValueperson.equals(getResources().getString(R.string.selector_number))) {
                        ToastUtil.show(context, "请选择就餐人数", 100);
                        return;
                    }
                    Intent intent_table = new Intent(context, QuickTableActivity.class);
                    intent_table.putExtra("mark", "main");
                    intent_table.putExtra("date", DATE);
                    intent_table.putExtra("time", TIME);
                    intent_table.putExtra("number", NUMBER);
                    startActivity(intent_table);
                    break;
                case R.id.meal_home:
                    EventBus.getDefault().post(new MainEvent("table"));
                    break;
                case R.id.hot_details_ll:
                    if (!TextUtils.isEmpty(New_Id)) {
                        Intent intent_detail = new Intent(context, HotDetailsActivity.class);
                        intent_detail.putExtra("news_id", New_Id);
                        startActivity(intent_detail);
                    }
                    break;
                case R.id.hot_details_ll2:
                    if (!TextUtils.isEmpty(New_Id2)) {
                        Intent intent_detail2 = new Intent(context, HotDetailsActivity.class);
                        intent_detail2.putExtra("news_id", New_Id2);
                        startActivity(intent_detail2);
                    }
                    break;
            }
        }
    };

    private void datePickerDialog() {
        dateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                DATE = date;
                tvValuedate.setText(date);
            }
        }, year, month, day_of_month);
        dateDialog.show();
        /** 不显示年设置**/
//        dateDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                String date = String.format("%d-%d-%d", year, monthOfYear, dayOfMonth);
//                tvValuedate.setText(date);
//            }
//        }, year, month, day_of_month);
//        dateDialog.setCancelable(true);
//        DatePicker dp = dateDialog.getDatePicker();
//        //设置当天为最小值
//        dp.setMinDate(Long.parseLong(DateUtil.getStringToTime(year + "年" + month + "月" + day_of_month + "日")));
//        //设置最大值是７天
////        calendar.set(Calendar.DAY_OF_MONTH, day_of_month + 6);
////        dp.setMaxDate(calendar.getTimeInMillis());
//        try {
//            //获取指定的字段
//            Field field = dp.getClass().getDeclaredField("mYearSpinner");
//            //解封装
//            field.setAccessible(true);
//            //获取当前实例的值
//            NumberPicker np = ((NumberPicker) field.get(dp));
//            np.setVisibility(View.GONE);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        dateDialog.show();
    }


    private void timePickerDialog() {
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int mounte) {
                String time = String.format("%d:%d", hour, mounte);
                TIME = time;
                tvValuetime.setText(time);
            }
        }, hour, minute, true).show();
    }

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValuedate.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        //Y轴偏移量
        float scrollY = who.getScrollY();
        //变化率
        float headerBarOffsetY = headerHeight - minHeaderHeight;//Toolbar与header高度的差值
        offset = 1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f);
        //Toolbar背景色透明度
        mToolbar.setBackgroundColor(Color.argb((int) (offset * 255), 255, 255, 255));//后三位设置 RGB 颜色
//        toolbar_tv_search.setBackgroundColor(Color.argb((int) (offset * 255), 212, 82, 16));
//        mToolbar.setBackgroundResource(R.color.touming50);
//        toolbar_tv_search.setBackgroundResource(R.drawable.shape_rounded_textview);
        if (offset > 0.5) {
            //TODO 滑动距离超过规定值
            home_search_line.setVisibility(View.VISIBLE);
            toolbar_iv_logo.setBackgroundResource(R.mipmap.black_scan);
            message_iv.setBackgroundResource(R.mipmap.shop_cart_black);
            message_tv_home.setTextColor(getResources().getColor(R.color.black));
            scan_tv_home.setTextColor(getResources().getColor(R.color.black));
            toolbar_tv_search.setBackgroundResource(R.drawable.shape_rounded_textview_gray);
        }
        if (offset == 0.0) {
            //TODO 恢复了原有值
            home_search_line.setVisibility(View.GONE);
            toolbar_iv_logo.setBackgroundResource(R.mipmap.scanning);
            message_iv.setBackgroundResource(R.mipmap.shop_cart_white);
            scan_tv_home.setTextColor(getResources().getColor(R.color.white));
            message_tv_home.setTextColor(getResources().getColor(R.color.white));
            toolbar_tv_search.setBackgroundResource(R.drawable.shape_rounded_textview);
        }
    }


    /**
     * 获取菜谱列表
     *
     * @param type
     * @param page
     * @param limit
     */
    private void getMenuGoods(final String type, int page, int limit) {
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
                    public void onSuccess(int statusCode, final NewGoods response) {
                        hideProgress();
                        Log.e("onSuccess",response.toString());
                        int status = response.getStatus();
                        if (status == 1) {

                            if (type.equals("hot")) {
                                //热卖-开胃
                                for (int i = 0; i < appetizer_iv_list.size(); i++) {
                                    ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + response.getData().get(i).getGoods_image(), WeakImageViewUtil.getImageView(appetizer_iv_list.get(i)));
                                    appetizer_title_list.get(i).setText(response.getData().get(i).getGoods_name());
                                    final int finalI = i;
                                    appetizer_iv_list.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getGoodsDetails(response.getData().get(finalI).getGoods_id());
                                        }
                                    });
                                }
                            } else if (type.equals("new")) {
//                                //最新-美食
//                                for (int i = 0; i < foodrecommend_iv_list.size(); i++) {
//                                    ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + response.getData().get(i).getGoods_image(), WeakImageViewUtil.getImageView(foodrecommend_iv_list.get(i)));
//                                    foodrecommend_title_list.get(i).setText(response.getData().get(i).getGoods_name());
//                                    foodrecommend_content_list.get(i).setText(response.getData().get(i).getGoods_summary());
//                                    final int finalI = i;
//                                    foodrecommend_iv_list.get(i).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            getGoodsDetails(response.getData().get(finalI).getGoods_id());
//                                        }
//                                    });
//                                }
                            } else {
                                recommendList = response.getData();
                                for (int i = 0; i < rec_iv_list.size(); i++) {
                                    if (i < rec_content_list.size()) {
                                        rec_content_list.get(i).setText(recommendList.get(i).getGoods_summary());
                                    }
                                    rec_title_list.get(i).setText(recommendList.get(i).getGoods_name());
                                    ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + recommendList.get(i).getGoods_image(), WeakImageViewUtil.getImageView(rec_iv_list.get(i)));
                                    final int finalI = i;
                                    rec_iv_list.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getGoodsDetails(response.getData().get(finalI).getGoods_id());
                                        }
                                    });
                                }
                                loadHomeShopItem(recommendList);
                            }
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        ToastUtil.show(context, "网络异常", 100);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_et_search:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }


//    @Override
//    public void onRefresh() {
//        isRefresh = true;
//        PAGE = 1;
//        getRecommend(PAGE,limit);
//        //设置下拉刷新结束
//        swipeToLoadLayout.setRefreshing(false);
//    }

    //    @Override
//    public void onLoadMore() {
//        isRefresh = false;
//        PAGE++;
//        getRecommend(PAGE,limit);
//        swipeToLoadLayout.setLoadingMore(false);
//    }
    private void getHotGoods(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETSHARELIST;
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");//0，分享，1，专题
        params.put("option", "hot");
        params.put("device", "android");
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
                        Log.e("onSuccess",response.toString());
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            if (response.getData().size() >= 2) {
                                hot_details_title.setText(response.getData().get(0).getTitle());
                                hot_details_title2.setText(response.getData().get(1).getTitle());
                                New_Id = response.getData().get(0).getNews_id();
                                New_Id2 = response.getData().get(1).getNews_id();
                            }
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();

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
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<MenuGoods>() {


                    @Override
                    public void onSuccess(int statusCode, final MenuGoods response) {
                        hideProgress();
                        Log.e("onSuccess",response.toString());
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            dialog = new LogDialog(context);
                            dialog.setTitle(response.getData().getGoods_name());
                            dialog.setContent(response.getData().getGoods_brief());
                            dialog.setBannerViews(Config.IMAGE_URL + response.getData().getGoods_image());
                            /**
                             * 设置收藏
                             */
                            if (response.getData().getColleted() == null) {
                                dialog.getCollectionText().setText("收藏");
                                dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
                            } else {
                                if (response.getData().getColleted().equals("0")) {//未收藏
                                    //添加收藏
                                    dialog.getCollectionText().setText("收藏");
                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
                                } else if (response.getData().getColleted().equals("1")) {//已收藏
                                    //取消收藏
                                    dialog.getCollectionText().setText("取消收藏");
                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_bule));
                                }
                            }
                            dialog.show();
                            /*if(response.getData().getStatus().equals("0")){
                                //未收藏
                                dialog.setLikeViews(R.mipmap.love);
                            }else{
                                //已收藏
                                dialog.setLikeViews(R.mipmap.love);
                            }

                            if (dialog.LikeView() != null) {
                                dialog.LikeView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        setCollect(response.getData().getGoods_id());
                                    }
                                });
                            }*/
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
                                                dialog.getCollectionText().setText("取消收藏");
                                                dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_bule));

                                            } else {
                                                if (response.getData().getColleted().equals("0")) {
                                                    //添加收藏
                                                    setCollect(GOODID, "1");
                                                    dialog.getCollectionText().setText("取消收藏");
                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_bule));
                                                } else if (response.getData().getColleted().equals("1")) {
                                                    //取消收藏
                                                    setCollect(GOODID, "0");
                                                    dialog.getCollectionText().setText("收藏");
                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
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
     * 添加购物车方法
     */
    private void addShopCart(String goodId, String goods_number) {
        showProgress();
        String url = Config.URL + Config.ADDCART;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
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
                        Log.e("onSuccess",response.toString());
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
     * 设置产品收藏
     */
    private void setCollect(String goodId, String type) {
        showProgress();
        String url = Config.URL + Config.COLLECT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        //params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
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
                        Log.e("onSuccess",response.toString());
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

}
