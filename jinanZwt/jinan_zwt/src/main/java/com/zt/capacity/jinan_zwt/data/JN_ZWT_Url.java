package com.zt.capacity.jinan_zwt.data;


import com.zt.capacity.common.data.Web;

public class JN_ZWT_Url {

    //更新
    public final static String QUERYAPPUPDATEINFO = Web.projectJN + "version/queryAppUpdateInfo";


    //所有车辆实时位置信息
    public final static String SELECTALLMYCAR =  Web.projectJN + "carruntime/selectCarRuntime";
    //车辆信息
    public final static String CARS =  Web.projectJN + "car/selectCarAndAll";

    //车辆信息
    public final static String SELECTNUMBERPLATE =  Web.projectJN + "car/selectNumberPlate";
    //登录
    public final static String LOGIN = Web.projectJN + "user/login";

    //获取权限信息
    public final static String ROLE = "user/role";

    //获取车辆历史轨迹
    public final static String CARHISTORYS =  Web.projectJN + "history/selectCarHistory";

    //疑点查车
    public final static String CIRCULAR =  Web.projectJN + "history/circular";

    //id获取工地围栏信息
    public final static String FINDCOSAPPFENCEBYID =  Web.projectJN + "enclosure/findCosappFenceById";

    //id获取消纳场围栏信息
    public final static String FINDUNLOADINGFENCEBYID =  Web.projectJN + "enclosure/findUnloadingFenceById";

    //指令下发历史信息
    public final static String FINDACTIONLIS =  Web.projectJN + "action/findActionList";

    //上传指令信息
    public final static String SENDACTION =  Web.projectJN + "action/sendAction";
    //车辆信息
    public final static String STATISTICSINFOTOTAL = Web.projectJN + "carruntime/statisticsInfoTotal";
    //工地围栏接口
    public final static String CARRUNTIMEALL = Web.projectJN + "carruntime/carRuntimeAll";
    //消纳场围栏接口
    public final static String GETUNLOADINGGPSFENCE = Web.projectJN + "gpsfence/getUnloadingGpsfence";
    //工地围栏接口
    public final static String GETCONSAPPGPSFENCE = Web.projectJN + "gpsfence/getConsappGpsfence";
    //工地围栏接口
    public final static String CERTIFICATEPOINTCAR = Web.projectJN + "card/certificatePointCar";
    //检查用户可用状态
    public final static String HWTOKENCHECK = Web.projectJN + "push/hwTokenCheck";
    //添加推送用户
    public final static String ADDPUSHUSER = Web.projectJN + "push/addPushUser";
    //删除推送用户
    public final static String DELETPUSHUSER = Web.projectJN + "push/deletPushUser";
    //周边违规
    public final static String GETAROUNDCARILLEGALINFO = Web.projectJN + "carIllegal/getAroundCarIllegalInfo";

    //查询推送记录
    public final static String GETPUSHRECORDVO = Web.projectJN + "pushrecord/getPushRecordVo";
    //修改记录状态
    public final static String UPPUSHRECORD = Web.projectJN + "pushrecord/upPushRecord";
    //查询未读数量
    public final static String GETPUSHRECORDCOUNT = Web.projectJN + "pushrecord/getPushRecordCount";



    //前端页面
    //首页
    public final static String MAINHOME = Web.htmlIP + "html/index.html?token=";
    //济南渣务厅首页
    public final static String JINANZHENGFU = Web.htmlIP + "html/html/index.html";
    //位置采集

    public final static String LOCATIONINDEX = "http://118.190.42.124:8080/zhatu/foreign/consapp/locationIndex";


    //济南的问题反馈
    public final static String JINANQUESTION = Web.htmlIP + "html/html/jinanquestion.html?token=";

    //济南政府问题反馈
    public final static String ABOUT = Web.htmlIP + "html/html/about.html?token=";

    //公用的问题反馈
    public final static String QUESTION = Web.htmlIP + "html/question.html?token=";
    //违规通知详情
    public final static String WISDOMLAWDETAIL_A = Web.htmlIP + "html/html/w_detailA.html?token=";




    //位置采集查询
    public final static String GETCONSAPPBYPOSITION = Web.projectJN + "consapp/getConsappByPosition";

    //位置采集信息上传
    public final static String SAVECONSAPPBYPOSITION = Web.projectJN + "consapp/saveConsappByPosition";
    public final static String GETCONSAPPINFO = Web.projectJN + "consapp/getConsappInfo";
    public final static String GETUNLOADINGINFO = Web.projectJN + "unloading/getUnloadingInfo";
    public final static String SAVEUNLOADBYPOSITION = Web.projectJN + "unloading/saveUnloadByPosition";
    public final static String GETCONSAPPPOS = Web.projectJN + "gpsfence/getConsappPos";


}
