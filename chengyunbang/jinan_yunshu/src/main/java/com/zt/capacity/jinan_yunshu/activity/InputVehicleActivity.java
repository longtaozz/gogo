package com.zt.capacity.jinan_yunshu.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.hjq.toast.ToastUtils;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.bean.Pickers;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.CarUtil;
import com.zt.capacity.common.view.PickerScrollView;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.bean.CarBean;
import com.zt.capacity.jinan_yunshu.bean.CityBean;
import com.zt.capacity.jinan_yunshu.request.ViolatedApi.IViolatedManager;
import com.zt.capacity.jinan_yunshu.request.ViolatedApi.ViolatedManager;

import java.util.ArrayList;
import java.util.List;
/**
 * 输入查询违章车辆信息
 * @author lt
 * @time 2018/12/24 14:18
 *
 **/
@Route(path = "/jinan/yunshu/InputVehicle")
public class InputVehicleActivity extends BaseActivity implements View.OnClickListener {
    private ImageView title_img;
    private TextView title_name;

    //行驶证
    TextView driving_license;

    private LinearLayout mModels;

    private PickerScrollView pickerscrlllview; // 滚动选择器
    private List<Pickers> list; // 滚动选择器数据
    private String[] id;
    private String[] name;
    private RelativeLayout picker_rel; // 选择器布局
    private TextView quxiao;
    private TextView qued;
    private TextView chexing;

    //控件
    private Button quer_button;//确定
    private EditText vehicle_carNumber, vehicle_chassisNumber, vehicle_engineNumber;//车牌号,车架号，发动机号


    //数据
    private String carNumber;
    private String chassisNumber;//c车架号
    private String engineNumber;//发动机号
    private String carType = "";//车辆类型
    private String cityId;//城市编号


