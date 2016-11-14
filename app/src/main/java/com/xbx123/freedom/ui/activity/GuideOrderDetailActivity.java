package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xbx123.freedom.R;

/**
 * Created by EricPeng on 2016/10/5.
 *
 */
public class GuideOrderDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_order);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.oDetailTitle));
    }
}
