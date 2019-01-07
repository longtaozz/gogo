package com.zt.capacity.jinan_zwt.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.debug.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.CustomDialog;
import com.zt.capacity.common.util.IToast;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.adapter.JNInstructionCarAdapter;
import com.zt.capacity.jinan_zwt.adapter.JNInstructionRecordAdapter;
import com.zt.capacity.jinan_zwt.bean.Car;
import com.zt.capacity.jinan_zwt.bean.DataActionBean;
import com.zt.capacity.jinan_zwt.request.instruction.InstructionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 指令下发
 */
public class JNInstructionActivity extends BaseActivity implements View.OnClickListener {

    private Integer stateA = 0;//用来区分是否为其它页面跳转而来  1为true

    //汽车信息数据
    private Car carA;
    //汽车信息集合
    private List<Car> carsB = new ArrayList<>();
    //历史数据信息
    List<DataActionBean> dataActionBs = new ArrayList<>();

    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    //主体控件
    private ListView jn_instruction_list_car;//车辆信息
    private ListView jn_instruction_list_record;//指令数据历史信息
    //锁车、解除锁车、限速、解除限速
    private LinearLayout lock_car, relieve_lock_car, speed_limit, relieve_speed_limit;
    //限举、解除限举、开启管控、解除管控、
    private LinearLayout limit_lift, relieve_limit_lift, control, relieve_control;
    //解除指纹、下发抓拍、刷新
    private LinearLayout relieve_fingerprint, snap, refresh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_instructions);
        carA = (Car) getIntent().getSerializableExtra("car");
        if (carA != null) {
            //证明是从其它页面跳过来的
            stateA = 1;
            carsB.add(carA);
        }

        init();//初始化控件
        instructionHistoryData();//历史指令信息请求
        setFrontData();//写入初始信息
    }


    //历史指令信息请求
    private void instructionHistoryData() {
        if (stateA == 1) {
            Log.e("histort.>>>>>>>>>>>>>>.", "开始请求");
            //其它页面跳入查单个车辆指令下发的历史信息
            UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
            InstructionManager.getInterface(1).getHistoryInstruction(carA.getNumberPlate(), new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    Message message = new Message();
                    UIUtils.cancelProgressDialog();
                    if (state == 0) {
                        //请求成功
                        Log.e("shujulishi...", body + "");
                        if (body != null) {
                            dataActionBs = (List<DataActionBean>) body;
                        }
                        message.what = 0;
                    } else {
                        //请求失败
                        message.what = 1;
                        message.obj = msg;
                    }
                    handler.sendMessage(message);
                }
            });
        }
    }

    //指令发送请求
    public void sendInstruction(String name, final String carPassPhoneNumber, final Integer actionType, final Integer actionValue, final String sendRemark) {

        CustomDialog.Builder builder2 = new CustomDialog.Builder(this);
        builder2.setMessage("确定发送"+name);
        builder2.setTitle("下发指令");
        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder2.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UIUtils.showLoadingProgressDialog(JNInstructionActivity.this, R.string.loading_process_tip, false);
                        InstructionManager.getInterface(2).sendInstruction(carPassPhoneNumber, actionType, actionValue, sendRemark, new OnNetResultListener() {

                            @Override
                            public void onResult(int state, String msg, Object body) {
                                Message message = new Message();
                                UIUtils.cancelProgressDialog();
                                if (state == 0) {
                                    //请求成功
                                    message.what = 3;
                                    message.obj = body;
                                } else {
                                    //请求失败
                                    message.what = 1;
                                    message.obj = msg;
                                }
                                handler.sendMessage(message);

                            }
                        });
                        dialog.dismiss();
                    }
                });

        builder2.create().show();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //请求成功
                    Log.e("shujulishi...", dataActionBs.size() + "");
                    if (dataActionBs.size() > 0) {
                        Log.e("shujulishi...", dataActionBs.size() + "");
                        //写入历史数据信息
                        JNInstructionRecordAdapter instructionRecordAdapter = new JNInstructionRecordAdapter(JNInstructionActivity.this, dataActionBs);
                        jn_instruction_list_record.setAdapter(instructionRecordAdapter);
                    }
                    break;
                case 1:
                    //请求失败
                    IToast.show(JNInstructionActivity.this, msg.obj.toString());
                    break;
                case 3:
                    //提交成功
                    if (msg.obj.equals("1")) {
                        IToast.show(JNInstructionActivity.this, "指令下发成功");
                        instructionHistoryData();//历史指令信息请求
                    } else {
                        IToast.show(JNInstructionActivity.this, "指令下发失败");
                    }
                    break;
            }
        }
    };

    //写入初始信息
    private void setFrontData() {
        //车辆信息数据写入
        JNInstructionCarAdapter instructionCarAdapter = new JNInstructionCarAdapter(this, carsB, stateA);
        jn_instruction_list_car.setAdapter(instructionCarAdapter);
    }

    private void init() {
        //头布局
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("下发指令");

        //主体控件
        jn_instruction_list_car = findViewById(R.id.jn_instruction_list_car);//车辆信息
        jn_instruction_list_record = findViewById(R.id.jn_instruction_list_record);//指令数据历史信息
        //锁车、解除锁车、限速、解除限速
        lock_car = findViewById(R.id.lock_car);
        lock_car.setOnClickListener(this);
        relieve_lock_car = findViewById(R.id.relieve_lock_car);
        relieve_lock_car.setOnClickListener(this);
        speed_limit = findViewById(R.id.speed_limit);
        speed_limit.setOnClickListener(this);
        relieve_speed_limit = findViewById(R.id.relieve_speed_limit);
        relieve_speed_limit.setOnClickListener(this);
        //限举、解除限举、开启管控、解除管控、
        limit_lift = findViewById(R.id.limit_lift);
        limit_lift.setOnClickListener(this);
        relieve_limit_lift = findViewById(R.id.relieve_limit_lift);
        relieve_limit_lift.setOnClickListener(this);
        control = findViewById(R.id.control);
        control.setOnClickListener(this);
        relieve_control = findViewById(R.id.relieve_control);
        relieve_control.setOnClickListener(this);
        //解除指纹、下发抓拍、刷新
        relieve_fingerprint = findViewById(R.id.relieve_fingerprint);
        relieve_fingerprint.setOnClickListener(this);
        snap = findViewById(R.id.snap);
        snap.setOnClickListener(this);
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        String carPassPhoneNumber = carA.getNumberPlate() + "," + carA.getSimNumber();
        int i = view.getId();
        if (i == R.id.title_img) {
            finish();

        } else if (i == R.id.lock_car) {
            sendInstruction("锁车", carPassPhoneNumber, 1, 1, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNSuoChe");

        } else if (i == R.id.relieve_lock_car) {
            sendInstruction("解除锁车", carPassPhoneNumber, 1, 0, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNJieChuSuoChe");

        } else if (i == R.id.speed_limit) {//第三个参数为限速值，必须大于0
            sendInstruction("限速", carPassPhoneNumber, 2, 23, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNXianSu");

        } else if (i == R.id.relieve_speed_limit) {
            sendInstruction("解除限速", carPassPhoneNumber, 2, 0, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNJieChuXianSu");

        } else if (i == R.id.limit_lift) {
            sendInstruction("限举", carPassPhoneNumber, 3, 1, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNXianJu");

        } else if (i == R.id.relieve_limit_lift) {
            sendInstruction("解除限举", carPassPhoneNumber, 3, 0, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNJieChuXianJu");

        } else if (i == R.id.control) {//第三个参数4为开启管控
            sendInstruction("开启管控", carPassPhoneNumber, 6, 4, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNKaiQiGuanKong");

        } else if (i == R.id.relieve_control) {//第三个参数2为解除管控
            sendInstruction("解除管控", carPassPhoneNumber, 6, 2, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNJieChuGuanKong");

        } else if (i == R.id.relieve_fingerprint) {//第三个参数
            sendInstruction("解除指纹", carPassPhoneNumber, 6, 1, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNJieChuZhiWen");

        } else if (i == R.id.snap) {//第三个参数2为解除管控
            sendInstruction("下发抓拍", carPassPhoneNumber, 4, 0, "");
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNXiaFaZhuaPai");

        } else if (i == R.id.refresh) {
            instructionHistoryData();//历史指令信息请求

        }
    }

    //弹出提示框
    public void outtishik(){
        CustomDialog.Builder builder2 = new CustomDialog.Builder(this);
        builder2.setMessage("是否要退出");
        builder2.setTitle("退出登录");
        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder2.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(MainActivity.mainActivity!=null){
                            MainActivity.mainActivity.finish();
                        };
//                                finish();
                        SharedPreferences sp = BaseApplication.getContext()
                                .getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", "");
                        editor.commit();// 提交修改
                        Web.setToken("");
                        ActivityJumpUtil.jumpActivity(JNInstructionActivity.this,LoginActivity.class);
                    }
                });

        builder2.create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZhiLingXiaFa"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZhiLingXiaFa"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
