package com.xbx123.freedom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx123.freedom.R;
import com.xbx123.freedom.linsener.AnimateFirstDisplayListener;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.ui.activity.BrowserPictureActivity;

import java.io.Serializable;
import java.util.List;

/**
 * project:XbxFreedom
 * author:Hi-Templar
 * dateTime:2016/10/5 11:07
 * describe:
 */
public class GuideDetailAlbumAdapter extends RecyclerView.Adapter<GuideDetailAlbumAdapter.MyViewHolder> {
    private Context context;
    private List<String> listAlbum;//图片地址列表
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    public GuideDetailAlbumAdapter(Context context, List<String> listAlbum, ImageLoader imageLoader, ImageLoaderConfigFactory configFactory) {
        this.context = context;
        this.listAlbum = listAlbum;
        this.imageLoader = imageLoader;
        this.configFactory = configFactory;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_detail_album, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position == 0)
            holder.item_Albumdivider.setVisibility(View.GONE);
        imageLoader.displayImage(listAlbum.get(position), holder.img, configFactory.getAlbumSquareImg(), new AnimateFirstDisplayListener());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BrowserPictureActivity.class);
                intent.putExtra("clickPosition", position);
                intent.putExtra("PictureUrlsList", (Serializable) listAlbum);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAlbum.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private View item_Albumdivider;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.albumImg);
            item_Albumdivider = itemView.findViewById(R.id.item_Albumdivider);
        }
    }
}
