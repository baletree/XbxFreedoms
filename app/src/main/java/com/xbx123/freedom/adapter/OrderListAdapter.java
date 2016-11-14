package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.OrderBean;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.StringUtil;

import java.util.List;

/**
 * Created by EricYuan on 2016/5/25.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private OnRecyItemClickListener mOnItemClickListener;
    private List<OrderBean> orderList;
    private Context context;

    public OrderListAdapter(Context context, List<OrderBean> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnItemClickListener(OnRecyItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.olPrice_tv.setTextColor(context.getResources().getColor(R.color.colorBTxt));
        holder.orLState_tv.setTextColor(context.getResources().getColor(R.color.colorBTxt));
        holder.orderLocate_img.setImageResource(R.mipmap.orderdown_locate);
        final OrderBean orderBean = orderList.get(position);
        holder.olPrice_tv.setText(context.getString(R.string.appYuan) + orderBean.getOrderPay());
        holder.oLOrderNum_tv.setText(orderBean.getOrderNum());
        holder.oLOrderTime_tv.setText(orderBean.getOrderTime());
        holder.oLLocate_tv.setText(StringUtil.splitCityStr(context, orderBean.getOrderAddress()));
        holder.oLServerType_tv.setText(StringUtil.serverType(context, orderBean.getServerType()));
        holder.orLState_tv.setText(StringUtil.orderStateStr(context, orderBean.getOrderState()));
        if (orderBean.getServerType() == Constant.GuidesType) {
            holder.orderLocate_img.setImageResource(R.mipmap.reservation_date);
            holder.oLLocate_tv.setText(context.getString(R.string.orderTripTime) + orderBean.getOrderStartTime());
        }
        switch (orderBean.getOrderState()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 9:
                holder.olPrice_tv.setText(" ");
                holder.orLState_tv.setTextColor(context.getResources().getColor(R.color.colorReward));
                break;
            case 10:
                holder.olPrice_tv.setTextColor(context.getResources().getColor(R.color.colorHint));
                holder.orLState_tv.setTextColor(context.getResources().getColor(R.color.colorHint));
                break;
            case 13:
                holder.orLState_tv.setTextColor(context.getResources().getColor(R.color.colorReward));
                break;
        }
        holder.orderItem_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position, orderBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout orderItem_ll;
        private TextView orLState_tv;
        private TextView olPrice_tv;
        private TextView oLServerType_tv;
        private TextView oLOrderNum_tv;
        private TextView oLOrderTime_tv;
        private TextView oLLocate_tv;
        private ImageView orderLocate_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderItem_ll = (LinearLayout) itemView.findViewById(R.id.orderItem_ll);
            orLState_tv = (TextView) itemView.findViewById(R.id.orLState_tv);
            olPrice_tv = (TextView) itemView.findViewById(R.id.olPrice_tv);
            oLServerType_tv = (TextView) itemView.findViewById(R.id.oLServerType_tv);
            oLOrderNum_tv = (TextView) itemView.findViewById(R.id.oLOrderNum_tv);
            oLOrderTime_tv = (TextView) itemView.findViewById(R.id.oLOrderTime_tv);
            oLLocate_tv = (TextView) itemView.findViewById(R.id.oLLocate_tv);
            orderLocate_img = (ImageView) itemView.findViewById(R.id.orderLocate_img);
        }
    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position, OrderBean orderBean);
    }
}
