package com.recipe.r.ui.adapter.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.NewGoods;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * 作者：Administrator on 2017/6/9 18:40
 * 功能:@描述
 * 首页数据适配器
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<NewGoods.Goods> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public HomeRecyclerViewAdapter(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updatelist(ArrayList<NewGoods.Goods> list) {
        this.mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void append(ArrayList<NewGoods.Goods> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_home_division, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewGoods.Goods goods = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
//        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenInfo.getWidth() / 4);
//        params.setMargins(10, 0, 10, 0);
//        (((Item2ViewHolder) holder).home_item_iv_banner).setLayoutParams(params);
//        (((Item2ViewHolder) holder).home_item_iv_banner).setScaleType(ImageView.ScaleType.FIT_XY);
        ((Item2ViewHolder) holder).home_tv_division.setText(goods.getGoods_name());
        ((Item2ViewHolder) holder).price_tv_home.setText("￥" + goods.getShop_price());
        ((Item2ViewHolder) holder).sell_number_home.setText("已售" + goods.getSold_num() + "份");
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goods.getGoods_image(), WeakImageViewUtil.getImageView(((Item2ViewHolder) holder).home_item_iv_banner));
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


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NewGoods.Goods data);
    }

    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView home_tv_division;
        ImageView home_item_iv_banner;
        TextView price_tv_home;
        TextView sell_number_home;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            home_tv_division = (TextView) itemView.findViewById(R.id.home_tv_division);
            home_item_iv_banner = (ImageView) itemView.findViewById(R.id.home_item_iv_banner);
            price_tv_home = (TextView) itemView.findViewById(R.id.price_tv_home);
            sell_number_home = (TextView) itemView.findViewById(R.id.sell_number_home);
        }
    }
}


