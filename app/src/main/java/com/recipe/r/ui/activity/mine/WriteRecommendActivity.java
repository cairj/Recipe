package com.recipe.r.ui.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.dialog.ActionSheetDialog;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NetworkUtils;
import com.recipe.r.utils.PermissionUtils;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 编辑菜谱界面的详情
 */
public class WriteRecommendActivity extends BaseActivity {
    private EditText et_release_recommend;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private ActionSheetDialog dialog;
    private final int resultCode = 1008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_recommend);
        initHead(R.mipmap.reset_back, "返回", "编辑步骤", 0, "保存");
        initView();
        initListener();
    }

    private void initView() {
        et_release_recommend = (EditText) findViewById(R.id.et_release_recommend);
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteRecommendActivity.this.finish();
            }
        });
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 保存
                if (!chkEditText(et_release_recommend)) {
                    ToastUtil.show(context, getString(R.string.no_add_recommen), 100);
                    return;
                }
                if (imagePaths.size() == 0) {
                    ToastUtil.show(context, getString(R.string.no_selector_imaghe), 100);
                    return;
                }
                UpImage();
            }
        });
        findViewById(R.id.add_recommend_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WriteRecommendActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    if (PermissionUtils.isCameraPermission(WriteRecommendActivity.this, 0x007))
                        showPhotoDialog();
                }
            }
        });
    }

    //拍照弹窗
    private void showPhotoDialog() {
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

    private void MuiltPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(9); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
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
     * 先上传图片，在发布上传推荐菜
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
                                ToastUtil.show(WriteRecommendActivity.this, info, 500);
                                if (status == 1) {
                                    ArrayList<String> file_img = new ArrayList<String>();
                                    JSONObject data = response.getJSONObject("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        file_img.add(data.getString("image" + i));
                                    }
                                    Intent mIntent = new Intent();
                                    mIntent.putExtra("file_img", file_img.get(0));
                                    mIntent.putExtra("recommend_tv", et_release_recommend.getText().toString());
                                    // 设置结果，并进行传送
                                    setResult(resultCode, mIntent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Logger.e("WriteRecommendActivity", error_msg);
                        }
                    });
        }
    }
}
