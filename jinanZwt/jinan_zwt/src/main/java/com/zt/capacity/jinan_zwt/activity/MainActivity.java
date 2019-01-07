package com.zt.capacity.jinan_zwt.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.service.LocationService;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.IToast;
import com.zt.capacity.common.util.JurisdictionUtil;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.Car;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;
import com.zt.capacity.jinan_zwt.fragment.MainHomeFragment;
import com.zt.capacity.jinan_zwt.request.history.HistoryManager;
import com.zt.capacity.jinan_zwt.request.history.IHistoryManager;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

import pr.platerecognization.PlateRecognition;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    // Used to load the 'native-lib' library on application startup.
    static {

        if (OpenCVLoader.initDebug()) {
            Log.d("Opencv", "opencv load_success");

        } else {
            Log.d("Opencv", "opencv can't load opencv .");

        }
    }

    //济南主页链接
    String bHomeUrl = JN_ZWT_Url.JINANZHENGFU + "?token=" + Web.getToken();

    protected static ImageView[] mBottom_img;// 底部菜单图标
    protected static TextView[] mBottom_text;// 底部菜单文字
    protected static int position = 0;// 记录当前底部选择位置
    protected FragmentManager mFragmentManager;// fragment管理器
    protected FragmentTransaction mFragmentTransaction;// fragment事务管理
    protected MainHomeFragment mHomeFragment;
    protected WebViewFragment webViewFragment1;
    protected WebViewFragment webViewFragment2;
    protected WebViewFragment webViewFragment3;
    protected Fragment mContent;


    private PlateRecognition plateRecognition;

    public static MainActivity mainActivity;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    //所有车辆
    public static List<Car> cars = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_main);
        mainActivity = this;

        requestCameraPermission();


        //定位

        MyApplication.locationService = new LocationService(getApplicationContext());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        MyApplication.locationService.registerListener(MyApplication.myLocationListener);
        //注册监听
//        int type = getIntent().getIntExtra("from", 0);
        MyApplication.locationService.setLocationOption(MyApplication.locationService.getDefaultLocationClientOption());
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
        MyApplication.locationService.start();

        //init plate recognizer
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                plateRecognition.initRecognizer("pr");
//            }
//        }).start();
        //控件初始化
        init();

        //请求所有车辆数据
        selectAllMyCar();

        //底部菜单初始化
        bottomMenu();



