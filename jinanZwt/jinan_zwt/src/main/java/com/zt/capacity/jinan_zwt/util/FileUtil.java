package com.zt.capacity.jinan_zwt.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {
    public static void copyFilesFromAssets(Context paramContext, String paramString1, String paramString2) {
        int i = 0;
        InputStream paramContextI=null;
        FileOutputStream paramStringU = null;
        try {
            String[] arrayOfString = paramContext.getAssets().list(paramString1);
            if (arrayOfString.length > 0) {
                if (!new File(paramString2).mkdir()) {
                    Log.d("mkdir", "can't make folder");
                }
                int j = arrayOfString.length;
                while (i < j) {
                    String str = arrayOfString[i];
                    copyFilesFromAssets(paramContext, paramString1 + "/" + str, paramString2 + "/" + str);
                    i += 1;
                }
            }
             paramContextI = paramContext.getAssets().open(paramString1);
             paramStringU = new FileOutputStream(new File(paramString2));
            byte[] paramStringB = new byte['?'];
            for (; ; ) {
                i = paramContextI.read(paramStringB);
                if (i == -1) {
                    break;
                }
                paramStringU.write(paramStringB, 0, i);
            }
            paramStringU.flush();
            paramContextI.close();
            paramStringU.close();
            return;
        } catch (Exception e) {
            Log.e("chucuo...",e.getMessage());
            e.printStackTrace();
        }
    }
}
