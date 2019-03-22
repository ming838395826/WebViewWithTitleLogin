package com.demo.ming.webview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.ming.webview.R;


/**
 * loading窗口
 * //todo 考虑新建一个DialogUtils， 将各种窗口类汇总
 * Created by liuxiaoming on 16-7-30.
 */
public class LoadingDialog extends Dialog {

    ProgressBar mPgLoading;
    TextView mTvLoading;

    public LoadingDialog(Context context) {
        super(context, R.style.selectorDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        mTvLoading = (TextView) view.findViewById(R.id.tv_loading);

        setContentView(view);
//        setCancelable(false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

//        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
    }

    public void setLoadingTip(String tip) {
        mTvLoading.setText(tip);
    }


}
