package com.xbx123.freedom.ui.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.OrderDetailBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.pay.alipay.Alipay;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/26.
 * 订单支付
 */
public class OrderPayActivity extends BaseActivity implements Alipay.AlipayCallBack {
    @Bind(R.id.serverHead_img)
    RoundedImageView serverHeadImg;
    @Bind(R.id.serverName_tv)
    TextView serverNameTv;
    @Bind(R.id.serverIdCard_tv)
    TextView serverIdCardTv;
    @Bind(R.id.serverInfo_rtb)
    RatingBar serverInfoRtb;
    @Bind(R.id.serverTime_tv)
    TextView serverTimeTv;

    @Bind(R.id.payAttentionTip_tv)
    TextView payAttentionTipTv;
    @Bind(R.id.orderInfoPrice_tv)
    TextView orderInfoPriceTv;
    @Bind(R.id.orderInfoLocate_tv)
    TextView orderInfoLocateTv;
    @Bind(R.id.orderInfoNum_tv)
    TextView orderInfoNumTv;
    @Bind(R.id.payReward_tv)
    TextView payRewardTv;
    @Bind(R.id.payCouponPrice_tv)
    TextView payCouponPriceTv;
    @Bind(R.id.payCouponType_tv)
    TextView payCouponTypeTv;

    @Bind(R.id.payWays)
    RadioGroup payWays;

    private IWXAPI wxApi = null;
    private String payOrderNum = "";
    private OrderDetailBean detailBean = null;
    private int notificationId = 0;
    private List<Integer> rewardList = null;
    private String payWay = "alipay";//alipay、wxpay
    private int rewardId = -1;
    private int couponId = -1;

