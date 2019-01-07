package com.hikvision;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.debug.MyApplication;
import com.hikvision.sdk.R;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.app.Constants;
import com.hikvision.sdk.app.TempDatas;
import com.hikvision.sdk.consts.HttpConstants;
import com.hikvision.sdk.net.bean.LoginData;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.ui.ResourceListActivity;
import com.hikvision.sdk.utils.SDKUtil;
import com.hikvision.sdk.utils.StringUtils;
import com.hikvision.sdk.utils.UIUtils;

import java.lang.ref.WeakReference;

public class ImsHelper {
    private static Context context;

    //    视频

    public static ImsHelper init(Context context) {
        ImsHelper.context=context;
        return new ImsHelper();
    }


    /**
     * 登录成功
     */
    public final int LOGIN_SUCCESS = 1;
    /**
     * 登录失败
     */
    public final int LOGIN_FAILED = 2;
    /**
     * 退出成功
     */
    public final int LOGOUT_SUCCESS = 3;
    /**
     * 退出失败
     */
    public final int LOGOUT_FAILED = 4;
    private SharedPreferences sharedPreferences;

    /**
     * 发送消息的对象
     */
    private Handler mHandler = null;

    @JavascriptInterface
    public void monitorVideo() {
        Activity activity = (Activity) context;
//        ActivityJumpUtil.jumpActivity(activity, MonitorVideoActivity.class);


        mHandler = new ViewHandler(activity);
        sharedPreferences = activity.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        loginOpt();
    }

    /***
     * UI处理Handler
     */
    private class ViewHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        ViewHandler(Activity activity) {
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


    /***
     * 登录方法
     */
    private void loginOpt() {
//        final String url = mUrlEdit.getText().toString().trim();
//        String userName = mUserEdit.getText().toString().trim();
//        String password = mPsdEdit.getText().toString().trim();
        Activity activity = (Activity) context;
        final String url = "139.159.246.31";
        final String userName = "admin";
        final String password = "Zt123456";
        String macAddress = MyApplication.getIns().getMacAddress();
        // 检查登录参数合法性
        if (StringUtils.isStrEmpty(url) || StringUtils.isStrEmpty(userName)
                || StringUtils.isStrEmpty(password) || StringUtils.isStrEmpty(macAddress)) {
            UIUtils.showToast(activity, R.string.empty_tip);
            return;
        }
        //https 证书验证
//        openHttps();
        // 登录请求
        String loginAddress = HttpConstants.HTTPS + url;
//        String loginAddress = "http://" + url;

        UIUtils.showLoadingProgressDialog(activity, R.string.loading_process_tip, false);
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
                    editor.putString(Constants.USER_NAME, userName);
                    editor.putString(Constants.PASSWORD, password);
                    editor.putString(Constants.ADDRESS_NET, url);
                    editor.apply();
                    //解析版本号
                    String appVersion = ((LoginData) obj).getVersion();
                    SDKUtil.analystVersionInfo(appVersion);
                    //跳转资源列表页面
                    Intent intent = new Intent(context, ResourceListActivity.class);
                    intent.putExtra(Constants.IntentKey.GET_ROOT_NODE, true);
                    context.startActivity(intent);
                }
            }
        });
    }
}
