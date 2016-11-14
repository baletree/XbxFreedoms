package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ResortsBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/3.
 * 首页存在多景区，景区选择界面
 */
public class SelectResortsActivity extends Activity {
    @Bind(R.id.resortsList_fl)
    FlowLayout resortsListFl;

    private List<ResortsBean> resortsList = null;
    private boolean isAllResorts = true;

    private WindowManager manager = null;
    private DisplayMetrics outMetrics = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_resorts);
        ButterKnife.bind(this);
        manager = this.getWindowManager();
        outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        initResorts();
    }

    private void initResorts() {
        resortsList = (List<ResortsBean>) getIntent().getSerializableExtra("ResortsList");
        isAllResorts = getIntent().getBooleanExtra("isAllResorts", true);
        if (resortsList == null)
            return;
        Util.pLog("resortsList == null "+(resortsList == null));
        resortsView();
    }

    private void resortsView() {
        int width2 = outMetrics.widthPixels;
        int itemWidth = (width2 - Util.dip2px(this,24f))/3;
        for (int i = 0; i < resortsList.size(); i++) {
            LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(
                    itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(this);
            View view = LayoutInflater.from(this).inflate(R.layout.item_select_resorts, null);
            TextView tetView = (TextView) view.findViewById(R.id.resortsNameTv);
            tetView.setText(resortsList.get(i).getResortsName());
            view.setLayoutParams(viewLp);
            linearLayout.setLayoutParams(viewLp);
            linearLayout.addView(view);
            resortsListFl.addView(linearLayout);
            final int position = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("ResortsBean",resortsList.get(position));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }

    @OnClick(R.id.resortsChoiceRl)
    public void onClick() {
        finish();
    }
}
