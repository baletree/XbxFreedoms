package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/8/12.
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.versionName_tv)
    TextView versionNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        versionNameTv.setText(getString(R.string.aboutUsVersionName) + Util.getAppVersionName(this));
    }


    @OnClick({R.id.aboutUsBack_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutUsBack_img:
                finish();
                break;

        }
    }
}
