package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * project:XbxFreedom
 * author:Hi-Templar
 * dateTime:2016/10/4 17:08
 * describe:
 */
public class ReservationPayResultActivity extends BaseActivity {

    @Bind(R.id.reservationSuccessLayout)
    LinearLayout reservationSuccessLayout;
    @Bind(R.id.reservationFailLayout)
    LinearLayout reservationFailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_submit);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
//        findViewById(R.id.titleTxt_tv).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.titleTxt_tv)).setText(getString(R.string.reservationSubmitTitle));
    }

    @OnClick({R.id.titleLeft_img,R.id.reservationSubmitBackHome,R.id.reservationSubmitLookOrder,R.id.reservationSubmitFailBackHome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.reservationSubmitBackHome:

                break;
            case R.id.reservationSubmitLookOrder:
                startActivity(new Intent(this,GuideOrderDetailActivity.class));
                break;
            case R.id.reservationSubmitFailBackHome:
                break;
        }
    }
}
