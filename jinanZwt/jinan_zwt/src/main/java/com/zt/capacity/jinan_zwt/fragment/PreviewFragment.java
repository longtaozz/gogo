package com.zt.capacity.jinan_zwt.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainActivity;
import com.zt.capacity.jinan_zwt.activity.RichScanCarActivity;
import com.zt.capacity.jinan_zwt.adapter.MatchingAdapter;
import com.zt.capacity.jinan_zwt.bean.Car;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pr.platerecognization.PlateRecognition;

/**
 * 扫车牌
 */
public class PreviewFragment
        extends Fragment
        implements Camera.PreviewCallback, SurfaceHolder.Callback, View.OnClickListener {

    private View view;
    int PreviewHeight = 0;
    int PreviewWidth = 0;
    Camera camera = null;
    SurfaceView camerasurface = null;
    long currentTime;
    Camera.Parameters parameters;

    private void openCamera() {
        if (this.camera == null) {
            try {
                this.camera = Camera.open(0);
                byte[] mPreBuffer = new byte[1024];//首先分配一块内存作为缓冲区，size的计算方式见第四点中
                camera.setPreviewCallback(this);
                return;
            } catch (Exception localException) {
                Log.d("TAG", "camera is not available");
            }
        }

    }


    @SuppressLint("WrongConstant")
    private void startPreView() {
        if (this.camera == null) {
            Log.e("TAG", "Open Camera fail");
            return;
        }
        this.PreviewWidth = 640;
        this.PreviewHeight = 480;
        this.parameters = this.camera.getParameters();
        this.parameters.setFocusMode("macro");
        this.parameters.setFocusMode("continuous-picture");
        this.camera.setParameters(this.parameters);
        try {
            this.camera.setPreviewDisplay(this.camerasurface.getHolder());
            ((WindowManager) getActivity().getSystemService("window")).getDefaultDisplay();
            this.parameters = this.camera.getParameters();
            Object localObject = this.parameters.getSupportedPreviewSizes();
            if (((List) localObject).size() > 1) {
                localObject = ((List) localObject).iterator();
                while (((Iterator) localObject).hasNext()) {
                    Camera.Size localSize = (Camera.Size) ((Iterator) localObject).next();
                    if (localSize.height <= 360) {
                        this.PreviewWidth = localSize.width;
                        this.PreviewHeight = localSize.height;
                    }
                }
            }
            this.PreviewWidth = 640;
            this.PreviewHeight = 480;
            this.parameters.setPreviewSize(this.PreviewWidth, this.PreviewHeight);
            this.parameters.setPictureSize(this.PreviewWidth, this.PreviewHeight);
            this.camera.setDisplayOrientation(90);
            this.camera.setParameters(this.parameters);
        } catch (IOException localIOException1) {
            try {
                this.camera.setPreviewDisplay(this.camerasurface.getHolder());
                this.camera.startPreview();
                this.camera.setPreviewCallback(this);
                return;
//                localIOException1.printStackTrace();
            } catch (IOException localIOException2) {
                for (; ; ) {
                    localIOException2.printStackTrace();
                }
            }
        }
    }


    private ImageView preview_put_car_number_button;//手动输入车牌
    private LinearLayout preview_put_car_number_linear;//输入框容器
    private int put_state = 0;//0不显示,1显示
    private AutoCompleteTextView preview_put_car_number;//输入框
    private LinearLayout preview_put_car_number_ok;//确定
    private LinearLayout preview_put_car_number_no;//取消

    //所有车辆
    private List<Car> cars;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        getActivity().requestWindowFeature(1);
//        getActivity().getWindow().setFlags(1024, 1024);
        view = inflater.inflate(R.layout.activity_camera_plate, null);
        Bundle bundle = this.getArguments();

        init();

        camerasurface = view.findViewById(R.id.surfaceView2);
        camerasurface.getHolder().addCallback(this);
        camerasurface.setKeepScreenOn(true);
        return view;
    }



    private void init() {
        preview_put_car_number_button = view.findViewById(R.id.preview_put_car_number_button);//手动输入车牌


        //手动输入车牌
        preview_put_car_number_button = view.findViewById(R.id.preview_put_car_number_button);//手动输入车牌
        preview_put_car_number_button.setOnClickListener(this);
        preview_put_car_number_linear = view.findViewById(R.id.preview_put_car_number_linear);//输入框容器
        preview_put_car_number = view.findViewById(R.id.preview_put_car_number);//输入框
        preview_put_car_number_ok = view.findViewById(R.id.preview_put_car_number_ok);//确定
        preview_put_car_number_ok.setOnClickListener(this);
        preview_put_car_number_no = view.findViewById(R.id.preview_put_car_number_no);//取消
        preview_put_car_number_no.setOnClickListener(this);


        if (MainActivity.cars.size() > 0) {
            //配置搜索框
            cars = MainActivity.cars;
            setMatching();
        }
    }


    private void setMatching() {
        //所有车辆
        List<Car> cars = MainActivity.cars;
        MatchingAdapter adapter = new MatchingAdapter(getActivity(), cars);
        preview_put_car_number.setAdapter(adapter);
        preview_put_car_number.setThreshold(0);
    }


    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    int x = 0;

    boolean state = true;

    public void onPreviewFrame(final byte[] paramArrayOfByte, Camera paramCamera) {
        if (camera == null) {
            return;
        }
        this.currentTime = System.currentTimeMillis();

        long l1 = System.currentTimeMillis();
        long l2 = this.currentTime;


        if (state) {
            state = false;
            Log.e("jinlai....","111111111");
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> carNums = PlateRecognition.plateRecognitionWithRaw(paramArrayOfByte, PreviewHeight, PreviewWidth, 0.8F, 40, 500, 1);
                    state = true;
                    if (carNums != null && carNums.size() > 0) {
                        Message message = new Message();
                        message.obj = carNums;
                        handler.sendMessage(message);
                    }
                }
            });
        }

