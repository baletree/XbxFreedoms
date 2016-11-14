package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.yoojia.anyversion.Version;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.VersionBean;
import com.xbx123.freedom.receive.UpdateService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/7/1.
 */
public class UpdateVerActivity extends Activity {
    @Bind(R.id.new_versioncode_tv)
    TextView newVersioncodeTv;
    @Bind(R.id.new_versionmsg_tv)
    TextView newVersionmsgTv;
    @Bind(R.id.version_layout)
    RelativeLayout versionLayout;

    private VersionBean versionBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_dialog);
        ButterKnife.bind(this);
        versionBean = (VersionBean) getIntent().getSerializableExtra("UpdateVersion");
        if (versionBean != null) {
            newVersioncodeTv.setText(versionBean.getVersionName());
            newVersionmsgTv.setText(Html.fromHtml(versionBean.getVersionContent()));
            if (versionBean.isForceUpdate())
                findViewById(R.id.update_cancel_tv).setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.update_cancel_tv, R.id.update_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_cancel_tv:
                if (!versionBean.isForceUpdate()) {
                    finish();
                }
                break;
            case R.id.update_submit_tv:
                Intent intent = new Intent(UpdateVerActivity.this, UpdateService.class);
                intent.putExtra("apkUrl", versionBean.getVersionUrl());
//                startActivity(intent);
                startService(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (!versionBean.isForceUpdate()) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
