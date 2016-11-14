package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.JsonUtils;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/19.
 * 预约导游取消订单
 */

public class GuideCancelOrderActivity extends BaseActivity {
    @Bind(R.id.gCancelReasonMsg_et)
    EditText gCancelReasonMsgEt;
    @Bind(R.id.gCancelRefund_tv)
    TextView gCancelRefundTv;
    @Bind(R.id.cancelReason_tv)
    TextView cancelReasonTv;

    private final int reasonCode = 101;
    private Api api = null;
    private String orderNumber = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HttpRequestFlag.requestPageTwo:
                    String canCelInfo = (String) msg.obj;
                    if (Util.isNull(canCelInfo))
                        return;
                    if (UtilParse.getRequestCode(canCelInfo) != 1)
                        return;
                    getRefundMoney(UtilParse.getRequestData(canCelInfo));
                    break;
                case HttpRequestFlag.requestPageThree:
                    String canCelResult = (String) msg.obj;
                    if (Util.isNull(canCelResult))
                        return;
                    Util.showToast(GuideCancelOrderActivity.this, UtilParse.getRequestMsg(canCelResult));
                    if (UtilParse.getRequestCode(canCelResult) != 1)
                        return;
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_cancel_order);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        orderNumber = getIntent().getStringExtra("DetailOrderNum");
        api.cancelOrder(orderNumber, "0");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.gCancelTitle));
        findViewById(R.id.gCancelLayout_rl).setVisibility(View.GONE);
    }

    private void getRefundMoney(String dataInfo) {
        findViewById(R.id.gCancelLayout_rl).setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(dataInfo);
            String refundFee = jsonObject.getString("refund");
            gCancelRefundTv.setText(getString(R.string.appYuan) + refundFee);
        } catch (JSONException e) {
            e.printStackTrace();
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
            case reasonCode:
                cancelReasonTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.cancelReasonChoice_rl, R.id.gCancelSure_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.cancelReasonChoice_rl:
                startActivityForResult(new Intent(this, SelectCancelReasonActivity.class), reasonCode);
                break;
            case R.id.gCancelSure_tv:
                if (Util.isNull(cancelReasonTv.getText().toString())) {
                    Util.showToast(this, getString(R.string.gCancelReasonChoice));
                    return;
                }
                api.cancelOrder(orderNumber, "1", cancelReasonTv.getText().toString());
                break;
        }
    }
}
