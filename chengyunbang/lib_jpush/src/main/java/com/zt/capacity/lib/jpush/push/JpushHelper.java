package com.zt.capacity.lib.jpush.push;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 极光辅助类
 *
 * @author lt
 * @time 2018/12/17 13:14
 **/
public class JpushHelper {

    private static boolean isAlias = false;//是否设置成功别名
    private Set<String> tags = new HashSet<>();//标签
    private String alias = "";//标签

    private static Context context;

    private static JpushHelper jpushHelper=null;

    //初始化
    public static JpushHelper init(Context contextx) {

        Log.e("contex.......2",contextx+"");
        if(jpushHelper==null) {
            //极光初始化
            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(contextx);            // 初始化 JPush
            jpushHelper=new JpushHelper();
            context = contextx;
        }
        return jpushHelper;
    }

    /**
     * 设置标签别名
     *
     * @param tags  标签
     * @param alias 别名
     */
    public void setAliasAndTag(Set<String> tags, String alias) {
        if (tags != null) {
            this.tags = tags;
        }
        if (alias != null) {
            this.alias = alias;
        }
        //设置别名
        if (!isAlias) {
            mHandler.sendMessage(new Message());
        }
    }

    //清空通知
    public void clearAllNotifications(){
        JPushInterface.clearAllNotifications(context);
    }


    private TagAliasCallback mAliasCallback = new TagAliasCallback() {
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
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (tags.size() > 0 && !TextUtils.isEmpty(alias)) {
                JPushInterface.setAliasAndTags(context, alias, tags, mAliasCallback);
                return;
            }
            if (tags.size() > 0) {
                JPushInterface.setTags(context, tags, mAliasCallback);
            }
            if (!TextUtils.isEmpty(alias)) {
                JPushInterface.setAlias(context, alias, mAliasCallback);
            }
        }
    };



}
