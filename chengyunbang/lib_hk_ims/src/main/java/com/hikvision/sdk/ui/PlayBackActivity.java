package com.hikvision.sdk.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.sdk.R;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.app.Constants;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.consts.SDKConstant.PlayBackSDKConstant;
import com.hikvision.sdk.net.bean.CameraInfo;
import com.hikvision.sdk.net.bean.CustomRect;
import com.hikvision.sdk.net.bean.PlaybackSpeed;
import com.hikvision.sdk.net.bean.RecordInfo;
import com.hikvision.sdk.net.bean.RecordSegment;
import com.hikvision.sdk.net.bean.SubResourceNodeBean;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.FileUtils;
import com.hikvision.sdk.utils.SDKUtil;
import com.hikvision.sdk.utils.UIUtils;
import com.hikvision.sdk.widget.CustomSurfaceView;
import com.hikvision.sdk.widget.CustomSurfaceView.OnZoomListener;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>远程回放Activity</p>
 *
 * @author zhangwei59 2017/3/10 14:32
 * @version V1.0.0
 */
public class PlayBackActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {

    /**
     * 获取监控点信息成功
     */
    private static final int CAMERA_INFO_SUCCESS = 1;
    /**
     * 获取监控点信息失败
     */
    private static final int CAMERA_INFO_FAILURE = 2;
    /**
     * 查找录像片段成功
     */
    private static final int QUERY_SUCCESS = 3;
    /**
     * 查找录像片段失败
     */
    private static final int QUERY_FAILURE = 4;
    /**
     * 开启回放成功
     */
    public static final int START_SUCCESS = 5;
    /**
     * 开启回放失败
     */
    public static final int START_FAILURE = 6;
    /**
     * 进度条最大值
     */
    private static final int PROGRESS_MAX_VALUE = 100;

    /**
     * 播放视图控件
     */
    private CustomSurfaceView mSurfaceView;
    /**
     * 存储介质选择控件
     */
    private RadioGroup mStorageTypesRG;
    /**
     * 暂停按钮
     */
    private Button mPauseButton;
    /**
     * 录像按钮
     */
    private Button mRecordButton;
    /**
     * 音频按钮
     */
    private Button mAudioButton;
    /**
     * 电子放大控件
     */
    private CheckBox mZoom;
    /**
     * 播放进度条控件
     */
    private SeekBar mProgressSeekBar = null;

    /**
     * 是否暂停标志
     */
    private boolean mIsPause;
    /**
     * 音频是否开启
     */
    private boolean mIsAudioOpen;
    /**
     * 是否正在录像
     */
    private boolean mIsRecord;
    /**
     * 监控点资源
     */
    private SubResourceNodeBean mCamera;
    /**
     * 监控点详情
     */
    private CameraInfo mCameraInfo;
    /**
     * 存储介质
     */
    private int mStorageType;
    /**
     * 录像唯一标识Guid
     */
    private String mGuid;
    /**
     * 录像详情
     */
    private RecordInfo mRecordInfo;
    /**
     * 初始开始时间
     */
    private Calendar mFirstStartTime;
    /**
     * 开始时间
     */
    private Calendar mStartTime;
    /**
     * 结束时间
     */
    private Calendar mEndTime;
    /**
     * 录像片段
     */
    private RecordSegment mRecordSegment;
    /**
     * 定时器
     */
    private Timer mUpdateTimer = null;
    /**
     * 定时器执行的任务
     */
    private TimerTask mUpdateTimerTask = null;
    /**
     * 创建消息对象
     */
    private Handler mMessageHandler;

    /**
     * 播放窗口1
     */
    private int PLAY_WINDOW_ONE = 1;

    /***
     * UI处理Handler
     */
    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        WeakReference<PlayBackActivity> mActivityReference;

