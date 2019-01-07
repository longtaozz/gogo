package com.zt.capacity.jinan_zwt.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.debug.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.common.base.BaseFragmentActivity;
import com.zt.capacity.common.base.PanoramaActivity;
import com.zt.capacity.common.bean.CoordinatesBean;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.map.SwitBdItem;
import com.zt.capacity.common.map.clusterutil.clustering.ClusterManager;
import com.zt.capacity.common.util.IToast;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.common.util.map.MapOperation;
import com.zt.capacity.common.util.map.TransformationUtil;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainActivity;
import com.zt.capacity.jinan_zwt.adapter.MatchingAdapter;
import com.zt.capacity.jinan_zwt.bean.AutoAnalysisdata;
import com.zt.capacity.jinan_zwt.bean.Car;
import com.zt.capacity.jinan_zwt.bean.CarRuntime;
import com.zt.capacity.jinan_zwt.bean.GpsfenceParameter;
import com.zt.capacity.jinan_zwt.bean.StatisticsInfo;
import com.zt.capacity.jinan_zwt.request.baidmap.BaiDu;
import com.zt.capacity.jinan_zwt.request.baidmap.IBaiDu;
import com.zt.capacity.jinan_zwt.request.gpsFence.GpsFenceManager;
import com.zt.capacity.jinan_zwt.request.gpsFence.IGpsFenceManager;
import com.zt.capacity.jinan_zwt.request.history.HistoryManager;
import com.zt.capacity.jinan_zwt.request.history.IHistoryManager;
import com.zt.capacity.jinan_zwt.request.realtimeposition.IRealTimePosition;
import com.zt.capacity.jinan_zwt.request.realtimeposition.RealTimePosition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 实时位置
 * Created by Administrator on 2018-04-12.
 */
public class LocationFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static LocationFragment context;

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    //头布局
    //返回键
//    private LinearLayout headTitleImg;
    private LinearLayout mSearchImageview;//搜索框
    private AutoCompleteTextView realTimeLicensePlate;

    private Integer queryState = 1;//1全部，2搜索
