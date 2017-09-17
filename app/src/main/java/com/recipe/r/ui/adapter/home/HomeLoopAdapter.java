package com.recipe.r.ui.adapter.home;/**
 * 作者：Administrator on 2017/6/9 18:11
 * 功能:@描述
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.recipe.r.R;
import com.recipe.r.entity.BannerModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 2017
 * 06
 * 2017/6/9
 * wangxiaoer
 * 功能描述：
 **/
public class HomeLoopAdapter extends LoopPagerAdapter {
    private Context mContext;
    private List<BannerModel> banner;

    public HomeLoopAdapter(RollPagerView viewPager, Context context, List<BannerModel> banner) {
        super(viewPager);
        this.mContext = context;
        this.banner = banner;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(new ImageView(container.getContext()));
        ImageView target = imageViewWeakReference.get();
        target.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        target.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (target != null) {
            Glide.with(mContext).load(banner.get(position).getImageUrl())
                    .skipMemoryCache(true)//跳过内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存原始数据
                    .placeholder(R.mipmap.default_photo)
                    .error(R.mipmap.default_photo)
                    .centerCrop()
                    .into(target);
        }
        return target;
    }

    @Override
    public int getRealCount() {
        return banner == null ? 0 : banner.size();
    }
}