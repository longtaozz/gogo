package com.zt.capacity.jinan_yunshu.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.zt.capacity.baidumap.adapter.MatchingAdapter;
import com.zt.capacity.baidumap.bean.CarRuntime;
import com.zt.capacity.baidumap.request.history.HistoryManager;
import com.zt.capacity.baidumap.request.history.IHistoryManager;
import com.zt.capacity.baidumap.request.realtimeposition.IRealTimePosition;
import com.zt.capacity.baidumap.request.realtimeposition.RealTimePosition;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.CustomDialog;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.adapter.InstructionRecordAdapter;
import com.zt.capacity.jinan_yunshu.bean.DataActionBean;
import com.zt.capacity.jinan_yunshu.request.instruction.InstructionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 指令下发
 */
@Route(path = "/jinan/yunshu/instruction")
public class InstructionActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private Integer stateA = 0;//用来区分是否为其它页面跳转而来  1为true

    @Autowired()
    public String carNumber;//车牌号

    //汽车信息数据
    private CarRuntime carRuntime;
    private Car car;
    //汽车信息集合
    private List<Car> cars = new ArrayList<>();
    //历史数据信息
    List<DataActionBean> dataActionBs = new ArrayList<>();

    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    //主体控件
    private ListView jn_instruction_list_record;//指令数据历史信息
    //锁车、解除锁车、限速、解除限速
    private LinearLayout lock_car, relieve_lock_car, speed_limit, relieve_speed_limit;
    //限举、解除限举、开启管控、解除管控、
    private LinearLayout limit_lift, relieve_limit_lift, control, relieve_control;
    //解除指纹、下发抓拍、刷新
    private LinearLayout relieve_fingerprint, snap, refresh;

    private LinearLayout mSearchImageview;//搜索框
    private AutoCompleteTextView realTimeLicensePlate;

    //基础信息
    private TextView instruction_car_number;//车牌
    private TextView instruction_driver;//驾驶员
    private TextView instruction_tel;//联系电话
    private TextView instruction_car_state;//车辆状态
    private TextView instruction_lcoation_msg;//位置信息

    private LinearLayout car_linear_t;//车辆信息
    private LinearLayout car_linear_f;//车辆信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_yunshu_activity_instructions);

        //inject
        ARouter.getInstance().inject(this);

        init();//初始化控件


        Log.e("car.....", carNumber + "");
        if (carNumber != null && !TextUtils.isEmpty(carNumber)) {
            //证明是从其它页面跳过来的
            stateA = 1;
        } else {
            carNumber = "";
        }
        //请求所有车辆信息
        if (Web.cars.size() > 0) {
            //配置搜索框
            cars = Web.cars;
            setMatching();
            if (stateA == 1) {
                realTimeLicensePlate.setText(carNumber);
                search(carNumber);
            }
        } else {
            selectAllMyCar();
            if (stateA == 1) {
                realTimeLicensePlate.setText(carNumber);
                search(carNumber);
            }
        }
        car_linear_f.setVisibility(View.VISIBLE);
        car_linear_t.setVisibility(View.GONE);
    }

    private void selectAllMyCar() {
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.arg1 = state;
                if (state == 0) {
                    message.what = 100002;
                    if (body != null) {
                        Web.cars = (List<Car>) body;
                    }
                    List<Car> cars = (List<Car>) body;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cars", (Serializable) cars);
                    message.setData(bundle);
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });


    }


    private void setMatching() {
        MatchingAdapter adapter = new MatchingAdapter(this, cars);
        realTimeLicensePlate.setAdapter(adapter);
        realTimeLicensePlate.setThreshold(0);
    }


    //历史指令信息请求
    private void instructionHistoryData() {
        Log.e("histort.>>>>>>>>>>>>>>.", "开始请求");
        //其它页面跳入查单个车辆指令下发的历史信息
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        InstructionManager.getInterface(1).getHistoryInstruction(carNumber, new OnNetResultListener() {
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

    //指令发送请求
    public void sendInstruction(String name, final String carPassPhoneNumber, final Integer actionType, final Integer actionValue, final String sendRemark) {

        CustomDialog.Builder builder2 = new CustomDialog.Builder(this);
        builder2.setMessage("确定发送" + name);
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
                        UIUtils.showLoadingProgressDialog(InstructionActivity.this, R.string.loading_process_tip, false);
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
                    UIUtils.cancelProgressDialog();
                    Log.e("shujulishi...", dataActionBs.size() + "");
                    //写入历史数据信息
                    InstructionRecordAdapter instructionRecordAdapter = new InstructionRecordAdapter(InstructionActivity.this, dataActionBs);
                    jn_instruction_list_record.setAdapter(instructionRecordAdapter);
                    break;
                case 1:
                    //请求失败
                    UIUtils.cancelProgressDialog();
                    ToastUtils.show(msg.obj.toString());
                    break;
                case 3:
                    //提交成功
                    if (msg.obj.equals("1")) {
                        ToastUtils.show("指令下发成功");
                        instructionHistoryData();//历史指令信息请求
                    } else {
                        ToastUtils.show("指令下发失败");
                    }
                    break;
                case 100002:
                    if (msg.arg1 == 0) {
                        cars = (List<Car>) msg.getData().getSerializable("cars");
                        if (cars.size() > 0) {
                            Web.cars = cars;
                        }
                    }
                    //配置搜索框
                    setMatching();
                    break;
                case 100006:
                    //搜索框搜索信息
                    if (carRuntime != null) {
                        carNumber = carRuntime.getCarNumber();
                        carData();
                        instructionHistoryData();
                    } else {
                        UIUtils.cancelProgressDialog();
                        carNumber = null;
                        carRuntime = null;
                        car = null;
                        dataActionBs=new ArrayList<>();
                        ToastUtils.show("无此车辆或车辆未在线");
                        setFrontData();
                        //写入历史数据信息
                        InstructionRecordAdapter instructionRecordAdapterx = new InstructionRecordAdapter(InstructionActivity.this, dataActionBs);
                        jn_instruction_list_record.setAdapter(instructionRecordAdapterx);
                    }
                    break;

                case 100007:
                    UIUtils.cancelProgressDialog();
                    setFrontData();
                    break;
            }
        }
    };

    private void carData() {
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(3);
        realTimePosition.carsByCarNumber(carRuntime.getCarNumber(), new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.e("fragment1xxxx", state + "");
                Message message = new Message();
                if (state == 0) {
                    car = (Car) body;
                    message.what = 100007;

                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });

    }

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    //写入初始信息
    private void setFrontData() {
        //车辆信息数据写入
        if (carRuntime != null && cars.size() > 0) {
            car_linear_t.setVisibility(View.VISIBLE);
            car_linear_f.setVisibility(View.GONE);
            instruction_car_number.setText(carRuntime.getCarNumber() == null ? "" : carRuntime.getCarNumber());
            instruction_driver.setText(car.getDrivername() == null ? "" : car.getDrivername());
            instruction_tel.setText(car.getDrivertel() == null ? "" : car.getDrivertel());

            String stateC = "";//车辆状态
            if (carRuntime.getBoxEmpty() == 1) {//空中车判断
                stateC = stateC + "空车 ";
            } else {
                stateC = stateC + "重车 ";
            }
            if (carRuntime.getBoxClose() == 1) {
                stateC = stateC + "关厢 ";
            } else {
                stateC = stateC + "开厢 ";
            }
            if (carRuntime.getBoxUp() == 1) {
                stateC = stateC + "平放 ";
            } else {
                stateC = stateC + "举升 ";
            }
            instruction_car_state.setText(stateC);

        } else {
            car_linear_f.setVisibility(View.VISIBLE);
            car_linear_t.setVisibility(View.GONE);
            instruction_car_number.setText("");
            instruction_driver.setText("");
            instruction_tel.setText("");
            instruction_car_state.setText("");
        }
    }

    private void init() {
        //头布局
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("下发指令");

        //主体控件
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


        mSearchImageview = findViewById(com.zt.capacity.baidumap.R.id.search_imageview);
        mSearchImageview.setOnClickListener(this);
        realTimeLicensePlate = findViewById(com.zt.capacity.baidumap.R.id.search);
        realTimeLicensePlate.setOnEditorActionListener(this);


        //基础信息
        instruction_car_number = findViewById(R.id.instruction_car_number);//车牌
        instruction_driver = findViewById(R.id.instruction_driver);//驾驶员
        instruction_tel = findViewById(R.id.instruction_tel);//联系电话
        instruction_car_state = findViewById(R.id.instruction_car_state);//车辆状态
        instruction_lcoation_msg = findViewById(R.id.instruction_lcoation_msg);//位置信息

        car_linear_f = findViewById(R.id.car_linear_f);
        car_linear_t = findViewById(R.id.car_linear_t);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_img) {
            finish();

        } else if (i == com.zt.capacity.baidumap.R.id.search_imageview) {//搜索处理
//            hideKeyboard();
            String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
            if (!Pattern.matches(pattern, realTimeLicensePlate.getText().toString())) {
                ToastUtils.show("请输入正确的车牌");
                return;
            }
            search(realTimeLicensePlate.getText().toString());
        } else {
            String carPassPhoneNumber = "";
            if (car != null) {
                carPassPhoneNumber = car.getNumberPlate() + "," + car.getSimNumber();
            } else {
                ToastUtils.show("请输入正确的车牌");
                return;
            }


            if (i == R.id.lock_car) {
                sendInstruction("锁车", carPassPhoneNumber, 1, 1, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNSuoChe");

            } else if (i == R.id.relieve_lock_car) {
                sendInstruction("解除锁车", carPassPhoneNumber, 1, 0, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNJieChuSuoChe");

            } else if (i == R.id.speed_limit) {//第三个参数为限速值，必须大于0
                sendInstruction("限速", carPassPhoneNumber, 2, 23, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNXianSu");

            } else if (i == R.id.relieve_speed_limit) {
                sendInstruction("解除限速", carPassPhoneNumber, 2, 0, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNJieChuXianSu");

            } else if (i == R.id.limit_lift) {
                sendInstruction("限举", carPassPhoneNumber, 3, 1, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNXianJu");

            } else if (i == R.id.relieve_limit_lift) {
                sendInstruction("解除限举", carPassPhoneNumber, 3, 0, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNJieChuXianJu");

            } else if (i == R.id.control) {//第三个参数4为开启管控
                sendInstruction("开启管控", carPassPhoneNumber, 6, 4, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNKaiQiGuanKong");

            } else if (i == R.id.relieve_control) {//第三个参数2为解除管控
                sendInstruction("解除管控", carPassPhoneNumber, 6, 2, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNJieChuGuanKong");

            } else if (i == R.id.relieve_fingerprint) {//第三个参数
                sendInstruction("解除指纹", carPassPhoneNumber, 6, 1, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNJieChuZhiWen");

            } else if (i == R.id.snap) {//第三个参数2为解除管控
                sendInstruction("下发抓拍", carPassPhoneNumber, 4, 0, "");
                UmengHelper.onEvent(BaseApplication.getContext(), "JNXiaFaZhuaPai");

            } else if (i == R.id.refresh) {
                instructionHistoryData();//历史指令信息请求

            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
            if (!Pattern.matches(pattern, realTimeLicensePlate.getText().toString())) {
                ToastUtils.show("请输入正确的车牌");
                return false;
            }
            search(realTimeLicensePlate.getText().toString());
            return true;
        }
        return false;
    }

    //搜索
    private void search(String carNumber) {
        UIUtils.showLoadingProgressDialog(this, com.zt.capacity.baidumap.R.string.loading_process_tip, false);
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(2);
        realTimePosition.carRuntimeByCarNumber(carNumber, "", new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100006;
                message.obj = msg;
                if (state == 0) {
                    //操作成功
                    if (body != null) {
                        List<CarRuntime> carRuntimes = (List<CarRuntime>) body;
                        if (carRuntimes.size() > 0) {
                            carRuntime = carRuntimes.get(0);
                        } else {
                            carRuntime = null;
                        }
                    } else {
                        carRuntime = null;
                    }
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });

    }
}
