package com.demo.ming.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ming.webview.dialog.ActionSheetDialog;
import com.demo.ming.webview.dialog.AlertDialog;
import com.demo.ming.webview.dialog.LoadingDialog;
import com.demo.ming.webview.login.LoginActivity;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    ProgressBar mProgressBarNormal;
    private RelativeLayout ll_contain;
    private Toolbar mToolbar;
    private LinearLayout viewToolbar;
    private ImageView mIvMainTitle;
    private TextView mTvMainTitle;
    private ImageView mIv_image_title;
    private ImageView ic_help, iv_add;

    DownloadManager mDownloadManager;
    private long mRequestId;

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadCallbackAboveL;
    public final int FILE_CHOOSER_RESULT_CODE = 300;

    public String backEventFuntion;
    private LoadingDialog mLoadingDialog;

    /**
     * 打开的Url
     */
//    private String openUrl = "http://oa.hylss.gov.cn:8048/mobileoa/rcsLogin!toDl.action";
    public String testUrl = "http://soft.imtt.qq.com/browser/tes/feedback.html";
    //        private String openUrl = "http://192.168.1.127:8848/5.0移动端/河源人社局/mindex.html#/login";
//    private String openUrl = "http://jtoa.ecinc.com.cn/jtapp/mindex.html#/login";//交投正式
//    private String openUrl = "http://192.168.1.250:8848/dcfj_app/mindex.html#/home/content/home.portal.portal/home.portal.portal";
    private String openUrl = "http://192.168.1.107:8067/mindex.html#/home/content/home.portal.portal/home.portal.portal";//河源
//        private String openUrl = "http://192.168.1.211:8848/hyrsj_app/mindex.html#/login";//河源
//        private String openUrl = "http://14.18.154.84:8096/mindex.html#home/content/home.portal.portal/home.portal.portal";//清远
//    private String openUrl = "http://192.168.1.102:8080/h5/mindex.html#/login";//交投测试
//    private String openUrl = "http://19.176.100.115:8081/rsjapp/mindex.html#/login";//潮州人设
//    private String openUrl = "http://19.176.100.115:8090/sbjapp/mindex.html#/login";//潮州社保
//        private String openUrl = "http://oa.hylss./gov.cn:8048/mindex.html#/login";//河源
//      private String openUrl = "http://14.18.154.84:8099/h5/mindex.html#/login";//东城公安局
//        private String openUrl ="file:///android_asset/test.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
//        getSupportActionBar().hide();// 隐藏ActionBar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //x5 设置键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mLoadingDialog=new LoadingDialog(this);
        ll_contain = (RelativeLayout) findViewById(R.id.ll_contain);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mIvMainTitle = (ImageView) findViewById(R.id.iv_main_title);
        mTvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        ic_help = (ImageView) findViewById(R.id.iv_help);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        viewToolbar = (LinearLayout) findViewById(R.id.view_toolbar);
        mIv_image_title = (ImageView) findViewById(R.id.iv_image_title);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initWebView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteDir(Environment.getExternalStorageDirectory() + File.separator + "webview");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mWebView.reload();
        verifyStoragePermissions(this);
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        //采用new WebView的方式进行动态的添加WebView
        //WebView 的包一定要注意不要导入错了
        //com.tencent.smtt.sdk.WebView;

        mWebView = new WebView(this);
        mProgressBarNormal = new ProgressBar(this);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams barLayoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        barLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mWebView.setLayoutParams(layoutParams);
        mProgressBarNormal.setLayoutParams(barLayoutParams);

        mWebView.loadUrl(Constants.portal_address);
        WebSettings settings = mWebView.getSettings();


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {

                webView.loadUrl(url);

                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                CookieManager cookieManager = CookieManager.getInstance();
                MyApplication.cookie = cookieManager.getCookie(url);

                mLoadingDialog.hide();

            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {

                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//处理https请
                //handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }


        });


        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, final JsResult jsResult) {
                new AlertDialog(MainActivity.this).builder()
                        .setMsg(s1)
                        .setNegativeButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jsResult.cancel();
                            }
                        }).show();
                return true;
