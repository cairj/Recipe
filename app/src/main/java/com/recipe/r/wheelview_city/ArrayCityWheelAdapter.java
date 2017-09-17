package com.recipe.r.wheelview_city;

import android.content.Context;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ArrayCityWheelAdapter<T> extends AbstractWheelTextAdapter {
    // items
    private T items[];

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ArrayCityWheelAdapter(Context context, T items[]) {
        super(context);
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            T item = items[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
}
