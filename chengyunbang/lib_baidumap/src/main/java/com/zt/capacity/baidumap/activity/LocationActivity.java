package com.zt.capacity.baidumap.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.zt.capacity.baidumap.R;
import com.zt.capacity.baidumap.adapter.MatchingAdapter;
import com.zt.capacity.baidumap.bean.AutoAnalysisdata;
import com.zt.capacity.baidumap.bean.CarRuntime;
import com.zt.capacity.baidumap.data.BaseData;
import com.zt.capacity.baidumap.fragment.LocationFBasicsFragment;
import com.zt.capacity.baidumap.fragment.LocationFPeripheryFragment;
import com.zt.capacity.baidumap.map.SwitBdItem;
import com.zt.capacity.baidumap.map.clusterutil.clustering.ClusterManager;
import com.zt.capacity.baidumap.request.baidmap.BaiDu;
import com.zt.capacity.baidumap.request.baidmap.IBaiDu;
import com.zt.capacity.baidumap.request.history.HistoryManager;
import com.zt.capacity.baidumap.request.history.IHistoryManager;
import com.zt.capacity.baidumap.request.realtimeposition.IRealTimePosition;
import com.zt.capacity.baidumap.request.realtimeposition.RealTimePosition;
import com.zt.capacity.baidumap.utils.MapOperation;
import com.zt.capacity.baidumap.utils.TransformationUtil;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.JurisdictionUtil;
import com.zt.capacity.common.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionItem;


/**
 * 实时位置
 * Created by Administrator on 2018-04-12.
 */
@Route(path = "/transit/location")
public class LocationActivity extends BaseFragmentActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private static LocationActivity context;

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题
    //返回键
    private LinearLayout headTitleImg;
    private LinearLayout mSearchImageview;//搜索框
    private AutoCompleteTextView realTimeLicensePlate;

    private Integer queryState = 1;//1全部，2搜索
//    private Integer threadState = 0;//0循环刷新线程，1停止刷新线程

    private MapView realTimeBmap;
    private static BaiduMap mBaiduMap;


    //车辆实时数据
    private List<CarRuntime> allCarRuntimes = new ArrayList<CarRuntime>();//所有车辆
    private Integer allCarRuntimesNum = 0;//所有车辆数

    private List<CarRuntime> onlieCarRuntimes = new ArrayList<>();//在线车辆
    private Integer onlieCarRuntimesNum = 0;//所有车辆数

    private List<CarRuntime> drawRuntimeList = new ArrayList<>();//绘制的数据


    //违规数据
    private static List<AutoAnalysisdata> autoAnalysisdatas;
    private Car car;
    //所有车辆
    private List<Car> cars;

    //marker
    View markerItem;
    private LinearLayout marker_number_linearlayout;
    TextView carNumberMarker;
    ImageView carImage;

    //聚集
    private static ClusterManager<SwitBdItem> mClusterManager;


    //地图点击功能按钮
    private LinearLayout location_right_layout;
    private ImageView location_take_up_right;//收起
    private Integer location_take_up_right_id = 1;//不收起
    private ImageView plate_number_button;//开关车牌显示
    private static Integer plate_number_button_id = 1;//开关车牌状态
    private ImageView gather_button;//聚集数
    private static Integer gather_button_id = 1;//聚集数状态
    private ImageView panorama_button;//全景
    public static Integer panorama_button_id = 1;//全景状态
    private ImageView legend_button;//图例开关
    protected PopupWindow popupWindow;//图例弹窗
    private ImageView traffic_button;//路况开关
    private Integer traffic_button_id = 1;//路况开关状态

    private Bundle bundleCarRuntime;


    //中心maker
    private LinearLayout marker_panorama_location;
    private LinearLayout marker_panorama_location_top;
    private ImageView maker_panorama_img;//全景小图
    public static TextView maker_panorama_text;//小图下面的文字
    private LatLng target;//小图获取的坐标


    private static LinearLayout location_bottom_linearLayout;
    private static LinearLayout location_fragment;
    protected static RelativeLayout[] mBottom_view;// 底部菜单
    protected static TextView[] mBottom_text;// 底部菜单文字
    protected static ImageView[] mBottom_img;// 底部菜单小横杆
    protected static int position = 0;// 记录当前底部选择位置
    protected Fragment mContent;
    private Fragment fragmentLocation1;
    private Fragment fragmentLocation2;


    private LinearLayout online_number_linear;//在线
    private TextView online_number;//在线数量
    private LinearLayout all_number_linear;//所有车辆
    private TextView all_number;//所有车辆数量
    private LinearLayout refurbish_number_linear;//刷新

    private Integer panelState = 1;//1在线，2所有

    private String searRecord = "";

    //更新面板
    private void upPanel() {
        if (panelState == 1) {
            online_number_linear.setBackgroundColor(getResources().getColor(R.color.entirety_theme));
            online_number.setSelected(true);
            all_number_linear.setBackgroundColor(getResources().getColor(R.color.white));
            all_number.setSelected(false);
        }
        if (panelState == 2) {
            online_number_linear.setBackgroundColor(getResources().getColor(R.color.white));
            online_number.setSelected(false);
            all_number_linear.setBackgroundColor(getResources().getColor(R.color.entirety_theme));
            ;
            all_number.setSelected(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_yunshu_activity_location);
        //初始化控件
        init();
        upPanel();
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.REQUEST_INSTALL_PACKAGES, "安装", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写", com.zt.capacity.common.R.drawable.permission_ic_storage));
//        permissionItems.add(new PermissionItem(android.Manifest.permission.RECORD_AUDIO, "录音", com.zt.capacity.common.R.drawable.permission_ic_micro_phone));
        permissionItems.add(new PermissionItem(android.Manifest.permission.ACCESS_NETWORK_STATE, "网络状态", com.zt.capacity.common.R.drawable.permission_ic_sensors));
        permissionItems.add(new PermissionItem(android.Manifest.permission.INTERNET, "联网", com.zt.capacity.common.R.drawable.permission_ic_sensors));
        permissionItems.add(new PermissionItem(android.Manifest.permission.READ_PHONE_STATE, "手机状态", com.zt.capacity.common.R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(JurisdictionUtil.ACCESS_FINE_LOCATION, "定位", com.zt.capacity.common.R.drawable.permission_ic_location));
        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(null);

        //数据请求
        creatData();

        MapOperation.setCenterPosition(mBaiduMap, BaseData.getPoint());
    }

    private void creatData() {
//        //查询所有违规
//        getAutoAnalysisdata("");
        //所有车辆信息
//        acquisitionData(0);
        //查询统计信息
//        information();
        search(0, realTimeLicensePlate.getText().toString());
        //请求所有车辆信息
        if (Web.cars.size() > 0) {
            //配置搜索框
            cars = Web.cars;
            setMatching();
        } else {
            selectAllMyCar();
        }


    }


    private void selectAllMyCar() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.arg1 = state;
                if (state == 0) {
                    message.what = 100002;
                    if (body != null) {
                        Web.cars = (List<Car>) body;
                    }
                    List<Car> cars = (List<Car>) body;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cars", (Serializable) cars);
                    message.setData(bundle);
                } else {
                    message.what = 1;
                }
                carRuntimeHandler.sendMessage(message);
            }
        });


    }


    public Handler carRuntimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UIUtils.cancelProgressDialog();
                    allCarRuntimes = (List<CarRuntime>) msg.getData().getSerializable("carRuntimes");

                    drawRuntimeList = allCarRuntimes;

                    break;
                case 1:
                    UIUtils.cancelProgressDialog();
                    //操作失败
                    ToastUtils.show(msg.obj + "");
                    Log.e("操作失败", msg.obj + "");
                    break;
                case 2:
                    maker_panorama_text.setText("查看全景>");
                    maker_panorama_img.setImageResource(R.drawable.jn_yunshu_no);
                    break;
                case 1012:
                    //token超时
                    ToastUtils.show("登录超时");
