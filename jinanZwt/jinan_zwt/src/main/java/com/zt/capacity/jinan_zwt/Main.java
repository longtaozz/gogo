package com.zt.capacity.jinan_zwt;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.RouteUtils;
import com.zt.capacity.jinan_zwt.activity.MainActivity;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * 路由
 */
public class Main {
    private static Integer cityId;//城市id
    private static Integer role;//权限
    private static Context context;
    private static boolean isAlias = false;//是否设置成功别名

    private static Set<String> tags = new HashSet<>();

    private static Main main;

    public static Main getInterface(Context mContext) {
        if (main == null)
            main = new Main();
//        if (null == Web.getUser() || mContext == null || Web.getUser().getCityId() == null || Web.getUser().getRole() == null) {
//            //检查用户信息存不存在
//            return null;
//        }
        context = mContext;
        cityId = Web.getUser().getCityId();
        role = Web.getUser().getRole();


        //设置别名
        tags = new HashSet<>();
        tags.add(Web.getUser().getUsername());
        if (!isAlias) {
            mHandler.sendMessage(new Message());
        }

        return main;
    }

    //    主页面
    public void goMainActivity() {
        ActivityJumpUtil.jumpActivity((Activity) context, MainActivity.class);

    }



    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
            switch (i) {
                case 0:
                    //设置别名成功
                    isAlias = true;
                    Log.e("setAliasAndTags...", "设置成功");
                    break;
                case 6001:
                    //无效的设置，tag/alias 不应参数都为 null
                    Log.e("setAliasAndTags...", "无效的设置");
                    break;
                case 6002:
                    //设置超时
                    mHandler.sendMessageDelayed(new Message(), 1000 * 5);
                    Log.e("setAliasAndTags...", "设置超时");
                    break;
                case 6003:
                    //alias 字符串不合法    有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
                    Log.e("setAliasAndTags...", "alias 字符串不合法");
                    break;
                case 6004:
                    //alias超长。最多 40个字节    中文 UTF-8 是 3 个字节
                    Log.e("setAliasAndTags...", "alias超长");
                    break;
                case 6005:
                    //某一个 tag 字符串不合法
                    Log.e("setAliasAndTags...", "某一个 tag 字符串不合法");
                    break;
                default:
                    Log.e("setAliasAndTags...", "设置出错:" + i);
                    break;
            }
        }
    };

    //专门用了一个Handler对象处理别名的注册问题
    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("id..............",Web.getUser().getUserId()+"");
            JPushInterface.setAliasAndTags(context, Web.getUser().getUserId()+"", tags, mAliasCallback);
        }
    };

}
