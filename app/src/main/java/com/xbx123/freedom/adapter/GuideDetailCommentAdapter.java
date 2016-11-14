package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx123.freedom.R;

/**
 * project:XbxFreedom
 * author:Hi-Templar
 * dateTime:2016/10/5 11:07
 * describe:
 */
public class GuideDetailCommentAdapter extends RecyclerView.Adapter<GuideDetailCommentAdapter.MyViewHolder> {
    private Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_detail_comment, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView headImg;
        private RatingBar score;
        private TextView comment;

        public MyViewHolder(View itemView) {
            super(itemView);
            headImg = (RoundedImageView) itemView.findViewById(R.id.commentHead_img);
            score = (RatingBar) itemView.findViewById(R.id.commentScoreRtb);
            comment = (TextView) itemView.findViewById(R.id.comment_tv);
        }
    }
}
