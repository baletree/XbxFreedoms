package com.xbx123.freedom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.FrequeCotactsBean;
import com.xbx123.freedom.beans.InsurerBean;

import java.util.List;

/**
 * Created by EricPeng on 2016/10/4.
 * 导游支付明细游客的列表
 */
public class GuideTraverInfoAdapter extends BaseAdapter {
    private Context context;
    private List<InsurerBean> insurerList;

    public GuideTraverInfoAdapter(Context context, List<InsurerBean> insurerList) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_detail_traver, null);
            holder.item_underContactsName_tv = (TextView) convertView.findViewById(R.id.item_underContactsName_tv);
            holder.item_underContactsIdCard_tv = (TextView) convertView.findViewById(R.id.item_underContactsIdCard_tv);
            holder.item_underContactsPhone_tv = (TextView) convertView.findViewById(R.id.item_underContactsPhone_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InsurerBean insurerBean = insurerList.get(position);
        holder.item_underContactsName_tv.setText(insurerBean.getInsurerName());
        holder.item_underContactsIdCard_tv.setText(insurerBean.getInsurerIdcard());
        holder.item_underContactsPhone_tv.setText(insurerBean.getInsurerPhone());
        return convertView;
    }

    class ViewHolder {
        private TextView item_underContactsName_tv;
        private TextView item_underContactsIdCard_tv;
        private TextView item_underContactsPhone_tv;
    }
}
