package com.zt.capacity.jinan_zwt.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.common.view.LoadListView;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.adapter.NotificationListAdapter;
import com.zt.capacity.jinan_zwt.bean.PushRecord;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;
import com.zt.capacity.jinan_zwt.request.push.IPushManager;
import com.zt.capacity.jinan_zwt.request.push.PushManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知
 */
public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    private LoadListView notification_list;//内容数据
    private NotificationListAdapter notificationListAdapter;

    private LinearLayout no_msg;//无消息

    //分页
    private Integer current = 1;
    private Integer size = 16;

    //消息数据
    private List<PushRecord> pushRecords = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_notification);

        init();

        //请求消息数据
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        getData();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //请求成功
                    UIUtils.cancelProgressDialog();
                    if (current == 1) {
                        listSetData();
                    } else {
                        //刷新详细信息数据
                        notificationListAdapter.notifyDataSetChanged();
                        notification_list.setLoadCompleted();
                    }
                    break;
                case 1:
                    //请求失败
                    UIUtils.cancelProgressDialog();
                    break;
            }
        }
    };

    private void getData() {
        IPushManager pushManager = PushManager.getInterface(2);
        pushManager.getPushRecord(current, size, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        List<PushRecord> pushRecordsx = (List<PushRecord>) body;
                        pushRecords.addAll(pushRecordsx);
                    }
                    message.what = state;
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);

            }
        });
    }

    private void init() {
        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("消息通知");

        notification_list = findViewById(R.id.notification_list);
        no_msg=findViewById(R.id.no_msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img:
                finish();
                break;
        }

    }

    //listView写入数据
    private void listSetData() {
        if(pushRecords.size()>0){
            //有消息
            notification_list.setVisibility(View.VISIBLE);
            no_msg.setVisibility(View.GONE);
            notificationListAdapter = new NotificationListAdapter(this, pushRecords);
            notification_list.setAdapter(notificationListAdapter);
            notification_list.setOnLoadMoreListener(new LoadListView.OnLoadMoreListener() {
                @Override
                public void onloadMore() {
                    current = current + 1;
                    getData();
                }
            });
            notification_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PushRecord pushRecord = pushRecords.get(i);
//                if ("通知公告".equals(pushRecord.getPushGenreName())) {

                    if (pushRecord.getPushState() == null || pushRecord.getPushState() == 0) {
                        //未读
                        pushRecords.get(i).setPushState(1);
                        notificationListAdapter.notifyDataSetChanged();//重绘当前可见区域

                        //数据标记为已读
                        IPushManager pushManager = PushManager.getInterface(3);
                        pushManager.putRead(pushRecord.getPushRecordId(), 1, new OnNetResultListener() {
                            @Override
                            public void onResult(int state, String msg, Object body) {

                            }
                        });
                    }
                    if(pushRecord.getPushGenreId()==2){
                        ActivityJumpUtil.jumpActivityByString(NotificationActivity.this,WebViewActivity.class, JN_ZWT_Url.WISDOMLAWDETAIL_A+Web.getToken()+"&pushDataId="+pushRecord.getPushDataId(),"url");
                    }else{
                        ActivityJumpUtil.jumpActivityByObject(NotificationActivity.this, NotificationNoticeActivity.class, pushRecord, "pushRecord");
                    }
//
// } else {
//
//                }
                }
            });
        }else {
            //无消息
            notification_list.setVisibility(View.GONE);
            no_msg.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTXiaoXiTongZhi"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTXiaoXiTongZhi"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
