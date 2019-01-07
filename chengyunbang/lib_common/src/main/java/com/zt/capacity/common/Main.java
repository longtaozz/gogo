package com.zt.capacity.common;

import android.content.Context;

import com.zt.capacity.common.data.Web;


/**
 * 路由
 */
public class Main {
    private static Integer cityId;//城市id
    private static Integer role;//权限
    private static Context context;


    private static Main main;

    public static Main getInterface(Context mContext) {
        if (main == null)
            main = new Main();
//        if (null == Web.getUser() || mContext == null || Web.getUser().getCityId() == null || Web.getUser().getRole() == null) {
//            //检查用户信息存不存在
//            return null;
//        }
        context = mContext;
        cityId = Web.getUser().getCityId();
        role = Web.getUser().getRole();

        return main;
    }


}
