package com.recipe.r.ui.adapter.home;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.ShareItem;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017
 * 06
 * 2017/6/19
 * wangxiaoer
 * 功能描述：
 **/
public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private List<ShareItem.Sharedata> mDatas = null;
    private HotAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public HotAdapter(Activity context) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<ShareItem.Sharedata>();
        }
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void updatelist(ArrayList<ShareItem.Sharedata> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<ShareItem.Sharedata> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new HotAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_hot, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ShareItem.Sharedata goods = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((HotAdapter.Item2ViewHolder) holder).goods_title.setText(goods.getTitle());
        if (goods.getImages() != null) {
            if (goods.getImages().size() != 0) {
                ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + goods.getImages().get(0), WeakImageViewUtil.getImageView(((Item2ViewHolder) holder).goods_image));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (ShareItem.Sharedata) view.getTag());
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
        TextView goods_title;
        ImageView goods_image;


        public Item2ViewHolder(View itemView) {
            super(itemView);
            goods_title = (TextView) itemView.findViewById(R.id.goods_title);
            goods_image = (ImageView) itemView.findViewById(R.id.goods_image);
        }
    }


    public void setOnItemClickListener(HotAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ShareItem.Sharedata data);
    }
}

