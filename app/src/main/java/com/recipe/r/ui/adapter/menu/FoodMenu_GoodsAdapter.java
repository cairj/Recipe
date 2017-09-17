package com.recipe.r.ui.adapter.menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.PaymentItem;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * 2017
 * 08
 * 2017/8/3
 * wangxiaoer
 * 功能描述：
 **/
public class FoodMenu_GoodsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PaymentItem.OrderInfo.GoodsInfo> list;
    private String TAG = "FoodMenu_GoodsAdapterextends";

    public FoodMenu_GoodsAdapter(Context context) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.context = context;
    }

    public void updatelist(ArrayList<PaymentItem.OrderInfo.GoodsInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public PaymentItem.OrderInfo.GoodsInfo getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_foodmenu,
                    null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PaymentItem.OrderInfo.GoodsInfo goodInfo = list.get(position);
        if (!TextUtils.isEmpty(goodInfo.getGoods_image())) {
            ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goodInfo.getGoods_image(), WeakImageViewUtil.getImageView(holder.food_menu));
        } else {
            holder.food_menu.setImageResource(R.mipmap.default_photo);
        }
        holder.foodmenu_name.setText(goodInfo.getGoods_name());
        holder.foodmenu_price.setText("¥" + goodInfo.getGoods_price());
        if (!TextUtils.isEmpty(goodInfo.getGoods_brief())) {
            holder.describe_menu.setText("菜品简介:" + goodInfo.getGoods_brief());
        } else {
            holder.describe_menu.setText("菜品简介暂无");
        }
        return view;
    }

    private class ViewHolder {
        ImageView food_menu;
        TextView foodmenu_name;
        TextView foodmenu_price;
        TextView describe_menu;

        public ViewHolder(View view) {
            food_menu = (ImageView) view.findViewById(R.id.food_menu);
            foodmenu_name = (TextView) view.findViewById(R.id.foodmenu_name);
            foodmenu_price = (TextView) view.findViewById(R.id.foodmenu_price);
            describe_menu = (TextView) view.findViewById(R.id.describe_menu);
        }
    }

}