package com.demo.ming.webview;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Administrator on 2018/7/12.
 */

public class MyApplication extends Application {

    public static Application instanse;
    public static String cookie;

    @Override
    public void onCreate() {
        super.onCreate();
        instanse=this;
        initX5();

    }

    /**
     * 初始化X5
     */
    private void initX5() {
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }
}
