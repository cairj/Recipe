package com.recipe.r.gildeutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.target.Target;

/**
 * 2017
 * 06
 * 2017/6/8
 * wangxiaoer
 * 功能描述：高斯模糊图片处理
 **/
public class BlurTransformation implements Transformation<Bitmap> {

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private Context mContext;
    private BitmapPool mBitmapPool;

    private int mRadius;
    private int mSampling;

    public BlurTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS,
                DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, BitmapPool pool) {
        this(context, pool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius) {
        this(context, pool, radius, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, int radius) {
        this(context, Glide.get(context).getBitmapPool(), radius,
                DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius,
                              int sampling) {
        mContext = context;
        mBitmapPool = pool;
        mRadius = radius;
        mSampling = sampling;
    }

    public BlurTransformation(Context context, int radius, int sampling) {
        mContext = context;
        mBitmapPool = Glide.get(context).getBitmapPool();
        mRadius = radius;
        mSampling = sampling;
    }


    /**
     * Transforms the given resource and returns the transformed resource.
     * <p>
     * <p>
     * Note - If the original resource object is not returned, the original resource will be recycled and it's
     * internal resources may be reused. This means it is not safe to rely on the original resource or any internal
     * state of the original resource in any new resource that is created. Usually this shouldn't occur, but if
     * absolutely necessary either the original resource object can be returned with modified internal state, or
     * the data in the original resource can be copied into the transformed resource.
     * </p>
     *
     * @param resource  The resource to transform.
     * @param outWidth  The width of the view or target the resource will be displayed in, or
     *                  {@link Target#SIZE_ORIGINAL} to indicate the original
     *                  resource width.
     * @param outHeight The height of the view or target the resource will be displayed in, or
     *                  {@link Target#SIZE_ORIGINAL} to indicate the original
     *                  resource height.
     * @return The transformed resource.
     */
    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();
        int scaledWidth = width / mSampling;
        int scaledHeight = height / mSampling;

        Bitmap bitmap = mBitmapPool.get(scaledWidth, scaledHeight,
                Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight,
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius);
            } catch (RSRuntimeException e) {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }
        } else {
            bitmap = FastBlur.blur(bitmap, mRadius, true);
        }

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    public String getId() {
        return "BlurTransformation(radius=" + mRadius + ", sampling="
                + mSampling + ")";
    }


}