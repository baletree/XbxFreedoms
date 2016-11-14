package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.utils.tool.Util;

import java.util.List;

/**
 * Created by EricYuan on 2016/6/17.
 */
public class InsurerListAdapter extends RecyclerView.Adapter<InsurerListAdapter.MyViewHolder> {
    private Context context;
    private List<InsurerBean> insurerList;
    private ItemClickListener itemClickListener;

    public InsurerListAdapter(Context context, List<InsurerBean> insurerList) {
        this.context = context;
        this.insurerList = insurerList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insurer_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        InsurerBean insurerBean = insurerList.get(position);
        holder.itemInsurerNameTv.setText(insurerBean.getInsurerName());
        if (!Util.isNull(insurerBean.getInsurerIdcard())) {
            String idCardStr = insurerBean.getInsurerIdcard().substring(6, 14);
            holder.itemInsurerNameId.setText(insurerBean.getInsurerIdcard().replace(idCardStr, "********"));
        }
        if (position == 0) {
            LinearLayout.LayoutParams reLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            reLp.setMargins(0, 32, 0, 0);
            reLp.height = Util.dip2px(context, 45f);
            holder.itemInsurerRl.setLayoutParams(reLp);
        }
        holder.itemInsurerRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insurerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemInsurerNameTv;
        private TextView itemInsurerNameId;
        private RelativeLayout itemInsurerRl;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemInsurerNameTv = (TextView) itemView.findViewById(R.id.itemInsurerNameTv);
            itemInsurerNameId = (TextView) itemView.findViewById(R.id.itemInsurerNameId);
            itemInsurerRl = (RelativeLayout) itemView.findViewById(R.id.itemInsurerRl);
        }
    }

    public interface ItemClickListener {
        void itemClick(int position);
    }
}
