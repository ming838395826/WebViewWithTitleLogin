package com.demo.ming.webview.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.ming.webview.Constants;
import com.demo.ming.webview.MainActivity;
import com.demo.ming.webview.R;
import com.demo.ming.webview.dialog.AlertDialog;
import com.demo.ming.webview.dialog.LoadingDialog;
import com.demo.ming.webview.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.ResponseCallback;
import com.tamic.novate.callback.RxResultCallback;
import com.tamic.novate.callback.RxStringCallback;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2019/3/21.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login_user_name)
    AppCompatEditText mEtLoginUserName;
    @BindView(R.id.iv_clear_account)
    ImageView mIvClearAccount;
    @BindView(R.id.et_login_password)
    AppCompatEditText mEtLoginPassword;
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.iv_load_history_account)
    ImageView mIvLoadHistoryAccount;
    @BindView(R.id.lly_account)
    LinearLayout mLlyAccount;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_department)
    ImageView ivDepartment;
    @BindView(R.id.iv_copyright)
    ImageView ivCopyright;
    @BindView(R.id.cb_password)
    CheckBox cbPassword;
    Unbinder bind;
    @BindView(R.id.et_login_code)
    AppCompatEditText mEtLoginCode;
    @BindView(R.id.iv_code)
    ImageView mIvCode;

    private LoadingDialog mLoadingDialog;
    private  PreferencesUtils prefer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        mLoadingDialog=new LoadingDialog(this);
        prefer=new PreferencesUtils(LoginActivity.this,"USERINFO");
        if(!TextUtils.isEmpty(prefer.getValue("username"))){
            mEtLoginUserName.setText(prefer.getValue("username"));
        }
        if(!TextUtils.isEmpty(prefer.getValue("password"))){
            mEtLoginPassword.setText(prefer.getValue("password"));
            cbPassword.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick(R.id.btn_login)
    void onHandleLogin() {


        CharSequence account = mEtLoginUserName.getText();
        CharSequence password = mEtLoginPassword.getText();

        if (TextUtils.isEmpty(account)) {
            mEtLoginUserName.setError("请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mEtLoginPassword.setError("请输入密码");
            return;
        }

        mLoadingDialog.show();
        TelephonyManager tm = (TelephonyManager)getSystemService(Activity.TELEPHONY_SERVICE);
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("password",mEtLoginPassword.getText());
        parameters.put("username",mEtLoginUserName.getText());
        parameters.put("phoneType",android.os.Build.MODEL);
        parameters.put("imie",tm.getDeviceId());
        Novate novate = new Novate.Builder(this)
                .baseUrl(Constants.address)
                .connectTimeout(60000)
                .readTimeout(60000)
                .addLog(true)
                .build();
        novate.rxPost(Constants.login_url, parameters, new RxStringCallback() {
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

                            if (cbPassword.isChecked()) {
                                prefer.putValue("username",mEtLoginUserName.getText().toString());
                                prefer.putValue("password",mEtLoginPassword.getText().toString());
                            }else {
                                prefer.clear();
                            }

                            StringBuilder jessionid = new StringBuilder();
                            jessionid.append(String.format("JSESSIONID=%s", loginResult.getBody().getJSESSIONID()));

                            StringBuilder ecwweb_jwtsso_token = new StringBuilder();
                            ecwweb_jwtsso_token.append(String.format("ECWEB-JWTSSO-TOKEN=%s", loginResult.getBody().getECWEBJWTSSOTOKEN()));

                            Constants.JSESSIONID=loginResult.getBody().getJSESSIONID();
                            Constants.ECWEB_JWTSSO_TOKEN=loginResult.getBody().getECWEBJWTSSOTOKEN();
                            CookieSyncManager.createInstance(LoginActivity.this);//创建一个cookie管理器
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.setAcceptCookie(true);
                            cookieManager.setCookie(Constants.address, ecwweb_jwtsso_token.toString());//为url设置cookie
                            cookieManager.setCookie(Constants.address, jessionid.toString());
                            cookieManager.flush();//同步cookie

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else if(loginResult.getMessage().indexOf("yanzheng-")==0){
                            CheckCodeDialogFragment payDialogFragment = CheckCodeDialogFragment.newInstance(mEtLoginUserName.getText().toString(),mEtLoginPassword.getText().toString());
                            payDialogFragment.show(getSupportFragmentManager(), "PAY");
                        }else {
                            new AlertDialog(LoginActivity.this).builder()
                                    .setMsg(loginResult.getMessage())
                                    .setNegativeButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    }
                });
    }

    @OnClick(R.id.iv_clear_account)
    void clearAccount() {
        mEtLoginUserName.setText("");
        mEtLoginPassword.setText("");
    }

}
