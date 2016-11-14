package com.xbx123.freedom.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/12.
 */
public class FeedBackActivity extends BaseActivity {
    @Bind(R.id.feedBack_et)
    EditText feedBackEt;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String dataRe = (String) msg.obj;
                    if (UtilParse.getRequestCode(dataRe) == 1){
                        Util.showToast(FeedBackActivity.this, UtilParse.getRequestMsg(dataRe));
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void initViews() {
        api = new Api(this, handler);
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.feedBackTitle));
    }

    @OnClick({R.id.titleLeft_img, R.id.feedBack_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.feedBack_btn:
                String content = feedBackEt.getText().toString();
                if (Util.isNull(content)) {
                    Util.showToast(this, getString(R.string.feedNotNull));
                    return;
                }
                api.submitFeedBack(SharePrefer.getUserInfo(this).getUid(), content);
                break;
        }
    }
}
