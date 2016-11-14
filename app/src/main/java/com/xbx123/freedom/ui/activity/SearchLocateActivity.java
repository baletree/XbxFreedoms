package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.SearchResultAdapter;
import com.xbx123.freedom.beans.LocationBean;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/3.
 * poi检索地址
 */
public class SearchLocateActivity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetGeoCoderResultListener, SearchResultAdapter.OnRecyItemClickListener {
    @Bind(R.id.searchInput_et)
    EditText searchInputEt;
    @Bind(R.id.searchLocate_rv)
    RecyclerView searchLocateRv;
    @Bind(R.id.cityChoice_tv)
    TextView cityChoiceTv;

    private PoiSearch mPoiSearch = null;
    private GeoCoder geoCoder = null;
    private SearchResultAdapter searchResultAdapter = null;
    private List<PoiInfo> poiList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_locate);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        searchLocateRv.setLayoutManager(new LinearLayoutManager(this));
        searchLocateRv.setItemAnimator(new DefaultItemAnimator());
        searchLocateRv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        LocationBean lBean = SharePrefer.getLocate(this);
        if (lBean != null) {
            if (!Util.isNull(lBean.getCity()) && lBean.getCity().contains(getString(R.string.appCity)))
                cityChoiceTv.setText(lBean.getCity().replace(getString(R.string.appCity), ""));
            else
                cityChoiceTv.setText(lBean.getCity());
        }
        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    return;
                String city = cityChoiceTv.getText().toString();
                PoiCitySearchOption poiCityOption = new PoiCitySearchOption();
                poiCityOption.city(city);
                poiCityOption.keyword(s.toString());
                poiCityOption.pageCapacity(50);
                mPoiSearch.searchInCity(poiCityOption);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.searchBack_rl, R.id.searchCancel_tv, R.id.cityChoice_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchBack_rl:
                finish();
                break;
            case R.id.searchCancel_tv:
                searchInputEt.setText("");
                if(poiList != null){
                    poiList.removeAll(poiList);
                    searchLocateRv.setAdapter(null);
                }
                break;
            case R.id.cityChoice_ll:

                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        poiList = poiResult.getAllPoi();
        if (poiList == null)
            return;
        searchResultAdapter = new SearchResultAdapter(this, poiList);
        searchLocateRv.setAdapter(searchResultAdapter);
        searchResultAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onItemClick(View v, int position) {
        if (poiList == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("outsetResult",poiList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
