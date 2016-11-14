package com.xbx123.freedom.linsener;

import android.content.Context;

import com.android.volley.VolleyError;
import com.xbx123.freedom.http.RequestListener;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class RequestNoErrorBackLisener implements RequestListener {

    private Context context;

    public RequestNoErrorBackLisener(Context context){
        this.context = context;
    }
    @Override
    public void requestSuccess(String json) {

    }

    @Override
    public void requestError(VolleyError e) {
    }
}
