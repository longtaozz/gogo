package com.zt.capacity.common.request.file;


import com.zt.capacity.common.data.listener.OnNetResultListener;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/17.
 * 扫一扫
 */

public interface IFileManager {
    /**
     * 获取文件
     */
    Response getFile();
}
