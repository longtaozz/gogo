package com.zt.capacity.baidumap.map;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.zt.capacity.baidumap.map.clusterutil.clustering.ClusterItem;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SwitBdItem implements ClusterItem {

    private Bundle bundle;
    private final LatLng mPosition;
    private View view;

    public SwitBdItem(LatLng latLng, View view, Bundle bundle) {
        mPosition = latLng;
        this.view=view;
        this.bundle=bundle;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromView(view);
    }
    public Bundle getBundle(){
        return bundle;
    }

}
