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
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.IdCard;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/17.
 */
public class InsurerEditActivity extends BaseActivity {
    @Bind(R.id.deInsNameEt)
    EditText deInsNameEt;
    @Bind(R.id.deInsIdCardEt)
    EditText deInsIdCardEt;
    @Bind(R.id.deInsPhoneEt)
    EditText deInsPhoneEt;
    @Bind(R.id.titleRtxt_tv)
    TextView titleRtxtTv;

    private String insurerId = "";
    private InsurerBean insurerBean = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    insurerBean = OrderParse.getInsurerInfo((String) msg.obj);
                    if (insurerBean == null)
                        return;
                    initInsurerInfo();
                    break;
                case HttpRequestFlag.requestPageTwo://删除被保人
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
                case HttpRequestFlag.requestPageThree://保存修改被保人
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_insurer);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        insurerId = getIntent().getStringExtra("insurerId");
        api.getInsurerInfo(insurerId);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.insurerInfo));
        titleRtxtTv.setText(getString(R.string.insurerDelete));
        titleRtxtTv.setTextColor(getResources().getColor(R.color.redColor));
        findViewById(R.id.editInsurerBtn).setVisibility(View.GONE);
    }

    private void initInsurerInfo() {
        findViewById(R.id.editInsurerBtn).setVisibility(View.VISIBLE);
        if (!Util.isNull(insurerBean.getInsurerName())) {
            deInsNameEt.setText(insurerBean.getInsurerName());
            deInsNameEt.setSelection(insurerBean.getInsurerName().length());
        }
        deInsIdCardEt.setText(insurerBean.getInsurerIdcard());
        deInsPhoneEt.setText(insurerBean.getInsurerPhone());
    }

    @OnClick({R.id.titleLeft_img, R.id.editInsurerBtn, R.id.titleRtxt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.editInsurerBtn:
                if (Util.isNull(deInsNameEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.insurerMsgHint));
                    return;
                }
                if (Util.isNull(deInsIdCardEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.insurerIdCardHint));
                    return;
                }
                if (Util.isNull(deInsPhoneEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.insurerPhoneHint));
                    return;
                }
                if (new IdCard(deInsIdCardEt.getText().toString()).isCorrect() != 0) {
                    Util.showToast(this, getString(R.string.insurerIdCardCheck));
                    return;
                }
                if(!StringUtil.isNameChinese(deInsNameEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerIdNameCheck));
                    return;
                }
                if (deInsNameEt.getText().toString().equals(insurerBean.getInsurerName()) && deInsIdCardEt.getText().toString().equals(insurerBean.getInsurerIdcard())
                        && deInsPhoneEt.getText().toString().equals(insurerBean.getInsurerPhone())) {
                    Util.showToast(this, getString(R.string.insurerEditTip));
                    return;
                }
                insurerBean.setInsurerName(deInsNameEt.getText().toString());
                insurerBean.setInsurerIdcard(deInsIdCardEt.getText().toString());
                insurerBean.setInsurerPhone(deInsPhoneEt.getText().toString());
                api.editInsurer(insurerBean);
                break;
            case R.id.titleRtxt_tv:
                api.deleteInsurer(insurerBean.getInsurerId());
                break;
        }
    }
}
