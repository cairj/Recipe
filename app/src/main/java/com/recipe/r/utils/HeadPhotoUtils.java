package com.recipe.r.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.recipe.r.base.ConfigApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hht on 2016/9/22.
 */

public class HeadPhotoUtils {
    private static Activity context;
    public static String path;//图片路径
    //    private static File file;//文件名
    private CheckPermissionUtils checkPermissionUtils;
    //保存剪裁图片本地的路径
    private static final String IMGPATH = Environment.getExternalStorageDirectory
            ().getPath();
    private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";

    public HeadPhotoUtils(Activity context) {
        this.context = context;
        checkPermissionUtils = new CheckPermissionUtils(context);
    }

    /**
     * 本地
     */
    public void toGetLocalImage(int requestCode) {// ACTION_GET_CONTENT
        if (checkPermissionUtils.checkExternalStorage(ConfigApp.REQUEST_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            context.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(context, "请开启相关存储权限，以更方便的使用相关服务", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 相册选择之后的数据处理
     *
     * @param data
     */
    public void LocalImageData(Intent data) {
        Uri url = data.getData();
        startPhotoZoom(url);
        // 这里开始的第二部分，获取图片的路径：
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(url, proj, null,
                null, null);
        if (cursor != null && cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(column_index);// 图片在的路径
            }
        } else {
            path = MiPictureHelper.getPath(context, url);
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                storageDir      /* 文件夹 */
        );
        path = image.getAbsolutePath();
        return image;
    }

    /**
     * 照相
     */
    public void toGetCameraImage(int requestCode) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (checkPermissionUtils.checkSingleCameraPermission(ConfigApp.REQUEST_CAMERA)) {
                if (checkPermissionUtils.checkExternalStorage(ConfigApp.REQUEST_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {//判断是否有相机应用
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();//创建临时图片文件
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//7.0
                                context.startActivityForResult(intent, requestCode);
                            } else {
                                //FileProvider 是一个特殊的 ContentProvider 的子类，
                                //它使用 content:// Uri 代替了 file:/// Uri. ，更便利而且安全的为另一个app分享文件
                                Uri photoURI = FileProvider.getUriForFile(context,
                                        "com.qilooecshop.fileprovider",
                                        photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                context.startActivityForResult(intent, requestCode);
                            }
                        }
                    } else {
                        Toast.makeText(context, "您的手机没有相机应用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(context, "SD卡不存在，请插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拍照之后的数据处理
     *
     * @param data
     */
    public void CameraImageData(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            File f = saveBitmap(bitmap);
            if (!f.exists()) {
                f.mkdirs();
            }
            String imagePath = f.getAbsolutePath();
            Uri.fromFile(new File(imagePath));
            startPhotoZoom(Uri.fromFile(f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将进行剪裁后的图片显示到UI界面上
     */
    public void deleteFile() {
        File ZoomFile = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
        if (ZoomFile.exists() && ZoomFile.isFile()) {
            ZoomFile.delete();
        }
    }

    /**
     * 保存方法
     *
     * @param
     */
    public File saveBitmap(Bitmap bitmap) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", 1);//aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// outputY 是裁剪图片宽高
        intent.putExtra("outputY", 200);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Uri out = Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, out);
        }
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        context.startActivityForResult(intent, ConfigApp.SAVEPIC);
    }

    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
