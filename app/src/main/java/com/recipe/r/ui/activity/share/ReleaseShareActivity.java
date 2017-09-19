package com.recipe.r.ui.activity.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.google.gson.Gson;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.share.GridAdapter;
import com.recipe.r.ui.dialog.ActionSheetDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NetworkUtils;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.recipe.r.base.ConfigApp.REQUEST_CAMERA_CODE;
import static com.recipe.r.base.ConfigApp.REQUEST_PREVIEW_CODE;

/**
 * 分享圈发布界面
 */
public class ReleaseShareActivity extends BaseActivity {
    private Context context = ReleaseShareActivity.this;
    private ActionSheetDialog dialog;
    private ArrayList<String> imagePaths;
    GridView gridGallery;
    private int columnWidth;
    private GridAdapter gridAdapter;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private EditText et_release_share;
    private EditText title_release_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_share);
        if (getIntent().getSerializableExtra("PhotoList") != null) {
            Intent intent = getIntent();
            imagePaths = (ArrayList<String>) intent.getSerializableExtra("PhotoList");
        } else {
            imagePaths = new ArrayList<String>();
        }
        initHead(R.mipmap.reset_back, "", "美食分享", 0, "发布");
        initView();
        initListener();
    }


    private void initView() {
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        et_release_share = (EditText) findViewById(R.id.et_release_share);
        title_release_share = (EditText) findViewById(R.id.title_release_share);
        gridGallery.setFastScrollEnabled(true);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridGallery.setNumColumns(cols);
        // Item Width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;
        // preview
        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(context);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });
        loadPhoto();//加载选择图片
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseShareActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布
                if (!chkEditText(et_release_share)) {
                    ToastUtil.show(ReleaseShareActivity.this, "请输入发布内容", 100);
                    return;
                }
                if (!chkEditText(title_release_share)) {
                    ToastUtil.show(ReleaseShareActivity.this, "请输入标题", 100);
                    return;
                }
                UpImage();

            }
        });
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseShareActivity.this.finish();
            }
        });
        gridAdapter.setOnClickListener(new GridAdapter.AddItemPhoto() {
            @Override
            public void addPhoto() {
                showDialog();
            }
        });
    }


    //拍照弹窗
    private void showDialog() {
        dialog = new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem(context.getString(R.string.selector_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                MuiltPhoto();
                                dialog.dissmiss();
                            }
                        })
                .addSheetItem(context.getString(R.string.taking_pictures), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                CaremaPhoto();
                                dialog.dissmiss();
                            }

                        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void loadPhoto() {
        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths, ReleaseShareActivity.this, columnWidth);
            gridGallery.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    break;

            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);

        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths, ReleaseShareActivity.this, columnWidth);
            gridGallery.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private void MuiltPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(9); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
//                ImageConfig config = new ImageConfig();
//                config.minHeight = 400;
//                config.minWidth = 400;
//                config.mimeType = new String[]{"image/jpeg", "image/png"};
//                config.minSize = 1 * 1024 * 1024; // 1Mb
//                intent.setImageConfig(config);
        startActivityForResult(intent, ConfigApp.REQUEST_CAMERA_CODE);
    }

    private void CaremaPhoto() {
        try {
            if (captureManager == null) {
                captureManager = new ImageCaptureManager(context);
            }
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(context, com.foamtrace.photopicker.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 先上传图片，在发布分享
     */
    private void UpImage() {
        if (NetworkUtils.isAvailable(this)) {
            showProgress();
            String url = Config.URL + Config.UPLOADIMAGE;
            Map<String, File> fileMap = new HashMap<>();
            for (int i = 0; i < imagePaths.size(); i++) {
                File f = new File(imagePaths.get(i));
                if (f != null) {
                    fileMap.put("image" + i, f);
                }
            }
            mMyOkhttp.upload()
                    .addHeader("enctype", "multipart/from-data")
                    .url(url)
                    .files(fileMap)
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {

                        @Override
                        public void onProgress(long currentBytes, long totalBytes) {
                            super.onProgress(currentBytes, totalBytes);
                        }

                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            hideProgress();
                            try {
                                int status = response.getInt("status");
                                String info = response.getString("info");
                                ToastUtil.show(ReleaseShareActivity.this, info, 500);
                                if (status == 1) {
                                    ArrayList<String> file_img = new ArrayList<String>();
                                    JSONObject data = response.getJSONObject("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        file_img.add(data.getString("image" + i));
                                    }
                                    AddShare(file_img);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Logger.e("ReleaseShareActivity", error_msg);
                        }
                    });
        }
    }

    /**
     * 发布分享接口
     */
    private void AddShare(ArrayList<String> file_img) {
        String url = Config.URL + Config.ADDSHARE;
        //请求参数数组
        Map<String, String> params = new HashMap<>();
        Gson g = new Gson();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("title", title_release_share.getText().toString());
        params.put("summary", et_release_share.getText().toString());
        String jsonString = g.toJson(file_img);
        if (imagePaths.size() != 0) {
            try {
                params.put("content", URLEncoder.encode(jsonString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        mMyOkhttp.upload()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        int status = 0;
                        try {
                            status = response.getInt("status");
                            String info = response.getString("info");
                            ToastUtil.show(ReleaseShareActivity.this, info, 500);
                            if (status == 1) {
                                ReleaseShareActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        hideProgress();
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}
