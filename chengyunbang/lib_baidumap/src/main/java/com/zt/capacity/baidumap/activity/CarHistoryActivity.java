package com.zt.capacity.baidumap.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.zt.capacity.baidumap.R;
import com.zt.capacity.baidumap.adapter.MatchingAdapter;
import com.zt.capacity.baidumap.bean.CarHistoryBean;
import com.zt.capacity.baidumap.data.BaseData;
import com.zt.capacity.baidumap.request.baidmap.BaiDu;
import com.zt.capacity.baidumap.request.baidmap.IBaiDu;
import com.zt.capacity.baidumap.request.history.HistoryManager;
import com.zt.capacity.baidumap.request.history.IHistoryManager;
import com.zt.capacity.baidumap.utils.TransformationUtil;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.bean.CoordinatesBean;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.DateUtil;
import com.zt.capacity.common.util.TimePickerView;
import com.zt.capacity.common.util.UIUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 济南历史轨迹
 * Created by Administrator on 2018-04-12.
 */
@Route(path = "/transit/carHistory")
public class CarHistoryActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, SeekBar.OnSeekBarChangeListener {

    private LinearLayout mSearchImageview;
    private LinearLayout mDateLayout;
    private boolean mDateBollean = true;
    private TextView mStartTime;
    private TextView mEndTime;
    private AutoCompleteTextView mSearch;

    private TimePickerView pvTime1;
    private int mDateInt = 0;// 开始 结束 时间选择判断
    private long mStartTimeLong = 0;
    private long mEndTimeLong = 0;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    ;
    private Polyline mPolyline;
    private TextView mTitleName;
    private LinearLayout mTitleImageView;

    private static int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00005; // 或 0,00009
    private Handler mHandler;
    private boolean BOFANG = true;


    private List<CarHistoryBean> mCarHistoryBeanList = new ArrayList<>();

    private ImageView bofang, chongz, jiansu, jiasu;
    private TextView licheng;
    private TextView sudu;
    private SeekBar jindu_seekBar;
    private TextView start_time_textview;
    private TextView end_time_textview;

    private TextView[] mTime_text;// 底部菜单文字
    private int type = 0; //用来判断 选择时间的 类型  0 默认 今天  1  昨天  2 自己选择

    //所有车辆
    private List<Car> cars;

    //全景
    private ImageView panorama_button;//全景
    public Integer panorama_button_id = 1;//全景状态

    //中心maker
    private LinearLayout marker_panorama_location;
    private LinearLayout marker_panorama_location_top;
    private ImageView maker_panorama_img;//全景小图
    public TextView maker_panorama_text;//小图下面的文字
    private LatLng target;//小图获取的坐标
    private LinearLayout youhao;
    private View view;
    private TextView youhaotextview;

    private Integer hour = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //方法一
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.jn_zwt_activity_car_history);
        mSearchImageview = findViewById(R.id.search_imageview);
        mDateLayout = findViewById(R.id.date_layout);
        mStartTime = findViewById(R.id.start_time);
        mSearch = findViewById(R.id.search);
        mEndTime = findViewById(R.id.end_time);
        mMapView = (MapView) findViewById(R.id.bmapView);


        mTitleName = findViewById(R.id.title_name);
        mTitleImageView = findViewById(R.id.title_img);
        mTime_text = new TextView[2];
        mTime_text[0] = findViewById(R.id.today);
        mTime_text[1] = findViewById(R.id.testerday);
        mSearch.setOnEditorActionListener(this);
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mSearchImageview.setOnClickListener(this);
        mTitleImageView.setOnClickListener(this);

        youhaotextview = findViewById(R.id.youhaotextview);
        youhao = findViewById(R.id.youhao);
        start_time_textview = findViewById(R.id.start_time_textview);
        end_time_textview = findViewById(R.id.end_time_textview);
        jindu_seekBar = findViewById(R.id.jindu_seekBar);
        jindu_seekBar.setSecondaryProgress(100);
        sudu = findViewById(R.id.sudu);
        licheng = findViewById(R.id.licheng);
        bofang = findViewById(R.id.bofang);
        chongz = findViewById(R.id.chongz);
        jiansu = findViewById(R.id.jiansu);
        jiasu = findViewById(R.id.jiasu);
        bofang.setOnClickListener(this);
        chongz.setOnClickListener(this);
        jiasu.setOnClickListener(this);
        jiansu.setOnClickListener(this);
        youhao.setOnClickListener(this);

        mTitleName.setText("历史轨迹");
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        initTimePicker1();
        mTime_text[0].setSelected(true);
        mTime_text[0].setOnClickListener(this);
        mTime_text[1].setOnClickListener(this);
//        MapStatus.Builder builder = new MapStatus.Builder();
//        LatLng center =Web.getPoint(); // 默认长沙
//        float zoom = 15.0f; // 默认 11级
//        builder.target(center).zoom(zoom);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

