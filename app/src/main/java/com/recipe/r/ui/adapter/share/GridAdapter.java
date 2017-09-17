package com.recipe.r.ui.adapter.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.recipe.r.R;
import com.recipe.r.utils.ShowImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/14
 * wangxiaoer
 * 功能描述：
 **/
public class GridAdapter extends BaseAdapter {
    private ArrayList<String> listUrls;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private int columnWidth;
    public AddItemPhoto addItemPhoto_click;

    public GridAdapter(ArrayList<String> listUrls, Context context, int columnWidth) {
        this.listUrls = listUrls;
        this.context = context;
        this.columnWidth = columnWidth;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listUrls == null ? 1 : listUrls.size() + 1;//返回listiview数目加1
    }

    @Override
    public String getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_image, null);
            imageView = (ImageView) convertView.findViewById(R.id.imageView_grid);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
        if (listUrls != null && position < listUrls.size()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
            imageView.setLayoutParams(params);
            Glide.with(context)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        } else {
            // 重置ImageView宽高
            ShowImageUtils.showImageView(context, R.mipmap.add_release, "", imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItemPhoto_click.addPhoto();
                }
            });
            return convertView;
        }
    }


    public void setOnClickListener(AddItemPhoto addItemPhoto) {

        this.addItemPhoto_click = addItemPhoto;
    }


    public interface AddItemPhoto {
        void addPhoto();
    }
}
