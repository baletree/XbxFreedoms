package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/15.
 * 索取发票填写信息
 */
public class InvoicesInfoActivity extends BaseActivity {
    @Bind(R.id.invoicesCompanyEt)
    EditText invoicesCompanyEt;
    @Bind(R.id.invoicesMonTv)
    TextView invoicesMonTv;
    @Bind(R.id.invoicesNameEt)
    EditText invoicesNameEt;
    @Bind(R.id.invoicesPhoneEt)
    EditText invoicesPhoneEt;
    @Bind(R.id.invoiceAddressEt)
    EditText invoiceAddressEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices_info);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.invoiceTitle));
    }

    @OnClick({R.id.titleLeft_img, R.id.invoiceSubmitBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.invoiceSubmitBtn:
                break;
        }
    }
}
