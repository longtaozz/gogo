package com.hikvision.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.debug.MyApplication;
import com.hikvision.sdk.R;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.app.Constants;
import com.hikvision.sdk.app.TempDatas;
import com.hikvision.sdk.consts.HttpConstants;
import com.hikvision.sdk.net.bean.LoginData;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.SDKUtil;
import com.hikvision.sdk.utils.StringUtils;
import com.hikvision.sdk.utils.UIUtils;

import java.lang.ref.WeakReference;

/**
 * <p>登录Activity</p>
 *
 * @author zhangwei59 2017/3/10 14:34
 * @version V1.0.0
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    /**
     * 登录成功
     */
    public static final int LOGIN_SUCCESS = 1;
    /**
     * 登录失败
     */
    public static final int LOGIN_FAILED = 2;
    /**
     * 退出成功
     */
    public static final int LOGOUT_SUCCESS = 3;
    /**
     * 退出失败
     */
    public static final int LOGOUT_FAILED = 4;
    /***
     * 服务器地址输入控件
     */
    private EditText mUrlEdit;
    /***
     * 用户名输入控件
     */
    private EditText mUserEdit;
    /***
     * 密码输入控件
     */
    private EditText mPsdEdit;
    /***
     * SharedPreferences
     */
    private SharedPreferences sharedPreferences;
    /**
     * 发送消息的对象
     */
    private Handler mHandler = null;

    /***
     * UI处理Handler
     */
    private static class ViewHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        ViewHandler(LoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    // 登录成功
                    UIUtils.cancelProgressDialog();
                    break;
                case LOGIN_FAILED:
                    // 登录失败
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(mActivity.get(), R.string.login_failed);
                    break;
                case LOGOUT_SUCCESS:
                    // 退出成功
                    UIUtils.cancelProgressDialog();
                    mActivity.get().finish();
                    break;
                case LOGOUT_FAILED:
                    // 退出失败
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(mActivity.get(), R.string.logout_failed);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mHandler = new ViewHandler(this);
        sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        initView();
    }

    /***
     * 初始化视图
     */
    private void initView() {
        mUrlEdit = (EditText) findViewById(R.id.main_url);
        mUserEdit = (EditText) findViewById(R.id.main_user_name);
        mPsdEdit = (EditText) findViewById(R.id.main_psd);
        findViewById(R.id.main_url_btn).setOnClickListener(this);
        //登录按钮控件
        findViewById(R.id.main_login_btn).setOnClickListener(this);
        //退出登录按钮控件
        findViewById(R.id.main_logout_btn).setOnClickListener(this);
        findViewById(R.id.main_video_btn).setOnClickListener(this);
        //持久化输入数据，避免多次输入
        String name = sharedPreferences.getString(Constants.USER_NAME, "");
        mUserEdit.setText(name);
        String psw = sharedPreferences.getString(Constants.PASSWORD, "");
        mPsdEdit.setText(psw);
        String adds = sharedPreferences.getString(Constants.ADDRESS_NET, "");
        mUrlEdit.setText(adds);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.main_login_btn) {
            loginOpt();

        } else if (i == R.id.main_logout_btn) {
            logoutOpt();

        } else if (i == R.id.main_url_btn) {
            startActivity(new Intent(LoginActivity.this, UrlActivity.class));

        } else if (i == R.id.main_video_btn) {
            startActivity(new Intent(LoginActivity.this, LocalVideoActivity.class));

        } else {
        }
    }

    /***
     * 登录方法
     */
    private void loginOpt() {
//        final String url = mUrlEdit.getText().toString().trim();
//        String userName = mUserEdit.getText().toString().trim();
//        String password = mPsdEdit.getText().toString().trim();

        final String url = "139.159.246.31";
        String userName = "admin";
        String password = "Zt123456";
        String macAddress = MyApplication.getIns().getMacAddress();
        // 检查登录参数合法性
        if (StringUtils.isStrEmpty(url) || StringUtils.isStrEmpty(userName)
                || StringUtils.isStrEmpty(password) || StringUtils.isStrEmpty(macAddress)) {
            UIUtils.showToast(this, R.string.empty_tip);
            return;
        }
        //https 证书验证
//        openHttps();
        // 登录请求
        String loginAddress = HttpConstants.HTTPS + url;
//        String loginAddress = "http://" + url;

        UIUtils.showLoadingProgressDialog(LoginActivity.this, R.string.loading_process_tip, false);
        VMSNetSDK.getInstance().Login(loginAddress, userName, password, macAddress, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                mHandler.sendEmptyMessage(LOGIN_FAILED);
            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof LoginData) {
                    mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                    //存储登录数据
                    TempDatas.getIns().setLoginData((LoginData) obj);
                    TempDatas.getIns().setLoginAddr(url);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.USER_NAME, mUserEdit.getText().toString().trim());
                    editor.putString(Constants.PASSWORD, mPsdEdit.getText().toString().trim());
                    editor.putString(Constants.ADDRESS_NET, mUrlEdit.getText().toString().trim());
                    editor.apply();
                    //解析版本号
                    String appVersion = ((LoginData) obj).getVersion();
                    SDKUtil.analystVersionInfo(appVersion);
                    //跳转资源列表页面
                    Intent intent = new Intent(LoginActivity.this, ResourceListActivity.class);
                    intent.putExtra(Constants.IntentKey.GET_ROOT_NODE, true);
                    startActivity(intent);
                }
            }
        });
    }

    /***
     * 退出登录
     */
    private void logoutOpt() {
        LoginData loginData = TempDatas.getIns().getLoginData();
        String url = TempDatas.getIns().getLoginAddr();
        if (loginData == null || TextUtils.isEmpty(url)) {
            UIUtils.showToast(this, R.string.have_not_login);
            return;
        }
        UIUtils.showLoadingProgressDialog(LoginActivity.this, R.string.loading_process_tip, false);
        VMSNetSDK.getInstance().Logout(new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                mHandler.sendEmptyMessage(LOGOUT_FAILED);
            }

            @Override
            public void onSuccess(Object obj) {
                //清除登录数据
                TempDatas.getIns().setLoginData(null);
                TempDatas.getIns().setLoginAddr(null);
                mHandler.sendEmptyMessage(LOGOUT_SUCCESS);
            }
        });
    }

//    /**
//     * 开启https网络交换模式，加载证书
//     */
//    public void openHttps() {
//        // The certificate's password
//        String STORE_PASS = "ivms8700";
//        // The certificate's alias.
//        String STORE_ALIAS = "ivms8700";
//        try {
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            InputStream is = getResources().openRawResource(R.raw.ivms8700);
//            keyStore.load(is, STORE_PASS.toCharArray());
//            SSLSocketFactory sf = new SecureSocketFactory(keyStore, STORE_ALIAS);
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            AsyncHttpExecute.getIns().setSSLSocketFactory(sf);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
