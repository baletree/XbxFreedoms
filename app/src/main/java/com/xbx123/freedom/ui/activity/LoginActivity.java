package com.xbx123.freedom.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.LoginParse;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.constant.UrlConstant;
import com.xbx123.freedom.utils.db.DBManager;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by EricYuan on 2016/5/30.
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.loginPhone_et)
    EditText loginPhoneEt;
    @Bind(R.id.loginCode_et)
    EditText loginCodeEt;
    @Bind(R.id.loginGetCode_tv)
    TextView loginGetCodeTv;

    private Api api = null;

    private int codeCount = 60;
    private boolean isTokenLose = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne://获取验证码
//                    Util.showToast(LoginActivity.this, getString(R.string.loginGetCodeSuc) + (String) msg.obj);
                    Util.showToast(LoginActivity.this, getString(R.string.loginGetCodeSuc));
                    handler.sendEmptyMessage(HttpRequestFlag.requestPageTwo);
                    loginGetCodeTv.setBackgroundResource(R.drawable.gray_rectangle_bg);
                    loginGetCodeTv.setEnabled(false);
                    break;
                case HttpRequestFlag.requestPageTwo://倒计时
                    if (codeCount == 0) {
                        codeCount = 60;
                        loginGetCodeTv.setText(getString(R.string.loginGetCode));
                        loginGetCodeTv.setBackgroundResource(R.drawable.theme_rectangle_bg);
                        loginGetCodeTv.setEnabled(true);
                    } else {
                        loginGetCodeTv.setText(codeCount + "s");
                        codeCount--;
                        handler.sendEmptyMessageDelayed(HttpRequestFlag.requestPageTwo, 1 * 1000);
                    }
                    break;
                case HttpRequestFlag.requestPageThree://登录成功
                    UserInfo userInfo = LoginParse.getUserInfo((String) msg.obj);
                    SharePrefer.saveUserInfo(LoginActivity.this, userInfo);
                    DBManager.getInstance().deleteTableLatLng();
                    Intent intent = new Intent();
                    lBManager.sendBroadcast(intent.setAction(Constant.actionLoginSuc));
                    if (isTokenLose) {
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        isTokenLose = getIntent().getBooleanExtra("isTokenLose", false);
        api = new Api(this, handler);
    }

    @Override
    protected void initViews() {
        super.initViews();
        String savePhone = SharePrefer.getUserPhone(this);
        if (!Util.isNull(savePhone) && savePhone.length() == 11) {
            loginPhoneEt.setText(savePhone);
            loginPhoneEt.setSelection(savePhone.length());
            findViewById(R.id.loginClearPhone_img).setVisibility(View.VISIBLE);
            findViewById(R.id.loginGetCode_tv).setBackgroundResource(R.drawable.theme_rectangle_bg);
            findViewById(R.id.loginGetCode_tv).setEnabled(true);
        }
        initTxtListener();
    }

    @OnClick({R.id.loginBack_img, R.id.loginClearPhone_img, R.id.loginGetCode_tv, R.id.login_btn, R.id.loginPact_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBack_img:
                finish();
                break;
            case R.id.loginClearPhone_img:
                loginPhoneEt.setText("");
                break;
            case R.id.loginGetCode_tv:
                String phone = loginPhoneEt.getText().toString();
                if (!Util.checkTel(phone)) {
                    Util.showToast(this, getString(R.string.loginPhoneCheck));
                    return;
                }
                SharePrefer.savePhone(this, phone);
                api.getCode(phone);
                break;
            case R.id.login_btn:
                String phoneLogin = loginPhoneEt.getText().toString();
                if (!Util.checkTel(phoneLogin)) {
                    Util.showToast(this, getString(R.string.loginPhoneCheck));
                    return;
                }
                String jPushId = SharePrefer.getJpushId(LoginActivity.this);
                if (!Util.isNull(jPushId))
                    api.toLogin(phoneLogin, loginCodeEt.getText().toString(), jPushId);
                else {
                    jPushId = JPushInterface.getRegistrationID(this);
                    SharePrefer.saveJPushId(this, jPushId);
                    api.toLogin(phoneLogin, loginCodeEt.getText().toString(), jPushId);
                }
                Util.pLog("pushId:" + "save:" + jPushId + " RegsId:" + JPushInterface.getRegistrationID(this));
                break;
            case R.id.loginPact_tv://服务协议
                WebShowActivity.webUrl = UrlConstant.urlPactService;
                WebShowActivity.title = getString(R.string.serviceIns);
                startActivity(new Intent(this, WebShowActivity.class));
                break;
        }
    }

    private void initTxtListener() {
        loginPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    findViewById(R.id.loginClearPhone_img).setVisibility(View.GONE);
                }
                if (s.length() == 1) {
                    findViewById(R.id.loginClearPhone_img).setVisibility(View.VISIBLE);
                }
                if (s.length() == 11 && codeCount == 60) {
                    findViewById(R.id.loginGetCode_tv).setBackgroundResource(R.drawable.theme_rectangle_bg);
                    findViewById(R.id.loginGetCode_tv).setEnabled(true);
                } else {
                    findViewById(R.id.loginGetCode_tv).setBackgroundResource(R.drawable.gray_rectangle_bg);
                    findViewById(R.id.loginGetCode_tv).setEnabled(false);
                }
            }
        });
        loginCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {

                    findViewById(R.id.login_btn).setBackgroundResource(R.drawable.theme_rectangle_bg);
                    findViewById(R.id.login_btn).setEnabled(true);
                } else {
                    findViewById(R.id.login_btn).setBackgroundResource(R.drawable.gray_rectangle_bg);
                    findViewById(R.id.login_btn).setEnabled(false);
                }
            }
        });
    }
}
