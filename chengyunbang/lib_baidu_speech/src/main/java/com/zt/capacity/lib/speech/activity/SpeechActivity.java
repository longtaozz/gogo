package com.zt.capacity.lib.speech.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hjq.toast.ToastUtils;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.lib.speech.R;
import com.zt.capacity.lib.speech.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 模拟使用语音识别
 *
 * @author lt
 * @time 2018/12/13 17:29
 **/
public class SpeechActivity extends ActivityRecog implements View.OnClickListener {

    protected static ImageView mVoiceImageview;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mVoiceImageview.performClick();
                    break;

            }
        }
    };

    Handler discernHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleMsg(msg);
        }
    };

    public SpeechActivity() {
        super(handler);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);


        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.REQUEST_INSTALL_PACKAGES, "安装", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写", com.zt.capacity.common.R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(android.Manifest.permission.RECORD_AUDIO, "录音", com.zt.capacity.common.R.drawable.permission_ic_micro_phone));
        permissionItems.add(new PermissionItem(android.Manifest.permission.ACCESS_NETWORK_STATE, "网络状态", com.zt.capacity.common.R.drawable.permission_ic_sensors));
        permissionItems.add(new PermissionItem(android.Manifest.permission.INTERNET, "联网", com.zt.capacity.common.R.drawable.permission_ic_sensors));
        permissionItems.add(new PermissionItem(android.Manifest.permission.READ_PHONE_STATE, "手机状态", com.zt.capacity.common.R.drawable.permission_ic_phone));

        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(null);

        init();

        Logger.setHandler(handler);
        initRecog(discernHandler);
    }

    private void init() {
        mVoiceImageview=findViewById(R.id.mVoiceImageview);
        mVoiceImageview.setOnClickListener(this);
    }

    @Override
    protected void clocePopuo() {
        //关闭弹出层
        if (VoiceMaskActivity.instance != null) {
            VoiceMaskActivity.instance.finish();
        }
    }

    @Override
    protected void popuo() {
        //弹出语音层
        ActivityJumpUtil.jumpActivity(SpeechActivity.this, VoiceMaskActivity.class);
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_FINISHED:
                if (msg.arg2 == 1) {
                    clocePopuo();
                    String str = msg.obj.toString();
//                    txtResult.setText(msg.obj.toString());
                    Log.e("str......", msg.obj.toString());

                    //识别处理
                    if (str.substring(0, 4).endsWith("识别错误")) {
                        ToastUtils.show("没有找到对应信息");
                        return;
                    }
                    str = str.substring(9, str.indexOf("；"));
                    Log.e("str......", str);
                    String stSmg = str.substring(0, str.length() - 1);
                    if (stSmg.endsWith("我的许可证")) {
//                        ActivityJumpUtil.jumpActivity(this, QueryActivity.class);
                    } else {
                        ToastUtils.show("没有找到对应信息");
                    }
                }
                status = msg.what;
                updateBtnTextByStatus();
                break;
            case STATUS_NONE:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                status = msg.what;
                updateBtnTextByStatus();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVoiceImageview = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.mVoiceImageview){
            //语音按钮点击
            activate();
        }
    }
}
