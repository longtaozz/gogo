package com.zt.capacity.jinan_zwt.request.user;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.bean.BaseUserBean;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;
import com.zt.capacity.jinan_zwt.request.util.ErrorCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/16.
 */

public class UserManager extends Web implements IUserManager {
    Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     */
    protected UserManager(String url) {
        super(url, 3);
    }

    public static IUserManager getInterface(Integer state) {
        UserManager userManager = null;
        if (state == 1) {
            userManager = new UserManager(JN_ZWT_Url.LOGIN);
        }
        return (IUserManager) userManager;
    }

    @Override
    public void login(final String user, final String password, final OnNetResultListener listener) {
        if (TextUtils.isEmpty(user)) {
            listener.onResult(ErrorCode.FAIL, "用户名不能为空", null);
            return;
        } else if (TextUtils.isEmpty(password)) {
            listener.onResult(ErrorCode.FAIL, "密码不能为空", null);
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("username", user);
        map.put("password", password);


        postQueryJson(map,new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {

                if (code == 0) {
                    try {
                        BaseUserBean baseBean = (BaseUserBean) gson.fromJson(data.toString(), BaseUserBean.class);
                        if (baseBean.getCode() == 0) {
                            setToken(token);
                            Web.setUserID(baseBean.getUser().getUserId());
                            Web.setUser(baseBean.getUser());
                        }
                        listener.onResult(baseBean.getCode(), baseBean.getMessage(), null);
                    }catch (Exception e){
                        Log.e("myerr", e.toString());
                        listener.onResult(code, "登录失败", null);
                    }
                } else if (code == 404) {
                    listener.onResult(code, "登录失败", null);
                } else {
                    if (data == null || "".equals(data.toString())) {
                        listener.onResult(code, "登录失败", null);
                    } else {
                        BaseUserBean baseBean = (BaseUserBean) gson.fromJson(data.toString(), BaseUserBean.class);
                        listener.onResult(code, baseBean.getMessage(), null);

                    }
                }
            }



            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "登录失败", null);
                Log.d("exe", "失败");
            }
        });


    }

}