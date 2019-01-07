package com.zt.capacity.jinan_yunshu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.util.CustomDialog;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.data.JN_YunShu_Url;
import com.zt.capacity.lib.jpush.push.JpushHelper;


/**
 * 我的
 *
 * @author lt
 * @time 2018/12/26 15:56
 **/
@Route(path = "/jinan/yunshu/mainmy")
public class MainMyActivity extends BaseActivity implements View.OnClickListener {

    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题


    private LinearLayout out_login;

    private LinearLayout problem_feedback;//问题反馈

    private LinearLayout connection_me;//联系我们

    private TextView my_version_code;//版本号


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_yunshu_my);
        init();
    }


    private void init() {
        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("我");

        problem_feedback = findViewById(R.id.problem_feedback);
        problem_feedback.setOnClickListener(this);
        connection_me = findViewById(R.id.connection_me);//联系我们
        connection_me.setOnClickListener(this);
        out_login = findViewById(R.id.out_login);
        out_login.setOnClickListener(this);

        my_version_code = findViewById(R.id.my_version_code);//版本号
        my_version_code.setText("版本" + BaseApplication.versionName);
    }

    @Override
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.title_img) {
            finish();
        } else if (i1 == R.id.out_login) {
            CustomDialog.Builder builder2 = new CustomDialog.Builder(this);
            builder2.setMessage("是否要退出");
            builder2.setTitle("退出登录");
            builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //设置你的操作事项
                }
            });

            builder2.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (MainActivity.mainActivity != null) {
                                MainActivity.mainActivity.finish();
                            }
                            //删除别名
                            JpushHelper.init(getApplication()).setAliasAndTag(null, "");
                            //清空通知
                            JpushHelper.init(getApplication()).clearAllNotifications();
                            finish();
                            SharedPreferences sp = getApplicationContext()
                                    .getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("password", "");
                            editor.commit();// 提交修改
                            Web.setToken("");
                            ARouter.getInstance().build("/app/main/login").navigation();
                        }
                    });

            builder2.create().show();


        } else if (i1 == R.id.problem_feedback) {
            Intent i = new Intent(this, WebViewActivity.class);
            i.putExtra("url", Web.htmlIP + Web.getHtmlProject() + JN_YunShu_Url.ABOUT + "?token=" + Web.getToken());
            startActivity(i);
        } else if (i1 == R.id.connection_me) {
            Intent i = new Intent(this, WebViewActivity.class);
            i.putExtra("url", Web.htmlIP + Web.getHtmlProject() + JN_YunShu_Url.ABOUT + "?token=" + Web.getToken());
            startActivity(i);
        }
    }

}
