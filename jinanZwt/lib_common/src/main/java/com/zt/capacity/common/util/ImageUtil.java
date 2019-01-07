package com.zt.capacity.common.util;

import android.graphics.Bitmap;

import org.opencv.core.Mat;

/**
 * 图片处理工具
 */
public class ImageUtil {

    /**
     * Mat转bitmap
     * @param bMat
     * @return
     */
    public static Bitmap matToBitmap(Mat bMat){
        Bitmap bitmap = Bitmap.createBitmap(bMat.cols(),bMat.rows(),Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(bMat,bitmap);
        return bitmap;
    }

    public static byte[] matToBytes(Mat bMat){
        return new byte[bMat.height()  *  bMat.width()];
    }
}
