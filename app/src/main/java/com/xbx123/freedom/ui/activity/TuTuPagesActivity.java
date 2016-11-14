package com.xbx123.freedom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.sp.SharePrefer;

import java.util.ArrayList;

/**
 * Created by EricYuan on 2016/6/20.
 * 途途导由引导页面
 */
public class TuTuPagesActivity extends AppCompatActivity {
    //创建一个数组，用来存放每个页面要显示的View
    private ArrayList<View> pageViews;
    //创建一个imageview类型的数组，用来表示导航小圆点
    private ImageView[] pointImages;
    //Viewpager对象
    private ViewPager viewPager;
    //导航小圆点
    private ViewGroup viewPoints;
    //导航布局装载
    private ViewGroup viewTuTuPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = this.getWindow();
        window.setFlags(flag, flag);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<>();
        pageViews.add(inflater.inflate(R.layout.item_viewpage01, null));
        pageViews.add(inflater.inflate(R.layout.item_viewpage02, null));
        pageViews.add(inflater.inflate(R.layout.item_viewpage03, null));
        pointImages = new ImageView[pageViews.size()];
        viewTuTuPage = (ViewGroup) inflater.inflate(R.layout.activity_tutu_pages, null);
        viewPager = (ViewPager) viewTuTuPage.findViewById(R.id.welcomePic_vp);
        viewPoints = (ViewGroup) viewTuTuPage.findViewById(R.id.welcomePoint_ll);
        // 添加小圆点导航的图片
        for (int i = 0; i < pageViews.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(30, 30));
            imageView.setPadding(5, 0, 5, 0);
            pointImages[i] = imageView;
            if (i == 0)
                pointImages[i].setImageResource(R.mipmap.pay_check);
            else
                pointImages[i].setImageResource(R.mipmap.pay_uncheck);
//            viewPoints.addView(pointImages[i]);
        }
        setContentView(viewTuTuPage);
        viewPager.setAdapter(new NavigationPageAdapter());
        // 为viewpager添加监听，当view发生变化时的响应
        viewPager.addOnPageChangeListener(new NavigationPageChangeListener());
    }

    // 导航图片view的适配器，必须要实现的是下面四个方法
    class NavigationPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 初始化每个Item
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        // 销毁每个Item
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }
    }

    // viewpager的监听器，主要是onPageSelected要实现
    class NavigationPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 循环主要是控制导航中每个小圆点的状态
            for (int i = 0; i < pointImages.length; i++) {
                // 当前view下设置小圆点为选中状态
                pointImages[i].setImageResource(R.mipmap.pay_check);
                // 其余设置为飞选中状态
                if (position != i)
                    pointImages[i].setImageResource(R.mipmap.pay_uncheck);
            }
        }

    }

    // 开始按钮方法，开始按钮在XML文件中onClick属性设置；
    // 我试图把按钮在本activity中实例化并设置点击监听，但总是报错，使用这个方法后没有报错，原因没找到
    public void startButton(View v) {
        SharePrefer.setFistUseState(TuTuPagesActivity.this, true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
        finish();
    }
}
