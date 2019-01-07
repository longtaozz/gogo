package com.zt.capacity.common.base;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.hjq.umeng.UmengHelper;

/**
 * 通用的Fragment设置
 * @author lt
 *
 **/
public class BaseFragment extends Fragment implements View.OnTouchListener {
    public Context context;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public void setContext(Context context){
        this.context=context;
    }


    @Override
    public void onResume() {
        super.onResume();
        UmengHelper.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengHelper.onPause(this);
    }
}
