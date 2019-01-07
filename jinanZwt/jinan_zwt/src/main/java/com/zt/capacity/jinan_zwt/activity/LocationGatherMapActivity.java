package com.zt.capacity.jinan_zwt.activity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.service.LocationService;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.CustomDialog;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.common.util.map.MapOperation;
import com.zt.capacity.common.util.map.TransformationUtil;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.Consapp;
import com.zt.capacity.jinan_zwt.bean.ConsappVo;
import com.zt.capacity.jinan_zwt.bean.GpsfenceBean;
import com.zt.capacity.jinan_zwt.bean.Unloading;
import com.zt.capacity.jinan_zwt.request.consapp.ConsappManager;
import com.zt.capacity.jinan_zwt.request.consapp.IConsappManager;

/**
 * 位置采集
 */
public class LocationGatherMapActivity extends BaseActivity implements View.OnClickListener {
    // 头布局
    private ImageView title_img;
    private TextView title_name;

    private BaiduMap mBaiduMap;

    private TextView gather_map_proname;//工程名称
    private MapView map_view;//地图
    private TextView gather_map_submit;//确定
    private TextView gather_map_back;//返回按钮

    //数据
    private ConsappVo consappVo;
    private Consapp consapp;
    private Unloading unloading;

    private GpsfenceBean gpsfence;//围栏信息

    private String consappId;
    private String unloadingId;

    private TextView gather_name;//名称

    private LatLng chuShiWeiZhi;//初始位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_activity_location_gather_map);


        //定位
        MyApplication.locationService = new LocationService(getApplicationContext());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        MyApplication.locationService.registerListener(MyApplication.myLocationListener);
        //注册监听
        MyApplication.locationService.setLocationOption(MyApplication.locationService.getDefaultLocationClientOption());
        MyApplication.locationService.start();


