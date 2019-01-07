package com.zt.capacity.common.data;

import com.baidu.mapapi.model.LatLng;
import com.zt.capacity.common.bean.UserBean;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.util.HttpUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/16.
 */

public abstract class Web extends HttpUtil {


//    private String apiIp = "http://113.247.235.86:9085/"; //正式 环境
        private String apiIp="http://114.116.156.44:9080/"; //测试 环境
//    private String apiIp="http://192.168.0.6:7081/"; //测试 环境
//    private  String apiIp="http://192.168.0.60:8086/"; //本地 环境
//    private String apiIp = "http://192.168.0.127:9080/"; //本地 环境
//    private String apiIp = "http://192.168.0.55:9081/yl/project/"; //本地


    //济南渣务厅


    /**
     * 前端页面端口
     */
//    public static String htmlIP = "http://113.247.235.86:9085/zwt/";//正式环境
    public static String htmlIP = "http://114.116.156.44:9080/zwt/";//测试环境


    public static String projectJN = "jinanzf/project/";//济南

    public static String mapAk = "eR3sGWC9deXA2Mg7HLySug8qp0IXPDsx";

    public static String webAk = "eR3sGWC9deXA2Mg7HLySug8qp0IXPDsx";

    //聚合数据apk
    public static String jhAk = "82be7808aee94419bcb66bb5b8855913";


    /**
     * 交通违规接口
     */
    public static String peccancyApiUrl = "http://v.juhe.cn/sweizhang/query";
    //车牌查城市号
    public static String peccancyCitysApiUrl = "http://v.juhe.cn/sweizhang/carPre";


    /**
     * 街景静态地图地图拾取
     * width=512&height=256&location=116.313393,40.04778&fov=180
     */
    public static String baiImage = "http://api.map.baidu.com/panorama/v2?ak=" + webAk + "&";
    /**
     * 逆地址解析
     * location="116.313393,40.04778"&pois=0";
     */
    public static String geocoder = "http://api.map.baidu.com/geocoder/v2/?ak=" + webAk + "&output=json&";

    //位置信息
    protected static LatLng point = new LatLng(28.118273, 119.969600);
    protected static String city = "长沙";

    public static LatLng jn_point = new LatLng(36.656733, 117.126543);

    protected static int gUserID;
    protected static String gUserGSName;

    public static String getgUserGSName() {
        return gUserGSName;
    }

    public static void setgUserGSName(String gUserGSName) {
        Web.gUserGSName = gUserGSName;
    }

    public static int getgUserID() {
        return gUserID;
    }

    public static LatLng getPoint() {
        return point;
    }

    public static void setPoint(LatLng point) {
        Web.point = point;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        Web.city = city;
    }

    /**
     * 登录接口成功后要设置userID
     *
     * @param userID
     */
    public static void setUserID(int userID) {
        Web.gUserID = userID;
//		FileLoader.setUserID(userID);
    }


    protected static String token = "";

    private String qurl;


    private static UserBean user;


    public static UserBean getUser() {
        return user;
    }

    public static void setUser(UserBean user) {
        Web.user = user;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Web.token = token;
    }

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     * @param code
     */
    protected Web(String url, Integer code) {
        if (code == 1) { //
            qurl = apiIp + url + "&" + token;
        }
        if (code == 2) { //post
            qurl = apiIp + url + "?" + token;
        }
        if (code == 3) {
            qurl = apiIp + url;
        }
        if (code == 4) {
            qurl = url;
        }
    }


    public void getQueryIamge(String Url, MyNetCall call) {
        this.getDataImage(qurl + Url, call);
    }

    public void getQuery(String Url, MyNetCall call) {

        this.getDataAsynFromNet(qurl + Url, call);
    }

    public void getQuery(MyNetCall call) {

        this.getDataAsynFromNet(qurl, call);
    }

    public void postQuery(Map body, MyNetCall call) {

        this.postDataAsynToNet(qurl, body, call);
    }

    public void postQueryJson(Map body, MyNetCall call) {
        this.postDataAsynToNetJson(qurl, body, call);
    }

    public void postUploadFile(Map<String, String> params, List<File> files, MyNetCall call) {
        this.postUoladFile(qurl, params, files, call);
    }

    public Response getFile() {
        return this.getDataSynFromNet(qurl);
    }

}
