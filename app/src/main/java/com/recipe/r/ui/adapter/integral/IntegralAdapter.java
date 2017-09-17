package com.recipe.r.ui.adapter.integral;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.entity.Integral;
import com.recipe.r.utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/14.
 * 积分适配器
 */
public class IntegralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<Integral.IntegralData> mDatas = null;

    public IntegralAdapter(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updatelist(ArrayList<Integral.IntegralData> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<Integral.IntegralData> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new IntegralAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_integral, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Integral.IntegralData menuItem = mDatas.get(position);
        ((IntegralAdapter.Item2ViewHolder) holder).number_integral.setText(menuItem.getValue());
        ((IntegralAdapter.Item2ViewHolder) holder).time_integral.setText(DateUtil.getTimeToString(Long.parseLong(menuItem.getTime())));
        if (Integer.parseInt(menuItem.getType()) == 1) {
            ((IntegralAdapter.Item2ViewHolder) holder).type_integral.setText("获取");
        } else if (Integer.parseInt(menuItem.getType()) == 2) {
            ((IntegralAdapter.Item2ViewHolder) holder).type_integral.setText("兑换");
        }
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView number_integral;
        TextView time_integral;
        TextView type_integral;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            number_integral = (TextView) itemView.findViewById(R.id.number_integral);
            time_integral = (TextView) itemView.findViewById(R.id.time_integral);
            type_integral = (TextView) itemView.findViewById(R.id.type_integral);
        }
    }
}
