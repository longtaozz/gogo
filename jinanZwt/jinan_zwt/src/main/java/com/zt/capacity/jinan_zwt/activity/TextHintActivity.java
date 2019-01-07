package com.zt.capacity.jinan_zwt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.jinan_zwt.R;


/**
 * 文字提示界面
 */
public class TextHintActivity extends BaseActivity implements View.OnClickListener {
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    private TextView hint_text_str;//提示文字

    private String textStr;//提示文字
    private String textTitle = "提示";//提示标题  默认提示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_hint);


        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题

        hint_text_str = findViewById(R.id.hint_text_str);


        String textTitlex = getIntent().getStringExtra("textTitle");
        textStr = getIntent().getStringExtra("textStr");
        if (textTitlex != null && !TextUtils.isEmpty(textTitlex)) {
            textTitle = textTitlex;
        }


        setData();

    }

    private void setData() {
        title_name.setText(textTitle);
        hint_text_str.setText(textStr == null ? "" : textStr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img:
                finish();
                break;
        }
    }
}
