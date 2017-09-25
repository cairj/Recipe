package com.recipe.r.ui.adapter.menu;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.MenuGoods;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.ui.dialog.LogDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hj on 2017/6/11.
 * 菜谱适配器
 */
public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private List<NewGoods.Goods> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    //购买商品
    private int[] goodsNum;
    private int buyNum;
    private ImageView buyImg;
    private TextView shopCart;
    private ViewGroup animMaskLayout;
    private MyOkHttp mMyOkhttp;
    private LogDialog dialog;

    public MenuRecyclerViewAdapter(Activity context) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<NewGoods.Goods>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setShopCart(TextView shopCart) {
        this.shopCart = shopCart;
    }


    /**
     * 初始化各个商品的购买数量
     */
    public void initGoodsNum() {
        int leng = mDatas.size();
        goodsNum = new int[leng];
        for (int i = 0; i < leng; i++) {
            goodsNum[i] = 0;
        }
    }

    public void updatelist(ArrayList<NewGoods.Goods> list) {
        this.mDatas.clear();
        if (list==null){
            list=new ArrayList<>();
        }
        this.mDatas = list;
        initGoodsNum();
        notifyDataSetChanged();
    }

    public void append(ArrayList<NewGoods.Goods> list) {
        if (list==null){
            list=new ArrayList<>();
        }
        this.mDatas.addAll(list);
        initGoodsNum();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new MenuRecyclerViewAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_menu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NewGoods.Goods goods = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        if (TextUtils.isEmpty(goods.getGoods_summary())) {
            ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).descripttion_tv_menu.setText("暂无简介");
        } else {
            ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).descripttion_tv_menu.setText(goods.getGoods_summary());
        }
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).title_tv_menu.setText(goods.getGoods_name());
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).price_tv_home.setText("¥" + goods.getShop_price());
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).sell_number_home.setText("已售出:" + goods.getSold_num() + "份");
        if (!TextUtils.isEmpty(goods.getGoods_image())) {
            ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goods.getGoods_image(), WeakImageViewUtil.getImageView(((MenuRecyclerViewAdapter.Item2ViewHolder) holder).food_iv_menu));

        } else {
            ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).food_iv_menu.setImageResource(R.mipmap.default_photo);

        }
        //判断是否隐藏
        isSelected(goodsNum[position], ((MenuRecyclerViewAdapter.Item2ViewHolder) holder));
        //添加商品
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsNum[position]++;
                // TODO: 2016/2/27 添加购物车
                int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                buyImg = new ImageView(context);
                buyImg.setBackgroundResource(R.mipmap.icon_goods_add_item);// 设置buyImg的图片
                buyNum++;
                addShopCart(goods.getGoods_id(), "1");
                setAnim(buyImg, start_location);// 开始执行动画
                mOnGoodsNunChangeListener.onNumChange();
                isSelected(goodsNum[position], ((MenuRecyclerViewAdapter.Item2ViewHolder) holder));
            }
        });
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsNum[position] > 0) {
                    goodsNum[position]--;
                    // TODO: 2016/2/27 删除购物车内容
                    isSelected(goodsNum[position], ((MenuRecyclerViewAdapter.Item2ViewHolder) holder));
                    buyNum--;
                    addShopCart(goods.getGoods_id(), "-1");
                    changeShopCart();
                    mOnGoodsNunChangeListener.onNumChange();
                } else {

                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (NewGoods.Goods) view.getTag());
                }

            }
        });
        ((MenuRecyclerViewAdapter.Item2ViewHolder) holder).food_iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGoodsDetails(goods.getGoods_id());
            }
        });
    }

    public void setBuyNum() {
        //TODO 清空购物车数量
        buyNum = 0;
        changeShopCart();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv_menu;
        ImageView food_iv_menu;
        TextView price_tv_home;
        TextView sell_number_home;
        TextView descripttion_tv_menu;
        //添加商品
        public final ImageView ivGoodsMinus;
        public final TextView tvGoodsSelectNum;
        public final ImageView ivGoodsAdd;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            title_tv_menu = (TextView) itemView.findViewById(R.id.title_tv_menu);
            descripttion_tv_menu = (TextView) itemView.findViewById(R.id.descripttion_tv_menu);
            food_iv_menu = (ImageView) itemView.findViewById(R.id.food_iv_menu);
            price_tv_home = (TextView) itemView.findViewById(R.id.price_tv_menu);
            sell_number_home = (TextView) itemView.findViewById(R.id.sellnumber_tv_menu);
            ivGoodsMinus = (ImageView) itemView.findViewById(R.id.ivGoodsMinus);
            tvGoodsSelectNum = (TextView) itemView.findViewById(R.id.tvGoodsSelectNum);
            ivGoodsAdd = (ImageView) itemView.findViewById(R.id.ivGoodsAdd);
        }
    }

    /**
     * 设置动画
     *
     * @param v              要显示的内容
     * @param start_location 坐标
     */
    private void setAnim(final View v, int[] start_location) {
        animMaskLayout = null;
        animMaskLayout = createAnimLayout();
        animMaskLayout.addView(v);// 把动画小球添加到动画层
        final View view = addViewToAnimLayout(animMaskLayout, v,
                start_location);
        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
        // 计算位移
//        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
//        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
//                -endX, 0, 0);
        /**
         * 修改动画方向
         */
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                -40, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(300);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                changeShopCart();
            }
        });
    }

    /**
     * 判断商品是否有添加到购物车中
     *
     * @param i  条目下标
     * @param vh ViewHolder
     */
    private void isSelected(int i, MenuRecyclerViewAdapter.Item2ViewHolder vh) {
        if (i == 0) {
            vh.tvGoodsSelectNum.setVisibility(View.GONE);
            vh.ivGoodsMinus.setVisibility(View.GONE);
        } else {
            vh.tvGoodsSelectNum.setVisibility(View.VISIBLE);
            vh.tvGoodsSelectNum.setText(i + "");
            vh.ivGoodsMinus.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 修改购物车状态
     */
    private void changeShopCart() {
        if (buyNum > 0) {
            shopCart.setVisibility(View.VISIBLE);
            shopCart.setText(buyNum + "");
        } else {
            shopCart.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化动画图层
     *
     * @return
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 将View添加到动画图层
     *
     * @param vg
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public interface OnShopCartGoodsChangeListener {
        public void onNumChange();
    }

    private OnShopCartGoodsChangeListener mOnGoodsNunChangeListener = null;

    public void setOnShopCartGoodsChangeListener(OnShopCartGoodsChangeListener e) {
        mOnGoodsNunChangeListener = e;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NewGoods.Goods data);
    }

    /**
     * 添加购物车方法
     */
    private void addShopCart(String goodId, String goods_number) {
        String url = Config.URL + Config.ADDCART;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "andriod");
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
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {

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
     * 获取商品详情
     */
    private void getGoodsDetails(final String GOODID) {
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
                        int status = response.getStatus();
                        String info = response.getInfo();

                        if (status == 1) {
                            dialog = new LogDialog(context);
                            dialog.setTitle(response.getData().getGoods_name());
                            dialog.setContent(response.getData().getGoods_brief());
                            dialog.setBannerViews(Config.IMAGE_URL + response.getData().getGoods_image());
                            dialog.show();
                            /**
                             * 设置收藏，暂时不用
                             */
//                            if(response.getData().getStatus().equals("0")){
//                                //未收藏
//                                dialog.setLikeViews(R.mipmap.love);
//                            }else{
//                                //已收藏
//                                dialog.setLikeViews(R.mipmap.love);
//                            }
//
//                            if (dialog.LikeView() != null) {
//                                dialog.LikeView().setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        setCollect(response.getData().getGoods_id());
//                                    }
//                                });
//                            }
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
                                                dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));

                                            } else {
                                                if (response.getData().getColleted().equals("0")) {
                                                    //添加收藏
                                                    setCollect(GOODID, "1");
                                                    dialog.getCollectionText().setText("取消收藏");
                                                    dialog.getCollectionText().setTextColor(context.getResources().getColor(R.color.text_color));
                                                } else if (response.getData().getColleted().equals("1")) {
                                                    //取消收藏
                                                    setCollect(GOODID, "0");
                                                    dialog.getCollectionText().setText("收藏");
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

        String url = Config.URL + Config.COLLECT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
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

