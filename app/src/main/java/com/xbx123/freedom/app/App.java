package com.xbx123.freedom.app;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.MapLocate;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONTokener;

import java.io.File;
import java.util.HashSet;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {
    private static Context mContext;
    private MapLocate mapLocate = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        getPhoneId();
        initSDK();
        toStartLocate();
    }

    private void initSDK() {
        initImageLoader();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        SDKInitializer.initialize(this);
        WXAPIFactory.createWXAPI(this, Constant.WXKey, false).registerApp(Constant.WXKey);
    }

    private void toStartLocate() {
        mapLocate = new MapLocate(mContext);
        mapLocate.startLocate();
    }

    private void getPhoneId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SharePrefer.savePhoneId(mContext, tm.getDeviceId());
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .threadPoolSize(4)
                .diskCache(new UnlimitedDiscCache(new File(Constant.PICTURE_ALBUM_PATH)))
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getContext() {
        return mContext;
    }
}