        MyHandler(PlayBackActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            PlayBackActivity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case CAMERA_INFO_SUCCESS:
                        UIUtils.cancelProgressDialog();
                        //解析监控点录像信息
                        int[] mRecordPos = SDKUtil.processStorageType(activity.mCameraInfo);
                        String[] mGuids = SDKUtil.processGuid(activity.mCameraInfo);
                        //默认选取第一种存储类型进行查询
                        if (null != mRecordPos && 0 < mRecordPos.length) {
                            activity.mStorageType = mRecordPos[0];
                        }
                        if (null != mGuids && 0 < mGuids.length) {
                            activity.mGuid = mGuids[0];
                        }
                        activity.initStorageTypeView(mRecordPos, mGuids);
                        if (null != mRecordPos && 0 < mRecordPos.length) {
                            activity.queryRecordSegment();
                        } else {
                            UIUtils.showToast(activity, "录像文件查询失败");
                        }
                        break;
                    case CAMERA_INFO_FAILURE:
                        UIUtils.cancelProgressDialog();
                        UIUtils.showToast(activity, R.string.loading_camera_info_failure);
                        activity.finish();
                        break;
                    case QUERY_SUCCESS:
                        //录像片段查询成功
                        UIUtils.cancelProgressDialog();
                        UIUtils.showToast(activity, "录像文件查询成功");
                        break;
                    case QUERY_FAILURE:
                        UIUtils.cancelProgressDialog();
                        UIUtils.showToast(activity, "录像文件查询失败");
                        break;
                    case START_SUCCESS:
                        UIUtils.cancelProgressDialog();
                        UIUtils.showToast(activity, R.string.rtsp_success);
                        PlayBackActivity.this.startUpdateTimer();
                        break;
                    case START_FAILURE:
                        UIUtils.cancelProgressDialog();
                        UIUtils.showToast(activity, R.string.rtsp_fail);
                        break;
                    case PlayBackSDKConstant.MSG_REMOTELIST_UI_UPDATE:
                        //更新播放进度条
                        activity.updateRemotePlayUI();
                        break;

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_back);
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mSurfaceView = (CustomSurfaceView) findViewById(R.id.playbackSurfaceView);
        mSurfaceView.getHolder().addCallback(this);
        mStorageTypesRG = (RadioGroup) findViewById(R.id.rg_storage_type);

        //开始按钮
        findViewById(R.id.playBackStart).setOnClickListener(this);

        //停止按钮
        findViewById(R.id.playBackStop).setOnClickListener(this);

        mPauseButton = (Button) findViewById(R.id.playBackPause);
        mPauseButton.setOnClickListener(this);

        //抓拍按钮
        findViewById(R.id.playBackCapture).setOnClickListener(this);

        mRecordButton = (Button) findViewById(R.id.playBackRecord);
        mRecordButton.setOnClickListener(this);

        mAudioButton = (Button) findViewById(R.id.playBackRadio);
        mAudioButton.setOnClickListener(this);

        mZoom = (CheckBox) findViewById(R.id.zoom);
        mZoom.setOnClickListener(this);

