package com.recipe.r.utils;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by hht on 2017/1/18.
 * 弱引用工具，用于有很占内存的Fragment或Activity不销毁而仅仅是隐藏视图，那么这些图片资源就没办法及时回收，即使是GC的时候
 */

public class WeakImageViewUtil {
    public static ImageView getImageView(ImageView imageView) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
        if (imageViewWeakReference.get() != null) {
            return imageViewWeakReference.get();
        } else {
            return imageView;
        }
    }
}
