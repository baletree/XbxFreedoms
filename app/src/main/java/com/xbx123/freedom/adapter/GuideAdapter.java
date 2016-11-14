package com.xbx123.freedom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.GuideReBean;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.utils.tool.StringUtil;

import java.util.List;

/**
 * Created by EricPeng on 2016/10/4.
 */
public class GuideAdapter extends BaseAdapter {
    private Context context;
    private List<GuideReBean> guideBeanList;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    public GuideAdapter(Context context, List<GuideReBean> guideBeanList,ImageLoader imageLoader) {
        this.context = context;
        this.guideBeanList = guideBeanList;
        this.imageLoader = imageLoader;
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    @Override
    public int getCount() {
        return guideBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return guideBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_list, null);
            holder.item_guideHead_img = (RoundedImageView) convertView.findViewById(R.id.item_guideHead_img);
            holder.item_guideName_tv = (TextView) convertView.findViewById(R.id.item_guideName_tv);
            holder.item_guideSex_img = (ImageView) convertView.findViewById(R.id.item_guideSex_img);
            holder.item_guideLanguage_tv = (TextView) convertView.findViewById(R.id.item_guideLanguage_tv);
            holder.item_guideAge_tv = (TextView) convertView.findViewById(R.id.item_guideAge_tv);
            holder.item_guideJobAge_tv = (TextView) convertView.findViewById(R.id.item_guideJobAge_tv);
            holder.item_gCardNum_tv = (TextView) convertView.findViewById(R.id.item_gCardNum_tv);
            holder.item_gServiceFee_tv = (TextView) convertView.findViewById(R.id.item_gServiceFee_tv);
            holder.item_guideStar_rtb = (RatingBar) convertView.findViewById(R.id.item_guideStar_rtb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GuideReBean guideReBean = guideBeanList.get(position);
        holder.item_guideName_tv.setText(guideReBean.getGuideName());
        holder.item_guideLanguage_tv.setText(guideReBean.getGuideLanguage());
        holder.item_gCardNum_tv.setText(guideReBean.getGuideCardNum());
        holder.item_gServiceFee_tv.setText(guideReBean.getGuideDayPrice() + "");
        holder.item_guideAge_tv.setText(StringUtil.getAgeLatter(guideReBean.getGuideAge()) + context.getString(R.string.appLatter));
        holder.item_guideStar_rtb.setRating(guideReBean.getGuideStars());
        holder.item_guideJobAge_tv.setText(StringUtil.getJobTimes(guideReBean.getGuideJobTime()) + context.getString(R.string.guideJobTime));
        if (context.getString(R.string.sexMale).equals(guideReBean.getGuideSex()))
            holder.item_guideSex_img.setImageResource(R.mipmap.icon_nan);
        else
            holder.item_guideSex_img.setImageResource(R.mipmap.icon_nv);
        imageLoader.displayImage(guideReBean.getGuideHead(), holder.item_guideHead_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        return convertView;
    }

    class ViewHolder {
        private RoundedImageView item_guideHead_img;
        private TextView item_guideName_tv;
        private ImageView item_guideSex_img;
        private TextView item_guideLanguage_tv;
        private TextView item_guideAge_tv;
        private TextView item_guideJobAge_tv;
        private RatingBar item_guideStar_rtb;
        private TextView item_gCardNum_tv;
        private TextView item_gServiceFee_tv;
    }
}
