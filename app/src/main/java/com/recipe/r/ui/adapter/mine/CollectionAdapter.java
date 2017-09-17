package com.recipe.r.ui.adapter.mine;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.CollectionItem;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.CancelPop;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hj on 2017/7/8.
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<CollectionItem.Collecyion> mDatas = null;
    private CancelPop mPop;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private MyOkHttp mMyOkhttp;

    public CollectionAdapter(Activity context, MyOkHttp mMyOkhttp) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<CollectionItem.Collecyion>();
        }
        if (mMyOkhttp != null) {
            this.mMyOkhttp = mMyOkhttp;
        }
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void updatelist(ArrayList<CollectionItem.Collecyion> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<CollectionItem.Collecyion> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new CollectionAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_collection, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CollectionItem.Collecyion collection = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((CollectionAdapter.Item2ViewHolder) holder).title_collection.setText(collection.getGoods_name());
        if (TextUtils.isEmpty(collection.getGoods_summary())) {
            ((CollectionAdapter.Item2ViewHolder) holder).content_collection.setText("暂无商品简介");
        } else {
            ((CollectionAdapter.Item2ViewHolder) holder).content_collection.setText("" + collection.getGoods_summary());
        }
        ((CollectionAdapter.Item2ViewHolder) holder).price_collection.setText("价格:¥" + collection.getShop_price());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + collection.getGoods_image(), WeakImageViewUtil.getImageView(((CollectionAdapter.Item2ViewHolder) holder).iv_collection));
        ((CollectionAdapter.Item2ViewHolder) holder).cancel_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCollect(collection.getGoods_id(), position);
            }
        });
        ((CollectionAdapter.Item2ViewHolder) holder).subscribe_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog(context).builder()
                        .setTitle("提示").setMsg("是否立即添加至购物车？")
                        .setPositiveButton("确认", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                addShopCart(collection.getGoods_id(), "1");
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (CollectionItem.Collecyion) view.getTag());
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
        ImageView iv_collection;
        TextView title_collection;
        TextView content_collection;
        TextView price_collection;
        Button cancel_collection;
        Button subscribe_collection;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            iv_collection = (ImageView) itemView.findViewById(R.id.iv_collection);
            title_collection = (TextView) itemView.findViewById(R.id.title_collection);
            content_collection = (TextView) itemView.findViewById(R.id.content_collection);
            price_collection = (TextView) itemView.findViewById(R.id.price_collection);
            cancel_collection = (Button) itemView.findViewById(R.id.cancel_collection);
            subscribe_collection = (Button) itemView.findViewById(R.id.subscribe_collection);
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
    public ArrayList<CollectionItem.Collecyion> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CollectionItem.Collecyion data);
    }

    /**
     * 取消产品收藏
     */
    private void deleteCollect(String goodId, final int position) {
        String url = Config.URL + Config.COLLECT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","andriod");
        params.put("goods_id", goodId);
        params.put("type", "0");//0，取消收藏，1，收藏，默认为1
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");
                            ToastUtil.show(context, info, 100);
                            if (status == 1) {
                                removeItem(position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("Collection", error_msg);
                    }
                });
    }

    /**
     * 添加购物车方法
     */
    private void addShopCart(String goodId, String goods_number) {

        String url = Config.URL + Config.ADDCART;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","andriod");
        params.put("goods_id", goodId);
        params.put("goods_number", goods_number);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {

                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }
}