//                    Intent intent = new Intent(LocationActivity.this, LoginActivity.class);
//                    startActivity(intent);
                    break;
                case 100001:
                    //当前用户没有车辆信息
                    UIUtils.cancelProgressDialog();
                    screenCarRuntime = new HashSet<>();//重置标记数据
                    mBaiduMap.clear();
                    presents = new ArrayList<>();//没有值时清空地图绘制信息

                    mClusterManager.clearItems();//清除聚合数据
                    MapOperation.setCenterPosition(mBaiduMap, BaseData.getPoint());
                    ToastUtils.show("没有车辆信息");
                    break;
                case 100002:
                    UIUtils.cancelProgressDialog();
                    if (msg.arg1 == 0) {
                        cars = (List<Car>) msg.getData().getSerializable("cars");
                        if (cars.size() > 0) {
                            Web.cars = cars;
                        }
                    }
                    //配置搜索框
                    setMatching();
                    break;
                case 100003:
                    maker_panorama_text.setText("查看全景>");
                    byte[] imageBytes = msg.getData().getByteArray("imageBytes");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    maker_panorama_img.setImageBitmap(bitmap);
                    break;
                case 100004:
                    UIUtils.cancelProgressDialog();
                    autoAnalysisdatas = (List<AutoAnalysisdata>) msg.getData().getSerializable("autoAnalysisdatas");
                    if (autoAnalysisdatas == null) {
                        autoAnalysisdatas = new ArrayList<AutoAnalysisdata>();
                    }
                case 100006:
                    //搜索框搜索信息
                    UIUtils.cancelProgressDialog();
                    if (msg.getData().getSerializable("carRuntimes") != null) {
                        allCarRuntimes = (List<CarRuntime>) msg.getData().getSerializable("carRuntimes");
                    }

                    if (allCarRuntimes.size() > 0) {
                        //去除重复的位置

                        drawRuntimeList = allCarRuntimes;

                        //分离数据
                        fenli(allCarRuntimes);
                        allCarRuntimesNum = allCarRuntimes.size();
                        onlieCarRuntimesNum = onlieCarRuntimes.size();
                        String allNumber=allCarRuntimesNum == null ? 0 + "" : allCarRuntimesNum+"";
                        String onlieNumber=onlieCarRuntimesNum == null ? "0" : onlieCarRuntimesNum+"";
                        all_number.setText("所有(" + allNumber + ")");
                        online_number.setText("在线数(" + onlieNumber + ")");

                        screenCarRuntime = new HashSet<>();//重置标记数据
                        mBaiduMap.clear();
                        presents = new ArrayList<>();//没有值时清空地图绘制信息
                        addB();
                    } else {
                        fenli(allCarRuntimes);
                        all_number.setText("所有(0)");
                        online_number.setText("在线数(0)");
                        screenCarRuntime = new HashSet<>();//重置标记数据
                        mBaiduMap.clear();
                        presents = new ArrayList<>();//没有值时清空地图绘制信息
                        mClusterManager.clearItems();//清除聚合数据
                    }
                    break;

            }


        }
    };

    private void addB() {
        //添加标注
        screenCarRuntime = new HashSet<>();//重置标记数据
        mBaiduMap.clear();
        presents = new ArrayList<>();//没有值时清空地图绘制信息
        mClusterManager.clearItems();//清除聚合数据
        if (panelState == 1) {
            //绘制在线
            addBiaozhu(onlieCarRuntimes, 0);
        } else {
            addBiaozhu(allCarRuntimes, 0);
        }
    }

    //分离在线数据
    private void fenli(List<CarRuntime> carRuntimes) {
        onlieCarRuntimes=new ArrayList<>();
        if(carRuntimes.size()>0) {
            for (int i = 0; i < carRuntimes.size(); i++) {
                CarRuntime carRuntime = carRuntimes.get(i);
                if (!"0".equals(carRuntime.getOnlineState())) {
                    //在线
                    onlieCarRuntimes.add(carRuntime);
                }
            }
        }
    }


    private void setMatching() {
        MatchingAdapter adapter = new MatchingAdapter(this, cars);
        realTimeLicensePlate.setAdapter(adapter);
        realTimeLicensePlate.setThreshold(0);
    }

    //实时车辆查询
    private void acquisitionData(final Integer statex) {
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(1);
        realTimePosition.getCarRuntime(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = state;
                message.arg1 = statex;
                message.obj = msg;
                queryState = 1;

                if (state == 0) {
                    //操作成功
                    List<CarRuntime> carRuntimesD = (List<CarRuntime>) body;
                    if (carRuntimesD.size() > 0) {
                        Bundle carRuntimeB = new Bundle();
                        carRuntimeB.putSerializable("carRuntimes", (Serializable) carRuntimesD);
                        message.setData(carRuntimeB);

                    } else {
                        //当前用户没有车辆信息
                        message.what = 100001;
                    }
                }
                carRuntimeHandler.sendMessage(message);

            }
        });


    }


    private void init() {
        //头布局初始化
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("实时位置");

        mSearchImageview = findViewById(R.id.search_imageview);
        mSearchImageview.setOnClickListener(this);
        realTimeLicensePlate = findViewById(R.id.search);
        realTimeLicensePlate.setOnEditorActionListener(this);

        //当前布局控件

        realTimeBmap = findViewById(R.id.real_time_bmap);
        mBaiduMap = realTimeBmap.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //    maker点击事件
            @Override
            public boolean onMarkerClick(Marker marker) {
                bundleCarRuntime = marker.getExtraInfo();
//        Intent i = new Intent(LocationActivity.this, LocationCarActivity.class);
//        i.putExtra("carRuntime", bundle);
//        startActivity(i);

                if (bundleCarRuntime != null) {
                    //点击无数据的时候直接返回
                    openFragment();
                }

//                GpsfenceParameter gpsfence = (GpsfenceParameter) bundleCarRuntime.getSerializable("gpsfence");
//                if (null == gpsfence) {
//                    openFragment();
//                } else {
//
//                    Log.e("tiaozhuan....", gson.toJson(gpsfence));
//                    if (gpsfence.getConsappId() != null && !"".equals(gpsfence.getConsappId())) {
//                        //工地
//                        Activity activity = (Activity) context;
//                        Intent i = new Intent(activity, WebViewActivity.class);
//                        i.putExtra("url", "http://113.247.235.86:9080/yl/html/android-pronamedetail.html?token=" + Web.getToken() + "&consappId=" + gpsfence.getConsappId() + "&proBelong=" + gpsfence.getProBelong());
//                        context.startActivity(i);
//                    } else {
//                        //消纳场
//                        Activity activity = (Activity) context;
//                        Intent i = new Intent(activity, WebViewActivity.class);
//                        i.putExtra("url", "http://113.247.235.86:9080/yl/html/android-unloaddetail.html?token=" + Web.getToken() + "&unloadingId=" + gpsfence.getUnloadingId() + "&proBelong=" + gpsfence.getProBelong() + "&buildunitId=" + gpsfence.getBuildunitId());
//                        context.startActivity(i);
//                    }
//                }
                return true;
            }
        });
