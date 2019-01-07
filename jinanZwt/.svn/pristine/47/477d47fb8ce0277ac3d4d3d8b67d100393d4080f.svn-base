package com.zt.capacity.jinan_zwt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.jinan_zwt.R;


/**
 * 语音
 * Created by Administrator on 2018/4/20.
 */

public class VoiceMaskActivity extends BaseActivity implements View.OnClickListener{

    public static Activity instance = null;
    private LinearLayout voice_backimg;

    protected ImageView voicePopImageview;//语音遮罩层按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_voice_popup);

        voice_backimg=findViewById(R.id.voice_backimg);
        Bitmap b= BitmapFactory.decodeResource(this.getResources(),R.drawable.jn_zwt_pop_mask);
        Bitmap bt=getTransparentBitmap(b,20);
        Drawable drawable=new BitmapDrawable(bt);
        voice_backimg.setBackground(drawable);


        voicePopImageview = findViewById(R.id.voice_pop_imageview);
        voicePopImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("myreceiver");
                sendBroadcast(intent);
                finish();
            }
        });
        instance=this;
    }

    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

    @Override
    public void onClick(View view) {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNYuYinShiBie"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNYuYinShiBie"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
