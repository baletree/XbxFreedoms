package com.xbx123.freedom.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.Util;

import butterknife.Bind;

/**
 * Created by EricYuan on 2016/6/3.
 * 抢单倒计时
 */
public class CountDownDialog extends Dialog implements DialogInterface.OnKeyListener {
    TextView dialogCountTv;
    ImageView dialogImg;
    LinearLayout pressLongCancel_ll;
    private OnLongPressListener longPressListener;
    private Context context;

    private int downCount = 60;
    private Handler pHandler;
    private int imgCount = 0;
    private Api api = null;
    private String orderNum = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    if (downCount == 0) {
                        pHandler.sendEmptyMessage(0);
                        handler.removeMessages(2);
                        handler.removeMessages(1);
                    } else {
                        dialogCountTv.setText(downCount + "");
                        downCount--;
                    }
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
                case 1:
                    setLoadingImg();
                    if (imgCount == 8)
                        imgCount = 0;
                    else
                        imgCount++;
                    handler.sendEmptyMessageDelayed(1, 500);
                    break;
                case HttpRequestFlag.requestPageOne:
                    pHandler.sendEmptyMessage(0);
                    break;
            }
        }
    };

    public CountDownDialog(Context context) {
        super(context, R.style.DialogCount);
        this.context = context;
    }

    public void setOnLongPressListen(OnLongPressListener longPressListener){
        this.longPressListener = longPressListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_countdown);
        api = new Api(context,handler);
        dialogCountTv = (TextView) findViewById(R.id.dialogCount_tv);
        dialogImg = (ImageView) findViewById(R.id.dialogImg);
        pressLongCancel_ll = (LinearLayout) findViewById(R.id.pressLongCancel_ll);
        pressLongCancel_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                api.cancelCallServer(orderNum);
                return false;
            }
        });
        setOnKeyListener(this);
    }

    public void setDownCount(Handler pHandler) {
        this.pHandler = pHandler;
        downCount = 60;
        handler.sendEmptyMessage(2);
    }

    public void setOrderNum(String orderNum){
        this.orderNum = orderNum;
    }

    @Override
    public void show() {
        super.show();
        handler.sendEmptyMessage(1);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeMessages(2);
        handler.removeMessages(1);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return false;
    }

    public interface OnLongPressListener{
        void onLongPress();
    }

    private void setLoadingImg() {
        if (dialogImg == null)
            return;
        switch (imgCount) {
            case 0:
                dialogImg.setImageResource(R.drawable.countloading1);
                break;
            case 1:
                dialogImg.setImageResource(R.drawable.countloading2);
                break;
            case 2:
                dialogImg.setImageResource(R.drawable.countloading3);
                break;
            case 3:
                dialogImg.setImageResource(R.drawable.countloading4);
                break;
            case 4:
                dialogImg.setImageResource(R.drawable.countloading5);
                break;
            case 5:
                dialogImg.setImageResource(R.drawable.countloading6);
                break;
            case 6:
                dialogImg.setImageResource(R.drawable.countloading7);
                break;
            case 7:
                dialogImg.setImageResource(R.drawable.countloading8);
                break;
            case 8:
                dialogImg.setImageResource(R.drawable.countloading9);
                break;
        }
    }

}
