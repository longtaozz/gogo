package com.zt.capacity.jinan_zwt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.jinan_zwt.R;

import pr.platerecognization.PlateRecognition;

/**
 * 动态
 */
public class MainDynamicFragment extends BaseFragment implements View.OnClickListener {
    private View view;

    private Button saomiao;//车牌扫描


    private PlateRecognition plateRecognition;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_fragment_dynamic, null);



        init();

        return view;
    }

    private void init() {
        saomiao=view.findViewById(R.id.saomiao);
        saomiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.saomiao) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ShouYeDongTai"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ShouYeDongTai");
    }

}
