package com.zt.capacity.baidumap.utils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.zt.capacity.common.util.GeoHasher;
import com.zt.capacity.common.util.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 数据转换
 * Created by Administrator on 2018/5/2.
 */

public class TransformationUtil {
    /**
     * 说明： 此处的WGS-84地图 即世界地图,代表真实的地理位置 此处的Google地图 即火星地图,代表国测局加密偏移过的地理位置
     * 此处的BD-09地图 即百度地图,代表百度二次加密偏移过的地理位置
     **/
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double pi = 3.14159265358979324;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    //未定位车辆位置坐标
    private static double noPositionX = 116.6;
    private static double noPositionY = 36.7;

    /**
     * GPS坐标转百度坐标
     *
     * @param sourceLatLng
     */
    public static LatLng gpsToBaiduCoordinate(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        return converter.convert();
    }


    /**
     * 比较选出集合中最大经纬度
     */
    public static float getMax(List<LatLng> latLngsList) {
        List<Double> latitudeList = new ArrayList<Double>();
        List<Double> longitudeList = new ArrayList<Double>();
        for (int i = 0; i < latLngsList.size(); i++) {
            double latitude = latLngsList.get(i).latitude;
            double longitude = latLngsList.get(i).longitude;
            latitudeList.add(latitude);
            longitudeList.add(longitude);
        }
        double maxLatitude;
        double minLatitude;
        double maxLongitude;
        double minLongitude;
        maxLatitude = Collections.max(latitudeList);
        minLatitude = Collections.min(latitudeList);
        maxLongitude = Collections.max(longitudeList);
        minLongitude = Collections.min(longitudeList);
        //计算两个点的距离
        double distance = GeoHasher.GetDistance(maxLatitude, maxLongitude, minLatitude, minLongitude);
        return getLevel(distance);
    }


    /**
     * 根据距离判断地图级别(米)
     */
    public static float getLevel(Double distance) {
        float level = 12;
        int zoom[] = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 1000, 2000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000};
        for (int i = 0; i < zoom.length; i++) {
            int zoomNow = zoom[i];
            if (zoomNow - distance * 1000 > 0) {
                level = 18 - i + 4;
                break;
            }
        }
        return level;
    }

    //list去重
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    /**
     * bd_lat 纬度 bd_lon 经度 百度的转换到WGS-84
     **/
    public static Point bd_wgs_encrypts(double bd_lat, double bd_lon) {
        Point point = bd_google_encrypt(bd_lat, bd_lon);
        Point pointResult = google_wgs_encrypts(point.getLat(), point.getLng());
        return pointResult;
    }

    /**
     * wgLat 纬度 wgLon 经度 google的转换到WGS-84（即 GPS加偏）
     **/
    public static Point google_wgs_encrypts(double lat, double lon) {
        Point point = transform(lat, lon);
        double lontitude = lon * 2 - point.getLng();
        double latitude = lat * 2 - point.getLat();
        return new Point(lontitude, latitude);
    }

    public static Point transform(double lat, double lon) {
        Point point = new Point();
        if (outOfChina(lat, lon)) {
            point.setLat(lat);
            point.setLng(lon);
            return point;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        point.setLat(mgLat);
        point.setLng(mgLon);
        return point;
    }

    /**
     * wgLat 纬度 wgLon 经度 百度转google
     */
    public static Point bd_google_encrypt(double bd_lat, double bd_lon) {
        Point point = new Point();
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        point.setLat(gg_lat);
        point.setLng(gg_lon);
        return point;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static LatLng baiduToGps(LatLng latLng) {
        //百度点保存成为gps点
        double baiduX = latLng.longitude;
        double baiduY = latLng.latitude;
        Point point = CoordinateConversion.bd_wgs_encrypts(baiduY, baiduX);
        double gpsX = Double.valueOf(point.getLng());
        double gpsY = Double.valueOf(point.getLat());
        return new LatLng(gpsY,gpsX);
    }

    //    jn获取随机坐标
    public static LatLng stochasticLatlon(){
        double x= noPositionX+(Math.random()/1000);
        double y= noPositionY+(Math.random()/1000);
        return new LatLng(y,x);
    }

    //百度地图判断坐标是否在屏幕范围内
    public static boolean isScreen(BaiduMap mBaiduMap, LatLng latLng){

        android.graphics.Point pt= mBaiduMap.getMapStatus().targetScreen;//获取屏幕中心坐标

        android.graphics.Point point= mBaiduMap.getProjection().toScreenLocation(latLng);//将地图坐标转换成屏幕坐标
        if(point.x < 0 || point.x > pt.x*2 || point.y < 0 || point.y > pt.y*2)
        {
            return false;
        }else{
            return true;
        }

    }
}
