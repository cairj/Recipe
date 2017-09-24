package com.recipe.r.ui.adapter.share;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView.ScaleType;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.entity.ImageBean;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.WeakImageViewUtil;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * @功能：相册Gallery适配器adapter
 * @作者
 * @时间：2015年6月18日 下午5:09:12
 */
@SuppressWarnings("deprecation")
public class PhotoViewAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageBean> list;
    private ArrayList<PhotoView> viewList;

    public PhotoViewAdapter(Context context) {
        this.context = context;
        if (list == null) {
            list = new ArrayList<ImageBean>();
        }
        viewList=new ArrayList<>();

    }

    public void updateList(ArrayList<ImageBean> list) {
        this.list = list;
        for (int i=0;i<list.size();i++){
            PhotoView photoView = new PhotoView(context);
            photoView.setAdjustViewBounds(true);
            photoView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            // photoView.setScaleType(ScaleType.FIT_XY);
            photoView.setScaleType(ScaleType.FIT_CENTER);
            ShowImageUtils.showImageView(context, R.mipmap.default_photo, Config.IMAGE_URL + list.get(i).getImageUrl(), WeakImageViewUtil.getImageView(photoView));
            viewList.add(photoView);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return list.size();
    }

    public ImageBean getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // @Override
    // public View getView(int position, View view, ViewGroup parent) {
    // PhotoView photoView = new PhotoView(context);
    // photoView.setAdjustViewBounds(true);
    // photoView.setLayoutParams(new Gallery.LayoutParams(
    // LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    // // photoView.setScaleType(ScaleType.FIT_XY);
    // photoView.setScaleType(ScaleType.FIT_CENTER);
    // ImageLoaderUtil.getInstance().displayImageWithOutCache(
    // list.get(position), photoView);
    // container.addView(photoView, LayoutParams.MATCH_PARENT,
    // LayoutParams.MATCH_PARENT);
    // return photoView;
    // }
    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView= viewList.get(position);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener!=null){
                    itemOnClickListener.click();
                }
            }
        });

        container.setBackgroundColor(context.getResources().getColor(
                R.color.black));
        container.addView(photoView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        return photoView;
    }

    private ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener){
        this.itemOnClickListener=itemOnClickListener;
    }

    public interface ItemOnClickListener{
        void click();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
