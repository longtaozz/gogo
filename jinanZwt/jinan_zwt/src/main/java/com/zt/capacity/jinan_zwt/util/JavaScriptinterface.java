package com.zt.capacity.jinan_zwt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.BaseJavaScript;
import com.zt.capacity.jinan_zwt.activity.CarHistoryActivity;
import com.zt.capacity.jinan_zwt.activity.DoubtfulPointActivity;
import com.zt.capacity.jinan_zwt.activity.LocationActivity;
import com.zt.capacity.jinan_zwt.activity.LocationFActivity;
import com.zt.capacity.jinan_zwt.activity.LocationGatherActivity;
import com.zt.capacity.jinan_zwt.activity.LocationGatherMapActivity;
import com.zt.capacity.jinan_zwt.activity.PreviewCodeActivity;
import com.zt.capacity.jinan_zwt.activity.WebViewActivity;


/**
 * Created by Administrator on 2018/4/23.
 */

public class JavaScriptinterface extends BaseJavaScript {


    public JavaScriptinterface(Context c) {
        super(c);
    }


    /**
     * 车牌识别
     */
    @JavascriptInterface
    public void distinguishActivity() {
        ActivityJumpUtil.jumpActivity((Activity) context, PreviewCodeActivity.class);
    }


    /**
     * 通用的web网页跳转
     */
    @JavascriptInterface
    public void baseUrl(String url) {
        Log.e("WEBVIEW......", url);
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivityByString(activity, WebViewActivity.class, url + "?token=" + Web.getToken(), "url");
    }


    /**
     * 位置采集(济南)
     */
    @JavascriptInterface
    public void acquisition() {
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivity(activity, LocationGatherActivity.class);
    }

    /**
     * 位置采集详细页面
     */
    @JavascriptInterface
    public void acquisitioById(String consappId, String unloadingId) {
        Log.e("daiiiiii...", consappId);
        Log.e("daiiiiii...", unloadingId);
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, LocationGatherMapActivity.class);
        intent.putExtra("consappId", consappId);
        intent.putExtra("unloadingId", unloadingId);
        activity.startActivity(intent);
    }

    /**
     * 轨迹查询(济南)
     */
    @JavascriptInterface
    public void carHistory() {
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivity(activity, CarHistoryActivity.class);
    }

    /**
     * 实时位置(济南)
     */
    @JavascriptInterface
    public void carLocation() {
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivity(activity, LocationActivity.class);
    }


    /**
     * 页面查询车辆信息
     *
     * @param carNumber
     */
    @JavascriptInterface
    public void locationCar(String carNumber) {
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivityByString(activity, LocationFActivity.class, carNumber, "carNumber");
    }


    /**
     * 疑点查车(济南)
     */
    @JavascriptInterface
    public void doubtfulPoint() {
        Activity activity = (Activity) context;
        ActivityJumpUtil.jumpActivity(activity, DoubtfulPointActivity.class);
    }


}