//        LatLng ll = new LatLng(
//                28.118273, 112.973020 );
//        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//        // 移动到某经纬度
//        mBaiduMap.animateMapStatus(update);
//        update = MapStatusUpdateFactory.zoomBy(2f);
//        // 放大
//        mBaiduMap.animateMapStatus(update);
//
//        LatLng ll = new LatLng(
//                28.118273, 112.973020 );
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(BaseData.getPoint());
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //设置比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                new MapStatus.Builder().zoom(14).build()));
        mHandler = new Handler(Looper.getMainLooper());

        //隐藏百度图标
        int count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls || child instanceof ImageView) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        //请求所有车辆信息
//        CustomProgressDialog.startProgressDialog(this);
        selectAllMyCar();
        changeBottom(0);
        String stro = DateUtil.showDate(DateUtil.DATE_FORMAT_YMD, System.currentTimeMillis()) + " 00:00";
        mStartTimeLong = DateUtil.birthdayToLong(DateUtil.DATE_FORMAT_YMDHM, stro);
        String stro2 = DateUtil.showDate(DateUtil.DATE_FORMAT_YMD, System.currentTimeMillis()) + " 23:59";
        mEndTimeLong = DateUtil.birthdayToLong(DateUtil.DATE_FORMAT_YMDHM, stro2);
        mStartTime.setText(stro);
        mEndTime.setText(stro2);

        //其它页面跳转
        String carNumber = getIntent().getStringExtra("carNumber");
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");
        Integer hourg = getIntent().getIntExtra("hour", 0);
        if (carNumber != null && !"".equals(carNumber)) {
            mSearch.setText(carNumber);
            //如果有传时间就设置时间
            if (endTime != null && !"".equals(endTime)) {
                Log.e("strtime.....", startTime);
                mEndTime.setText(endTime);
                mEndTimeLong = DateUtil.toDate(endTime + ":00").getTime();

            }
            if (startTime != null && !"".equals(startTime)) {
                Log.e("strtime.....", endTime);
                mStartTime.setText(startTime);
                mStartTimeLong = DateUtil.toDate(startTime + ":00").getTime();
            }
            if (hourg != 0) {
                Log.e("hourg.....", hourg + "");
                hour = hourg;
            }
            getHistory();
        }


        //全景
        panorama_button = findViewById(R.id.panorama_hist_button);
        panorama_button.setOnClickListener(this);

        //中心maker
        marker_panorama_location = findViewById(R.id.marker_panorama_location);
        marker_panorama_location_top = findViewById(R.id.marker_panorama_location_top);
        maker_panorama_img = findViewById(R.id.maker_panorama_img);//全景小图
        maker_panorama_text = findViewById(R.id.maker_panorama_text);//小图下面的文字
        marker_panorama_location_top.setOnClickListener(this);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                //手势操作地图，设置地图状态等操作导致地图状态开始改变
