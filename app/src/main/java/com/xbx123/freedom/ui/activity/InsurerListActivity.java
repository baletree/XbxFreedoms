package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.InsurerListAdapter;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/15.
 * 保险人列表
 */
public class InsurerListActivity extends BaseActivity implements InsurerListAdapter.ItemClickListener {
    @Bind(R.id.title_right_img)
    ImageView titleRightImg;
    @Bind(R.id.insurerRv)
    RecyclerView insurerRv;

    private UserInfo userInfo = null;
    private InsurerListAdapter insurerAdapter = null;
    private List<InsurerBean> insurerList = null;
    private boolean isFromSetting = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    insurerList = OrderParse.getInsurerList((String) msg.obj);
                    if(insurerAdapter != null)
                        insurerRv.setAdapter(null);
                    if (insurerList == null)
                        return;
                    insurerAdapter = new InsurerListAdapter(InsurerListActivity.this, insurerList);
                    insurerRv.setAdapter(insurerAdapter);
                    insurerAdapter.setItemClickListener(InsurerListActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurer_list);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        userInfo = SharePrefer.getUserInfo(this);
        api.getInsurerList(userInfo.getUid());
        isFromSetting = getIntent().getBooleanExtra("isFromSetting", false);
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.insurerTitle));
        titleRightImg.setVisibility(View.VISIBLE);
        titleRightImg.setImageResource(R.mipmap.icon_tianjia);
        insurerRv.setLayoutManager(new LinearLayoutManager(this));
        insurerRv.addItemDecoration(new RecycleViewDivider(this, R.drawable.line));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Util.pLog("resultCode = " + resultCode + " data == null ? " + (data == null));
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case requestCodeOne:
                api.getInsurerList(userInfo.getUid());
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.title_right_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.title_right_img:
                startActivityForResult(new Intent(this, InsurerAddActivity.class), requestCodeOne);
                break;
        }
    }

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent();
        if (isFromSetting) {
            intent.setClass(this, InsurerEditActivity.class);
            intent.putExtra("insurerId", insurerList.get(position).getInsurerId());
            startActivityForResult(intent, requestCodeOne);
        } else {
            intent.putExtra("InsurerBean", insurerList.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
