package com.xbx123.freedom.adapter;

import android.content.Context;
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
public class SelectInsurerListAdapter extends RecyclerView.Adapter<SelectInsurerListAdapter.MyViewHolder> {
    private Context context;
    private List<InsurerBean> insurerList;
    private List<InsurerBean> choiceIsList;
    private ItemClickListener itemClickListener;

    public SelectInsurerListAdapter(Context context, List<InsurerBean> insurerList, List<InsurerBean> choiceIsList) {
        this.context = context;
        this.insurerList = insurerList;
        this.choiceIsList = choiceIsList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_insurer_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final InsurerBean insurerBean = insurerList.get(position);
        holder.itemSelect_InsurerNameTv.setText(insurerBean.getInsurerName());
        if (!Util.isNull(insurerBean.getInsurerIdcard())) {
            String idCardStr = insurerBean.getInsurerIdcard().substring(6, 14);
            holder.itemSelect_InsurerNameId.setText(insurerBean.getInsurerIdcard().replace(idCardStr, "********"));
        }
        if (position == 0) {
            LinearLayout.LayoutParams reLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            reLp.setMargins(0, 32, 0, 0);
            reLp.height = Util.dip2px(context, 45f);
            holder.itemSelect_InsurerRl.setLayoutParams(reLp);
        }
        holder.itemSelect_InsurerRl.setBackgroundResource(R.color.colorWhite);
        if (choiceIsList != null && choiceIsList.size() > 0) {
            for (int i = 0; i < choiceIsList.size(); i++) {
                if ((choiceIsList.get(i).getInsurerId()).equals(insurerBean.getInsurerId())) {
                    holder.itemSelect_InsurerRl.setBackgroundResource(R.drawable.d9d9_gray_bg);
                    Util.pLog("choiceId = " + insurerBean.getInsurerId());
                    break;
                }
            }
        }
        holder.itemSelect_InsurerRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.itemClick(position, insurerBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insurerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemSelect_InsurerNameTv;
        private TextView itemSelect_InsurerNameId;
        private RelativeLayout itemSelect_InsurerRl;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemSelect_InsurerNameTv = (TextView) itemView.findViewById(R.id.itemSelect_InsurerNameTv);
            itemSelect_InsurerNameId = (TextView) itemView.findViewById(R.id.itemSelect_InsurerNameId);
            itemSelect_InsurerRl = (RelativeLayout) itemView.findViewById(R.id.itemSelect_InsurerRl);
        }
    }

    public interface ItemClickListener {
        void itemClick(int position, InsurerBean insurerBean);
    }
}
