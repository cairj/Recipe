package com.recipe.r.ui.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.MenuItem;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/13
 * wangxiaoer
 * 功能描述：我的推荐菜单适配器
 **/
public class RecommendedMineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<MenuItem.MenuInfo> mDatas = new ArrayList<>();

    public RecommendedMineAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
    }

    public void updatelist(ArrayList<MenuItem.MenuInfo> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<MenuItem.MenuInfo> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecommendedMineAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_recommended_division, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MenuItem.MenuInfo menuItem = mDatas.get(position);
        ((RecommendedMineAdapter.Item2ViewHolder) holder).title_tv_recommended.setText(menuItem.getGoods_name());
        ((RecommendedMineAdapter.Item2ViewHolder) holder).sellnumber_tv_recommended.setText("已售出" + menuItem.getSold_num() + "份");
        ShowImageUtils.showImageView(context, R.mipmap.logo, Config.IMAGE_URL + menuItem.getGoods_image(), WeakImageViewUtil.getImageView(((RecommendedMineAdapter.Item2ViewHolder) holder).recommended_iv));
        //是否审核通过
        if (menuItem.getStatus() == 0) {
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setText("审核通过");
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setTextColor(context.getResources().getColor(R.color.prize_blue));
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setBackgroundResource(R.mipmap.approved);
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setClickable(true);
        } else if (menuItem.getStatus() == 1) {
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setText("审核中");
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setTextColor(context.getResources().getColor(R.color.text_blue));
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setBackgroundResource(R.mipmap.approved_ongoing);
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setClickable(false);
        } else if (menuItem.getStatus() == 2) {
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setText("审核未通过");
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setTextColor(context.getResources().getColor(R.color.red_recommend));
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setBackgroundResource(R.mipmap.approved_nopass);
            ((RecommendedMineAdapter.Item2ViewHolder) holder).audit_btn.setClickable(false);
        }

    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv_recommended;
        ImageView recommended_iv;
        Button audit_btn;
        TextView sellnumber_tv_recommended;


        public Item2ViewHolder(View itemView) {
            super(itemView);
            title_tv_recommended = (TextView) itemView.findViewById(R.id.title_tv_recommended);
            audit_btn = (Button) itemView.findViewById(R.id.audit_btn);
            recommended_iv = (ImageView) itemView.findViewById(R.id.recommended_iv);
            sellnumber_tv_recommended = (TextView) itemView.findViewById(R.id.sellnumber_tv_recommended);
        }
    }

    /**
     * 获取数组
     */
    public ArrayList<MenuItem.MenuInfo> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

}