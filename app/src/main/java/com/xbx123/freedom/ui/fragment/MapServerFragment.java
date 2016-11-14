package com.xbx123.freedom.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.LocationBean;
import com.xbx123.freedom.beans.MyServerInfoBean;
import com.xbx123.freedom.beans.ResortsBean;
import com.xbx123.freedom.beans.ServerBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.CreateOrderParse;
import com.xbx123.freedom.jsonparse.MainStateParse;
import com.xbx123.freedom.jsonparse.ServerParse;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.ui.activity.LoginActivity;
import com.xbx123.freedom.ui.activity.MainActivity;
import com.xbx123.freedom.ui.activity.NarratorCallActivity;
import com.xbx123.freedom.ui.activity.NarratorCalledActivity;
import com.xbx123.freedom.ui.activity.NativeCallActivity;
import com.xbx123.freedom.ui.activity.ReservationGuideActivity;
import com.xbx123.freedom.ui.activity.SelectResortsActivity;
import com.xbx123.freedom.utils.baiduroute.OverlayManager;
import com.xbx123.freedom.utils.baiduroute.WalkingRouteOverlay;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.db.DBManager;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.dialog.CountDownDialog;
import com.xbx123.freedom.view.dialog.PromptDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/5/24.
 */
public class MapServerFragment extends BasedFragment implements RadioGroup.OnCheckedChangeListener {
    public static MapServerFragment msFragment = null;
    private View msView = null;
    private final int callServerOverTime = 0;
    private final int nearServer = 410;
    private final int countServiceTime = 420;

    @Bind(R.id.fragServer_map)
    TextureMapView mapView;
    @Bind(R.id.onMapfuc_ll)
    LinearLayout onMapfuc_ll;//为进入订单所有的功能模块
    @Bind(R.id.mainServerInfo_rl)
    RelativeLayout mainServerInfo_rl;//我的服务者信息
    @Bind(R.id.mainLocate_txt)
    TextView mainLocate_txt;
    @Bind(R.id.serverHead_img)
    RoundedImageView serverHeadImg; //服务者信息
    @Bind(R.id.serverName_tv)
    TextView serverNameTv;
    @Bind(R.id.serverIdCard_tv)
    TextView serverIdCardTv;
    @Bind(R.id.serverInfo_rtb)
    RatingBar serverInfoRtb;
    @Bind(R.id.serverTime_tv)
    TextView serverTimeTv;
    @Bind(R.id.serversLocateState_tv)
    TextView serversLocateStateTv; //服务人员位置提醒、是否已到达
    @Bind(R.id.serviceUnitPrice_tv)
    TextView serviceUnitPriceTv;
    @Bind(R.id.serviceStartTime_tv)
    TextView serviceStartTimeTv;

    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private LatLng curtLatLng = null;
    private GeoCoder geoCoder = null;
    private UserInfo userInfo = null;
    private List<ResortsBean> resortsServerList = null;
    private List<ServerBean> nativeServerList = null;
    private List<ResortsBean> resortsList = null;
    private CountDownDialog countDialog = null;
    private MyServerInfoBean serverInfo = null;
    private Marker mMarkerServer = null;//服务于我的导游图标
    private OverlayManager routeOverlay = null;
    private RoutePlanSearch mSerarch = null;

    private boolean isFirstLoc = true; // 是否是第一次像用户定位
    private boolean isInOrder = false; // 是否处在订单中
    private boolean isFromMainActivity = false;//是否从MainActivity进入
    private boolean isPoiSearch = false;
    private long currentMills = 0;
    private int serverType = Constant.GuidesType;
    private String orderNum = "";
    private long serviceTimeDiffer = 0;

    public MapServerFragment() {
    }

