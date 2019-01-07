package com.zt.capacity.common.data;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由控制
 *
 * @author lt
 * @time 2018/12/13 13:35
 **/
public class MyRoute {
    //路由路径
    public static String JN_MAIN = "/jinan/transit/main";//济南主页面
    public static String CS_MAIN = "/changsha/transit/main";//长沙主页面

    //城市编号
    public static final String JINAN="250000";//济南
    public static final String CHANGSHA="360100";//长沙

    private static Context context;

    public static MyRoute getInterface(Context context) {
        MyRoute.context = context;
        MyRoute route = null;
        if (TextUtils.equals(Web.getUser().getCityNumber(), JINAN)) {
            Web.setProject("jinan");
            Web.setHtmlProject("");
            route = new MyRoute();
        }
        if (TextUtils.equals(Web.getUser().getCityNumber(), CHANGSHA)) {
            Web.setProject("changsha");
            Web.setHtmlProject("/changsha");
            route = new MyRoute();
        }
        return route;
    }

    public void mainActivity() {
        //根据帐号确定主页面位置
        if (TextUtils.equals(Web.getUser().getCityNumber(), JINAN)) {
            ARouter.getInstance().build(JN_MAIN).navigation();
        }
        if (TextUtils.equals(Web.getUser().getCityNumber(), CHANGSHA)) {
            ARouter.getInstance().build(CS_MAIN).navigation();
        }
    }
}
