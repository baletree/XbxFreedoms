package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.xbx123.freedom.R;
import com.xbx123.freedom.view.views.WheelView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/4.
 * 出行遊玩人數選擇
 */
public class SelectReservationInfo extends Activity {
    @Bind(R.id.select_reservation_wlv)
    WheelView select_reservation_wlv;

    String[] reservationArray = null;
    private int reservationCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_reservation_num);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        reservationCode = getIntent().getIntExtra("reservationFlag", 0);
        switch (reservationCode) {
            case 0:
                reservationArray = getResources().getStringArray(R.array.reservationDate);
                break;
            case 1:
                reservationArray = getResources().getStringArray(R.array.reservationStar);
                break;
            case 2:
                reservationArray = getResources().getStringArray(R.array.reservationLanguage);
                break;
            case 3:
                reservationArray = getResources().getStringArray(R.array.reservationSex);
                break;
            case 4:
                reservationArray = getResources().getStringArray(R.array.reservationServiceFee);
                break;
        }
        select_reservation_wlv.setItems(Arrays.asList(reservationArray));
        select_reservation_wlv.setSeletion(0);
    }

    @OnClick({R.id.select_revCancel_tv, R.id.select_revSure_tv, R.id.dialogReservation_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogReservation_rl:
            case R.id.select_revCancel_tv:
                if (reservationCode == 0)
                    return;
                finish();
                break;
            case R.id.select_revSure_tv:
                backResultPosition();
                break;
        }
    }

    /**
     * 选择后的数据返回
     */
    private void backResultPosition() {
        Intent intent = new Intent();
        intent.putExtra("SelectPositionIndex", select_reservation_wlv.getSeletedIndex());
        intent.putExtra("SelectPositionStr", reservationArray[select_reservation_wlv.getSeletedIndex()]);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (reservationCode != 0)
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