//                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";

                } else {
                    // to do something...
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }

        });

        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSInterface(), "native");
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小


        //设置加载图片
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("utf-8");// 避免中文乱码
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setNeedInitialFocus(false);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);//适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadsImagesAutomatically(true);//自动加载图片
        settings.setCacheMode(WebSettings.LOAD_DEFAULT
                | WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        //开启 Application Caches 功能
//        mWebView.getSettings().setAppCacheEnabled(false);


        //将WebView添加到底部布局
        ll_contain.removeAllViews();
        ll_contain.addView(mWebView);
        ll_contain.addView(mProgressBarNormal);
        mProgressBarNormal.setVisibility(View.GONE);

        mLoadingDialog.show();
//        initDownLoad();
    }

    DownloadObserver mDownloadObserver;

    public void initDownLoad(String url) {
        mDownloadObserver = new DownloadObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownloadObserver);

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "测试.pdf");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        mRequestId = mDownloadManager.enqueue(request);


    }

    private class MyWebViewDownLoadListener implements DownloadListener {


        private String tempUrl = "";

        @Override
        public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//            initDownLoad(url);
//            FileActivity.show(MainActivity.this, url);
            if (tempUrl.equalsIgnoreCase(url)) {
                return;
            }
            tempUrl = url;
            Intent intent = new Intent(MainActivity.this, FileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("path", url);
            intent.putExtras(bundle);
            startActivity(intent);
        }

//        @Override
//        public void onDownloadStart(String url, String s1, byte[] bytes, String s2, String s3, String s4, long l, String s5, String s6) {
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//        }
//
//        @Override
//        public void onDownloadVideo(String s, long l, int i) {
//
//        }

    }

    private class DownloadObserver extends ContentObserver {

        private DownloadObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            queryDownloadStatus();
        }
    }

    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mRequestId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载的字节数
                int currentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                Log.i("downloadUpdate: ", currentBytes + " " + totalBytes + " " + status);
//                mDownloadBtn.setText("正在下载：" + currentBytes + "/" + totalBytes);
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
//                    mDownloadBtn.setVisibility(View.GONE);
//                    mDownloadBtn.performClick();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //使用Webview的时候，返回键没有重写的时候会直接关闭程序，这时候其实我们要其执行的知识回退到上一步的操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(backEventFuntion)) {
                mWebView.loadUrl("javascript:" + backEventFuntion + "()");
            }
