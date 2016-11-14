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
import com.xbx123.freedom.adapter.SelectInsurerListAdapter;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.OrderParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.RecycleViewDivider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/17.
 * 常用游客列表-选择
 */
public class SelectInsurerListActivity extends BaseActivity implements SelectInsurerListAdapter.ItemClickListener {
    @Bind(R.id.title_right_img)
    ImageView titleRightImg;
    @Bind(R.id.insurerRv)
    RecyclerView insurerRv;

    private UserInfo userInfo = null;
    private SelectInsurerListAdapter insurerAdapter = null;
    private List<InsurerBean> insurerList = null;
    private List<InsurerBean> choiceIsList = null;
    private int choiceInsurerNum = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    insurerList = OrderParse.getInsurerList((String) msg.obj);
                    if (insurerAdapter != null)
                        insurerRv.setAdapter(null);
                    if (insurerList == null)
                        return;
                    insurerAdapter = new SelectInsurerListAdapter(SelectInsurerListActivity.this, insurerList, choiceIsList);
                    insurerRv.setAdapter(insurerAdapter);
                    insurerAdapter.setItemClickListener(SelectInsurerListActivity.this);
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
        choiceIsList = (List<InsurerBean>) getIntent().getSerializableExtra("ChoiceInsurerList");
        choiceInsurerNum = getIntent().getIntExtra("ChoiceInsurerNum", 1);
        api = new Api(this, handler);
        userInfo = SharePrefer.getUserInfo(this);
        api.getInsurerList(userInfo.getUid());
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.insurerTitle));
        findViewById(R.id.titleRtxt_tv).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.titleRtxt_tv)).setText(getString(R.string.appSure));
        insurerRv.setLayoutManager(new LinearLayoutManager(this));
        insurerRv.addItemDecoration(new RecycleViewDivider(this, R.drawable.line));
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
                api.getInsurerList(userInfo.getUid());
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.title_right_img, R.id.titleRtxt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.title_right_img:
                startActivityForResult(new Intent(this, InsurerAddActivity.class), requestCodeOne);
                break;
            case R.id.titleRtxt_tv:
                if (choiceIsList != null && choiceIsList.size() > 0) {
                    if (choiceInsurerNum < choiceIsList.size()) {
                        Util.showToast(this, getString(R.string.gChoiceTraver1) + choiceInsurerNum + getString(R.string.gChoiceTraver2));
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("ChoiceInsurer", (Serializable) choiceIsList);
                    setResult(RESULT_OK, intent);
                    finish();
                } else
                    Util.showToast(this, getString(R.string.guideTraver));
                break;
        }
    }

    @Override
    public void itemClick(int position, InsurerBean insurerBean) {
        if (choiceIsList == null)
            choiceIsList = new ArrayList<>();
        if (choiceIsList.size() > 0) {
            boolean isInChoiceList = false;
            for (int i = 0; i < choiceIsList.size(); i++) {
                if ((choiceIsList.get(i).getInsurerId()).equals(insurerBean.getInsurerId())) {
                    isInChoiceList = true;
                    choiceIsList.remove(i);
                    break;
                }
            }
            if (isInChoiceList)
                insurerAdapter.notifyDataSetChanged();
            else {
                choiceIsList.add(insurerBean);
                insurerAdapter.notifyDataSetChanged();
            }
        } else {
            choiceIsList.add(insurerBean);
            insurerAdapter.notifyDataSetChanged();
        }
    }
}
