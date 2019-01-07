package com.zt.capacity.jinan_zwt.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.fragment.CaptureFragment;
import com.zt.capacity.jinan_zwt.fragment.LocationFragment;
import com.zt.capacity.jinan_zwt.fragment.PreviewFragment;


/**
 * 车牌识别
 */
public class PreviewCodeActivity extends BaseFragmentActivity implements View.OnClickListener {


    private Integer select_state = 0;//0为扫二维码,1为扫车牌

    private ImageView preview_out;//退出

    //扫二维码头
    private LinearLayout pseudo_scan_code;
    private ImageView pseudo_scan_code_img;
    private TextView pseudo_scan_code_text;
    //扫车牌头
    private LinearLayout pseudo_scan_carnumber;
    private ImageView pseudo_scan_carnumber_img;
    private TextView pseudo_scan_carnumber_text;


    private FragmentManager mFragmentManager;// fragment管理器
    private FragmentTransaction mFragmentTransaction;// fragment事务管理
    private static int position = 0;// 记录当前底部选择位置
    private PreviewFragment previewFragment;//扫描车牌
    private CaptureFragment captureFragment;//扫二维码
    private Fragment mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_preview);

        init();


        // fragment管理器初始化
        mFragmentManager = getSupportFragmentManager();
        // 开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        // 初始化主页面
        previewFragment = new PreviewFragment();
        mFragmentTransaction.add(R.id.preview_p_fragment, previewFragment);
        mContent = previewFragment;
        mFragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    private void init() {


        //扫二维码头
        pseudo_scan_code = findViewById(R.id.pseudo_scan_code);
        pseudo_scan_code.setOnClickListener(this);
        pseudo_scan_code_img = findViewById(R.id.pseudo_scan_code_img);
        pseudo_scan_code_text = findViewById(R.id.pseudo_scan_code_text);
        //扫车牌头
        pseudo_scan_carnumber = findViewById(R.id.pseudo_scan_carnumber);
        pseudo_scan_carnumber.setOnClickListener(this);
        pseudo_scan_carnumber_img = findViewById(R.id.pseudo_scan_carnumber_img);
        pseudo_scan_carnumber_text = findViewById(R.id.pseudo_scan_carnumber_text);

        preview_out = findViewById(R.id.preview_out);
        preview_out.setOnClickListener(this);

        switchover();

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.preview_out:
                finish();
                break;
            case R.id.pseudo_scan_code:
                //扫二维码
                select_state = 1;
                switchover();
                if (captureFragment == null) {
                    captureFragment = new CaptureFragment();
                }
                mFragmentTransaction.replace(R.id.preview_p_fragment, captureFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.pseudo_scan_carnumber:
                //扫车牌
                select_state = 0;
                switchover();
                if (previewFragment == null) {
                    previewFragment = new PreviewFragment();
                }
                mFragmentTransaction.replace(R.id.preview_p_fragment, previewFragment);
                mFragmentTransaction.commit();
                break;
        }
    }

    //切换不同状态界面
    private void switchover() {
        if (select_state == 1) {
            //扫二维码
            pseudo_scan_code_img.setSelected(true);
            pseudo_scan_code_text.setSelected(true);
            //扫车牌
            pseudo_scan_carnumber_img.setSelected(false);
            pseudo_scan_carnumber_text.setSelected(false);


        } else {
            //扫车牌
            pseudo_scan_code_img.setSelected(false);
            pseudo_scan_code_text.setSelected(false);
            //扫车牌
            pseudo_scan_carnumber_img.setSelected(true);
            pseudo_scan_carnumber_text.setSelected(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTSaoYiSao"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTSaoYiSao"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
