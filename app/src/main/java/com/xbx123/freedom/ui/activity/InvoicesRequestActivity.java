package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.RequestInvoicesAdapter;
import com.xbx123.freedom.beans.OrderBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/15.
 * 索取发票
 */
public class InvoicesRequestActivity extends BaseActivity implements RequestInvoicesAdapter.ChoiceInvoicesListen{
    @Bind(R.id.invoicesRv)
    RecyclerView invoicesRv;

    private int pageIndex = 1;
    private int pageNum = 100;
    private List<OrderBean> orderList = null;
    private boolean[] choiceArr = null;
    private RequestInvoicesAdapter requestAdapter = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String orderData = (String) msg.obj;
                    orderList = OrderParse.getOrderList(orderData);
                    if(orderList == null)
                        return;
                    choiceArr = new boolean[orderList.size()];
                    requestAdapter = new RequestInvoicesAdapter(InvoicesRequestActivity.this, orderList,choiceArr);
                    invoicesRv.setAdapter(requestAdapter);
                    requestAdapter.setInvoicesListen(InvoicesRequestActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_invoices);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        api.getOrderList(SharePrefer.getUserInfo(this).getUid(), pageIndex + "", pageNum + "");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.settingInvoice));
        invoicesRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.titleLeft_img,R.id.nextStep_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.nextStep_btn:
                startActivity(new Intent(this,InvoicesInfoActivity.class));
                break;
        }
    }

    @Override
    public void clickChoice(int position) {
        if(choiceArr[position])
            choiceArr[position] = false;
        else
            choiceArr[position] = true;
        requestAdapter.notifyDataSetChanged();
    }
}