//                maker_panorama_text.setText("正在加载...");
                handler.sendEmptyMessage(7);
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                //手势操作地图，设置地图状态等操作导致地图状态开始改变
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                //地图状态变化中
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //地图状态改变结束
                if (panorama_button_id == 2) {
                    handler.sendEmptyMessage(8);
                }
            }
        });

    }

    public void changeBottom(int i) {
        for (int j = 0; j < mTime_text.length; j++) {
            if (j == i) {
                mTime_text[j].setSelected(true);
            } else {
                mTime_text[j].setSelected(false);
            }
        }
    }

    private void selectAllMyCar() {
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                if (state == 0) {
                    List<Car> cars = (List<Car>) body;
                    Message message = new Message();
                    message.what = 100001;
                    message.arg1 = state;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cars", (Serializable) cars);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.search_imageview) {
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiShouSuo");
            mStop = false;
            if (mSearch.getText().toString().equals("")) {
                ToastUtils.show("车牌号不能为空");
                return;
            } else if (isCarNo(mSearch.getText().toString())) {
                ToastUtils.show("请输入完整车牌号");
                return;
            } else if (hour != 0) {
                //按最后时间差
                getHistory();
            }
            if (type == 2) {
                if (mStartTimeLong == 0) {
                    ToastUtils.show("请选择开始时间");
                    return;
                } else if (mEndTimeLong == 0) {
                    ToastUtils.show("请选择结束时间");
                    return;
                } else if (mEndTimeLong < mStartTimeLong) {
                    ToastUtils.show("开始时间不能大于结束时间");
                    return;
                } else if ((mEndTimeLong - mStartTimeLong) > 86400000) {
                    ToastUtils.show("开始结束时间不能大于24小时");
                    return;
                } else {
                    UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
                    getHistory();
                }
            } else {
                UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
                getHistory();
            }

        } else if (i == R.id.start_time) {
            Log.e("mStartTime", mStartTime.getText().toString());
            // 让软键盘消失
//            hideKeyboard();
            mDateInt = 1;
            pvTime1.show();

        } else if (i == R.id.end_time) {//                mCustomDatePickerTwo.show(DateUtil.DATE_FORMAT_YMD);
            // 让软键盘消失
//            hideKeyboard();
            mDateInt = 2;
            pvTime1.show();

        } else if (i == R.id.title_img) {
            finish();

        } else if (i == R.id.today) {
            type = 0;
            hour = 0;
//                time();
            changeBottom(0);
            String stro = DateUtil.showDate(DateUtil.DATE_FORMAT_YMD, System.currentTimeMillis()) + " 00:00";
            mStartTimeLong = DateUtil.birthdayToLong(DateUtil.DATE_FORMAT_YMDHM, stro);
            String stro2 = DateUtil.showDate(DateUtil.DATE_FORMAT_YMD, System.currentTimeMillis()) + " 23:59";
            mEndTimeLong = DateUtil.birthdayToLong(DateUtil.DATE_FORMAT_YMDHM, stro2);
            mStartTime.setText(stro);
            mEndTime.setText(stro2);
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiJinRi");

        } else if (i == R.id.testerday) {
            hour = 1;
            type = 1;
            changeBottom(1);
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiZuiHouYiXiaoShi");

        } else if (i == R.id.bofang) {
            if (BOFANG) {
                BOFANG = false;
                bofang.setImageResource(R.drawable.jn_zwt_zanting_img);
                if (mStop) {
                    my.resumeThread();
                } else {
                    if (mCarHistoryBeanList == null || mCarHistoryBeanList.size() < 2) {
                        ToastUtils.show("无轨迹可播放");
                        BOFANG = true;
                        bofang.setImageResource(R.drawable.jn_zwt_bofang_img);
                        mStop = false;
                    } else {
                        addImageView2(mCarHistoryBeanList, false);
                    }
                }
            } else {
                BOFANG = true;
                bofang.setImageResource(R.drawable.jn_zwt_bofang_img);
                mStop = true;
                if (my != null) {
                    Log.e("bofang", "--pauseThread");
                    my.pauseThread();

                } else {
                    mStop = false;
                }
            }
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiBoFang");

        } else if (i == R.id.chongz) {//                if(mCarHistoryBeanList ==null || mCarHistoryBeanList.size()<2){
//                    UIUtils.showToast(this,"无轨迹可重置");
//                }else {
//                    addImageView2(mCarHistoryBeanList);
//                }
            if (my == null) return;
            mIntPos = 0;
            mPosBoolean = true;
            my.resumeThread();
            handler.sendEmptyMessageDelayed(6, 200);
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiChongZhi");

        } else if (i == R.id.jiansu) {
            if (TIME_INTERVAL < 640) {
                TIME_INTERVAL = TIME_INTERVAL * 2;
            } else {
                TIME_INTERVAL = 80;
            }
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiJianSu");

        } else if (i == R.id.jiasu) {
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiJiaSu");
            if (TIME_INTERVAL > 10) {
                TIME_INTERVAL = TIME_INTERVAL / 2;
            } else {
                TIME_INTERVAL = 80;
            }

        } else if (i == R.id.panorama_hist_button) {//全景按钮
            if (panorama_button_id == 1) {
                //当前未开启
                panorama_button_id = 2;
                panorama_button.setSelected(true);
                marker_panorama_location.setVisibility(View.VISIBLE);

                //展示全景
                target = mBaiduMap.getMapStatus().target;
                displayPanoramic(target);
            } else {
                //当前已开启
                panorama_button_id = 1;
                panorama_button.setSelected(false);
                marker_panorama_location.setVisibility(View.INVISIBLE);

            }
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiQuanJin");

        } else if (i == R.id.marker_panorama_location_top) {//全景地图图标点击target
            Intent intent = new Intent(CarHistoryActivity.this, PanoramaActivity.class);
            intent.putExtra("longitude", target.longitude);
            intent.putExtra("latitude", target.latitude);
            startActivity(intent);
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiDaKaiQuanJin");

        } else if (i == R.id.youhao) {
            mEditText = new EditText(this);
            view = View.inflate(this, R.layout.alertdialog, null);
            final EditText e = view.findViewById(R.id.youhao_e);
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("根据经验设置油耗")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String stre = e.getText().toString().equals("") ? "0" : e.getText().toString();
                            String lic = licheng.getText().toString();
                            int streint = Integer.valueOf(stre);
                            double licInt = Double.valueOf(lic);
                            double youhaol = (licInt * streint / 100);
                            BigDecimal bg = new BigDecimal(youhaol);
                            double youhaols = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            youhaotextview.setText("" + youhaols);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            UmengHelper.onEvent(BaseApplication.getContext(), "JNLiShiGuiJiYouHaoSheZhi");

        } else {
        }
    }

    private EditText mEditText;
    private AlertDialog mAlertDialog;
    private boolean mStop;


    private void getHistory() {
        mBaiduMap.clear();
        mStop = false;
        if (my != null) {
            BOFANG = true;
            mIntPos = 0;
            bofang.setImageResource(R.drawable.jn_zwt_bofang_img);
            my.colse();
        }

        IHistoryManager historyManager = HistoryManager.getInterface(1);
        Log.e("time", DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, mStartTimeLong) + "---" + DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, mEndTimeLong));
        historyManager.getHistory(mSearch.getText().toString(), DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, mStartTimeLong),
                DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, mEndTimeLong), hour,
                new OnNetResultListener() {
                    @Override
                    public void onResult(int state, String msg, Object body) {
                        UIUtils.cancelProgressDialog();
                        if (state == 0) {
                            if (body != null) {
                                mCarHistoryBeanList = (List<CarHistoryBean>) body;
                            }
                            //过滤掉错误的信息
                            if (mCarHistoryBeanList.size() > 0) {

//                                for (CarHistoryBean carHistory : mCarHistoryBeanList) {
//                                    if (carHistory.getGpsPosX() < 10.0 || carHistory.getGpsPosY() < 10.0) {
//                                        mCarHistoryBeanList.remove(carHistory);
//                                    }
//                                }

                                for (int i = 0; i < mCarHistoryBeanList.size(); i++) {
                                    if (mCarHistoryBeanList.get(i).getGpsPosX() < 10.0 || mCarHistoryBeanList.get(i).getGpsPosY() < 10.0) {
                                        mCarHistoryBeanList.remove(mCarHistoryBeanList.get(i));
                                    }
                                }
                            }
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if (my != null) {
            my.pauseThread();
        }
        mStop = false;
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mCarHistoryBeanList != null) {
                        Log.e("mCarHistoryBeanList", mCarHistoryBeanList.size() + "");
                        double distance = 0; //总里程
                        List<Long> listL = new ArrayList<Long>();
                        int x = 0;
                        for (int i = 0; i < mCarHistoryBeanList.size(); i++) {
                            if (mCarHistoryBeanList.get(i).getGpsPosX() > 1 && mCarHistoryBeanList.get(i).getGpsPosY() > 1) {
                                if (x == 0) {
                                    ifFrist = true;
                                    CoordinatesBean coordinatesBean = new CoordinatesBean();
                                    coordinatesBean.setX(mCarHistoryBeanList.get(i).getGpsPosX());
                                    coordinatesBean.setY(mCarHistoryBeanList.get(i).getGpsPosY());
                                    coordinatesBean.setAlpha(mCarHistoryBeanList.get(i).getGpsDirect());
                                    addImageView(coordinatesBean);
                                    navigateTo(mCarHistoryBeanList.get(0).getGpsPosX(), mCarHistoryBeanList.get(0).getGpsPosY());
                                } else {
                                    distance = distance + getDistance(mCarHistoryBeanList.get(i - 1).getGpsPosY(), mCarHistoryBeanList.get(i - 1).getGpsPosX(), mCarHistoryBeanList.get(i).getGpsPosY(), mCarHistoryBeanList.get(i).getGpsPosX());
                                }
                                listL.add(mCarHistoryBeanList.get(i).getSendDatetime());
                                x++;
                            } else {
                                mCarHistoryBeanList.remove(mCarHistoryBeanList.get(i));
                            }

                        }
                        if (listL.size() > 0) {
                            Log.e("sdadasdasdad", DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS, listL.get(0)));
                            Log.e("sdadasdasdad", DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS, listL.get(listL.size() - 1)));
                            start_time_textview.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS, listL.get(0)));
                            end_time_textview.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS, listL.get(listL.size() - 1)));

                            mStartTime.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, listL.get(0)));
                            mEndTime.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, listL.get(listL.size() - 1)));
                        }
                        licheng.setText("" + distance / 1000);
                        Log.e("distance", distance / 1000 + "km");
                        if (mCarHistoryBeanList == null || mCarHistoryBeanList.size() < 2) {
                            ToastUtils.show( "无轨迹可播放");
                        } else {
                            addImageView2(mCarHistoryBeanList, true);
                        }
                        if (mCarHistoryBeanList.size() == 0) {
//                            ToastUtils.show("没有找到对应的历史轨迹");
                            return;
                        }

                    } else {
                        ToastUtils.show("没有找到对应的历史轨迹");
                        return;
                    }
                    addCustomElementsDemo(mCarHistoryBeanList);
                    break;
                case 1:
                    ToastUtils.show("搜索失败");
                    break;

                case 100001:
                    if (msg.arg1 == 0) {
                        cars = (List<Car>) msg.getData().getSerializable("cars");
                    }
                    UIUtils.cancelProgressDialog();

                    //配置搜索框
                    setMatching();


