package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.FrequentContactsAdapter;
import com.xbx123.freedom.beans.FrequeCotactsBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.GuideParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/5.
 * 常用联系人
 */
public class FrequentContactsActivity extends BaseActivity implements FrequentContactsAdapter.ItemClickListener {
    @Bind(R.id.title_right_img)
    ImageView titleRightImg;
    @Bind(R.id.frequentContacts_rv)
    RecyclerView frequentContacts_rv;

    private boolean isFromSetting = false;
    private final int refreshCode = 100;
    private Api api = null;
    private FrequentContactsAdapter fContactsAdapter = null;
    private List<FrequeCotactsBean> fContactsList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String contactsInfo = (String) msg.obj;
                    if (Util.isNull(contactsInfo))
                        return;
                    fContactsList = GuideParse.getListOfFreContact(contactsInfo);
                    if (fContactsList == null)
                        return;
                    fContactsAdapter = new FrequentContactsAdapter(FrequentContactsActivity.this, fContactsList, isFromSetting);
                    frequentContacts_rv.setAdapter(fContactsAdapter);
                    fContactsAdapter.setItemClickListener(FrequentContactsActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequen_contacts);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        fContactsList = new ArrayList<>();
        isFromSetting = getIntent().getBooleanExtra("isFromSetting", false);
        api.listFrequenContact(SharePrefer.getUserInfo(this).getUid());
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.settingContacts));
        titleRightImg.setVisibility(View.VISIBLE);
        titleRightImg.setImageResource(R.mipmap.icon_tianjia);
        frequentContacts_rv.setLayoutManager(new LinearLayoutManager(this));
        frequentContacts_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.line));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case refreshCode:
                api.listFrequenContact(SharePrefer.getUserInfo(this).getUid());
                break;
        }
    }

    @OnClick({R.id.title_right_img, R.id.titleLeft_img})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.title_right_img:
                intent.setClass(this, EditContactsActivity.class);
                startActivityForResult(intent, refreshCode);
                break;
        }
    }

    @Override
    public void itemClick(int position, FrequeCotactsBean frequeCotactsBean) {
        Intent intent = new Intent();
        intent.putExtra("EditContacts", true);
        intent.putExtra("FrequentContactsName", frequeCotactsBean.getContactName());
        intent.putExtra("FrequentContactsPhone", frequeCotactsBean.getContactPhone());
        intent.putExtra("FrequentContactsId", frequeCotactsBean.getContactId());
        if (isFromSetting) {
            intent.setClass(this, EditContactsActivity.class);
            startActivity(intent);
        } else {
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
