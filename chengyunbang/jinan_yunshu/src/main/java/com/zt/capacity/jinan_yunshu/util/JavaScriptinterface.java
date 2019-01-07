package com.zt.capacity.jinan_yunshu.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zt.capacity.baidumap.activity.CarHistoryActivity;
import com.zt.capacity.baidumap.activity.LocationActivity;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.BaseJavaScript;
import com.zt.capacity.jinan_yunshu.activity.InputVehicleActivity;
import com.zt.capacity.jinan_yunshu.activity.InstructionActivity;
import com.zt.capacity.jinan_yunshu.activity.MainMyActivity;
import com.zt.capacity.jinan_yunshu.activity.WebViewActivity;


/**
 * 济南运输公司js交互
 *
 * @author lt
 * @time 2018/12/18 14:47
 **/
public class JavaScriptinterface extends BaseJavaScript {


    public JavaScriptinterface(Context c) {
        super(c);
    }


    /**
     * 通用的web网页跳转
     */
    @JavascriptInterface
    public void baseUrl(String url) {
        Log.e("WEBVIEW......", url);
        Activity activity = (Activity) context;
        Intent i = new Intent(activity, WebViewActivity.class);
        i.putExtra("url", url + "?token=" + Web.getToken());
        context.startActivity(i);
    }

    /**
     * 实时监控位置
     *
     * @author lt
     * @time 2018/12/18 14:44
     **/
    @JavascriptInterface
    public void goLocation() {
        ActivityJumpUtil.jumpActivity((Activity) context, LocationActivity.class);
    }

    /**
     * 历史轨迹
     *
     * @author lt
     * @time 2018/12/18 14:45
     **/
    @JavascriptInterface
    public void goCarHistory() {
        ActivityJumpUtil.jumpActivity((Activity) context, CarHistoryActivity.class);
    }

    /**
     * 指令下发(车辆管控)
     * @author lt
     * @time 2018/12/19 15:56
     *
     **/
    @JavascriptInterface
    public void goInstruction() {
        ActivityJumpUtil.jumpActivity((Activity) context, InstructionActivity.class);
    }

    /**
     * 违章信息查询
     * @author lt
     * @time 2018/12/24 13:50
     *
     **/
    @JavascriptInterface
    public void goInputVehicle() {
        ActivityJumpUtil.jumpActivity((Activity) context, InputVehicleActivity.class);
    }

    /**
     * 我的
     * @author lt
     * @time 2018/12/26 16:01
     *
     **/
    @JavascriptInterface
    public void goMainMy(){
        ActivityJumpUtil.jumpActivity((Activity) context, MainMyActivity.class);
    }

}