//        mBaiduMap.setOnMarkerDragListener(this);//marker拖拽监听
//        mBaiduMapsSatusChange();//地图状态监听
        //地图状态监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                //手势操作地图，设置地图状态等操作导致地图状态开始改变
                maker_panorama_text.setText("正在加载...");
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
                    target = mBaiduMap.getMapStatus().target;
                    displayPanoramic(target);
                }
                if (presents.size() > 0) {
                    //有数据需要绘制
                    addBiaozhu(presents, 1);
                }
            }
        });
//        realTimeBmap.regMapStatusChangeListener


        //聚集数
        MapOperation.hideBaiDuIcon(realTimeBmap);//隐藏百度图标
        mClusterManager = new ClusterManager<SwitBdItem>(this, mBaiduMap);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SwitBdItem>() {
            @Override
            public boolean onClusterItemClick(SwitBdItem item) {
                bundleCarRuntime = item.getBundle();
                if (bundleCarRuntime != null) {
                    //点击无数据的时候直接返回
                    openFragment();
                }
//                location_bottom_linearLayout.setVisibility(View.VISIBLE);
//                location_fragment.setVisibility(View.VISIBLE);
//                mBottom_text[position].setSelected(true);
//                mBottom_image[position].setSelected(true);
                return false;
            }
        });


        //地图点击功能按钮
        panorama_button = findViewById(R.id.panorama_button);
        plate_number_button = findViewById(R.id.plate_number_button);
        gather_button = findViewById(R.id.gather_button);
        location_take_up_right = findViewById(R.id.location_take_up_right);//收起
        location_right_layout = findViewById(R.id.location_right_layout);
        location_take_up_right.setOnClickListener(this);
        panorama_button.setOnClickListener(this);
        plate_number_button.setOnClickListener(this);
        gather_button.setOnClickListener(this);
        legend_button = findViewById(R.id.legend_button);//图例按钮
        legend_button.setOnClickListener(this);
        traffic_button = findViewById(R.id.traffic_button);//路况开关
        traffic_button.setOnClickListener(this);


        location_bottom_linearLayout = findViewById(R.id.location_bottom_linearLayout);
        location_fragment = findViewById(R.id.location_fragment);

        mBottom_text = new TextView[2];
        mBottom_text[0] = findViewById(R.id.location_text_1);
        mBottom_text[1] = findViewById(R.id.location_text_4);
        mBottom_img = new ImageView[2];
        mBottom_img[0] = findViewById(R.id.location_bottom_1_img);
        mBottom_img[1] = findViewById(R.id.location_bottom_4_img);
        mBottom_view = new RelativeLayout[2];
        mBottom_view[0] = findViewById(R.id.location_bottom_1);
        mBottom_view[1] = findViewById(R.id.location_bottom_4);

        findViewById(R.id.location_bottom_1).setOnClickListener(this);
        findViewById(R.id.location_bottom_4).setOnClickListener(this);

        //中心maker
        marker_panorama_location = findViewById(R.id.marker_panorama_location);
        marker_panorama_location_top = findViewById(R.id.marker_panorama_location_top);
        maker_panorama_img = findViewById(R.id.maker_panorama_img);//全景小图
        maker_panorama_text = findViewById(R.id.maker_panorama_text);//小图下面的文字
        marker_panorama_location_top.setOnClickListener(this);


        //面板
        online_number_linear = findViewById(R.id.online_number_linear);//在线
        online_number_linear.setOnClickListener(this);
        online_number = findViewById(R.id.online_number);//在线数量
        all_number_linear = findViewById(R.id.all_number_linear);//所有车辆
        all_number_linear.setOnClickListener(this);
        all_number = findViewById(R.id.all_number);//所有车辆数量
        refurbish_number_linear = findViewById(R.id.refurbish_number_linear);//刷新
        refurbish_number_linear.setOnClickListener(this);


    }

    // 底部导航栏选择切换
    public static void changeBottom(int i) {
        for (int j = 0; j < mBottom_text.length; j++) {
            if (j == i) {
                mBottom_text[j].setSelected(true);
                mBottom_view[j].setSelected(true);
                mBottom_img[j].setSelected(true);
                position = i;
            } else {
                mBottom_text[j].setSelected(false);
                mBottom_view[j].setSelected(false);
                mBottom_img[j].setSelected(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_img) {//返回按钮
            finish();
        } else if (i == R.id.search_imageview) {//搜索处理
//            hideKeyboard();
            search(0, realTimeLicensePlate.getText().toString());
            UmengHelper.onEvent(BaseApplication.getContext(), "ShiShiWeiZhiSouSuo");

        } else if (i == R.id.location_take_up_right) {//收起按钮
            if (location_take_up_right_id == 1) {
                location_take_up_right.setSelected(true);
                location_right_layout.setVisibility(View.INVISIBLE);
                location_take_up_right_id = 2;
            } else {
                UmengHelper.onEvent(BaseApplication.getContext(), "ShiShiWeiZhiShouQi");
                location_take_up_right.setSelected(false);
                location_right_layout.setVisibility(View.VISIBLE);
                location_take_up_right_id = 1;
            }

        } else if (i == R.id.panorama_button) {//全景
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
            UmengHelper.onEvent(BaseApplication.getContext(), "ShiShiWeiZhiQuanJin");

        } else if (i == R.id.traffic_button) {//显示路况
            if (traffic_button_id == 1) {
                //当前未开启
                traffic_button.setSelected(true);
                traffic_button_id = 2;
                mBaiduMap.setTrafficEnabled(true);//路况
            } else {
                //当前已开启
                traffic_button.setSelected(false);
                traffic_button_id = 1;
                mBaiduMap.setTrafficEnabled(false);//路况
            }
            UmengHelper.onEvent(BaseApplication.getContext(), "ShiShiWeiZhiLuKuang");

        } else if (i == R.id.plate_number_button) {//显示车牌
            if (plate_number_button_id == 1) {
                //当前未开启
                plate_number_button.setSelected(true);
                plate_number_button_id = 2;
                if (queryState == 2) {
                    dataAndPositionUpdate(1);
                } else {
                    dataAndPositionUpdate(1);
                }
            } else {
                //当前已开启
                plate_number_button.setSelected(false);
                plate_number_button_id = 1;
                if (queryState == 2) {
                    dataAndPositionUpdate(1);
                } else {
                    dataAndPositionUpdate(1);
                }
            }
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiChePai");

        } else if (i == R.id.gather_button) {//聚集数开关
            if (drawRuntimeList.size() > 1000) {
                ToastUtils.show("数量过多，无法关闭聚集数");
                return;
            }
            if (gather_button_id == 1) {
                //当前未开启
                gather_button.setSelected(true);
                gather_button_id = 2;
                if (queryState == 2) {
                    dataAndPositionUpdate(1);
                } else {
                    dataAndPositionUpdate(1);
                }
            } else {
                //当前已开启
                gather_button.setSelected(false);
                gather_button_id = 1;
                if (queryState == 2) {
                    dataAndPositionUpdate(1);
                } else {
                    dataAndPositionUpdate(1);
                }
            }
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiJuJiShu");

        } else if (i == R.id.legend_button) {
            openPop();
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiTuLi");

        } else if (i == R.id.marker_panorama_location_top) {//全景地图图标点击target
            Intent intent = new Intent(this, PanoramaActivity.class);
            intent.putExtra("longitude", target.longitude);
            intent.putExtra("latitude", target.latitude);
            startActivity(intent);
            UmengHelper.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiDaKaiQuanJin");

        } else if (i == R.id.location_bottom_1) {
            if (position == 0) {
                return;
            }
            fragmentLocation1 = new LocationFBasicsFragment();
            fragmentLocation1.setArguments(bundleCarRuntime);
            position = 0;
            changeBottom(position);
            switchContent(fragmentLocation1);

        } else if (i == R.id.location_bottom_4) {
            if (position == 1) {
                return;
            }
            fragmentLocation2 = new LocationFPeripheryFragment();
            fragmentLocation2.setArguments(bundleCarRuntime);
            position = 1;
            changeBottom(position);
            switchContent(fragmentLocation2);

        } else if (i == R.id.online_number_linear) {
            //点击在线
            if (panelState == 1) {
                return;
            }
            panelState = 1;
            upPanel();
            addB();

        } else if (i == R.id.all_number_linear) {
            //点击所有
            if (panelState == 2) {
                return;
            }
            panelState = 2;
            upPanel();
            addB();

        } else if (i == R.id.refurbish_number_linear) {
            //点击刷新
            search(0, searRecord);
        }
    }


    //数据更新加位置调整
    private void dataAndPositionUpdate(Integer zxd) {
        //清空地图

        if (allCarRuntimes.size() > 0) {
            //去除重复的位置
            allCarRuntimes = TransformationUtil.removeDuplicate(allCarRuntimes);

            screenCarRuntime = new HashSet<>();//重置地图标注数据

            mBaiduMap.clear();

            //添加标注
            addB();
        } else {
            mBaiduMap.clear();
            presents = new ArrayList<>();//没有值时清空地图绘制信息
            mClusterManager.clearItems();//清除聚合数据
        }

    }


    //展示全景小图
    public static void displayPanoramic(LatLng latLng) {
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
                context.carRuntimeHandler.sendMessage(message);
            }
        });

    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = this.getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.location_fragment, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    //搜索
    public void search(final Integer statex, String searchString) {
        //记录搜索字符
        searRecord = searchString;
        //搜索处理
//        if ("".equals(searchString)) {
//            //数据请求
//            ToastUtils.show("请输入车牌号");
//        } else {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(2);
        realTimePosition.carRuntimeByCarNumber(searchString, "", new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100006;
                message.arg1 = statex;
                message.obj = msg;
                queryState = 2;
                if (state == 0) {
                    //操作成功
                    List<CarRuntime> carRuntimesD = (List<CarRuntime>) body;
                    if (carRuntimesD != null && carRuntimesD.size() > 0) {
                        Bundle carRuntimeB = new Bundle();
                        carRuntimeB.putSerializable("carRuntimes", (Serializable) carRuntimesD);
                        message.setData(carRuntimeB);

                    } else {
                        //当前用户没有车辆信息
                        message.what = 100001;
                    }
                } else {
                    message.what = 1;
                }
                carRuntimeHandler.sendMessage(message);
            }
        });


