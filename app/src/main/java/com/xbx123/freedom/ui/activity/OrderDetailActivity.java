package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ComItemTagBean;
import com.xbx123.freedom.beans.CommentTagBean;
import com.xbx123.freedom.beans.OrderDetailBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.FlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/26.
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener {
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
    //订单信息
    @Bind(R.id.orderInfoPrice_tv)
    TextView orderInfoPriceTv;
    @Bind(R.id.orderInfoLocate_tv)
    TextView orderInfoLocateTv;
    @Bind(R.id.orderInfoNum_tv)
    TextView orderInfoNumTv;
    /* @Bind(R.id.orderInfoTime_tv)
     TextView orderInfoTimeTv;
     @Bind(R.id.orderStart_tv)
     TextView orderStartTv;
     @Bind(R.id.orderEnd_tv)
     TextView orderEndTv;*/
    //提交评论View
    @Bind(R.id.commentStar_rtb)
    RatingBar commentStarRtb;
    @Bind(R.id.commentTag_fl)
    FlowLayout commentTagFl;
    @Bind(R.id.commentMsg_et)
    EditText commentMsgEt;
    @Bind(R.id.commentOperate_ll)
    LinearLayout commentOperateLl;
    //显示评论
    @Bind(R.id.commentShow_rtb)
    RatingBar commentShowRtb;
    @Bind(R.id.commentShowTag_fl)
    FlowLayout commentShowTagFl;
    @Bind(R.id.commentShowMsg_tv)
    TextView commentShowMsgTv;


    private String detailOrderNum = "";
    private OrderDetailBean detailBean = null;
    private CommentTagBean tagBean = null;
    private List<ComItemTagBean> itemTagList = null;
    private List<String> choiceTagList = null;
    private boolean[] tagState = null;

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
                case HttpRequestFlag.requestPageFive:
                    tagBean = OrderParse.getCommentTag((String) msg.obj);
                    if (tagBean == null)
                        return;
                    setTagLayout();
                    break;
                case HttpRequestFlag.requestPageThree:
                    api.getOrderDetail(detailOrderNum);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        detailOrderNum = getIntent().getStringExtra("DetailOrderNum");
        api = new Api(this, handler);
        api.getOrderDetail(detailOrderNum);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.oDetailTitle));
        ((TextView) findViewById(R.id.payAttentionTip_tv)).setText(getString(R.string.oDetailAlreadyPay));
        commentStarRtb.setOnRatingBarChangeListener(this);
        findViewById(R.id.orderDetail_sv).setVisibility(View.GONE);
    }

    /**
     * 订单详情初始化
     */
    private void initOrderInfo() {
        findViewById(R.id.orderDetail_sv).setVisibility(View.VISIBLE);
        findViewById(R.id.serverIdCard_tv).setVisibility(View.GONE);
        imageLoader.displayImage(detailBean.getServerHeadImg(), serverHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        serverNameTv.setText(detailBean.getServerName());
        if (!Util.isNull(detailBean.getServerIdCard()) && detailBean.getServerIdCard().length() > 10) {
            String idCardStr = detailBean.getServerIdCard().substring(6, 14);
            serverIdCardTv.setText(detailBean.getServerIdCard().replace(idCardStr, "********"));
        }
        if (!Util.isNull(detailBean.getServerStar())) {
            serverInfoRtb.setRating(Float.valueOf(detailBean.getServerStar()) / 2);
            serverTimeTv.setText(detailBean.getServerStar() + getString(R.string.appScore));
        }
        orderInfoPriceTv.setText(getString(R.string.appYuan) + detailBean.getOrderPay());
        orderInfoLocateTv.setText(StringUtil.splitCityStr(this, detailBean.getUserAddress()));
        orderInfoNumTv.setText(detailBean.getOrderNum());
//        orderInfoTimeTv.setText(detailBean.getServiceDuration());
        setOrderState();
    }

    @OnClick({R.id.titleLeft_img, R.id.serverPhone_img, R.id.orderMoney_rl, R.id.commentSubmit_btn, R.id.serverHead_img})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
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

                break;
            case R.id.orderMoney_rl:
                if (detailBean.getOrderState() != 6)
                    return;
                intent.setClass(this, OrderDetailPayActivity.class);
                intent.putExtra("fromWhichAct", 1);
                intent.putExtra("OrderDetailBean", detailBean);
                startActivity(intent);
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
                Util.pLog("tagStr:" + tagStr);
                api.submitComment(detailOrderNum, commentStarRtb.getRating() * 2 + "", commentMsgEt.getText().toString(), tagStr);
                break;
        }
    }

    /**
     * 设置View显示的状态
     */
    private void setOrderState() {
        switch (detailBean.getOrderState()) {
            case 5://待评论
                findViewById(R.id.commentSubmit_ll).setVisibility(View.VISIBLE);
                findViewById(R.id.commentShow_ll).setVisibility(View.GONE);
                break;
            case 6://已完成
                findViewById(R.id.commentSubmit_ll).setVisibility(View.GONE);
                findViewById(R.id.commentShow_ll).setVisibility(View.VISIBLE);
                showCommentView();
                break;
            case 8://已取消
                findViewById(R.id.commentSubmit_ll).setVisibility(View.GONE);
                findViewById(R.id.commentShow_ll).setVisibility(View.GONE);
                findViewById(R.id.orderNumLocate_ll).setVisibility(View.GONE);
                findViewById(R.id.orderIntoPayInfo_img).setVisibility(View.GONE);
                findViewById(R.id.orderCancel_ll).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.payAttentionTip_tv)).setText(getString(R.string.oDetailCancel));
                if (detailBean.getOrderPay() == 0.0) {
                    findViewById(R.id.orderMoney_rl).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.orderCancelCloseTip_tv)).setText(getString(R.string.orderCancelHint1));
                } else {
                    findViewById(R.id.orderMoney_rl).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.orderCancelCloseTip_tv)).setText(getString(R.string.orderCancelHint));
                }
                break;
            case 10://已关闭
                findViewById(R.id.commentSubmit_ll).setVisibility(View.GONE);
                findViewById(R.id.commentShow_ll).setVisibility(View.GONE);
                findViewById(R.id.orderIntoPayInfo_img).setVisibility(View.GONE);
                findViewById(R.id.orderCancel_ll).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.payAttentionTip_tv)).setText(getString(R.string.oLOrderClosed));
                ((TextView) findViewById(R.id.orderInfoPrice_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.orderInfoLocate_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.orderInfoNum_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.orderCancelClose_tv)).setText(getString(R.string.orderClosed));
                ((TextView) findViewById(R.id.orderCancelCloseTip_tv)).setText(detailBean.getOrderCloseReason());
                break;
        }
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
