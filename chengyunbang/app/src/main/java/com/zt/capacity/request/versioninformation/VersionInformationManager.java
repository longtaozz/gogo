package com.zt.capacity.request.versioninformation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.bean.VersionBean;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.data.ZtUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2018-05-23.
 */

public class VersionInformationManager extends Web implements IVersionInformationManager {
    private static VersionInformationManager mVersionInformationManager;
    public static IVersionInformationManager getInterface(){
        if (mVersionInformationManager == null) {
            mVersionInformationManager = new VersionInformationManager();
        }
        return (IVersionInformationManager) mVersionInformationManager;
    }

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     */
    protected VersionInformationManager() {
        super(ZtUrl.QUERYAPPUPDATEINFO, 4);
    }


    @Override
    public void getVersionInformation(final OnNetResultListener listener) {
        Map<String, Object> map = new HashMap<String, Object>();
        postQueryJson(map, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                if(code == 0) {
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    Log.e("baseBean", data.toString());
                    VersionBean vehicleBean = gson.fromJson(data.toString(), VersionBean.class);
                    listener.onResult(0,"成功",vehicleBean);
                }else {
                    listener.onResult(1,"版本信息获取失败",null);
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1,"版本信息获取失败",null);
            }
        });
    }
}
