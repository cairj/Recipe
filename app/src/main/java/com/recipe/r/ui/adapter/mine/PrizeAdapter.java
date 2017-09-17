package com.recipe.r.ui.adapter.mine;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.entity.Prize;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/20.
 * 关于奖品的适配器
 * 带滑动删除
 */
public class PrizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<Prize.Gift> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public PrizeAdapter(Activity context) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<Prize.Gift>();
        }
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void updatelist(ArrayList<Prize.Gift> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<Prize.Gift> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new PrizeAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.item_prize, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Prize.Gift prizeItem = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((PrizeAdapter.ItemViewHolder) holder).time_prize.setText(prizeItem.getTime());
        if (prizeItem.getStatus().equals("0")) {
            ((PrizeAdapter.ItemViewHolder) holder).statue_prize.setText("抽奖");
        } else {
            ((PrizeAdapter.ItemViewHolder) holder).statue_prize.setText("礼品");
        }
        if (!TextUtils.isEmpty(prizeItem.getGoods_name())) {
            ((PrizeAdapter.ItemViewHolder) holder).content_prize.setText(prizeItem.getGoods_name());
        } else {
            ((PrizeAdapter.ItemViewHolder) holder).content_prize.setText(R.string.no_prize);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (Prize.Gift) view.getTag());
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
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView time_prize;
        public TextView statue_prize;
        public TextView content_prize;
        public LinearLayout prize_layout;
        public TextView tv_text_prize;

        public ItemViewHolder(View itemView) {
            super(itemView);
            time_prize = (TextView) itemView.findViewById(R.id.time_prize);
            statue_prize = (TextView) itemView.findViewById(R.id.statue_prize);
            content_prize = (TextView) itemView.findViewById(R.id.content_prize);
            prize_layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            tv_text_prize = (TextView) itemView.findViewById(R.id.tv_text_prize);

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
    public ArrayList<Prize.Gift> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Prize.Gift data);
    }
}
