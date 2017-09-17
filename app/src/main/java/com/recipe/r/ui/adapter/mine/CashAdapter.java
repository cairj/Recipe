package com.recipe.r.ui.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.entity.CashItem;
import com.recipe.r.utils.DateUtil;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/22
 * wangxiaoer
 * 功能描述：现金卷适配
 **/
public class CashAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<CashItem.Cash> mDatas = null;
    private String TYPE;

    public CashAdapter(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updatelist(ArrayList<CashItem.Cash> list, String TYPE) {
        this.mDatas.clear();
        this.mDatas = list;
        this.TYPE = TYPE;
        notifyDataSetChanged();
    }

    public void append(ArrayList<CashItem.Cash> list, String TYPE) {
        this.mDatas.addAll(list);
        this.TYPE = TYPE;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CashAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_cash, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CashItem.Cash cashItem = mDatas.get(position);
        if (TYPE.equals("1")) {
            holder.itemView.setVisibility(View.VISIBLE);
        } else if (!TYPE.equals("1") && cashItem.getStatus().equals("1")) {
            holder.itemView.setVisibility(View.GONE);
        }
        if (cashItem.getStatus().equals("1")) {
            ((CashAdapter.Item2ViewHolder) holder).iv_cash.setBackgroundResource(R.mipmap.cash_sure);
            ((Item2ViewHolder) holder).price_cash.setTextColor(context.getResources().getColor(R.color.main_red));
            ((Item2ViewHolder) holder).top_cash.setTextColor(context.getResources().getColor(R.color.main_red));
            ((Item2ViewHolder) holder).top_cash.setText("立即\n使用");
        } else if (cashItem.getStatus().equals("2")) {
            ((CashAdapter.Item2ViewHolder) holder).iv_cash.setBackgroundResource(R.mipmap.cash_no);
            ((Item2ViewHolder) holder).price_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setText("已使用");
        } else if (cashItem.getStatus().equals("3")) {
            ((CashAdapter.Item2ViewHolder) holder).iv_cash.setBackgroundResource(R.mipmap.cash_no);
            ((Item2ViewHolder) holder).price_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setText("已失效");
        } else if (cashItem.getStatus().equals("4")) {
            ((CashAdapter.Item2ViewHolder) holder).iv_cash.setBackgroundResource(R.mipmap.cash_no);
            ((Item2ViewHolder) holder).price_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setTextColor(context.getResources().getColor(R.color.text_gray));
            ((Item2ViewHolder) holder).top_cash.setText("已过期");
        }
        ((Item2ViewHolder) holder).price_cash.setText(cashItem.getFace_value() + "元现金卷");
        if (cashItem.getStatus().equals("1")) {
            ((Item2ViewHolder) holder).time_cash.setText(DateUtil.getTimeToString(Long.parseLong(cashItem.getSend_time())));
        } else {
            ((Item2ViewHolder) holder).time_cash.setText(DateUtil.getTimeToString(Long.parseLong(cashItem.getExpire_time())));
        }
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cash;
        TextView time_cash;
        TextView top_cash;
        TextView price_cash;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            time_cash = (TextView) itemView.findViewById(R.id.time_cash);
            top_cash = (TextView) itemView.findViewById(R.id.top_cash);
            price_cash = (TextView) itemView.findViewById(R.id.price_cash);
            iv_cash = (ImageView) itemView.findViewById(R.id.iv_cash);
        }
    }
}