package com.xbx123.freedom.receive;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import java.io.File;

/**
 * Created by EricYuan on 2016/7/1.
 */
public class DownLoadReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constant.actionDownAppCom.equals(intent.getAction())) {
            File file = new File(Constant.APK_PATH + intent.getStringExtra("ApkName"));
            Util.pLog("action Install !"+file.getAbsolutePath());
            Intent intentIns = new Intent(Intent.ACTION_VIEW);
            intentIns.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intentIns.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentIns);
        }
    }
}
