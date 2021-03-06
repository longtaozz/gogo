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

import com.zt.capacity.common.R;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 权限申请
 */
public class JurisdictionUtil {
    private static int CAMERA_REQUEST_CODE = 1;//成功


    private static int SDK_PERMISSION_REQUEST = 127;


    public static void accessibility(Context context,PermissionCallback permissionCallback){
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.REQUEST_INSTALL_PACKAGES, "安装", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
        HiPermission.create(context)
                .permissions(permissionItems)
                .checkMutiPermission(permissionCallback);
    }


    /**
     * 获取读写权限
     * @param context
     */
    public static void GETWRITEEXTERNAL(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission},123);
            }
        }
    }

    //camera权限
    public static void GETCAMERA(final Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(context)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        } else {
            //已有权限
        }
    }

    //录音权限
    public static void RECORD_AUDIO(final Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO)) {
                new AlertDialog.Builder(context)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.RECORD_AUDIO}, CAMERA_REQUEST_CODE);
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.RECORD_AUDIO}, CAMERA_REQUEST_CODE);
            }
        } else {
            //已有权限
        }
    }

    /**
     * 定位权限
     * @param context
     */
    public static void ACCESS_FINE_LOCATION(final Context context) {


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(context)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CAMERA_REQUEST_CODE);
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CAMERA_REQUEST_CODE);
            }
        } else {
            //已有权限
        }
    }



    /**
     * android 8.0以上需要安装权限
     * @param context
     */
    public static void REQUEST_INSTALL_PACKAGES(final Context context) {


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.REQUEST_INSTALL_PACKAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                new AlertDialog.Builder(context)
                        .setMessage("申请安装权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, CAMERA_REQUEST_CODE);
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, CAMERA_REQUEST_CODE);
            }
        } else {
            //已有权限
        }
    }


}
