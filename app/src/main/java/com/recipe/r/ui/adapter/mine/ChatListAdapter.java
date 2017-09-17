package com.recipe.r.ui.adapter.mine;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.UserInfo;
import com.recipe.r.ui.dialog.CancelPop;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * 2017
 * 07
 * 2017/7/17
 * wangxiaoer
 * 功能描述：
 **/
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<UserInfo.User> mDatas = null;
    private CancelPop mPop;
    private View parentView;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ChatListAdapter(Context context) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<UserInfo.User>();
        }
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void updatelist(ArrayList<UserInfo.User> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<UserInfo.User> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final UserInfo.User userinfo = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((Item2ViewHolder) holder).user_tv.setText(userinfo.getUser_name());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + userinfo.getHeadimgurl(), WeakImageViewUtil.getImageView(((Item2ViewHolder) holder).user_iv));
        ((Item2ViewHolder) holder).time_tv.setText(DateUtil.getDateToString(userinfo.getLogin_time()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (UserInfo.User) view.getTag());
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

        ImageView user_iv;
        TextView user_tv;
        TextView time_tv;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            user_iv = (ImageView) itemView.findViewById(R.id.user_iv);
            user_tv = (TextView) itemView.findViewById(R.id.user_tv);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
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
    public ArrayList<UserInfo.User> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(ChatListAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, UserInfo.User data);
    }
}