package com.zt.capacity.jinan_zwt.fragment;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.common.util.map.MapOperation;
import com.zt.capacity.common.util.map.TransformationUtil;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.RichScanCarActivity;
import com.zt.capacity.jinan_zwt.adapter.DoubtfulAdapter;
import com.zt.capacity.jinan_zwt.adapter.PeriphryAdapter;
import com.zt.capacity.jinan_zwt.bean.CarHistoryBean;
import com.zt.capacity.jinan_zwt.bean.CarRuntime;
import com.zt.capacity.jinan_zwt.request.periphery.IPeripheryManager;
import com.zt.capacity.jinan_zwt.request.periphery.PeripheryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 周边违规
 */
public class PeripheryViolationFragment extends BaseFragment implements View.OnClickListener {
    private View view;

    private TextureMapView my_map;
    private BaiduMap mBaiduMap;

    //数据
    private List<CarRuntime> carRuntimes = new ArrayList<>();

    private LinearLayout one_km_button;//一公里
    private TextView one_km_text;//一公里文字
    private LinearLayout two_km_button;//二公里
    private TextView two_km_text;//二公里文字
    private LinearLayout three_km_button;//三公里
    private TextView three_km_text;//三公里文字

    private Marker marker;//当前显示的marker


    private List<Marker> markers=new ArrayList<>();//当前显示的所有marker


    int raidus = 1;//公里


