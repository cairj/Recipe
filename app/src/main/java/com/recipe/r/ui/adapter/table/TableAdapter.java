package com.recipe.r.ui.adapter.table;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.TableItem;
import com.recipe.r.ui.activity.table.QuickTableActivity;
import com.recipe.r.ui.dialog.BannerDialog;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.UserIsLogin;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/23.
 * 订桌适配器
 */
public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<TableItem.Table> mDatas = null;
    private BannerDialog dialog;


    public TableAdapter(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        dialog = new BannerDialog(this.context);
    }

    public void updatelist(ArrayList<TableItem.Table> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<TableItem.Table> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TableAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_table, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TableItem.Table Item = mDatas.get(position);
        ((Item2ViewHolder) holder).tv_person_number.setText("人数:" + Item.getCapacity() + "人");
        ((Item2ViewHolder) holder).tv_table_number.setText(Item.getTable_name());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + Item.getTable_image(), WeakImageViewUtil.getImageView(((Item2ViewHolder) holder).table_iv));
        //TODO 统一可预订
//        if (Item.getStatus() == 0) {
        ((Item2ViewHolder) holder).table_btn.setText("预订");
        ((Item2ViewHolder) holder).table_btn.setBackgroundColor(context.getResources().getColor(R.color.main_red));
        ((Item2ViewHolder) holder).table_btn.setEnabled(true);
//        } else if (Item.getStatus() == 1) {
//            ((Item2ViewHolder) holder).table_btn.setText("有人");
//            ((Item2ViewHolder) holder).table_btn.setBackgroundColor(context.getResources().getColor(R.color.text_gray));
//            ((Item2ViewHolder) holder).table_btn.setEnabled(false);
//        } else if (Item.getStatus() == 2) {
//            ((Item2ViewHolder) holder).table_btn.setText("被预订");
//            ((Item2ViewHolder) holder).table_btn.setBackgroundColor(context.getResources().getColor(R.color.text_gray));
//            ((Item2ViewHolder) holder).table_btn.setEnabled(false);
//        }
        ((Item2ViewHolder) holder).table_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setImageViews(Config.IMAGE_URL + Item.getTable_image());
                dialog.show();
            }
        });
        ((Item2ViewHolder) holder).table_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (UserIsLogin.IsLogn(context)) {
                    Intent intent = new Intent(context, QuickTableActivity.class);
                    intent.putExtra("table_id", Item.getTable_id());
                    intent.putExtra("shopname", Item.getShop_name());
                    intent.putExtra("number", Item.getCapacity());
                    intent.putExtra("table_name", Item.getTable_name());
                    context.startActivity(intent);
                //}
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        ImageView table_iv;
        TextView tv_table_number;
        TextView tv_person_number;
        Button table_btn;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            table_iv = (ImageView) itemView.findViewById(R.id.table_iv);
            tv_table_number = (TextView) itemView.findViewById(R.id.tv_table_number);
            tv_person_number = (TextView) itemView.findViewById(R.id.tv_person_number);
            table_btn = (Button) itemView.findViewById(R.id.table_btn);
        }
    }
}