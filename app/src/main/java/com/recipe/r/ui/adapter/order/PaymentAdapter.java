package com.recipe.r.ui.adapter.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.recipe.r.ui.activity.order.PaymentActivity;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.CancelPop;
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
 * Created by hj on 2017/6/20.
 * 待付款适配器
 */
public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity context;
    // 接收数据集
    private ArrayList<PaymentItem.OrderInfo> mDatas = null;
    private CancelPop mPop;
    private View parentView;
    private String TYPE;
    private MyOkHttp mMyOkhttp;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean isPay = false;
    private PayWayDialog pay_dialog;

    public PaymentAdapter(Activity context, View parentView, String type, MyOkHttp mMyOkhttp) {
        this.context = context;
        if (mDatas == null) {
            mDatas = new ArrayList<PaymentItem.OrderInfo>();
        }
        if (mMyOkhttp != null) {
            this.mMyOkhttp = mMyOkhttp;
        }
        mLayoutInflater = LayoutInflater.from(context);
        this.parentView = parentView;
        this.TYPE = type;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new PaymentAdapter.Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_payment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PaymentItem.OrderInfo PaymentItem = mDatas.get(position);
        holder.itemView.setTag(mDatas.get(position));
        if (PaymentItem.getShop_info() != null) {
            if (PaymentItem.getShop_info()/*.size() > 0*/!=null) {
                ((PaymentAdapter.Item2ViewHolder) holder).name_payment.setText(PaymentItem.getShop_info()/*.get(0)*/.getShop_name());
            } else {
                ((PaymentAdapter.Item2ViewHolder) holder).name_payment.setText(PaymentItem.getConsignee());
            }
        } else {
            ((PaymentAdapter.Item2ViewHolder) holder).name_payment.setText(PaymentItem.getConsignee());
        }
        //TODO 订单状态,-2,订单无效,-1,订单取消,0,待付款,1,已付款,2,已发货,3,已完成,4,申请退款,5,拒绝退款,6,退款成功
        if (PaymentItem.getStatus() == -2) {
            isPay = false;
//            ((Item2ViewHolder) holder).status_payment.setText("订单无效");
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
        } else if (PaymentItem.getStatus() == -1) {
            isPay = false;
//            ((Item2ViewHolder) holder).status_payment.setText("订单取消");
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
        } else if (PaymentItem.getStatus() == 0) {
//            ((Item2ViewHolder) holder).status_payment.setText("待付款");
            isPay = true;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
        } else if (PaymentItem.getStatus() == 1) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("已付款");
        } else if (PaymentItem.getStatus() == 2) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("已发货");
        } else if (PaymentItem.getStatus() == 3) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("已完成");
        } else if (PaymentItem.getStatus() == 4) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("申请退款");
        } else if (PaymentItem.getStatus() == 5) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("拒绝退款");
        } else if (PaymentItem.getStatus() == 6) {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("退款成功");
        } else {
            isPay = false;
            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText(R.string.str_pay);
//            ((Item2ViewHolder) holder).status_payment.setText("订单状态未知");
        }
        if (TYPE.equals("1")) {
            ((PaymentAdapter.Item2ViewHolder) holder).time_payment.setTextColor(context.getResources().getColor(R.color.main_red));
            if (!TextUtils.isEmpty(PaymentItem.getOrder_time())) {
                ((PaymentAdapter.Item2ViewHolder) holder).time_payment.setText(DateUtil.getDateToString2(Long.parseLong(PaymentItem.getOrder_time())));
            }
        } else {
            ((PaymentAdapter.Item2ViewHolder) holder).time_payment.setTextColor(context.getResources().getColor(R.color.text_color));
            if (!TextUtils.isEmpty(PaymentItem.getOrder_time())) {
                ((PaymentAdapter.Item2ViewHolder) holder).time_payment.setText(DateUtil.getDateToString2(Long.parseLong(PaymentItem.getOrder_time())));
            }
        }
        ((PaymentAdapter.Item2ViewHolder) holder).table_payment.setText("桌号" + PaymentItem.getTable_id());
        if (PaymentItem.getGoods_info().size() > 0) {
            String content = "";
            for (int i = 0; i < PaymentItem.getGoods_info().size(); i++) {
                content = content + PaymentItem.getGoods_info().get(i).getGoods_name() + "、";
            }
            ((PaymentAdapter.Item2ViewHolder) holder).content_payment.setText(content);
        } else {
            ((PaymentAdapter.Item2ViewHolder) holder).content_payment.setText("尚未点菜");
            ((PaymentAdapter.Item2ViewHolder) holder).content_payment.setOnClickListener(new View.OnClickListener() {
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
        ((PaymentAdapter.Item2ViewHolder) holder).number_payment.setText("共计" + PaymentItem.getPeople_num() + "人");
        ((PaymentAdapter.Item2ViewHolder) holder).price_payment.setText("￥" + (PaymentItem.getOrder_type().equals("1")?PaymentItem.getRemain_total():PaymentItem.getFinal_total()));
        ((PaymentAdapter.Item2ViewHolder) holder).deposit_payment.setText("￥" + PaymentItem.getPaid_total());
        if (!TextUtils.isEmpty(PaymentItem.getTable_image())) {
            ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + PaymentItem.getTable_image(), WeakImageViewUtil.getImageView(((PaymentAdapter.Item2ViewHolder) holder).iv_payment));
        } else {
            if (PaymentItem.getGoods_info().size() != 0) {
                if (!TextUtils.isEmpty(PaymentItem.getGoods_info().get(0).getGoods_image())) {
                    ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + PaymentItem.getGoods_info().get(0).getGoods_image(), WeakImageViewUtil.getImageView(((PaymentAdapter.Item2ViewHolder) holder).iv_payment));
                } else {
                    ((PaymentAdapter.Item2ViewHolder) holder).iv_payment.setImageResource(R.mipmap.default_photo);
                }
            } else {
                ((PaymentAdapter.Item2ViewHolder) holder).iv_payment.setImageResource(R.mipmap.default_photo);
            }
        }
//        if (TYPE.equals("0")) {
        ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPay) {
//                    //TODO 不是待付款订单
//                    new AlertDialog(context).builder()
//                            .setTitle("提示").setMsg("是否删除订单？")
//                            .setPositiveButton(context.getString(R.string.str_sure), new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
//                                }
//                            }).setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    }).show();
                    //TODO 支付订单
                    PayConfirmOrder(PaymentItem.getOrder_sn());
                } else {
                    //TODO 支付订单
                    PayConfirmOrder(PaymentItem.getOrder_sn());
                }
            }
        });
        ((PaymentAdapter.Item2ViewHolder) holder).cancel_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog(context).builder()
                        .setTitle("提示").setMsg("是否删除订单？")
                        .setPositiveButton(context.getString(R.string.str_sure), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
//                //TODO 再来一单
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("type", "menu");
//                context.startActivity(intent);
//                AppManager.getInstance().killActivity(PaymentActivity.class);
            }
        });
