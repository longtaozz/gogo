package com.zt.capacity.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

//竖着放字
@SuppressLint("AppCompatCustomView")
public class RotateTextView extends TextView{

    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(90, getMeasuredHeight(), getMeasuredWidth());
        super.onDraw(canvas);
    }
}
