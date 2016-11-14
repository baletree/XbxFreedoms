package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.xbx123.freedom.R;

import java.util.List;

/**
 * Created by EricYuan on 2016/10/11.
 */

public class SortGuideItemAdapter extends RecyclerView.Adapter<SortGuideItemAdapter.MyViewHolder> {
    private Context context;
    private List<String> strList;
    private boolean[] positionCheck = null;
    private ItemClickListener itemClickListener;

    public SortGuideItemAdapter(Context context, List<String> strList) {
        this.context = context;
        this.strList = strList;
        positionCheck = new boolean[strList.size()];
        positionCheck[0] = true;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_guide_str, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.sortGuideStr_rb.setText(strList.get(position));
        if (positionCheck[position])
            holder.sortGuideStr_rb.setChecked(true);
        else
            holder.sortGuideStr_rb.setChecked(false);
        holder.sortGuideStr_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    resetCheckState(position);
                    itemClickListener.itemClick(position);
                }
            }
        });
    }

    private void resetCheckState(int position) {
        for (int i = 0; i < strList.size(); i++) {
            if (i == position)
                positionCheck[position] = true;
            else
                positionCheck[i] = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RadioButton sortGuideStr_rb;

        public MyViewHolder(View itemView) {
            super(itemView);
            sortGuideStr_rb = (RadioButton) itemView.findViewById(R.id.sortGuideStr_rb);
        }
    }

    public interface ItemClickListener {
        void itemClick(int position);
    }
}
