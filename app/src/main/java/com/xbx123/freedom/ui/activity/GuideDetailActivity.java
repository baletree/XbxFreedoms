package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.GuideDetailAlbumAdapter;
import com.xbx123.freedom.adapter.GuideDetailCommentAdapter;
import com.xbx123.freedom.beans.GuideDetailBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.GuideParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;
import com.xbx123.freedom.view.views.FlowLayout;
import com.xbx123.freedom.view.views.FullyLinearLayoutManager;
import com.xbx123.freedom.view.views.UniRecycleViewDivider;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * project:XbxFreedom
 * author:Hi-Templar
 * dateTime:2016/10/5 11:24
 * describe:
 */
public class GuideDetailActivity extends BaseActivity {
    @Bind(R.id.gDetailHead_img)
    RoundedImageView gDetailHead_img;//导游头像
    @Bind(R.id.gDetailName)
    TextView gDetailName;//导游姓名
    @Bind(R.id.gDetailSex_img)
    ImageView gDetailSex_img;
    @Bind(R.id.gDetailLaguage_tv)
    TextView gDetailLaguage_tv;//导游语言
    @Bind(R.id.gDetailAge_tv)
    TextView gDetailAge_tv;//导游年龄
    @Bind(R.id.gDetailJobAge_tv)
    TextView gDetailJobAge_tv;//导游工作年限
    @Bind(R.id.gDetailCertNo)
    TextView gDetailCertNo;//导游证件号
    @Bind(R.id.gDetailScoreRtb)
    RatingBar gDetailScoreRtb;//导游评分
    @Bind(R.id.gDetailIntro)
    TextView gDetailIntro;//导游简介
    @Bind(R.id.gDetailAlbum)
    RecyclerView gDetailAlbum;//导游相册
    @Bind(R.id.guideSpecialTag_fl)
    FlowLayout guideSpecialTag_fl;
    @Bind(R.id.gDetailComment)
    RecyclerView gDetailComment;//导游评论
    @Bind(R.id.gDetailNoComment)
    TextView gDetailNoComment;//无评价内容
    @Bind(R.id.guideDayPrice_tv)
    TextView guideDayPrice_tv;

    private Api api = null;
    GuideDetailAlbumAdapter albumAdapter;
    GuideDetailCommentAdapter commentAdapter;
    private Map<String, String> sortConditionMap = null;
    private String guideCardNum = "";
    private GuideDetailBean guideDetailBean = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String guideInfo = (String) msg.obj;
                    if (Util.isNull(guideInfo))
                        return;
                    guideDetailBean = GuideParse.getGuideDetailInfo(guideInfo);
                    if (guideDetailBean == null)
                        return;
                    setGuideInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        guideCardNum = getIntent().getStringExtra("guideCardNum");
        api = new Api(this, handler);
        api.getGuideHomePage(guideCardNum);
        sortConditionMap = (Map<String, String>) getIntent().getSerializableExtra("ReservationMapInfo");
    }

    @Override
    protected void initViews() {
        findViewById(R.id.guideDetailPage_rl).setVisibility(View.GONE);
        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(this);
        gDetailComment.setLayoutManager(commentLayoutManager);
        FullyLinearLayoutManager albumLayoutManager = new FullyLinearLayoutManager(this);
        albumLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        gDetailAlbum.setLayoutManager(albumLayoutManager);
        gDetailAlbum.addItemDecoration(new UniRecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, R.drawable.divider_recycleview));
    }

    private void setGuideInfo() {
        findViewById(R.id.guideDetailPage_rl).setVisibility(View.VISIBLE);
        gDetailName.setText(guideDetailBean.getGuideName());
        gDetailLaguage_tv.setText(guideDetailBean.getGuideLanguage());
        imageLoader.displayImage(guideDetailBean.getGuideHead(), gDetailHead_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        if (getString(R.string.sexMale).equals(guideDetailBean.getGuideSex()))
            gDetailSex_img.setImageResource(R.mipmap.icon_nan);
        else
            gDetailSex_img.setImageResource(R.mipmap.icon_nv);
        gDetailAge_tv.setText(StringUtil.getAgeLatter(guideDetailBean.getGuideAge()) + getString(R.string.appLatter));
        gDetailJobAge_tv.setText(StringUtil.getJobTimes(guideDetailBean.getGuideJobTimes()) + getString(R.string.guideDetailJobTime));
        gDetailCertNo.setText(guideDetailBean.getGuideNumber());
        if (!Util.isNull(guideDetailBean.getGuideStar()))
            gDetailScoreRtb.setRating(Integer.parseInt(guideDetailBean.getGuideStar()));
        gDetailIntro.setText(guideDetailBean.getGuideIntroduce());
        guideDayPrice_tv.setText(getString(R.string.appYuan) + guideDetailBean.getGuideDayPrice());
        if (!Util.isNull(guideDetailBean.getGuideAlbum()) && guideDetailBean.getGuideAlbum().contains("@@")) {
            String[] albumArray = guideDetailBean.getGuideAlbum().split("@@");
            albumAdapter = new GuideDetailAlbumAdapter(this, Arrays.asList(albumArray), imageLoader, configFactory);
            gDetailAlbum.setAdapter(albumAdapter);
            findViewById(R.id.gDetailAlbum_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.gDetailNoAlbum_ll).setVisibility(View.GONE);
        }
        if (!Util.isNull(guideDetailBean.getGuideSpecial()) && guideDetailBean.getGuideSpecial().contains("@@")) {
            String[] specialArray = guideDetailBean.getGuideSpecial().split("@@");
            for (int i = 0; i < specialArray.length; i++) {
                guideSpecialTag_fl.addView(addTextView(specialArray[i]));
            }
            guideSpecialTag_fl.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.guideDetailBackImg, R.id.gDetailNotice, R.id.guideDetailAppoint_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.guideDetailBackImg:
                finish();
                break;
            case R.id.gDetailNotice:

                break;
            case R.id.guideDetailAppoint_tv:
                intent.setClass(this, GuideAppointInfoActivity.class);
                intent.putExtra("ReservationMapInfo", (Serializable) sortConditionMap);
                intent.putExtra("GuideDetailBean", guideDetailBean);
                startActivity(intent);
                break;
        }
    }

    /**
     * 展示导游特长的流式布局
     *
     * @param txt
     * @return
     */
    private TextView addTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 75);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(12, 0, 12, 0);
        txtLp.rightMargin = 24;
        txtLp.bottomMargin = 14;
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.border_input_bg);
        textView.setLayoutParams(txtLp);
        textView.setTextSize(12f);
        textView.setTextColor(getResources().getColor(R.color.sixColor));
        return textView;
    }
}