    public static MapServerFragment newInstance() {
        if (msFragment == null) {
            msFragment = new MapServerFragment();
        }
        return msFragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case callServerOverTime://呼叫服务者时间过了
                    if (countDialog != null && countDialog.isShowing()) {
                        countDialog.dismiss();
                        handler.removeMessages(HttpRequestFlag.requestPageTwo);
                    }
                    break;
                case nearServer://请求接口获取周边服务者
                    if (isFirstLoc)
                        return;
                    if (isInOrder)
                        return;
                    if (Util.isNull(mainLocate_txt.getText().toString()))
                        return;
                    if (curtLatLng == null)
                        return;
                    api.getNearServer(curtLatLng.latitude + "," + curtLatLng.longitude + "," + mainLocate_txt.getText(), serverType + "");
                    break;
                case HttpRequestFlag.requestPageOne://周边服务者获取结果
                    mBaiduMap.clear();
                    String serverAllData = (String) msg.obj;
                    toDealNearServer(serverAllData);
                    break;
                case HttpRequestFlag.requestPageTwo://创建订单后呼叫为我服务的服务者
                    String myServerData = (String) msg.obj;
                    if (!Util.isNull(myServerData) && UtilParse.getRequestCode(myServerData) == 1) {
                        intoGoingOrder(orderNum, false);
                        handler.sendEmptyMessage(0);
                        DBManager.getInstance().deleteTableLatLng();
                        handler.removeMessages(HttpRequestFlag.requestPageTwo);
                    } else {
                        if ((System.currentTimeMillis() - currentMills) >= 2 * 1000) {
                            currentMills = System.currentTimeMillis();
                            api.findMyServer(orderNum);
                        } else {
                            if (countDialog != null && countDialog.isShowing())
                                handler.sendEmptyMessage(HttpRequestFlag.requestPageTwo);
                        }
                    }
                    break;
                case HttpRequestFlag.requestPageThree://服务于用户的的服务者信息
                    String serverInfoData = (String) msg.obj;
                    serverInfo = CreateOrderParse.getServerInfo(serverInfoData);
                    initMyServerInfo();
                    break;
                case HttpRequestFlag.requestPageFour://服务于用户的服务者的经纬度
                    if (!isInOrder)
                        return;
                    if (serverInfo == null)
                        return;
                    String latLngData = (String) msg.obj;
                    if (latLngData != null) {
                        serverInfo = CreateOrderParse.getServerLatLng(latLngData, serverInfo);
                        if (serverInfo != null) {
                            setMyServerPosition();
                            if (serverInfo.getStartTime() != 0) {
                                handler.removeMessages(HttpRequestFlag.requestPageFour);
                                return;
                            }
                        }
                    }
                    currentMills = System.currentTimeMillis();
                    LatLng latLng = SharePrefer.getLatlng(getActivity());
                    if (latLng == null)
                        return;
                    api.getServerLatLng(SharePrefer.getUserInfo(getActivity()).getUid(), latLng.latitude + "", latLng.longitude + "", orderNum);
                    break;
                case HttpRequestFlag.requestPageFive://开通的景区
                    String resortsData = (String) msg.obj;
                    List<ResortsBean> resortsList = MainStateParse.getResorts(resortsData);
                    if (resortsList == null)
                        return;
                    if (resortsList.size() == 0)
                        return;
                    Intent intent = new Intent(getActivity(), SelectResortsActivity.class);
                    intent.putExtra("ResortsList", (Serializable) resortsList);
                    intent.putExtra("isAllResorts", true);
                    startActivityForResult(intent, requestCodeTwo);
                    break;
                case countServiceTime:
                    //4500-是一小时60分钟+15分钟，乘以60
                    if (serviceTimeDiffer < 4500)
                        serviceUnitPriceTv.setText(serverInfo.getServerUnitPrice() + "");
                    else {
                        int serviceHour = (int) (serviceTimeDiffer / 3600);
                        long remainingMinute = serviceTimeDiffer % 3600;
                        if (remainingMinute <= Constant.billingPeriod * 60)
                            serviceUnitPriceTv.setText((serverInfo.getServerUnitPrice() * serviceHour) + "");
                        else
                            serviceUnitPriceTv.setText((serverInfo.getServerUnitPrice() * (serviceHour + 1)) + "");
                    }
                    serviceTimeDiffer++;
                    handler.sendEmptyMessageDelayed(countServiceTime, 1 * 1000);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        msView = inflater.inflate(R.layout.frag_mapserver, container, false);
        initDatas();
        ButterKnife.bind(this, msView);
        return msView;
    }

