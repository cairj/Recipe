package com.recipe.r.ui.adapter.integral;

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
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Integral;
import com.recipe.r.ui.activity.integral.IntegralMineActivity;
import com.recipe.r.ui.activity.prize.PrizeActivity;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 2017
 * 06
 * 2017/6/22
 * wangxiaoer
 * 功能描述：积分兑换适配
 **/
public class ExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    // 接收数据集
    private ArrayList<Integral.IntegralData> mDatas = null;
    private AlertDialog successDialog;
    private AlertDialog exchangeDialog;
    private MyOkHttp mMyOkhttp;

    public ExchangeAdapter(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        this.context = context;
        exchangeDialog = new AlertDialog(context);
        successDialog = new AlertDialog(context);
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

        return new ExchangeAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_exchange, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Integral.IntegralData exchangeItem = mDatas.get(position);
        ((ExchangeAdapter.Item2ViewHolder) holder).tv_name_exchange.setText(exchangeItem.getGoods_name());
        ((ExchangeAdapter.Item2ViewHolder) holder).tv_integral_exchange.setText(exchangeItem.getPoint() + "积分");
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + exchangeItem.getGoods_image(), WeakImageViewUtil.getImageView(((ExchangeAdapter.Item2ViewHolder) holder).iv_exchange));
        ((ExchangeAdapter.Item2ViewHolder) holder).exchange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exchangeDialog.builder().setTitle("兑换礼品:" + exchangeItem.getGoods_name())
                        .setMsg("将扣除积分:" + exchangeItem.getPoint() + "积分")
                        .setPositiveButton("确认", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                setDeduct(exchangeItem.getGoods_id());
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exchangeDialog.dismiss();
                    }
                }).setTitleColor(context.getResources().getColor(R.color.text_color)).setMsgColor(context.getResources().getColor(R.color.main_red)).show();
            }
        });
    }

    /**
     * 设置扣除积分兑换商品
     *
     * @param good_id
     */
    private void setDeduct(String good_id) {
        //点击兑换
        String url = Config.URL + Config.POINTEXCHANGE;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","andriod");
        params.put("goods_id", good_id);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {

                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            
                            if (status == 1) {
                                successDialog.builder().setTitle("恭喜您成功兑换" )
//                                        .setMsg("您的剩余积分:" + exchangeItem.getPoint() + "积分")
                                        .setPositiveButton("确认", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, PrizeActivity.class);
                                                context.startActivity(intent);
                                                AppManager.getInstance().killActivity(IntegralMineActivity.class);
                                                successDialog.dismiss();
                                            }
                                        }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        successDialog.dismiss();
                                    }
                                }).setTitleColor(context.getResources().getColor(R.color.main_red)).setMsgColor(context.getResources().getColor(R.color.text_color)).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name_exchange;
        TextView tv_integral_exchange;
        Button exchange_btn;
        ImageView iv_exchange;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            tv_name_exchange = (TextView) itemView.findViewById(R.id.tv_name_exchange);
            tv_integral_exchange = (TextView) itemView.findViewById(R.id.tv_integral_exchange);
            exchange_btn = (Button) itemView.findViewById(R.id.exchange_btn);
            iv_exchange = (ImageView) itemView.findViewById(R.id.iv_exchange);
        }
    }
}