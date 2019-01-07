package com.zt.capacity.baidumap.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjq.umeng.UmengHelper;
import com.zt.capacity.baidumap.R;
import com.zt.capacity.baidumap.activity.CarHistoryActivity;
import com.zt.capacity.baidumap.activity.LocationActivity;
import com.zt.capacity.baidumap.bean.BaseBaiduReturn;
import com.zt.capacity.baidumap.bean.CarRuntime;
import com.zt.capacity.baidumap.bean.GeocoderBean;
import com.zt.capacity.baidumap.request.baidmap.BaiDu;
import com.zt.capacity.baidumap.request.baidmap.IBaiDu;
import com.zt.capacity.baidumap.request.realtimeposition.IRealTimePosition;
import com.zt.capacity.baidumap.request.realtimeposition.RealTimePosition;
import com.zt.capacity.baidumap.utils.TransformationUtil;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;

/**
 * 实时位置车辆信息
 * Created by Administrator on 2018/5/11.
 */

public class LocationFBasicsFragment extends BaseFragment implements View.OnClickListener {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private View view;
    private CarRuntime carRuntime = new CarRuntime();

    private Car car = new Car();

    //xx键
    private ImageView location_fragment_close_img;

    //内容
    private TextView location_fragment_carnumber;//车牌号码
    private TextView location_fragment_carid;//城建号
    private TextView location_fragment_factory_name;//车辆品牌
    private TextView location_fragment_car_owners_contact;//设备厂商
    private TextView location_fragment_company;//所属公司
    private TextView jn_speed;//实时速度
    private TextView location_fragment_box_state;//车辆状态
    private TextView testing_state;//检测状态
    private TextView equipment_time;//设备时间
    private TextView current_address;//位置信息

    private TextView history_go;//历史轨迹按钮
    private TextView instructions_go;//下发指令按钮
    private TextView parameter_go;//下发参数按钮

    private GeocoderBean geocoderBean;//解析地址

