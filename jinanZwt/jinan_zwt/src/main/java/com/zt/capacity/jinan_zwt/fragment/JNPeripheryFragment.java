package com.zt.capacity.jinan_zwt.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseFragment;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.map.TransformationUtil;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.LocationActivity;
import com.zt.capacity.jinan_zwt.bean.BaseBaiduReturn;
import com.zt.capacity.jinan_zwt.bean.CarRuntime;
import com.zt.capacity.jinan_zwt.bean.GeocoderBean;
import com.zt.capacity.jinan_zwt.bean.PoiBean;
import com.zt.capacity.jinan_zwt.request.baidmap.BaiDu;
import com.zt.capacity.jinan_zwt.request.baidmap.IBaiDu;

import java.util.ArrayList;
import java.util.List;


/**
 * 周边信息
 */
public class JNPeripheryFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private CarRuntime carRuntime = null;
    private ListView location_fragment_4_list;
    //xx键
    private ImageView location_fragment_close_img;




    //周边信息
    PoiSearch mPoiSearch;
    private List<PoiBean> pois;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_fragment_periphery, null);
        view.setOnTouchListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("carRuntime")) {
            carRuntime = (CarRuntime) bundle.getSerializable("carRuntime");
            initFaction();
        }

        init();

        //周边信息
//        periphery();

        return view;
    }

    private void initFaction() {

        //周边信息
        geocoderDate(TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX())));
    }

    //poi检索
//    private void periphery() {
//        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//
//        LatLng center;
//        if (carRuntime == null) {
//            center = Web.getPoint();
//        } else {
//            center = TransformationUtil.gpsToBaiduCoordinate(new LatLng(carRuntime.getGpsPosY(), carRuntime.getGpsPosX()));
//        }
////        LatLng center = new LatLng(39.92235, 116.380338);
//        int radius = 500;
//
//        mPoiSearch.searchNearby(new PoiNearbySearchOption()
//                .keyword("政府服务")
//                .sortType(PoiSortType.distance_from_near_to_far)
//                .location(center)
//                .radius(radius)
//                .pageNum(0));
//
//    }

    private void init() {
        //xx键
        location_fragment_close_img = view.findViewById(R.id.location_fragment_close_img);
        location_fragment_close_img.setOnClickListener(this);


        location_fragment_4_list = view.findViewById(R.id.location_fragment_4_list);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.location_fragment_close_img) {//                mPoiSearch.destroy();
            LocationActivity.close();

        }
    }

//    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
//
//        public void onGetPoiResult(PoiResult result) {
//            //获取POI检索结果
//            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//                Toast.makeText(getContext(), "未找到结果", Toast.LENGTH_LONG)
//                        .show();
//                return;
//            }
//            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//                List<String> listDates = new ArrayList<String>();
//                List<PoiInfo> poiInfos = result.getAllPoi();
//                for (Integer i = 0; i < poiInfos.size(); i++) {
//                    String listDate = poiInfos.get(i).name + "   政府服务   地址：" + poiInfos.get(i).address;
//                    if (i > 3) {
//                        listDates.add("......");
//                        break;
//                    }
//                    listDates.add(listDate);
//                }
//                ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.array_adapter, listDates);
//                location_fragment_4_list.setAdapter(adapter);
//
//
//            }
//            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
//
//                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//                String strInfo = "在";
//                for (CityInfo cityInfo : result.getSuggestCityList()) {
//                    strInfo += cityInfo.city;
//                    strInfo += ",";
//                }
//                strInfo += "找到结果";
//                Toast.makeText(getContext(), strInfo, Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
//
//        public void onGetPoiDetailResult(PoiDetailResult result) {
//            //获取Place详情页检索结果
//            Log.e("检索结果222222", result.toString());
//        }
//
//        @Override
//        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//            Log.e("检索结果333333", poiIndoorResult.toString());
//
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放检索实例
//        mPoiSearch.destroy();
    }


    Handler carHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 2:
                    //逆地址解析请求成功数据有误
                    Log.e("ddddddd", "数据解析有误");
                    break;

                case 100001:
                    //逆地址解析成功无误
                    BaseBaiduReturn baseBaiduReturn = (BaseBaiduReturn) msg.getData().getSerializable("baseBaiduReturn");
                    GeocoderBean geocoderBean = baseBaiduReturn.getResult();
                    pois=geocoderBean.getPois();

                    List<String> listDates = new ArrayList<String>();
                    if(pois.size()>0){
                        for (Integer i = 0; i < pois.size(); i++) {
                            String listDate = pois.get(i).getName() + "   "+pois.get(i).getPoiType()+"   地址：" + pois.get(i).getAddr();
                            if (i > 3) {
                                listDates.add("......");
                                break;
                            }
                            listDates.add(listDate);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.jn_zwt_array_adapter, listDates);
                        location_fragment_4_list.setAdapter(adapter);
                    }
            }
        }
    };

    //逆地址解析
    private void geocoderDate(LatLng latLng) {
        IBaiDu baiDu = BaiDu.getInterface(2);
        String location = latLng.latitude + "," + latLng.longitude;
        baiDu.geocoder(location, 1, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Log.e("fragment1xxxx", state + "");
                Message message = new Message();
                if (state == 0) {
                    Bundle bundle = new Bundle();
                    BaseBaiduReturn baseBaiduReturn = (BaseBaiduReturn) body;
                    if (baseBaiduReturn.getStatus() == 0) {
                        message.what = 100001;
                    } else {
                        message.what = 2;
                    }
                    bundle.putSerializable("baseBaiduReturn", baseBaiduReturn);
                    message.setData(bundle);
                } else {
                    message.what = state;
                }
                carHandler.sendMessage(message);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNShiShiWeiZhiZhouBian"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNShiShiWeiZhiZhouBian");
    }
}
