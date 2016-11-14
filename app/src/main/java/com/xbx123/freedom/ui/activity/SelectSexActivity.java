package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xbx123.freedom.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/25.
 * 性别选择
 */
public class SelectSexActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_choice);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sexMale_tv, R.id.sexFemale_tv, R.id.sexCancel_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.sexMale_tv:
                intent.putExtra("sexMaleIndex",0);
                setResult(RESULT_OK,intent);
                break;
            case R.id.sexFemale_tv:
                intent.putExtra("sexMaleIndex",1);
                setResult(RESULT_OK,intent);
                break;
            case R.id.sexCancel_tv:
                break;
        }
        finish();
    }
}
