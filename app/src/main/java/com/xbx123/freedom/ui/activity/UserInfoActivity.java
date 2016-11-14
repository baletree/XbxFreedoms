package com.xbx123.freedom.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.LoginParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import java.io.File;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by EricYuan on 2016/5/25.
 */
public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.uInfoHead_img)
    RoundedImageView uInfoHeadImg;
    @Bind(R.id.uInfoNickName_tv)
    TextView uInfoNickNameTv;
    @Bind(R.id.uInfoSex_tv)
    TextView uInfoSexTv;
    @Bind(R.id.uInfoBirthday_tv)
    TextView uInfoBirthdayTv;
    @Bind(R.id.uInfoPhone_tv)
    TextView uInfoPhoneTv;
    @Bind(R.id.uInfoEmail_tv)
    TextView uInfoEmailTv;
    @Bind(R.id.uInfoRealName_tv)
    TextView uInfoRealNameTv;
    @Bind(R.id.uInfoIdcard_tv)
    TextView uInfoIdcardTv;

    private UserInfo userInfo = null;
    private boolean isUpdateName = false;
    private boolean isUpdateHead = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String modifyInfo = (String) msg.obj;
                    SharePrefer.saveUserInfo(UserInfoActivity.this, LoginParse.modifyUserInfo(modifyInfo, userInfo));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    private void setUserInfo(boolean isNeedLoadHead) {
        userInfo = SharePrefer.getUserInfo(this);
        uInfoNickNameTv.setText(userInfo.getNickName());
        if (!"0000-00-00".equals(userInfo.getUserBirthday()))
            uInfoBirthdayTv.setText(userInfo.getUserBirthday());
        if (!Util.isNull(userInfo.getUserPhone())) {
            String num = userInfo.getUserPhone();
            String result = num.substring(0, 3) + "****" + num.substring(7, num.length());
            uInfoPhoneTv.setText(result);
        }
        uInfoEmailTv.setText(userInfo.getUserEmail());
        uInfoRealNameTv.setText(userInfo.getUserRealName());
        uInfoIdcardTv.setText(userInfo.getUserIdCard());
        if (userInfo.getUserSex() == 0)
            uInfoSexTv.setText(getString(R.string.sexMale));
        else if (userInfo.getUserSex() == 1)
            uInfoSexTv.setText(getString(R.string.sexFemale));
        if (isNeedLoadHead)
            loadHeadImg();
    }

    private void loadHeadImg() {
        imageLoader.displayImage(userInfo.getUserHead(), uInfoHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this, handler);
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.uInfoTitle));
        setUserInfo(true);
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
                String picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
                Util.pLog("picPath:" + picPath);
                Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                if (bitmap == null)
                    return;
                isUpdateHead = true;
                uInfoHeadImg.setImageBitmap(bitmap);
                api.modifyUserInfo(userInfo, new File(picPath));
                break;
            case requestCodeTwo://昵称
                setUserInfo(false);
                break;
            case RequestCodeThree:
                int sexIndex = data.getIntExtra("sexMaleIndex", 0);
                userInfo.setUserSex(sexIndex);
                if (userInfo.getUserSex() == 0)
                    uInfoSexTv.setText(getString(R.string.sexMale));
                else if (userInfo.getUserSex() == 1)
                    uInfoSexTv.setText(getString(R.string.sexFemale));
                api.modifyUserInfo(userInfo, null);
                break;
            case RequestCodeFour:
            case RequestCodeFive:
            case RequestCodeSix:
                setUserInfo(false);
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.uInfoHead_rl, R.id.uInfoNickName_rl, R.id.uInfoSex_rl, R.id.uInfoBirthday_rl,
            R.id.uInfoPhone_rl, R.id.uInfoEmail_rl, R.id.uInfoRealname_rl, R.id.uInfoIDcard_rl})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                intent.putExtra("isUpdateHead", isUpdateHead);
                intent.putExtra("isUpdateName", isUpdateName);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.uInfoHead_rl:
                intent.setClass(this, SelectAlbumActivity.class);
                startActivityForResult(intent, requestCodeOne);
                break;
            case R.id.uInfoNickName_rl:
                intent.setClass(this, ModifyInfoActivity.class);
                intent.putExtra("ModifyType", requestCodeTwo);
                intent.putExtra("UserInfoData", userInfo);
                isUpdateName = true;
                startActivityForResult(intent, requestCodeTwo);
                break;
            case R.id.uInfoSex_rl:
                intent.setClass(this, SelectSexActivity.class);
                startActivityForResult(intent, RequestCodeThree);
                break;
            case R.id.uInfoBirthday_rl:
                choiceBirthDay();
                break;
            case R.id.uInfoPhone_rl:
                break;
            case R.id.uInfoEmail_rl:
                intent.setClass(this, ModifyInfoActivity.class);
                intent.putExtra("ModifyType", RequestCodeFour);
                intent.putExtra("UserInfoData", userInfo);
                startActivityForResult(intent, RequestCodeFour);
                break;
            case R.id.uInfoRealname_rl:
                intent.setClass(this, ModifyInfoActivity.class);
                intent.putExtra("ModifyType", RequestCodeFive);
                intent.putExtra("UserInfoData", userInfo);
                startActivityForResult(intent, RequestCodeFive);
                break;
            case R.id.uInfoIDcard_rl:
                intent.setClass(this, ModifyInfoActivity.class);
                intent.putExtra("ModifyType", RequestCodeSix);
                intent.putExtra("UserInfoData", userInfo);
                startActivityForResult(intent, RequestCodeSix);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("isUpdateHead", isUpdateHead);
            intent.putExtra("isUpdateName", isUpdateName);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置生日日期
     */
    private void choiceBirthDay() {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                if (year >= cYear && monthOfYear >= month && dayOfMonth > day) {
                    Util.showToast(UserInfoActivity.this, getString(R.string.check_birthday));
                    return;
                }
                uInfoBirthdayTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                userInfo.setUserBirthday(uInfoBirthdayTv.getText().toString());
                api.modifyUserInfo(userInfo, null);
            }
        }, cYear, month, day);
        datePicker.show();
    }
}
