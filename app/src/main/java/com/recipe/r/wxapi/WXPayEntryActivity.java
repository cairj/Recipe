package com.recipe.r.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.recipe.r.R;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.events.MessageEvent;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.dialog.MyAlertDialog;
import com.recipe.r.utils.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信支付回调界面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConfigApp.WX_PP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(final BaseResp resp) {
        Logger.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            final MyAlertDialog dialog = new MyAlertDialog(this);
            //String.valueOf(resp.errCode)
            if (resp.errCode == 0) {
                dialog.oneYes(getString(R.string.tip), getString(R.string.wx_success), getString(R.string.sure_str));
            } else if(resp.errCode == -1) {
                dialog.oneYes(getString(R.string.tip), getString(R.string.wx_fail), getString(R.string.sure_str));
            }else{
                dialog.oneYes(getString(R.string.tip), getString(R.string.wx_cancel), getString(R.string.sure_str));
            }
            dialog.yes(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (resp.errCode == 0) {
                        payResult(true, "支付成功");
                    } else {
                        payResult(false, "支付失败");
                    }
                    dialog.dismiss();
                    WXPayEntryActivity.this.finish();
                }
            });
            dialog.show();
        }
    }

    /**
     * 根据结果，显示支付详细信息
     */
    public static void payResult(boolean payState, String payString) {
        if (payState) {
            EventBus.getDefault().post(new MessageEvent(payString));
        }
    }

}