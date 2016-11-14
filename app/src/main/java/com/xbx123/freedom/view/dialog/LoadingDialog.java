package com.xbx123.freedom.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.xbx123.freedom.R;

/**
 * Created by EricYuan on 2016/4/20.
 */
public class LoadingDialog extends Dialog {
    private ImageView dialogLoading_img;
    private Context context;

    private String msg = "";
    private int count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setLoadingImg();
                    if (count == 2)
                        count = 0;
                    else
                        count++;
                    handler.sendEmptyMessageDelayed(1, 150);
                    break;
            }
        }
    };

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        initViews();

    }

    private void initViews() {
        dialogLoading_img = (ImageView) findViewById(R.id.dialogLoading_img);
    }

    @Override
    public void show() {
        super.show();
        count = 0;
        handler.sendEmptyMessage(1);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        count = 0;
        handler.removeMessages(1);
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    private void setLoadingImg() {
        if (dialogLoading_img == null)
            return;
        switch (count) {
            case 0:
                dialogLoading_img.setImageResource(R.drawable.loading1);
                break;
            case 1:
                dialogLoading_img.setImageResource(R.drawable.loading2);
                break;
            case 2:
                dialogLoading_img.setImageResource(R.drawable.loading3);
                break;
        }
    }
}