    private LinearLayout enforcement_report;//执法上报

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_fragment_jn_basics, null);
        view.setOnTouchListener(this);
        Bundle bundle = this.getArguments();
        init();//控件初始化
        if (bundle != null && bundle.containsKey("carRuntime")) {
            carRuntime = (CarRuntime) bundle.getSerializable("carRuntime");
            Log.e("carRuntime...", gson.toJson(carRuntime));
            initFaction();
        }
        return view;
    }


    private void initFaction() {

        carData();//请求数据


        String carState = carRuntime.getCarState();
        if ((carState.substring(carState.length() - 7, carState.length() - 6).equals("1") && carRuntime.getOnlineState().equals("1"))
                || ((carRuntime.getGpsPosY() < 1 || carRuntime.getGpsPosX() < 1) && carRuntime.getOnlineState().equals("1")))//gps故障
        {
            current_address.setText("车辆未定位!");
        } else {
            //逆地址解析
            geocoderDate(TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX())));
        }
    }

    private void init() {

        //xx键
        location_fragment_close_img = view.findViewById(R.id.location_fragment_close_img);
        location_fragment_close_img.setOnClickListener(this);


        //内容
        location_fragment_carnumber = view.findViewById(R.id.location_fragment_carnumber);//车牌号码
        location_fragment_carid = view.findViewById(R.id.location_fragment_carid);//城建号
        location_fragment_factory_name = view.findViewById(R.id.location_fragment_factory_name);//车辆品牌
        location_fragment_car_owners_contact = view.findViewById(R.id.location_fragment_car_owners_contact);//设备厂商
        location_fragment_company = view.findViewById(R.id.location_fragment_company);//所属公司
        jn_speed = view.findViewById(R.id.jn_speed);//实时速度
        location_fragment_box_state = view.findViewById(R.id.location_fragment_box_state);//车辆状态
        testing_state = view.findViewById(R.id.testing_state);//检测状态
        equipment_time = view.findViewById(R.id.equipment_time);//设备时间
        current_address = view.findViewById(R.id.current_address);//位置信息

        history_go = view.findViewById(R.id.history_go);//历史轨迹按钮
        history_go.setOnClickListener(this);
        instructions_go = view.findViewById(R.id.instructions_go);//下发指令按钮
        instructions_go.setOnClickListener(this);
        parameter_go = view.findViewById(R.id.parameter_go);//下发参数按钮
        parameter_go.setOnClickListener(this);

        enforcement_report = view.findViewById(R.id.enforcement_report);//执法上报
        enforcement_report.setOnClickListener(this);

    }


    Handler carHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e("fragment1", msg.what + "");
            switch (msg.what) {
                case 0:
                    car = (Car) msg.getData().getSerializable("car");

                    Log.e("car...", gson.toJson(car));
                    Log.e("car...carRuntime", gson.toJson(carRuntime));

                    String stateC = "";//车辆状态
                    String stateJ = "";//检测状态
                    boolean car16StateNormal = true;//这个是平台抄下来的状态，我也不知道是什么意思，判断开厢的时候要用
                    String carState = carRuntime.getCarState();//车辆状态码

                    location_fragment_carnumber.setText(car.getNumberPlate());//车牌号码
                    location_fragment_carid.setText(car.getAdmitNumber());//城建号
                    location_fragment_factory_name.setText(car.getCarFactory());//车辆品牌
                    location_fragment_car_owners_contact.setText(car.getEquipmentName());//设备厂商
                    location_fragment_company.setText(car.getEnterpriseAbbreviation());//所属公司
                    if (carRuntime.getOnlineState().equals("1")) {
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
                        location_fragment_box_state.setText(stateC);//车辆状态
                        jn_speed.setText(carRuntime.getGpsSpeed() + "");//实时速度


                        if (carState.substring(carState.length() - 5, carState.length() - 4).equals("1") && carRuntime.getOnlineState() == "1")//无证车辆
                        {
                            stateJ = stateJ + "无证 ";
                            car16StateNormal = false;
                        }
                        if ((carState.substring(carState.length() - 7, carState.length() - 6).equals("1") && carRuntime.getOnlineState().equals("1"))
                                || ((carRuntime.getGpsPosY() < 1 || carRuntime.getGpsPosX() < 1) && carRuntime.getOnlineState().equals("1")))//gps故障
                        {
                            stateJ = stateJ + "定位故障 ";
                            car16StateNormal = false;
                        }
                        if (carState.substring(carState.length() - 11, carState.length() - 10).equals("1") && carRuntime.getOnlineState().equals("1")) {
                            stateJ = stateJ + "举斗失联 ";
                            car16StateNormal = false;
                        }
                        if (carState.substring(carState.length() - 12, carState.length() - 11).equals("1") && carRuntime.getOnlineState().equals("1")) {
                            stateJ = stateJ + "开关箱失联 ";
                            car16StateNormal = false;
                        }
                        if (carState.substring(carState.length() - 14, carState.length() - 13).equals("1") && carRuntime.getOnlineState().equals("1")) {
                            stateJ = stateJ + "ECU失联 ";
                            car16StateNormal = false;
                        }
                        if (carRuntime.getBoxClose() == 2 && car16StateNormal && carRuntime.getGpsSpeed() > 20)//开厢行驶
                            stateJ = stateJ + "开厢行驶 ";
                        if ("".equals(stateJ)) {
                            stateJ = stateJ + "正常";
                        }
                        testing_state.setText(stateJ);//检测状态
                    } else {
                        location_fragment_box_state.setText("离线");//车辆状态
                        jn_speed.setText("0.0");//实时速度
                        testing_state.setText("离线");//检测状态
                    }
                    equipment_time.setText(carRuntime.getSendDatetime());//设备时间


                    break;
                case 1:
//                    IToast.show(LocationCarActivity.this,"此车辆查不到信息");
                    break;
                case 2:
                    //逆地址解析请求成功数据有误
                    Log.e("ddddddd", "数据解析有误");
                    break;

                case 100001:
                    //逆地址解析成功无误
                    BaseBaiduReturn baseBaiduReturn = (BaseBaiduReturn) msg.getData().getSerializable("baseBaiduReturn");
                    geocoderBean = baseBaiduReturn.getResult();
                    current_address.setText(geocoderBean.getFormatted_address());
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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("car", car);
                    message.setData(bundle);
                }
                message.what = state;
                carHandler.sendMessage(message);
            }
        });

    }

    //逆地址解析
    private void geocoderDate(LatLng latLng) {
        IBaiDu baiDu = BaiDu.getInterface(2);
        String location = latLng.latitude + "," + latLng.longitude;
        baiDu.geocoder(location, 0, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.e("fragment1xxxx", state + "");
                Message message = new Message();
                if (state == 0) {
                    Bundle bundle = new Bundle();
                    BaseBaiduReturn baseBaiduReturn = (BaseBaiduReturn) body;
                    if (baseBaiduReturn.getStatus() == 0) {
                        message.what = 100001;
                    } else {
                        message.what = 2;
                    }
                    bundle.putSerializable("baseBaiduReturn", baseBaiduReturn);
                    message.setData(bundle);
                } else {
                    message.what = state;
                }
                carHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i3 = view.getId();
        if (i3 == R.id.location_fragment_close_img) {
            LocationActivity.close();

        } else if (i3 == R.id.history_go) {//历史轨迹
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiLiShiGuiJi");
            Intent i = new Intent(getContext(), CarHistoryActivity.class);
            i.putExtra("carNumber", carRuntime.getCarNumber());
            startActivity(i);

        } else if (i3 == R.id.instructions_go) {//下发指令
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiXaiFaZhiLing");
            ARouter.getInstance().build("/jinan/yunshu/instruction").withString("carNumber",carRuntime.getCarNumber()).navigation();
        } else if (i3 == R.id.parameter_go) {//下发参数
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiXaiFaChanShu");

        } else if (i3 == R.id.enforcement_report) {//执法上报
            UmengHelper.onEvent(BaseApplication.getContext(), "JNZhiFaShangBao");

        }

    }

    // 百度地图通过坐标获取地址，（ 要签名打包才能得到地址）
    private void latlngToAddress(LatLng latlng) {


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