    private LocalReceiver localReceiver = null;
    private IntentFilter intentFilter = null;
    private LocalBroadcastManager lBManager = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String detailInfo = (String) msg.obj;
                    detailBean = OrderParse.getOrderDetail(detailInfo);
                    if (detailBean == null)
                        return;
                    initOrderInfo();
                    break;
                case HttpRequestFlag.requestPageTwo://获取sign
                    if (notificationId != 0) {
                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
                    }
                    toPayOrder((String) msg.obj);
                    break;
                case HttpRequestFlag.requestPageThree:
                    rewardList = OrderParse.getRewardMon((String) msg.obj);
                    if (rewardList == null)
                        return;
                    Intent intentRe = new Intent(OrderPayActivity.this, SelectRewardActivity.class);
                    intentRe.putIntegerArrayListExtra("rewardList", (ArrayList<Integer>) rewardList);
                    startActivityForResult(intentRe, requestCodeOne);
                    break;
                case HttpRequestFlag.requestPageFour://支付前修改金额
                    String updateData = (String) msg.obj;
                    detailBean = OrderParse.getUpdatePayInfo(detailBean, updateData);
                    orderInfoPriceTv.setText(getString(R.string.appYuan) + detailBean.getOrderPay());
                    if (detailBean.getRewardMoney() != 0)
                        payRewardTv.setText(detailBean.getRewardMoney() + getString(R.string.appYuanC));
                    else
                        payRewardTv.setText(getString(R.string.noReward));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay2);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        payOrderNum = getIntent().getStringExtra("PayOrderNum");
        notificationId = getIntent().getIntExtra("JPushNotificationId", 0);
        api = new Api(this, handler);
        api.getOrderDetail(payOrderNum);
        wxApi = WXAPIFactory.createWXAPI(OrderPayActivity.this, Constant.WXKey, false);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.actionWxPayComplete);
        localReceiver = new LocalReceiver();
        lBManager = LocalBroadcastManager.getInstance(this);
        lBManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.oDetailTitle));
        findViewById(R.id.orderPay_sv).setVisibility(View.GONE);
    }

    /**
     * 订单详情初始化
     */
    private void initOrderInfo() {
        findViewById(R.id.orderPay_sv).setVisibility(View.VISIBLE);
        findViewById(R.id.serverIdCard_tv).setVisibility(View.GONE);
        imageLoader.displayImage(detailBean.getServerHeadImg(), serverHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        serverNameTv.setText(detailBean.getServerName());
        if (!Util.isNull(detailBean.getServerIdCard())) {
            String idCardStr = detailBean.getServerIdCard().substring(6, 14);
            serverIdCardTv.setText(detailBean.getServerIdCard().replace(idCardStr, "********"));
        }
        if (!Util.isNull(detailBean.getServerStar())) {
            serverInfoRtb.setRating(Float.valueOf(detailBean.getServerStar()) / 2);
            serverTimeTv.setText(detailBean.getServerStar() + getString(R.string.appScore));
        }
        if (detailBean.getRewardMoney() != 0)
            payRewardTv.setText(detailBean.getRewardMoney() + getString(R.string.appYuanC));
        orderInfoPriceTv.setText(getString(R.string.appYuan) + detailBean.getOrderPay());
        orderInfoLocateTv.setText(StringUtil.splitCityStr(this, detailBean.getUserAddress()));
        orderInfoNumTv.setText(detailBean.getOrderNum());
        setOrderState();
    }

    private void toPayOrder(String backMsg) {
        switch (payWay) {
            case "alipay":
                String strSign = OrderParse.getSign(backMsg);
                if (Util.isNull(strSign))
                    return;
                Alipay alipay = new Alipay(OrderPayActivity.this, strSign);
                alipay.setCallBack(OrderPayActivity.this);
                alipay.pay();
                break;
            case "wxpay":
                JSONObject json = null;
                try {
                    json = new JSONObject(backMsg);
                    if (json != null && !json.has("retcode")) {
                        PayReq req = new PayReq();
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = json.getString("package");
                        req.sign = json.getString("sign");
                        req.extData = "app data"; // optional
                        wxApi.sendReq(req);
                        Util.pLog(/*"isReg:" + isReg + */" appId:" + req.appId + " req.partnerId:" + req.partnerId + " req.sign:" + req.sign);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case requestCodeOne:
                int tempReId = data.getIntExtra("RewardMonPosition", -1);
                if (tempReId == rewardId)
                    return;
                rewardId = tempReId;
                api.updatePay(payOrderNum, rewardId + "", couponId + "");
                /*detailBean.setRewardMoney(rewardList.get(rewardId));
                payRewardTv.setText(rewardList.get(rewardId) + getString(R.string.appYuanC));
                orderInfoPriceTv.setText(getString(R.string.appYuan) + (detailBean.getOrderActualPay() + detailBean.getRewardMoney()));*/
                break;
        }
    }

    /**
     * 设置View显示的状态
     */
    private void setOrderState() {
        switch (detailBean.getOrderState()) {
            case 7://取消待付款
                findViewById(R.id.payReward_ll).setVisibility(View.GONE);
                findViewById(R.id.orderNumLocate_ll).setVisibility(View.GONE);
                findViewById(R.id.orderIntoPayInfo_img).setVisibility(View.GONE);
                findViewById(R.id.orderCancel_ll).setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.serverPhone_img, R.id.orderMoney_rl, R.id.payReward_ll,
            R.id.payNow_btn, R.id.serverHead_img})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                intent.putExtra("shouldRefresh", true);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.serverPhone_img:
                String serverPhone = detailBean.getServerPhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + serverPhone);
                intentCall.setData(data);
                startActivity(intentCall);
                break;
            case R.id.serverHead_img://服务者主页
//                startActivity(new Intent(this, ServerPageActivity.class));
                break;
            case R.id.orderMoney_rl:
                if (detailBean.getOrderState() == 7)
                    return;
                intent.setClass(this, OrderDetailPayActivity.class);
                intent.putExtra("fromWhichAct", 0);
                intent.putExtra("OrderDetailBean", detailBean);
                startActivity(intent);
                break;
            case R.id.payReward_ll:
                api.getRewardMoney();
                break;
            case R.id.payNow_btn:
                switch (payWays.getCheckedRadioButtonId()) {
                    case R.id.payAlipay:
                        payWay = "alipay";
                        break;
                    case R.id.payWx:
                        payWay = "wxpay";
                        boolean isPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                        if (!isPaySupported) {
                            notInstallWx();
                            return;
                        }
                        break;
                }
                Util.pLog("payWay = " + payWay);
                api.toPaySign(payOrderNum, payWay);
                break;
        }
    }

    @Override
    public void onSuccess() {
        payFinished();
    }

    @Override
    public void onFailed() {

    }

    private void payFinished() {
        if (detailBean.getOrderState() == 7) {
            finish();
            return;
        }
        Intent intent = new Intent(OrderPayActivity.this, OrderDetailActivity.class);
        intent.putExtra("DetailOrderNum", payOrderNum);
        startActivity(intent);
        finish();
    }

    private class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Util.pLog("This is In OrderPayActivity...");
            if (Constant.actionWxPayComplete.equals(action)) {
                int bErrorCode = intent.getIntExtra("BaseRespErrCode", -1);
                if (bErrorCode == 0) {
                    payFinished();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localReceiver != null)
            lBManager.unregisterReceiver(localReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("shouldRefresh", true);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
