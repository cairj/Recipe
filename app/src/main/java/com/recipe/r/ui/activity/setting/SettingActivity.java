package com.recipe.r.ui.activity.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.recipe.r.R;
import com.recipe.r.base.Config;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.activity.address.AdressActivity;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.login.NewLoginActivity;
import com.recipe.r.ui.activity.login.ResetPassworldActivity;
import com.recipe.r.ui.activity.mine.AboutActivity;
import com.recipe.r.ui.activity.mine.AgreementActivity;
import com.recipe.r.ui.dialog.ActionSheetDialog;
import com.recipe.r.ui.dialog.AlertDialog;
import com.recipe.r.ui.dialog.AlertDialogUpdate;
import com.recipe.r.ui.dialog.MyAlertDialog;
import com.recipe.r.ui.widget.DatePickter;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.AppSettings;
import com.recipe.r.utils.CleanMessageUtil;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.ImageUtil;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.NetworkUtils;
import com.recipe.r.utils.PermissionUtils;
import com.recipe.r.utils.ShowImageUtils;
import com.recipe.r.utils.ToastUtil;
import com.recipe.r.utils.VersionManagementUtil;
import com.recipe.r.utils.WeakImageViewUtil;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView avater_set;
    private RelativeLayout rl_avater_set;
    // 下载安装包的网络路径
    private String apkUrl = "";
    private ActionSheetDialog dialog;
    private TextView nickname_set;
    private TextView birthday_set;
    private Button exit_set_btn;
    private TextView adress_set;
    private RadioButton man_gender;
    private RadioButton woman_gender;
    private MyAlertDialog myAlertDialog;
    //    private HeadPhotoUtils headPhotoUtils;
    public static final int TAKE_PICTURE = 0;
    public static final int CHOOSE_PICTURE = 1;
    public static final int CROP = 2;
    public static final int CROP_PICTURE = 3;
    private File file;
    private String fileName = null;
    private Uri imageUri;
    private Intent intent;
    final static int CAMERAPRESS = 3;
    private static final int DOWN_UPDATE = 1001;
    private static final int DOWN_OVER = 1002;
    private TextView newversion_set;
    private int progress;// 当前进度
    private Thread downLoadThread; // 下载线程
    private boolean interceptFlag = false;// 用户取消下载
    private Dialog downloadDialog;// 下载对话框
    private static final String savePath = Environment.getExternalStorageDirectory().getPath();// 保存apk的文件夹
    private static final String saveFileName = savePath + "/" + "gusinn.apk";
    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    // 通知处理刷新界面的handler
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initHead(R.mipmap.reset_back, "返回", "资料设置", 0, "");
        initView();
        initDoClick();
    }

    private void initView() {
        newversion_set = (TextView) findViewById(R.id.newversion_set);
        avater_set = (ImageView) findViewById(R.id.avater_set);
        nickname_set = (TextView) findViewById(R.id.nickname_set);
        birthday_set = (TextView) findViewById(R.id.birthday_set);
        adress_set = (TextView) findViewById(R.id.adress_set);
        exit_set_btn = (Button) findViewById(R.id.exit_set_btn);
        rl_avater_set = (RelativeLayout) findViewById(R.id.rl_avater_set);
        man_gender = (RadioButton) findViewById(R.id.man_gener);
        woman_gender = (RadioButton) findViewById(R.id.woman_gener);
        myAlertDialog = new MyAlertDialog(this, R.style.NormalAlertDialogStyle);
        newversion_set.setText(VersionManagementUtil.getVersion(SettingActivity.this));
    }

    /**
     * 刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    public void initDoClick() {
        setClickListener(R.id.rl_avater_set);
        setClickListener(R.id.adress_set_rl);
        setClickListener(R.id.resetpassworld_set_rl);
        setClickListener(R.id.birthday_set_rl);
        setClickListener(R.id.about_set_rl);
        setClickListener(R.id.agreement_set_rl);
        setClickListener(R.id.nickname_set_rl);
        setClickListener(R.id.exit_set_btn);
        setClickListener(R.id.man_gener);
        setClickListener(R.id.clear_set_rl);
        setClickListener(R.id.woman_gener);
        setClickListener(R.id.newversion_set_rl);
        getReturnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
    }

    /**
     * 设置点击效果
     */
    private void setClickListener(int id) {
        findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avater_set:
                if (Build.VERSION.SDK_INT >= 23) {
                    //android 6.0权限问题
                    if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERAPRESS);
                    } else {
                        if (PermissionUtils.isCameraPermission(SettingActivity.this, 0x007))
                            showPhotoDialog();
                    }
                } else {
                    if (PermissionUtils.isCameraPermission(SettingActivity.this, 0x007))
                        showPhotoDialog();
                }
                break;
            case R.id.nickname_set_rl:
                //昵称
                Intent intent_nickname = new Intent(SettingActivity.this, NickNameActivity.class);
                startActivity(intent_nickname);
                break;
            case R.id.about_set_rl:
                //关于
                Intent intent_about = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent_about);
                break;
            case R.id.agreement_set_rl:
                //用户协议
                Intent intent_agreement = new Intent(SettingActivity.this, AgreementActivity.class);
                startActivity(intent_agreement);
                break;
            case R.id.birthday_set_rl:
                //出生日期
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy");
                String time = format.format(date);
