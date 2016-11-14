package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xbx123.freedom.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/7/8.
 */
public class MessageCenterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.menuMsg));
    }

    @OnClick(R.id.titleLeft_img)
    public void onClick() {
        finish();
    }
}
