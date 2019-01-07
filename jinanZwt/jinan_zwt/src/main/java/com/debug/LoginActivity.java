package com.debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.IToast;
import com.zt.capacity.common.util.JurisdictionUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.Main;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainActivity;
import com.zt.capacity.jinan_zwt.request.user.IUserManager;
import com.zt.capacity.jinan_zwt.request.user.UserManager;
import com.zt.capacity.jinan_zwt.util.HWPushUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    //UI控件
    private TextView user_register;//注册
    private EditText user_name;//用户名
    private EditText user_password;//密码
    private Button login_submi;//登录
    private TextView login_forget_password;//忘记密码

    //记住密码
    private LinearLayout remember_password;
    private ImageView remember_password_imgv;//选框


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_login);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sp.edit();

        //获取读写权限
//        JurisdictionUtil.GETWRITEEXTERNAL(this);
        //初始化
        init();

    }

    private void init() {

        //控件
        user_register = findViewById(R.id.user_register);
        user_register.setOnClickListener(this);
        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        login_submi = findViewById(R.id.login_submi);
        login_submi.setOnClickListener(this);
        login_forget_password = findViewById(R.id.login_forget_password);
        login_forget_password.setOnClickListener(this);

        //记住密码
        remember_password = findViewById(R.id.remember_password);
        remember_password.setOnClickListener(this);
        remember_password_imgv = findViewById(R.id.remember_password_imgv);
        remember_password_imgv.setSelected(BaseApplication.rememberState);


        user_name.setText(sp.getString("account", ""));
        user_password.setText(sp.getString("password", ""));

//        if("".equals(sp.getString("password",null))){

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://登录失败
                    UIUtils.cancelProgressDialog();
                    IToast.show(LoginActivity.this, msg.obj.toString());
                    break;
            }
        }
    };

    private void login() {
        String account = user_name.getText().toString();
        String passWord = user_password.getText().toString();
        //储存帐号
        editor.putString("account", user_name.getText().toString());
        if (BaseApplication.rememberState) {
            editor.putString("password", user_password.getText().toString());
        } else {
            editor.putString("password", "");
        }
        editor.commit();
        IUserManager userLogin = UserManager.getInterface(1);
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        userLogin.login(account, passWord, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.d("state", "" + state);
                Log.d("msg", msg);
                Message message = new Message();
                if (state == 0) {

                    //极光注册
                    Main.getInterface(LoginActivity.this);

                    if (TextUtils.isEmpty(Web.getToken())) {
                        login();
                    } else {
                        UIUtils.cancelProgressDialog();
                        ActivityJumpUtil.jumpActivity(LoginActivity.this, MainActivity.class);
                        finish();
                    }
                    return;
                } else {
                    message.what = 1;
                    message.obj = msg;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.user_register) {//注册

        } else if (i == R.id.login_submi) {//登录

            login();
        } else if (i == R.id.login_forget_password) {//忘记密码

        } else if (i == R.id.remember_password) {//记住密码选项
            if (BaseApplication.rememberState) {
                remember_password_imgv.setSelected(false);
                BaseApplication.rememberState = false;
            } else {
                remember_password_imgv.setSelected(true);
                BaseApplication.rememberState = true;
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回事件
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CSDengLu"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CSDengLu"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