    public static LinearLayout location_bottom_linearLayout;
    public static LinearLayout location_fragment;
    protected static RelativeLayout[] mBottom_view;// 底部菜单
    protected static TextView[] mBottom_text;// 底部菜单文字
    protected static ImageView[] mBottom_img;// 底部菜单小横杆
    protected static int position = 0;// 记录当前底部选择位置
    protected Fragment mContent;
    private Fragment fragmentLocation1;
    private Fragment fragmentLocation2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_activity_periphery, null);

        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        init();


        MapOperation.drawCircular(mBaiduMap, Web.getPoint(), (int) Math.ceil(raidus * 1000));

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(Web.getPoint());
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //设置比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                new MapStatus.Builder().zoom(16).build()));

        return view;
    }


    //初始化操作
    private void init() {
        //地图
        my_map = view.findViewById(R.id.my_map);
        mBaiduMap = my_map.getMap();
//        mBaiduMap.setOnMapClickListener(this);
//        mBaiduMap.setOnMarkerClickListener(this);//marker点击事件
        //去掉百度logo
        MapOperation.hideBaiDuIconTextureMapView(my_map);

        one_km_button = view.findViewById(R.id.one_km_button);//一公里
        one_km_button.setOnClickListener(this);
        one_km_text = view.findViewById(R.id.one_km_text);//一公里文字
        two_km_button = view.findViewById(R.id.two_km_button);//二公里
        two_km_button.setOnClickListener(this);
        two_km_text = view.findViewById(R.id.two_km_text);//二公里文字
        three_km_button = view.findViewById(R.id.three_km_button);//三公里
        three_km_button.setOnClickListener(this);
        three_km_text = view.findViewById(R.id.three_km_text);//三公里文字


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



        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                CarRuntime carRuntime = (CarRuntime) marker.getExtraInfo().get("carRuntime");
//                if (carRuntime != null) {
//                    ActivityJumpUtil.jumpActivityByString(getActivity(), RichScanCarActivity.class, carRuntime.getCarNumber(), "carNumber");
//                }

                bundleCarRuntime = marker.getExtraInfo();
                if (bundleCarRuntime != null) {
                    //点击无数据的时候直接返回
                    openFragment();
                }
                return true;
            }
        });
    }

    private Bundle bundleCarRuntime;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one_km_button://一公里
                MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhouBianWeiGuiYiGongLi");
                if (raidus == 1) {
                    return;
                }

                //请求违规数据
                raidus = 1;
                mBaiduMap.clear();
                changeB();

                getPerpheryData(1);

                break;
            case R.id.two_km_button://三公里
                MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhouBianWeiGuiSanGongLi");
                if (raidus == 3) {
                    return;
                }
                //请求违规数据
                raidus = 3;
                mBaiduMap.clear();
                changeB();
                getPerpheryData(3);
                break;
            case R.id.three_km_button://五公里
                MobclickAgent.onEvent(BaseApplication.getContext(), "JNZWTZhouBianWeiGuiWuGongLi");
                if (raidus == 5) {
                    return;
                }
                //请求违规数据
                raidus = 5;
                mBaiduMap.clear();
                changeB();
                getPerpheryData(raidus);
                break;
            case R.id.location_bottom_1://基础信息
                if (position == 0) {
                    return;
                }
                fragmentLocation1 = new LocationFBasicsFragment();
                fragmentLocation1.setArguments(bundleCarRuntime);
                position = 0;
                changeBottom(position);
                switchContent(fragmentLocation1);

            case R.id.location_bottom_4://周边车辆
                if (position == 1) {
                    return;
                }
                fragmentLocation2 = new LocationFPeripheryFragment();
                fragmentLocation2.setArguments(bundleCarRuntime);
                position = 1;
                changeBottom(position);
                switchContent(fragmentLocation2);

        }
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


    // 底部导航栏选择切换
    public static void changeBottom(int i) {
        Log.e("changeBottom",""+i);
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

    private void changeB(){
        MapOperation.drawCircular(mBaiduMap, Web.getPoint(), (int) Math.ceil(raidus * 1000));
        if(raidus == 1){

            one_km_button.setBackground(getResources().getDrawable(R.color.color_1AAD19));
            one_km_text.setSelected(false);
            two_km_button.setBackground(getResources().getDrawable(R.color.white));
            two_km_text.setSelected(true);
            three_km_button.setBackground(getResources().getDrawable(R.color.white));
            three_km_text.setSelected(true);
        }
        if(raidus == 3){

            one_km_button.setBackground(getResources().getDrawable(R.color.white));
            one_km_text.setSelected(true);
            two_km_button.setBackground(getResources().getDrawable(R.color.color_1AAD19));
            two_km_text.setSelected(false);
            three_km_button.setBackground(getResources().getDrawable(R.color.white));
            three_km_text.setSelected(true);
        }
        if(raidus == 5){

            one_km_button.setBackground(getResources().getDrawable(R.color.white));
            one_km_text.setSelected(true);
            two_km_button.setBackground(getResources().getDrawable(R.color.white));
            two_km_text.setSelected(true);
            three_km_button.setBackground(getResources().getDrawable(R.color.color_1AAD19));
            three_km_text.setSelected(false);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //数据请求成功
                    UIUtils.cancelProgressDialog();
                    cGridView();
                    break;
                case 1:
                    //数据请求失败
                    UIUtils.cancelProgressDialog();
                    break;
            }
        }
    };

    //请求违规数据
    private void getPerpheryData(Integer km) {
        UIUtils.showLoadingProgressDialog(getActivity(), R.string.loading_process_tip, false);
        IPeripheryManager peripheryManager = PeripheryManager.getInterface(1);
//        peripheryManager.peripheryData(km, TransformationUtil.baiduToGps(Web.getPoint()), new OnNetResultListener() {
        peripheryManager.peripheryData(km, Web.getPoint(), new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    message.what = state;
                    if (body != null) {
                        carRuntimes = (List<CarRuntime>) body;
                    }
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });
    }

    //创建底部展示面板
    private void cGridView() {
        if (carRuntimes.size() > 0) {
            for (int i = 0; i < carRuntimes.size(); i++) {
                //绘制车辆marker
                CarRuntime carRuntime = carRuntimes.get(i);
                addCarMarker(carRuntime);
            }
        }
    }

    //在地图上添加车辆
    public void addCarMarker(CarRuntime carRuntime) {
        //处理坐标偏
//        LatLng point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carHistory.getGpsPosY(), carHistory.getGpsPosX()));
        LatLng point = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX()));

//        //计算缩放比例
//        float rai = TransformationUtil.getLevel(raidus);
        //设置中心位置
//        MapOperation.setCenterPositionAndZoom(mBaiduMap, point, rai);
        MapOperation.setCenterPositionAndZoom(mBaiduMap, point, 15);

        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.jn_zwt_car_bimg);

        //marker数据储存
        Bundle bundle = new Bundle();
        bundle.putSerializable("carRuntime", carRuntime);
        marker = MapOperation.addMarkerByRotate(mBaiduMap, point, bundle, bitmap, carRuntime.getGpsDirect());
        markers.add(marker);//将marker添加至所有记录中
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTZhouBianWeiGui"); //统计页面，"MainScreen"为页面名称，可自定义
        mBaiduMap.clear();
        changeB();
        getPerpheryData(raidus);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTZhouBianWeiGui");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        location_bottom_linearLayout=null;
        location_fragment=null;
        RelativeLayout[] mBottom_view=null;// 底部菜单
        TextView[] mBottom_text=null;// 底部菜单文字
        ImageView[] mBottom_img=null;// 底部菜单小横杆
        position = 0;// 记录当前底部选择位置
    }
}
