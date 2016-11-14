package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xbx123.freedom.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/13.
 */
public class WebShowActivity extends BaseActivity {
    private WebView webShow_wv;
    public static String webUrl = "";
    public static String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_show);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(title);
        webShow_wv = (WebView) findViewById(R.id.webShow_wv);
        webShow_wv.getSettings().setJavaScriptEnabled(true);
        webShow_wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webShow_wv.getSettings().setUseWideViewPort(true);
        webShow_wv.getSettings().setLoadWithOverviewMode(true);
        webShow_wv.setWebViewClient(new WebViewClient() {
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
        webShow_wv.loadUrl(webUrl);
    }

    @OnClick({R.id.titleLeft_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
        }
    }
}
