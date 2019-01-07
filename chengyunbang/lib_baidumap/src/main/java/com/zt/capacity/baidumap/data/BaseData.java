package com.zt.capacity.baidumap.data;

import com.baidu.mapapi.model.LatLng;

public class BaseData {

    //位置
    private static LatLng point=new LatLng(28.118273, 119.969600);

    public static LatLng getPoint() {
        return point;
    }

    public static void setPoint(LatLng point) {
        BaseData.point = point;
    }
}
