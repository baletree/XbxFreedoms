package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/16.
 * 取消订单提醒
 */
public class OrderCancelTipActivity extends BaseActivity {
    @Bind(R.id.isPayImg)
    ImageView isPayImg;
    @Bind(R.id.serverTipTv)
    TextView serverTipTv;
    private int isPay = 0;
    private int serverType = 0;
    private String orderNum = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case HttpRequestFlag.requestPageTwo:
                    String cancelData = (String) msg.obj;
                    if (UtilParse.getRequestCode(cancelData) == 0)
                        return;
                    Util.showToast(OrderCancelTipActivity.this, UtilParse.getRequestMsg(cancelData));
                    if (UtilParse.getRequestCode(cancelData) == 1) {//支付违约金
                        if (isPay == 1) {
                            intent.setClass(OrderCancelTipActivity.this, OrderPayActivity.class);
                            intent.putExtra("PayOrderNum", orderNum);
                            startActivity(intent);
                        }
                        LocalBroadcastManager lBManager = LocalBroadcastManager.getInstance(OrderCancelTipActivity.this);
                        lBManager.sendBroadcast(intent.setAction(Constant.actionCancelOrderSuc));
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order_tip);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        isPay = getIntent().getIntExtra("isPay", 0);
        orderNum = getIntent().getStringExtra("orderNum");
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.cancelOrderTitle));
        findViewById(R.id.titleLeft_img).setVisibility(View.GONE);
        if (isPay == 0)
            isPayImg.setImageResource(R.mipmap.free_cancel);
        else
            isPayImg.setImageResource(R.mipmap.fee_cancel);
    }

    @OnClick({R.id.noCancelBtn, R.id.yesCancelBtn, R.id.titleLeft_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noCancelBtn:
                finish();
                break;
            case R.id.yesCancelBtn:
                api.cancelOrder(orderNum, "1");
                break;
        }
    }
}
