package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.LoginParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.dialog.PromptDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/8.
 */
public class ModifyInfoActivity extends BaseActivity {
    @Bind(R.id.modifyInfo_et)
    EditText modifyInfoEt;
    private int modifyType = 0;
    private UserInfo userInfo = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String modifyData = (String) msg.obj;
                    UserInfo modifyUserInfo = LoginParse.modifyUserInfo(modifyData, userInfo);
                    SharePrefer.saveUserInfo(ModifyInfoActivity.this, modifyUserInfo);
                    setResult(RESULT_OK, new Intent());
//                    Util.pLog("userInfo:"+userInfo.getNickName());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
        modifyType = getIntent().getIntExtra("ModifyType", 0);
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfoData");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.uInfoTitle));
        ((TextView) findViewById(R.id.titleRtxt_tv)).setText(getString(R.string.uInfoSure));
        ((TextView) findViewById(R.id.titleRtxt_tv)).setTextColor(getResources().getColor(R.color.colorReward));
        findViewById(R.id.titleRtxt_tv).setVisibility(View.VISIBLE);
        switch (modifyType) {
            case requestCodeTwo:
                if (!Util.isNull(userInfo.getNickName())) {
                    modifyInfoEt.setText(userInfo.getNickName());
                    modifyInfoEt.setSelection(userInfo.getNickName().length());
                } else
                    modifyInfoEt.setHint(getString(R.string.uInfoNickNameHint));
                break;
            case RequestCodeFour:
                if (!Util.isNull(userInfo.getUserEmail())) {
                    modifyInfoEt.setText(userInfo.getUserEmail());
                    modifyInfoEt.setSelection(userInfo.getUserEmail().length());
                } else
                    modifyInfoEt.setHint(getString(R.string.uInfoEmailHint));
                break;
            case RequestCodeFive:
                if (!Util.isNull(userInfo.getUserRealName())) {
                    modifyInfoEt.setText(userInfo.getUserRealName());
                    modifyInfoEt.setSelection(userInfo.getUserRealName().length());
                } else
                    modifyInfoEt.setHint(getString(R.string.uInfoRealNameHint));
                break;
            case RequestCodeSix:
                if (!Util.isNull(userInfo.getUserIdCard())) {
                    modifyInfoEt.setText(userInfo.getUserIdCard());
                    modifyInfoEt.setSelection(userInfo.getUserIdCard().length());
                } else
                    modifyInfoEt.setHint(getString(R.string.uInfoIDcardHint));
                break;
        }
    }
    private void notNullTip() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setDialogTitleMsg(getString(R.string.check_input), "");
        promptDialog.setBtnMsg("noShowCancelBtn",getString(R.string.mainSure));
        promptDialog.show();
        promptDialog.setDialogClickListener(new PromptDialog.DialogClickListener() {
            @Override
            public void cancelLisen() {
            }

            @Override
            public void confirmLisen() {
                promptDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.titleLeft_img, R.id.titleRtxt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.titleRtxt_tv:
                if(Util.isNull(modifyInfoEt.getText().toString())){
                    notNullTip();
                    return;
                }
                switch (modifyType) {
                    case requestCodeTwo:
                        if(modifyInfoEt.getText().toString().length() > 5){
                            Util.showToast(this,getString(R.string.check_nickInput));
                            return;
                        }
                        userInfo.setNickName(modifyInfoEt.getText().toString());
                        break;
                    case RequestCodeFour:
                        if(!Util.isEmail(modifyInfoEt.getText().toString())){
                            Util.showToast(this,getString(R.string.check_emailInput));
                            return;
                        }
                        userInfo.setUserEmail(modifyInfoEt.getText().toString());
                        break;
                    case RequestCodeFive:
                        userInfo.setUserRealName(modifyInfoEt.getText().toString());
                        break;
                    case RequestCodeSix:
                        userInfo.setUserIdCard(modifyInfoEt.getText().toString());
                        break;
                }
                api.modifyUserInfo(userInfo, null);
                break;
        }
    }
}