//            if (carRuntimes.size() > 0) {
//                mBaiduMap.clear();
//                int i = 0;
//                for (CarRuntime carRuntime : carRuntimes) {
//                    if (carRuntime.getCarNumber().contains(searchString)) {
//                        LatLng point = new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX());
//                        if (i == 0) {
//                            setCenterPosition(point);
//                        }
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("carRuntime", carRuntime);
//                        addCarMarker(point, bundle);
//                        i++;
//                    }
//                }
//            } else {
//                UIUtils.showToast(this, "当前帐号下没有车辆");
//            }
//        }
    }


    //在地图上添加车辆
    public void addCarMarker(final LatLng point, final Bundle bundle, final View markerItem) {
//        CarRuntime carRuntime = (CarRuntime) bundle.getSerializable("carRuntime");


//        //单个点
//        markerItem = View.inflate(this, R.layout.marker_item, null);
//        carNumberMarker = markerItem.findViewById(R.id.car_number_marker);
//        carImage = markerItem.findViewById(R.id.car_image);
//        //marker布局
//        Bitmap i = BitmapFactory.decodeResource(this.getResources(), R.drawable.car_bimg);
//        Bitmap x = MapOperation.rotateBitmap(i, carRuntime.getGpsDirect());
//        carImage.setImageBitmap(x);
//
//        carNumberMarker.setText(carRuntime.getCarNumber());
//        if(plate_number_button_id == 1){
//            carNumberMarker.setVisibility(View.GONE);
//        }else{
//            carNumberMarker.setVisibility(View.VISIBLE);
//        }


        Bitmap bitmap = MapOperation.viewToBitmap(markerItem);
        //创建OverlayOptions的集合
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.car_marker);
        MarkerOptions options = new MarkerOptions()
                .position(point)//设置位置
