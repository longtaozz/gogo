package com.debug;


import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.lbsapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.hjq.toast.ToastUtils;
import com.zt.capacity.baidumap.listener.MyLocationListener;
import com.zt.capacity.baidumap.service.LocationService;
import com.zt.capacity.common.data.MyRoute;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.requests.user.IUserManager;
import com.zt.capacity.common.requests.user.UserManager;
import com.zt.capacity.common.util.MyToastBlackStyle;


/**
 * 单独测试开发用application
 * @author lt
 * @time 2018/12/14 11:34
 *
 **/
public class MyApplication extends Application {

    //全景
    public BMapManager mBMapManager = null;

    //定位
    public static LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化吐司工具类
        ToastUtils.init(this,new MyToastBlackStyle());
        //初始化baiduMap
        SDKInitializer.initialize(getApplicationContext());
        //全景地图初始化
        initEngineManager(this);

        //定位使用案例

        MyApplication.locationService = new LocationService(getApplicationContext(),0);
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        //定位回调监听
        MyApplication.locationService.registerListener(MyApplication.myLocationListener);
        //注册监听
//        MyApplication.locationService.setLocationOption(MyApplication.locationService.getDefaultLocationClientOption());
        MyApplication.locationService.start();

        //不需要请求无需模拟登陆
        login();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        Log.d("ljx", "initEngineManager");
    }


    //进行一次定位并设为公共变量
    public static MyLocationListener myLocationListener = new MyLocationListener();//定位监听


    /**
     * 在这里模拟登陆，然后拿到sessionId或者Token
     * 这样就能够在组件请求接口了
     */
    private void login() {
        String account = "恒久";
        String passWord = "123456";
        //储存帐号
        IUserManager userLogin = UserManager.getInterface(1);
        userLogin.login(account, passWord, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.d("state", "" + state);
                Log.d("msg", msg);
                Message message = new Message();
                if (state == 0) {
                    if (TextUtils.isEmpty(Web.getToken())) {
                        login();
                    } else {
                        //模拟登陆成功
                        MyRoute.getInterface(getApplicationContext());
                    }
                }
            }
        });
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        locationService.unregisterListener(myLocationListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onTerminate();
    }
}
