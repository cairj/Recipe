package com.recipe.r.ui.activity.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.entity.Comment;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.adapter.share.CommentAdapter;
import com.recipe.r.ui.dialog.ActionSheetDialog;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.Logger;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 评论界面
 */
public class CommentsCollegeActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private String NEWID = "";
    private String TAG = "CommentsCollegeActivity";
    private Context mContext = CommentsCollegeActivity.this;
    PopupWindow pop;
    private View view;
    public SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private CommentAdapter adapter;
    private int PAGE = 1;
    private int limit = 10;
    private boolean isRefresh = true;
    private ArrayList<Comment.Item> mDatas = null;

    private ActionSheetDialog dialog;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    public boolean isAddReply = false;
    private String comment_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("news_id"))) {
            NEWID = getIntent().getStringExtra("news_id");
        }
        setContentView(R.layout.activity_comments_college);
        initHead(R.mipmap.reset_back, "返回", "", 0, "");
        initView();
        initData();
        initPopupWindow();
        initListener();
    }


    private void initView() {
        this.swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommentAdapter(CommentsCollegeActivity.this);
        mRecyclerView.setAdapter(adapter);

    }

    private void initData() {
        mDatas = new ArrayList<>();
        getCommentsDetails(0);
        getComment(PAGE, limit);
    }

    /**
     * 自定义的评论弹窗 包含@评论人和发布评论 监听Edittext的输入状态
     */
    private void initPopupWindow() {
        view = this.getLayoutInflater().inflate(R.layout.pop_comments, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        TextView cancle_pop = (TextView) view.findViewById(R.id.cancle_pop);
        final EditText et_pop = (EditText) view.findViewById(R.id.et_pop);
        RelativeLayout pop_head = (RelativeLayout) view
                .findViewById(R.id.pop_head);
        final TextView published = (TextView) view.findViewById(R.id.published);
        pop.setFocusable(true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        cancle_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pop.setText("");
                pop.dismiss();
                et_pop.clearFocus();
            }
        });
        pop_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                et_pop.setText("");
                pop.dismiss();
                et_pop.clearFocus();
            }
        });
        et_pop.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_pop.getText().length() == 0) {
                    published.setTextColor(getResources().getColor(
                            R.color.gray_home));
                    published.setFocusable(false);
                } else {
                    published.setTextColor(getResources().getColor(
                            R.color.black_gray));
                    published.setFocusable(true);
                    published.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            if (!chkEditText(et_pop)) {
                                Toast.makeText(mContext, "未输入内容",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String str = stringFilter(et_pop.getText().toString());
                            if (!(et_pop.getText().toString())
                                    .equals(str)) {
                                Toast.makeText(mContext, "内容携带特殊字符",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (isAddReply) {
                                setAddReply(str, comment_id);
                            } else {
                                setPublished(str);
                            }
                            et_pop.setText("");// 清空
                            pop.dismiss();
                            et_pop.clearFocus();
                        }
//                            }).show();
//                        }
                    });
                }
            }
        });
    }

    private void initListener() {
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentsCollegeActivity.this.finish();
            }
        });
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new CommentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Comment.Item data) {
                pop.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                isAddReply = true;
                comment_id = data.getComment_id();
            }
        });
    }

    /**
     * 获取详情
     */
    private void getCommentsDetails(final int type) {
        String url = Config.URL + Config.GETINFORMATIONDETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("news_id", NEWID);
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            String info = response.getString("info");

                            if (status == 1) {
                                JSONObject data = response.getJSONObject("data");
//                                if (type != 1) {
                                adapter.setContent(data, NEWID);
//
//                                } else {
//                                    zan_share.setText("赞" + data.getString("digg_num"));
//                                }
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

                    }
                });
    }

    /**
     * 获取评论
     */
    private void getComment(int page, int limit) {
        String url = Config.URL + Config.GETCOMMENTLISTS;
        Map<String, String> params = new HashMap<>();
        params.put("type", "0");
        params.put("news_id", NEWID);
        params.put("page", "" + page);
        params.put("limit", "" + limit);
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<Comment>() {

                    @Override
                    public void onSuccess(int statusCode, Comment response) {
                        Log.e("onSuccess",response.toString());
                        int status = response.getStatus();
                        String info = response.getInfo();
                        if (status == 1) {
                            mDatas = response.getData();
                            loadCommentItem(1, mDatas);
                        }
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 发布评论
     */
    private void setPublished(String content) {
        showProgress();
        String url = Config.URL + Config.ADDCOMMENT;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("news_id", NEWID);
        params.put("content", content);
        mMyOkhttp.post()
                .url(url)
                .params(params)
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
                            if (status == 1) {
                                //刷新
                                swipeToLoadLayout.setRefreshing(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    /**
     * 发布评论的评论
     */
    private void setAddReply(String content, String comment_id) {
        showProgress();
        String url = Config.URL + Config.ADDREPLY;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device","android");
        params.put("comment_id", comment_id);
        params.put("content", content);
        mMyOkhttp.post()
                .url(url)
                .params(params)
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
                            if (status == 1) {
                                //刷新
                                swipeToLoadLayout.setRefreshing(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.e("LoginActivity", error_msg);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }


    //获取展示数据
    private void loadCommentItem(int type, final ArrayList<Comment.Item> items) {
        runOnUiThread(new Runnable() {
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

    @Override
    public void onRefresh() {
        isRefresh = true;
        PAGE = 1;
        getComment(PAGE, limit);
        //设置下拉刷新结束
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        PAGE++;
        getComment(PAGE, limit);
        swipeToLoadLayout.setLoadingMore(false);
    }

    //拍照弹窗
    public void showPhotoDialog() {
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

}