//                    IAPI iapi= API.getInterface(1);
//                    iapi.peccancyApi(cars.get(0), "HUN_CS", new OnNetResultListener() {
//                        @Override
//                        public void onResult(int state, String msg, Object body) {
//
//                        }
//                    });
                    break;
                case 100003:
                    maker_panorama_text.setText("查看全景>");
                    byte[] imageBytes = msg.getData().getByteArray("imageBytes");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    maker_panorama_img.setImageBitmap(bitmap);
                    break;
                case 5:
                    String strsud = msg.getData().getString("sudu");
                    int pos = msg.getData().getInt("pos");
                    long send = msg.getData().getLong("send");
                    sudu.setText("" + strsud);
                    jindu_seekBar.setProgress(pos);
                    start_time_textview.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS, send));
                    break;
                case 6:
                    mPosBoolean = false;
                    Log.e("my6", "--");
                    my.pauseThread();
                    BOFANG = true;
                    bofang.setImageResource(R.drawable.jn_zwt_bofang_img);
                    mStop = true;
                    TIME_INTERVAL = 80;
                    break;
                case 7:
                    maker_panorama_text.setText("正在加载...");
                    break;

                case 8:
                    target = mBaiduMap.getMapStatus().target;
                    displayPanoramic(target);
                    break;
            }
        }
    };
    private static final double EARTH_RADIUS = 6378137.0;

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private void zhuan(LatLng latLng) {
        Point pt = mBaiduMap.getMapStatus().targetScreen;

        Point point = mBaiduMap.getProjection().toScreenLocation(latLng);
        if (point.x < 0 || point.x > pt.x * 2 || point.y < 0 || point.y > pt.y * 2) {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        }

    }

    private void setMatching() {
        MatchingAdapter adapter = new MatchingAdapter(this, cars);
        mSearch.setAdapter(adapter);
        mSearch.setThreshold(0);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEND
                || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                  st_search = search.getText().toString();
//					UIUtils.showToast(getActivity(), "搜索....", Toast.LENGTH_SHORT)
//							.show();
            switch (keyEvent.getAction()) {
                case KeyEvent.ACTION_UP:
                    //发送请求
                    // 让软键盘消失
//                    hideKeyboard();
                    mStop = false;
                    Log.e("mStartTimeLong", mStartTimeLong + " mEndTimeLong" + mEndTimeLong);
                    if (mSearch.getText().toString().equals("")) {
                        ToastUtils.show("车牌号不能为空");
                        break;
                    } else if (isCarNo(mSearch.getText().toString())) {
                        ToastUtils.show("请输入完整车牌号");
                        break;
                    }
                    if (hour != 0) {
                        //按最后时间差
                        getHistory();
                    } else if (type == 2) {
                        if (mStartTimeLong == 0) {
                            ToastUtils.show("请选择开始时间");
                            break;
                        } else if (mEndTimeLong == 0) {
                            ToastUtils.show("请选择结束时间");
                            break;
                        } else if (mEndTimeLong < mStartTimeLong) {
                            ToastUtils.show("开始时间不能大于结束时间");
                            break;
                        } else if ((mEndTimeLong - mStartTimeLong) > 86400000) {
                            ToastUtils.show("开始结束时间不能大于24小时");
                            break;
                        } else {
                            UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
                            getHistory();
                        }
                    } else {
                        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
                        getHistory();
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    boolean ifFrist = true;

    private void update(double x, double y) {
        // 按照经纬度确定地图位置
        if (ifFrist) {
            Log.d("lod", x + "--" + y);
            LatLng ll = new LatLng(y,
                    x);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            mBaiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomBy(2f);
            // 放大
            mBaiduMap.animateMapStatus(update);

            ifFrist = false;
        }
    }

    private void navigateTo(double x, double y) {
        if (ifFrist) {
            ifFrist = false;
            LatLng ll = TransformationUtil.gpsToBaiduCoordinate(new LatLng(y,
                    x));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        // 按照经纬度确定地图位置
//        if (ifFrist) {
//            Log.d("lod",x+"--"+y);
//            LatLng ll = new LatLng(y ,
//                    x);
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            // 移动到某经纬度
//            mBaiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomBy(2f);
//            // 放大
//            mBaiduMap.animateMapStatus(update);
//
//            ifFrist = false;
//        }
        // 显示个人位置图标
//        MyLocationData.Builder builder = new MyLocationData.Builder();
//        builder.latitude(28.1149);
//        builder.longitude(112.5842);
//        MyLocationData data = builder.build();
//        mBaiduMap.setMyLocationData(data);
    }


    /**
     * 判断是否是车牌号
     */
    public static boolean isCarNo(String CarNum) {
        //匹配第一位汉字
        String str = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼甲乙丙己庚辛壬寅辰戍午未申";
        CarNum = CarNum.toUpperCase();
        if (!(CarNum == null || CarNum.equals(""))) {
            String s1 = CarNum.substring(0, 1); //获取字符串的第一个字符
            if (str.contains(s1)) {
                String s2 = CarNum.substring(1, CarNum.length()); //不包含I O i o的判断
//                if (s2.contains("I") || s2.contains("i") || s2.contains("O") || s2.contains("o")) {
//                    return true;
//                } else {
                if (!CarNum.matches("^[\u4e00-\u9fa5]{1}[A-Z]{1}[a-z_A-Z_0-9]{5}$")) {
                    return true;
                }
//            }
            } else {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


    double getRound(double temp) {
        double i = Math.round(Math.random() * 9 + 1);
        if (i % 2 == 0) {
            return temp + i * 0.0001;
        } else {
            return temp - i * 0.0001;
        }
    }

    Marker marker = null;

    /**
     * 描绘点
     */
    public void addImageView(CoordinatesBean coordinatesBean) {
        if (marker != null) {
            marker.remove();
        }

        Log.d("CoordinatesBean", coordinatesBean.getX() + "y" + coordinatesBean.getY());
        LatLng point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(coordinatesBean.getY(), coordinatesBean.getX()));

//构建Marker图标

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.jn_zwt_green_car);


//构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point).rotate(coordinatesBean.getAlpha())
                .icon(bitmap).anchor(0.5f, 0.5f);
//在地图上添加Marker，并显示

        marker = (Marker) mBaiduMap.addOverlay(option);

    }

    private List<Double> listDouble;
    private List<Long> listLong;

    public void addImageView2(List<CarHistoryBean> coordinatesBean, boolean cboolean) {
        if (marker != null) {
            marker.remove();
        }
        List<LatLng> latLng = new ArrayList<LatLng>();
        listDouble = new ArrayList<Double>();
        listLong = new ArrayList<Long>();
        Log.e("size", coordinatesBean.size() + "");
        for (int i = 0; i < coordinatesBean.size(); i++) {
            latLng.add(TransformationUtil.gpsToBaiduCoordinate(new LatLng(coordinatesBean.get(i).getGpsPosY(), coordinatesBean.get(i).getGpsPosX())));
            listDouble.add(coordinatesBean.get(i).getGpsSpeed());
            listLong.add(coordinatesBean.get(i).getSendDatetime());
        }
        ;
//        start_time_textview.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS,listLong.get(0)));
//        end_time_textview.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_HMSS,listLong.get(listLong.size()-1)));
        jindu_seekBar.setMax(listDouble.size() - 2);
        jindu_seekBar.setOnSeekBarChangeListener(this);
//        Log.d("CoordinatesBean", coordinatesBean.getX() + "y" + coordinatesBean.getY());
        LatLng point = TransformationUtil.gpsToBaiduCoordinate(latLng.get(0));

//构建Marker图标

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.jn_zwt_green_car);


//构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point).rotate(coordinatesBean.get(0).getGpsDirect())
                .icon(bitmap).anchor(0.5f, 0.5f);
//在地图上添加Marker，并显示

        marker = (Marker) mBaiduMap.addOverlay(option);
//        moveLooper(latLng , marker);
        my = new MyThread(latLng, listDouble, listLong, marker);
        my.starts();
        Thread thread = new Thread(my);
        thread.start();
        if (cboolean) {
            Log.e("pauseThread", "-----");
            my.pauseThread();
            mStop = true;
        }
    }

    MyThread my;
    private Thread myt;
    private boolean mPosBoolean = false;

    /**
     * 当进度条发生变化时调用该方法
     *
     * @param seekBar
     * @param
     * @param
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.e("seekBar", i + "");
        if (mPosBoolean) {
            mIntPos = i;
            my.resumeThread();
//              my.pauseThread();
        }

    }

    /**
     * 开始滑动时调用该方法
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPosBoolean = true;
        Log.e("dianji", "点击了");
        my.pauseThread();
    }

    /**
     * 结束滑动时调用该方法
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("BOFANG", BOFANG + "");

        if (BOFANG) {
            try {
                Thread.sleep(30);
                my.pauseThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Thread.sleep(30);
                my.resumeThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mPosBoolean = false;
        Log.e("dianji", "松开了");
    }

    private int mIntPos = 0;

    /**
     * 循环进行移动逻辑
     */
    class MyThread extends Thread {
        List<LatLng> polylines;
        Marker mMoveMarker;
        List<Double> listDoubles;
        List<Long> listLongs;

        MyThread(List<LatLng> polylines, List<Double> listDouble, List<Long> listLong, Marker mMoveMarker) {
            this.polylines = polylines;
            this.mMoveMarker = mMoveMarker;
            this.listDoubles = listDouble;
            listLongs = listLong;
        }

        private final Object lock = new Object();
        private boolean pause = false;
        boolean colse = true;

        /**
         * 调用这个方法实现暂停线程
         */
        void pauseThread() {
            Log.e("pause", "------");
            pause = true;
        }

        void colse() {
            colse = false;
        }

        void starts() {
            colse = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        void resumeThread() {
            pause = false;
            synchronized (lock) {
                Log.e("lock", "---resume");
                lock.notifyAll();
            }
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    Log.e("lock", "---onPause");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                int index = 0;
                // 让线程处于暂停等待状态
                while (colse) {
                    for (int i = 0; i < polylines.size() - 1; i++) {
                        if (mPosBoolean) {
                            i = mIntPos;
                        }
                        if (!colse) {
                            break;
                        }
                        final LatLng startPoint = polylines.get(i);
                        final LatLng endPoint = polylines.get(i + 1);
                        Log.e("--", startPoint.latitude + "/"
                                + startPoint.longitude);
                        Bundle bundle = new Bundle();
                        bundle.putString("sudu", String.valueOf(listDoubles.get(i)));
                        bundle.putInt("pos", i);
                        bundle.putLong("send", listLongs.get(i));
                        Message message = new Message();
                        message.what = 5;
                        message.setData(bundle);
                        handler.sendMessage(message);
                        mMoveMarker.setPosition(startPoint);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(
                                        startPoint, endPoint));
                            }
                        });
                        Log.e("mPosBoolean", "---" + mPosBoolean);
                        if (mPosBoolean) {
                            zhuan(new LatLng(startPoint.latitude, startPoint.longitude));
                            Log.e("pos", "pauseThread");
                            my.pauseThread();
                        }
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope)
                                : -1 * getXMoveDistance(slope);

                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j
                                - xMoveDistance) {
                            LatLng latLng = null;
                            Log.e("pause", pause + "");
                            while (pause) {
                                onPause();
                            }
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }
                            zhuan(latLng);
                            if (mPosBoolean) {
                                break;
                            }

                            if (!colse) {
                                break;
                            }
//                            ifFrist = true;
//                            update(latLng.longitude,latLng.latitude);
                            final LatLng finalLatLng = latLng;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);// 走完一圈回到起点
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }


    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo(List<CarHistoryBean> carHistoryBeanList) {
        if (carHistoryBeanList.size() < 2) {
            return;
        }
        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> colorValue = new ArrayList<Integer>();
        for (int i = 0; i < carHistoryBeanList.size(); i++) {
            Log.e("getGpsPosY", carHistoryBeanList.get(i).getGpsPosY() + "w" + carHistoryBeanList.get(i).getGpsPosX());
            if (carHistoryBeanList.get(i).getGpsPosY() > 1 && carHistoryBeanList.get(i).getGpsPosX() > 0) {
                LatLng p = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carHistoryBeanList.get(i).getGpsPosY(),
                        carHistoryBeanList.get(i).getGpsPosX()));
                points.add(p);
                if (carHistoryBeanList.get(i).getGpsSpeed() < 40) {
                    colorValue.add(getResources().getColor(R.color.car_speed_green));
                } else if (carHistoryBeanList.get(i).getGpsSpeed() < 60) {
                    colorValue.add(getResources().getColor(R.color.car_speed_orange));
                } else {
                    colorValue.add(getResources().getColor(R.color.car_speed_red));
                }
            }
        }

        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points).colorsValues(colorValue);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

