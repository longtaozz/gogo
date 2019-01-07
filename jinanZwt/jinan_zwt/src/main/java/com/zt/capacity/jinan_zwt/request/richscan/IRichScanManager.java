package com.zt.capacity.jinan_zwt.request.richscan;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018/4/17.
 * 扫一扫
 */

public interface IRichScanManager {
    /**
     * 扫二维码查看核准证信息
     * @param cardId
     * @param listener
     */
    void richScanQrCode(String cardId,OnNetResultListener listener);
}
