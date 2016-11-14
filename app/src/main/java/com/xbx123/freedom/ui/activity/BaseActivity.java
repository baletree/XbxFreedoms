package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.view.dialog.PromptDialog;

import java.util.Calendar;

import butterknife.ButterKnife;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public final int requestCodeOne = 420;
    public final int requestCodeTwo = 530;
    public final int RequestCodeThree = 640;
    public final int RequestCodeFour = 750;
    public final int RequestCodeFive = 860;
    public final int RequestCodeSix = 970;
    Calendar calendar = Calendar.getInstance();
    final int cYear = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    public LocalBroadcastManager lBManager = null;
    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;
    public Api api = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initDatas();
        initViews();
    }

    protected void initViews() {

    }

    protected void initDatas() {
        lBManager = LocalBroadcastManager.getInstance(this);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 没有安转微信
     */
    public void notInstallWx() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setDialogTitleMsg(getString(R.string.app_tip), getString(R.string.notInstallWx));
        promptDialog.setBtnMsg("noShowCancelBtn", getString(R.string.mainSure));
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
}
