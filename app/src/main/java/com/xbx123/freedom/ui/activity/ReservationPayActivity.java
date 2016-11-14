package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xbx123.freedom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * project:XbxFreedom
 * author:Hi-Templar
 * dateTime:2016/10/4 11:19
 * describe:
 */
public class ReservationPayActivity extends BaseActivity {

    @Bind(R.id.payWays)
    RadioGroup payWays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_reservation_pay);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        ((TextView)findViewById(R.id.titleTxt_tv)).setText(getString(R.string.reservationPayTitle));
    }

    @OnClick({R.id.titleLeft_img,R.id.reservationPay_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.reservationPay_ll:
                switch(payWays.getCheckedRadioButtonId()) {
                    case R.id.payAlipay:
                        break;
                    case R.id.payWx:
                        break;
                }
                startActivity(new Intent(this,ReservationPayResultActivity.class));
                break;
        }
    }
}
