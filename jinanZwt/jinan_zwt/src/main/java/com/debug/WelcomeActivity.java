package com.debug;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.request.file.FileManager;
import com.zt.capacity.common.request.file.IFileManager;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.HttpUtil;
import com.zt.capacity.common.util.JurisdictionUtil;
import com.zt.capacity.jinan_zwt.Main;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainActivity;
import com.zt.capacity.jinan_zwt.bean.VersionBean;
import com.zt.capacity.jinan_zwt.request.user.IUserManager;
import com.zt.capacity.jinan_zwt.request.user.UserManager;
import com.zt.capacity.jinan_zwt.request.versioninformation.IVersionInformationManager;
import com.zt.capacity.jinan_zwt.request.versioninformation.VersionInformationManager;
import com.zt.capacity.jinan_zwt.util.HWPushUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.Response;

import static java.lang.Thread.sleep;


public class WelcomeActivity extends BaseActivity {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static SharedPreferences sharedPreferences;
    private String account;
    private String password;
    public static WelcomeActivity welcomeActivity = null;

    //版本
    private VersionBean mVersionBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_welcome);

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(3000);
//                    //申请安装权限
//                    JurisdictionUtil.REQUEST_INSTALL_PACKAGES(WelcomeActivity.this);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();


        welcomeActivity = this;
        sharedPreferences = getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account", null);
        password = sharedPreferences.getString("password", null);


        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.REQUEST_INSTALL_PACKAGES, "安装", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写", com.zt.capacity.common.R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", com.zt.capacity.common.R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", com.zt.capacity.common.R.drawable.permission_ic_location));
        HiPermission.create(WelcomeActivity.this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //关闭
                    }

                    @Override
                    public void onFinish() {
                        //所有权限申请完成
                        current += 25;
                        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {

                            //版本请求
                            initInformation(1);
                        } else {
                            initInformation(2);
                        }
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        //否认
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


//        JurisdictionUtil.accessibility(this,new PermissionCallback() {
//            @Override
//            public void onClose() {
//                //关闭
//            }
//
//            @Override
//            public void onFinish() {
//                //所有权限申请完成
//                current += 25;
//                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
//
//                    //版本请求
//                    initInformation(1);
//                } else {
//                    initInformation(2);
//                }
//            }
//
//            @Override
//            public void onDeny(String permission, int position) {
//                //否认
//            }
//
//            @Override
//            public void onGuarantee(String permission, int position) {
//
//            }
//        });


    }

    //检测本程序的版本，这里假设从服务器中获取到最新的版本号为3
    public void checkVersion(int type) {
        current += 25;
        //如果检测本程序的版本号小于服务器的版本号，那么提示用户更新
        if (getVersionCode() < Integer.valueOf(mVersionBean.getVersionCode())) {
            if (Integer.valueOf(TextUtils.isEmpty(mVersionBean.getVersionType()) ? "0" : mVersionBean.getVersionType()) == 1) {//强制更新
                showDialogUpdate(type, 1);//弹出提示版本更新的对话框
            } else {
                showDialogUpdate(type, 0);//弹出提示版本更新的对话框
            }

        } else {
            login();
            if (type == 1) {
                handler.sendEmptyMessageDelayed(10, delay);
            } else {
                handler.sendEmptyMessageDelayed(11, delay);
            }
            //否则吐司，说现在是最新的版本
//            Toast.makeText(this, "当前已经是最新的版本", Toast.LENGTH_SHORT).show();

        }
    }

    public void login() {
        IUserManager userLogin = UserManager.getInterface(1);
        userLogin.login(account, password, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.d("state", "" + state);
                Log.d("msg", msg);
                if (state == 0) {
                    //极光注册
                    Main.getInterface(WelcomeActivity.this);
                }
                jump(state);
            }
        });
    }


    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate(final int type, int versionType) {
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (versionType == 1) {
            // 设置提示框的标题
            builder.setTitle("版本升级").
                    // 设置提示框的图标
                            setIcon(R.mipmap.home).
                    // 设置要显示的信息
                            setMessage("发现新版本！请及时更新").
                    // 设置确定按钮
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "选择确定哦", 0).show();
                            loadNewVersionProgress();//下载最新的版本程序
                        }
                    }).

                    // 设置取消按钮,null是什么都不做，并关闭对话框
                            setNegativeButton("", null).setCancelable(false);
        } else {
            // 设置提示框的标题
            builder.setTitle("版本升级").
                    // 设置提示框的图标
                            setIcon(R.mipmap.ic_launcher).
                    // 设置要显示的信息
                            setMessage("发现新版本！请及时更新").
                    // 设置确定按钮
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "选择确定哦", 0).show();
                            loadNewVersionProgress();//下载最新的版本程序
                        }
                    }).

                    // 设置取消按钮,null是什么都不做，并关闭对话框
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (type == 1) {
                                IUserManager userLogin = UserManager.getInterface(1);
                                userLogin.login(account, password, new OnNetResultListener() {
                                    @Override
                                    public void onResult(int state, String msg, Object body) {
                                        Log.d("state", "" + state);
                                        Log.d("msg", msg);
                                        if (state == 0) {
                                            //极光注册
                                            Main.getInterface(WelcomeActivity.this);
                                        }
                                        jump(state);
//					if(state == 0){
//						ActivityJumpUtil.jumpActivity(WelcomeActivity.this,MainActivity.class);
//						finish();
//					}else {
//						ActivityJumpUtil.jumpActivity(WelcomeActivity.this,LoginActivity.class);
//						finish();
//					}
                                    }
                                });
                                handler.sendEmptyMessageDelayed(10, delay);
                            } else {
                                handler.sendEmptyMessageDelayed(11, delay);
                            }
                        }
                    }).setCancelable(false);
        }

        if (welcomeActivity != null) {
            // 生产对话框
            AlertDialog alertDialog = builder.create();
            // 显示对话框
            alertDialog.show();
        }

    }

    ProgressDialog pd;    //进度条对话框

    /**
     * 下载新版本程序
     */
    private void loadNewVersionProgress() {
//		final   String uri="http://192.168.0.6/zwb/attachment/apk/app-debug(3).apk";
        final String uri = mVersionBean.getDownloadUrl();
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在下载更新");
        pd.show();
        //启动子线程下载任务
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    File file = getFileFromServer(uri, pd);
                    sleep(1000);
                    Log.e("xiazai....", "0");
                    Log.e("xiazai....", file.getPath());
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.zt.capacity.jinan_zwt" + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            String[] command = {"chmod", "777", file.getPath()};
            ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public File getFileFromServer(String uri, ProgressDialog pd) throws Exception {
        Log.e("uri", "uri" + uri);

        File file = null;
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Response response = HttpUtil.getInstance().getDataSynFromNet(uri);

            InputStream is = null;//输入流
            FileOutputStream fos = null;//输出流
            try {
                is = response.body().byteStream();//获取输入流
                long total = response.body().contentLength();//获取文件大小
                pd.setMax((int) total);//为progressDialog设置大小
                if (is != null) {
                    Log.d("SettingPresenter", "onResponse: 不为空");
                    String path = WelcomeActivity.this.getFilesDir() + "/zt.apk";
//                    String path = Environment.getExternalStorageDirectory() + "/zt.apk";
                    file = new File(path);

                    if (file.exists()) {
                        file.delete();
                    }
                    fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int ch = -1;
                    int process = 0;
                    while ((ch = is.read(buf)) != -1) {
                        fos.write(buf, 0, ch);
                        process += ch;
                        pd.setProgress(process);
                    }
                }
                fos.flush();
                // 下载完成
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
                return file;
            } catch (Exception e) {
                Log.d("SettingPresenter", e.toString());
                return null;
            }
        } else {
            return null;
        }


    }

    /*
     * 获取当前程序的版本号
     */
    private int getVersionCode() {
        try {

            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            Log.e("TAG", "版本号" + packInfo.versionCode);
            Log.e("TAG", "版本名" + packInfo.versionName);
            return packInfo.versionCode;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return 1;
    }

    private void initInformation(final int type) {
        IVersionInformationManager versionInformationManager = VersionInformationManager.getInterface();
        versionInformationManager.getVersionInformation(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                if (state == 0) {
                    mVersionBean = (VersionBean) body;
                    if (body == null) {
                        login();
                        if (type == 1) {
                            handler.sendEmptyMessageDelayed(10, delay);
                        } else {
                            handler.sendEmptyMessageDelayed(11, delay);
                        }
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    Message message = new Message();
                    message.what = 5;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    login();
                    if (type == 1) {
                        handler.sendEmptyMessageDelayed(10, delay);
                    } else {
                        handler.sendEmptyMessageDelayed(11, delay);
                    }
                }


            }
        });
    }


    private Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0:
//				jump();
                    break;
                case 5:
                    int type = msg.getData().getInt("type");
                    checkVersion(type);
                    break;
                case 10:
                    initData();
                    break;
                case 11:
                    initData2();
                    break;
                case 12:
                    initData2();
                    break;
                default:
                    break;
            }

        }


    };
    private long delay = 300;
    private int total = 100;
    private int current = 0;
    private boolean isboolean = true;

    private void initData() {
        current += 25;
        if (state == 0) {
            if (TextUtils.isEmpty(Web.getToken())) {
                initInformation(1);
            } else {
                ActivityJumpUtil.jumpActivity(WelcomeActivity.this, MainActivity.class);
                finish();
            }
        } else {
            ActivityJumpUtil.jumpActivity(WelcomeActivity.this, LoginActivity.class);
            finish();
        }
    }

    private void initData2() {
        current += 25;
//        if (current == total) {
        ActivityJumpUtil.jumpActivity(this, LoginActivity.class);
        finish();
//        } else {
//            handler.sendEmptyMessageDelayed(11, delay);
//        }
    }

    private int sum = 0;
    private int state = 1;

    private void jump(int state) {
        sum++;
        this.state = state;
//		if (sum == 1) {
//			 finish();
//		}
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //重新打开
        if (pd != null && pd.isShowing()) {
            //正在加载

        } else {
            IUserManager userLogin = UserManager.getInterface(1);
            userLogin.login(account, password, new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    Log.d("state", "" + state);
                    Log.d("msg", msg);

                    jump(state);
                }
            });
            handler.sendEmptyMessageDelayed(10, delay);
        }

    }

}