    private void initDatas() {
        api = new Api(getActivity(), handler);
        geoCoder = GeoCoder.newInstance();
        mSerarch = RoutePlanSearch.newInstance();
        mSerarch.setOnGetRoutePlanResultListener(listener);
        initViews();
    }

    private void initViews() {
        mapView = (TextureMapView) msView.findViewById(R.id.fragServer_map);
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false); //隐藏缩放控件
        mapView.showScaleControl(false);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);
        ((RadioButton) msView.findViewById(R.id.mainCheckZero_rb)).setChecked(true);
        LocationBean locationBean = SharePrefer.getLocate(getActivity());
        if (locationBean != null && !Util.isNull(locationBean.getLat())) {
            LatLng latLng = new LatLng(Double.parseDouble(locationBean.getLat()), Double.parseDouble(locationBean.getLon()));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(latLng).zoom(16f)
                    .build()));
            mBaiduMap.setMyLocationEnabled(true);//开启定位图层
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(latLng));
        }
        initViewLisener();
    }

    private void initViewLisener() {
        ((RadioGroup) msView.findViewById(R.id.mainCheck_rg)).setOnCheckedChangeListener(this);
        msView.findViewById(R.id.mainCall_btn).setOnClickListener(this);
        mLocClient.registerLocationListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    /**
     * 获取周围服务者的处理
     */
    private void toDealNearServer(String serverAllData) {
        if (UtilParse.getRequestCode(serverAllData) == 0) {
            resortsServerList = null;
            nativeServerList = null;
            if (serverType == Constant.NarratorType)
                mainLocate_txt.setText(getString(R.string.mainNoServer));
            else if (serverType == Constant.NativeType)
                mainLocate_txt.setText(getString(R.string.mainNoServerNative));
            else
                mainLocate_txt.setText(getString(R.string.mainNoGuide));
            return;
        }
        switch (serverType) {
            case Constant.GuidesType:
                mainLocate_txt.setText(getString(R.string.mainNoGuide));
                break;
            case Constant.NarratorType://讲解员
                resortsServerList = ServerParse.getNarratorServer(UtilParse.getRequestData(serverAllData));
                if (resortsServerList != null) {
                    if (resortsServerList.size() == 0) {
                        mainLocate_txt.setText(getString(R.string.mainNoServer));
                        return;
                    }
                    List<ServerBean> serverListTemp = new ArrayList<>();
                    for (int i = 0; i < resortsServerList.size(); i++) {
                        if (resortsServerList.get(i) != null && resortsServerList.get(i).getResortsServerList() != null)
                            serverListTemp.addAll(resortsServerList.get(i).getResortsServerList());
                    }
                    addServerOverly(serverListTemp);
                }
                break;
            case Constant.NativeType://领路人
                nativeServerList = ServerParse.getNativeServer(UtilParse.getRequestData(serverAllData));
                if (nativeServerList == null) {
                    mainLocate_txt.setText(getString(R.string.mainNoServerNative));
                    return;
                }
                if (nativeServerList.size() == 0) {
                    mainLocate_txt.setText(getString(R.string.mainNoServerNative));
                    return;
                }
                addServerOverly(nativeServerList);
                break;
        }
    }

    /**
     * 用户找到服务者进入服务订单
     *
     * @param goingOrderNum
     * @param isFromMain
     */
    public void intoGoingOrder(String goingOrderNum, boolean isFromMain) {
        isInOrder = true;
        this.isFromMainActivity = isFromMain;
        onMapfuc_ll.setVisibility(View.GONE);
        mainServerInfo_rl.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).userInOrder(goingOrderNum);
        orderNum = goingOrderNum;
        mBaiduMap.clear();
        api.getMyServerInfo(orderNum);
    }

    /**
     * 初始化服务者信息
     */
    private void initMyServerInfo() {
        if (serverInfo == null)
            return;
        mBaiduMap.clear();
        imageLoader.displayImage(serverInfo.getServerHeadImg(), serverHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        serverNameTv.setText(serverInfo.getServerName());
        if (!Util.isNull(serverInfo.getServerIdcard())) {
            String idCardStr = serverInfo.getServerIdcard().substring(6, 14);
            serverIdCardTv.setText(serverInfo.getServerIdcard().replace(idCardStr, "********"));
        }
        if (!Util.isNull(serverInfo.getServerStarts())) {
            serverInfoRtb.setRating(Float.valueOf(serverInfo.getServerStarts()) / 2);
            serverTimeTv.setText(serverInfo.getServerStarts() + getString(R.string.appScore));
        }
        if (serverInfo.getStartTime() == 0) {
            setTowPointInScreen();
            handler.sendEmptyMessage(HttpRequestFlag.requestPageFour);
            final MarkerOptions ooA = new MarkerOptions().position(new LatLng(serverInfo.getServerLat(), serverInfo.getServerLon())).icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.guide_locate));
            mMarkerServer = (Marker) mBaiduMap.addOverlay(ooA);
            mapView.invalidate();
            msView.findViewById(R.id.callSuccess_tv).setVisibility(View.VISIBLE);
            if (serverInfo.getIsArriveNearby() == 1)
                serversLocateStateTv.setText(getString(R.string.mainCallArrive));
            else if (serverInfo.getIsArriveNearby() == 0)
                serversLocateStateTv.setText(getString(R.string.mainCallInRoad));
            LocationBean locationBean = SharePrefer.getLocate(getActivity());
            LatLng userLatLng = new LatLng(Double.parseDouble(locationBean.getLat()), Double.parseDouble(locationBean.getLon()));
            routePlan(new LatLng(serverInfo.getServerLat(), serverInfo.getServerLon()), userLatLng);
        } else {
            startServiceUser();
            if (!isFromMainActivity) {
                startServiceTip();
            }
        }
    }

    /**
     * 将用户和导游的中心点放入屏幕正中心
     */
    private void setTowPointInScreen() {
        LatLng latLng = SharePrefer.getLatlng(getActivity());
        if (latLng == null)
            return;
        LatLng centerLatLng = new LatLng((serverInfo.getServerLat() + latLng.latitude) / 2, (serverInfo.getServerLon() + latLng.longitude) / 2);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(centerLatLng).zoom(17)
                .build()), 1000);
    }

    /**
     * 刷新服务者的经纬度和运动轨迹
     */
    public void setMyServerPosition() {
        if (serverInfo.getStartTime() == 0) {
            LatLng cLatLng = new LatLng(serverInfo.getServerLat(), serverInfo.getServerLon());
            if (mMarkerServer != null) {
                Util.pLog("" + serverInfo.getServerLat() + "," + serverInfo.getServerLon());
                mMarkerServer.setPosition(cLatLng);
            } else {
                final MarkerOptions ooA = new MarkerOptions().position(new LatLng(serverInfo.getServerLat(), serverInfo.getServerLon())).icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.guide_locate));
                mMarkerServer = (Marker) mBaiduMap.addOverlay(ooA);
                mapView.invalidate();
            }
            LocationBean locationBean = SharePrefer.getLocate(getActivity());
            LatLng userLatLng = new LatLng(Double.parseDouble(locationBean.getLat()), Double.parseDouble(locationBean.getLon()));
            routePlan(cLatLng, userLatLng);
            if (serverInfo.getIsArriveNearby() == 1)
                serversLocateStateTv.setText(getString(R.string.mainCallArrive));
            else if (serverInfo.getIsArriveNearby() == 0)
                serversLocateStateTv.setText(getString(R.string.mainCallInRoad));
        } else {
            startServiceUser();
            startServiceTip();
        }
    }

    /**
     * 开始服务或者已经处于服务中的状态
     */
    private void startServiceUser() {
        mBaiduMap.clear();
        ((MainActivity) getActivity()).serverStartService();
        handler.removeMessages(HttpRequestFlag.requestPageFour);
        msView.findViewById(R.id.callSuccess_tv).setVisibility(View.GONE);
        msView.findViewById(R.id.itemPriceAssess_ll).setVisibility(View.VISIBLE);
        serversLocateStateTv.setText(getString(R.string.mainServiceForUser));
        serviceUnitPriceTv.setText(serverInfo.getServerUnitPrice() + "");
        serviceStartTimeTv.setText(Util.timeToStr(serverInfo.getStartTime() * 1000));
        if (serverInfo.getStartTime() == 0)
            serviceTimeDiffer = 0;
        else
            serviceTimeDiffer = serverInfo.getServerTimeDiffer();
        if (serverInfo.getServiceType() == 1)
            handler.sendEmptyMessage(countServiceTime);
        else
            serviceUnitPriceTv.setText(serverInfo.getServerUnitPrice() + "");
    }

    /**
     * 开始服务提醒
     */
    private void startServiceTip() {
        final PromptDialog promptDialog = new PromptDialog(getActivity());
        promptDialog.setDialogTitleMsg(getString(R.string.mainStartService), getString(R.string.mainStartServiceHint));
        promptDialog.setBtnMsg("noShowCancelBtn", getString(R.string.mainSure));
        promptDialog.show();
        promptDialog.setDialogClickListener(new PromptDialog.DialogClickListener() {
            @Override
            public void cancelLisen() {
            }

            @Override
            public void confirmLisen() {
                promptDialog.dismiss();
            }
        });
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rGeoCodeResult) {
        if (rGeoCodeResult == null)
            return;
        if (rGeoCodeResult.getPoiList() == null)
            return;
        if (rGeoCodeResult.getPoiList().size() == 0)
            return;
        if (!Util.isNull(rGeoCodeResult.getPoiList().get(0).name) && mainLocate_txt != null)
            mainLocate_txt.setText(rGeoCodeResult.getPoiList().get(0).name);
        curtLatLng = rGeoCodeResult.getLocation();
        handler.sendEmptyMessage(nearServer);
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (isPoiSearch) {
            isPoiSearch = false;
            return;
        }
        if (curtLatLng != null && mapStatus.target != null) {
            double distanced = Util.getDistance(curtLatLng.longitude, curtLatLng.latitude, mapStatus.target.longitude, mapStatus.target.latitude);
            if (distanced < 40.0)
                return;
        }
        mainLocate_txt.setText("");
        mainLocate_txt.setHint(getString(R.string.mainLoadHint));
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (curtLatLng != null && mapStatus.target != null) {
            double distanced = Util.getDistance(curtLatLng.longitude, curtLatLng.latitude, mapStatus.target.longitude, mapStatus.target.latitude);
            if (distanced < 40.0)
                return;
        }
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        super.onReceiveLocation(bdLocation);
        if (bdLocation == null) {
            return;
        }
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc && !Util.isNull(bdLocation.getAddrStr())) {
            isFirstLoc = false;
            curtLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(curtLatLng).zoom(16f)
                    .build()));
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bdMyLocate, accuracyCircleFillColor, accuracyCircleStrokeColor));
            mBaiduMap.setMyLocationEnabled(true);//开启定位图层
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(curtLatLng));
        }
    }

    /**
     * 添加服务者覆盖物在地图上
     *
     * @param serverList
     */
    private void addServerOverly(List<ServerBean> serverList) {
        if (getActivity() == null)
            return;
        if (isInOrder)
            return;
        if (serverList == null)
            return;
        if (serverList.size() == 0)
            return;
        mBaiduMap.clear();
        float mapScaling = StringUtil.getMapScaling2(StringUtil.calculateMapScaling2(curtLatLng, serverList));
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(curtLatLng).zoom(mapScaling)
                .build()), 1000);
        for (int i = 0; i < serverList.size(); i++) {
            final View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.overly_inmap, null);
            final ServerBean serverBean = serverList.get(i);
            final RoundedImageView markerImg = (RoundedImageView) markerView.findViewById(R.id.pinHeaded_img);
            imageLoader.displayImage(serverBean.getServerHeadImg(), markerImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bdServerLocate = BitmapDescriptorFactory.fromBitmap(Util.getBitmapFromView(markerView));
                    final MarkerOptions mos = new MarkerOptions().position(new LatLng(serverBean.getServerLat(), serverBean.getServerLog())).icon(bdServerLocate)
                            .zIndex(9).draggable(false);
                    if (mapView == null)
                        return;
                    if (mBaiduMap == null)
                        return;
                    if (isInOrder)
                        return;
                    mBaiduMap.addOverlay(mos);
                    mapView.invalidate();
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mBaiduMap.clear();
        switch (checkedId) {
            case R.id.mainCheckZero_rb://私人导游
                msView.findViewById(R.id.guideCall_rl).setVisibility(View.VISIBLE);
                msView.findViewById(R.id.narratorCall_rl).setVisibility(View.GONE);
                serverType = Constant.GuidesType;
                break;
            case R.id.mainCheckOne_rb://景区讲解
                msView.findViewById(R.id.guideCall_rl).setVisibility(View.GONE);
                msView.findViewById(R.id.narratorCall_rl).setVisibility(View.VISIBLE);
                serverType = Constant.NarratorType;
                break;
            case R.id.mainCheckTwo_rb://全民向导
                msView.findViewById(R.id.guideCall_rl).setVisibility(View.GONE);
                msView.findViewById(R.id.narratorCall_rl).setVisibility(View.VISIBLE);
                serverType = Constant.NativeType;
                break;
        }
        mainLocate_txt.setText("");
        mainLocate_txt.setHint(getString(R.string.mainLoadHint));
        if (curtLatLng != null)
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(curtLatLng));
//        handler.sendEmptyMessage(nearServer);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        initParams();
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
        isFirstLoc = true;
        serverType = Constant.GuidesType;
        mapView.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 重置参数
     */
    private void initParams() {
        orderNum = "";
        isInOrder = false;
        isPoiSearch = false;
        isFromMainActivity = false;
        handler.removeMessages(HttpRequestFlag.requestPageFour);
        handler.removeMessages(countServiceTime);
    }

    /**
     * 取消订单或者定完完成的数据重置
     */
    public void cancelOrderReset() {
        initParams();
        onMapfuc_ll.setVisibility(View.VISIBLE);
        mainServerInfo_rl.setVisibility(View.GONE);
        msView.findViewById(R.id.itemPriceAssess_ll).setVisibility(View.GONE);
        handler.sendEmptyMessage(nearServer);
        handler.removeMessages(countServiceTime);
        if (mBaiduMap != null)
            mBaiduMap.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case requestCodeOne:
                orderNum = data.getStringExtra("createOrderNum");
                countDialog = new CountDownDialog(getActivity());
                countDialog.setDownCount(handler);
                countDialog.setOrderNum(orderNum);
                countDialog.show();
                handler.sendEmptyMessage(HttpRequestFlag.requestPageTwo);
                break;
            case requestCodeTwo://顶部地址选择后进入
                ResortsBean resortsBean = (ResortsBean) data.getSerializableExtra("ResortsBean");
                if (resortsBean == null)
                    return;
                isPoiSearch = true;
                curtLatLng = new LatLng(resortsBean.getLat(), resortsBean.getLon());
                handler.sendEmptyMessage(nearServer);
                mainLocate_txt.setText(resortsBean.getResortsName());
                MapStatus mMapstatus = new MapStatus.Builder().target(curtLatLng).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapstatus));
                break;
            case requestCodeThree:
                ResortsBean resortsBean1 = (ResortsBean) data.getSerializableExtra("ResortsBean");
                if (resortsBean1 == null)
                    return;
                for (ResortsBean resortsB : resortsServerList) {
                    if (resortsB.getResortsId().equals(resortsBean1.getResortsId())) {
                        String currentLocate = curtLatLng.latitude + "," + curtLatLng.longitude + "," + mainLocate_txt.getText();
//                        Intent intent = new Intent(getActivity(), NarratorCallActivity.class);
                        Intent intent = new Intent(getActivity(), NarratorCalledActivity.class);
                        intent.putExtra("MapResortsBean", resortsB);
                        intent.putExtra("MapCurrentLocate", currentLocate);
                        startActivityForResult(intent, requestCodeOne);
                    }
                }
                break;
        }
    }

    @OnClick({/*R.id.mainChoiceLocate_rl,*/ R.id.mainCall_btn, R.id.mainSelfLocate_img, R.id.serverPhone_img, R.id.priceAssess_ll, R.id.item_priceTip_rl, R.id.mainAppointGuide_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mainSelfLocate_img:
                LatLng latLng = SharePrefer.getLatlng(getActivity());
                if (latLng != null) {
                    MapStatus mMapstatus = new MapStatus.Builder().target(latLng).build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                    mBaiduMap.animateMapStatus(u, 1000);
                }
                break;
            /*case R.id.mainChoiceLocate_rl:
                api.getResortsList();
                break;*/
            case R.id.mainCall_btn:
                if (curtLatLng == null)
                    return;
                String currentLocate = curtLatLng.latitude + "," + curtLatLng.longitude + "," + mainLocate_txt.getText();
                if (!checkLogin())
                    return;
                if (Util.isNull(mainLocate_txt.getText().toString())) {
                    Util.showToast(getActivity(), getString(R.string.mainInputLocate));
                    return;
                }
                switch (serverType) {
                    case Constant.NarratorType:
                        if (resortsServerList == null) {
                            Util.showToast(getActivity(), getString(R.string.mainNoServer));
                            return;
                        }
                        if (resortsServerList.size() == 0) {
                            Util.showToast(getActivity(), getString(R.string.mainNoServer));
                            return;
                        }
                        if (resortsServerList.size() == 1) {//只有一个景区点,直接传ResortsBean
                            if (resortsServerList.get(0).getResortsServerList() != null) {
                                intent.setClass(getActivity(), NarratorCalledActivity.class);
                                intent.putExtra("MapResortsBean", resortsServerList.get(0));
                                intent.putExtra("MapCurrentLocate", currentLocate);
                                startActivityForResult(intent, requestCodeOne);
                            }
                        } else {//多个景区点,直接传选中位置ResortsBean
                            intent.setClass(getActivity(), SelectResortsActivity.class);
                            intent.putExtra("ResortsList", (Serializable) resortsServerList);
                            intent.putExtra("isAllResorts", false);
                            startActivityForResult(intent, requestCodeThree);
                        }
                        break;
                    case Constant.NativeType:
                        if (nativeServerList == null) {
                            Util.showToast(getActivity(), getString(R.string.mainNoServerNative));
                            return;
                        }
                        if (nativeServerList.size() == 0) {
                            Util.showToast(getActivity(), getString(R.string.mainNoServerNative));
                            return;
                        }
                        intent.putExtra("NativeServicePrice", nativeServerList.get(0).getServerPrice());
                        intent.putExtra("MapCurrentLocate", currentLocate);
                        intent.setClass(getActivity(), NativeCallActivity.class);
                        startActivityForResult(intent, requestCodeOne);
                        break;
                }
                break;
            case R.id.serverPhone_img:
                if (serverInfo == null)
                    return;
                String serverPhone = serverInfo.getServerPhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + serverPhone);
                intentCall.setData(data);
                startActivity(intentCall);
                break;
            case R.id.priceAssess_ll:
                if (serverInfo.getServiceType() == 2)
                    return;
                msView.findViewById(R.id.item_priceTip_rl).setVisibility(View.VISIBLE);
                ((ImageView) msView.findViewById(R.id.priceTim_img)).setImageResource(R.mipmap.icon_up_small);
                break;
            case R.id.item_priceTip_rl:
                msView.findViewById(R.id.item_priceTip_rl).setVisibility(View.GONE);
                ((ImageView) msView.findViewById(R.id.priceTim_img)).setImageResource(R.mipmap.icon_down_small);
                break;
            case R.id.mainAppointGuide_btn:
                if (!checkLogin())
                    return;
                startActivity(new Intent(getActivity(), ReservationGuideActivity.class));
                break;
        }
    }

    /**
     * 检查是否登录
     */
    private boolean checkLogin() {
        userInfo = SharePrefer.getUserInfo(getActivity());
        Intent intent = new Intent();
        if (userInfo == null) {
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            return false;
        }
        if (Util.isNull(userInfo.getUid())) {
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    /**
     * 路径规划
     *
     * @param startLatLng
     * @param endLatLng
     */
    private void routePlan(LatLng startLatLng, LatLng endLatLng) {
        PlanNode stNode = PlanNode.withLocation(startLatLng);
        PlanNode endNode = PlanNode.withLocation(endLatLng);
        mSerarch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(endNode));
    }

    private OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //获取步行线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                if (routeOverlay != null)
                    routeOverlay.removeFromMap();
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            //获取公交换乘路径规划结果
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            //获取驾车线路规划结果
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            //获取骑行线路规划结果
        }
    };

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }
}
