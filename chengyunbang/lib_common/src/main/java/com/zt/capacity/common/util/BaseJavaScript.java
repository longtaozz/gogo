package com.zt.capacity.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hjq.umeng.UmengHelper;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;



/**
 * Created by Administrator on 2018/4/23.
 */

public class BaseJavaScript {

    public Context context;

    public BaseJavaScript(Context c) {
        context = c;
    }

    /**
     * 关掉当前activity
     */
    @JavascriptInterface
    public void backActivity() {
        ((Activity) context).finish();
    }



    /**
     * 友盟统计
     * @param eventName
     */
    @JavascriptInterface
    public void umengStatistics(String eventName){
        Log.e("eventName",eventName);
        UmengHelper.onEvent(BaseApplication.getContext(),eventName);
    }
}
