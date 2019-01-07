package com.zt.capacity.common.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-04-12.
 */

public class ActivityJumpUtil {

    public final static int RC_SUBWAY = 0;
    public final static int FRAGMENT_SEARCH = 1;

    public static void jumpActivity(Activity context, Class clz){
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
    }
    public static void jumpActivity(Activity context, Class clz, Bundle bundle){
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void jumpforResultActivity(Activity context, Class clz, Bundle bundle, int requestCode){
        Intent intent = new Intent(context, clz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }
    public static void jumpforResultActivity(Activity context, Class clz, int requestCode){
        Intent intent = new Intent(context, clz);
        context.startActivityForResult(intent, requestCode);
    }

    public static void jumpActivityByObject(Activity context, Class clz, Serializable o, String name){
        Intent intent = new Intent(context, clz);
        intent.putExtra(name,o);
        context.startActivity(intent);
    }

    public static void jumpActivityByString(Activity context, Class clz, String str, String name){
        Intent intent = new Intent(context, clz);
        intent.putExtra(name,str);
        context.startActivity(intent);
    }
}
