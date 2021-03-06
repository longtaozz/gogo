package com.hikvision.sdk.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.sdk.R;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.FileUtils;
import com.hikvision.sdk.utils.UIUtils;

import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.hikvision.sdk.consts.SDKConstant.PlayBackSDKConstant.MSG_REMOTELIST_UI_UPDATE;

public class LocalVideoActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private final static int MESSAGE_UI_SHOW_TOAST = 100;
    /*播放view*/
    protected SurfaceView mSurfaceview;
    /*已播放时间*/
    protected TextView playedTimeView;
    /*播放进度调节*/
    protected SeekBar mProgressSeekBar;
    /*视频总时间*/
    protected TextView totalTimeView;
    /*抓图按钮*/
    protected Button captureBtn;
    /*暂停/恢复 播放*/
    protected CheckBox playPauseBtn;
    /*声音开关*/
    protected CheckBox playSoundBtn;
    /**
     * 定时器
     */
    private Timer mUpdateTimer = null;
    /**
     * 定时器执行的任务
     */
    private TimerTask mUpdateTimerTask = null;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_UI_SHOW_TOAST) {
                UIUtils.showToast(LocalVideoActivity.this, (String) msg
                        .obj);
            } else if (msg.what == MSG_REMOTELIST_UI_UPDATE) {
                updateRemotePlayUI();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_local_video);
        initView();
        //播放本地视频
        final File demoVideo = new File(FileUtils.getVideoDirPath() + "/demo.mp4");
        mSurfaceview.post(new Runnable() {
            @Override
            public void run() {
                VMSNetSDK.getInstance().playLocalVideo(demoVideo.getAbsolutePath(), mSurfaceview, new OnVMSNetSDKBusiness() {
                    @Override
                    public void onFailure() {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MESSAGE_UI_SHOW_TOAST;
                        msg.obj = "播放失败";
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onStatusCallback(int status) {
                        //录像回放结束
                        if (status == RtspClient.RTSPCLIENT_MSG_PLAYBACK_FINISH) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = MESSAGE_UI_SHOW_TOAST;
                            msg.obj = "录像播放结束";
                            mHandler.sendMessage(msg);
                            LocalVideoActivity.this.finish();
                            stopUpdateTimer();
                        }
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MESSAGE_UI_SHOW_TOAST;
                        msg.obj = "播放成功";
                        mHandler.sendMessage(msg);
                        startUpdateTimer();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.localplay_capture_btn) {
            int code = VMSNetSDK.getInstance().localCapture(FileUtils.getPictureDirPath().getAbsolutePath(),
                    "Picture" + System.currentTimeMillis() + ".jpg");
            switch (code) {
                case SDKConstant.PlayBackSDKConstant.SD_CARD_UN_USABLE:
                    UIUtils.showToast(this, R.string.sd_card_fail);
                    break;
                case SDKConstant.PlayBackSDKConstant.SD_CARD_SIZE_NOT_ENOUGH:
                    UIUtils.showToast(this, R.string.sd_card_not_enough);
                    break;
                case SDKConstant.PlayBackSDKConstant.CAPTURE_FAILED:
                    UIUtils.showToast(this, R.string.capture_fail);
                    break;
                case SDKConstant.PlayBackSDKConstant.CAPTURE_SUCCESS:
                    UIUtils.showToast(this, R.string.capture_success);
                    break;
            }
        }
    }

    private void initView() {
        mSurfaceview = (SurfaceView) findViewById(R.id.localplay_surfaceview);
        mSurfaceview.getHolder().addCallback(this);
        playedTimeView = (TextView) findViewById(R.id.localplay_played_time_textview);
        mProgressSeekBar = (SeekBar) findViewById(R.id.localplay_progress_seekbar);
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        totalTimeView = (TextView) findViewById(R.id.localplay_total_time_textview);
        captureBtn = (Button) findViewById(R.id.localplay_capture_btn);
        captureBtn.setOnClickListener(LocalVideoActivity.this);
        playPauseBtn = (CheckBox) findViewById(R.id.localplay_play_pause_btn);
        playPauseBtn.setOnCheckedChangeListener(LocalVideoActivity.this);
        playSoundBtn = (CheckBox) findViewById(R.id.localplay_sound_btn);
        playSoundBtn.setOnCheckedChangeListener(LocalVideoActivity.this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.localplay_play_pause_btn) {
            if (!isChecked) {
                if (VMSNetSDK.getInstance().pauseLocalVideo())
                    playPauseBtn.setText("播放");
            } else {
                if (VMSNetSDK.getInstance().resumeLocalVideo())
                    playPauseBtn.setText("暂停");
            }
        } else if (buttonView.getId() == R.id.localplay_sound_btn) {
            if (!isChecked) {
                if (VMSNetSDK.getInstance().setLocalAudio(false))
                    playSoundBtn.setText("开启声音");
            } else {
                if (VMSNetSDK.getInstance().setLocalAudio(true))
                    playSoundBtn.setText("关闭声音");
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        VMSNetSDK.getInstance().setLocalCurrentFrame(seekBar.getProgress() / 100.0);
    }

    /**
     * 启动定时器
     */
    private void startUpdateTimer() {
        totalTimeView.post(new Runnable() {
            @Override
            public void run() {
                totalTimeView.setText(getTimeString(VMSNetSDK.getInstance().getLocalTotalTime()));
            }
        });
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_REMOTELIST_UI_UPDATE);
            }
        };
        // 延时1000ms后执行，1000ms执行一次
        mUpdateTimer.schedule(mUpdateTimerTask, 0, 1000);
    }

    /**
     * 停止定时器
     */
    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VMSNetSDK.getInstance().stopLocalVideo();
        stopUpdateTimer();
    }

    /***
     * 更新播放UI
     */
    private void updateRemotePlayUI() {
        //获取播放进度
        long osd = VMSNetSDK.getInstance().getLocalPlayedTime();
        long total = VMSNetSDK.getInstance().getLocalTotalTime();
        if (osd != -1) {
            mProgressSeekBar.setProgress((int) (osd / (total * 1.0) * 100));
        }
        playedTimeView.setText(getTimeString(VMSNetSDK.getInstance().getLocalPlayedTime()));
    }

    /**
     * 时间转换为HH:MM:SS字符串
     *
     * @param seconds long
     * @return HH:MM:SS
     */
    private String getTimeString(long seconds) {
        int hour = (int) (seconds / 3600);
        int minute = (int) (seconds % 3600 / 60);
        int second = (int) (seconds % 60);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        VMSNetSDK.getInstance().setLocalVideoHolder(holder);
        VMSNetSDK.getInstance().resumeLocalVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        VMSNetSDK.getInstance().setLocalVideoHolder(null);
        VMSNetSDK.getInstance().pauseLocalVideo();
    }
}
