package com.debug;


import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.requests.user.IUserManager;
import com.zt.capacity.common.requests.user.UserManager;


/**
 * 单独测试开发用application
 * @author lt
 * @time 2018/12/14 11:34
 *
 **/
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //不需要请求无需模拟登陆
        login();
    }

    /**
     * 在这里模拟登陆，然后拿到sessionId或者Token
     * 这样就能够在组件请求接口了
     */
    private void login() {
        String account = "济南恒久";
        String passWord = "123456";
        //储存帐号
        IUserManager userLogin = UserManager.getInterface(1);
        userLogin.login(account, passWord, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.d("state", "" + state);
                Log.d("msg", msg);
                Message message = new Message();
                if (state == 0) {
                    if (TextUtils.isEmpty(Web.getToken())) {
                        login();
                    } else {
                        //模拟登陆成功
                    }
                }
            }
        });
    }
}
