package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.UrlConstant;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.view.dialog.PromptDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/30.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.settingTile));
    }

    @OnClick({R.id.titleLeft_img, R.id.settingGuide_rl, R.id.settingInsurer_rl, R.id.settingProvision_rl, R.id.settingInvoice_rl, R.id.settingAboutUs_rl,
            R.id.settingSub_btn, R.id.settingContacts_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.settingGuide_rl://用户指南
                WebShowActivity.webUrl = UrlConstant.urlHelp;
                WebShowActivity.title = getString(R.string.settingGuide);
                startActivity(new Intent(this, WebShowActivity.class));
                break;
            case R.id.settingInsurer_rl://被保人列表
                Intent intent = new Intent(this, InsurerListActivity.class);
                intent.putExtra("isFromSetting", true);
                startActivity(intent);
                break;
            case R.id.settingContacts_rl://常用联系人
                Intent intents = new Intent(this, FrequentContactsActivity.class);
                intents.putExtra("isFromSetting", true);
                startActivity(intents);
                break;
            case R.id.settingProvision_rl://法律条款
                WebShowActivity.webUrl = UrlConstant.urlLegal;
                WebShowActivity.title = getString(R.string.settingProvision);
                startActivity(new Intent(this, WebShowActivity.class));
                break;
            case R.id.settingInvoice_rl://索取发票
                startActivity(new Intent(this, InvoicesRequestActivity.class));
                break;
            case R.id.settingAboutUs_rl:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.settingSub_btn:
                final PromptDialog exitDialog = new PromptDialog(this);
                exitDialog.setDialogTitleMsg(getString(R.string.exitLoginTitle), getString(R.string.exitLoginMsg));
                exitDialog.setDialogClickListener(new PromptDialog.DialogClickListener() {
                    @Override
                    public void cancelLisen() {
                        exitDialog.dismiss();
                    }

                    @Override
                    public void confirmLisen() {
                        exitDialog.dismiss();
                        SharePrefer.saveUserInfo(SettingActivity.this, new UserInfo());
                        LocalBroadcastManager lBManager = LocalBroadcastManager.getInstance(SettingActivity.this);
//                        lBManager.sendBroadcast(new Intent().setAction(Constant.actionExitApp));
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.putExtra("isTokenLose", true);
//                        startActivity(intent);
                        finish();
                    }
                });
                exitDialog.show();
                break;
        }
    }
}