        chuShiWeiZhi = Web.getPoint();
        //初始化控件
        init();
        consappVo = (ConsappVo) getIntent().getSerializableExtra("consappVo");
        if (consappVo != null) {
            setProname();
        }
        consappId = getIntent().getStringExtra("consappId");
        unloadingId = getIntent().getStringExtra("unloadingId");
        if (consappId != null && !TextUtils.isEmpty(consappId)) {
            getConsapp();
        }
        if (unloadingId != null && !TextUtils.isEmpty(unloadingId)) {
            getUnloading();
        }

    }

    private void init() {
        map_view = findViewById(R.id.map_view);//地图
        mBaiduMap = map_view.getMap();
        //隐藏百度图标
        MapOperation.hideBaiDuIcon(map_view);


        //头
        title_img = findViewById(R.id.title_img);
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);
        title_name.setText("位置采集");

        gather_map_proname = findViewById(R.id.gather_map_proname);//工程名称
        gather_map_submit = findViewById(R.id.gather_map_submit);//确定
        gather_map_back = findViewById(R.id.gather_map_back);//返回按钮
        gather_map_back.setOnClickListener(this);

        gather_name = findViewById(R.id.gather_name);
    }

    private void setProname() {
        if (consappVo != null) {
            gather_map_proname.setText(consappVo.getProName().toString());
            gather_name.setText("工 程 名 称：");
        }
        if (consapp != null) {
            gather_map_proname.setText(consapp.getProName().toString());
            gather_name.setText("工 程 名 称：");
            if (consapp.getGpsAddress() != null && !TextUtils.isEmpty(consapp.getGpsAddress())) {
                gather_map_submit.setText("重新定位");
                gather_map_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTGongDiChongZhi");

                        CustomDialog.Builder builder2 = new CustomDialog.Builder(LocationGatherMapActivity.this);
                        builder2.setMessage("是否要重新定位");
                        builder2.setTitle("重新定位");
                        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //设置你的操作事项
                            }
                        });

                        builder2.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        chuShiWeiZhi = Web.getPoint();
                                        gather_map_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                inputData();
                                            }
                                        });
                                        gather_map_submit.setText("确定");
                                        setCoenter(1);

                                        dialog.dismiss();
                                    }
                                });

                        builder2.create().show();
                    }
                });
                getHistoryGps(consapp.getGpsAddress());

            } else {
                chuShiWeiZhi = Web.getPoint();
                gather_map_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTGongDiTiJiao");
                        inputData();
                    }
                });
                gather_map_submit.setText("确定");
                setCoenter(1);
            }
        }
        if (unloading != null) {
            gather_map_proname.setText(unloading.getUnloadingName().toString());
            gather_name.setText("消纳场名称：");
            Log.e("weizhi........x", unloading.getGpsAddress());
            if (unloading.getGpsAddress() != null && !TextUtils.isEmpty(unloading.getGpsAddress())) {
                gather_map_submit.setText("重新定位");
                gather_map_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTXiaoNaChangChongZhi");
                        CustomDialog.Builder builder2 = new CustomDialog.Builder(LocationGatherMapActivity.this);
                        builder2.setMessage("是否要重新定位");
                        builder2.setTitle("重新定位");
                        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //设置你的操作事项
                            }
                        });

                        builder2.setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        chuShiWeiZhi = Web.getPoint();
                                        gather_map_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                inputData();
                                            }
                                        });
                                        gather_map_submit.setText("确定");
                                        setCoenter(1);
                                        dialog.dismiss();
                                    }
                                });

                        builder2.create().show();
                    }
                });
                getHistoryGps(unloading.getGpsAddress());
            } else {
                chuShiWeiZhi = Web.getPoint();
                gather_map_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTXiaoNaChangTiJiao");
                        inputData();
                    }
                });
                gather_map_submit.setText("确定");
                setCoenter(1);
            }
        }
    }

    private void getHistoryGps(String gspAdress) {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IConsappManager consappManager = ConsappManager.getInterface(6);
        consappManager.getConsappPos(gspAdress, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        gpsfence = (GpsfenceBean) body;
                        if (gpsfence.getFencePos().split(",") != null && gpsfence.getFencePos().split(",").length > 0) {

                            Log.e("shujuiii.....", gpsfence.getFencePos().split(",")[1]);
                            Log.e("shujuiii.....", gpsfence.getFencePos().split(",")[0]);

                            chuShiWeiZhi = TransformationUtil.gpsToBaiduCoordinate(new LatLng(Double.parseDouble(gpsfence.getFencePos().split(",")[1]), Double.parseDouble(gpsfence.getFencePos().split(",")[0])));
//                            chuShiWeiZhi = new LatLng(Double.parseDouble(gpsfence.getFencePos().split(",")[1]), Double.parseDouble(gpsfence.getFencePos().split(",")[0]));
                        }
                    }
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UIUtils.cancelProgressDialog();
                    if ("0".equals(msg.obj.toString())) {
                        UIUtils.showToast(LocationGatherMapActivity.this, "提交成功");
                        finish();
                    } else
                        UIUtils.showToast(LocationGatherMapActivity.this, "提交失败");
                    break;
                case 1:
                    //失败
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(LocationGatherMapActivity.this, msg.obj + "");
                case 2:
                    //工地
                    UIUtils.cancelProgressDialog();
                    setProname();
                case 3:
                    //消纳场
                    UIUtils.cancelProgressDialog();
                    setProname();
                case 4:
                    //请求成功
                    UIUtils.cancelProgressDialog();
                    setCoenter(2);
            }
        }
    };
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    //获取工地信息
    private void getConsapp() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IConsappManager consappManager = ConsappManager.getInterface(3);
        consappManager.getConsappInfo(Integer.parseInt(consappId), new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        consapp = (Consapp) body;
                    }
                    message.what = 2;
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });
    }

    //获取消纳场信息
    private void getUnloading() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IConsappManager consappManager = ConsappManager.getInterface(4);
        consappManager.getUnloadingInfo(Integer.parseInt(unloadingId), new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        unloading = (Unloading) body;
                    }
                    message.what = 3;
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });
    }

    //提交信息
    private void inputData() {
        LatLng latLng = chuShiWeiZhi;
//        latLng = TransformationUtil.baiduToGps(latLng);
        Log.e("shujuiii.....1", latLng.latitude + "");
        Log.e("shujuiii.....2", latLng.longitude + "");

        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        String cityId = "";
        String consappIdS = "";
        String unloadingIdS = "";
        String licNumber = "";
        if (consappVo != null) {
            consappIdS = consappVo.getConsappId() != null ? consappVo.getConsappId() + "" : "";
            cityId = consappVo.getGpsPos().getCityId() != null ? consappVo.getGpsPos().getCityId() + "" : "";
            licNumber = consappVo.getLicNumber();

            IConsappManager consappManager = ConsappManager.getInterface(2);
            consappManager.inputGatherData(cityId, consappIdS, licNumber, latLng.longitude + "", latLng.latitude + "", new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    Message message = new Message();
                    message.what = state;
                    message.obj = body;
                    handler.sendMessage(message);
                }
            });
        }
        if (consapp != null) {
            consappIdS = consapp.getConsappId() != null ? consapp.getConsappId() + "" : "";
            cityId = consapp.getCityId() != null ? consapp.getCityId() + "" : "";
            licNumber = consapp.getLicNumber();

            IConsappManager consappManager = ConsappManager.getInterface(2);
            consappManager.inputGatherData(cityId, consappIdS, licNumber, latLng.longitude + "", latLng.latitude + "", new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    Message message = new Message();
                    message.what = state;
                    message.obj = body;
                    handler.sendMessage(message);
                }
            });
        }
        if (unloading != null) {
            unloadingIdS = unloading.getUnloadingId() != null ? unloading.getUnloadingId() + "" : "";
            cityId = unloading.getCityId() != null ? unloading.getCityId() + "" : "";

            IConsappManager consappManager = ConsappManager.getInterface(5);
            consappManager.inputUnlodGatherData(cityId, unloadingIdS, licNumber, latLng.longitude + "", latLng.latitude + "", new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    Message message = new Message();
                    message.what = state;
                    message.obj = body;
                    handler.sendMessage(message);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_img) {
            finish();

        } else if (i == R.id.gather_map_back) {
            finish();

        }
    }

    //设置中心点
    private void setCoenter(int code) {
        if (code == 1) {
            //设置中心点
            mBaiduMap.clear();
            MapOperation.setCenterPosition(mBaiduMap, chuShiWeiZhi);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jn_zwt_maker_panorama_tagging));
            MarkerOptions options = new MarkerOptions()
                    .position(Web.getPoint())//设置位置
