package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.IdCard;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/15.
 * 添加被保人
 */
public class InsurerAddActivity extends BaseActivity {
    @Bind(R.id.insurerNameEt)
    EditText insurerNameEt;
    @Bind(R.id.insurerIdCardEt)
    EditText insurerIdCardEt;
    @Bind(R.id.insurerPhoneEt)
    EditText insurerPhoneEt;

    private UserInfo userInfo = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpRequestFlag.requestPageOne:
                    String addData = (String) msg.obj;
                    if(Util.isNull(addData))
                        return;
                    Util.showToast(InsurerAddActivity.this,UtilParse.getRequestMsg(addData));
                    if(UtilParse.getRequestCode(addData) == 1){
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insurer);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        userInfo = SharePrefer.getUserInfo(this);
        api = new Api(this,handler);
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.insurerAdd));
    }

    @OnClick({R.id.titleLeft_img, R.id.insurerSave_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.insurerSave_btn:
                if(Util.isNull(insurerNameEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerMsgTip));
                    return;
                }
                if(Util.isNull(insurerIdCardEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerIdCardTip));
                    return;
                }
                if(Util.isNull(insurerPhoneEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerPhoneHint));
                    return;
                }
                if(!StringUtil.isNameChinese(insurerNameEt.getText().toString())){
                    Util.showToast(this,getString(R.string.insurerIdNameCheck));
                    return;
                }
                if(new IdCard(insurerIdCardEt.getText().toString()).isCorrect() != 0){
                    Util.showToast(this,getString(R.string.insurerIdCardCheck));
                    return;
                }
                if(userInfo == null)
                    return;
                api.addInsurer(userInfo.getUid(),insurerNameEt.getText().toString(),insurerIdCardEt.getText().toString(),insurerPhoneEt.getText().toString());
                break;
        }
    }
}
