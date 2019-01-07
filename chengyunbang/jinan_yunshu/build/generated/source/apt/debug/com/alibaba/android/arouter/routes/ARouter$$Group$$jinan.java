package com.alibaba.android.arouter.routes;

import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IRouteGroup;
import com.zt.capacity.jinan_yunshu.activity.InputVehicleActivity;
import com.zt.capacity.jinan_yunshu.activity.InstructionActivity;
import com.zt.capacity.jinan_yunshu.activity.MainActivity;
import com.zt.capacity.jinan_yunshu.activity.MainMyActivity;
import com.zt.capacity.jinan_yunshu.activity.NotificationActivity;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY AROUTER. */
public class ARouter$$Group$$jinan implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/jinan/transit/main", RouteMeta.build(RouteType.ACTIVITY, MainActivity.class, "/jinan/transit/main", "jinan", null, -1, -2147483648));
    atlas.put("/jinan/yunshu/InputVehicle", RouteMeta.build(RouteType.ACTIVITY, InputVehicleActivity.class, "/jinan/yunshu/inputvehicle", "jinan", null, -1, -2147483648));
    atlas.put("/jinan/yunshu/instruction", RouteMeta.build(RouteType.ACTIVITY, InstructionActivity.class, "/jinan/yunshu/instruction", "jinan", new java.util.HashMap<String, Integer>(){{put("carNumber", 8); }}, -1, -2147483648));
    atlas.put("/jinan/yunshu/mainmy", RouteMeta.build(RouteType.ACTIVITY, MainMyActivity.class, "/jinan/yunshu/mainmy", "jinan", null, -1, -2147483648));
    atlas.put("/jinan/yunshu/notification", RouteMeta.build(RouteType.ACTIVITY, NotificationActivity.class, "/jinan/yunshu/notification", "jinan", null, -1, -2147483648));
  }
}
