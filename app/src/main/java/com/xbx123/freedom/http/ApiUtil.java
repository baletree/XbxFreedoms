package com.xbx123.freedom.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/6/1.
 */
public class ApiUtil {
    public Handler mHandler;
    public Context context;
    /**
     * 无判断code=0的情况
     */
    public void sendEmptyMsg(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        if (UtilParse.getRequestCode(json) == 1) {
            msg.obj = UtilParse.getRequestData(json);
            msg.what = flag;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 无判断code=0的情况
     */
    public void sendEmptyMsgDeploy(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        if (UtilParse.getRequestCode(json) == 1) {
            msg.obj = UtilParse.getRequestData(json);
            msg.what = flag;
            mHandler.sendMessageDelayed(msg,2000);
        }
    }

    /**
     * 有判断code=0的情况
     */
    public void sendShowMsg(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        if (UtilParse.getRequestCode(json) == 1) {
            msg.obj = UtilParse.getRequestData(json);
            msg.what = flag;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(HttpRequestFlag.requestCodeZero);
            String showMsg = UtilParse.getRequestMsg(json);
            if (!Util.isNull(showMsg))
                Util.showToast(context, showMsg);
        }
    }

    /**
     * 发送含有data标签的所有数据
     * @param flag
     * @param json
     */
    public void sendCompleteData(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        msg.obj = json;
        msg.what = flag;
        mHandler.sendMessage(msg);
    }
}
