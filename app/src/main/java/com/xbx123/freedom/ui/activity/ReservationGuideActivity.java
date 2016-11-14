package com.xbx123.freedom.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/4.
 * 预约导游信息选择
 */
public class ReservationGuideActivity extends AppCompatActivity {

    private final int dateNumCode = 100;
    private final int starVerCode = 101;
    private final int languageCode = 102;
    private final int sexCode = 103;
    private final int serviceFeeCode = 104;

    @Bind(R.id.tripDate_tv)
    TextView tripDate_tv;//出行日期
    @Bind(R.id.tripPNum_tv)
    TextView tripPNumTv;//游玩人数
    @Bind(R.id.tripStar_tv)
    TextView tripStarTv;//星级
    @Bind(R.id.tripLanguage_tv)
    TextView tripLanguageTv;//语言
    @Bind(R.id.tripSex_tv)
    TextView tripSexTv;//性别
    @Bind(R.id.tripPriceFee_tv)
    TextView tripPriceFeeTv;//服务费

    private Map<String, String> sortConditionMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guide_reservation);
        ButterKnife.bind(this);
        sortConditionMap = new HashMap<>();
        tripDate_tv.setText(StringUtil.getNextDate());
        sortConditionMap.put("TripDate", StringUtil.getNextDate());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case dateNumCode://出游的天数选择
                int tripDateNum = data.getIntExtra("SelectPositionIndex", 0);
                sortConditionMap.put("TripDateNum", (tripDateNum + 1) + "");
                tripPNumTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
            case starVerCode://星级不限
                int tripStar = data.getIntExtra("SelectPositionIndex", 0);
                if (tripStar == 0)
                    return;
                sortConditionMap.put("TripStarLev", tripStar + "");
                tripStarTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
            case languageCode://普通话
                int tripLanguage = data.getIntExtra("SelectPositionIndex", 0);
                sortConditionMap.put("TripLanguage", (tripLanguage + 1) + "");
                tripLanguageTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
            case sexCode://性别不限
                int tripSex = data.getIntExtra("SelectPositionIndex", 0);
                if (tripSex == 0)
                    return;
                sortConditionMap.put("TripSex", tripSex + "");
                tripSexTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
            case serviceFeeCode://服务费不限
                int tripServiceFee = data.getIntExtra("SelectPositionIndex", 0);
                if (tripServiceFee == 0)
                    return;
                sortConditionMap.put("TripServiceFee", data.getStringExtra("SelectPositionStr"));
                tripPriceFeeTv.setText(data.getStringExtra("SelectPositionStr"));
                break;
        }
    }

    @OnClick({R.id.reservationActivityRL, R.id.reservationDate_ll, R.id.reservationPNum_ll, R.id.reservationStar_ll, R.id.reservationLanguage_ll, R.id.reservationSex_ll, R.id.reservationServiceFee_ll,
            R.id.guideNext_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.reservationActivityRL:
                finish();
                break;
            case R.id.reservationDate_ll:
                selectTripDate();
                break;
            case R.id.reservationPNum_ll:
                intent.putExtra("reservationFlag", 0);
                startActivityForResult(intent.setClass(this, SelectReservationInfo.class), dateNumCode);
                break;
            case R.id.reservationStar_ll:
                intent.putExtra("reservationFlag", 1);
                startActivityForResult(intent.setClass(this, SelectReservationInfo.class), starVerCode);
                break;
            case R.id.reservationLanguage_ll:
                intent.putExtra("reservationFlag", 2);
                startActivityForResult(intent.setClass(this, SelectReservationInfo.class), languageCode);
                break;
            case R.id.reservationSex_ll:
                intent.putExtra("reservationFlag", 3);
                startActivityForResult(intent.setClass(this, SelectReservationInfo.class), sexCode);
                break;
            case R.id.reservationServiceFee_ll:
                intent.putExtra("reservationFlag", 4);
                startActivityForResult(intent.setClass(this, SelectReservationInfo.class), serviceFeeCode);
                break;
            case R.id.guideNext_tv:
                if (Util.isNull(sortConditionMap.get("TripDate"))) {
                    Util.showToast(this, getString(R.string.mainAppointDateNull));
                    return;
                }
                if (Util.isNull(sortConditionMap.get("TripDateNum"))) {
                    Util.showToast(this, getString(R.string.mainAppointPNumNull));
                    return;
                }
                intent.setClass(this, GuideListActivity.class);
                intent.putExtra("ReservationMapInfo", (Serializable) sortConditionMap);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 设置游玩日期
     */
    private void selectTripDate() {
        Calendar calendar = Calendar.getInstance();
        final int cYear = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                if (year >= cYear && monthOfYear >= month && dayOfMonth >= day) {
                    tripDate_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    sortConditionMap.put("TripDate", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } else {
                    Util.showToast(ReservationGuideActivity.this, getString(R.string.mainAppointDateTip));
                }
            }
        }, cYear, month, day);
        datePicker.show();
    }
}