//                .rotate(carRuntime.getGpsDirect())//设置方向
                .icon(bitmapDescriptor)//设置图标样式
                .zIndex(9) // 设置marker所在层级
                .draggable(false); // 设置手势拖拽;
        //添加marker
        Marker marker = (Marker) mBaiduMap.addOverlay(options);
        marker.setExtraInfo(bundle);


    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            search(0, realTimeLicensePlate.getText().toString());
            return true;
        }
        return false;
    }


    @Override
    public void onDestroy() {

        //由于地图监听冲突，过多使用静态变量，所以在界面销毁时要初始化变量值，不然会导致程序崩溃
        mBaiduMap = null;
        autoAnalysisdatas = null;
        mClusterManager = null;
        plate_number_button_id = 1;
        gather_button_id = 1;
        panorama_button_id = 1;


        location_bottom_linearLayout = null;
        location_fragment = null;
        mBottom_view = null;// 底部菜单
        mBottom_text = null;// 底部菜单文字
        mBottom_img = null;// 底部菜单小横杆
        position = 0;// 记录当前底部选择位置

        screenCarRuntime = new HashSet<>();//当前已绘制的点
        presents = new ArrayList<>();//地图需要绘制的点

        jn_cluster_state = 0;

        /**
         *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
         */
        realTimeBmap.onDestroy();
        super.onDestroy();
    }


    /**
     * 15秒刷新一次地图
     * state:0刷新、其它数字为停止
     */
