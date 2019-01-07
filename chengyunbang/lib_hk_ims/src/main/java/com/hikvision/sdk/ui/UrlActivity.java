package com.hikvision.sdk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hikvision.sdk.R;
import com.hikvision.sdk.RealPlayManagerEx;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.FileUtils;
import com.hikvision.sdk.utils.UIUtils;

public class UrlActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {

    protected SurfaceView surfaceView;
    protected Button captureView;
    protected Button recordView;
    protected Button soundView;
    /* 是否正在录像     */
    private boolean mIsRecord;
    /*音频是否开启 */
    private boolean mIsAudioOpen;
    /**
     * 播放窗口1
     */
    private int PLAY_WINDOW_ONE = 1;
    /*取流url*/
    //TODO 该地址请自行从平台获取
    private String playUrl = "rtsp://10.10.33.31:554/pag://100.100.1.241:7302:f6f3616cd679455bba4299eed0915aba:0:MAIN:TCP?cnid=4&pnid=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_url);
        initView();
        //等待surfaceview创建完毕
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                RealPlayManagerEx.getInstance().startRealPlay(PLAY_WINDOW_ONE, playUrl, surfaceView, new OnVMSNetSDKBusiness() {
                    @Override
                    public void onFailure() {
                        super.onFailure();
                        Toast.makeText(UrlActivity.this, "播放失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusCallback(int status) {
                        super.onStatusCallback(status);
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        super.onSuccess(obj);
                        Toast.makeText(UrlActivity.this, "播放成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.capture_view) {
            //抓图操作
            int opt = RealPlayManagerEx.getInstance().capture(PLAY_WINDOW_ONE, FileUtils.getPictureDirPath().getAbsolutePath(), "Picture" + System.currentTimeMillis() + ".jpg");
            switch (opt) {
                case SDKConstant.LiveSDKConstant.SD_CARD_UN_USABLE:
                    UIUtils.showToast(this, R.string.sd_card_fail);
                    break;
                case SDKConstant.LiveSDKConstant.SD_CARD_SIZE_NOT_ENOUGH:
                    UIUtils.showToast(this, R.string.sd_card_not_enough);
                    break;
                case SDKConstant.LiveSDKConstant.CAPTURE_FAILED:
                    UIUtils.showToast(this, R.string.capture_fail);
                    break;
                case SDKConstant.LiveSDKConstant.CAPTURE_SUCCESS:
                    UIUtils.showToast(this, R.string.capture_success);
                    break;
            }
        } else if (view.getId() == R.id.record_view) {
            //录像按钮点击操作
            if (!mIsRecord) {
                int recordOpt = RealPlayManagerEx.getInstance().startRecord(PLAY_WINDOW_ONE, FileUtils.getVideoDirPath().getAbsolutePath(), "Video" + System.currentTimeMillis() + ".mp4");
                switch (recordOpt) {
                    case SDKConstant.LiveSDKConstant.SD_CARD_UN_USABLE:
                        UIUtils.showToast(this, R.string.sd_card_fail);
                        break;
                    case SDKConstant.LiveSDKConstant.SD_CARD_SIZE_NOT_ENOUGH:
                        UIUtils.showToast(this, R.string.sd_card_not_enough);
                        break;
                    case SDKConstant.LiveSDKConstant.RECORD_FAILED:
                        mIsRecord = false;
                        UIUtils.showToast(this, R.string.start_record_fail);
                        break;
                    case SDKConstant.LiveSDKConstant.RECORD_SUCCESS:
                        mIsRecord = true;
                        UIUtils.showToast(this, R.string.start_record_success);
                        recordView.setText(R.string.stop_record);
                        break;
                }
            } else {
                RealPlayManagerEx.getInstance().stopRecord(PLAY_WINDOW_ONE);
                mIsRecord = false;
                UIUtils.showToast(this, R.string.stop_record_success);
                recordView.setText(R.string.start_record);
            }
        } else if (view.getId() == R.id.sound_view) {
            //音频按钮点击操作
            if (mIsAudioOpen) {
                boolean audioOpt = RealPlayManagerEx.getInstance().turnoffAudio(PLAY_WINDOW_ONE);
                if (audioOpt) {
                    mIsAudioOpen = false;
                    UIUtils.showToast(this, R.string.stop_Audio);
                    soundView.setText(R.string.start_Audio);
                }
            } else {
                boolean ret = RealPlayManagerEx.getInstance().openAudio(PLAY_WINDOW_ONE);
                if (!ret) {
                    mIsAudioOpen = false;
                    UIUtils.showToast(UrlActivity.this, R.string.start_Audio_fail);
                    soundView.setText(R.string.start_Audio);
                } else {
                    mIsAudioOpen = true;
                    // 开启音频成功，并不代表一定有声音，需要设备开启声音。
                    UIUtils.showToast(UrlActivity.this, R.string.start_Audio_success);
                    soundView.setText(R.string.stop_Audio);
                }
            }

        }
    }

    private void initView() {
        //初始化view
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        captureView = (Button) findViewById(R.id.capture_view);
        captureView.setOnClickListener(UrlActivity.this);
        recordView = (Button) findViewById(R.id.record_view);
        recordView.setOnClickListener(UrlActivity.this);
        soundView = (Button) findViewById(R.id.sound_view);
        soundView.setOnClickListener(UrlActivity.this);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //页面销毁时停止预览
        boolean stopLiveResult = RealPlayManagerEx.getInstance().stopRealPlay(PLAY_WINDOW_ONE);
        if (stopLiveResult) {
            UIUtils.showToast(this, R.string.live_stop_success);
        }
    }

}
