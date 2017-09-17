package com.recipe.r.ui.adapter.adress;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.entity.ExpressDelivery;

import java.util.List;

/**

 * 快递配送适配器
 * 2017/7/31
 * wangxiaoer
 * 功能描述：
 **/
public class ExpressDeliveryRvAdapter extends RecyclerView.Adapter<ExpressDeliveryRvAdapter.MyViewHolder> {
    private List<ExpressDelivery> datas;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ExpressDeliveryRvAdapter(Context context, List<ExpressDelivery> datas) {
        this.datas = datas;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(mLayoutInflater.inflate(R.layout.express_delivery_rv_layout, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        myViewHolder.tv.setText(datas.get(i).getMethod());
        /**
         * 根据选中状态来设置item的背景和字体颜色
         */
        if (datas.get(i).isClick()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                myViewHolder.tv.setBackground(ContextCompat.getDrawable(context, R.drawable.maincolor_nostroke_item_unclick));
            } else {
                myViewHolder.tv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.maincolor_nostroke_item_unclick));
            }
            myViewHolder.tv.setTextColor(context.getResources().getColor(R.color.main_color));
            myViewHolder.iv.setVisibility(View.VISIBLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                myViewHolder.tv.setBackground(ContextCompat.getDrawable(context, R.drawable.gray_nostroke_item_unclick));
            } else {
                myViewHolder.tv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gray_nostroke_item_unclick));
            }
            myViewHolder.tv.setTextColor(context.getResources().getColor(R.color.lighter_black));
            myViewHolder.iv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.express_delivery_Rv_Tv);
            iv = (ImageView) itemView.findViewById(R.id.express_delivery_Rv_Iv);
        }
    }
}
