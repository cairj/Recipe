package com.recipe.r.ui.adapter.mine;

import android.app.Activity;
import android.content.Intent;
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
import com.recipe.r.entity.PaymentItem;
import com.recipe.r.payment.ZhiFuBaoPay;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.ui.activity.menu.MenuMineActivity;
import com.recipe.r.ui.adapter.menu.FoodMenu_GoodsAdapter;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.PayWayDialog;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.recipe.r.wxapi.Wx;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hj on 2017/6/13.
 */
public class FoodMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private List<PaymentItem.OrderInfo> mDatas = new ArrayList<>();
    private FoodMenu_GoodsAdapter adapter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean isPay = false;
    private PayWayDialog pay_dialog;
    private MyOkHttp mMyOkhttp;

    public FoodMenuAdapter(Activity context) {
        if (mDatas == null) {
            mDatas = new ArrayList<PaymentItem.OrderInfo>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updatelist(ArrayList<PaymentItem.OrderInfo> list) {
        this.mDatas.clear();
        this.mDatas = list;

        notifyDataSetChanged();
    }

    public void append(ArrayList<PaymentItem.OrderInfo> list) {
        this.mDatas.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new FoodMenuAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_menu_nopay, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PaymentItem.OrderInfo menuItem = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        ((Item2ViewHolder) holder).title_menu.setText(menuItem.getGoods_info().get(0).getGoods_name());
        if (!TextUtils.isEmpty(menuItem.getGoods_info().get(0).getGoods_summary())) {
            ((Item2ViewHolder) holder).describe_menu.setText("" + menuItem.getGoods_info().get(0).getGoods_summary());
        } else {
            ((Item2ViewHolder) holder).describe_menu.setText("暂无菜品简介");
        }
        ((Item2ViewHolder) holder).price_menu.setText("¥" + menuItem.getGoods_info().get(0).getGoods_price());
        ((Item2ViewHolder) holder).number_menu.setText("共计" + menuItem.getGoods_info().get(0).getGoods_number() + "份");
        if (menuItem.getStatus() == -2) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("订单无效");
        } else if (menuItem.getStatus() == -1) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("订单取消");
        } else if (menuItem.getStatus() == 0) {
            isPay = true;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("待付款");
        } else if (menuItem.getStatus() == 1) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("已付款");
        } else if (menuItem.getStatus() == 2) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("已发货");
        } else if (menuItem.getStatus() == 3) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("已完成");
        } else if (menuItem.getStatus() == 4) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("申请退款");
        } else if (menuItem.getStatus() == 5) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("拒绝退款");
        } else if (menuItem.getStatus() == 6) {
            isPay = false;
            ((Item2ViewHolder) holder).pay_payment.setText(R.string.str_delete_order);
            ((FoodMenuAdapter.Item2ViewHolder) holder).status_nopay.setText("退款成功");
        }
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + menuItem.getGoods_info().get(0).getGoods_image(), WeakImageViewUtil.getImageView(((FoodMenuAdapter.Item2ViewHolder) holder).food_menu));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (PaymentItem.OrderInfo) view.getTag());
                }

            }
        });
        ((Item2ViewHolder) holder).pay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPay) {
                    //TODO 不是待付款订单
                    new AlertDialog(context).builder()
                            .setTitle("提示").setMsg("是否删除订单？")
                            .setPositiveButton(context.getString(R.string.str_sure), new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    DeleteOrder(menuItem.getOrder_id(), position, "-3");
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();

                } else {
                    //TODO 支付订单
                    PayConfirmOrder(menuItem.getOrder_sn());
                }
            }
        });
        ((Item2ViewHolder) holder).cancel_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              DeletePop();
                //TODO 再来一单
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("type", "menu");
                context.startActivity(intent);
                AppManager.getInstance().killActivity(MenuMineActivity.class);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView title_menu;
        TextView price_menu;
        TextView number_menu;
        TextView time_menu;
        TextView status_nopay;
        ImageView food_menu;
        TextView describe_menu;
        Button pay_payment;
        Button cancel_payment;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            title_menu = (TextView) itemView.findViewById(R.id.title_menu);
            time_menu = (TextView) itemView.findViewById(R.id.time_menu);
            food_menu = (ImageView) itemView.findViewById(R.id.food_menu);
            price_menu = (TextView) itemView.findViewById(R.id.price_menu);
            number_menu = (TextView) itemView.findViewById(R.id.number_menu);
            status_nopay = (TextView) itemView.findViewById(R.id.status_nopay);
            describe_menu = (TextView) itemView.findViewById(R.id.describe_menu);
            pay_payment = (Button) itemView.findViewById(R.id.pay_payment);
            cancel_payment = (Button) itemView.findViewById(R.id.cancel_payment);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, PaymentItem.OrderInfo data);
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
     * 删除订单
     */
    private void DeleteOrder(String order_id, final int position, String status) {
        String url = Config.URL + Config.SETORDERSTATUS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("order_id", "" + order_id);
        params.put("status", status);//-1，取消，-3，删除
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                             @Override
                             public void onSuccess(int statusCode, JSONObject response) {
                                 super.onSuccess(statusCode, response);
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");

                                     if (status == 1) {
                                         removeItem(position);
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }

                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {

                             }


                         }
                );
    }

    /**
     * 结算付款
     */
    private void PayConfirmOrder(String order_sn) {
        String url = Config.URL + Config.PAYCONFIRM;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        params.put("order_sn", order_sn);
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                             @Override
                             public void onSuccess(int statusCode, JSONObject response) {
                                 try {
                                     int status = response.getInt("status");
                                     JSONObject data = response.getJSONObject("data");
                                     if (status == 1) {
                                         initDialog(data.getString("order_sn"), data.getJSONObject("goods").getDouble("total_amount"));
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }

                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {

                             }
                         }
                );
    }

    /**
     * 初始化支付方式Dialog
     */
    private void initDialog(final String ORDER_SN, Double totalMoney) {
        // 隐藏输入法
        pay_dialog = new PayWayDialog(context, R.style.recharge_pay_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay_dialog.payWay == 1) {
                    //支付宝支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        ZhiFuBaoPay aliPay = new ZhiFuBaoPay(context);
                        aliPay.payAliBaba(1, "0", ORDER_SN);
                    } else {
                        ToastUtil.show(context, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                } else {
                    //微信支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        Wx weixin_pay = new Wx(context);
                        weixin_pay.sendPayReq(ORDER_SN, "0");
                    } else {
                        ToastUtil.show(context, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                }

            }
        });
        pay_dialog.setPrice("" + totalMoney);
        pay_dialog.show();
    }
}