//        // 添加普通折线绘制
//        LatLng p1 = new LatLng(28.235,
//                112.931);
//        LatLng p2 = new LatLng(28.245,
//                112.925);
//        LatLng p3 = new LatLng(28.225,
//                112.925);
//        List<LatLng> points = new ArrayList<LatLng>();
//        points.add(p1);
//        points.add(p2);
//        points.add(p3);
//        OverlayOptions ooPolyline = new PolylineOptions().width(10)
//                .color(0xAAFF0000).points(points);
//        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);


//        // 添加多颜色分段的折线绘制
//        LatLng p11 = new LatLng(39.965, 116.444);
//        LatLng p21 = new LatLng(39.925, 116.494);
//        LatLng p31 = new LatLng(39.955, 116.534);
//        LatLng p41 = new LatLng(39.905, 116.594);
//        LatLng p51 = new LatLng(39.965, 116.644);
//        List<LatLng> points1 = new ArrayList<LatLng>();
//        points1.add(p11);
//        points1.add(p21);
//        points1.add(p31);
//        points1.add(p41);
//        points1.add(p51);
//        List<Integer> colorValue = new ArrayList<Integer>();
//        colorValue.add(0xAAFF0000);
//        colorValue.add(0xAA00FF00);
//        colorValue.add(0xAA0000FF);
//        OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
//                .color(0xAAFF0000).points(points1).colorsValues(colorValue);
//        mColorfulPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline1);
//
//        // 添加多纹理分段的折线绘制
//        LatLng p111 = new LatLng(39.865, 116.444);
//        LatLng p211 = new LatLng(39.825, 116.494);
//        LatLng p311 = new LatLng(39.855, 116.534);
//        LatLng p411 = new LatLng(39.805, 116.594);
//        List<LatLng> points11 = new ArrayList<LatLng>();
//        points11.add(p111);
//        points11.add(p211);
//        points11.add(p311);
//        points11.add(p411);
//        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
//        textureList.add(mRedTexture);
//        textureList.add(mBlueTexture);
//        textureList.add(mGreenTexture);
//        List<Integer> textureIndexs = new ArrayList<Integer>();
//        textureIndexs.add(0);
//        textureIndexs.add(1);
//        textureIndexs.add(2);
//        OverlayOptions ooPolyline11 = new PolylineOptions().width(20)
//                .points(points11).dottedLine(true).customTextureList(textureList).textureIndex(textureIndexs);
//        mTexturePolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline11);
//
//        // 添加弧线
//        OverlayOptions ooArc = new ArcOptions().color(0xAA00FF00).width(4)
//                .points(p1, p2, p3);
//        mBaiduMap.addOverlay(ooArc);
//
//        // 添加圆
//        LatLng llCircle = new LatLng(39.90923, 116.447428);
//        OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
//                .center(llCircle).stroke(new Stroke(5, 0xAA000000))
//                .radius(1400);
//        mBaiduMap.addOverlay(ooCircle);
//
//        LatLng llDot = new LatLng(39.98923, 116.397428);
//        OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
//                .color(0xFF0000FF);
//        mBaiduMap.addOverlay(ooDot);
//
//        // 添加多边形
//        LatLng pt1 = new LatLng(39.93923, 116.357428);
//        LatLng pt2 = new LatLng(39.91923, 116.327428);
//        LatLng pt3 = new LatLng(39.89923, 116.347428);
//        LatLng pt4 = new LatLng(39.89923, 116.367428);
//        LatLng pt5 = new LatLng(39.91923, 116.387428);
//        List<LatLng> pts = new ArrayList<LatLng>();
//        pts.add(pt1);
//        pts.add(pt2);
//        pts.add(pt3);
//        pts.add(pt4);
//        pts.add(pt5);
//        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
//                .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
//        mBaiduMap.addOverlay(ooPolygon);
//
//        // 添加文字
//        LatLng llText = new LatLng(39.86923, 116.397428);
//        OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
//                .fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
//                .position(llText);
//        mBaiduMap.addOverlay(ooText);
    }

