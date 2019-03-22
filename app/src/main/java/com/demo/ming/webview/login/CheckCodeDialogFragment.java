package com.demo.ming.webview.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ming.webview.Constants;
import com.demo.ming.webview.MainActivity;
import com.demo.ming.webview.R;
import com.demo.ming.webview.dialog.AlertDialog;
import com.demo.ming.webview.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by ming on 2018/10/3.
 */

public class CheckCodeDialogFragment extends DialogFragment {
    Unbinder unbinder;
    @BindView(R.id.txt_username)
    TextView mTxtUsername;
    @BindView(R.id.et_code)
    EditText mEtCode;

    private LoadingDialog mLoadingDialog;

    public static CheckCodeDialogFragment newInstance(String username, String password) {
        CheckCodeDialogFragment fragment = new CheckCodeDialogFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("password", password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_check_code, container, false);
        unbinder = ButterKnife.bind(this, view);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTxtUsername.setText(getArguments().getString("username"));
        mLoadingDialog=new LoadingDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.85f), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_neg, R.id.btn_pos})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_neg:
                dismiss();
                break;
            case R.id.btn_pos:
                mLoadingDialog.show();
                TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Activity.TELEPHONY_SERVICE);
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("imie",tm.getDeviceId());
                parameters.put("password",getArguments().getString("password"));
                parameters.put("username",getArguments().getString("username"));
                parameters.put("msmCode",mEtCode.getText());
                parameters.put("phoneType",android.os.Build.MODEL);
                Novate novate = new Novate.Builder( getActivity())
                        .baseUrl(Constants.address)
                        .connectTimeout(60000)
                        .readTimeout(60000)
                        .addLog(true)
                        .build();
                novate.rxPost(Constants.codeCheck_url, parameters, new RxStringCallback() {
                    @Override
                    public void onError(Object tag, Throwable e) {
                        mLoadingDialog.hide();
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {
                        mLoadingDialog.hide();
                    }

                    @Override
                    public void onNext(Object tag, String response) {
                        mLoadingDialog.hide();
                        Gson gson=new Gson();
                        LoginResult loginResult = gson.fromJson(response, LoginResult.class);
                        if("success".equalsIgnoreCase(loginResult.getCode())){
                            Toast.makeText(getActivity(),"绑定成功",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            new AlertDialog(getActivity()).builder()
                                    .setMsg(loginResult.getMessage())
                                    .setNegativeButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    }
                });
                break;
        }
    }
}