//        Bundle extra =new Bundle();
//        extra.putString("package", "com.zt.capacity.jinan_zwt");
//        extra.putString("class", "com.debug.WelcomeActivity");
//        extra.putInt("badgenumber", 5);
//        getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);




    }


    //申请相机权限
    @TargetApi(Build.VERSION_CODES.M)
    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 1);
        requestPermissions(new String[]{"android.permission.CAMERA"}, 100);
    }





    private void selectAllMyCar() {
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                if (state == 0) {
                    if (body != null) {
                        cars = (List<Car>) body;
                    }
                } else {
                    Log.e("操作失败", body + "");
                }
            }
        });


    }

    private void bottomMenu() {


        // 底部菜单数组初始化3
        mBottom_img = new ImageView[5];
        mBottom_img[0] = findViewById(R.id.img_letter);
        mBottom_img[1] = findViewById(R.id.img_monitoring);
        mBottom_img[2] = findViewById(R.id.img_approval);
        mBottom_img[3] = findViewById(R.id.img_enforcement);
        mBottom_img[4] = findViewById(R.id.img_information);
        mBottom_text = new TextView[5];
        mBottom_text[0] = findViewById(R.id.text_letter);
        mBottom_text[1] = findViewById(R.id.text_monitoring);
        mBottom_text[2] = findViewById(R.id.text_approval);
        mBottom_text[3] = findViewById(R.id.text_enforcement);
        mBottom_text[4] = findViewById(R.id.text_information);


        mBottom_img[position].setSelected(true);
        mBottom_text[position].setSelected(true);
        // 设置底部导航栏点击事件
        findViewById(R.id.bottom_1).setOnClickListener(this);
        findViewById(R.id.bottom_2).setOnClickListener(this);
        findViewById(R.id.bottom_3).setOnClickListener(this);
        findViewById(R.id.bottom_4).setOnClickListener(this);
        findViewById(R.id.bottom_5).setOnClickListener(this);
        // fragment管理器初始化
        mFragmentManager = getSupportFragmentManager();
        // 开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        // 初始化主页面
        mHomeFragment = MainHomeFragment.newInstance(bHomeUrl);
        mFragmentTransaction.add(R.id.main_fragment, mHomeFragment);
        mContent = mHomeFragment;
        mFragmentTransaction.commit();
        changeBottom(0);
    }

    private void init() {
    }


    // 底部导航栏选择切换
    public static void changeBottom(int i) {
        for (int j = 0; j < mBottom_img.length; j++) {
            if (j == i) {
                mBottom_img[j].setSelected(true);
                mBottom_text[j].setSelected(true);
                position = i;
            } else {
                mBottom_img[j].setSelected(false);
                mBottom_text[j].setSelected(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            IToast.show(getApplicationContext(), "再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onClick(View view) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        int i = view.getId();
        if (i == R.id.bottom_1) {

            if (position == 0) {
                return;
            }
            if (mHomeFragment == null) {
                mHomeFragment = MainHomeFragment.newInstance(bHomeUrl);
            }
            mFragmentTransaction.replace(R.id.main_fragment, mHomeFragment);
            mFragmentTransaction.commit();
            changeBottom(0);

        } else if (i == R.id.bottom_2) {
            //监控

            //申请定位权限
//            JurisdictionUtil.ACCESS_FINE_LOCATION(this);
            //定位

            MyApplication.locationService = new LocationService(getApplicationContext());
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            MyApplication.locationService.registerListener(MyApplication.myLocationListener);
            //注册监听
//        int type = getIntent().getIntExtra("from", 0);
            MyApplication.locationService.setLocationOption(MyApplication.locationService.getDefaultLocationClientOption());
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
            MyApplication.locationService.start();

            ActivityJumpUtil.jumpActivity(this, LocationFActivity.class);
        } else if (i == R.id.bottom_3) {

            MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhuYeShengPi");
            //审批
            if (position == 2) {
                return;
            }
            if (webViewFragment1 == null) {
                webViewFragment1 = WebViewFragment.newInstance(Web.htmlIP + "html/html/verification.html" + "?sh=1&token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment1);
            mFragmentTransaction.commit();
            changeBottom(2);
        } else if (i == R.id.bottom_4) {
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhuYeZhiFa");
            //执法

            if (position == 3) {
                return;
            }
            if (webViewFragment2 == null) {
                webViewFragment2 = WebViewFragment.newInstance(Web.htmlIP + "html/html/wisdomlaw.html" + "?sh=1&token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment2);
            mFragmentTransaction.commit();
            changeBottom(3);
        } else if (i == R.id.bottom_5) {
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhuYeShuJu");
            //数据

            //申请定位权限
            JurisdictionUtil.ACCESS_FINE_LOCATION(this);
            //定位

            MyApplication.locationService = new LocationService(getApplicationContext());
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            MyApplication.locationService.registerListener(MyApplication.myLocationListener);
            //注册监听
//        int type = getIntent().getIntExtra("from", 0);
            MyApplication.locationService.setLocationOption(MyApplication.locationService.getDefaultLocationClientOption());
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
            MyApplication.locationService.start();
            if (position == 4) {
                return;
            }
            if (webViewFragment3 == null) {
                webViewFragment3 = WebViewFragment.newInstance(Web.htmlIP + "html/html/datacenter.html" + "?token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment3);
            mFragmentTransaction.commit();
            changeBottom(4);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZhuYe"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZhuYe"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
