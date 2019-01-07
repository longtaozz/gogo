package com.zt.capacity.changsha_yunshu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.zt.capacity.baidumap.request.history.HistoryManager;
import com.zt.capacity.baidumap.request.history.IHistoryManager;
import com.zt.capacity.changsha_yunshu.R;
import com.zt.capacity.changsha_yunshu.data.ChangSha_YunShu_Url;
import com.zt.capacity.changsha_yunshu.fragment.MainHomeFragment;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;

import java.util.List;

/**
 * 济南运输公司首页
 *
 * @author lt
 * @time 2018/12/17 16:36
 **/

@Route(path = "/changsha/transit/main")
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    // Used to load the 'native-lib' library on application startup.
    public static MainActivity mainActivity = null;

    //济南主页链接
    String bHomeUrl = Web.htmlIP + Web.getHtmlProject() + ChangSha_YunShu_Url.JINANZHENGFU + "?token=" + Web.getToken();

    protected static ImageView[] mBottom_img;// 底部菜单图标
    protected static TextView[] mBottom_text;// 底部菜单文字
    protected static int position = 0;// 记录当前底部选择位置
    protected FragmentManager mFragmentManager;// fragment管理器
    protected FragmentTransaction mFragmentTransaction;// fragment事务管理
    protected MainHomeFragment mHomeFragment;
    protected WebViewFragment webViewFragment1;
    protected WebViewFragment webViewFragment2;
    protected WebViewFragment webViewFragment3;
    protected WebViewFragment webViewFragment4;
    protected Fragment mContent;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changsha_yunshu_activity_main);

        mainActivity = this;

        //控件初始化
        init();

        selectAllMyCar();

        //底部菜单初始化
        bottomMenu();


    }


    private void selectAllMyCar() {
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                if (state == 0) {
                    if (body != null) {
                        Web.cars = (List<Car>) body;
                    }
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
            ToastUtils.show("再按一次退出程序");
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
            Log.e("lianjie..", bHomeUrl);
            if (mHomeFragment == null) {
                mHomeFragment = MainHomeFragment.newInstance(bHomeUrl);
            }
            mFragmentTransaction.replace(R.id.main_fragment, mHomeFragment);
            mFragmentTransaction.commit();
            changeBottom(0);

        } else if (i == R.id.bottom_2) {
            //车辆

            if (position == 1) {
                return;
            }
            if (webViewFragment1 == null) {
                webViewFragment1 = WebViewFragment.newInstance(Web.htmlIP + Web.getHtmlProject() + ChangSha_YunShu_Url.CAR + "?token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment1);
            mFragmentTransaction.commit();
            changeBottom(1);

//            ActivityJumpUtil.jumpActivity(this, LocationFActivity.class);
        } else if (i == R.id.bottom_3) {

            //运输公司
            if (position == 2) {
                return;
            }
            if (webViewFragment2 == null) {
                webViewFragment2 = WebViewFragment.newInstance(Web.htmlIP + Web.getHtmlProject() + ChangSha_YunShu_Url.COMPANY + "?token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment2);
            mFragmentTransaction.commit();
            changeBottom(2);
        } else if (i == R.id.bottom_4) {
            UmengHelper.onEvent(BaseApplication.getContext(), "JNZWTZhuYeZhiFa");
            //证件

            if (position == 3) {
                return;
            }
            if (webViewFragment3 == null) {
                webViewFragment3 = WebViewFragment.newInstance(Web.htmlIP + Web.getHtmlProject() + ChangSha_YunShu_Url.CARD + "?token=" + Web.getToken() + "&sh=1");
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment3);
            mFragmentTransaction.commit();
            changeBottom(3);
        } else if (i == R.id.bottom_5) {
            UmengHelper.onEvent(BaseApplication.getContext(), "JNZWTZhuYeShuJu");
            //数据

            if (position == 4) {
                return;
            }
            if (webViewFragment4 == null) {
                webViewFragment4 = WebViewFragment.newInstance(Web.htmlIP + Web.getHtmlProject() + ChangSha_YunShu_Url.DATACENTER + "?token=" + Web.getToken());
            }
            mFragmentTransaction.replace(R.id.main_fragment, webViewFragment4);
            mFragmentTransaction.commit();
            changeBottom(4);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivity = null;
    }
}
