package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.GuideTraverInfoAdapter;
import com.xbx123.freedom.beans.OrderDetailBean;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/18.
 * 导游支付订单的明细
 */

public class GuidePayDetailActivity extends BaseActivity {
    @Bind(R.id.underOrderNum_tv)
    TextView underOrderNumTv; //订单编号
    @Bind(R.id.underOrderDest_tv)
    TextView underOrderDestTv;//目的地
    @Bind(R.id.underTraveDate_tv)
    TextView underTraveDateTv;//游玩时间
    @Bind(R.id.underTravePNum_tv)
    TextView underTravePNumTv;//游玩人数
    @Bind(R.id.underOrderTimes_tv)
    TextView underOrderTimesTv;//下单时间
    @Bind(R.id.underSiglePrice_tv)
    TextView underSiglePriceTv;//服务费单价
    @Bind(R.id.underTraveDNum_tv)
    TextView underTraveDNumTv;//游玩天数
    @Bind(R.id.underTotalPrice_tv)
    TextView underTotalPriceTv;//总价
    @Bind(R.id.underPayWay_tv)
    TextView underPayWayTv;//支付方式
    @Bind(R.id.underContactsName_tv)
    TextView underContactsNameTv;//紧急联系人姓名
    @Bind(R.id.underContactsPhone_tv)
    TextView underContactsPhoneTv;//紧急联系人电话
    @Bind(R.id.underTravers_lv)
    ListView underTraversLv;//出行人列表

    private GuideTraverInfoAdapter traverInfoAdapter = null;
    private OrderDetailBean detailBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_pay_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        detailBean = (OrderDetailBean) getIntent().getSerializableExtra("GuideOrderDetail");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.payOrderDetail));
        if (detailBean == null)
            return;
        initOrderDetailInfo();
    }

    private void initOrderDetailInfo() {
        underOrderNumTv.setText(detailBean.getOrderNum());
        underTraveDateTv.setText(StringUtil.formatDate(detailBean.getOrderStartTime()) + getString(R.string.appGet) + StringUtil.formatDate(detailBean.getOrderEndTime()));
        if (detailBean.getInsurerList() != null && detailBean.getInsurerList().size() > 0)
            underTravePNumTv.setText(detailBean.getInsurerList().size() + getString(R.string.appPeople));
        underOrderTimesTv.setText(detailBean.getOrderDownTime());
        underSiglePriceTv.setText(getString(R.string.appYuan) + detailBean.getServerUnitPrice() + getString(R.string.guidePerDay));
        underTotalPriceTv.setText(getString(R.string.appYuan) + detailBean.getOrderActualPay());
        if (detailBean.getOrderPayType() == 1)
            underPayWayTv.setText(getString(R.string.payWayAlipay));
        else if (detailBean.getOrderPayType() == 2)
            underPayWayTv.setText(getString(R.string.payWayWx));
        underContactsNameTv.setText(detailBean.getContactsName());
        underContactsPhoneTv.setText(detailBean.getContactsPhone());
        if (detailBean.getInsurerList() != null) {
            traverInfoAdapter = new GuideTraverInfoAdapter(this, detailBean.getInsurerList());
            underTraversLv.setAdapter(traverInfoAdapter);
            Util.setListViewHeight(underTraversLv);
        }
        underTraveDNumTv.setText(StringUtil.getDateDiffer(detailBean.getOrderStartTime(), detailBean.getOrderEndTime()) + getString(R.string.appDay));
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
