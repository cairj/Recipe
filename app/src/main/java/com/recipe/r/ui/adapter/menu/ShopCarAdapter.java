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
import com.recipe.r.entity.NewGoods;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hj on 2017/7/14.
 * 购物车适配器
 */
public class ShopCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private List<NewGoods.Goods> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    //购买商品
    private int[] goodsNum;
    private int buyNum;
    private ImageView buyImg;
    //    private TextView shopCart;
    private LinearLayout shop_LL;
    private TextView shopping_totalMoney_Tv;
    private ViewGroup animMaskLayout;
    private double TOTAL = 0;//商品总价格
    private MyOkHttp mMyOkhttp;

    public ShopCarAdapter(Activity context) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<NewGoods.Goods>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setShopCart(LinearLayout shop_LL, TextView shopping_totalMoney_Tv) {
        this.shop_LL = shop_LL;
        this.shopping_totalMoney_Tv = shopping_totalMoney_Tv;
    }


    /**
     * 初始化各个商品的购买数量
     */
    private void initGoodsNum() {
        int leng = mDatas.size();
        goodsNum = new int[leng];
        for (int i = 0; i < leng; i++) {
            goodsNum[i] = mDatas.get(i).getGoods_number();
        }
    }

    /**
     * 初次加载购物车
     */
    public void setFirstShop(double total_price) {
        TOTAL = total_price;
    }

    public void updatelist(ArrayList<NewGoods.Goods> list) {
        this.mDatas.clear();
        this.mDatas = list;
        initGoodsNum();
        notifyDataSetChanged();
    }

    public void append(ArrayList<NewGoods.Goods> list) {
        this.mDatas.addAll(list);
        initGoodsNum();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ShopCarAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_menu_division, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NewGoods.Goods goods = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        if (TextUtils.isEmpty(goods.getGoods_brief())) {
            ((ShopCarAdapter.Item2ViewHolder) holder).descripttion_tv_menu.setText("暂无商品简介");
        } else {
            ((ShopCarAdapter.Item2ViewHolder) holder).descripttion_tv_menu.setText(goods.getGoods_summary());
        }
        ((ShopCarAdapter.Item2ViewHolder) holder).title_tv_menu.setText(goods.getGoods_name());
        ((ShopCarAdapter.Item2ViewHolder) holder).price_tv_home.setText("¥" + goods.getShop_price());
//        ((ShopCarAdapter.Item2ViewHolder) holder).sell_number_home.setText(goods.getSold_num());
//        if (goods.getCollect_num() >= 0) {
//            ((Item2ViewHolder) holder).tvGoodsSelectNum.setVisibility(View.VISIBLE);
//            ((Item2ViewHolder) holder).tvGoodsSelectNum.setText("" + goods.getGoods_number());
//        } else {
//            ((Item2ViewHolder) holder).tvGoodsSelectNum.setVisibility(View.GONE);
//        }
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goods.getGoods_image(), WeakImageViewUtil.getImageView(((ShopCarAdapter.Item2ViewHolder) holder).food_iv_menu));
        //判断是否隐藏
        isSelected(goodsNum[position], ((ShopCarAdapter.Item2ViewHolder) holder));
        //删除商品
        ((Item2ViewHolder) holder).delete_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteShop(goods.getGoods_id(), position);
            }
        });
        //添加商品
        ((ShopCarAdapter.Item2ViewHolder) holder).ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsNum[position]++;
                // TODO: 2016/2/27 添加购物车
                int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                buyImg = new ImageView(context);
                buyImg.setBackgroundResource(R.mipmap.icon_goods_add_item);// 设置buyImg的图片
                buyNum++;
                TOTAL = TOTAL + Double.parseDouble(goods.getShop_price());
                setAnim(buyImg, start_location);// 开始执行动画
                mOnGoodsNunChangeListener.onNumChange();
                isSelected(goodsNum[position], ((ShopCarAdapter.Item2ViewHolder) holder));
            }
        });
        ((ShopCarAdapter.Item2ViewHolder) holder).ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsNum[position] > 0) {
                    goodsNum[position]--;
                    // TODO: 2016/2/27 删除购物车内容
                    isSelected(goodsNum[position], ((ShopCarAdapter.Item2ViewHolder) holder));
                    buyNum--;
                    TOTAL = TOTAL - Double.parseDouble(goods.getShop_price());
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
    }

    /**
     * 删除商品
     */
    private void DeleteShop(String goods_id, final int position) {
        String url = Config.URL + Config.DELETESHOP;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("goods_id", goods_id);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                             @Override
                             public void onSuccess(int statusCode, JSONObject response) {
                                 super.onSuccess(statusCode, response);
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");

                                     if (status == 1) {
                                         removeItem(position);
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }

                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {

                             }


                         }
                );
    }

    /**
     * 删除数据
     *
     * @param position
     */
    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
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
        //        TextView sell_number_home;
        TextView descripttion_tv_menu;
        //添加商品
        public final ImageView ivGoodsMinus;
        public final TextView tvGoodsSelectNum;
        public final ImageView ivGoodsAdd;
        LinearLayout delete_shop;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            title_tv_menu = (TextView) itemView.findViewById(R.id.title_tv_menu);
            descripttion_tv_menu = (TextView) itemView.findViewById(R.id.descripttion_tv_menu);
            food_iv_menu = (ImageView) itemView.findViewById(R.id.food_iv_menu);
            price_tv_home = (TextView) itemView.findViewById(R.id.price_tv_menu);
//            sell_number_home = (TextView) itemView.findViewById(R.id.sellnumber_tv_menu);
            delete_shop = (LinearLayout) itemView.findViewById(R.id.delete_shop);
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
//        shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
        // 计算位移
        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                -endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, -10);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
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
    private void isSelected(int i, ShopCarAdapter.Item2ViewHolder vh) {
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
    public void changeShopCart() {
        if (TOTAL > 0) {
            shop_LL.setVisibility(View.VISIBLE);
            DecimalFormat decimalFormat = new DecimalFormat("######0.00");
            shopping_totalMoney_Tv.setText("¥" + decimalFormat.format(TOTAL));
        } else {
            shop_LL.setVisibility(View.GONE);
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

    public void setOnShopCartGoodsChangeListener(ShopCarAdapter.OnShopCartGoodsChangeListener e) {
        mOnGoodsNunChangeListener = e;
    }

    public void setOnItemClickListener(ShopCarAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NewGoods.Goods data);
    }
}

