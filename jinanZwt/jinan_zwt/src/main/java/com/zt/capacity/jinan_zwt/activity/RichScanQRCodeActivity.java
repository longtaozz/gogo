package com.zt.capacity.jinan_zwt.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseActivity;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.bean.SaoCard;
import com.zt.capacity.jinan_zwt.request.richscan.IRichScanManager;
import com.zt.capacity.jinan_zwt.request.richscan.RichScanManager;

/**
 * 二维码扫描结果界面
 */
public class RichScanQRCodeActivity extends BaseActivity implements View.OnClickListener {
    private String cardId;
    //车辆信息
    private SaoCard saoCard;


    //头布局
    private ImageView title_img;//返回
    private TextView title_name;//标题

    //项目名称,运输单位,工程地址,车牌号,城建号,倾倒地点,证件有效期起,证件有效期止,
    // 通行路线
    private TextView pro_name, enterpriseName,
            constructionAddress, carNumber,
            admitNumber, dumping,
            tranStarttime, tranEndtime,
            loadInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jn_zwt_rich_scan_qr_code);
        cardId = getIntent().getStringExtra("cardId");
        if (cardId != null && !"".equals(cardId)) {
            cardIdByCardData();
        }
        init();
    }

    private void init() {
        //头布局
        title_img = findViewById(R.id.title_img);//返回
        title_img.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);//标题
        title_name.setText("准运证详情");

        //项目名称,运输单位,工程地址,车牌号,城建号,倾倒地点,证件有效期起,证件有效期止,
        // 通行路线
        pro_name = findViewById(R.id.rich_scan_qr_code_pro_name);
        enterpriseName = findViewById(R.id.rich_scan_qr_code_enterpriseName);
        constructionAddress = findViewById(R.id.rich_scan_qr_code_constructionAddress);
        carNumber = findViewById(R.id.rich_scan_qr_code_carNumber);
        admitNumber = findViewById(R.id.rich_scan_qr_code_admitNumber);
        dumping = findViewById(R.id.rich_scan_qr_code_dumping);
        tranStarttime = findViewById(R.id.rich_scan_qr_code_tranStarttime);
        tranEndtime = findViewById(R.id.rich_scan_qr_code_tranEndtime);
        loadInfo = findViewById(R.id.rich_scan_qr_code_loadInfo);
    }

    //更新页面数据
    private void updateTextView() {
        if (saoCard == null) {
            return;
        }
        pro_name.setText(saoCard.getProName() != null ? saoCard.getProName() + "" : "");
        enterpriseName.setText(saoCard.getEnterpriseName() != null ? saoCard.getEnterpriseName() + "" : "");
        constructionAddress.setText(saoCard.getProAddress() != null ? saoCard.getProAddress() + "" : "");
        carNumber.setText(saoCard.getNumberPlate() != null ? saoCard.getNumberPlate() + "" : "");
        admitNumber.setText(saoCard.getAdmitNumber() != null ? saoCard.getAdmitNumber() + "" : "");
        dumping.setText(saoCard.getXiaonaFencename() != null ? saoCard.getXiaonaFencename() + "" : "");
        tranStarttime.setText(saoCard.getTranStarttime() != null ? saoCard.getTranStarttime() + "" : "");
        tranEndtime.setText(saoCard.getTranEndtime() != null ? saoCard.getTranEndtime() + "" : "");
        loadInfo.setText(saoCard.getLoadInfo() != null ? saoCard.getLoadInfo() + "" : "");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //请求成功
                    UIUtils.cancelProgressDialog();
                    updateTextView();
                    break;
                case 1:
                    //请求失败
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(RichScanQRCodeActivity.this, "请求失败");
                    break;
            }
        }
    };

    //车牌查询车辆信息
    private void cardIdByCardData() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip, false);
        IRichScanManager richScanManager = RichScanManager.getInterface(1);
        richScanManager.richScanQrCode(cardId, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    if (body != null) {
                        saoCard = (SaoCard) body;
                    }
                    message.what = 0;
                } else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTErWeiMaXinXi"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTErWeiMaXinXi"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
