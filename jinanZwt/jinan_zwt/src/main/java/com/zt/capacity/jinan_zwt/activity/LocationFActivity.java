package com.zt.capacity.jinan_zwt.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.fragment.LocationFragment;
import com.zt.capacity.jinan_zwt.fragment.PeripheryViolationFragment;


/**
 * 监控
 * Created by Administrator on 2018-04-12.
 */
public class LocationFActivity extends BaseFragmentActivity implements View.OnClickListener {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题


    private FragmentManager mFragmentManager;// fragment管理器
    private FragmentTransaction mFragmentTransaction;// fragment事务管理
    private static int position = 0;// 记录当前底部选择位置
    private LocationFragment locationFragment;
    private PeripheryViolationFragment peripheryViolationFragment;
    private Fragment mContent;

    private TextView location_text;//实时位置文字
    private View location_fragment_button_lin;//实时位置横杠
    private TextView periphery_text;//周边违规文字
    private View periphery_fragment_button_lin;//周边违规横杠


    private String carNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jn_zwt_location_f_activity);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        carNumber=getIntent().getStringExtra("carNumber");
        //初始化控件
        init();

        position=0;

    }


    private void init() {
        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("监控中心");


        // fragment管理器初始化
        mFragmentManager = getSupportFragmentManager();
        // 开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        // 初始化主页面
        locationFragment = LocationFragment.newInstance(carNumber);
        mFragmentTransaction.add(R.id.location_f_fragment, locationFragment);
        mContent = locationFragment;
        mFragmentTransaction.commit();


        findViewById(R.id.location_fragment_button).setOnClickListener(this);
        findViewById(R.id.periphery_fragment_button).setOnClickListener(this);
        location_fragment_button_lin=findViewById(R.id.location_fragment_button_lin);
        periphery_fragment_button_lin=findViewById(R.id.periphery_fragment_button_lin);
        location_fragment_button_lin.setVisibility(View.VISIBLE);
        periphery_fragment_button_lin.setVisibility(View.INVISIBLE);


        location_text=findViewById(R.id.location_text);//实时位置文字
        location_text.setSelected(true);
        periphery_text=findViewById(R.id.periphery_text);//周边违规文字

    }


    @Override
    public void onClick(View view) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        int i = view.getId();
        if (i == R.id.title_img) {//返回按钮
            finish();

        } else if (i == R.id.location_fragment_button) {
            if (position == 0) {
                return;
            }
            if (locationFragment == null) {
                locationFragment = LocationFragment.newInstance(carNumber);
            }
            mFragmentTransaction.replace(R.id.location_f_fragment, locationFragment);
            mFragmentTransaction.commit();
            position = 0;
            location_fragment_button_lin.setVisibility(View.VISIBLE);
            location_text.setSelected(true);
            periphery_text.setSelected(false);
            periphery_fragment_button_lin.setVisibility(View.INVISIBLE);
        } else if (i == R.id.periphery_fragment_button) {
            if (position == 1) {
                return;
            }
            if (peripheryViolationFragment == null) {
                peripheryViolationFragment = new PeripheryViolationFragment();
            }
            mFragmentTransaction.replace(R.id.location_f_fragment, peripheryViolationFragment);
            mFragmentTransaction.commit();
            position = 1;
            location_fragment_button_lin.setVisibility(View.INVISIBLE);
            periphery_fragment_button_lin.setVisibility(View.VISIBLE);
            location_text.setSelected(false);
            periphery_text.setSelected(true);
        }
    }




//    private void hideKeyboard() {
//        // 让软键盘消失
//        InputMethodManager im = (InputMethodManager) this.
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(this.getCurrentFocus()
//                .getWindowToken(), 0);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        MobclickAgent.onPageStart("JNJianKong"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        MobclickAgent.onPageEnd("JNJianKong"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
