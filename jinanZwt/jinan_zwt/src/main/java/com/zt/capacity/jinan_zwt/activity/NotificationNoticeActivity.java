package com.zt.capacity.jinan_zwt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.DateUtil;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.PushRecord;
import com.zt.capacity.jinan_zwt.request.push.IPushManager;
import com.zt.capacity.jinan_zwt.request.push.PushManager;


public class NotificationNoticeActivity extends BaseActivity implements View.OnClickListener {
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    //控件
    private TextView notification_notice_title;//标题
    private TextView notification_notice_user_name;//发送人姓名
    private TextView notification_notice_cotent;//内容
    private TextView notification_notice_time;//发送时间

    private PushRecord pushRecord;//通知数据


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_notification_notice);
        init();//初始化控件


        pushRecord = (PushRecord) getIntent().getSerializableExtra("pushRecord");
        Log.e("dadadadad.....", pushRecord + "");
        if (pushRecord != null) {
            //写入数据
            setData();
        }

    }

    private void setData() {

        notification_notice_title.setText(pushRecord.getPushRecordTitle() == null ? "" : pushRecord.getPushRecordTitle());//标题
//        notification_notice_user_name.setText(pushRecord.getRealName() == null ? "" : pushRecord.getRealName());//发送人姓名
        notification_notice_user_name.setText("");//发送人姓名
        String cotentStr = pushRecord.getPushRecordContent() == null ? "" : pushRecord.getPushRecordContent();
        if (!TextUtils.isEmpty(cotentStr) && cotentStr.indexOf("\\n") >= 0)
            cotentStr = cotentStr.replace("\\n", "\n");
        notification_notice_cotent.setText(cotentStr);//内容
        Long time = pushRecord.getPushRecordTime() == null ? 0 : pushRecord.getPushRecordTime();
        String tiemx = "";
        if (time != 0) {
            tiemx = DateUtil.showDate(DateUtil.DATE_FORMAT_YMD_Z, time);
        }
        notification_notice_time.setText(tiemx);//发送时间
    }


    private void init() {
        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("消息");


        notification_notice_title = findViewById(R.id.notification_notice_title);//标题
        notification_notice_user_name = findViewById(R.id.notification_notice_user_name);//发送人姓名
        notification_notice_cotent = findViewById(R.id.notification_notice_cotent);//内容
        notification_notice_time = findViewById(R.id.notification_notice_time);//发送时间
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTXiaoXiTongZhiXinXi"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTXiaoXiTongZhiXinXi"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
