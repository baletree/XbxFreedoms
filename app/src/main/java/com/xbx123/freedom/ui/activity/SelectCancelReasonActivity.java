package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xbx123.freedom.R;
import com.xbx123.freedom.view.views.WheelView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/10/19.
 */

public class SelectCancelReasonActivity extends Activity {
    @Bind(R.id.select_reservation_wlv)
    WheelView select_reservation_wlv;
    String[] reasonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_reservation_num);
        ButterKnife.bind(this);
        findViewById(R.id.select_revCancel_tv).setVisibility(View.VISIBLE);
        initData();
    }

    private void initData() {
        reasonArray = getResources().getStringArray(R.array.cancelReason);
        select_reservation_wlv.setItems(Arrays.asList(reasonArray));
        select_reservation_wlv.setSeletion(0);
    }

    @OnClick({R.id.select_revCancel_tv, R.id.select_revSure_tv, R.id.dialogReservation_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogReservation_rl:
            case R.id.select_revCancel_tv:
                finish();
                break;
            case R.id.select_revSure_tv:
                Intent intent = new Intent();
                intent.putExtra("SelectPositionStr", reasonArray[select_reservation_wlv.getSeletedIndex()]);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
