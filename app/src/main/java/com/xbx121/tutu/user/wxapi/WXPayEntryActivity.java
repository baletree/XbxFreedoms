package com.xbx121.tutu.user.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/7/18.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {
    private IWXAPI api;
    private LocalBroadcastManager lBManager = null;
    private int payResultCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.WXKey);
        api.handleIntent(getIntent(), this);
        findViewById(R.id.wxPayFinish_tv).setOnClickListener(this);
        payResultCode = -1;
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
        payResultCode = baseResp.errCode;
        if (payResultCode == 0) {
            Intent intent = new Intent(Constant.actionWxPayComplete);
            intent.putExtra("BaseRespErrCode", 0);
            lBManager = LocalBroadcastManager.getInstance(this);
            lBManager.sendBroadcast(intent);
            ((TextView) findViewById(R.id.wxPayResultInfo_tv)).setText(getString(R.string.pay_success));
        } else {
            ((TextView) findViewById(R.id.wxPayResultInfo_tv)).setText(getString(R.string.pay_fail));
            Util.showToast(this, getString(R.string.pay_fail));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wxPayFinish_tv:
                finish();
                break;
        }
    }
}
