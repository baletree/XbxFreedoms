package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.OrderBean;
import com.xbx123.freedom.utils.tool.StringUtil;

import java.util.List;

/**
 * Created by EricYuan on 2016/6/16.
 */
public class RequestInvoicesAdapter extends RecyclerView.Adapter<RequestInvoicesAdapter.MyViewHolder> {
    private List<OrderBean> orderList;
    private boolean[] choiceArr;
    private Context context;
    private ChoiceInvoicesListen invoicesListen;

    public RequestInvoicesAdapter(Context context, List<OrderBean> orderList,boolean[] choiceArr) {
        this.context = context;
        this.orderList = orderList;
        this.choiceArr = choiceArr;
    }

    public void setInvoicesListen(ChoiceInvoicesListen invoicesListen) {
        this.invoicesListen = invoicesListen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_invoices, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        OrderBean orderBean = orderList.get(position);
        if(choiceArr[position])
            holder.invoicesChoiceImg.setImageResource(R.mipmap.pay_check);
        else
            holder.invoicesChoiceImg.setImageResource(R.mipmap.pay_uncheck);
        holder.invoicesST_tv.setText(StringUtil.serverType(context, orderBean.getServerType()));
        holder.invoicesONum_tv.setText(orderBean.getOrderNum());
        holder.invoicesOTime_tv.setText(orderBean.getOrderTime());
        holder.invoicesLocate_tv.setText(StringUtil.splitCityStr(context, orderBean.getOrderAddress()));
        holder.invoicesMoney_tv.setText(orderBean.getOrderPay());
        holder.invoicesChoiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invoicesListen != null)
                    invoicesListen.clickChoice(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView invoicesChoiceImg;
        private TextView invoicesST_tv;
        private TextView invoicesONum_tv;
        private TextView invoicesOTime_tv;
        private TextView invoicesLocate_tv;
        private TextView invoicesMoney_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            invoicesChoiceImg = (ImageView) itemView.findViewById(R.id.invoicesChoiceImg);
            invoicesST_tv = (TextView) itemView.findViewById(R.id.invoicesST_tv);
            invoicesONum_tv = (TextView) itemView.findViewById(R.id.invoicesONum_tv);
            invoicesLocate_tv = (TextView) itemView.findViewById(R.id.invoicesLocate_tv);
            invoicesOTime_tv = (TextView) itemView.findViewById(R.id.invoicesOTime_tv);
            invoicesMoney_tv = (TextView) itemView.findViewById(R.id.invoicesMoney_tv);
        }
    }

    public interface ChoiceInvoicesListen {
        public void clickChoice(int position);
    }
}
