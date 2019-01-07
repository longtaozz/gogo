package com.zt.capacity.jinan_zwt.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.debug.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.jinan_zwt.activity.WebViewActivity;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.CustomDialog;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainActivity;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

/**
 * 济南我的页面
 */
public class MainMyFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout out_login;

    private LinearLayout problem_feedback;//问题反馈

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_fragment_my, null);

        init();

        return view;
    }

    private void init() {
        problem_feedback=view.findViewById(R.id.problem_feedback);
        problem_feedback.setOnClickListener(this);
        out_login = view.findViewById(R.id.out_login);
        out_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.out_login) {
            CustomDialog.Builder builder2 = new CustomDialog.Builder(getActivity());
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
                            ;
                            ((Activity) context).finish();
//                                SharedPreferences sp = BApplication.getContext()
////                                        .getSharedPreferences("userInfo", Context.MODE_PRIVATE);
////                                SharedPreferences.Editor editor = sp.edit();
////                                editor.putString("password", "");
////                                editor.commit();// 提交修改
                            Web.setToken("");
                            ActivityJumpUtil.jumpActivity(getActivity(), LoginActivity.class);
                        }
                    });

            builder2.create().show();


        } else if (i1 == R.id.problem_feedback) {
            Intent i = new Intent(getActivity(), WebViewActivity.class);
            i.putExtra("url", JN_ZWT_Url.JINANQUESTION + Web.getToken());
            getActivity().startActivity(i);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNShouYeWoDe"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNShouYeWoDe");
    }
}
