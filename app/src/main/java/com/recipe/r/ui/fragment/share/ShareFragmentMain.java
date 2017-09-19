package com.recipe.r.ui.fragment.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.BannerModel;
import com.recipe.r.entity.ShareItem;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.share.CommentsCollegeActivity;
import com.recipe.r.ui.activity.share.ReleaseShareActivity;
import com.recipe.r.ui.adapter.share.ShareRecyclerViewAdapter;
import com.recipe.r.ui.dialog.ActionSheetDialog;
import com.recipe.r.ui.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.recipe.r.ui.dialog.ActionSheetDialog.SheetItemColor;
import com.recipe.r.ui.fragment.base.BaseFragment;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.PermissionUtils;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * 2017/6/9
 * wangxiaoer
 * 功能描述：分享界面
 **/
@SuppressLint("ValidFragment")
public class ShareFragmentMain extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private Activity context;
    private ActionSheetDialog dialog;
    private RecyclerView mRecyclerView;
    ArrayList<ShareItem.Sharedata> shareItemList = null;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ShareRecyclerViewAdapter adapter;
    private ImageView no_login_iv;
    private LinearLayout swipeToLoadLayout_ll;

    public ShareFragmentMain(Activity mainActivity) {
        this.context = mainActivity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMyOkhttp == null) {
            mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        }
        View view = inflater.inflate(R.layout.fragment_share_main, container, false);
        initHead(view, 0, "", "美食分享", R.mipmap.camear, "");
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView(View view) {
        no_login_iv = (ImageView) view.findViewById(R.id.no_login_iv);
//        if (UserIsLogin.isShowLogin(context)) {
//            //显示
//            no_login_iv.setVisibility(View.VISIBLE);
//        } else {
//            //不显示
        no_login_iv.setVisibility(View.GONE);
//        }
        swipeToLoadLayout_ll = (LinearLayout) view.findViewById(R.id.swipeToLoadLayout_ll);
        swipeToLoadLayout_ll.setBackgroundColor(getResources().getColor(R.color.white));
        this.swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShareRecyclerViewAdapter(context);
        mRecyclerView.setAdapter(adapter);
        initRefresh(swipeToLoadLayout, view);
    }

    private void initData() {
        getBanner();
        shareItemList = new ArrayList<>();
        getShareList(PAGE, limit);
    }

    /**
     * 获取Banner接口
     */
    private void getBanner() {
        String url = Config.URL + Config.GETBANNER;
        Map<String, String> params = new HashMap<>();
        params.put("type", "share");
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("error", "statusCode" + statusCode + "error_msg" + error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {
                                /*JSONObject data = response.getJSONObject("data");
                                JSONArray banner_Images = data.getJSONArray("images");
                                for (int i = 0; i < banner_Images.length(); i++) {

                                    adapter.setBanner(Config.IMAGE_URL + banner_Images.get(i), 1);
                                }*/
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    BannerModel bannerModel = new BannerModel();
                                    bannerModel.setTips("" + i);
                                    bannerModel.setImageUrl(Config.IMAGE_URL + data.getJSONObject(i).getString("thumb"));
                                    bannerModel.setHref(Config.IMAGE_URL + data.getJSONObject(i).getString("href"));
                                    adapter.setBanner(bannerModel.getImageUrl(), 1);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //获取分享列表数据
    private void getShareList(int page, int limit) {
        showProgress();
        String url = Config.URL + Config.GETSHARELIST;
        Map<String, String> params = new HashMap<>();
        params.put("type", "0");
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        params.put("option", "");
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<ShareItem>() {


                             @Override
                             public void onFailure(int statusCode, String error_msg) {
                                 hideProgress();
                             }

                             @Override
                             public void onSuccess(int statusCode, ShareItem response) {
                                 hideProgress();
                                 int status = response.getStatus();
                                 String info = response.getInfo();

                                 if (status == 1) {
                                     shareItemList = response.getData();
                                     loadShareItem(shareItemList);
                                 }
                             }
                         }
                );
    }

    private void initListener() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getRightView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConfigApp.MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    if (PermissionUtils.isCameraPermission(context, 0x007))
                        showPhotoDialog();
                }
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        no_login_iv.setOnClickListener(this);
        adapter.setOnItemClickListener(new ShareRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ShareItem.Sharedata data) {
                // 評論
                Intent intent = new Intent(context,
                        CommentsCollegeActivity.class);
                intent.putExtra("news_id", data.getNews_id());
                context.startActivity(intent);
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

    //获取展示数据,加载分享圈数据
    private void loadShareItem(final ArrayList<ShareItem.Sharedata> items) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    adapter.updatelist(items);
                } else {
                    adapter.append(items);
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
                .addSheetItem(context.getString(R.string.selector_album), SheetItemColor.Blue,
                        new OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                MuiltPhoto();
                                dialog.dissmiss();
                            }
                        })
                .addSheetItem(context.getString(R.string.taking_pictures), SheetItemColor.Blue,
                        new OnSheetItemClickListener() {
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

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getShareList(PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getShareList(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Intent intent = new Intent(context, ReleaseShareActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("PhotoList", imagePaths);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_login_iv:
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
        }
    }
}
