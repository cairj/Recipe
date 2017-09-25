package com.recipe.r.ui.activity.share;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.recipe.r.entity.ImageBean;
import com.recipe.r.ui.adapter.share.PhotoViewAdapter;
import com.recipe.r.ui.widget.HackyViewPager;
import com.recipe.r.utils.Logger;

import java.util.ArrayList;

public class PhotoGalleryActivity extends Activity {

    private Activity activity = PhotoGalleryActivity.this;
    private ViewPager mViewPager;
    private PhotoViewAdapter adapter;
    private ArrayList<ImageBean> list;
    private int position;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是4.4及以上版本、
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        // setContentView(R.layout.activity_photo_gallery);
        position = getIntent().getExtras().getInt("position");
        list = (ArrayList<ImageBean>) getIntent().getExtras().getSerializable(
                "key");
        for (int i = 0; i < list.size(); i++) {
            Logger.e("tupian ", list.get(i).toString());
        }
        initView();
    }

    private void initView() {
        mViewPager = new HackyViewPager(this);
        setContentView(mViewPager);
        adapter = new PhotoViewAdapter(activity);
        // adapter = new PhotoViewAdapter(activity);
        adapter.updateList(list);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        adapter.setItemOnClickListener(new PhotoViewAdapter.ItemOnClickListener() {
            @Override
            public void click() {
                finish();
            }
        });
        // gallery = (GuideGallery) this.findViewById(R.id.gallery_photos);
        // gallery.setAdapter(adapter);
        // gallery.setOnItemClickListener(new OnItemClickListener() {
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // PhotoGalleryActivity.this.finish();
        // }
        // });

    }
}