//    private Integer threadState = 0;//0循环刷新线程，1停止刷新线程

    private TextureMapView realTimeBmap;
    private static BaiduMap mBaiduMap;


    private List<CarRuntime> llCarRuntimes = new ArrayList<CarRuntime>();//工具栏所用的所有车辆
    //车辆实时数据
    private List<CarRuntime> allCarRuntimes = new ArrayList<CarRuntime>();//所有车辆

    private List<CarRuntime> onlineCarRuntimes = new ArrayList<CarRuntime>();//所有在线车辆
    private List<CarRuntime> offlineCarRuntimes = new ArrayList<CarRuntime>();//所有离线车辆
    private List<CarRuntime> normalCarRuntimes = new ArrayList<CarRuntime>();//正常车辆
    private List<CarRuntime> unlicensedCarRuntimes = new ArrayList<CarRuntime>();//无证车辆
    private List<CarRuntime> gpsCarRuntimes = new ArrayList<CarRuntime>();//gps故障车辆
    private List<CarRuntime> stateCarRuntimes = new ArrayList<CarRuntime>();//状态失联
    private List<CarRuntime> openBoxCarRuntimes = new ArrayList<CarRuntime>();//开厢行驶
    public static List<CarRuntime> carRuntimeList = new ArrayList<>();//选择的数据

    private List<CarRuntime> drawRuntimeList = new ArrayList<>();//绘制的数据


    private StatisticsInfo statisticsInfo;//选择面板统计数据


    //0为未选中，1为选中
    int allCar_int = 0;
    int normalCar_int = 0;
    int unlicensedCar_int = 0;
    int gpsCar_int = 0;
    int stateCar_int = 0;
    int openBoxCar_int = 0;
    int consumption_int = 0;
    int construction_int = 0;


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


    private LinearLayout malfunction_screen;//筛选

    //状态统计控件
    private TextView allCar;
    private TextView normalCar;
    private TextView unlicensedCar;
    private TextView gpsCar;
    private TextView stateCar;
    private TextView openBoxCar;

    //选框
    private LinearLayout allCar_CheckBox;
    private LinearLayout normalCar_CheckBox;
    private LinearLayout unlicensedCar_CheckBox;
    private LinearLayout gpsCar_CheckBox;
    private LinearLayout stateCar_CheckBox;
    private LinearLayout openBoxCar_CheckBox;
    //新加选框
    private LinearLayout consumption_CheckBox;//消纳场
    private TextView consumption_number;//消纳场数量
    private LinearLayout construction_CheckBox;//工地
    private TextView construction_number;//工地数量
    //选框中收起按钮
    private LinearLayout pack_up_option;//收起按钮
    //选项卡
    private LinearLayout option_box;

    //工具栏中统计面板按钮
    private ImageView statistics_button;

    //消纳场围栏集合
    private List<GpsfenceParameter> unloadingGpsfences = new ArrayList<>();//信息集合
    private List<Overlay> unloadingMarkerAndLin = new ArrayList<>();//marker和围栏集合
    //工地围栏集合
    private List<GpsfenceParameter> consappGpsfences = new ArrayList<>();
    private List<Overlay> consappMarkerAndLin = new ArrayList<>();//marker和围栏集合


    private static ExecutorService drawThreadPool;//用来绘制地图的线程


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_activity_location_f, null);
        context = new LocationFragment();

        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //初始化控件
        init();
        //数据请求
        creatData();

        if (getArguments() != null) {
            String carNumber = getArguments().getString("carNumber");
            realTimeLicensePlate.setText(carNumber != null ? carNumber : "");
            if (carNumber != null && !TextUtils.isEmpty(carNumber)) {
                search(0);
            } else {
                UIUtils.showToast(getActivity(), "无车牌!");
            }
        }

        MapOperation.setCenterPosition(mBaiduMap, Web.jn_point);

        //初始化绘制线程
        drawThreadPool = Executors.newCachedThreadPool();

        return view;
    }


    private void creatData() {
//        //查询所有违规
//        getAutoAnalysisdata("");
        //所有车辆信息
//        acquisitionData(0);
        //查询统计信息
//        information();
//        //请求所有车辆信息
        if (MainActivity.cars.size() > 0) {
            //配置搜索框
            UIUtils.cancelProgressDialog();
            cars = MainActivity.cars;
            setMatching();
        } else {
            selectAllMyCar();
        }


    }

    //查询统计信息
    private void information() {
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(8);
        realTimePosition.getStatisticsJN(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100005;
                message.obj = msg;
                if (state == 0) {
                    //操作成功
                    statisticsInfo = (StatisticsInfo) body;
                } else {
                    message.what = 10000003;
                }
                carRuntimeHandler.sendMessage(message);

            }
        });
    }

    private void selectAllMyCar() {
        UIUtils.showLoadingProgressDialog(getActivity(), R.string.loading_process_tip, false);
        IHistoryManager historyManager = HistoryManager.getInterface(2);
        historyManager.selectAllMyCar(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.arg1 = state;
                if (state == 0) {
                    message.what = 100002;
                    if (body != null) {
                        MainActivity.cars = (List<Car>) body;
                    }
                    List<Car> cars = (List<Car>) body;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cars", (Serializable) cars);
                    message.setData(bundle);
                } else {
                    message.what = state;
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
                    //显示统计框
                    malfunction_screen.setVisibility(View.GONE);
                    //隐藏右边工具栏
                    option_box.setVisibility(View.GONE);
                    location_take_up_right.setSelected(true);
                    location_right_layout.setVisibility(View.INVISIBLE);
                    location_take_up_right_id = 2;

                    UIUtils.cancelProgressDialog();
                    onlineCarRuntimes = (List<CarRuntime>) msg.getData().getSerializable("carRuntimes");
                    allCarRuntimes.addAll(onlineCarRuntimes);

                    drawRuntimeList = onlineCarRuntimes;
                    dataAndPositionUpdate(0);
                    calculationCount(onlineCarRuntimes);
                    break;
                case 1:
                    UIUtils.cancelProgressDialog();
                    //操作失败
                    IToast.show(getActivity(), msg.obj + "");
                    Log.e("操作失败", msg.obj + "");
                    break;
                case 2:
                    maker_panorama_text.setText("查看全景>");
                    maker_panorama_img.setImageResource(R.drawable.jn_zwt_no_img);
                    break;
                case 1012:
                    //token超时
                    IToast.show(getActivity(), "登录超时");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case 100001:
                    //当前用户没有车辆信息
                    UIUtils.cancelProgressDialog();
                    screenCarRuntime = new HashSet<>();//重置标记数据
                    mBaiduMap.clear();
                    presents = new ArrayList<>();//没有值时清空地图绘制信息
                    if (unloadingMarkerAndLin.size() > 0) {
                        //有消纳场信息
                        for (GpsfenceParameter gpsfence : unloadingGpsfences) {
                            drownPolygon(gpsfence, 1);
                        }
                    }
                    if (consappMarkerAndLin.size() > 0) {
                        //有消纳场信息
                        for (GpsfenceParameter gpsfence : consappGpsfences) {
                            drownPolygon(gpsfence, 2);
                        }
                    }
                    mClusterManager.clearItems();//清除聚合数据
                    MapOperation.setCenterPosition(mBaiduMap, Web.jn_point);
                    IToast.show(getActivity(), "没有车辆信息");
                    break;
                case 100002:
                    UIUtils.cancelProgressDialog();
                    if (msg.arg1 == 0) {
                        cars = (List<Car>) msg.getData().getSerializable("cars");
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
                case 100005:
                    //选择面板统计信息
                    if (statisticsInfo != null) {
                        consumption_number.setText(statisticsInfo.getUnloadingotal() != null ? statisticsInfo.getUnloadingotal().toString() : "0");
                        construction_number.setText(statisticsInfo.getConsappTotal() != null ? statisticsInfo.getConsappTotal().toString() : "0");
                        normalCar.setText(statisticsInfo.getNormalCarTotal() != null ? statisticsInfo.getNormalCarTotal().toString() : "0");
                        allCar.setText(statisticsInfo.getCarTotal() != null ? statisticsInfo.getCarTotal().toString() : "0");
                    }
                    break;
                case 100006:
                    //搜索框搜索信息
                    UIUtils.cancelProgressDialog();
                    if (msg.getData().getSerializable("carRuntimes") != null) {
                        llCarRuntimes = (List<CarRuntime>) msg.getData().getSerializable("carRuntimes");
                    }

                    if (llCarRuntimes.size() > 0) {
                        //去除重复的位置

                        drawRuntimeList = llCarRuntimes;

                        screenCarRuntime = new HashSet<>();//重置标记数据
                        mBaiduMap.clear();
                        presents = new ArrayList<>();//没有值时清空地图绘制信息
                        if (unloadingMarkerAndLin.size() > 0) {
                            //有消纳场信息
                            for (GpsfenceParameter gpsfence : unloadingGpsfences) {
                                drownPolygon(gpsfence, 1);
                            }
                        }
                        if (consappMarkerAndLin.size() > 0) {
                            //有消纳场信息
                            for (GpsfenceParameter gpsfence : consappGpsfences) {
                                drownPolygon(gpsfence, 2);
                            }
                        }
                        //添加标注
                        addBiaozhu(llCarRuntimes, 0);
                    } else {
                        screenCarRuntime = new HashSet<>();//重置标记数据
                        mBaiduMap.clear();
                        presents = new ArrayList<>();//没有值时清空地图绘制信息
                        if (unloadingMarkerAndLin.size() > 0) {
                            //有消纳场信息
                            for (GpsfenceParameter gpsfence : unloadingGpsfences) {
                                drownPolygon(gpsfence, 1);
                            }
                        }
                        if (consappMarkerAndLin.size() > 0) {
                            //有消纳场信息
                            for (GpsfenceParameter gpsfence : consappGpsfences) {
                                drownPolygon(gpsfence, 2);
                            }
                        }
                        mClusterManager.clearItems();//清除聚合数据
                    }
                    break;
                case 100007:
                    //消纳场围栏请求成功
                    //    private List<GpsfenceParameter> unloadingGpsfences = new ArrayList<>();
                    //    //工地围栏集合
                    //    private List<GpsfenceParameter> consappGpsfences = new ArrayList<>();
                    if (unloadingGpsfences.size() > 0) {
                        if (consumption_int == 1) {
                            for (int ud = 0; ud < unloadingGpsfences.size(); ud++) {
                                drownPolygon(unloadingGpsfences.get(ud), 1);
                            }
                        } else {
                            if (unloadingMarkerAndLin.size() > 0) {
                                for (int u = 0; u < unloadingMarkerAndLin.size(); u++) {
                                    unloadingMarkerAndLin.get(u).remove();
                                }
                            }
                        }
                    }
                    break;
                case 100008:
                    //工地围栏请求成功
                    if (consappGpsfences.size() > 0) {
                        if (construction_int == 1) {
                            for (int ud = 0; ud < consappGpsfences.size(); ud++) {
                                drownPolygon(consappGpsfences.get(ud), 2);
                            }
                        } else {
                            if (consappMarkerAndLin.size() > 0) {
                                for (int u = 0; u < consappMarkerAndLin.size(); u++) {
                                    consappMarkerAndLin.get(u).remove();
                                }
                            }
                        }
                    }
                    break;
                case 100009:
                    //离线车辆信息查询
                    UIUtils.cancelProgressDialog();
                    if (offlineCarRuntimes.size() > 0) {
                        allCarRuntimes.addAll(offlineCarRuntimes);
                        drawRuntimeList = allCarRuntimes;
                    }
                    dataAndPositionUpdate(0);
                    break;

            }


        }
    };

    private void setMatching() {
        MatchingAdapter adapter = new MatchingAdapter(getActivity(), cars);
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
//        headTitleImg = view.findViewById(R.id.title_img);
//        headTitleImg.setOnClickListener(this);
        mSearchImageview = view.findViewById(R.id.search_imageview);
        mSearchImageview.setOnClickListener(this);
        realTimeLicensePlate = view.findViewById(R.id.search);
        realTimeLicensePlate.setOnEditorActionListener(this);

        //当前布局控件

        realTimeBmap = view.findViewById(R.id.real_time_bmap);
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
        MapOperation.hideBaiDuIconTextureMapView(realTimeBmap);//隐藏百度图标
        mClusterManager = new ClusterManager<SwitBdItem>(getActivity(), mBaiduMap);
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
        panorama_button = view.findViewById(R.id.panorama_button);
        plate_number_button = view.findViewById(R.id.plate_number_button);
        gather_button = view.findViewById(R.id.gather_button);
        location_take_up_right = view.findViewById(R.id.location_take_up_right);//收起
        location_right_layout = view.findViewById(R.id.location_right_layout);
        location_take_up_right.setOnClickListener(this);
        panorama_button.setOnClickListener(this);
        plate_number_button.setOnClickListener(this);
        gather_button.setOnClickListener(this);
        legend_button = view.findViewById(R.id.legend_button);//图例按钮
        legend_button.setOnClickListener(this);
        traffic_button = view.findViewById(R.id.traffic_button);//路况开关
        traffic_button.setOnClickListener(this);


        location_bottom_linearLayout = view.findViewById(R.id.location_bottom_linearLayout);
        location_fragment = view.findViewById(R.id.location_fragment);

        mBottom_text = new TextView[2];
        mBottom_text[0] = view.findViewById(R.id.location_text_1);
        mBottom_text[1] = view.findViewById(R.id.location_text_4);
        mBottom_img = new ImageView[2];
        mBottom_img[0] = view.findViewById(R.id.location_bottom_1_img);
        mBottom_img[1] = view.findViewById(R.id.location_bottom_4_img);
        mBottom_view = new RelativeLayout[2];
        mBottom_view[0] = view.findViewById(R.id.location_bottom_1);
        mBottom_view[1] = view.findViewById(R.id.location_bottom_4);

        view.findViewById(R.id.location_bottom_1).setOnClickListener(this);
        view.findViewById(R.id.location_bottom_4).setOnClickListener(this);

        //中心maker
        marker_panorama_location = view.findViewById(R.id.marker_panorama_location);
        marker_panorama_location_top = view.findViewById(R.id.marker_panorama_location_top);
        maker_panorama_img = view.findViewById(R.id.maker_panorama_img);//全景小图
        maker_panorama_text = view.findViewById(R.id.maker_panorama_text);//小图下面的文字
        marker_panorama_location_top.setOnClickListener(this);

        //筛选
        malfunction_screen = view.findViewById(R.id.malfunction_screen);

        //筛选控件
        allCar = view.findViewById(R.id.allCar);
        normalCar = view.findViewById(R.id.normalCar);
        unlicensedCar = view.findViewById(R.id.unlicensedCar);
        gpsCar = view.findViewById(R.id.gpsCar);
        stateCar = view.findViewById(R.id.stateCar);
        openBoxCar = view.findViewById(R.id.openBoxCar);

        allCar_CheckBox = view.findViewById(R.id.allCar_CheckBox);
        allCar_CheckBox.setOnClickListener(this);
        normalCar_CheckBox = view.findViewById(R.id.normalCar_CheckBox);
        normalCar_CheckBox.setOnClickListener(this);
        unlicensedCar_CheckBox = view.findViewById(R.id.unlicensedCar_CheckBox);
        unlicensedCar_CheckBox.setOnClickListener(this);
        gpsCar_CheckBox = view.findViewById(R.id.gpsCar_CheckBox);
        gpsCar_CheckBox.setOnClickListener(this);
        stateCar_CheckBox = view.findViewById(R.id.stateCar_CheckBox);
        stateCar_CheckBox.setOnClickListener(this);
        openBoxCar_CheckBox = view.findViewById(R.id.openBoxCar_CheckBox);
        openBoxCar_CheckBox.setOnClickListener(this);


        //新加选框
        consumption_CheckBox = view.findViewById(R.id.consumption_CheckBox);//消纳场
        consumption_CheckBox.setOnClickListener(this);
        consumption_number = view.findViewById(R.id.consumption_number);//消纳场数量
        construction_CheckBox = view.findViewById(R.id.construction_CheckBox);//工地
        construction_CheckBox.setOnClickListener(this);
        construction_number = view.findViewById(R.id.construction_number);//工地数量
        //选框中收起按钮
        pack_up_option = view.findViewById(R.id.pack_up_option);//收起按钮
        pack_up_option.setOnClickListener(this);

        //选项卡
        option_box = view.findViewById(R.id.option_box);

        //工具栏中的统计面板按钮
        statistics_button = view.findViewById(R.id.statistics_button);
        statistics_button.setOnClickListener(this);


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
            getActivity().finish();

        } else if (i == R.id.search_imageview) {//搜索处理
//            hideKeyboard();
            search(0);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiSouSuo");

        } else if (i == R.id.location_take_up_right) {//收起按钮
            if (location_take_up_right_id == 1) {
                location_take_up_right.setSelected(true);
                location_right_layout.setVisibility(View.INVISIBLE);
                location_take_up_right_id = 2;
            } else {
                MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiShouQi");
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
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiQuanJin");

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
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiLuKuang");

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
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiChePai");

        } else if (i == R.id.gather_button) {//聚集数开关
            if (drawRuntimeList.size() > 1000) {
                UIUtils.showToast(getActivity(), "数量过多，无法关闭聚集数");
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
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiJuJiShu");

        } else if (i == R.id.legend_button) {
            openPop();
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiTuLi");

        } else if (i == R.id.marker_panorama_location_top) {//全景地图图标点击target
            Intent intent = new Intent(getActivity(), PanoramaActivity.class);
            intent.putExtra("longitude", target.longitude);
            intent.putExtra("latitude", target.latitude);
            startActivity(intent);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiDaKaiQuanJin");


            //点击选项卡
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

        } else if (i == R.id.allCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (allCar_int == 0) {
                allCar_CheckBox.setSelected(true);
                allCar_int = 1;
            } else {
                allCar_CheckBox.setSelected(false);
                allCar_int = 0;
            }
            if (offlineCarRuntimes.size() > 0) {
                dataAndPositionUpdate(0);
            } else {
                UIUtils.showLoadingProgressDialog(getActivity(), R.string.loading_process_tip, false);
                //请求离线车辆信息
                getOffLineData();
            }
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiSuoYouCheLiang");

        } else if (i == R.id.normalCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (normalCar_int == 0) {
                normalCar_CheckBox.setSelected(true);
                normalCar_int = 1;
            } else {
                normalCar_CheckBox.setSelected(false);
                normalCar_int = 0;
            }
            dataAndPositionUpdate(0);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiZhenChang");

        } else if (i == R.id.unlicensedCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (unlicensedCar_int == 0) {
                unlicensedCar_CheckBox.setSelected(true);
                unlicensedCar_int = 1;
            } else {
                unlicensedCar_CheckBox.setSelected(false);
                unlicensedCar_int = 0;
            }
            dataAndPositionUpdate(0);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiWuZheng");

        } else if (i == R.id.gpsCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (gpsCar_int == 0) {
                gpsCar_CheckBox.setSelected(true);
                gpsCar_int = 1;
            } else {
                gpsCar_CheckBox.setSelected(false);
                gpsCar_int = 0;
            }
            dataAndPositionUpdate(0);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiDingWeiGuZhang");

        } else if (i == R.id.stateCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (stateCar_int == 0) {
                stateCar_CheckBox.setSelected(true);
                stateCar_int = 1;
            } else {
                stateCar_CheckBox.setSelected(false);
                stateCar_int = 0;
            }
            dataAndPositionUpdate(0);
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiShiLian");

        } else if (i == R.id.openBoxCar_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (openBoxCar_int == 0) {
                openBoxCar_CheckBox.setSelected(true);
                openBoxCar_int = 1;
            } else {
                openBoxCar_CheckBox.setSelected(false);
                openBoxCar_int = 0;
            }
            dataAndPositionUpdate(0);

        } else if (i == R.id.consumption_CheckBox) {
            llCarRuntimes = new ArrayList<>();
//                int consumption_int=0;
//                int construction_int=0;
            if (consumption_int == 0) {
                consumption_CheckBox.setSelected(true);
                consumption_int = 1;
            } else {
                consumption_CheckBox.setSelected(false);
                consumption_int = 0;
            }
            consumptionRequest();//消纳场请求
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiXiaoNaChang");

        } else if (i == R.id.construction_CheckBox) {
            llCarRuntimes = new ArrayList<>();
            if (construction_int == 0) {
                construction_CheckBox.setSelected(true);
                construction_int = 1;
            } else {
                construction_CheckBox.setSelected(false);
                construction_int = 0;
            }
            cosappRequest();
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiGongDi");

        } else if (i == R.id.pack_up_option) {//选择板收起
            //隐藏掉统计框
            malfunction_screen.setVisibility(View.GONE);
            //显示右边工具栏
            option_box.setVisibility(View.VISIBLE);
            location_take_up_right.setSelected(false);
            location_right_layout.setVisibility(View.VISIBLE);
            location_take_up_right_id = 1;

        } else if (i == R.id.statistics_button) {//工具栏中统计面板按钮
            //显示统计框
            malfunction_screen.setVisibility(View.GONE);
            //隐藏右边工具栏
            option_box.setVisibility(View.GONE);
            location_take_up_right.setSelected(true);
            location_right_layout.setVisibility(View.INVISIBLE);
            location_take_up_right_id = 2;
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiXuanZhe");

        } else if (i == R.id.right_bt) {//标题右边加号按钮
            //显示统计框
            malfunction_screen.setVisibility(View.GONE);
            //隐藏右边工具栏
            option_box.setVisibility(View.GONE);
            location_take_up_right.setSelected(true);
            location_right_layout.setVisibility(View.INVISIBLE);
            location_take_up_right_id = 2;
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNShiShiWeiZhiXuanZhe");

        }
    }

    //请求离线车辆数据
    private void getOffLineData() {
        IRealTimePosition realTimePosition = RealTimePosition.LonginManager(9);
        realTimePosition.getOfflineCarRuntime(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100009;
                message.obj = msg;
                if (state == 0) {
                    //操作成功
                    if (body != null) {
                        offlineCarRuntimes = (List<CarRuntime>) body;
                    }
                } else {
                    message.what = 1;
                }
                carRuntimeHandler.sendMessage(message);

            }
        });
    }

    //消纳场围栏请求
    private void consumptionRequest() {
        IGpsFenceManager gpsFenceManager = GpsFenceManager.gpsFenceManager(1);
        gpsFenceManager.getUnloadingGpsfence("", new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100007;
                message.obj = msg;
                if (state == 0) {
                    //操作成功
                    if (body != null) {
                        unloadingGpsfences = (List<GpsfenceParameter>) body;
//                        private List<GpsfenceParameter> consappGpsfences = new ArrayList<>();
                    }
                } else {
                    message.what = 1;
                }
                carRuntimeHandler.sendMessage(message);

            }
        });
    }

    //工地围栏请求
    private void cosappRequest() {
        IGpsFenceManager gpsFenceManager = GpsFenceManager.gpsFenceManager(2);
        gpsFenceManager.getUnloadingGpsfence("", new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                message.what = 100008;
                message.obj = msg;
                if (state == 0) {
                    //操作成功
                    if (body != null) {
                        consappGpsfences = (List<GpsfenceParameter>) body;
                    }
                } else {
                    message.what = 1;
                }
                carRuntimeHandler.sendMessage(message);

            }
        });
    }

    //展示全景小图
    public static void displayPanoramic(LatLng latLng) {
        LocationFragment.maker_panorama_text.setText("正在加载..");

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
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
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
    public void search(final Integer statex) {
        //搜索处理
        String searchString = realTimeLicensePlate.getText().toString();
        if ("".equals(searchString)) {
            //数据请求
            UIUtils.showToast(getActivity(), "请输入车牌号");
        } else {
            UIUtils.showLoadingProgressDialog(getActivity(), R.string.loading_process_tip, false);
            IRealTimePosition realTimePosition = RealTimePosition.LonginManager(2);
            realTimePosition.carRuntimeByCarNumber(searchString, new OnNetResultListener() {
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
//                IToast.show(this, "当前帐号下没有车辆");
//            }
        }
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


        drawThreadPool.execute(new Runnable() {
            @Override
            public void run() {
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
        });


    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            search(0);
            return true;
        }
        return false;
    }


    @Override
    public void onDestroy() {

        //由于地图监听冲突，过多使用静态变量，所以在界面销毁时要初始化变量值，不然会导致程序崩溃
        carRuntimeList = null;
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

        drawThreadPool = null;

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

        if (location_fragment != null) {
            location_fragment.setVisibility(View.GONE);
            location_bottom_linearLayout.setVisibility(View.GONE);
        }
        //周边信息还有一个。。
        if (PeripheryViolationFragment.location_fragment != null) {
            PeripheryViolationFragment.location_fragment.setVisibility(View.GONE);
            PeripheryViolationFragment.location_bottom_linearLayout.setVisibility(View.GONE);
        }
    }

    private void openFragment() {
        // fragment管理器初始化
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
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
        location_bottom_linearLayout.setVisibility(View.VISIBLE);


    }


    //弹出遮罩层
    public void openPop() {

        //图例按钮
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.jn_zwt_layout_legend, null);
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


        View popView = LayoutInflater.from(getActivity()).inflate(
                R.layout.jn_zwt_layout_legend, null);
        View rootView = view.findViewById(R.id.activity_location_id); // 當前頁面的根佈局
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
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    //绘制maker图片
    private View drawMakerImg(CarRuntime carRuntime) {
        markerItem = View.inflate(getActivity(), R.layout.jn_zwt_marker_item, null);
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
        Bitmap ix = BitmapFactory.decodeResource(LocationFragment.this.getResources(), R.drawable.jn_zwt_car_bimg);
        if ("0".equals(carRuntime.getOnlineState())) {
            ix = BitmapFactory.decodeResource(LocationFragment.this.getResources(), R.drawable.jn_zwt_car_bimg_off);
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
                UIUtils.showToast(getActivity(), "数据过多自动开启聚合");
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
                        Bitmap ix = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), R.drawable.jn_zwt_car_bimg);
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

                            drawThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
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
                            });

                        }
                    }
                }
            }


            if (gather_button_id == 2 || carRuntimes.size() > 300) {

                drawThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
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
                });

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

    //数据更新加位置调整
    private void dataAndPositionUpdate(Integer zxd) {
        //清空地图
        carRuntimeList = new ArrayList<CarRuntime>();
        if (llCarRuntimes.size() > 0) {//搜索框搜出来数据了
            carRuntimeList = llCarRuntimes;
        } else if (allCar_int == 1) {
            carRuntimeList.addAll(allCarRuntimes);
        } else {
            if (normalCar_int == 1) {
                carRuntimeList.addAll(normalCarRuntimes);
            }
            if (unlicensedCar_int == 1) {
                carRuntimeList.addAll(unlicensedCarRuntimes);
            }
            if (gpsCar_int == 1) {
                carRuntimeList.addAll(gpsCarRuntimes);
            }
            if (stateCar_int == 1) {
                carRuntimeList.addAll(stateCarRuntimes);
            }
            if (openBoxCar_int == 1) {
                carRuntimeList.addAll(openBoxCarRuntimes);
            }
        }

        if (carRuntimeList.size() > 0) {
            //去除重复的位置
            carRuntimeList = TransformationUtil.removeDuplicate(carRuntimeList);

            screenCarRuntime = new HashSet<>();//重置地图标注数据

            mBaiduMap.clear();
            if (unloadingMarkerAndLin.size() > 0) {
                //有消纳场信息
                for (GpsfenceParameter gpsfence : unloadingGpsfences) {
                    drownPolygon(gpsfence, 1);
                }
            }
            if (consappMarkerAndLin.size() > 0) {
                //有消纳场信息
                for (GpsfenceParameter gpsfence : consappGpsfences) {
                    drownPolygon(gpsfence, 2);
                }
            }
            //添加标注
            addBiaozhu(carRuntimeList, zxd);
        } else {
            mBaiduMap.clear();
            if (unloadingMarkerAndLin.size() > 0) {
                //有消纳场信息
                for (GpsfenceParameter gpsfence : unloadingGpsfences) {
                    drownPolygon(gpsfence, 1);
                }
            }
            if (consappMarkerAndLin.size() > 0) {
                //有消纳场信息
                for (GpsfenceParameter gpsfence : consappGpsfences) {
                    drownPolygon(gpsfence, 2);
                }
            }
            presents = new ArrayList<>();//没有值时清空地图绘制信息
            mClusterManager.clearItems();//清除聚合数据
        }

    }

    //计算车辆状态数量
    private void calculationCount(List<CarRuntime> carRuntimes) {

        //车辆实时数据初始化
        normalCarRuntimes = new ArrayList<CarRuntime>();//正常车辆
        unlicensedCarRuntimes = new ArrayList<CarRuntime>();//无证车辆
        gpsCarRuntimes = new ArrayList<CarRuntime>();//gps故障车辆
        stateCarRuntimes = new ArrayList<CarRuntime>();//状态失联
        openBoxCarRuntimes = new ArrayList<CarRuntime>();//开厢行驶
        if (carRuntimes.size() > 0) {
            normalCarRuntimes.addAll(onlineCarRuntimes);
            for (int i = 0; i < carRuntimes.size(); i++) {
                CarRuntime carRuntime = carRuntimes.get(i);
                String carState = carRuntime.getCarState();
                boolean car16StateNormal = true;//这个是平台抄下来的状态，我也不知道是什么意思，判断开厢的时候要用

//                if ("0".equals(carRuntime.getOnlineState()))//车辆在线
//                    normalCarRuntimes.add(carRuntime);
                if (carState.substring(carState.length() - 5, carState.length() - 4).equals("1") && carRuntime.getOnlineState() == "1")//无证车辆
                {
                    unlicensedCarRuntimes.add(carRuntime);
                    car16StateNormal = false;
                }
                if ((carState.substring(carState.length() - 7, carState.length() - 6).equals("1") && carRuntime.getOnlineState().equals("1"))
                        || ((carRuntime.getGpsPosY() < 1 || carRuntime.getGpsPosX() < 1) && carRuntime.getOnlineState().equals("1")))//gps故障
                {
                    gpsCarRuntimes.add(carRuntime);
                    car16StateNormal = false;
                }
                if (carState.substring(carState.length() - 11, carState.length() - 10).equals("1") && carRuntime.getOnlineState().equals("1")
                        || carState.substring(carState.length() - 12, carState.length() - 11).equals("1") && carRuntime.getOnlineState().equals("1")
                        || carState.substring(carState.length() - 14, carState.length() - 13).equals("1") && carRuntime.getOnlineState().equals("1"))//状态失联
                {
                    stateCarRuntimes.add(carRuntime);
                    car16StateNormal = false;
                }
                if ((carRuntime.getBoxClose() == 2 && car16StateNormal && carRuntime.getGpsSpeed() > 20) && carRuntime.getOnlineState().equals("1"))//开厢行驶
                    openBoxCarRuntimes.add(carRuntime);
            }
        }

//        allCar.setText(carRuntimes.size() + "");
//        normalCar.setText(normalCarRuntimes.size() + "");
        unlicensedCar.setText(unlicensedCarRuntimes.size() + "");
        gpsCar.setText(gpsCarRuntimes.size() + "");
        stateCar.setText(stateCarRuntimes.size() + "");
        openBoxCar.setText(openBoxCarRuntimes.size() + "");
    }

    //绘制多边形,围栏加类型(1.消纳场2.工地)
    private void drownPolygon(GpsfenceParameter gpsfence, Integer type) {
        String str = gpsfence.getFencePos();
        List<CoordinatesBean> CoordinatesBean = new ArrayList<CoordinatesBean>();
        if (str == null || str.equals("")) {
            return;
        }
        String[] strs = str.split(";");
        for (int i = 0; i < strs.length; i++) {
            String[] strts;
            strts = strs[i].split(",");
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setX(Double.valueOf(strts[0]).doubleValue());
            coordinatesBean.setY(Double.valueOf(strts[1]).doubleValue());
            CoordinatesBean.add(coordinatesBean);
        }
        Log.e("mCoordinatesBean", "---");
        if (type == 1) {
            unloadingMarkerAndLin.add(MapOperation.addCustomElementsDemoReturn(mBaiduMap, CoordinatesBean));
        } else {
            consappMarkerAndLin.add(MapOperation.addCustomElementsDemoReturn(mBaiduMap, CoordinatesBean));
        }
        CoordinatesBean c = MapOperation.getCenterPoint(CoordinatesBean);
        MapOperation.setCenterPosition(mBaiduMap, new LatLng(c.getY(), c.getX()));
        addImageView(gpsfence, c, type);
    }

    /**
     * 描绘点
     */
    public void addImageView(GpsfenceParameter gpsfence, CoordinatesBean coordinatesBean, int icon) {
        LatLng point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(coordinatesBean.getY(), coordinatesBean.getX()));
        BitmapDescriptor bitmap;
        //构建Marker图标
        if (icon == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.jn_zwt_xiao_img);
            //构建MarkerOption，用于在地图上添加Marker

            Bundle bundle = new Bundle();

            bundle.putSerializable("gpsfence", gpsfence);
            OverlayOptions option = new MarkerOptions()
                    .position(point).anchor(0.5f, 0.5f).icon(bitmap);

            //在地图上添加Marker，并显示
            unloadingMarkerAndLin.add(mBaiduMap.addOverlay(option));
            unloadingMarkerAndLin.get(unloadingMarkerAndLin.size() - 1).setExtraInfo(bundle);
        } else {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.jn_zwt_gong_img);
            //构建MarkerOption，用于在地图上添加Marker

            Bundle bundle = new Bundle();

            bundle.putSerializable("gpsfence", gpsfence);
            OverlayOptions option = new MarkerOptions()
                    .position(point).anchor(0.5f, 0.5f).icon(bitmap);

            //在地图上添加Marker，并显示
            consappMarkerAndLin.add(mBaiduMap.addOverlay(option));
            consappMarkerAndLin.get(consappMarkerAndLin.size() - 1).setExtraInfo(bundle);
        }

    }


    public static LocationFragment newInstance(String carNumber) {

        LocationFragment fragment = new LocationFragment();
        if (carNumber != null && !TextUtils.isEmpty(carNumber)) {
            Bundle args = new Bundle();
            args.putString("carNumber", carNumber);
            fragment.setArguments(args);
        }
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        realTimeBmap.setVisibility(View.VISIBLE);
        realTimeBmap.onResume();
        MobclickAgent.onPageStart("JNShiShiWeiZhi"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        realTimeBmap.setVisibility(View.INVISIBLE);
        realTimeBmap.onPause();
        MobclickAgent.onPageEnd("JNShiShiWeiZhi");
    }

}
