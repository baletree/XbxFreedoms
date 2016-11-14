package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.GuideAdapter;
import com.xbx123.freedom.adapter.SortGuideItemAdapter;
import com.xbx123.freedom.beans.GuideReBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.GuideParse;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.MultiLineRadioGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/4.
 */
public class GuideListActivity extends BaseActivity implements ListView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, PullToRefreshBase.OnRefreshListener2<ListView>, SortGuideItemAdapter.ItemClickListener {
    @Bind(R.id.guideList_plv)
    PullToRefreshListView guideList_plv;
    @Bind(R.id.sortGuideItem_rv)
    RecyclerView sortGuideItem_rv;

    private GuideAdapter guideAdapter = null;
    private List<GuideReBean> guideBeanList = null;
    private Map<String, String> sortConditionMap = null;
    private Api api = null;
    private GuideReBean guideReBean = null;
    private String sortParams = "";
    private boolean isLoadMore = false;
    private SortGuideItemAdapter sortGuideItemAdapter = null;
    private String[] sortGuideStr = null;
    private int pageIndex = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String msgGuideList = (String) msg.obj;
                    if (Util.isNull(msgGuideList))
                        return;
                    guideReBean = GuideParse.getGuideList(msgGuideList);
                    if (guideReBean != null && guideReBean.getGuideReBeanList() != null) {
                        if (isLoadMore && guideAdapter != null) {
                            if (!checkPullUp()) {
                                isLoadMore = false;
                                return;
                            }
                            if (guideAdapter != null) {
                                isLoadMore = false;
                                guideBeanList.addAll(guideReBean.getGuideReBeanList());
                                guideAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                        guideBeanList = guideReBean.getGuideReBeanList();
                        guideAdapter = new GuideAdapter(GuideListActivity.this, guideReBean.getGuideReBeanList(), imageLoader);
                        guideList_plv.setAdapter(guideAdapter);
                        guideList_plv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
            }
            isLoadMore = false;
            guideList_plv.onRefreshComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        sortParams = "";
        api = new Api(this, handler);
        guideBeanList = new ArrayList<>();
        sortConditionMap = (Map<String, String>) getIntent().getSerializableExtra("ReservationMapInfo");
        api.getConditionGuideList(sortConditionMap, sortParams, pageIndex + "");
        sortGuideStr = getResources().getStringArray(R.array.sortGuideStr);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.guideSelectTitle));
        ((CheckBox) findViewById(R.id.guideSortList_cb)).setOnCheckedChangeListener(this);
        sortGuideItem_rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        guideList_plv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        guideList_plv.setOnItemClickListener(this);
        guideList_plv.setOnRefreshListener(this);
        sortGuideItemAdapter = new SortGuideItemAdapter(this, Arrays.asList(sortGuideStr));
        sortGuideItem_rv.setAdapter(sortGuideItemAdapter);
        sortGuideItemAdapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < 0)
            return;
        Intent intent = new Intent(this, GuideDetailActivity.class);
        intent.putExtra("guideCardNum", guideReBean.getGuideReBeanList().get(position - 1).getGuideCardNum());
        intent.putExtra("ReservationMapInfo", (Serializable) sortConditionMap);
        startActivity(intent);
    }

    @OnClick({R.id.titleLeft_img, R.id.sortCondition_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.sortCondition_ll:
                ((CheckBox) findViewById(R.id.guideSortList_cb)).setChecked(false);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            findViewById(R.id.sortGuideList_ll).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sortGuideList_ll).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex = 1;
        guideBeanList = new ArrayList<>();
        api.getConditionGuideList(sortConditionMap, sortParams, pageIndex + "");
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!checkPullUp())
            return;
        isLoadMore = true;
        pageIndex++;
        api.getConditionGuideList(sortConditionMap, sortParams, pageIndex + "");
    }

    public boolean checkPullUp() {
        if (guideReBean == null) {
            guideList_plv.onRefreshComplete();
            return false;
        }
        if (guideReBean.getCurrentPage() == guideReBean.getTotalSize()) {
            Util.showToast(this, getString(R.string.appNoData));
            handler.sendEmptyMessageDelayed(0, 1 * 1000);
            return false;
        }
        return false;
    }

    @Override
    public void itemClick(int position) {
        switch (position) {
            case 0:
                sortParams = "";
                break;
            case 1://价格最高
                sortParams = "&price=desc";
                break;
            case 2://价格最低
                sortParams = "&price=asc";
                break;
            case 3://星级优先
                sortParams = "&stars=desc";
                break;
            case 4://年龄最高
                sortParams = "&age=desc";
                break;
            case 5://年龄最低
                sortParams = "&age=asc";
                break;
        }
        api.getConditionGuideList(sortConditionMap, sortParams, pageIndex + "");
        ((CheckBox) findViewById(R.id.guideSortList_cb)).setChecked(false);
    }
}
