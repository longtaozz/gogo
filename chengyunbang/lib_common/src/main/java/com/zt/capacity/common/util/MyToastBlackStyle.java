package com.zt.capacity.common.util;

import android.view.Gravity;

import com.hjq.toast.style.ToastBlackStyle;

public class MyToastBlackStyle extends ToastBlackStyle {
    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getYOffset() {
        return 100;
    }

}