//    class RealThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//            while (threadState == 0) {
//                //刷新数据
//                try {
//                    sleep(15000);
//                    Log.d("queryState请求刷新", queryState + "");
//                    if (queryState == 2) {
//                        search(1);
//                    } else {
//                        getAutoAnalysisdata("", 1);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public static void close() {

        location_fragment.setVisibility(View.GONE);
//        location_bottom_linearLayout.setVisibility(View.GONE);
    }

    private void openFragment() {
        // fragment管理器初始化
        FragmentManager mFragmentManager = this.getSupportFragmentManager();
        // 开启事务
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        if (fragmentLocation1 != null)
            mFragmentTransaction.remove(fragmentLocation1);
        if (fragmentLocation2 != null)
            mFragmentTransaction.remove(fragmentLocation2);
        // 初始化主页面
        position = 0;
        fragmentLocation1 = new LocationFBasicsFragment();
        fragmentLocation1.setArguments(bundleCarRuntime);
        mFragmentTransaction.add(R.id.location_fragment, fragmentLocation1);
        mContent = fragmentLocation1;
        mFragmentTransaction.commit();
        changeBottom(position);


        location_fragment.setVisibility(View.VISIBLE);
//        location_bottom_linearLayout.setVisibility(View.VISIBLE);


    }


    //弹出遮罩层
    public void openPop() {

        //图例按钮
        View contentView = LayoutInflater.from(this).inflate(R.layout.jn_zwt_layout_legend, null);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


        View popView = LayoutInflater.from(this).inflate(
                R.layout.jn_zwt_layout_legend, null);
        View rootView = findViewById(R.id.activity_location_id); // 當前頁面的根佈局
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundAlpha(0.8f);//设置屏幕透明度

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口

        //隐藏时关掉透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        // 顯示在根佈局的右边
        popupWindow.showAtLocation(rootView, Gravity.TOP | Gravity.RIGHT, 0,
                0);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    //绘制maker图片
    private View drawMakerImg(CarRuntime carRuntime) {
        markerItem = View.inflate(this, R.layout.jn_zwt_marker_item, null);
        marker_number_linearlayout = markerItem.findViewById(R.id.marker_number_linearlayout);
        Integer y = 0;//违规数
        if (autoAnalysisdatas != null && autoAnalysisdatas.size() > 0) {
            for (Integer x = 0; x < autoAnalysisdatas.size(); x++) {
                if (carRuntime.getCarNumber().equals(autoAnalysisdatas.get(x).getCarNumber())) {
                    y++;
                }
            }
        }
        if (y < 3) {
            marker_number_linearlayout.setBackgroundResource(R.drawable.jn_zwt_real_time_marker_greed);
        }
        carNumberMarker = markerItem.findViewById(R.id.car_number_marker);
        carImage = markerItem.findViewById(R.id.car_image);
        carNumberMarker.setText(carRuntime.getCarNumber());
        Bitmap ix = BitmapFactory.decodeResource(this.getResources(), R.drawable.jn_zwt_green_car);
        if ("0".equals(carRuntime.getOnlineState())) {
            ix = BitmapFactory.decodeResource(this.getResources(), R.drawable.jn_zwt_car_bimg_off);
        }
        Bitmap b = MapOperation.rotateBitmap(ix, carRuntime.getGpsDirect());

        carImage.setImageBitmap(b);
        marker_number_linearlayout = markerItem.findViewById(R.id.marker_number_linearlayout);
        if (plate_number_button_id == 1) {
            marker_number_linearlayout.setVisibility(View.GONE);
        } else {
            marker_number_linearlayout.setVisibility(View.VISIBLE);
        }
        return markerItem;
    }


    private static Set<CarRuntime> screenCarRuntime = new HashSet<>();//当前已绘制的点
    public static List<CarRuntime> presents = new ArrayList<>();//地图需要绘制的点

    //添加地图标注
    private void addBiaozhu(List<CarRuntime> carRuntimes, Integer zxd) {
        //清空地图
        mClusterManager.clearItems();//清除聚合数据
        final List<SwitBdItem> items = new ArrayList<SwitBdItem>();
        if (carRuntimes != null && carRuntimes.size() > 0) {
            presents = carRuntimes;//赋值给当前要绘制的值
            for (int i = 0; i < carRuntimes.size(); i++) {
                CarRuntime carRuntime = carRuntimes.get(i);
                LatLng point;

                //判断gps是否故障，故障给与随机坐标点
                if (carRuntime.getCarState().substring(9, 10).equals("1") || carRuntime.getGpsPosX() < 1 || carRuntime.getGpsPosY() < 1) {//gps故障
                    point = TransformationUtil.stochasticLatlon();
                } else {
                    point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX()));
                }

                if (zxd == 0 || carRuntimes.size() > 300) {
                    if (i == 0) {
                        MapOperation.setCenterPositionAndZoom(mBaiduMap, point, 13);

                    }
                }


                Bundle bundle = new Bundle();
                bundle.putSerializable("carRuntime", carRuntime);
                if (point == null || mBaiduMap == null) {
                    return;
                }
                //判断是否在屏幕范围内
                //获取屏幕范围
//                if (TransformationUtil.isScreen(mBaiduMap, point)) {
                //在屏幕范围内
                if (screenCarRuntime.add(carRuntime)) {
                    //当前要添加的不在已绘制的标记里
                    markerItem = drawMakerImg(carRuntime);
                    items.add(new SwitBdItem(point, markerItem, bundle));
                    if (gather_button_id == 1 && carRuntimes.size() < 300) {
                        //不需要聚合
                        addCarMarker(point, bundle, markerItem);
                    }
                }
//                }

            }
        }
        if (gather_button_id == 2 || carRuntimes.size() > 300) {

            jn_cluster_state = 1;
            if (gather_button_id == 1) {
                //数据过多自动开启
                gather_button.setSelected(true);
                gather_button_id = 2;
                ToastUtils.show("数据过多自动开启聚合");
            }

            //点聚合
            mClusterManager.addItems(items);
            // 设置地图监听，当地图状态发生改变时，进行点聚合运
            mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
            // 设置maker点击时的响应
//                    mBaiduMap.setOnMarkerClickListener(mClusterManager);
            //设置比例
            Log.e("缩放级别", mBaiduMap.getMapStatus().zoom + "");
            Float zoomLevel = mBaiduMap.getMapStatus().zoom + 0.0001f;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                    new MapStatus.Builder().zoom(zoomLevel).build()));
            zoomLevel = mBaiduMap.getMapStatus().zoom - 0.0001f;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                    new MapStatus.Builder().zoom(zoomLevel).build()));


        }
    }

    public static Integer jn_cluster_state = 0;

    public static void juheJinan(List<CarRuntime> carRuntimes) {

        if (carRuntimes != null && carRuntimes.size() > 0) {

            final List<SwitBdItem> items = new ArrayList<SwitBdItem>();
            for (int i = 0; i < carRuntimes.size(); i++) {
                CarRuntime carRuntime = carRuntimes.get(i);
                LatLng point;


                final Bundle bundle = new Bundle();
                bundle.putSerializable("carRuntime", carRuntime);

                //判断gps是否故障，故障给与随机坐标点
                if (carRuntime.getCarState().substring(9, 10).equals("1") || carRuntime.getGpsPosX() < 1 || carRuntime.getGpsPosY() < 1) {//gps故障
                    point = TransformationUtil.stochasticLatlon();
                } else {
                    point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX()));
                }

                //判断gps是否故障，故障给与随机坐标点
                if (carRuntime.getCarState().substring(9, 10).equals("1") || carRuntime.getGpsPosX() < 1 || carRuntime.getGpsPosY() < 1) {//gps故障
                    point = TransformationUtil.stochasticLatlon();
                } else {
                    point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX()));
                }

                if (point == null || mBaiduMap == null) {
                    return;
                }

                //判断是否在屏幕范围内
                //获取屏幕范围
                if (TransformationUtil.isScreen(mBaiduMap, point)) {
                    //在屏幕范围内
                    if (screenCarRuntime.add(carRuntime)) {
                        //当前要添加的不在已绘制的标记里
                        final View markerItem = View.inflate(BaseApplication.getContext(), R.layout.jn_zwt_marker_item, null);
                        LinearLayout marker_number_linearlayout = markerItem.findViewById(R.id.marker_number_linearlayout);
                        Integer y = 0;//违规数
                        if (autoAnalysisdatas != null && autoAnalysisdatas.size() > 0) {
                            for (Integer x = 0; x < autoAnalysisdatas.size(); x++) {
                                if (carRuntime.getCarNumber().equals(autoAnalysisdatas.get(x).getCarNumber())) {
                                    y++;
                                }
                            }
                        }
                        if (y < 3) {
                            marker_number_linearlayout.setBackgroundResource(R.drawable.jn_zwt_real_time_marker_greed);
                        }
                        TextView carNumberMarker = markerItem.findViewById(R.id.car_number_marker);
                        ImageView carImage = markerItem.findViewById(R.id.car_image);
                        carNumberMarker.setText(carRuntime.getCarNumber());
                        Bitmap ix = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), R.drawable.jn_zwt_green_car);
                        if ("0".equals(carRuntime.getOnlineState())) {
                            ix = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), R.drawable.jn_zwt_car_bimg_off);
                        }
                        Bitmap b = MapOperation.rotateBitmap(ix, carRuntime.getGpsDirect());

                        carImage.setImageBitmap(b);
                        marker_number_linearlayout = markerItem.findViewById(R.id.marker_number_linearlayout);
                        if (plate_number_button_id == 1) {
                            marker_number_linearlayout.setVisibility(View.GONE);
                        } else {
                            marker_number_linearlayout.setVisibility(View.VISIBLE);
                        }
                        items.add(new SwitBdItem(point, markerItem, bundle));

                        final LatLng ll = point;
                        if (gather_button_id == 1 && carRuntimes.size() < 300) {
                            //不需要聚合
                            Bitmap bitmap = MapOperation.viewToBitmap(markerItem);
                            //创建OverlayOptions的集合
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                            //        BitmapDescriptor bitmap = BitmapDescriptorFactory
                            //                .fromResource(R.drawable.car_marker);
                            MarkerOptions options = new MarkerOptions()
                                    .position(ll)//设置位置
                                    //                .rotate(carRuntime.getGpsDirect())//设置方向
                                    .icon(bitmapDescriptor)//设置图标样式
                                    .zIndex(9) // 设置marker所在层级
                                    .draggable(false); // 设置手势拖拽;
                            //添加marker
                            Marker marker = (Marker) mBaiduMap.addOverlay(options);
                            marker.setExtraInfo(bundle);

                        }
                    }
                }
            }


            if (gather_button_id == 2 || carRuntimes.size() > 300) {

                //点聚合
                mClusterManager.addItems(items);
                // 设置地图监听，当地图状态发生改变时，进行点聚合运
                mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
                // 设置maker点击时的响应
//                        mBaiduMap.setOnMarkerClickListener(mClusterManager);
                //设置比例
                Log.e("缩放级别", mBaiduMap.getMapStatus().zoom + "");
                Float zoomLevel = mBaiduMap.getMapStatus().zoom + 0.0001f;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                        new MapStatus.Builder().zoom(zoomLevel).build()));
                zoomLevel = mBaiduMap.getMapStatus().zoom - 0.0001f;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                        new MapStatus.Builder().zoom(zoomLevel).build()));

            }
        }
    }


//    private void hideKeyboard() {
//        // 让软键盘消失
//        InputMethodManager im = (InputMethodManager) getActivity().
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (getActivity().getCurrentFocus() != null)
//            im.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                    .getWindowToken(), 0);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        realTimeBmap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        realTimeBmap.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
