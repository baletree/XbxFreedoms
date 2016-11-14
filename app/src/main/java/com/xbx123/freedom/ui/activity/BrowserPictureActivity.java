package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.BrowserPictureAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eric on 2016/7/28.
 * 图片浏览界面
 */
public class BrowserPictureActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.picBrowser_vp)
    ViewPager picBrowserVp;
    @Bind(R.id.itemPageFlagTv)
    TextView itemPageFlagTv;

    private BrowserPictureAdapter browserAdapter = null;
    private List<String> picUrlList = null;
    private int picPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_picture);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        picPosition = intent.getIntExtra("clickPosition", -1);
        picUrlList = (List<String>) intent.getSerializableExtra("PictureUrlsList");
    }

    @Override
    protected void initViews() {
        super.initViews();
        browserAdapter = new BrowserPictureAdapter(BrowserPictureActivity.this, picUrlList, imageLoader);
        picBrowserVp.setAdapter(browserAdapter);
        picBrowserVp.setCurrentItem(picPosition);
        picBrowserVp.addOnPageChangeListener(this);
        if (picPosition != -1 && picUrlList != null)
            itemPageFlagTv.setText((picPosition + 1) + "/" + picUrlList.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        itemPageFlagTv.setText((position + 1) + "/" + picUrlList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
