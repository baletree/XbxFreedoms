package com.xbx123.freedom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ServerCommentBean;
import com.xbx123.freedom.view.views.FullyLinearLayoutManager;

import java.util.List;

/**
 * Created by EricYuan on 2016/5/27.
 */
public class ServerPageCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int EMPTY_VIEW = 1;
    private static final int VIEW_PROG = 2;
    private List<ServerCommentBean> commentList;
    private Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 1;
    private boolean loading = false;

    public ServerPageCommentAdapter(Context context, List<ServerCommentBean> commentList,RecyclerView recyclerView) {
        this.context = context;
        this.commentList = commentList;
        if (recyclerView.getLayoutManager() instanceof FullyLinearLayoutManager) {
            final FullyLinearLayoutManager fLinearLayoutM = (FullyLinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = fLinearLayoutM.getItemCount();
                    lastVisibleItem = fLinearLayoutM.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadingStart() {
        loading = true;
    }

    public void setLoadOnComplete() {
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_PROG) {
            return new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadmore, parent, false));
        } else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_page_comment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServerCommentBean commentBean = commentList.get(position);
        ((MyViewHolder)holder).commentTitle_tv.setText(context.getString(R.string.pageCommentName));
        ((MyViewHolder)holder).commentMsg_tv.setText(commentBean.getUserComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size() > 0 ? commentList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return commentList.get(position) != null ? super.getItemViewType(position) : VIEW_PROG;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTitle_tv;
        private TextView commentMsg_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            commentTitle_tv = (TextView) itemView.findViewById(R.id.commentTitle_tv);
            commentMsg_tv = (TextView) itemView.findViewById(R.id.commentMsg_tv);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
