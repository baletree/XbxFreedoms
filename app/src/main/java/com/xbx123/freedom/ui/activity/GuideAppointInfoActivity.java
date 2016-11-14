package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.ChoicedTraverAdapter;
import com.xbx123.freedom.beans.GuideDetailBean;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.http.Api;
import com.xbx123.freedom.jsonparse.CreateOrderParse;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.StringUtil;
import com.xbx123.freedom.utils.tool.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricPeng on 2016/10/4.
 * 预约信息填写
 */
public class GuideAppointInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.reservationGHead_img)
    RoundedImageView reservationGHeadImg;//导游头像
    @Bind(R.id.reGuideName_tv)
    TextView reGuideNameTv;//导游名字
    @Bind(R.id.reGuideSex_img)
    ImageView reGuideSexImg;//导游性别的标志
    @Bind(R.id.reGuideLanguage_tv)
    TextView reGuideLanguageTv;//语种
    @Bind(R.id.reGuideAge_tv)
    TextView reGuideAgeTv;//年龄区间
    @Bind(R.id.reGuideJobAge_tv)
    TextView reGuideJobAgeTv;//工作年限
    @Bind(R.id.reGuideStar_rtb)
    RatingBar reGuideStarRtb;//星级
    @Bind(R.id.reGuideCard_tv)
    TextView reGuideCardTv;//导游证号
    @Bind(R.id.reGuideTripDate_tv)
    TextView reGuideTripDateTv;//
    @Bind(R.id.reGuideTripDateNum_tv)
    TextView reGuideTripDateNumTv;//出行天数
    @Bind(R.id.reGuideTripPNum_tv)
    TextView reGuideTripPNumTv;//出行人数
    @Bind(R.id.reUrgencyPName_tv)
    TextView reUrgencyPNameTv;//紧急联系人姓名
    @Bind(R.id.reUrgencyPhone_tv)
    TextView reUrgencyPhoneTv;//紧急联系人电话
    @Bind(R.id.reRemarks_et)
    EditText reRemarksEt;//备注信息
    @Bind(R.id.readKnowTxt_tv)
    TextView readKnowTxtTv;//条款须知
    @Bind(R.id.guideTotalPrice_tv)
    TextView guideTotalPriceTv;//预约总价
    @Bind(R.id.travelerList_lv)
    ListView travelerList_lv;//出行人列表
    @Bind(R.id.guideKnow_cb)
    CheckBox guideKnow_cb;//条款须知的选中

    private Api api = null;
    private int tripPNum = 1;
    private final int intentCode = 101;
    private final int intentCodeContact = 102;
    private GuideDetailBean guideDetailBean = null;
    private Map<String, String> sortConditionMap = null;
    private List<InsurerBean> choiceIsList = null;
    private ChoicedTraverAdapter choicedTraverAdapter = null;
    private String orderNumber = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequestFlag.requestPageOne:
                    String orderResult = (String) msg.obj;
                    orderNumber = CreateOrderParse.getOrderNum(orderResult);
                    Intent intent = new Intent(GuideAppointInfoActivity.this, GuideOrderPayActivity.class);
                    intent.putExtra("PayOrderNum", orderNumber);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_appoint);
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(GuideAppointInfoActivity.this, handler);
        sortConditionMap = (Map<String, String>) getIntent().getSerializableExtra("ReservationMapInfo");
        guideDetailBean = (GuideDetailBean) getIntent().getSerializableExtra("GuideDetailBean");
        choiceIsList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.titleTxt_tv)).setText(getString(R.string.guideAppointTitle));
        travelerList_lv.setOnItemClickListener(this);
        reGuideTripPNumTv.setText(tripPNum + "");
        setGuideInfo();
        if (sortConditionMap == null)
            return;
        reGuideTripDateTv.setText(sortConditionMap.get("TripDate"));
        reGuideTripDateNumTv.setText(sortConditionMap.get("TripDateNum") + getString(R.string.appDay));
    }

    private void setGuideInfo() {
        if (guideDetailBean == null)
            return;
        imageLoader.displayImage(guideDetailBean.getGuideHead(), reservationGHeadImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        if (getString(R.string.sexMale).equals(guideDetailBean.getGuideSex()))
            reGuideSexImg.setImageResource(R.mipmap.icon_nan);
        else
            reGuideSexImg.setImageResource(R.mipmap.icon_nv);
        reGuideNameTv.setText(guideDetailBean.getGuideName());
        reGuideLanguageTv.setText(guideDetailBean.getGuideLanguage());
        reGuideAgeTv.setText(StringUtil.getAgeLatter(guideDetailBean.getGuideAge()) + getString(R.string.appLatter));
        reGuideJobAgeTv.setText(StringUtil.getJobTimes(guideDetailBean.getGuideJobTimes()) + getString(R.string.guideDetailJobTime));
        reGuideCardTv.setText(guideDetailBean.getGuideNumber());
        if (!Util.isNull(guideDetailBean.getGuideStar()))
            reGuideStarRtb.setRating(Integer.parseInt(guideDetailBean.getGuideStar()));
        guideTotalPriceTv.setText(getString(R.string.appYuan) + (Util.getOnePointDouble(guideDetailBean.getGuideDayPrice() * Integer.parseInt(sortConditionMap.get("TripDateNum")))));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case intentCode:
                choiceIsList = (List<InsurerBean>) data.getSerializableExtra("ChoiceInsurer");
                if (choiceIsList != null && choiceIsList.size() > 0) {
                    choicedTraverAdapter = new ChoicedTraverAdapter(GuideAppointInfoActivity.this, choiceIsList);
                    travelerList_lv.setAdapter(choicedTraverAdapter);
                    travelerList_lv.setVisibility(View.VISIBLE);
                    Util.setListViewHeight(travelerList_lv);
                }
                break;
            case intentCodeContact:
                String contactsName = data.getStringExtra("FrequentContactsName");
                String contactsPhone = data.getStringExtra("FrequentContactsPhone");
                if (!Util.isNull("contactsName") && !Util.isNull("contactsPhone")) {
                    findViewById(R.id.urgencyInfo_ll).setVisibility(View.VISIBLE);
                    reUrgencyPNameTv.setText(contactsName);
                    reUrgencyPhoneTv.setText(contactsPhone);
                }
                break;
        }
    }

    @OnClick({R.id.titleLeft_img, R.id.guideAddUpdate_rl, R.id.UrgencyPeo_rl, R.id.clientNumAdd_img,
            R.id.clientNumLess_img, R.id.submitAppoint_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleLeft_img:
                finish();
                break;
            case R.id.guideAddUpdate_rl:
                intent.setClass(this, SelectInsurerListActivity.class);
                intent.putExtra("ChoiceInsurerList", (Serializable) choiceIsList);
                intent.putExtra("ChoiceInsurerNum", tripPNum);
                startActivityForResult(intent, intentCode);
                break;
            case R.id.UrgencyPeo_rl:
                intent.setClass(this, FrequentContactsActivity.class);
                startActivityForResult(intent, intentCodeContact);
                break;
            case R.id.submitAppoint_tv:
                if (choiceIsList == null) {
                    Util.showToast(this, getString(R.string.guideTraverTip));
                    return;
                }
                if (choiceIsList.size() < tripPNum) {
                    Util.showToast(this, getString(R.string.guideTraverTip2));
                    return;
                }
                if (Util.isNull(reUrgencyPNameTv.getText().toString()) || Util.isNull(reUrgencyPhoneTv.getText().toString())) {
                    Util.showToast(this, getString(R.string.guideContactsTip));
                    return;
                }
                if (!guideKnow_cb.isChecked()) {
                    Util.showToast(this, getString(R.string.guideGuideReadKnowCheck) + getString(R.string.guideGuideReadKnow));
                    return;
                }
                String traverId = "";
                for (int i = 0; i < choiceIsList.size(); i++) {
                    traverId = traverId + choiceIsList.get(i).getInsurerId() + ",";
                }
                if (!Util.isNull(traverId))
                    traverId = traverId.substring(0, traverId.length() - 1);
                String uid = SharePrefer.getUserInfo(this).getUid();
                //用户id、导游证号、游玩日期、游玩天数、游玩人数、出行人id拼接、紧急联系人姓名、紧急联系人电话、备注
                api.orderToGuide(uid, guideDetailBean.getGuideNumber(), reGuideTripDateTv.getText().toString(), sortConditionMap.get("TripDateNum"), tripPNum + "",
                        traverId, reUrgencyPNameTv.getText().toString(), reUrgencyPhoneTv.getText().toString(), reRemarksEt.getText().toString());
                break;
            case R.id.clientNumAdd_img:
                if (tripPNum >= 50)
                    return;
                tripPNum++;
                reGuideTripPNumTv.setText(tripPNum + "");
                break;
            case R.id.clientNumLess_img:
                if (choiceIsList != null) {
                    if (tripPNum <= choiceIsList.size())
                        return;
                }
                if (tripPNum <= 1)
                    return;
                tripPNum--;
                reGuideTripPNumTv.setText(tripPNum + "");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        choiceIsList.remove(i);
        choicedTraverAdapter.notifyDataSetChanged();
        Util.setListViewHeight(travelerList_lv);
    }
}
