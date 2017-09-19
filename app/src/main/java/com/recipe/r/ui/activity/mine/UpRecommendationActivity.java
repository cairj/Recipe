package com.recipe.r.ui.activity.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
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
import com.recipe.r.ui.widget.SpinerPopWindow;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NetworkUtils;
import com.recipe.r.utils.PermissionUtils;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 上传推荐菜单界面
 */
public class UpRecommendationActivity extends BaseActivity implements View.OnClickListener {
    private Context context = UpRecommendationActivity.this;
    private ActionSheetDialog dialog;
    private ArrayList<String> imagePaths;
    GridView gridGallery;
    private int columnWidth;
    private GridAdapter gridAdapter;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private Button up_recommend;
    private EditText recommendation_name;
    private EditText recommendation_Ingredients;
    private EditText et_release_recommended;
    private EditText recommendation_accessories;
    //选择数组
    private ArrayList<String> tastelist = new ArrayList<>();
    private ArrayList<String> processlist = new ArrayList<>();
    private ArrayList<String> whenlist = new ArrayList<>();
    TextView tv_value_flavor;
    TextView tv_value_technology;
    TextView tv_value_when;
    private String TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_recommendation);
        if (getIntent().getSerializableExtra("PhotoList") != null) {
            Intent intent = getIntent();
            imagePaths = (ArrayList<String>) intent.getSerializableExtra("PhotoList");
        } else {
            imagePaths = new ArrayList<String>();
        }
        initHead(R.mipmap.reset_back, "", "上传推荐", 0, "");
        initView();
        initListener();
    }

    private void initView() {
        gridGallery = (GridView) findViewById(R.id.gridGallery_recommended);
        up_recommend = (Button) findViewById(R.id.up_recommend);
        recommendation_name = (EditText) findViewById(R.id.recommendation_name);
        recommendation_Ingredients = (EditText) findViewById(R.id.recommendation_Ingredients);
        et_release_recommended = (EditText) findViewById(R.id.et_release_recommended);
        recommendation_accessories = (EditText) findViewById(R.id.recommendation_accessories);
        tv_value_flavor = (TextView) findViewById(R.id.tv_value_flavor);
        tv_value_technology = (TextView) findViewById(R.id.tv_value_technology);
        tv_value_when = (TextView) findViewById(R.id.tv_value_when);
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
                startActivityForResult(intent, ConfigApp.REQUEST_PREVIEW_CODE);
            }
        });
        loadPhoto();//加载选择图片
        initSpiner();
    }


    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpRecommendationActivity.this.finish();
            }
        });
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpRecommendationActivity.this.finish();
            }
        });
        gridAdapter.setOnClickListener(new GridAdapter.AddItemPhoto() {
            @Override
            public void addPhoto() {
                if (ContextCompat.checkSelfPermission(UpRecommendationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpRecommendationActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    if (PermissionUtils.isCameraPermission(UpRecommendationActivity.this, 0x007))
                        showPhotoDialog();
                }
            }
        });
        up_recommend.setOnClickListener(this);
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

    private void loadPhoto() {
        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths, UpRecommendationActivity.this, columnWidth);
            gridGallery.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private SpinerPopWindow mSpinerPopWindow;
    private SpinerPopWindow mSpinerPopWindow2;
    private SpinerPopWindow mSpinerPopWindow3;


    /**
     * 初始化选项列表
     */
    private void initSpiner() {
        String[] taste_array = getResources().getStringArray(R.array.taste_array);
        String[] process_array = getResources().getStringArray(R.array.process_array);
        String[] when_array = getResources().getStringArray(R.array.when_aray);
        for (int i = 0; i < taste_array.length; i++) {
            tastelist.add(taste_array[i]);
        }
        for (int i = 0; i < process_array.length; i++) {
            processlist.add(process_array[i]);
        }
        for (int i = 0; i < when_array.length; i++) {
            whenlist.add(when_array[i]);
        }
        tv_value_flavor.setOnClickListener(clickListener);
        mSpinerPopWindow = new SpinerPopWindow<String>(this, tastelist, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
        tv_value_technology.setOnClickListener(clickListener);
        mSpinerPopWindow2 = new SpinerPopWindow<String>(this, processlist, itemClickListener);
        mSpinerPopWindow2.setOnDismissListener(dismissListener);
        tv_value_when.setOnClickListener(clickListener);
        mSpinerPopWindow3 = new SpinerPopWindow<String>(this, whenlist, itemClickListener);
        mSpinerPopWindow3.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.mipmap.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (TYPE.equals("0")) {
                mSpinerPopWindow.dismiss();
                tv_value_flavor.setText(tastelist.get(position));
            } else if (TYPE.equals("1")) {
                mSpinerPopWindow2.dismiss();
                tv_value_technology.setText(processlist.get(position));
            } else if (TYPE.equals("2")) {
                mSpinerPopWindow3.dismiss();
                tv_value_when.setText(whenlist.get(position));
            }
        }
    };

    /**
     * 显示PopupWindow
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_value_flavor:
                    TYPE = "0";
                    mSpinerPopWindow.setWidth(tv_value_flavor.getWidth());
                    mSpinerPopWindow.setHeight(320);
                    mSpinerPopWindow.showAsDropDown(tv_value_flavor);
                    setTextImage(R.mipmap.icon_down);
                    break;
                case R.id.tv_value_technology:
                    TYPE = "1";
                    mSpinerPopWindow2.setWidth(tv_value_technology.getWidth());
                    mSpinerPopWindow2.setHeight(320);
                    mSpinerPopWindow2.showAsDropDown(tv_value_technology);
                    setTextImage(R.mipmap.icon_down);
                    break;
                case R.id.tv_value_when:
                    TYPE = "2";
                    mSpinerPopWindow3.setWidth(tv_value_when.getWidth());
                    mSpinerPopWindow3.setHeight(320);
                    mSpinerPopWindow3.showAsDropDown(tv_value_when);
                    setTextImage(R.mipmap.icon_down);
                    break;
            }
        }
    };

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tv_value_flavor.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case ConfigApp.REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case ConfigApp.REQUEST_PREVIEW_CODE:
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
            gridAdapter = new GridAdapter(imagePaths, UpRecommendationActivity.this, columnWidth);
            gridGallery.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private void MuiltPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(1); // 最多选择照片数量，默认为9
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
     * 先上传图片，在发布上传推荐菜
     */
    private void UpImage() {
        if (NetworkUtils.isAvailable(this)) {
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
                            try {
                                int status = response.getInt("status");
                                String info = response.getString("info");
                                ToastUtil.show(UpRecommendationActivity.this, info, 500);
                                if (status == 1) {
                                    ArrayList<String> file_img = new ArrayList<String>();
                                    JSONObject data = response.getJSONObject("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        file_img.add(data.getString("image" + i));
                                    }
                                    AddRecommend(file_img);
//                                    //TODO 新界面，跳转编辑菜谱
//                                    Intent intent = new Intent(UpRecommendationActivity.this, EditRecommendActiv.class);
//                                    intent.putExtra("goods_image", file_img.get(0));
//                                    intent.putExtra("goods_name", recommendation_name.getText().toString());
//                                    intent.putExtra("goods_summary",et_release_recommended.getText().toString());
//                                    intent.putExtra("main_material",recommendation_Ingredients.getText().toString());
//                                    intent.putExtra("sub_material",  recommendation_accessories.getText().toString());
//                                    intent.putExtra("taste", tv_value_flavor.getText().toString());
//                                    intent.putExtra("method", tv_value_technology.getText().toString());
//                                    intent.putExtra("make_time",  tv_value_when.getText().toString());
//                                    startActivity(intent);
//                                    UpRecommendationActivity.this.finish();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_recommend:
                //发布
                if (!chkEditText(recommendation_name)) {
                    ToastUtil.show(UpRecommendationActivity.this, "请输入菜品名称", 100);
                    return;
                }
                if (!chkEditText(recommendation_Ingredients)) {
                    ToastUtil.show(UpRecommendationActivity.this, "请输入食材", 100);
                    return;
                }
                if (!chkEditText(et_release_recommended)) {
                    ToastUtil.show(UpRecommendationActivity.this, "请输入菜品说明", 100);
                    return;
                }
                if (!chkEditText(recommendation_accessories)) {
                    ToastUtil.show(UpRecommendationActivity.this, "请输入辅料说明", 100);
                    return;
                }
                if (tv_value_flavor.getText().toString().equals("口味")) {
                    ToastUtil.show(UpRecommendationActivity.this, "请选择口味", 100);
                    return;
                }
                if (tv_value_technology.getText().toString().equals("工艺")) {
                    ToastUtil.show(UpRecommendationActivity.this, "请选择工艺", 100);
                    return;
                }
                if (tv_value_when.getText().toString().equals("用时")) {
                    ToastUtil.show(UpRecommendationActivity.this, "请选择用时", 100);
                    return;
                }
                UpImage();
                break;
        }
    }

    /**
     * 发布上传推荐菜方法
     */
    private void AddRecommend(ArrayList<String> file_img) {
        showProgress();
        String url = Config.URL + Config.GOODSIMPORT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> goods_info = new HashMap<>();
        Gson g = new Gson();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        //拼接上传数组
        goods_info.put("goods_name", recommendation_name.getText().toString());
        goods_info.put("goods_summary", et_release_recommended.getText().toString());
        goods_info.put("main_material", recommendation_Ingredients.getText().toString());
        goods_info.put("sub_material", recommendation_accessories.getText().toString());
        goods_info.put("taste", tv_value_flavor.getText().toString());
        goods_info.put("method", tv_value_technology.getText().toString());
        goods_info.put("make_time", tv_value_when.getText().toString());//
        if (file_img.size() > 0) {
            goods_info.put("goods_image", file_img.get(0));
        }
        String jsonString = g.toJson(goods_info);
        try {
            params.put("goods_info", URLEncoder.encode(jsonString, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
                            ToastUtil.show(UpRecommendationActivity.this, info, 500);
                            if (status == 1) {
                                UpRecommendationActivity.this.finish();
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
                        Logger.e("UpRecommendationActivity", error_msg);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPhotoDialog();
            } else {
                // Permission Denied
                Toast.makeText(context, "权限被禁止", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPhotoDialog();
            } else {
                // Permission Denied
                Toast.makeText(context, "权限被禁止", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
