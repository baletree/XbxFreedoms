package com.xbx123.freedom.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx123.freedom.R;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.utils.sp.SharePrefer;

/**
 * Created by EricYuan on 2016/4/13.
 */
public class BasedFragment extends Fragment implements View.OnClickListener, BDLocationListener, BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback {
    public static final int accuracyCircleFillColor = 0x1E1AA692;
    public static final int accuracyCircleStrokeColor = 0x00000000;
    public final int requestCodeOne = 750;
    public final int requestCodeTwo = 860;
    public final int requestCodeThree = 970;

    public Api api = null;
    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;
    public MyLocationData locData = null;
    public BitmapDescriptor bdMyLocate = null;//自己的图标
    public BitmapDescriptor bdServerLocate = null;//导游的图标
    public LocationClient mLocClient;
    public LocationClientOption option = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initBDinfo();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initBDinfo() {
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();

        mLocClient = new LocationClient(getActivity());
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        bdMyLocate = BitmapDescriptorFactory
                .fromResource(R.mipmap.my_locate);
        bdServerLocate = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_locate);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        SharePrefer.saveLatlng(getActivity(), bdLocation.getLongitude() + "", bdLocation.getLatitude() + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rGeoCodeResult) {
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }
    /**
     * 行程开始的提示
     */
    public void notifyUserStartStroke(){

    }
}
