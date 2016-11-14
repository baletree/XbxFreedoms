package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/12.
 */

public class NarratorCalledActivity extends AppCompatActivity {
    @Bind(R.id.tripScenic_tv)
    TextView tripScenicTv;
    @Bind(R.id.nCalledPNum_tv)
    TextView nCalledPNumTv;
    @Bind(R.id.nCalledLanguage_tv)
    TextView nCalledLanguageTv;
    @Bind(R.id.callByHourPrice_tv)
    TextView callByHourPriceTv;
    @Bind(R.id.callByTimePrice_tv)
    TextView callByTimePriceTv;

    private Api api = null;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_narrator_called);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        api = new Api(this, handler);
        userInfo = SharePrefer.getUserInfo(NarratorCalledActivity.this);
        resortsBean = (ResortsBean) getIntent().getSerializableExtra("MapResortsBean");
        userLocate = getIntent().getStringExtra("MapCurrentLocate");
        if (resortsBean != null) {
            callByHourPriceTv.setText(resortsBean.getResortsHourPrice() + "");
            callByTimePriceTv.setText(resortsBean.getResortsTimePrice() + "");
        }
    }

    @OnClick({R.id.nCalledTripScenic_ll, R.id.nCalledPNum_ll, R.id.nCalledLanguage_ll, R.id.callByHour_ll, R.id.callByTime_ll, R.id.callImmediate_tv, R.id.narratorCalled_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.narratorCalled_rl:
                finish();
                break;
            case R.id.nCalledTripScenic_ll:
                break;
            case R.id.nCalledPNum_ll:
                break;
            case R.id.nCalledLanguage_ll:
                break;
            case R.id.callByHour_ll:
                changeServiceWays(0);
                chargingType = 1;
                break;
            case R.id.callByTime_ll:
                changeServiceWays(1);
                chargingType = 2;
                break;
            case R.id.callImmediate_tv:
                String nickName = userInfo.getNickName();
                if (Util.isNull(nickName))
                    nickName = "user_" + userInfo.getUserPhone().substring(6, 10);
                Util.pLog("nickName = " + nickName);
                api.createOrder(userInfo.getUid(), userLocate, Constant.NarratorType, nickName,
                        "", resortsBean.getResortsId(), userInfo.getUserPhone(), chargingType + "");
                break;
        }
    }

    /**
     * 价格选择切换
     *
     * @param waysFlag
     */
    private void changeServiceWays(int waysFlag) {
        switch (waysFlag) {
            case 0:
                ((TextView) findViewById(R.id.callByHour_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByHourPrice_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByHourPer_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByTime_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callByTimePrice_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callByTimePer_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callWayTip_tv)).setText(getString(R.string.nCallHourTip));
                findViewById(R.id.callByHour_img).setVisibility(View.VISIBLE);
                findViewById(R.id.callByTime_img).setVisibility(View.GONE);
                break;
            case 1:
                ((TextView) findViewById(R.id.callByTime_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByTimePrice_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByTimePer_tv)).setTextColor(getResources().getColor(R.color.themeColor));
                ((TextView) findViewById(R.id.callByHour_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callByHourPrice_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callByHourPer_tv)).setTextColor(getResources().getColor(R.color.colorHint));
                ((TextView) findViewById(R.id.callWayTip_tv)).setText(getString(R.string.nCallTimesTip));
                findViewById(R.id.callByTime_img).setVisibility(View.VISIBLE);
                findViewById(R.id.callByHour_img).setVisibility(View.GONE);
                break;
        }
    }
}
