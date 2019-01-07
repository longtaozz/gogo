package com.zt.capacity.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.R;

/**
 * 3D全景地图
 * Created by Administrator on 2018/5/13.
 */

public class PanoramaActivity extends BaseActivity implements View.OnClickListener {
    private PanoramaView mPanoView;
    private Double longitude;
    private Double latitude;

    //头
    private ImageView title_img;
    private TextView title_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        longitude = getIntent().getDoubleExtra("longitude", 0.00);
        latitude = getIntent().getDoubleExtra("latitude", 0.00);


        init();
        //全景
        quanjing();
    }

    private void init() {
        mPanoView = findViewById(R.id.panorama);

        title_img = findViewById(R.id.title_img);
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);
        title_name.setText("3D实景");

    }

    private void quanjing() {

//        double lat = 40.029233;
//        double lon = 116.32085;
//        BaiduPanoData mPanoDataWithLatLon = panoramaRequest.getPanoramaInfoByLatLon(lon, lat);
//        Log.e(LTAG, "PanoDataWithLatLon");


        // 全景图清晰度  1-5  5最清晰
        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionLow);
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {
            @Override
            public void onDescriptionLoadEnd(String s) {

            }

            @Override
            public void onLoadPanoramaBegin() {

            }

            @Override
            public void onLoadPanoramaEnd(String s) {

            }

            @Override
            public void onLoadPanoramaError(String s) {

            }

            @Override
            public void onMessage(String s, int i) {

            }

            @Override
            public void onCustomMarkerClick(String s) {

            }

            @Override
            public void onMoveStart() {

            }

            @Override
            public void onMoveEnd() {

            }
        });
        //是否显示指示邻接街景的箭头
        mPanoView.setShowTopoLink(true);
        //根据经纬度设置街景点
        //mPanoView.setPanorama(lon, lat);
        mPanoView.setPanorama(longitude, latitude);
    }




    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_img) {
            finish();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
        MobclickAgent.onPageStart("quanJinDiTu"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
        MobclickAgent.onPageEnd("quanJinDiTu"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
