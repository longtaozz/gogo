package com.zt.capacity.common.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zt.capacity.common.R;

/**
 * 查看大图
 *
 * @author lt
 * @time 2018/12/20 9:44
 **/
@Route(path = "common/base/ImageBig")
public class ImageBigActivity extends BaseActivity {
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshower);
        imgView = findViewById(R.id.imgView);
        String imgurl = getIntent().getStringExtra("imgUrl");

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.preloading_images)
                .error(R.drawable.preloading_images)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(imgurl).apply(options).into(imgView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finish();
        return true;
    }

}
