package com.zt.capacity.common.base;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/5/11.
 */

public class BaseFragment extends Fragment implements View.OnTouchListener {
    public Context context;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public void setContext(Context context){
        this.context=context;
    }

}
