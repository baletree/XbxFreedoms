package com.xbx123.freedom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xbx123.freedom.R;
import com.xbx123.freedom.linsener.ImageLoaderConfigFactory;
import com.xbx123.freedom.ui.activity.BrowserPictureActivity;
import com.xbx123.freedom.utils.tool.Util;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by EricYuan on 2016/10/20.
 */

public class BrowserPictureAdapter extends PagerAdapter {
    private BrowserPictureActivity context;
    private List<String> picUrlList;
    private ImageLoader imageLoader;

    public BrowserPictureAdapter(BrowserPictureActivity context, List<String> picUrlList, ImageLoader imageLoader) {
        this.context = context;
        this.picUrlList = picUrlList;
        this.imageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return picUrlList == null ? 0 : picUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.item_pager_image, view, false);
        assert imageLayout != null;
        PhotoView img = (PhotoView) imageLayout.findViewById(R.id.itemBrowserImage);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.itemBrowserLoading);
        imageLoader.displayImage(picUrlList.get(position), img,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        spinner.setVisibility(View.GONE);
                    }
                });
        view.addView(imageLayout, 0);
        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                context.finish();
            }
        });
        return imageLayout;
    }
}
