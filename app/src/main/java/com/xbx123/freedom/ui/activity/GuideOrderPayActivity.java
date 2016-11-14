package com.xbx123.freedom.ui.activity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ComItemTagBean;
import com.xbx123.freedom.beans.CommentTagBean;
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
import com.xbx123.freedom.view.views.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/18.
 * 导游订单支付详情
 */

public class GuideOrderPayActivity extends BaseActivity implements Alipay.AlipayCallBack, PromptDialog.DialogClickListener, RatingBar.OnRatingBarChangeListener {
    @Bind(R.id.gOrderStates_tv)
    TextView gOrderStates_tv;//该订单状态
    @Bind(R.id.gOrderNumShow_tv)
    TextView gOrderNumShow_tv;//该订单编号
    @Bind(R.id.gOrder_payTxt_rb)
    RadioButton gOrderPayTxtRb;
    @Bind(R.id.gOrder_payImg_rb)
    RadioButton gOrderPayImgRb;
    @Bind(R.id.gOrder_reviseTxt_rb)
    RadioButton gOrderReviseTxtRb;
    @Bind(R.id.gOrder_reviseImg_rb)
    RadioButton gOrderReviseImgRb;
    @Bind(R.id.gOrder_tripTxt_rb)
    RadioButton gOrderTripTxtRb;
    @Bind(R.id.gOrder_tripImg_rb)
    RadioButton gOrderTripImgRb;

    @Bind(R.id.serverHead_img)
    RoundedImageView serverHeadImg;
    @Bind(R.id.serverName_tv)
    TextView serverNameTv;
    @Bind(R.id.serverTime_tv)
    TextView serverTimeTv;
    @Bind(R.id.serverInfo_rtb)
    RatingBar serverInfoRtb;
    @Bind(R.id.guideOrderLastTime_tv)
    TextView guideOrderLastTimeTv;
    @Bind(R.id.gOrderTotalPrice_tv)
    TextView gOrderTotalPriceTv;
    @Bind(R.id.payWays)
    RadioGroup payWays;
    //显示评论
    @Bind(R.id.commentShow_rtb)
    RatingBar commentShowRtb;
    @Bind(R.id.commentShowTag_fl)
    FlowLayout commentShowTagFl;
    @Bind(R.id.commentShowMsg_tv)
    TextView commentShowMsgTv;
    //提交评论View
    @Bind(R.id.commentStar_rtb)
    RatingBar commentStarRtb;
    @Bind(R.id.commentTag_fl)
    FlowLayout commentTagFl;
    @Bind(R.id.commentMsg_et)
    EditText commentMsgEt;
    @Bind(R.id.commentOperate_ll)
    LinearLayout commentOperateLl;

