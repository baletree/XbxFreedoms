package com.xbx123.freedom.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/5/25.
 * 提示对话框
 */
public class PromptDialog extends Dialog implements View.OnClickListener {
    private TextView dialogTitle_tv;
    private TextView dialogMsg_tv;
    private TextView dialogCancel_tv;
    private TextView dialogSure_tv;

    private String dialogTitle = "";
    private String dialogMsg = "";
    private String dialogCancel = "";
    private String dialogSure = "";

    private DialogClickListener clickListener;

    public PromptDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
        setCanceledOnTouchOutside(false);
        initViews();
    }

    public void setDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void initViews() {
        dialogTitle_tv = (TextView) findViewById(R.id.dialogTitle_tv);
        dialogMsg_tv = (TextView) findViewById(R.id.dialogMsg_tv);
        dialogCancel_tv = (TextView) findViewById(R.id.dialogCancel_tv);
        dialogSure_tv = (TextView) findViewById(R.id.dialogSure_tv);
        if (!Util.isNull(dialogTitle))
            dialogTitle_tv.setText(dialogTitle);
        if (!Util.isNull(dialogMsg))
            dialogMsg_tv.setText(dialogMsg);
        if (!Util.isNull(dialogSure))
            dialogSure_tv.setText(dialogSure);
        if (!Util.isNull(dialogCancel) && "noShowCancelBtn".equals(dialogCancel)) {
            findViewById(R.id.dialogCancel_ll).setVisibility(View.GONE);
            findViewById(R.id.dialogDist_view).setVisibility(View.GONE);
        } else if (!Util.isNull(dialogCancel))
            dialogCancel_tv.setText(dialogCancel);
        dialogCancel_tv.setOnClickListener(this);
        dialogSure_tv.setOnClickListener(this);
    }

    public void setDialogTitleMsg(String dialogTitle, String dialogMsg) {
        this.dialogTitle = dialogTitle;
        this.dialogMsg = dialogMsg;
    }

    public void setBtnMsg(String dialogCancel, String dialogSure) {
        this.dialogCancel = dialogCancel;
        this.dialogSure = dialogSure;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogCancel_tv:
                if (clickListener != null)
                    clickListener.cancelLisen();
                break;
            case R.id.dialogSure_tv:
                if (clickListener != null)
                    clickListener.confirmLisen();
                break;
        }
    }

    public interface DialogClickListener {
        public void cancelLisen();

        public void confirmLisen();
    }
}
