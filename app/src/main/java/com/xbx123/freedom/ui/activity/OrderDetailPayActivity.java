package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.OrderDetailBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/13.
 * 订单支付明细
 */
public class OrderDetailPayActivity extends BaseActivity {
    @Bind(R.id.payDetTip_tv)
    TextView payDetTipTv;//需要支付、成功支付
    @Bind(R.id.payDetMon_tv)
    TextView payDetMonTv;//需要支付、成功支付的金额显示
    @Bind(R.id.serviceMon_tv)
    TextView serviceMonTv;//服务费金额显示
    @Bind(R.id.rewardMon_tv)
    TextView rewardMonTv;//打赏金额显示
    @Bind(R.id.couponMon_tv)
    TextView couponMonTv;//优惠券金额显示
    @Bind(R.id.payBreaks_tv)
    TextView payBreaksTv;//减免金额显示
    @Bind(R.id.payWays_tv)
    TextView payWaysTv;//支付方式

    @Bind(R.id.startTime_tv)
    TextView startTimeTv;//开始时间
    @Bind(R.id.endTime_tv)
    TextView endTimeTv;//结束时间
    @Bind(R.id.lengthTime_tv)
    TextView lengthTimeTv;//服务时长

    private OrderDetailBean detailBean = null;
    private int fromWhichAct = 0;//0-支付页面进入，1-订单详情进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oderpay_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        detailBean = (OrderDetailBean) getIntent().getSerializableExtra("OrderDetailBean");
        fromWhichAct = getIntent().getIntExtra("fromWhichAct", 0);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.payDetail));
        if (fromWhichAct == 0)
            payDetTipTv.setText(getString(R.string.oDetailNeedPay));
        else
            payDetTipTv.setText(getString(R.string.oDetailAlreadyPay));
        payDetMonTv.setText(getString(R.string.appYuan) + detailBean.getOrderPay());
        serviceMonTv.setText(getString(R.string.appYuan) + detailBean.getOrderActualPay());
        rewardMonTv.setText(getString(R.string.appYuan) + detailBean.getRewardMoney());
        startTimeTv.setText(detailBean.getOrderStartTime());
        endTimeTv.setText(detailBean.getOrderEndTime());
        lengthTimeTv.setText(detailBean.getServiceDuration());
        if (detailBean.getRewardMoney() == 0.0)
            findViewById(R.id.rewardMon_rl).setVisibility(View.GONE);
        if (detailBean.getBreaksMoney() == 0.0)
            findViewById(R.id.payBreaks_rl).setVisibility(View.GONE);
        if (detailBean.getOrderPayType() == 1)
            payWaysTv.setText(getString(R.string.payWayAlipay));
        else
            payWaysTv.setText(getString(R.string.payWayWx));
    }

    @OnClick({R.id.titleLeft_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
        }
    }
}
