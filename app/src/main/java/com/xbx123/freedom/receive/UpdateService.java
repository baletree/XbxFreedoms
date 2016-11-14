package com.xbx123.freedom.receive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by EricYuan on 2016/7/5.
 * 版本更新
 */
public class UpdateService extends Service {
    private int fileSize = 0;
    private int downLoadSize = 0;

    private NotificationManager notiManager = null;
    private NotificationCompat.Builder mBuilder = null;
    private Notification notification = null;
    private RemoteViews rViews = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        rViews = new RemoteViews(getPackageName(), R.layout.notification_update);
        rViews.setTextViewText(R.id.notiTitle, getString(R.string.app_name));
        mBuilder.setSmallIcon(R.mipmap.icon).setOngoing(true);
        mBuilder.setContent(rViews);
        notification = mBuilder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Util.pLog("apkUrl = " + intent.getStringExtra("apkUrl"));
        downApk(intent.getStringExtra("apkUrl"));
        return super.onStartCommand(intent, flags, startId);
    }

    private void downApk(final String apkUrl) {
        new Thread() {
            @Override
            public void run() {
                toDownApk(apkUrl);
            }
        }.start();
    }

    private void toDownApk(String apkUrl) {
        String filename = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        File file1 = new File(Constant.APK_PATH);
        File file2 = new File(Constant.APK_PATH + filename);
        if (!file1.exists())
            file1.mkdirs();
        try {
            if (!file2.exists())
                file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadUpdateFile(apkUrl, file2, filename);
    }

    public void downloadUpdateFile(String apkUrl, File saveFile, String fileName) {
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(apkUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            this.fileSize = httpConnection.getContentLength();//根据响应获取文件大小
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            notiManager.notify(111, mBuilder.build());
            downLoadSize = 0;
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;
            int downLoadCount = 0;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                downLoadSize += readsize;
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(0);
                String reBaFen = numberFormat.format((float) downLoadSize / (float) fileSize * 100);
                if (Integer.parseInt(reBaFen) != downLoadCount) {
                    rViews.setProgressBar(R.id.updatePb, fileSize, downLoadSize, false);
                    rViews.setTextViewText(R.id.updateSizeTv, reBaFen + "%");
                    notiManager.notify(111, mBuilder.build());
                    downLoadCount = Integer.parseInt(reBaFen);
                    Util.pLog("downLoadSize = " + reBaFen);
                }
            }

            notiManager.cancel(111);
            Intent intent = new Intent(Constant.actionDownAppCom);
            intent.putExtra("ApkName", fileName);
            sendBroadcast(intent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
