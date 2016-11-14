package com.xbx123.freedom.linsener;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.xbx123.freedom.http.RequestListener;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class RequestBackLisener implements RequestListener {

    private Context context;
    private Handler handler;

    public RequestBackLisener(Context context) {
        this.context = context;
    }

    public RequestBackLisener(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void requestSuccess(String json) {

    }

    @Override
    public void requestError(VolleyError e) {
        Util.checkNetError(context, e, handler);
    }
}
