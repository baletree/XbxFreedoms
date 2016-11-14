package com.xbx123.freedom.linsener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xbx123.freedom.R;

/**
 * Created by EricYuan on 2016/4/14.
 */
public class ImageLoaderConfigFactory {
    private DisplayImageOptions options;
    private static ImageLoaderConfigFactory factory;

    private ImageLoaderConfigFactory(){

    }

    public static ImageLoaderConfigFactory getInstance() {
        if(factory == null) {
            synchronized (ImageLoaderConfigFactory.class) {
                if(factory == null) {
                    factory = new ImageLoaderConfigFactory();
                }
            }
        }
        return factory;
    }

    public DisplayImageOptions getHeadImg() {
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.mipmap.head_default)
                .showImageForEmptyUri(R.mipmap.head_default)
                .showImageOnFail(R.mipmap.head_default)
                .cacheInMemory(true)
                .build();
        return options;
    }
    public DisplayImageOptions getAlbumSquareImg() {
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.drawable.gray_rectangle_bg)
                .showImageForEmptyUri(R.drawable.gray_rectangle_bg)
                .showImageOnFail(R.drawable.gray_rectangle_bg)
                .cacheInMemory(true)
                .build();
        return options;
    }
}
