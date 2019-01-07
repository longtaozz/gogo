package com.zt.capacity.common.request.file;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/17.
 * 文件下载
 */

public class FileManager extends Web implements IFileManager {
    private static FileManager fileManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截,4为独立url
     *
     * @param url
     */
    protected FileManager(String url) {
        super(url, 4);
    }


    public static IFileManager getInterface(String url) {
        //二维码查询核准证信息
        fileManager = new FileManager(url);
        return (IFileManager) fileManager;
    }

    @Override
    public Response getFile() {
        return getFile();
    }
}