//    private void hideKeyboard() {
//        // 让软键盘消失
//        InputMethodManager im = (InputMethodManager) this.
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(this.getCurrentFocus()
//                .getWindowToken(), 0);
//    }

    private void initTimePicker1() {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        //时间选择器
        pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                changeBottom(3);
                type = 2;
                hour = 0;
                if (mDateInt == 1) {
                    mStartTimeLong = date.getTime();
                    mStartTime.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, date));
                } else {
                    mEndTimeLong = date.getTime();
                    mEndTime.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_YMDHM, date));
                }
            }
        })

                .setType(new boolean[]{true, true, true, true, true, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setCancelColor(getResources().getColor(R.color.color_1AAD19))
                .setSubmitColor(getResources().getColor(R.color.color_1AAD19))
                .setContentSize(16)
                .setSubCalSize(16)
//                .setTitleText("选择时间")
//                .setTitleSize(16)
                .setTitleBgColor(getResources().getColor(R.color.color_f6f6f6))
//                .setBgColor(getResources().getColor(R.color.white))
//                .setTitleColor(getResources().getColor(R.color.white))
                .setDividerColor(getResources().getColor(R.color.colors_ebebeb))
                .setTextColorCenter(getResources().getColor(R.color.color_151515))//设置选中项的颜色
                .setTextColorOut(getResources().getColor(R.color.colors_b7b7b7))//设置没有被选中项的颜色
                .setContentSize(22)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.6f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }


    //展示全景小图
    public void displayPanoramic(LatLng latLng) {
        maker_panorama_text.setText("正在加载..");

        //图pain请求116.313393,40.04778
        String location = latLng.longitude + "," + latLng.latitude;
        IBaiDu baiDu = BaiDu.getInterface(1);
        baiDu.postPanoramaImage(location, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.arg1 = state;
                if (state == 0) {
                    byte[] bytes = (byte[]) body;
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("imageBytes", bytes);
                    message.what = 100003;
                    message.setData(bundle);
                } else {
                    message.what = state;
                }
                handler.sendMessage(message);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