//        if (paramArrayOfByte != "") {
//            this.warning.setText(paramArrayOfByte);
//            showRes(paramArrayOfByte);
//        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> n = (List<String>) msg.obj;
            numberCL(n);
        }
    };

    int number = 0;

    //车牌处理
    private void numberCL(List<String> carNums) {
        Log.e("debug_res6", carNums.get(0));
        List<Car> cars = MainActivity.cars;
        String carnum = carNums.get(0);
        Log.e("debug_res7", carnum);
        for (int i = 0; i < cars.size(); i++) {
            if (carnum.equals(cars.get(i).getNumberPlate())) {
                state = false;
                ActivityJumpUtil.jumpActivityByString(getActivity(), RichScanCarActivity.class, carnum, "carNumber");
                getActivity().finish();
                return;
            }
        }
        number = number + 1;
        if (number > 0) {
            UIUtils.showToast(getActivity(), "无此车记录或车牌位置不对~");
            number = 0;
        }
    }

    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
        if (paramInt == 1) {
            paramInt = 0;
            if (paramInt < paramArrayOfString.length) {
                if (paramArrayOfInt[paramInt] == 0) {
                    openCamera();
                    startPreView();
                }
                for (; ; ) {
                    paramInt += 1;
                    break;
                }
            }
        }
    }


    public void onStop() {
        super.onStop();
    }

    @TargetApi(23)
    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            openCamera();
            startPreView();
            return;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA"}, 1);
        requestPermissions(new String[]{"android.permission.CAMERA"}, 100);
    }

    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {

    }

    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        openCamera();
        startPreView();
    }

    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
            camerasurface.getHolder().removeCallback(this);
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.preview_put_car_number_button:
                //手动输入车牌按钮点击
                put_state = 1;
                preview_put_car_number_linear.setVisibility(View.VISIBLE);
                break;
            case R.id.preview_put_car_number_ok:
                //确定
                String cNumber = preview_put_car_number.getText() + "";
                if (!TextUtils.isEmpty(cNumber)) {
                    int number = 0;
                    List<Car> cars = MainActivity.cars;
                    if (cars.size() > 0) {
                        for (int i = 0; i < cars.size(); i++) {
                            if (cNumber.equals(cars.get(i).getNumberPlate())) {
                                ActivityJumpUtil.jumpActivityByString(getActivity(), RichScanCarActivity.class, cNumber, "carNumber");
                                getActivity().finish();
                                return;
                            }
                        }
                        number++;
                        if (number > 0) {
                            UIUtils.showToast(getActivity(), "无此车记录或车牌位置不对~");
                            number = 0;
                        }
                    }
                } else {
                    UIUtils.showToast(getActivity(), "请输入车牌");
                }
                break;
            case R.id.preview_put_car_number_no:
                //取消
                put_state = 0;
                preview_put_car_number_linear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTChePaiSaoMiaoYe"); //统计页面，"MainScreen"为页面名称，可自定义
        Log.e("onResume", "onResume");
        camerasurface.getHolder().addCallback(this);
        requestCameraPermission();
        if (this.camera != null) {
            this.camera.startPreview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTChePaiSaoMiaoYe");
        if (camera != null) {
            camera.stopPreview();
            camerasurface.getHolder().removeCallback(this);
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }
}