//                myAlertDialog.threeTimepicker(getString(R.string.brith_str),
//                        getString(R.string.sure_str),
//                        getResources().getString(R.string.cancel),
//                        AppSettings.getPrefString(this, ConfigApp.BIRTHDAY, ""))
//                        .time_yes(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                myAlertDialog.dismiss();
//                                String birthday = v.getTag().toString();
//                                if (birthday.equals(AppSettings.getPrefString(SettingActivity.this, ConfigApp.BIRTHDAY, ""))) {
//                                    return;
//                                }
//                                setBirthday(birthday);
//                            }
//
//
//                        })
//                        .no(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                myAlertDialog.dismiss();
//                            }
//                        });
//                myAlertDialog.show();
                DatePickter.showDataPop((Activity) context, 1890, Integer.parseInt(time), 14, 14, "2013-11-11").setDateResultListener(new DatePickter.OnDateResultListener() {
                    @Override
                    public void getDateResult(int year, int month, int day, String dateDesc) {
                        if (dateDesc.equals(AppSettings.getPrefString(SettingActivity.this, ConfigApp.BIRTHDAY, ""))) {
                            return;
                        }
                        setBirthday(dateDesc);
                    }
                });
                break;
            case R.id.adress_set_rl:
                //设置地址
                Intent intent_adress = new Intent(SettingActivity.this, AdressActivity.class);
                startActivity(intent_adress);
                break;
            case R.id.resetpassworld_set_rl:
                //修改密码
                Intent intent_reset = new Intent(SettingActivity.this, ResetPassworldActivity.class);
                startActivity(intent_reset);
                break;
            case R.id.exit_set_btn:
                new AlertDialog(SettingActivity.this).builder()
                        .setTitle(getString(R.string.str_tip)).setMsg("是否确认退出？")
                        .setPositiveButton("确认", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //点击退出,清空缓存
//                                if (UserIsLogin.IsLogn(context)) {
//                                    //退出环信
//                                    ExitHypLogin(0);
//                                }
//                                AppSettings.setPrefString(context, ConfigApp.USERNAME, "");
//                                AppSettings.setPrefString(context, ConfigApp.ISLOGIN, "1");
//                                AppSettings.setPrefString(context, ConfigApp.USERID, "");
//                                AppSettings.setPrefString(context, ConfigApp.AVATER, "");
//                                AppSettings.setPrefString(context, ConfigApp.POSINTS, "");
                                if (TextUtils.isEmpty(AppSettings.getPrefString(context, ConfigApp.USERNAME, ""))) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    Intent intent = new Intent(context, NewLoginActivity.class);
                                    context.startActivity(intent);
                                }
                                AppManager.getInstance().killAllActivity();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

                break;
            case R.id.man_gener:
                //选男
                setUserInfo(0);
                break;
            case R.id.woman_gener:
                //选女
                setUserInfo(0);
                break;
            case R.id.clear_set_rl:
                AlertDialog dialog = new AlertDialog(context);
                dialog.builder();
                dialog.setTitle(getString(R.string.str_tip));
                dialog.setMsg(getString(R.string.is_clean));
                dialog.setPositiveButton(context.getResources().getString(R.string.sure_str), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CleanMessageUtil.clearAllCache(getApplicationContext());
                    }
                });
                dialog.setNegativeButton(context.getResources().getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.newversion_set_rl:
                //TODO 版本更新
                getVersion();
                break;
        }
    }

    /**
     * 获取版本更新号码
     * 1，android，2，iOS
     */
    private void getVersion() {
        if (NetworkUtils.isAvailable(this)) {
            showProgress();
            final String url = Config.URL + Config.GETVERSION;
            Map<String, String> params = new HashMap<>();
            params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
            params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
            params.put("device", "android");
            params.put("type", "1");
            mMyOkhttp.post()
                    .params(params)
                    .url(url)
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
                                if (status == 1) {
                                    JSONObject data = response.getJSONObject("data");
                                    String version = data.getString("version");
                                    if (!version.equals(VersionManagementUtil.getVersion(SettingActivity.this))) {
                                        //TODO 版本不一致
                                        apkUrl = data.getString("url");
                                        if (TextUtils.isEmpty(data.getString("version_info"))) {
                                            showNoticeDialog(getString(R.string.normal_version));
                                        } else {
                                            showNoticeDialog(data.getString("version_info"));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            hideProgress();
                            Logger.e("SettingActivity", error_msg);
                        }
                    });
        }
    }

    //设置生日
    private void setBirthday(String birthday) {
        birthday_set.setText(birthday);
        setUserInfo(1);
    }

    //拍照弹窗
    private void showPhotoDialog() {
        dialog = new ActionSheetDialog(context)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(context.getString(R.string.selector_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                selectPicFromLocal();
                            }
                        })
                .addSheetItem(context.getString(R.string.taking_pictures), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                selectPicFromCamera();
                            }

                        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void selectPicFromLocal() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, CROP);
    }

    private void selectPicFromCamera() {
        try {
            // 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
            // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 保存本次截图临时文件名字
            fileName = String.valueOf(System.currentTimeMillis()) + "_tmp.jpg";
            imageUri = Uri.fromFile(new File(ImageUtil.getPicCachePath(),
                    fileName));

            // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP);
        } catch (Exception e) {
            Toast.makeText(context, "操作失败~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    file = new File(imageUri.getPath());
                    upHeadImage(file);
                    break;

                case CHOOSE_PICTURE:
                    // 照片的原始资源地址
                    Uri originalUri = data.getData();
                    if (originalUri == null) {
                        return;
                    }
                    String path = null;
                    if (originalUri.getScheme().equals("content")) {
                        String[] pojo = {MediaStore.Images.Media.DATA};
                        ContentResolver cr = context.getContentResolver();
                        Cursor cursor = cr.query(originalUri, pojo, null, null,
                                null);
                        if (cursor != null) {
                            int colunm_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            path = cursor.getString(colunm_index);
                            cursor.close();
                        } else {
                            return;
                        }
                    } else {
                        path = originalUri.getPath();
                    }
                    file = new File(path);
                    upHeadImage(file);
                    break;

                case CROP:
                    Intent intent = null;
                    if (data != null) {
                        Uri srcUri = data.getData();
                        // 保存本次截图临时文件名字
                        String fileName = String.valueOf(System
                                .currentTimeMillis()) + "_tmp.jpg";
                        imageUri = Uri.fromFile(new File(ImageUtil
                                .getPicCachePath(), fileName));
                        intent = getCropImageIntent(srcUri, imageUri);
                    } else {
                        intent = getCropImageIntent(imageUri, imageUri);
                    }
                    startActivityForResult(intent, CROP_PICTURE);
                    break;

                case CROP_PICTURE:
                    if (data == null) {
                        return;
                    }
                    if (imageUri != null) {
                        Logger.e("SelectPicPopWindow", "imageUrl:" + imageUri);
                        upHeadImage(new File(imageUri.getPath()));
                    }
                    break;
                default:
                    break;
            }
//			}
        }
    }

    private Intent getCropImageIntent(Uri data, Uri outUri) {
        Intent intent = new Intent();
        if (data == null) {
            return intent;
        }
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 设置按长和宽的比例裁剪
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250); // 设置输出的大小
        intent.putExtra("outputY", 250);
        intent.putExtra("scale", true); // 设置是否允许拉伸
        // 如果要在给定的uri中获取图片，则必须设置为false，如果设置为true，那么便不会在给定的uri中获取到裁剪的图片
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//
        // 设置输出格式
        intent.putExtra("noFaceDetection", true); // 无需人脸识别 默认不需要设置
        return intent;
    }

    /**
     * 上传头像
     *
     * @param filepath
     */
    private void upHeadImage(final File filepath) {
        if (NetworkUtils.isAvailable(this)) {
            showProgress();
            final String url = Config.URL + Config.UPDATEACATAR;
            Map<String, String> params = new HashMap<>();
            params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
            params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
            params.put("device", "android");
            mMyOkhttp.upload()
                    .addHeader("enctype", "multipart/from-data")
                    .url(url)
                    .addFile("UpLoadFile", filepath)
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
//                                ToastUtil.show(SettingActivity.this, info, 500);
                                if (status == 1) {
                                    JSONObject data = response.getJSONObject("data");
                                    AppSettings.setPrefString(context, ConfigApp.AVATER, Config.IMAGE_URL + data.getString("src"));
                                    //刷新新的头像
                                    ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, AppSettings.getPrefString(SettingActivity.this, ConfigApp.AVATER, ""), WeakImageViewUtil.getImageView(avater_set));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Logger.e("SettingActivity", error_msg);
                        }
                    });
        }
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(int type) {
        showProgress();
        String url = Config.URL + Config.SETUP;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        if (type == 0) {
            //TODO 修改性别
            if (woman_gender.isChecked()) {
                params.put("gender", "" + 1);
            } else if (man_gender.isChecked()) {
                params.put("gender", "" + 0);
            }
        }
        if (type == 1) {
            //TODO 修改生日
            if (!TextUtils.isEmpty(birthday_set.getText().toString())) {
                params.put("birthday", DateUtil.getStringToTime3(birthday_set.getText().toString()));
            }
        }
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                             @Override
                             public void onSuccess(int statusCode, JSONObject response) {
                                 hideProgress();
                                 try {
                                     int status = response.getInt("status");
                                     String info = response.getString("info");
                                     if (status == 1) {
                                         JSONObject data = response.getJSONObject("data");
                                         if (data.getLong("birthday") != 0) {
                                             AppSettings.setPrefString(SettingActivity.this, ConfigApp.BIRTHDAY, DateUtil.getTimeToString(data.getLong("birthday")));
                                         }
                                         int gender = data.getInt("gender");
                                         if (gender == 0) {
                                             //男
                                             AppSettings.setPrefString(context, ConfigApp.GENDER, "0");
                                         } else {
                                             //女
                                             AppSettings.setPrefString(context, ConfigApp.GENDER, "1");
                                         }

                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }

                             @Override
                             public void onFailure(int statusCode, String error_msg) {
                                 hideProgress();
                             }
                         }
                );

    }

    /**
     * 获取用户个人数据
     */
    private void getUserInfo() {
        showProgress();
        String url = Config.URL + Config.MEMBERS;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppSettings.getPrefString(context, ConfigApp.USERID, ""));
        params.put("token", AppSettings.getPrefString(context, ConfigApp.TOKEN, ""));
        params.put("device", "android");
        mMyOkhttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        hideProgress();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                if (response.getJSONObject("data") != null) {
                                    JSONObject data = response.getJSONObject("data");
                                    //TODO 获取昵称
                                    if (!TextUtils.isEmpty(data.getString("user_name"))) {
                                        nickname_set.setText(data.getString("user_name"));
                                    }
                                    //TODO 获取头像
                                    if (!TextUtils.isEmpty(data.getString("headimgurl"))) {
                                        ShowImageUtils.showImageViewToCircle(context, R.mipmap.default_head, Config.IMAGE_URL + data.getString("headimgurl"), WeakImageViewUtil.getImageView(avater_set));
                                    }
                                    //TODO 获取生日
                                    if (!TextUtils.isEmpty(data.getString("birthday"))) {
                                        birthday_set.setText(DateUtil.getTimeToString(data.getLong("birthday")));
                                    }
                                    //TODO 判断男女
                                    if (!TextUtils.isEmpty(data.getString("gender"))) {
                                        if (data.getString("gender").equals("0")) {
                                            man_gender.setChecked(true);
                                        } else {
                                            woman_gender.setChecked(true);
                                        }
                                    }
                                } else {
                                    ToastUtil.show(context, "暂未获取到用户信息", 100);
                                }
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

