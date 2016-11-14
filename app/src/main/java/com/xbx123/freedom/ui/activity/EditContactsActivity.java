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
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/5.
 * 编辑/添加常用联系人
 */
public class EditContactsActivity extends BaseActivity {
    @Bind(R.id.fContactsName_et)
    EditText fContactsNameEt;
    @Bind(R.id.fContactsPhone_et)
    EditText fContactsPhoneEt;

    private boolean isEdit = false;
    private String editContactId = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    if (isEdit)
                        Util.showToast(EditContactsActivity.this, getString(R.string.fContactsEditSuc));
                    else
                        Util.showToast(EditContactsActivity.this, getString(R.string.fContactsAddSuc));
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        isEdit = getIntent().getBooleanExtra("EditContacts", false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (isEdit) {
            findViewById(R.id.titleRtxt_tv).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.fContactsTitleEdit));
            ((TextView) findViewById(R.id.titleRtxt_tv)).setText(getString(R.string.fContactsTitleDelete));
            ((TextView) findViewById(R.id.titleRtxt_tv)).setTextColor(getResources().getColor(R.color.redColor));
            editContactId = getIntent().getStringExtra("FrequentContactsId");
            String name = getIntent().getStringExtra("FrequentContactsName");
            fContactsNameEt.setText(name);
            if (!Util.isNull(name))
                fContactsNameEt.setSelection(name.length());
            fContactsPhoneEt.setText(getIntent().getStringExtra("FrequentContactsPhone"));
        } else
            ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.fContactsTitleAdd));
    }

    @OnClick({R.id.titleLeft_img, R.id.fContactSave_btn, R.id.titleRtxt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.fContactSave_btn:
                if (Util.isNull(fContactsNameEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.fContactsNameTip));
                    return;
                }
                if (Util.isNull(fContactsPhoneEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.fContactsPhoneTip));
                    return;
                }
                if(!StringUtil.isNameChinese(fContactsNameEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerIdNameCheck));
                    return;
                }
                if (!Util.checkTel(fContactsPhoneEt.getText().toString())) {
                    Util.showToast(this, getString(R.string.loginPhoneCheck));
                    return;
                }
                if (isEdit) {
                    api.editFrequenContact(editContactId, fContactsNameEt.getText().toString(), fContactsPhoneEt.getText().toString());
                } else {
                    api.addFrequenContact(SharePrefer.getUserInfo(this).getUid(), fContactsNameEt.getText().toString(), fContactsPhoneEt.getText().toString());
                }
                break;
            case R.id.titleRtxt_tv:
                if (isEdit)
                    api.deleteFrequenContact(editContactId);
                break;
        }
    }
}