//        } else {
//            ((PaymentAdapter.Item2ViewHolder) holder).cancel_payment.setVisibility(View.GONE);
//            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setText("确认收货");
//            ((PaymentAdapter.Item2ViewHolder) holder).pay_payment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ToastUtil.show(context, "收货" + position, 100);
//                }
//            });
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (PaymentItem.OrderInfo) view.getTag());
                }

            }
        });
    }

//    /**
//     * 删除订单的弹窗
//     */
//    private void DeletePop() {
//        mPop = new CancelPop(context);
//        mPop.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        mPop.setOnItemClickListener(new CancelPop.OnItemClickListener() {
//            @Override
//            public void setOnItemClick(View v) {
//                // TODO Auto-generated method stub
//                switch (v.getId()) {
//                    case R.id.id_btn_no:
//                        DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
//                        mPop.dismiss();
//                        break;
//                    case R.id.id_btn_error:
//                        DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
//                        mPop.dismiss();
//                        break;
//                    case R.id.id_btn_stop:
//                        DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
//                        mPop.dismiss();
//                        break;
//                    case R.id.id_btn_other:
//                        DeleteOrder(PaymentItem.getOrder_id(), position, "-3");
//                        mPop.dismiss();
//                        break;
//                    case R.id.id_btn_cancel:
//                        mPop.dismiss();
//                        break;
//                }
//            }
//        });
//    }

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

        ImageView iv_payment;
//      TextView status_payment;
        TextView name_payment;
        TextView time_payment;
        TextView table_payment;
        TextView content_payment;
        TextView number_payment;
        TextView price_payment;
        TextView deposit_payment;
        Button pay_payment;
        Button cancel_payment;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            iv_payment = (ImageView) itemView.findViewById(R.id.iv_payment);
            name_payment = (TextView) itemView.findViewById(R.id.name_payment);
//            status_payment = (TextView) itemView.findViewById(R.id.status_payment);
            time_payment = (TextView) itemView.findViewById(R.id.time_payment);
            table_payment = (TextView) itemView.findViewById(R.id.table_payment);
            content_payment = (TextView) itemView.findViewById(R.id.content_payment);
            number_payment = (TextView) itemView.findViewById(R.id.number_payment);
            price_payment = (TextView) itemView.findViewById(R.id.price_payment);
            deposit_payment = (TextView) itemView.findViewById(R.id.deposit_payment);
            pay_payment = (Button) itemView.findViewById(R.id.pay_payment);
            cancel_payment = (Button) itemView.findViewById(R.id.cancel_payment);
        }
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
                                 Log.e("onSuccess",response.toString());
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");
                                     JSONObject data = response.getJSONObject("data");
                                     if (status == 1) {
                                         if (data.getInt("status")==0
                                                 &&data.getJSONObject("goods").has("count")
                                                 && data.getJSONObject("goods").getDouble("count")==0){
                                             initDialog(data.getString("order_sn"),"prepay", Double.valueOf(100));
                                         }else {
                                             String key = data.getInt("order_type") == 1 ? "remain_total" : "final_total";
                                             initDialog(data.getString("order_sn"),"finalpay", data.getJSONObject("goods").getDouble(key));
                                         }
                                     } else {
                                         ToastUtil.show(context, info, 100);
                                         Intent intent = new Intent(context, MainActivity.class);
                                         intent.putExtra("type", "menu");
                                         context.startActivity(intent);
                                         AppManager.getInstance().killActivity(PaymentActivity.class);
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
    public ArrayList<PaymentItem.OrderInfo> getDatas() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, PaymentItem.OrderInfo data);
    }

    /**
     * 初始化支付方式Dialog
     */
    private void initDialog(final String ORDER_SN, final String payType, Double totalMoney) {
        // 隐藏输入法
        pay_dialog = new PayWayDialog(context, R.style.recharge_pay_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay_dialog.payWay == 1) {
                    //支付宝支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        ZhiFuBaoPay aliPay = new ZhiFuBaoPay(context);
                        aliPay.payAliBaba(1, payType, ORDER_SN);
                    } else {
                        ToastUtil.show(context, "订单生成失败", 500);
                    }
                    pay_dialog.dismiss();
                } else {
                    //微信支付
                    if (!TextUtils.isEmpty(ORDER_SN)) {
                        Wx weixin_pay = new Wx(context);
                        weixin_pay.sendPayReq(ORDER_SN, payType);
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