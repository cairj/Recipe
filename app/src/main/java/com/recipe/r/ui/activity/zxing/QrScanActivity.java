package com.recipe.r.ui.activity.zxing;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afun.zxinglib.ScanView.ZXingScannerViewNew;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.recipe.r.R;
import com.recipe.r.utils.PermissionUtils;
import com.recipe.r.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class QrScanActivity extends Activity implements ZXingScannerViewNew.ResultHandler, ZXingScannerViewNew.QrSize, View.OnClickListener {
    ZXingScannerViewNew scanView;
    private TextView result;
    private LinearLayout ll_qrscan;
    private WebView web_qrscan;
    private static int REQUEST_CAMERA=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanView = new ZXingScannerViewNew(this);
        scanView.setContentView(R.layout.logistics_scan_qr);
        scanView.setQrSize(this);
        if (PermissionUtils.checkCameraPermission(this,REQUEST_CAMERA)){
            openCamera();
        }else {
            ToastUtil.show(this, "相机权限被禁用，无法扫描，请开启", 500);
        }
        setContentView(scanView);
        setupFormats();
        initUI();
    }

    private void initUI() {
        findViewById(R.id.confirm).setOnClickListener(this);
        result = (TextView) findViewById(R.id.editText);
        ll_qrscan = (LinearLayout) findViewById(R.id.ll_qrscan);
        web_qrscan = (WebView) findViewById(R.id.web_qrscan);
        setWebView();
    }

    /**
     * 设置webView
     */
    private void setWebView() {
        web_qrscan.canGoBack();
        web_qrscan.goBack();
        //是否可以前进
        web_qrscan.canGoForward();
        //前进网页
        web_qrscan.goForward();
        //声明WebSettings子类
        WebSettings webSettings = web_qrscan.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        web_qrscan.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
        web_qrscan.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.show(this, "相机权限被禁用，无法扫描", 500);
            } else {
                openCamera();
                setContentView(scanView);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void openCamera(){
        scanView.setResultHandler(this);
        scanView.startCamera(-1);
        scanView.setFlash(false);
        scanView.setAutoFocus(true);
    }

    @Override
    public void handleResult(Result rawResult) {
        result.setText(rawResult.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanView.stopCamera();
        web_qrscan.onPause();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);
        if (scanView != null) {
            scanView.setFormats(formats);
        }
    }

    @Override
    public Rect getDetectRect() {
        View view = findViewById(R.id.scan_window);
        int top = ((View) view.getParent()).getTop() + view.getTop();
        int left = view.getLeft();
        int width = view.getWidth();
        int height = view.getHeight();
        Rect rect = null;
        if (width != 0 && height != 0) {
            rect = new Rect(left, top, left + width, top + height);
            addLineAnim(rect);
        }
        return rect;
    }

    private void addLineAnim(Rect rect) {
        ImageView imageView = (ImageView) findViewById(R.id.scanner_line);
        imageView.setVisibility(View.VISIBLE);
        if (imageView.getAnimation() == null) {
            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, rect.height());
            anim.setDuration(1500);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) {
            //TODO something
            String Url = result.getText().toString();
            ll_qrscan.setVisibility(View.GONE);
            web_qrscan.setVisibility(View.VISIBLE);
            web_qrscan.loadUrl(Url);
//            try {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse();
//                intent.setData(content_url);
//                startActivity(intent);
//            } catch (ActivityNotFoundException a) {
//                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                // 将文本内容放到系统剪贴板里。
//                cm.setText(result.getText().toString());
//                Toast.makeText(QrScanActivity.this, "扫描结果无法打开，已经复制到剪切板，请自行访问", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        web_qrscan.destroy();
    }
}
