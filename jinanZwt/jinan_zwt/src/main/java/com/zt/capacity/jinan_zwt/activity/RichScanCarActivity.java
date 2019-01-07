package com.zt.capacity.jinan_zwt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.DateUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.Car;
import com.zt.capacity.jinan_zwt.request.realtimeposition.IRealTimePosition;
import com.zt.capacity.jinan_zwt.request.realtimeposition.RealTimePosition;

/**
 * 车牌扫描结果界面
 */
public class RichScanCarActivity extends BaseActivity implements View.OnClickListener {
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    private String carNumber;
    //车辆信息
    private Car car;

    //城建号,车牌号码,SIM卡号,所属企业,车辆所有人,驾驶员姓名,驾驶员联系电话,驾驶证号,
    // 注册日期,载重量,车辆颜色,密闭类型,从业监管信息,信用评价信息
    private TextView admitNumber, numberPlate,
            simNumber, enterpriseName,
            carOwnersName, drivername,
            drivertel, drivingLicenseNumber,
            register, quality, color,
            airtightype, certificate,
            credit;

    private LinearLayout navicert;//准运证
    private LinearLayout interfacciami;//违规记录
    private LinearLayout car_history;//历史轨迹

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_rich_scan_car);
        carNumber = getIntent().getStringExtra("carNumber");
        if (carNumber != null && !"".equals(carNumber)) {
            carNumberByCarData();
        }
        init();
    }

    private void init() {
        //头布局
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("车辆详情");

        //城建号,车牌号码,SIM卡号,所属企业,车辆所有人,驾驶员姓名,驾驶员联系电话,驾驶证号,
        // 注册日期,载重量,车辆颜色,密闭类型,从业监管信息,信用评价信息
        admitNumber = findViewById(R.id.rich_scan_car_admitNumber);
        numberPlate = findViewById(R.id.rich_scan_car_numberPlate);
        simNumber = findViewById(R.id.rich_scan_car_simNumber);
        enterpriseName = findViewById(R.id.rich_scan_car_enterpriseName);
        carOwnersName = findViewById(R.id.rich_scan_car_carOwnersName);
        drivername = findViewById(R.id.rich_scan_car_drivername);
        drivertel = findViewById(R.id.rich_scan_car_drivertel);
        drivingLicenseNumber = findViewById(R.id.rich_scan_car_drivingLicenseNumber);
        register = findViewById(R.id.rich_scan_car_register);
        quality = findViewById(R.id.rich_scan_car_quality);
        color = findViewById(R.id.rich_scan_car_color);
        airtightype = findViewById(R.id.rich_scan_car_airtightype);
        certificate = findViewById(R.id.rich_scan_car_certificate);
        credit = findViewById(R.id.rich_scan_car_credit);


        navicert = findViewById(R.id.navicert);//准运证
        navicert.setOnClickListener(this);
        interfacciami = findViewById(R.id.interfacciami);//违规记录
        interfacciami.setOnClickListener(this);
        car_history = findViewById(R.id.car_history);//历史轨迹
        car_history.setOnClickListener(this);
    }

    //更新页面数据
    private void updateTextView() {
        if (car == null) {
            return;
        }
        //城建号,车牌号码,SIM卡号,所属企业,车辆所有人,驾驶员姓名,驾驶员联系电话,驾驶证号,
        // 注册日期,载重量,车辆颜色,密闭类型,从业监管信息,信用评价信息
        admitNumber.setText(car.getAdmitNumber() + "");
        numberPlate.setText(car.getNumberPlate() + "");
        simNumber.setText(car.getSimNumber() + "");
        enterpriseName.setText(car.getEnterpriseName() + "");
        carOwnersName.setText(car.getCarOwnersName() + "");
        drivername.setText(car.getDrivername() + "");
        drivertel.setText(car.getDrivertel() + "");
        drivingLicenseNumber.setText(car.getDrivingLicenseNumber() + "");
        register.setText(car.getRegister() != null ? DateUtil.dateFormat(car.getRegister() + "") : "");
        quality.setText(car.getQuality() + "");
        color.setText(car.getColor() + "");
        airtightype.setText(car.getAirtightype() + "");
        certificate.setText("");
        credit.setText("");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //请求成功
                    UIUtils.cancelProgressDialog();
                    updateTextView();
                    break;
                case 1:
                    //请求失败
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(RichScanCarActivity.this, "请求失败");
                    break;
            }
        }
    };

    //车牌查询车辆信息
    private void carNumberByCarData() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(3);
        realTimePosition.carsByCarNumber(carNumber, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        car = (Car) body;
                    }
                    message.what = 0;
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img:
                finish();
                break;
            case R.id.navicert:
                //准运证
                break;
            case R.id.interfacciami:
                //违规记录
                break;
            case R.id.car_history:
                //历史轨迹
                Intent intent = new Intent(RichScanCarActivity.this, CarHistoryActivity.class);
                intent.putExtra("hour", 1);
                intent.putExtra("carNumber", carNumber);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTShaoChePaiXinXi"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTShaoChePaiXinXi"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
