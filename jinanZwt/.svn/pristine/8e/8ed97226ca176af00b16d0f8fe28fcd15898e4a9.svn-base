package com.zt.capacity.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/27.
 */

public class IToast {
    private static Toast t;

    public static void show(Context context, String msg) {
        if ( t == null) {

            t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            t.setText(msg);
        }
        t.show();
    }
}
