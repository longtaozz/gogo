package com.zt.capacity.common.util;

/**
 * 地图坐标转换 google,baidu,gps
 * 
 * 
 * */
public class CoordinateConversion {
	/**
	 * 说明： 此处的WGS-84地图 即世界地图,代表真实的地理位置 此处的Google地图 即火星地图,代表国测局加密偏移过的地理位置
	 * 此处的BD-09地图 即百度地图,代表百度二次加密偏移过的地理位置
	 **/
	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	private static final double pi = 3.14159265358979324;
	private static final double a = 6378245.0;
	private static final double ee = 0.00669342162296594323;

	/**
	 * gg_lat 纬度 gg_lon 经度 Google地图经纬度转百度地图经纬度
	 * */
	public static Point google_bd_encrypt(double gg_lat, double gg_lon) {
		Point point = new Point();
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		point.setLat(bd_lat);
		point.setLng(bd_lon);
		return point;
	}

	/**
	 * wgLat 纬度 wgLon 经度 百度转google
	 * */
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

	/**
	 * wgLat 纬度 wgLon 经度 WGS-84到google的转换（即 GPS加偏修正）
	 * */
	public static Point wgs_google_encrypts(double wgLat, double wgLon) {
		Point point = new Point();
		if (outOfChina(wgLat, wgLon)) {
			point.setLat(wgLat);
			point.setLng(wgLon);
			return point;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double lat = wgLat + dLat;
		double lon = wgLon + dLon;
		point.setLat(lat);
		point.setLng(lon);
		return point;
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

	/**
	 * bd_lat 纬度 bd_lon 经度 百度的转换到WGS-84
	 **/
	public static Point bd_wgs_encrypts(double bd_lat, double bd_lon) {
		Point point = bd_google_encrypt(bd_lat, bd_lon);
		Point pointResult = google_wgs_encrypts(point.getLat(), point.getLng());
		return pointResult;
	}

/*	public static void main(String[] args) {
		String points = "102.903602,25.144161;102.902843,25.140684;102.891586,25.138293;102.886962,25.129077;102.880387,25.125984;102.880821,25.11564;102.865088,25.093354;102.859987,25.088526;102.843478,25.084239;102.847154,25.080261;102.841484,25.06319;102.830232,25.057135;102.820318,25.039224;102.80395,25.04287;102.78028,25.039694;102.739461,25.050111;102.740927,25.037189;102.723759,25.039738;102.718819,25.064252;102.720335,25.077751;102.737489,25.098957;102.743379,25.100042;102.749722,25.120499;102.757748,25.123309;102.750484,25.131182;102.741291,25.122481;102.725736,25.125647;102.715583,25.134492;102.71603,25.15334;102.720829,25.162038;102.724241,25.185192;102.728937,25.195556;102.741528,25.207812;102.741906,25.214839;102.733213,25.23558;102.735812,25.250974;102.730671,25.260188;102.743806,25.280129;102.741489,25.289196;102.750984,25.29185;102.764955,25.286854;102.778723,25.291288;102.791396,25.282222;102.791984,25.262515;102.770148,25.242885;102.771458,25.238153;102.778011,25.236806;102.784896,25.229556;102.806693,25.220599;102.829705,25.217938;102.8478,25.225267;102.866021,25.204162;102.884975,25.20277;102.891872,25.197999;102.901,25.199097;102.904227,25.189292;102.903602,25.144161";
		String[] arrayPoints = points.split(";");
		String resStr = "";
		for (String str : arrayPoints) {
			String[] point = str.split(",");
			double bd_lat  = Double.parseDouble(point[1]);
			double bd_lon  = Double.parseDouble(point[0]);
			Point point2 = bd_wgs_encrypts(bd_lat, bd_lon);
			resStr = resStr + point2.getLng()+","+point2.getLat()+";";
		}
		System.out.println(resStr);

	}*/

	/**
	 * wgLat 纬度 wgLon 经度 WGS-84的转换到百度
	 **/
	public static Point wgs_bd_encrypts(double wgLat, double wgLon) {
		Point point = wgs_google_encrypts(wgLat, wgLon);
		Point pointResult = google_bd_encrypt(point.getLat(), point.getLng());
		return pointResult;
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

}