package com.xbx123.freedom.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.oneapm.agent.android.OneApmAgent;
import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/6/17.
 */
public class LoadingActivity extends BaseActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case requestCodeOne:
                    if (SharePrefer.getFistUseState(LoadingActivity.this)) {
                        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoadingActivity.this, TuTuPagesActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = this.getWindow();
        window.setFlags(flag, flag);
        super.onCreate(savedInstanceState);
        if (checkActivity())
            return;
        setContentView(R.layout.activity_loading);
        handler.sendEmptyMessageDelayed(requestCodeOne, 2500);
//        OneApmAgent.init(this.getApplicationContext()).setToken("1EB3A05896F583D0601FE76A10523EC740").start();//Beta
        OneApmAgent.init(this.getApplicationContext()).setToken("148A2C80AB89DD014DDDD566DA4352FF40").start();//OnLine
//        ((TextView) findViewById(R.id.loadingVersionTv)).setText("V" + getAppVersionName(this));
    }

    private boolean checkActivity() {
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return true;
            }
        }
        return false;
    }


}
