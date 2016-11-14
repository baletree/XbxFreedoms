package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.CreateOrderParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/24.
 * 确认呼叫页面-领路人
 */
public class NativeCallActivity extends BaseActivity {
    private final int sexChoice = 410;
    @Bind(R.id.callRealName_tv)
    TextView callRealNameTv;
    @Bind(R.id.callIdCard_tv)
    TextView callIdCardTv;
    @Bind(R.id.callPrice_tv)
    TextView callPriceTv;
    @Bind(R.id.callPriceExplain_tv)
    TextView callPriceExplainTv;//价格组成

    private InsurerBean insurerBean = null;
    private String servicePrice = "";
    private String userLocate = "";
    private UserInfo userInfo = null;
    private String chargingType = "1";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String orderNum = CreateOrderParse.getOrderNum((String) msg.obj);
                    setResult(RESULT_OK, new Intent().putExtra("createOrderNum", orderNum));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_call);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        servicePrice = getIntent().getStringExtra("NativeServicePrice");
        userLocate = getIntent().getStringExtra("MapCurrentLocate");
        userInfo = SharePrefer.getUserInfo(this);
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.callTitle));
        callPriceTv.setText(servicePrice);
        ((TextView) findViewById(R.id.nCallTipTwo_tv)).setText(getString(R.string.callAttentionTwo) + servicePrice + getString(R.string.appYuanC) + ";");
        callPriceExplainTv.setText(getString(R.string.appYuan)+servicePrice + getString(R.string.callPriceTip));
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
                findViewById(R.id.insurerInfo_ll).setVisibility(View.VISIBLE);
                insurerBean = (InsurerBean) data.getSerializableExtra("InsurerBean");
                callRealNameTv.setText(insurerBean.getInsurerName());
                callIdCardTv.setText(insurerBean.getInsurerIdcard());
                break;
        }
    }


    @OnClick({R.id.titleLeft_img, R.id.callInsDetail_tv, R.id.nativeCall_btn, R.id.choiceInsurer_ll})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.callInsDetail_tv:
                intent.setClass(this, InsuranceExplainActivity.class);
                startActivity(intent);
                break;
            case R.id.nativeCall_btn:
                if (insurerBean == null) {
                    Util.showToast(this, getString(R.string.callChoiceInsurer));
                    return;
                }
                api.createOrder(userInfo.getUid(), userLocate, Constant.NativeType, insurerBean.getInsurerName(),
                        insurerBean.getInsurerId(), "", insurerBean.getInsurerPhone(), chargingType);
                break;
            case R.id.choiceInsurer_ll:
                intent.setClass(this, InsurerListActivity.class);
                startActivityForResult(intent, requestCodeOne);
                break;
        }
    }
}