//            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //js调用本地
    private class JSInterface {
        @JavascriptInterface
        public void close() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public String getJSESSIONID() {
            return Constants.JSESSIONID;
        }

        @JavascriptInterface
        public String getECWEBJWTSSOTOKEN() {
            return Constants.ECWEB_JWTSSO_TOKEN;
        }

        @JavascriptInterface
        public void setTitle(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setViewToolbar(View.VISIBLE);
                    if (title.contains("<")) {
                        setImage(R.mipmap.ic_qygaj_title);
                    } else {
                        setTextTitle(title);
                    }


                }


            });
        }

        @JavascriptInterface
        public String getImie() {
            TelephonyManager tm = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
            String imie = tm.getDeviceId();
            return imie;
        }

        @JavascriptInterface
        public void exit() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    MainActivity.this.finish();
                }
            });
        }

        @JavascriptInterface
        public String getPhoneModel() {
            return android.os.Build.MODEL;
        }

        @JavascriptInterface
        public void downLoadAndOpenFile(final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBarNormal.setVisibility(View.VISIBLE);
                    downLoadFile(url);
                }
            });
        }

        @JavascriptInterface
        public void showUpRightButtons(int id, boolean show, final String functionName) {//此方法是将android端获取的url返给js
            //301新增、302帮助  303返回
            if (id == 301) {
                showivMainTitleRight(R.id.iv_add, show, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWebView.loadUrl("javascript:" + functionName + "()");
//                                mWebView.evaluateJavascript("javascript:"+functionName+"()", new ValueCallback<String>() {
//                                    @Override
//                                    public void onReceiveValue(String value) {
//                                        //此处为 js 返回的结果
//                                    } });
                    }
                });
            } else if (id == 302) {


                showivMainTitleRight(R.id.iv_help, show, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWebView.evaluateJavascript("javascript:" + functionName + "()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                            }
                        });
                    }
                });

            } else if (id == 303) {


                final ActionBar toolbar = getSupportActionBar();
                if (show) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setDisplayHomeAsUpEnabled(true);
                            toolbar.setDisplayShowHomeEnabled(true);
                            backEventFuntion = functionName;
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setDisplayHomeAsUpEnabled(false);
                            toolbar.setDisplayShowHomeEnabled(false);
                            backEventFuntion = "";
                        }
                    });
                }

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(backEventFuntion)) {
                        mWebView.loadUrl("javascript:" + backEventFuntion + "()");
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downLoadFile(String url) {

        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                TLog.d(TAG, "下载文件-->onResponse");
                Headers headers = response.headers();
                String disposition = headers.get("Content-disposition");
                String fileName = disposition.split(";")[1].split("=")[1];
                fileName = fileName.substring(1, fileName.length() - 1);
                try {
                    fileName = URLDecoder.decode(fileName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String cacheDir = Environment.getExternalStorageDirectory() + File.separator + "webview";
                File cacheFile = new File(cacheDir + File.separator + fileName);
                if (cacheFile.exists()) {
                    mProgressBarNormal.setVisibility(View.GONE);
                    openFile(cacheFile.getPath());
                } else {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        ResponseBody responseBody = response.body();
                        is = responseBody.byteStream();
                        long total = responseBody.contentLength();
                        File file1 = new File(cacheDir);
                        if (!file1.exists()) {
                            file1.mkdirs();
                        }
                        File fileN = new File(cacheDir + File.separator + fileName);
                        if (!fileN.exists()) {
                            boolean mkdir = fileN.createNewFile();
                        }
                        fos = new FileOutputStream(fileN);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            mProgressBarNormal.setProgress(progress);
//                        TLog.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                        }
                        fos.flush();
                        mProgressBarNormal.setVisibility(View.GONE);
                        openFile(fileN.getPath());
                    } catch (Exception e) {
                        mProgressBarNormal.setVisibility(View.GONE);
                        showToast("下载失败");
                    } finally {

                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {

                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("报错", Log.getStackTraceString(t));
                mProgressBarNormal.setVisibility(View.GONE);
                showToast("下载失败");
            }
        });
    }

    public void openFile(String filePath) {
        String fFileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        if (true || "jpg".equals(fFileType) || "gif".equals(fFileType) || "png".equals(fFileType) || "jpeg".equals(fFileType) || "bmp".equals(fFileType) || "doc".equals(fFileType) || "docx".equals(fFileType) || "xls".equals(fFileType) || "pdf".equals(fFileType) || "ppt".equals(fFileType)) {
            String mimeType = "";
            if (fFileType != null) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fFileType);
                if (mimeType == null) {
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fFileType.toLowerCase());
                }
            }

            Intent intent = OpenFileUtils.opentAttatchmentFile(filePath, mimeType);
            if (OpenFileUtils.isIntentAvailable(this, intent)) {
                startActivity(intent);
            } else {
                showToast("附件打开失败！");

            }
            //打开文件
        } else {
            showToast("该附件不支持在客户端打开！");
            return;
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // 3.选择图片后处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    //文件上传相关
    // 2.回调方法触发本地选择文件
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadCallbackAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    public void setImage(int src) {
        if (mIv_image_title != null && mIv_image_title != null) {
            showImageTitle(false);
            mTvMainTitle.setVisibility(View.GONE);
            mIv_image_title.setVisibility(View.VISIBLE);
            mIv_image_title.setImageResource(src);
        }
    }

    /**
     * 设置图案标题
     *
     * @param isShow
     */
    public void showImageTitle(boolean isShow) {
        if (mIvMainTitle != null && mTvMainTitle != null) {
            viewToolbar.setVisibility(View.VISIBLE);
            mIvMainTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
            mTvMainTitle.setVisibility(isShow ? View.GONE : View.VISIBLE);
        }
    }

    public void setViewToolbar(int visibilable) {
        viewToolbar.setVisibility(visibilable);

    }

    /**
     * 设置顶部文字标题,如果没有使用activity_with_title中view_toolbar是没效果的
     *
     * @param title
     */
    public void setTextTitle(String title) {
        if (mIvMainTitle != null && mTvMainTitle != null) {
            viewToolbar.setVisibility(View.VISIBLE);
            showImageTitle(false);
            mTvMainTitle.setVisibility(View.VISIBLE);
            mIv_image_title.setVisibility(View.GONE);
            mTvMainTitle.setText(title);

//            mTvMainTitle.setText(Html.fromHtml(title));
        }
    }

    public void showivMainTitleRight(final int id, final boolean ishow, final View.OnClickListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = findViewById(id);
                if (ishow) {
                    view.setVisibility(View.VISIBLE);
                    view.setOnClickListener(listener);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        });

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions(Activity activity) {
        try {
//检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
// 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do nothing here
                } else {
                    Toast.makeText(getBaseContext(), "请到设置界面设置App的定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