        mProgressSeekBar = (SeekBar) findViewById(R.id.progress_seekbar);
        mProgressSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
                stopUpdateTimer();
                int progress = arg0.getProgress();
                long begin = mFirstStartTime.getTimeInMillis();
                long end = mEndTime.getTimeInMillis();
                long avg = (end - begin) / PROGRESS_MAX_VALUE;
                long trackTime = begin + (progress * avg);
                Calendar track = Calendar.getInstance();
                track.setTimeInMillis(trackTime);
                mStartTime = track;
                VMSNetSDK.getInstance().startPlayBackOpt(PLAY_WINDOW_ONE, mSurfaceView, mRecordInfo.getSegmentListPlayUrl(), mStartTime, mEndTime, new OnVMSNetSDKBusiness() {
                    @Override
                    public void onFailure() {
                        mMessageHandler.sendEmptyMessage(START_FAILURE);
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        mMessageHandler.sendEmptyMessage(START_SUCCESS);
                    }

                    @Override
                    public void onStatusCallback(int status) {
                        //录像片段回放结束
                        if (status == RtspClient.RTSPCLIENT_MSG_PLAYBACK_FINISH) {
                            mMessageHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UIUtils.showToast(PlayBackActivity.this, R.string.play_back_finish);
                                }
                            });
                        }
                    }
                });
            }

            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mMessageHandler = new MyHandler(this);
        //监控点
        mCamera = (SubResourceNodeBean) getIntent().getSerializableExtra(Constants.IntentKey.CAMERA);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mFirstStartTime = Calendar.getInstance();
        mStartTime = Calendar.getInstance();
        mEndTime = Calendar.getInstance();
        mFirstStartTime.set(year, month, day, 0, 0, 0);
        mStartTime.set(year, month, day, 0, 0, 0);
        mEndTime.set(year, month, day, 23, 59, 59);
        getCameraInfo();
    }

    /**
     * 获取监控点详细信息
     */
    private void getCameraInfo() {
        if (null == mCamera) {
            return;
        }
        UIUtils.showLoadingProgressDialog(this, R.string.loading_camera_info);
        VMSNetSDK.getInstance().getPlayBackCameraInfo(PLAY_WINDOW_ONE, mCamera.getSysCode(), new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                mMessageHandler.sendEmptyMessage(CAMERA_INFO_FAILURE);
            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof CameraInfo) {
                    mCameraInfo = (CameraInfo) obj;
                    mMessageHandler.sendEmptyMessage(CAMERA_INFO_SUCCESS);
                }
            }
        });
    }

    /***
     * 初始化录像存储类型
     */
    private void initStorageTypeView(final int[] mRecordPos, final String[] mGuids) {
        if (mRecordPos == null || mRecordPos.length <= 0) {
            return;
        }
        for (int i = 0; i < mRecordPos.length; i++) {
            RadioButton rb = new RadioButton(PlayBackActivity.this);
            rb.setTag(i);
            switch (mRecordPos[i]) {
                case PlayBackSDKConstant.RECORD_TYPE_NVT:
                    rb.setText(PlayBackSDKConstant.RECORD_TYPE_NVT_STR);
                    break;

                case PlayBackSDKConstant.RECORD_TYPE_PU:
                    rb.setText(PlayBackSDKConstant.RECORD_TYPE_PU_STR);
                    break;

                case PlayBackSDKConstant.RECORD_TYPE_NVR:
                    rb.setText(PlayBackSDKConstant.RECORD_TYPE_NVR_STR);
                    break;

                case PlayBackSDKConstant.RECORD_TYPE_CVM:
                    rb.setText(PlayBackSDKConstant.RECORD_TYPE_CVM_STR);
                    break;

                default:
                    break;
            }
            mStorageTypesRG.addView(rb);
            mStorageTypesRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(radioButtonId);
                    String type = rb.getTag().toString();
                    int index = Integer.valueOf(type);
                    if (index < mRecordPos.length) {
                        mStorageType = mRecordPos[index];
                    }
                    if (null != mGuids && index < mGuids.length) {
                        mGuid = mGuids[index];
                    }
                    VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
                    stopUpdateTimer();
                    queryRecordSegment();
                }
            });
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        VMSNetSDK.getInstance().resumePlayBackOpt(PLAY_WINDOW_ONE);
        VMSNetSDK.getInstance().setVideoWindowOpt(PLAY_WINDOW_ONE, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        VMSNetSDK.getInstance().pausePlayBackOpt(PLAY_WINDOW_ONE);
        VMSNetSDK.getInstance().setVideoWindowOpt(PLAY_WINDOW_ONE, null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.playBackStart) {
            if (mRecordInfo == null) return;
            //开始回放按钮点击操作
            UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip);
            VMSNetSDK.getInstance().startPlayBackOpt(PLAY_WINDOW_ONE, mSurfaceView, mRecordInfo.getSegmentListPlayUrl(), mStartTime, mEndTime, new OnVMSNetSDKBusiness() {
                @Override
                public void onFailure() {
                    mMessageHandler.sendEmptyMessage(START_FAILURE);
                }

                @Override
                public void onSuccess(Object obj) {
                    mMessageHandler.sendEmptyMessage(START_SUCCESS);
                }

                @Override
                public void onStatusCallback(int status) {
                    //录像片段回放结束
                    if (status == RtspClient.RTSPCLIENT_MSG_PLAYBACK_FINISH) {
                        UIUtils.showToast(PlayBackActivity.this, R.string.play_back_finish);
                    }
                }
            });

        } else if (i == R.id.playBackStop) {//停止回放按钮点击操作
            boolean stopPlayBackOpt = VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
            if (stopPlayBackOpt) {
                stopUpdateTimer();
                UIUtils.showToast(this, R.string.live_stop_success);
            }

        } else if (i == R.id.playBackPause) {//暂停、恢复按钮点击操作
            if (!mIsPause) {
                boolean pausePlayBackOpt = VMSNetSDK.getInstance().pausePlayBackOpt(PLAY_WINDOW_ONE);
                if (pausePlayBackOpt) {
                    UIUtils.showToast(this, "暂停成功");
                    mPauseButton.setText("恢复");
                    mIsPause = true;
                } else {
                    UIUtils.showToast(this, "暂停失败");
                    mPauseButton.setText("暂停");
                    mIsPause = false;
                }
            } else {
                boolean resumePlayBackOpt = VMSNetSDK.getInstance().resumePlayBackOpt(PLAY_WINDOW_ONE);
                if (resumePlayBackOpt) {
                    UIUtils.showToast(this, "恢复播放成功");
                    mPauseButton.setText("暂停");
                    mIsPause = false;
                } else {
                    UIUtils.showToast(this, "恢复播放失败");
                    mPauseButton.setText("恢复");
                    mIsPause = true;
                }
            }

        } else if (i == R.id.playBackCapture) {//抓拍按钮点击操作
            int opt = VMSNetSDK.getInstance().capturePlaybackOpt(PLAY_WINDOW_ONE, FileUtils.getPictureDirPath().getAbsolutePath(),
                    "Picture" + System.currentTimeMillis() + ".jpg");
            switch (opt) {
                case PlayBackSDKConstant.SD_CARD_UN_USABLE:
                    UIUtils.showToast(this, R.string.sd_card_fail);
                    break;
                case PlayBackSDKConstant.SD_CARD_SIZE_NOT_ENOUGH:
                    UIUtils.showToast(this, R.string.sd_card_not_enough);
                    break;
                case PlayBackSDKConstant.CAPTURE_FAILED:
                    UIUtils.showToast(this, R.string.capture_fail);
                    break;
                case PlayBackSDKConstant.CAPTURE_SUCCESS:
                    UIUtils.showToast(this, R.string.capture_success);
                    break;
            }

        } else if (i == R.id.playBackRecord) {//录像按钮点击操作
            if (!mIsRecord) {
                int recordOpt = VMSNetSDK.getInstance().startPlayBackRecordOpt(PLAY_WINDOW_ONE, FileUtils.getVideoDirPath().getAbsolutePath(), "Video" + System.currentTimeMillis() + ".mp4");
                switch (recordOpt) {
                    case SDKConstant.LiveSDKConstant.SD_CARD_UN_USABLE:
                        UIUtils.showToast(this, R.string.sd_card_fail);
                        break;
                    case SDKConstant.LiveSDKConstant.SD_CARD_SIZE_NOT_ENOUGH:
                        UIUtils.showToast(this, R.string.sd_card_not_enough);
                        break;
                    case PlayBackSDKConstant.RECORD_FAILED:
                        mIsRecord = false;
                        UIUtils.showToast(this, R.string.start_record_fail);
                        break;
                    case PlayBackSDKConstant.RECORD_SUCCESS:
                        mIsRecord = true;
                        UIUtils.showToast(this, R.string.start_record_success);
                        mRecordButton.setText(R.string.stop_record);
                        break;
                }
            } else {
                VMSNetSDK.getInstance().stopPlayBackRecordOpt(PLAY_WINDOW_ONE);
                mIsRecord = false;
                UIUtils.showToast(PlayBackActivity.this, R.string.stop_record_success);
                mRecordButton.setText(R.string.start_record);
            }

        } else if (i == R.id.playBackRadio) {//音频按钮点击操作
            if (mIsAudioOpen) {
                VMSNetSDK.getInstance().stopPlayBackAudioOpt(PLAY_WINDOW_ONE);
                mIsAudioOpen = false;
                UIUtils.showToast(PlayBackActivity.this, "关闭音频");
                mAudioButton.setText("开启音频");
            } else {
                boolean retAudio = VMSNetSDK.getInstance().startPlayBackAudioOpt(PLAY_WINDOW_ONE);
                if (!retAudio) {
                    mIsAudioOpen = false;
                    UIUtils.showToast(PlayBackActivity.this, "开启音频失败");
                } else {
                    mIsAudioOpen = true;
                    // 开启音频成功，并不代表一定有声音，需要设备开启声音。
                    UIUtils.showToast(PlayBackActivity.this, "开启音频成功");
                    mAudioButton.setText("关闭音频");
                }
            }

        } else if (i == R.id.zoom) {//电子放大选中操作
            boolean isZoom = mZoom.isChecked();
            if (isZoom) {
                mSurfaceView.setOnZoomListener(new OnZoomListener() {

                    @Override
                    public void onZoomChange(CustomRect original, CustomRect current) {
                        VMSNetSDK.getInstance().zoomPlayBackOpt(PLAY_WINDOW_ONE, true, original, current);
                    }
                });
            } else {
                mSurfaceView.setOnZoomListener(null);
                VMSNetSDK.getInstance().zoomPlayBackOpt(PLAY_WINDOW_ONE, false, null, null);
            }

        } else if (i == R.id.speed14_btn) {
            if (!mIsPause) {
                VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, PlaybackSpeed.QUARTER);
            } else {
                Toast.makeText(this, "非播放状态不能调节倍速！", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.speed12_btn) {
            if (!mIsPause) {
                VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, PlaybackSpeed.HALF);
            } else {
                Toast.makeText(this, "非播放状态不能调节倍速！", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.speed1_btn) {
            if (!mIsPause) {
                VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, PlaybackSpeed.NORMAL);
            } else {
                Toast.makeText(this, "非播放状态不能调节倍速！", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.speed2_btn) {
            if (!mIsPause) {
                VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, PlaybackSpeed.DOUBLE);
            } else {
                Toast.makeText(this, "非播放状态不能调节倍速！", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.speed4_btn) {
            if (!mIsPause) {
                VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, PlaybackSpeed.FOUR);
            } else {
                Toast.makeText(this, "非播放状态不能调节倍速！", Toast.LENGTH_SHORT).show();
            }

        } else {
        }
    }

    /**
     * 查找录像片段
     */
    private void queryRecordSegment() {
        if (null == mCameraInfo) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar queryStartTime = Calendar.getInstance();
        Calendar queryEndTime = Calendar.getInstance();
        queryStartTime.set(year, month, day, 0, 0, 0);
        queryEndTime.set(year, month, day, 23, 59, 59);
        UIUtils.showLoadingProgressDialog(this, R.string.loading_record_info);
        VMSNetSDK.getInstance().queryRecordSegment(PLAY_WINDOW_ONE, mCameraInfo, queryStartTime, queryEndTime, mStorageType, mGuid, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                mMessageHandler.sendEmptyMessage(QUERY_FAILURE);
            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof RecordInfo) {
                    mRecordInfo = ((RecordInfo) obj);
                    if (null != mRecordInfo.getSegmentList() && 0 < mRecordInfo.getSegmentList().size()) {
                        mRecordSegment = mRecordInfo.getSegmentList().get(0);
                        //级联设备的时候使用录像片段中的时间
                        if (SDKConstant.CascadeFlag.CASCADE == mCameraInfo.getCascadeFlag()) {
                            mEndTime = SDKUtil.convertTimeString(mRecordSegment.getEndTime());
                            mStartTime = SDKUtil.convertTimeString(mRecordSegment.getBeginTime());
                            mFirstStartTime = mStartTime;
                        }
                        mMessageHandler.sendEmptyMessage(QUERY_SUCCESS);
                    } else {
                        mMessageHandler.sendEmptyMessage(QUERY_FAILURE);
                    }
                }
            }
        });

    }

    /***
     * 更新播放库UI
     */
    private void updateRemotePlayUI() {
        //获取播放进度
        long osd = VMSNetSDK.getInstance().getOSDTimeOpt(PLAY_WINDOW_ONE);
        if (osd != -1) {
            handlePlayProgress(osd);
        }
    }

    /***
     * 更新播放进度
     *
     * @param osd 播放进度
     */
    private void handlePlayProgress(long osd) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(osd);
        long begin = mFirstStartTime.getTimeInMillis();
        long end = mEndTime.getTimeInMillis();

        double x = ((osd - begin) * PROGRESS_MAX_VALUE) / (double) (end - begin);
        int progress = (int) (x);
        mProgressSeekBar.setProgress(progress);
    }

    /**
     * 启动定时器
     */
    private void startUpdateTimer() {
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                mMessageHandler.sendEmptyMessage(PlayBackSDKConstant.MSG_REMOTELIST_UI_UPDATE);
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
}
