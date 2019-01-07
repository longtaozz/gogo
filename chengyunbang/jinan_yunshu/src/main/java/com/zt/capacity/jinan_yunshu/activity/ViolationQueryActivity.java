package com.zt.capacity.jinan_yunshu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.hjq.toast.ToastUtils;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.DateUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_yunshu.R;
import com.zt.capacity.jinan_yunshu.adapter.ViolationQueryAdapter;
import com.zt.capacity.jinan_yunshu.bean.CarBean;
import com.zt.capacity.jinan_yunshu.bean.ViolationsBean;
import com.zt.capacity.jinan_yunshu.request.ViolatedApi.IViolatedManager;
import com.zt.capacity.jinan_yunshu.request.ViolatedApi.ViolatedManager;

import java.io.Serializable;
import java.util.List;

/**
 * 违章信息界面
 * Created by Administrator on 2018-05-07.
 */

public class ViolationQueryActivity extends BaseActivity implements View.OnClickListener {

    private TextView mIllegalTextview;//违章
    private TextView points_textview;//扣分
    private TextView fine_textview;
    ;//罚款
    private TextView the_recent_query;//最近查询时间
    private LinearLayout noviolation;
    private ListView violation_listview;//
    private String str = "湘AF2C75";
    private ViolationQueryAdapter mViolationQueryAdapter;
    private List<ViolationsBean> lists;
    private LinearLayout back;
    private TextView title_name;
    private String numberPlate;//车牌号
    private String chassisNumber;//车架号
    private String engineNumber;//发动机号
    private String carType;// 车辆类型

    //数据
    private CarBean car = null;
    private String cityId;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_yunshu_activity_violation_query);

        car = (CarBean) getIntent().getSerializableExtra("car");
        cityId = getIntent().getStringExtra("cityId");
//        type = getIntent().getIntExtra("type",0);
//        if(type == 2){
//            PrivateCar privateCar = (PrivateCar) getIntent().getSerializableExtra("privateCar");
//            car = new Car();
//            car.setNumberPlate(privateCar.getCarNumber());
//            car.setEngineNumber(privateCar.getEngineNumber());
//            car.setChassisNumber(privateCar.getChassisNumber());
//            Log.e("car",car.getNumberPlate()+"---"+car.getEngineNumber()+"--"+car.getChassisNumber());
//        }else {
//            car = (Car) getIntent().getSerializableExtra("car");
//        }

        Log.e("car", car.getNumberPlate() + "---" + car.getEngineNumber() + "--" + car.getChassisNumber());
//        car.setNumberPlate("湘AL8P89");
//        car.setEngineNumber("664072");
//        car.setChassisNumber("718403");
        initview();
        title_name.setText(car.getNumberPlate());
        initDate();
    }

    private void initDate() {
        if (car != null) {
            IViolatedManager violatedManager = ViolatedManager.getInterface(1);
            violatedManager.peccancyApi(car, cityId, new OnNetResultListener() {
                @Override
                public void onResult(int state, String msg, Object body) {
                    UIUtils.cancelProgressDialog();
                    if (state == 0) {
                        lists = (List<ViolationsBean>) body;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("vio", (Serializable) lists);
                        Message m = new Message();
                        m.what = 0;
                        m.setData(bundle);
                        handler.sendMessage(m);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", msg);
                        Message m = new Message();
                        m.what = 2;
                        m.setData(bundle);
                        handler.sendMessage(m);
//                                      IToast.show(ViolationQueryActivity.this,msg);
                    }
                }
            });
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    List<ViolationsBean> listvo = (List<ViolationsBean>) msg.getData().getSerializable("vio");

                    if (listvo == null || listvo.size() < 1) {
                        noviolation.setVisibility(View.VISIBLE);
                        violation_listview.setVisibility(View.GONE);
                    } else {
                        Log.e("list", listvo.size() + "--" + listvo.toString());
                        int illega, points = 0, fine = 0;
                        illega = listvo.size();
                        for (int i = 0; i < illega; i++) {
                            fine = fine + Integer.parseInt(listvo.get(i).getMoney());
                            points = points + Integer.parseInt(listvo.get(i).getFen());
                        }
                        Log.e("time --- ", listvo.get(0).getTime() + "");
                        the_recent_query.setText(DateUtil.showDate(DateUtil.DATE_FORMAT_YMD, listvo.get(0).getTime()));
                        mIllegalTextview.setText(illega + "");
                        points_textview.setText(points + "");
                        fine_textview.setText(fine + "");
                        noviolation.setVisibility(View.GONE);
                        violation_listview.setVisibility(View.VISIBLE);
                        mViolationQueryAdapter = new ViolationQueryAdapter(ViolationQueryActivity.this, listvo);
                        violation_listview.setAdapter(mViolationQueryAdapter);
                    }
                    break;
                case 1:

                    break;
                case 2:
                    String msgs = (String) msg.getData().get("msg");
                    ToastUtils.show(msgs);
                    break;
            }
        }
    };


    private void initview() {
        mIllegalTextview = findViewById(R.id.illegal_textview);
        points_textview = findViewById(R.id.points_textview);
        fine_textview = findViewById(R.id.fine_textview);
        the_recent_query = findViewById(R.id.the_recent_query);
        noviolation = findViewById(R.id.noviolation);//无违规显示内容
        violation_listview = findViewById(R.id.violation_listview);
        back = findViewById(R.id.back);
        title_name = findViewById(R.id.title_name);
        back.setOnClickListener(this);
    }

    ;

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back) {
            finish();

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