//                .rotate(carRuntime.getGpsDirect())//设置方向
                    .icon(bitmapDescriptor)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(true); // 设置手势拖拽;
            //添加marker
            mBaiduMap.addOverlay(options);
            mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng latLng = marker.getPosition();//拖动结束获取当前位置
                    double d = DistanceUtil.getDistance(latLng, chuShiWeiZhi);
                    if (d > 5000) {
                        Log.e("dayu200", "dayu200");
                        UIUtils.showToast(LocationGatherMapActivity.this, "移动距离大于5000米无法移动");
                        setCoenter(1);
                    } else {
                        chuShiWeiZhi = latLng;
                    }
                }

                @Override
                public void onMarkerDragStart(Marker marker) {

                }
            });


        } else {
            //设置中心点
            mBaiduMap.clear();
            MapOperation.setCenterPosition(mBaiduMap, chuShiWeiZhi);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jn_zwt_maker_panorama_tagging));
            MarkerOptions options = new MarkerOptions()
                    .position(chuShiWeiZhi)//设置位置
//                .rotate(carRuntime.getGpsDirect())//设置方向
                    .icon(bitmapDescriptor)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(false); // 设置手势拖拽;
            //添加marker
            mBaiduMap.addOverlay(options);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTWeiZhiCaiJiCaiJi"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTWeiZhiCaiJiCaiJi"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
