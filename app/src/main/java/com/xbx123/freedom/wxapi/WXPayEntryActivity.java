package com.xbx123.freedom.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.Util;

import android.app.AlertDialog;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by EricYuan on 2016/7/18.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private LocalBroadcastManager lBManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.WXKey);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
//        baseResp.errCode
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(Constant.actionWxPayComplete);
            intent.putExtra("BaseRespErrCode", baseResp.errCode);
            lBManager = LocalBroadcastManager.getInstance(this);
            lBManager.sendBroadcast(intent);
            finish();
        }
    }
}
