package com.xbx123.freedom.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Callback;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.MainViewPagerAdapter;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.beans.UserStateBean;
import com.xbx123.freedom.beans.VersionBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.MainStateParse;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.ui.fragment.MapServerFragment;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.constant.UrlConstant;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.StatusBarUtil;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.dialog.PromptDialog;
import com.xbx123.freedom.view.views.BanSlideViewpager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PromptDialog.DialogClickListener {
    @Bind(R.id.mainFrag_vp)
    BanSlideViewpager mainFragVp;
    @Bind(R.id.menu_head_img)
    RoundedImageView menuHeadImg;
    @Bind(R.id.menu_name_tv)
    TextView menuNameTv;
    @Bind(R.id.menu_phone_tv)
    TextView menuPhoneTv;
    @Bind(R.id.mainMenu_drawl)
    DrawerLayout mainMenuDrawl;
    @Bind(R.id.mainMenu_img)
    ImageView mainMenu_img;
    @Bind(R.id.mainBack_img)
    ImageView mainBack_img;
    @Bind(R.id.mainTitle_tv)
    TextView mainTitleTv;
    @Bind(R.id.mainMore_tv)
    TextView mainMoreTv;

    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;
    private LocalReceiver localReceiver = null;
    private IntentFilter intentFilter = null;
    private LocalBroadcastManager lBManager = null;
    private MapServerFragment msFragment = null;
    private MainViewPagerAdapter mViewpAdapter = null;
    private UserInfo userInfo = null;
    private Api api = null;
    private PromptDialog promptDialog = null;
    private PopupWindow popupWindow = null;

    private boolean isExit = false;
    private String dealOrderNum = "";
    private int dialogType = 0;
    private final int requestCodeOne = 540;
    private final int requestCodeTwo = 650;

    private IWXAPI wxApi = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case HttpRequestFlag.requestPageOne://验证登录状态及检查是否有待处理的订单
                    String checkData = (String) msg.obj;
                    switch (UtilParse.getRequestCode(checkData)) {
                        case 0://登录失效
                            Util.showToast(MainActivity.this, getString(R.string.mainTokenLose));
                            SharePrefer.saveUserInfo(MainActivity.this, new UserInfo());
                            /*intent.setClass(MainActivity.this, LoginActivity.class);
                            intent.putExtra("isTokenLose", true);
                            startActivity(intent);
                            finish();*/
                            break;
                        case 1://验证通过，重置Token
                            MainStateParse.resetToken(MainActivity.this, UtilParse.getRequestData(checkData));
                            String unDealDataType = MainStateParse.checkDataType(UtilParse.getRequestData(checkData));
                            if (Util.isNull(unDealDataType))
                                return;
                            toDealState(UtilParse.getRequestData(checkData), unDealDataType);
                            break;
                    }
                    break;
                case HttpRequestFlag.requestPageTwo://用户成功取消订单
                    String cancelData = (String) msg.obj;
                    if (UtilParse.getRequestCode(cancelData) == 0)
                        return;
                    int isPay = MainStateParse.getCancelIsPay(UtilParse.getRequestData(cancelData));
                    if (isPay == -1)
                        return;
                    intent.setClass(MainActivity.this, OrderCancelTipActivity.class);
                    intent.putExtra("isPay", isPay);
                    intent.putExtra("orderNum", dealOrderNum);
                    startActivityForResult(intent, requestCodeOne);
//                    resetViewState();
                    /*if (UtilParse.getRequestCode(cancelData) == 2) {//支付违约金
                        intent.setClass(MainActivity.this, OrderPayActivity.class);
                        intent.putExtra("PayOrderNum", unDealOrderNum);
                        startActivity(intent);
                    }*/
                    break;
                case HttpRequestFlag.requestPageThree:
                    String versionMsg = (String) msg.obj;
                    VersionBean versionBean = MainStateParse.getVersionInfo(versionMsg);
                    if(versionBean != null){
                        if(versionBean.getVersionCode() > Util.getAppVersionCode(MainActivity.this)){
                            updateVerDialog(versionBean);
                        }
                    }
                    break;
                case HttpRequestFlag.requestPageFour:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        wxApi = WXAPIFactory.createWXAPI(MainActivity.this, Constant.WXKey, false);
        api = new Api(this, handler);
        api.checkUpdateVersion();
        mainMenuDrawl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.actionLoginSuc);
        intentFilter.addAction(Constant.actionOverOrder);
        intentFilter.addAction(Constant.actionCancelOrderSuc);
        intentFilter.addAction(Constant.actionExitApp);
        localReceiver = new LocalReceiver();
        lBManager = LocalBroadcastManager.getInstance(this);
        lBManager.registerReceiver(localReceiver, intentFilter);
        mainMenuDrawl.setScrimColor(0xbe000000);// 设置半透明度
        ((TextView) findViewById(R.id.versionTxt)).setText("v" + Util.getAppVersionName(this));
        StatusBarUtil.setColorForDrawerLayout(this, mainMenuDrawl, Color.parseColor("#000000"), StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
//        StatusBarUtil.setColor(this,Color.parseColor("#EF000000"));
        findViewById(R.id.mainActive_img).setVisibility(View.GONE);
        initFragControl();
        initUserInfo();
    }

    private void updateVerDialog(final VersionBean versionBean) {
        Intent intent = new Intent(this, UpdateVerActivity.class);
        intent.putExtra("UpdateVersion", versionBean);
        startActivity(intent);
    }

    private void initFragControl() {
        mViewpAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);
        msFragment = MapServerFragment.newInstance();
        mViewpAdapter.addFragment(msFragment, "");
        mainFragVp.setAdapter(mViewpAdapter);
    }

    private void initUserInfo() {
        userInfo = SharePrefer.getUserInfo(this);
        if (userInfo == null)
            return;
        imageLoader.displayImage(userInfo.getUserHead(), menuHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        if (Util.isNull(userInfo.getNickName()))
            menuNameTv.setVisibility(View.GONE);
        else {
            menuNameTv.setVisibility(View.VISIBLE);
            menuNameTv.setText(userInfo.getNickName());
        }
        if (!Util.isNull(userInfo.getUserPhone())) {
            String num = userInfo.getUserPhone();
            String result = num.substring(0, 3) + "****" + num.substring(7, num.length());
            menuPhoneTv.setText(result);
        }
        if (Util.isNull(userInfo.getUserPhone()))
            return;
        String jPushId = SharePrefer.getJpushId(MainActivity.this);
        if (!Util.isNull(jPushId))
            api.checkHasNoHandlerOrder(userInfo.getUserPhone(), userInfo.getLoginToken(), jPushId);
        else {
            jPushId = JPushInterface.getRegistrationID(this);
            SharePrefer.saveJPushId(this, jPushId);
            api.checkHasNoHandlerOrder(userInfo.getUserPhone(), userInfo.getLoginToken(), jPushId);
        }
        Util.pLog("pushId:" + jPushId);
    }

    /**
     * 用户成功呼叫到服务者
     */
    public void userInOrder(String orderNum) {
        dealOrderNum = orderNum;
        mainMenu_img.setVisibility(View.GONE);
        mainMoreTv.setVisibility(View.VISIBLE);
        mainTitleTv.setText(getString(R.string.mainWaitServer));
    }

    /**
     * 服务者开始服务
     */
    public void serverStartService() {
        mainTitleTv.setText(getString(R.string.mainStartServer));
        mainMoreTv.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mainMenuDrawl.isDrawerOpen(Gravity.LEFT)) {
            mainMenuDrawl.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 改变左侧边栏打开状态
     */
    public void toggleLeftLayout() {
        if (mainMenuDrawl.isDrawerOpen(Gravity.LEFT)) {
            mainMenuDrawl.closeDrawer(Gravity.LEFT);
        } else {
            mainMenuDrawl.openDrawer(Gravity.LEFT);
        }
    }

    /**
     * 取消订单或者行程结束状态初始化
     */
    private void resetViewState() {
        mainMoreTv.setVisibility(View.GONE);
        mainBack_img.setVisibility(View.GONE);
        mainMenu_img.setVisibility(View.VISIBLE);
        mainTitleTv.setText(getString(R.string.mainTitle));
        msFragment.cancelOrderReset();
        dealOrderNum = "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case requestCodeOne:
                userInfo = SharePrefer.getUserInfo(this);
                if (data.getBooleanExtra("isUpdateName", true)) {
                    if (Util.isNull(userInfo.getNickName()))
                        menuNameTv.setVisibility(View.GONE);
                    else {
                        menuNameTv.setVisibility(View.VISIBLE);
                        menuNameTv.setText(userInfo.getNickName());
                    }
                }
                if (data.getBooleanExtra("isUpdateHead", true))
                    imageLoader.displayImage(userInfo.getUserHead(), menuHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
                break;
            case requestCodeTwo:
                dealOrderNum = data.getStringExtra("InOrderNum");
                if (!Util.isNull(dealOrderNum)) {
                    mainMenu_img.setVisibility(View.GONE);
                    mainBack_img.setVisibility(View.VISIBLE);
                    msFragment.intoGoingOrder(dealOrderNum, true);
                }
                break;
        }
    }

    @OnClick({R.id.mainMenu_img, R.id.mainActive_img, R.id.menuHead_rl, R.id.menuUserInfo_rl, R.id.menuOrder_ll, R.id.menuMsg_ll, R.id.menuFeedBack_ll,
            R.id.menuSetting_ll, R.id.mainMore_tv, R.id.mainBack_img})
    public void onClick(View view) {
        userInfo = null;
        userInfo = SharePrefer.getUserInfo(this);
        if (userInfo == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (Util.isNull(userInfo.getUid())) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.mainMenu_img:
                toggleLeftLayout();
                break;
            case R.id.mainBack_img:
                resetViewState();
                Intent intent = new Intent(this, OrderListActivity.class);
                intent.putExtra("isBackOrderList", true);
                startActivity(intent);
                break;
            case R.id.mainActive_img:

                break;
            case R.id.menuUserInfo_rl:
                startActivityForResult(new Intent(this, UserInfoActivity.class), requestCodeOne);
                toggleLeftLayout();
                break;
            case R.id.menuOrder_ll:
                startActivityForResult(new Intent(this, OrderListActivity.class), requestCodeTwo);
                toggleLeftLayout();
                break;
            case R.id.menuMsg_ll:
                startActivity(new Intent(this, MessageCenterActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menuFeedBack_ll:
                startActivity(new Intent(this, FeedBackActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menuSetting_ll:
                startActivity(new Intent(this, SettingActivity.class));
                toggleLeftLayout();
                break;
            case R.id.mainMore_tv://取消订单
                if (popupWindow != null && popupWindow.isShowing())
                    return;
                showPopupMore();
                break;
        }
    }

    /**
     * 更多
     */
    private void showPopupMore() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_main_more, null);
        popupWindow = new PopupWindow(contentView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(findViewById(R.id.mainMore_tv));
        contentView.findViewById(R.id.popOutside_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.popupCancel_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                api.cancelOrder(dealOrderNum, "");
            }
        });
        contentView.findViewById(R.id.popupService_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "4008469989");
                intentCall.setData(data);
                startActivity(intentCall);
            }
        });
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.actionLoginSuc.equals(action)) {
                initUserInfo();
            } else if (Constant.actionOverOrder.equals(action) || Constant.actionCancelOrderSuc.equals(action)) {
                resetViewState();
            } else if ((Constant.actionExitApp.equals(action))) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localReceiver != null)
            lBManager.unregisterReceiver(localReceiver);
    }

    /**
     * 处理未处理的订单
     *
     * @param dataJson
     * @param unDealDataType
     */
    private void toDealState(String dataJson, String unDealDataType) {
        UserStateBean stateBean = MainStateParse.checkUserState(dataJson, unDealDataType);
        if (stateBean == null)
            return;
        if (Util.isNull(stateBean.getOrderNum()))
            return;
        dealOrderNum = stateBean.getOrderNum();
        if (unDealDataType.equals("unpay")) {
            dialogType = 1;
        } else if (unDealDataType.equals("uncomment")) {
            dialogType = 2;
        } else if (unDealDataType.equals("going")) {
            dialogType = 3;
        }
        showUnDealDialog();
    }

    /**
     * 显示未处理的对话框提醒
     */
    private void showUnDealDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setDialogClickListener(this);
        switch (dialogType) {
            case 1:
                promptDialog.setDialogTitleMsg(getString(R.string.dUnpayTitle), getString(R.string.dUnpayMsg));
                break;
            case 2:
                promptDialog.setDialogTitleMsg(getString(R.string.dCommentTitle), getString(R.string.dCommentMsg));
                break;
            case 3:
                promptDialog.setDialogTitleMsg(getString(R.string.dGoingOrderTitle), getString(R.string.dGoingOrderMsg));
                break;
        }
        promptDialog.show();
    }

    @Override
    public void cancelLisen() {
        if (promptDialog != null && promptDialog.isShowing())
            promptDialog.dismiss();
    }

    @Override
    public void confirmLisen() {
        promptDialog.dismiss();
        Intent intent = new Intent();
        switch (dialogType) {
            case 1:
                intent.setClass(this, OrderPayActivity.class);
                intent.putExtra("PayOrderNum", dealOrderNum);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(this, OrderDetailActivity.class);
                intent.putExtra("DetailOrderNum", dealOrderNum);
                startActivity(intent);
                break;
            case 3:
                msFragment.intoGoingOrder(dealOrderNum, true);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 再按一次退出app
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Util.showToast(MainActivity.this, getString(R.string.mainExitApp));
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            this.finish();
        }
    }
}
