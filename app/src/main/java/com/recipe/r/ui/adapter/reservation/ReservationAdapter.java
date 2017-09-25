package com.recipe.r.ui.adapter.reservation;

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
import com.recipe.r.entity.ReservationItem;
import com.recipe.r.payment.ZhiFuBaoPay;
import com.recipe.r.ui.activity.home.MainActivity;
import com.recipe.r.ui.activity.reservation.ReservationActivity;
import com.recipe.r.ui.activity.table.QuickTableActivity;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.PayWayDialog;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.DateUtil;
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
import java.util.Map;

/**
 * 2017
 * 06
 * 2017/6/20
 * wangxiaoer
 * 功能描述：
 **/
public class ReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<ReservationItem.OrderInfo> mDatas = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private MyOkHttp mMyOkhttp;
    private String TYPE;
    private boolean isPay = false;
    private PayWayDialog pay_dialog;

    public ReservationAdapter(Activity context, MyOkHttp mMyOkhttp) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<ReservationItem.OrderInfo>();
        }
        if (mMyOkhttp != null) {
            this.mMyOkhttp = mMyOkhttp;
        }
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void updatelist(ArrayList<ReservationItem.OrderInfo> list, String TYPE) {
        this.mDatas.clear();
        this.mDatas = list;
        this.TYPE = TYPE;
        notifyDataSetChanged();
    }

    public void append(ArrayList<ReservationItem.OrderInfo> list, String TYPE) {
        this.mDatas.addAll(list);
        this.TYPE = TYPE;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ReservationAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_reservation, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ReservationItem.OrderInfo reservationItem = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        if (reservationItem.getShop_info() != null) {
            ((ReservationAdapter.Item2ViewHolder) holder).name_reservation.setText(reservationItem.getShop_info().getShop_name());
        }
        //TODO  订单状态,-2,订单无效,-1,订单取消,0,待付款,1,已付款,2,已发货,3,已完成,4,申请退款,5,拒绝退款,6,退款成功
        if (reservationItem.getStatus() == -2) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("订单无效");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == -1) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("订单取消");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 0) {
            isPay = true;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("支付定金");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("待付款");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("取消订单");
        } else if (reservationItem.getStatus() == 1) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("已付款");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 2) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("已发货");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 3) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("已完成");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 4) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("申请退款");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 5) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("拒绝退款");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        } else if (reservationItem.getStatus() == 6) {
            isPay = false;
            ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setText("再次预订");
            ((ReservationAdapter.Item2ViewHolder) holder).status_reservation.setText("退款成功");
            ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setText("删除订单");
        }
        ((ReservationAdapter.Item2ViewHolder) holder).time_reservation.setText(DateUtil.getDateToString2(Long.parseLong(reservationItem.getOrder_time())));
        ((ReservationAdapter.Item2ViewHolder) holder).table_reservation.setText("桌号：" + reservationItem.getTable_name());//TODO 标注
//        if (TextUtils.isEmpty(reservationItem.getRemark())) {
//            ((ReservationAdapter.Item2ViewHolder) holder).content_reservation.setText("暂无备注信息");
//        } else {
//            ((ReservationAdapter.Item2ViewHolder) holder).content_reservation.setText(reservationItem.getRemark());
//        }
        //TODO 备注信息改为点菜清单
        if (reservationItem.getGoods_info().size() > 0) {
            String content = "";
            for (int i = 0; i < reservationItem.getGoods_info().size(); i++) {
                content = content + reservationItem.getGoods_info().get(i).getGoods_name() + "、";
            }
            ((ReservationAdapter.Item2ViewHolder) holder).content_reservation.setText(content);
        } else {
            ((ReservationAdapter.Item2ViewHolder) holder).content_reservation.setText("尚未点菜");
            ((ReservationAdapter.Item2ViewHolder) holder).content_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //尚未点菜
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("type", "menu");
                    context.startActivity(intent);
                    context.finish();
                }
            });
        }
        ((ReservationAdapter.Item2ViewHolder) holder).number_reservation.setText(reservationItem.getPeople_num() + "人");
        ((ReservationAdapter.Item2ViewHolder) holder).price_reservation.setText("" + reservationItem.getFinal_total());
        ((ReservationAdapter.Item2ViewHolder) holder).deposit_reservation.setText("" + reservationItem.getPaid_total());
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + reservationItem.getTable_image(), WeakImageViewUtil.getImageView(((ReservationAdapter.Item2ViewHolder) holder).iv_reservation));
        ((ReservationAdapter.Item2ViewHolder) holder).pay_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPay) {
                    //TODO 再次订桌
                    Intent intent = new Intent(context, QuickTableActivity.class);
                    intent.putExtra("mark", "order");
                    context.startActivity(intent);
                    AppManager.getInstance().killActivity(ReservationActivity.class);
                } else {
                    //TODO 支付订单
                    PayConfirmOrder(reservationItem.getOrder_sn());
                }
            }
        });

        ((ReservationAdapter.Item2ViewHolder) holder).cancel_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 不是待付款订单
                new AlertDialog(context).builder()
                        .setTitle("提示").setMsg("是否删除订单？")
                        .setPositiveButton(context.getString(R.string.str_sure), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                DeleteOrder(reservationItem.getOrder_id(), position, "-3");
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
                    mOnItemClickListener.onItemClick(view, (ReservationItem.OrderInfo) view.getTag());
                }

            }
        });
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

        ImageView iv_reservation;
        TextView name_reservation;
        TextView time_reservation;
        TextView table_reservation;
        TextView content_reservation;
        TextView number_reservation;
        TextView price_reservation;
        TextView deposit_reservation;
        Button pay_reservation;
        Button cancel_reservation;
        TextView status_reservation;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            iv_reservation = (ImageView) itemView.findViewById(R.id.iv_reservation);
            name_reservation = (TextView) itemView.findViewById(R.id.name_reservation);
            time_reservation = (TextView) itemView.findViewById(R.id.time_reservation);
            table_reservation = (TextView) itemView.findViewById(R.id.table_reservation);
            content_reservation = (TextView) itemView.findViewById(R.id.content_reservation);
            number_reservation = (TextView) itemView.findViewById(R.id.number_reservation);
            price_reservation = (TextView) itemView.findViewById(R.id.price_reservation);
            deposit_reservation = (TextView) itemView.findViewById(R.id.deposit_reservation);
            pay_reservation = (Button) itemView.findViewById(R.id.pay_reservation);
            cancel_reservation = (Button) itemView.findViewById(R.id.cancel_reservation);
            status_reservation = (TextView) itemView.findViewById(R.id.status_reservation);
        }
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
                                 super.onSuccess(statusCode, response);
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");

                                     if (status == 1) {
                                         JSONObject data = response.getJSONObject("data");
                                         if (data.getInt("status")==0
                                                 &&data.getJSONObject("goods").has("count")
                                                 && data.getJSONObject("goods").getDouble("count")==0){
                                             initDialog(data.getString("order_sn"), Double.valueOf(100));
                                         }else {
                                             String key = data.getJSONObject("goods").getInt("order_type") == 1 ? "remain_total" : "final_total";
                                             initDialog(data.getString("order_sn"), data.getJSONObject("goods").getDouble(key));
                                         }
                                     } else {
                                         //TODO 订单无效
                                         ToastUtil.show(context, info, 100);
                                         Intent intent = new Intent(context, MainActivity.class);
                                         intent.putExtra("type", "menu");
                                         context.startActivity(intent);
                                         AppManager.getInstance().killActivity(ReservationActivity.class);
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
    public ArrayList<ReservationItem.OrderInfo> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ReservationItem.OrderInfo data);
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