//    /**
//     * 退出环信
//     * 如果调用异步退出方法，在收到onsucess的回调后才去调用IM相关的方法，比如login
//     */
//    private void ExitHypLogin(int type) {
//        //第一个参数为是否解绑推送的devicetoken
//        ChatClient.getInstance().logout(true, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
//    }

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

    /**
     * 版本更新
     *
     * @param updateMsg
     */
    private void showNoticeDialog(String updateMsg) {
        final AlertDialogUpdate noticeDialog = new AlertDialogUpdate(SettingActivity.this).builder();
        noticeDialog.setTitle("应用版本更新").setMsg(updateMsg)
                .setPositiveButton("下载", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
                        showDownloadDialog();
                    }
                }).setNegativeButton("不更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        }).show();
//        noticeDialog.setCancelable(false);
    }


    protected void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                SettingActivity.this);
        downloadDialog = builder.create();
        downloadDialog.show();
        Window window = downloadDialog.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.my_dialog);
        mProgress = (ProgressBar) window.findViewById(R.id.progress);
        Button cancel_download = (Button) window
                .findViewById(R.id.cancel_download);
        TextView textView_mydialog = (TextView) window
                .findViewById(R.id.textView_mydialog);
        textView_mydialog.setText("应用版本更新");
        downloadDialog.setCancelable(false);
        cancel_download.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                downloadDialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadApk();
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    protected void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");// File.toString()会返回路径信息
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream ins = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream outStream = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    outStream.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消停止下载
                outStream.close();
                ins.close();
            } catch (FileNotFoundException fileException) {
                //TODO 下载文件异常
                downloadDialog.dismiss();
                interceptFlag = true;
                ToastUtil.show(SettingActivity.this, getString(R.string.no_exception_down_address), 100);
            } catch (Exception e) {
                //TODO 其他异常
                e.printStackTrace();
            }
        }
    };
}
