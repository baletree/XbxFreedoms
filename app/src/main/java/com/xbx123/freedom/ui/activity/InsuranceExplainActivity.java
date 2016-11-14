package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.constant.StrConstant;

/**
 * Created by EricYuan on 2016/6/20.
 * 保险说明页面
 */
public class InsuranceExplainActivity extends Activity implements View.OnClickListener {
    private TextView insuranceTxt_tv;
    private WebView insuranceWv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_explain);
        insuranceTxt_tv = (TextView) findViewById(R.id.insuranceTxt_tv);
        insuranceWv = (WebView) findViewById(R.id.insuranceWv);
        insuranceTxt_tv.setText(StrConstant.Insurance.insStr1);
        findViewById(R.id.finishImg).setOnClickListener(this);
    }

    private void initWebView(){
        insuranceWv.getSettings().setJavaScriptEnabled(true);
        insuranceWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        insuranceWv.getSettings().setUseWideViewPort(true);
        insuranceWv.getSettings().setLoadWithOverviewMode(true);
        insuranceWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:")
                        || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url); // 在当前的webview中跳转到新的url
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
