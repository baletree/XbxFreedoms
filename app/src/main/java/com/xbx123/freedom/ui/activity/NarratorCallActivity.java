package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ResortsBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.CreateOrderParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/24.
 * 确认呼叫-讲解员
 */
public class NarratorCallActivity extends BaseActivity {
    @Bind(R.id.nCallNickName_et)
    EditText nCallNickNameEt;
    @Bind(R.id.nCallPhone_et)
    EditText nCallPhoneEt;
    @Bind(R.id.nCallTimeBg_rl)
    RelativeLayout nCallTimeBgRl;
    @Bind(R.id.nCallTimePrice_tv)
    TextView nCallTimePriceTv;
    @Bind(R.id.nCallHourBg_rl)
    RelativeLayout nCallHourBgRl;
    @Bind(R.id.nCallHourPrice_tv)
    TextView nCallHourPriceTv;
    @Bind(R.id.nCallPrice_tv)
    TextView nCallPriceTv;

    private UserInfo userInfo = null;
    private ResortsBean resortsBean = null;

    private String userLocate = "";
    private int chargingType = 1;

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
        setContentView(R.layout.activity_narrator_call);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        userInfo = SharePrefer.getUserInfo(NarratorCallActivity.this);
        resortsBean = (ResortsBean) getIntent().getSerializableExtra("MapResortsBean");
        userLocate = getIntent().getStringExtra("MapCurrentLocate");
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.callTitle));
        if (!Util.isNull(userInfo.getNickName())) {
            nCallNickNameEt.setText(userInfo.getNickName());
            nCallNickNameEt.setSelection(userInfo.getNickName().length());
        } else {
            String nickStr = SharePrefer.getNarratorName(this);
            if (!Util.isNull(nickStr)) {
                nCallNickNameEt.setText(nickStr);
                nCallNickNameEt.setSelection(nickStr.length());
            }
        }
        nCallNickNameEt.setCursorVisible(false);
        nCallPhoneEt.setText(userInfo.getUserPhone());
        if (resortsBean != null) {
            nCallHourPriceTv.setText(resortsBean.getResortsHourPrice() + getString(R.string.nCallSingleHour));
            nCallTimePriceTv.setText(resortsBean.getResortsTimePrice() + getString(R.string.nCallSingleTime));
            nCallPriceTv.setText(getString(R.string.appYuan) + resortsBean.getResortsHourPrice() + getString(R.string.nCallPriceHour));
            ((TextView) findViewById(R.id.nCallHourTipTwo_Tv)).setText(getString(R.string.nCallAttentionTwo).concat(resortsBean.getResortsHourPrice() + getString(R.string.appYuanC)) + ";");
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.nCallTime_rl, R.id.nCallHour_rl, R.id.narratorCall_btn, R.id.nCallNickName_et})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.nCallNickName_et:
                nCallNickNameEt.setCursorVisible(true);
                break;
            case R.id.nCallTime_rl://按次计费
                chargingType = 2;
                findViewById(R.id.nCallTimeBg_rl).setVisibility(View.VISIBLE);
                findViewById(R.id.nCallHourBg_rl).setVisibility(View.GONE);
                nCallTimePriceTv.setTextColor(getResources().getColor(R.color.colorWhite));
                ((TextView) findViewById(R.id.nCallTimeHint_tv)).setTextColor(getResources().getColor(R.color.colorWhite));
                nCallHourPriceTv.setTextColor(getResources().getColor(R.color.colorBTxt));
                nCallPriceTv.setText(getString(R.string.nCallPriceTime) + resortsBean.getResortsTimePrice() + getString(R.string.appPerTime));
                ((TextView) findViewById(R.id.nCallHourHint_tv)).setTextColor(getResources().getColor(R.color.colorBTxt));
                ((TextView) findViewById(R.id.nCallHourTipOne_Tv)).setText(getString(R.string.nCallAttentionFour));
                ((TextView) findViewById(R.id.nCallHourTipTwo_Tv)).setText(getString(R.string.nCallAttentionFive));
                findViewById(R.id.nCallHourTipThree_Tv).setVisibility(View.GONE);
                break;
            case R.id.nCallHour_rl:
                chargingType = 1;
                findViewById(R.id.nCallTimeBg_rl).setVisibility(View.GONE);
                findViewById(R.id.nCallHourBg_rl).setVisibility(View.VISIBLE);
                nCallHourPriceTv.setTextColor(getResources().getColor(R.color.colorWhite));
                ((TextView) findViewById(R.id.nCallHourHint_tv)).setTextColor(getResources().getColor(R.color.colorWhite));
                nCallTimePriceTv.setTextColor(getResources().getColor(R.color.colorBTxt));
                nCallPriceTv.setText(getString(R.string.appYuan) + resortsBean.getResortsHourPrice() + getString(R.string.nCallPriceHour));
                ((TextView) findViewById(R.id.nCallTimeHint_tv)).setTextColor(getResources().getColor(R.color.colorBTxt));
                ((TextView) findViewById(R.id.nCallHourTipOne_Tv)).setText(getString(R.string.nCallAttentionOne));
                ((TextView) findViewById(R.id.nCallHourTipTwo_Tv)).setText(getString(R.string.nCallAttentionTwo) + resortsBean.getResortsHourPrice() + getString(R.string.appYuanC) + ";");
                ((TextView) findViewById(R.id.nCallHourTipThree_Tv)).setText(getString(R.string.nCallAttentionThree));
                findViewById(R.id.nCallHourTipThree_Tv).setVisibility(View.VISIBLE);
                break;
            case R.id.narratorCall_btn:
                if (Util.isNull(nCallNickNameEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.nCallNameHint));
                    return;
                }
                SharePrefer.saveNarratorName(NarratorCallActivity.this, nCallNickNameEt.getText().toString());
                if (Util.isNull(nCallPhoneEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.nCallPhone));
                    return;
                }
                api.createOrder(userInfo.getUid(), userLocate, Constant.NarratorType, nCallNickNameEt.getText().toString(),
                        "", resortsBean.getResortsId(), nCallPhoneEt.getText().toString(), chargingType + "");
                break;
        }
    }
}