    protected PopupWindow popupWindow;//图例弹窗


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_yunshu_activity_add_vehicle);
        mModels = findViewById(R.id.models);
        initView();
        initLinstener();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list = new ArrayList<Pickers>();
        id = new String[]{"02", "01", "52", "51"};//号牌类型,默认02:小型车,01:大型车,52:新能源小型车,51:新能源大型车
        name = new String[]{"小型车", "大型车", "新能源小型车", "新能源大型车"};
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], id[i]));
        }
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);
    }

    /**
     * 初始化
     */
    private void initView() {
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        quxiao = findViewById(R.id.quxiao);
        chexing = findViewById(R.id.chexing);
        qued = findViewById(R.id.qued);
        quxiao.setOnClickListener(this);
        qued.setOnClickListener(this);
        picker_rel.setOnClickListener(this);

        title_img = findViewById(R.id.title_img);
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);
        title_name.setText("查询车辆信息");

        quer_button = findViewById(R.id.quer_button);//确定
        quer_button.setOnClickListener(this);
        vehicle_carNumber = findViewById(R.id.vehicle_carNumber);//车牌号
        vehicle_chassisNumber = findViewById(R.id.vehicle_chassisNumber);//车架号
        vehicle_engineNumber = findViewById(R.id.vehicle_engineNumber);//发动机号

        driving_license = findViewById(R.id.driving_license);//行驶证
        driving_license.setOnClickListener(this);
    }

    /**
     * 设置监听事件
     */
    private void initLinstener() {
        mModels.setOnClickListener(onClickListener);
        pickerscrlllview.setOnSelectListener(pickerListener);
    }

    // 滚动选择器选中事件
    PickerScrollView.onSelectListener pickerListener = new PickerScrollView.onSelectListener() {

        @Override
        public void onSelect(Pickers pickers) {
            carType = pickers.getShowId();
            System.out.println("选择：" + pickers.getShowId() + "--银行："
                    + pickers.getShowConetnt());
            chexing.setText("" + pickers.getShowConetnt());
        }
    };

    // 点击监听事件
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mModels) {
                hideKeyboard();
                if (carType.equals("")) {
                    carType = "02";
                    chexing.setText("小型车");
                }
                picker_rel.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_img) {
            finish();

        } else if (i == R.id.models) {
        } else if (i == R.id.picker_rel) {
            picker_rel.setVisibility(View.GONE);

        } else if (i == R.id.quxiao) {
            picker_rel.setVisibility(View.GONE);

        } else if (i == R.id.qued) {
            picker_rel.setVisibility(View.GONE);

        } else if (i == R.id.quer_button) {//确定
            Submission();

        } else if (i == R.id.driving_license) {//点击行驶证
            openPop();

        }
    }

    private void Submission() {
        carNumber = vehicle_carNumber.getText().toString().trim().toUpperCase();
        chassisNumber = vehicle_chassisNumber.getText().toString();
        engineNumber = vehicle_engineNumber.getText().toString();
        if ("".equals(carNumber) || CarUtil.isCarNo(carNumber))
            ToastUtils.show("请输入正确的车牌号");
        else if ("".equals(chassisNumber) && chassisNumber.length() != 6)
            ToastUtils.show("请输入正确车架号");
        else if ("".equals(engineNumber) && engineNumber.length() != 6)
            ToastUtils.show("请输入正确发动机号");
        else if (carType.equals("")) {
            ToastUtils.show("请选择车辆类型");
        } else {
            getCityId(carNumber);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //cityid查询成功
                    cityId = (String) msg.getData().get("city_code");
                    insertPrivateCar();
                case 1:
                    //失败
                    ToastUtils.show((String) msg.obj);
                case 2:
                    //cityid查询错误
                    String msgs = (String) msg.getData().get("msg");
                    ToastUtils.show(msgs);
                    break;
            }
        }
    };

    //获取城市编号
    private void getCityId(String carNumber) {
        IViolatedManager apiq = ViolatedManager.getInterface(2);
        StringBuffer strb = new StringBuffer(carNumber);
        String str = strb.substring(0, 2);
        Log.e("str", str);
        apiq.peccancyNuber(str, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                if (state == 0) {
                    CityBean cityBean = (CityBean) body;
                    Message m = new Message();
                    Bundle bundle = new Bundle();
                    if (cityBean == null) {
                        bundle.putString("msg", msg);
                        m.what = 2;
                    } else {
                        Log.e("getCity_code()", cityBean.getCity_code());
                        bundle.putString("city_code", cityBean.getCity_code());
                        m.what = 0;
                    }
                    m.setData(bundle);
                    handler.sendMessage(m);

                }
            }
        });
    }

    private void hideKeyboard() {
        // 让软键盘消失
        InputMethodManager im = (InputMethodManager) this.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    //查询
    private void insertPrivateCar() {
        CarBean car=new CarBean();
        car.setNumberPlate(carNumber);//车牌
        car.setChassisNumber(chassisNumber);//车架号
        car.setEngineNumber(engineNumber);//发动机号
        car.setCarType(carType);//车辆类型
        Bundle bundle=new Bundle();
        bundle.putSerializable("car",car);
        bundle.putString("cityId",cityId);
        ActivityJumpUtil.jumpActivity(InputVehicleActivity.this,ViolationQueryActivity.class,bundle);



    }


    //弹出遮罩层
    public void openPop() {

        //图例按钮
        View contentView = LayoutInflater.from(InputVehicleActivity.this).inflate(R.layout.jn_yunshu_driving_license_pop, null);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


        View popView = LayoutInflater.from(InputVehicleActivity.this).inflate(
                R.layout.jn_yunshu_driving_license_pop, null);
        View rootView = findViewById(R.id.add_vehicle_id); // 當前頁面的根佈局
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundAlpha(0.2f);//设置屏幕透明度

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口

        //隐藏时关掉透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        // 顯示在根佈局的右边
        popupWindow.showAtLocation(rootView, Gravity.CENTER | Gravity.CENTER, 0,
                0);
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
