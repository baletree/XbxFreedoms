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
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.GuideReBean;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.utils.tool.StringUtil;

import java.util.List;

/**
 * Created by EricPeng on 2016/10/4.
 * 导游下单选择游客的列表
 */
public class ChoicedTraverAdapter extends BaseAdapter {
    private Context context;
    private List<InsurerBean> insurerList;

    public ChoicedTraverAdapter(Context context, List<InsurerBean> insurerList) {
        this.context = context;
        this.insurerList = insurerList;
    }

    @Override
    public int getCount() {
        return insurerList.size();
    }

    @Override
    public Object getItem(int position) {
        return insurerList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_traver, null);
            holder.item_traverName_tv = (TextView) convertView.findViewById(R.id.item_traverName_tv);
            holder.item_traverCardId_tv = (TextView) convertView.findViewById(R.id.item_traverCardId_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InsurerBean insurerBean = insurerList.get(position);
        holder.item_traverName_tv.setText(insurerBean.getInsurerName());
        holder.item_traverCardId_tv.setText(insurerBean.getInsurerIdcard());
        return convertView;
    }

    class ViewHolder {
        private TextView item_traverName_tv;
        private TextView item_traverCardId_tv;
    }
}
