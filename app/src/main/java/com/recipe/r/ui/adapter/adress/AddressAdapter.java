package com.recipe.r.ui.adapter.adress;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Address;
import com.recipe.r.ui.activity.address.ModifyAddressActivity;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressAdapter extends BaseAdapter {
    private String TAG = "AddressAdapter";
    private Context context;
    private ArrayList<Address.Adress> list;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    public HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    ViewHolder holder;
    private boolean isRadio = false;
    private MyOkHttp mMyOkhttp;

    public AddressAdapter(Context context) {
        super();
        if (list == null) {
            list = new ArrayList<Address.Adress>();
        }
        if (mMyOkhttp == null) {
            mMyOkhttp = new MyOkHttp();
        }
        this.context = context;

    }

    public void updatelist(ArrayList<Address.Adress> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void append(ArrayList<Address.Adress> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();

    }

    @Override
    public Address.Adress getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_single_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Address.Adress address = list.get(position);
        // 默认第一张图片
        holder.nickname_address.setText(address.getConsignee());
        holder.phone_address.setText(address.getMobile());
        if (!TextUtils.isEmpty(address.getDistrict())) {
            holder.content_address.setText(address.getProvince()
                    + address.getCity() + address.getDistrict()
                    + address.getAddress());
        } else if (!TextUtils.isEmpty(address.getCity())) {
            holder.content_address.setText(address.getProvince()
                    + address.getCity() + address.getAddress());
        } else if (!TextUtils.isEmpty(address.getProvince())) {
            holder.content_address.setText(address.getProvince()
                    + address.getAddress());
        } else {
            holder.content_address.setText(address.getAddress());
        }
        holder.rb_light.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                isRadio = true;
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position),
                        holder.rb_light.isChecked());
                notifyDataSetChanged();
            }
        });
        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else {
            res = true;
        }
        if (!isRadio) {
            if (address.getStatus() == 1) {
                holder.rb_light.setChecked(true);
            } else {
                holder.rb_light.setChecked(false);
            }
        } else {
            holder.rb_light.setChecked(res);
        }
        holder.delete_address.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 删除
                DeteleAddress(address.getAddress_id(), position);
            }
        });
        holder.edit_address.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 编辑地址
                Intent intent = new Intent(context, ModifyAddressActivity.class);
                intent.putExtra("code", address.getZipcode());
                intent.putExtra("detailAddr", address.getAddress());
                intent.putExtra("province", address.getProvince());
                intent.putExtra("town", address.getDistrict());
                intent.putExtra("city", address.getCity());
                intent.putExtra("isDefault", address.getStatus());
                intent.putExtra("receiverName", address.getConsignee());
                intent.putExtra("receiverPhone", address.getMobile());
                intent.putExtra("receiverAddrId", address.getUser_id());
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        RadioButton rb_light;
        TextView nickname_address;
        TextView phone_address;
        TextView content_address;
        Button edit_address;
        Button delete_address;
        LinearLayout ll_rb_light;

        public ViewHolder(View view) {
            rb_light = (RadioButton) view.findViewById(R.id.rb_light);
            edit_address = (Button) view.findViewById(R.id.edit_address);
            delete_address = (Button) view.findViewById(R.id.delete_address);
            nickname_address = (TextView) view
                    .findViewById(R.id.nickname_address);
            phone_address = (TextView) view.findViewById(R.id.phone_address);
            content_address = (TextView) view
                    .findViewById(R.id.content_address);
            ll_rb_light = (LinearLayout) view.findViewById(R.id.ll_rb_light);
        }
    }

    //删除地址接口
    private void DeteleAddress(String addrid, final int position) {
        String url = Config.URL + Config.SETUSERADDRESSTODEFAULT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("address_id", addrid);
        params.put("status", "-1");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        int status = 0;
                        try {
                            status = response.getInt("status");
                            String info = response.getString("info");
                            
                            if (status == 1) {
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 初始化选中
     * @param position
     */
    public void SetRB(int position) {
        isRadio = true;
        holder.rb_light.setChecked(true);
        for (String key : states.keySet()) {
            states.put(key, false);
        }
        states.put(String.valueOf(position),
                holder.rb_light.isChecked());
        notifyDataSetChanged();
    }
//	 public HashMap<String, Boolean> getIsStates() {
//		 return states;
//	 }
//	
//	 public void setIsStates(HashMap<String, Boolean> states) {
//		 this.states = states;
//	 }
}
