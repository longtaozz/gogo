package com.zt.capacity.jinan_zwt.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class CacheUtil
{
    public static String readString(Context paramContext, String paramString)
    {
        return paramContext.getSharedPreferences(paramContext.getPackageName(), 0).getString(paramString, null);
    }

    public static void writeString(Context paramContext, String paramString1, String paramString2)
    {
        Editor paramContextx = paramContext.getSharedPreferences(paramContext.getPackageName(), 0).edit();
        paramContextx.putString(paramString1, paramString2);
        paramContextx.commit();
    }
}
