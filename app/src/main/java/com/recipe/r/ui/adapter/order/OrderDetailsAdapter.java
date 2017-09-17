package com.recipe.r.ui.adapter.order;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.OrderConfirmModel;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * 2017/8/1
 * wangxiaoer
 * 功能描述：确认订单适配器
 **/
public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<OrderConfirmModel.OrderGoods> mDatas = null;

    private OrderDetailsAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public OrderDetailsAdapter(Activity context, ArrayList<OrderConfirmModel.OrderGoods> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new OrderDetailsAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_orderdetails, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final OrderConfirmModel.OrderGoods goodsinfo = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((OrderDetailsAdapter.Item2ViewHolder) holder).name_orderdetails.setText(goodsinfo.getGoods_name());
        ((OrderDetailsAdapter.Item2ViewHolder) holder).num_orderdetails.setText("×" + goodsinfo.getGoods_number());
        ((OrderDetailsAdapter.Item2ViewHolder) holder).price_orderdetails.setText("¥" + goodsinfo.getShop_price());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goodsinfo.getGoods_image(), WeakImageViewUtil.getImageView(((OrderDetailsAdapter.Item2ViewHolder) holder).shop_orderinfo));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (OrderConfirmModel.OrderGoods) view.getTag());
                }

            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView name_orderdetails;
        TextView price_orderdetails;
        TextView num_orderdetails;
        ImageView shop_orderinfo;

        public Item2ViewHolder(View itemView) {
            super(itemView);
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
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    /**
     * 获取数组
     */
    public ArrayList<OrderConfirmModel.OrderGoods> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, OrderConfirmModel.OrderGoods data);
    }
}