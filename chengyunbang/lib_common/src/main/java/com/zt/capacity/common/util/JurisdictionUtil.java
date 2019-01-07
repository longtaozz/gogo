package com.zt.capacity.common.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请帮助类
 *
 * @author lt
 * @time 2018/12/13 14:39
 **/
public class JurisdictionUtil {

    //读写权限
    public static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //camera权限
    public static String CAMERA = Manifest.permission.CAMERA;
    //录音权限
    public static String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    //定位权限
    public static String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //安装权限
    public static String REQUEST_INSTALL_PACKAGES = Manifest.permission.REQUEST_INSTALL_PACKAGES;
    /**
     * 获取安装权限
     * @param context
     */
    public static void REQUESTINSTALLPACKAGES(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = REQUEST_INSTALL_PACKAGES;
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission},123);
            }
        }
    }

    /**
     * 权限申请
     */
    public static void GETPERMISSIONS(Context context, List<String> permissionList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionList != null && permissionList.size() > 0) {
            List<String> permissionJ = new ArrayList<>();
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            for (int i = 0; i < permissionList.size(); i++) {
                if (TextUtils.equals(REQUEST_INSTALL_PACKAGES, permissionList.get(i))) {
                    //安装权限需要8.0以上申请
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (ActivityCompat.checkSelfPermission(context, permissionList.get(i))
                                != PackageManager.PERMISSION_GRANTED) {
                            permissionJ.add(permissionList.get(i));
                        }
                    }

                } else if (ActivityCompat.checkSelfPermission(context, permissionList.get(i))
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionJ.add(permissionList.get(i));
                }
            }
            if (permissionJ.size() > 0) {
                String[] permissions = permissionJ.toArray(new String[permissionJ.size()]);
                ActivityCompat.requestPermissions((Activity) context, permissions, 123);
            }
        }
    }


}
