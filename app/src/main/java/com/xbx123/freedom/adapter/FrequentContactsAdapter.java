package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.FrequeCotactsBean;

import java.util.List;

/**
 * Created by EricPeng on 2016/10/5.
 */
public class FrequentContactsAdapter extends RecyclerView.Adapter<FrequentContactsAdapter.MyViewHolder> {
    private List<FrequeCotactsBean> fContactsList;
    private Context context;
    private boolean isFromSetting;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FrequentContactsAdapter(Context context, List<FrequeCotactsBean> fContactsList, boolean isFromSetting) {
        this.context = context;
        this.fContactsList = fContactsList;
        this.isFromSetting = isFromSetting;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frequet_contacts, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (isFromSetting)
            holder.fContactsEditImg.setVisibility(View.VISIBLE);
        else
            holder.fContactsEditImg.setVisibility(View.GONE);
        final FrequeCotactsBean frequeCotactsBean = fContactsList.get(position);
        holder.fContactsNameTv.setText(frequeCotactsBean.getContactName());
        holder.fContactsPhoneTv.setText(frequeCotactsBean.getContactPhone());
        holder.itemFContactsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.itemClick(position, frequeCotactsBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fContactsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout itemFContactsRl;
        private TextView fContactsNameTv;
        private TextView fContactsPhoneTv;
        private ImageView fContactsEditImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemFContactsRl = (RelativeLayout) itemView.findViewById(R.id.itemFContactsRl);
            fContactsNameTv = (TextView) itemView.findViewById(R.id.fContactsNameTv);
            fContactsPhoneTv = (TextView) itemView.findViewById(R.id.fContactsPhoneTv);
            fContactsEditImg = (ImageView) itemView.findViewById(R.id.fContactsEditImg);
        }
    }

    public interface ItemClickListener {
        void itemClick(int position, FrequeCotactsBean frequeCotactsBean);
    }
}
