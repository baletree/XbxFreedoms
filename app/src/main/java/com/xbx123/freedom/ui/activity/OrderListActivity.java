package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.OrderListAdapter;
import com.xbx123.freedom.beans.OrderBean;
import com.xbx123.freedom.beans.OrderDetailBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/5/25.
 */
public class OrderListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OrderListAdapter.OnRecyItemClickListener {
    private RecyclerView orderList_rv;
    private SwipeRefreshLayout orderList_srf;

    private int pageIndex = 1;
    private int pageNum = 100;

    private OrderListAdapter orderListAdapter = null;
    private UserInfo userInfo = null;
    private List<OrderBean> orderList = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    orderList_srf.setRefreshing(false);
                    String orderData = (String) msg.obj;
                    orderList = OrderParse.getOrderList(orderData);
                    orderListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
                    orderList_rv.setAdapter(orderListAdapter);
                    orderListAdapter.setOnItemClickListener(OrderListActivity.this);
                    break;
                case 10:
                    orderList_srf.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void initDatas() {
        userInfo = SharePrefer.getUserInfo(this);
        api = new Api(this, handler);
        api.getOrderList(userInfo.getUid(), pageIndex + "", pageNum + "");
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.oLTitle));
        findViewById(R.id.titleLeft_img).setOnClickListener(this);
        orderList_rv = (RecyclerView) findViewById(R.id.orderList_rv);
        orderList_srf = (SwipeRefreshLayout) findViewById(R.id.orderList_srf);
        orderList_rv.setLayoutManager(new LinearLayoutManager(this));
        orderList_srf.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        api.getOrderList(userInfo.getUid(), pageIndex + "", pageNum + "");
        handler.sendEmptyMessageDelayed(10, 20 * 1000);
    }

    @Override
    public void onItemClick(View v, int position, OrderBean orderBean) {
        dealOrderState(orderBean.getOrderNum(), orderBean.getOrderState(), orderBean.getServerType());
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
                if (data.getBooleanExtra("shouldRefresh", false))
                    api.getOrderList(userInfo.getUid(), pageIndex + "", pageNum + "");
                break;
        }
    }

    private void dealOrderState(String orderNum, int orderState, int serverType) {
        Intent intent = new Intent();
        switch (orderState) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 9:
                //进行中
                intent.putExtra("InOrderNum", orderNum);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 4:
            case 7://待支付
                if (serverType == Constant.GuidesType) {//导游
                    intent.setClass(this, GuideOrderPayActivity.class);
                } else//讲解员和领路人
                    intent.setClass(this, OrderPayActivity.class);
                intent.putExtra("PayOrderNum", orderNum);
                startActivityForResult(intent, requestCodeOne);
                break;
            case 5:
            case 6:
            case 8:
            case 10:
                if (serverType == Constant.GuidesType) {//导游
                    intent.setClass(this, GuideOrderPayActivity.class);
                    intent.putExtra("PayOrderNum", orderNum);
                } else {//讲解员和领路人
                    intent.setClass(this, OrderDetailActivity.class);
                    intent.putExtra("DetailOrderNum", orderNum);
                }
                startActivity(intent);
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                intent.setClass(this, GuideOrderPayActivity.class);
                intent.putExtra("PayOrderNum", orderNum);
                startActivity(intent);
                break;
        }
    }
}
