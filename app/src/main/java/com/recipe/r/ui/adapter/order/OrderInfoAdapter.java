package com.recipe.r.ui.adapter.order;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.OrderGoods;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * Created by hj on 2017/8/30.
 */

public class OrderInfoAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<OrderGoods> mDatas = null;

    public OrderInfoAdapter(Activity context, ArrayList<OrderGoods> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public OrderGoods getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Item2ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_orderdetails, null);
            holder = new Item2ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (Item2ViewHolder) view.getTag();
        }
        final OrderGoods goodsinfo = mDatas.get(position);
        holder.name_orderdetails.setText(goodsinfo.getGoods_name());
        holder.num_orderdetails.setText("×" + goodsinfo.getGoods_number());
        holder.price_orderdetails.setText("¥" + goodsinfo.getShop_price());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goodsinfo.getGoods_image(), WeakImageViewUtil.getImageView(holder.shop_orderinfo));
        return view;
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder {
        TextView name_orderdetails;
        TextView price_orderdetails;
        TextView num_orderdetails;
        ImageView shop_orderinfo;

        public Item2ViewHolder(View itemView) {
            name_orderdetails = (TextView) itemView.findViewById(R.id.name_orderdetails);
            price_orderdetails = (TextView) itemView.findViewById(R.id.price_orderdetails);
            num_orderdetails = (TextView) itemView.findViewById(R.id.num_orderdetails);
            shop_orderinfo = (ImageView) itemView.findViewById(R.id.shop_orderinfo);
        }
    }

    /**
     * 删除数据
     *
     * @param position
     */
    public void removeItem(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 获取数组
     */
    public ArrayList<OrderGoods> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

}
