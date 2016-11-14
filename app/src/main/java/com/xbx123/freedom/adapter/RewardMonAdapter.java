package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xbx123.freedom.R;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/1.
 */
public class RewardMonAdapter extends RecyclerView.Adapter<RewardMonAdapter.MyViewHolder> {
    private Context context;
    private List<Integer> rewardList;

    private OnRecyItemClickListener mOnItemClickListener;

    public RewardMonAdapter(Context context, List<Integer> rewardList) {
        this.context = context;
        this.rewardList = rewardList;
    }

    public void setOnItemClickListener(OnRecyItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward_mon, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemReward_tv.setText(rewardList.get(position) + context.getString(R.string.appYuanC));
        holder.itemReward_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemReward_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemReward_tv = (TextView) itemView.findViewById(R.id.itemReward_tv);
        }
    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position);
    }
}
