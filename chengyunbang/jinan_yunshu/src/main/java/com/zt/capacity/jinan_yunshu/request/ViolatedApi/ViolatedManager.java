package com.zt.capacity.jinan_yunshu.request.ViolatedApi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_yunshu.bean.CarBean;
import com.zt.capacity.jinan_yunshu.bean.CityBean;
import com.zt.capacity.jinan_yunshu.bean.CityNoBean;
import com.zt.capacity.jinan_yunshu.bean.ViolationsBean;
import com.zt.capacity.jinan_yunshu.bean.ViolationsMoreBean;
import com.zt.capacity.jinan_yunshu.bean.ViolationsMoreSBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 违章详细信息
 * @author lt
 * @time 2018/12/24 14:19
 *
 **/
public class ViolatedManager extends Web implements IViolatedManager {
    private static ViolatedManager mAPI;

    public static IViolatedManager getInterface(Integer state) {
        ViolatedManager api = null;
        if (state == 1) {
            //车辆违章接口
            api = new ViolatedManager(Web.peccancyApiUrl);
        }
        if (state == 2) {
            //车牌号查城市
            api = new ViolatedManager(Web.peccancyCitysApiUrl);
        }
        return (IViolatedManager) api;
    }

    /**
     * @param url
     */
    protected ViolatedManager(String url) {
        super(url, 5);
    }

    @Override
    public void peccancyApi(CarBean car, String city, final OnNetResultListener listener) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("dtype","json");//返回数据类型
//        map.put("callback","");
//        map.put("hphm",car.getNumberPlate());//	号牌号码 完整7位 ,需要utf8 urlencode*
//        map.put("hpzl",car.getCarType());//号牌类型,默认02:小型车,01:大型车,52:新能源小型车,51:新能源大型车
//        map.put("city",city);//城市编号
//        map.put("engineno","");//发动机号 (根据城市接口中的参数填写)
//        map.put("key","7958a2b7d97167e15fff4d6d86905668");
//        map.put("classno",car.getChassisNumber());//车架号 (根据城市接口中的参数填写)


//        Map<String, String> map = new HashMap<String, String>();
//        map.put("dtype","json");//返回数据类型
//        map.put("callback","");
//        map.put("hphm",car.getNumberPlate());//	号牌号码 完整7位 ,需要utf8 urlencode*
//        map.put("hpzl",car.getCarType());//号牌类型,默认02:小型车,01:大型车,52:新能源小型车,51:新能源大型车
//        map.put("city","HUN_CS");//城市编号
//        map.put("engineno",car.getEngineNumber());//发动机号 (根据城市接口中的参数填写)
//        map.put("key",Web.jhAk);
//        map.put("classno",car.getChassisNumber());//车架号 (根据城市接口中的参数填写)

//        String EngineNumber = car.getEngineNumber();
//        car.setEngineNumber(EngineNumber.substring(EngineNumber.length() - 6));
//        String ChassisNumber = car.getChassisNumber();
//        car.setChassisNumber(ChassisNumber.substring(ChassisNumber.length() - 6));
        map.put("dtype","json");//返回数据类型
        map.put("callback","");
        map.put("hphm",car.getNumberPlate()== null ?"" :car.getNumberPlate());//	号牌号码 完整7位 ,需要utf8 urlencode*
        map.put("hpzl",car.getCarType());//号牌类型,默认02:小型车,01:大型车,52:新能源小型车,51:新能源大型车
        map.put("city",city);//城市编号
        map.put("engineno",car.getEngineNumber()==null? "":car.getEngineNumber());//发动机号 (根据城市接口中的参数填写)
        map.put("key","82be7808aee94419bcb66bb5b8855913");
        map.put("classno",car.getChassisNumber()==null? "" :car.getChassisNumber());//车架号 (根据城市接口中的参数填写)
        map.put("dtype", "json");//返回数据类型
        map.put("callback", "");
        map.put("hphm", car.getNumberPlate() == null ? "" : car.getNumberPlate());//	号牌号码 完整7位 ,需要utf8 urlencode*

        postQuery(map, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                if (code == 0) {
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    ViolationsMoreSBean violationsMoresBean = gson.fromJson(data.toString(), ViolationsMoreSBean.class);
                    Log.e("xxxxxxxxxxxxxxxxx", data.toString());
                    ViolationsMoreBean violationsMoreBean = violationsMoresBean.getResult();
                    List<ViolationsBean> lists = new ArrayList<ViolationsBean>();
                    if (violationsMoresBean.getResultcode().equals("200")) {
                        if (violationsMoreBean != null && violationsMoreBean.getLists() != null) {
                            for (int i = 0; i < violationsMoreBean.getLists().size(); i++) {
                                ViolationsBean violationsBean = violationsMoreBean.getLists().get(i);
                                violationsBean.setTime(System.currentTimeMillis());
                                violationsBean.setHphm(violationsMoreBean.getHphm());
                                violationsBean.setProvince(violationsMoreBean.getProvince());
                                violationsBean.setCity(violationsMoreBean.getCity());
                                violationsBean.setHphm(violationsMoreBean.getHphm());
                                lists.add(violationsBean);
                            }
                        }
                        listener.onResult(0, violationsMoresBean.getReason(), lists);
                    } else {

                        listener.onResult(1, violationsMoresBean.getReason(), lists);
                    }
                } else {

                    listener.onResult(1, message, "");
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "查询失败", "");
            }
        });


    }

    @Override
    public void peccancyNuber(String nuber, final OnNetResultListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("hphm", nuber);
        map.put("isNer", "");
        map.put("key", Web.jhAk);
        postQuery(map, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                if (code == 0) {
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    CityNoBean cityNoBean = gson.fromJson(data.toString(), CityNoBean.class);
                    Log.e("xxxxxxxxxxxxxxxxx", data.toString());
                    CityBean cityBean = cityNoBean.getResult();
                    listener.onResult(0, cityNoBean.getReason(), cityBean);
                } else {

                    listener.onResult(1, "查询失败", "");
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "查询失败", "");
            }
        });

    }
}