    private LocalReceiver localReceiver = null;
    private IntentFilter intentFilter = null;
    private final int countdownCode = 101;
    private final int requestCancelCode = 99;
    private Api api = null;
    private IWXAPI wxApi = null;
    private PopupWindow popupWindow = null;
    private OrderDetailBean detailBean = null;
    private PromptDialog promptDialog = null;
    private CommentTagBean tagBean = null;
    private List<ComItemTagBean> itemTagList = null;
    private boolean[] tagState = null;
    private boolean isBackRefresh = false;
    private String payWay = "alipay";//alipay、wxpay
    private String orderNumber = "";

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
                    initGuideInfo();
                    break;
                case HttpRequestFlag.requestPageTwo:
                    toPayOrder((String) msg.obj);
                    break;
                case HttpRequestFlag.requestPageThree://取消订单刷新界面
                    api.getOrderDetail(orderNumber);
                    break;
                case countdownCode:
                    countDownToPayOrder();
                    detailBean.setServerNowTime(detailBean.getServerNowTime() + 1);
                    handler.sendEmptyMessageDelayed(countdownCode, 1 * 1000);
                    break;
                case HttpRequestFlag.requestPageFive:
                    tagBean = OrderParse.getCommentTag((String) msg.obj);
                    if (tagBean == null)
                        return;
                    setTagLayout();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_order_pay);
        ButterKnife.bind(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.actionWxPayComplete);
        localReceiver = new LocalReceiver();
        lBManager = LocalBroadcastManager.getInstance(this);
        lBManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        wxApi = WXAPIFactory.createWXAPI(GuideOrderPayActivity.this, Constant.WXKey, false);
        orderNumber = getIntent().getStringExtra("PayOrderNum");
        api.getOrderDetail(orderNumber);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.guideOrderPay_sv).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.oDetailTitle));
        ((ImageView) findViewById(R.id.title_right_img)).setImageResource(R.mipmap.order_complaint);
        findViewById(R.id.serverPhone_img).setEnabled(false);
        ((ImageView) findViewById(R.id.serverPhone_img)).setImageResource(R.mipmap.phone_hui);
        commentStarRtb.setOnRatingBarChangeListener(this);
    }

    private void initGuideInfo() {
        findViewById(R.id.title_right_img).setVisibility(View.VISIBLE);
        findViewById(R.id.guideOrderPay_sv).setVisibility(View.VISIBLE);
        gOrderNumShow_tv.setText(detailBean.getOrderNum());
        imageLoader.displayImage(detailBean.getServerHeadImg(), serverHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        serverNameTv.setText(detailBean.getServerName());
        if (!Util.isNull(detailBean.getServerStar())) {
            serverInfoRtb.setRating(Float.valueOf(detailBean.getServerStar()));
            serverTimeTv.setText((Float.valueOf(detailBean.getServerStar()) * 2) + getString(R.string.appScore));
        }
        gOrderTotalPriceTv.setText(getString(R.string.appYuan) + detailBean.getOrderActualPay());
        initOrderStateInfo();
    }

    /**
     * 评论显示
     */
    private void showCommentView() {
        if (detailBean.getServerTagList() != null && detailBean.getServerTagList().size() > 0) {
            commentShowTagFl.setVisibility(View.VISIBLE);
            for (int i = 0; i < detailBean.getServerTagList().size(); i++) {
                commentShowTagFl.addView(addTextView(detailBean.getServerTagList().get(i)));
            }
        }
        if (!Util.isNull(detailBean.getCommentStar()))
            commentShowRtb.setRating(Float.valueOf(detailBean.getCommentStar()) / 2);
        commentShowMsgTv.setText(detailBean.getServerContent());
    }

    /**
     * 根据订单状态对页面的处理
     */
    private void initOrderStateInfo() {
        gOrderStates_tv.setText(StringUtil.orderStateStr(this, detailBean.getOrderState()));
        switch (detailBean.getOrderState()) {
            case 4://待支付，此状态下取消状态归为8
                gOrderPayTxtRb.setChecked(true);
                gOrderPayImgRb.setChecked(true);
                handler.sendEmptyMessage(countdownCode);
                break;
            case 8://未付款时的取消
                refundState();
                break;
            case 10:
                findViewById(R.id.gOrderProcessState_ll).setVisibility(View.GONE);
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                break;
            case 11://预约中
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                gOrderPayTxtRb.setText(getString(R.string.gDetailYetPay));
                gOrderReviseTxtRb.setChecked(true);
                gOrderReviseImgRb.setChecked(true);
                break;
            case 12://待出行
                findViewById(R.id.serverPhone_img).setEnabled(true);
                ((ImageView) findViewById(R.id.serverPhone_img)).setImageResource(R.mipmap.server_phone);
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                gOrderReviseTxtRb.setText(getString(R.string.gDetailYetReservation));
                gOrderTripTxtRb.setChecked(true);
                gOrderTripImgRb.setChecked(true);
                break;
            case 13://服务进行中
                findViewById(R.id.serverPhone_img).setEnabled(true);
                ((ImageView) findViewById(R.id.serverPhone_img)).setImageResource(R.mipmap.server_phone);
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                gOrderTripTxtRb.setText(getString(R.string.gDetailServiceIng));
                gOrderTripTxtRb.setChecked(true);
                gOrderTripImgRb.setChecked(true);
                break;
            case 14://用户取消退款中，退款完成后状态为8
                refundState();
                break;
            case 15://已付款但预约失败，退款完成后状态为8
                gOrderStates_tv.setText(getString(R.string.gDetailReservationFailTitle));
                ((TextView) findViewById(R.id.gStateTip_tv)).setText(getString(R.string.gDetailReservationFailTitle));
                findViewById(R.id.gOrderPay_ll).setVisibility(View.GONE);
                findViewById(R.id.gReservationFail_ll).setVisibility(View.VISIBLE);
                break;
            case 5://待评论
                findViewById(R.id.gOrderProcessState_ll).setVisibility(View.GONE);
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                findViewById(R.id.commentGuide_rl).setVisibility(View.VISIBLE);
                break;
            case 6://已完成
                findViewById(R.id.gOrderProcessState_ll).setVisibility(View.GONE);
                findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
                findViewById(R.id.commentGuide_rl).setVisibility(View.VISIBLE);
                findViewById(R.id.commentSubmit_ll).setVisibility(View.GONE);
                findViewById(R.id.commentShow_ll).setVisibility(View.VISIBLE);
                showCommentView();
                break;
        }
    }

    /**
     * 退款完成后状态归为8(已取消)时状态显示
     */
    private void refundState() {
        findViewById(R.id.gOrderPay_ll).setVisibility(View.GONE);
        findViewById(R.id.gOrderProcessState_ll).setVisibility(View.GONE);
        gOrderStates_tv.setTextColor(getResources().getColor(R.color.themeColor));
        if (detailBean.getRefundMoney() == 0)
            findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
        else {
            findViewById(R.id.guideOrderStates_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.gRefundFee_ll).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.gStateTip_tv)).setText(getString(R.string.gDetailRefundFeeTitle));
            ((TextView) findViewById(R.id.gRefundFee_tv)).setText(getString(R.string.appYuan) + detailBean.getRefundMoney());
            if (detailBean.getRefundMoney() > detailBean.getOrderActualPay()) {
                ((TextView) findViewById(R.id.gCancelFee_tv)).setText("+" + getString(R.string.appYuan) + Util.getOnePointDouble(detailBean.getRefundMoney() - detailBean.getOrderActualPay()));
            } else if (detailBean.getRefundMoney() < detailBean.getOrderActualPay()) {
                ((TextView) findViewById(R.id.gCancelFee_tv)).setText("-" + getString(R.string.appYuan) + Util.getOnePointDouble(detailBean.getOrderActualPay() - detailBean.getRefundMoney()));
            } else {
                findViewById(R.id.gCancelFee_rl).setVisibility(View.GONE);
            }
        }
    }


    private void toPayOrder(String backMsg) {
        switch (payWay) {
            case "alipay":
                String strSign = OrderParse.getSign(backMsg);
                if (Util.isNull(strSign))
                    return;
                Alipay alipay = new Alipay(GuideOrderPayActivity.this, strSign);
                alipay.setCallBack(GuideOrderPayActivity.this);
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void countDownToPayOrder() {
        long lastPayOrderTime = StringUtil.getLastPayOrderTime(detailBean.getOrderDownTime());
        if (detailBean.getServerNowTime() * 1000 >= lastPayOrderTime) {
            handler.removeMessages(countdownCode);
            guideOrderLastTimeTv.setText(0 + getString(R.string.appHour) + 0 + getString(R.string.appMinute) + 0 + getString(R.string.appMinute));
            findViewById(R.id.guideToPay_tv).setBackgroundResource(R.drawable.gray_rectangle_bg);
            findViewById(R.id.guideToPay_tv).setEnabled(false);
        } else {
            guideOrderLastTimeTv.setText(StringUtil.hoursBetween(this, lastPayOrderTime, detailBean.getServerNowTime() * 1000));
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.title_right_img, R.id.serverPhone_img, R.id.guideOrderPayDetail_ll, R.id.guideToPay_tv, R.id.commentSubmit_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                isSHowRefresh();
                break;
            case R.id.title_right_img:
                showPopupMore();
                break;
            case R.id.serverPhone_img://拨打电话
                String serverPhone = detailBean.getServerPhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + serverPhone);
                intentCall.setData(data);
                startActivity(intentCall);
                break;
            case R.id.guideOrderPayDetail_ll:
                intent.setClass(this, GuidePayDetailActivity.class);
                intent.putExtra("GuideOrderDetail", detailBean);
                startActivity(intent);
                break;
            case R.id.guideToPay_tv:
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
                api.toPaySign(orderNumber, payWay);
                break;
            case R.id.commentSubmit_btn:
                String tagStr = "";
                if (tagState != null) {
                    for (int i = 0; i < tagState.length; i++) {
                        if (tagState[i])
                            tagStr = tagStr + itemTagList.get(i).getComTagId() + ",";
                    }
                }
                if (!Util.isNull(tagStr))
                    tagStr = tagStr.substring(0, tagStr.length() - 1);
                api.submitComment(orderNumber, commentStarRtb.getRating() * 2 + "", commentMsgEt.getText().toString(), tagStr);
                break;
        }
    }

    /**
     * 更多
     */
    private void showPopupMore() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_main_more, null);
        popupWindow = new PopupWindow(contentView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        switch (detailBean.getOrderState()) {
            case 5:
            case 6:
            case 8:
            case 14:
            case 15:
                ((TextView) contentView.findViewById(R.id.popupOneTxt_tv)).setText(getString(R.string.gDetailComplaints));
                break;
        }
        // 设置好参数之后再show
        popupWindow.showAsDropDown(findViewById(R.id.title_right_img));
        contentView.findViewById(R.id.popOutside_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.popupCancel_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消订单
                skipChoice();
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.popupService_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "4008469989");
                intentCall.setData(data);
                startActivity(intentCall);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case requestCancelCode:
                api.getOrderDetail(orderNumber);
                break;
        }
    }

    private void skipChoice() {
        Intent intent = new Intent();
        switch (detailBean.getOrderState()) {
            case 4://取消对话框
                showCancelDialog();
                break;
            case 11:
            case 12:
            case 13://到取消订单页面
                intent.setClass(GuideOrderPayActivity.this, GuideCancelOrderActivity.class);
                intent.putExtra("DetailOrderNum", detailBean.getOrderNum());
                startActivityForResult(intent, requestCancelCode);
                break;
            case 5:
            case 6://投诉
                break;
        }
    }

    /**
     * 取消订单的对话框
     */
    private void showCancelDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setDialogClickListener(this);
        promptDialog.setDialogTitleMsg("", getString(R.string.gDetailCancelTip));
        promptDialog.setBtnMsg(getString(R.string.gDetailCancelNo), "");
        promptDialog.show();
    }

    /**
     * 支付完成的处理
     */
    private void payFinished() {
        api.getOrderDetail(orderNumber);
        isBackRefresh = true;
        findViewById(R.id.guideOrderStates_ll).setVisibility(View.GONE);
        gOrderPayTxtRb.setChecked(false);
        gOrderPayImgRb.setChecked(false);
        gOrderReviseTxtRb.setChecked(true);
        gOrderReviseImgRb.setChecked(true);
    }

    @Override
    public void onSuccess() {
        payFinished();
    }

    @Override
    public void onFailed() {

    }

    private class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Util.pLog("This is In GuideOrderPayActivity...");
            if (Constant.actionWxPayComplete.equals(action)) {
                int bErrorCode = intent.getIntExtra("BaseRespErrCode", -1);
                if (bErrorCode == 0) {
                    payFinished();
                }
            }
        }
    }

    @Override
    public void cancelLisen() {
        if (promptDialog != null && promptDialog.isShowing())
            promptDialog.dismiss();
    }

    @Override
    public void confirmLisen() {
        if (promptDialog != null && promptDialog.isShowing())
            promptDialog.dismiss();
        api.cancelOrder(orderNumber, "1", "");
    }

    private void isSHowRefresh() {
        if (!isBackRefresh) {
            finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("shouldRefresh", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 流式布局-提交评论的
     */
    private void setTagLayout() {
        if (commentStarRtb.getRating() > 3.0) {
            if (tagBean.getGoodComList() == null)
                return;
            itemTagList = tagBean.getGoodComList();
        } else {
            if (tagBean.getBadComList() == null)
                return;
            itemTagList = tagBean.getBadComList();
        }
        tagState = new boolean[itemTagList.size()];
        commentTagFl.setVisibility(View.VISIBLE);
        commentTagFl.removeAllViews();
        for (int i = 0; i < itemTagList.size(); i++) {
            final TextView txtView = addCommentTextView(itemTagList.get(i).getComTagName());
            commentTagFl.addView(txtView);
            final int finalI = i;
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagState[finalI]) {
                        txtView.setBackgroundResource(R.drawable.border_input_bg);
                        txtView.setTextColor(getResources().getColor(R.color.colorBTxt));
                        tagState[finalI] = false;
                    } else {
                        txtView.setBackgroundResource(R.drawable.tag_select_bg);
                        txtView.setTextColor(getResources().getColor(R.color.colorReward));
                        tagState[finalI] = true;
                    }
                }
            });
        }
    }


    /**
     * 展示评论的流式布局
     *
     * @param txt
     * @return
     */
    private TextView addTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 8, 20, 8);
        txtLp.rightMargin = 30;
        txtLp.bottomMargin = 20;
        textView.setBackgroundResource(R.drawable.border_input_bg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    private TextView addCommentTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 10, 20, 10);
        txtLp.rightMargin = 30;
        txtLp.bottomMargin = 20;
        textView.setBackgroundResource(R.drawable.border_input_bg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            isSHowRefresh();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(countdownCode);
        if (localReceiver != null)
            lBManager.unregisterReceiver(localReceiver);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser) {
            findViewById(R.id.commentOperate_ll).setVisibility(View.VISIBLE);
            if (tagBean != null) {
                setTagLayout();
                return;
            }
            api.getCommentTag();
        }
    }
}
