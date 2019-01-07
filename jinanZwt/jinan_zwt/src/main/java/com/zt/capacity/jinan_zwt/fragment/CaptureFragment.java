package com.zt.capacity.jinan_zwt.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.qrcode.camera.CameraManager;
import com.qrcode.camera.PreviewFrameShotListener;
import com.qrcode.camera.Size;
import com.qrcode.decode.DecodeListener;
import com.qrcode.decode.DecodeThread;
import com.qrcode.decode.LuminanceSource;
import com.qrcode.decode.PlanarYUVLuminanceSource;
import com.qrcode.decode.RGBLuminanceSource;
import com.qrcode.util.DocumentUtil;
import com.qrcode.view.CaptureView;
import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.common.util.UIUtils;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.RichScanQRCodeActivity;
import com.zt.capacity.jinan_zwt.activity.TextHintActivity;

/**
 * 二维码扫描
 */
public class CaptureFragment extends Fragment implements SurfaceHolder.Callback, PreviewFrameShotListener, DecodeListener,
        OnCheckedChangeListener {

    private static final long VIBRATE_DURATION = 200L;
    private static final int REQUEST_CODE_ALBUM = 0;
    public static final String EXTRA_RESULT = "result";
    public static final String EXTRA_BITMAP = "bitmap";

    private SurfaceView previewSv;
    private CaptureView captureView;

    private CameraManager mCameraManager;
    private DecodeThread mDecodeThread;
    private Rect previewFrameRect = null;
    private boolean isDecoding = false;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_capture, null);

        previewSv = view.findViewById(R.id.sv_preview);
        captureView = view.findViewById(R.id.cv_capture);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("JNZWTErWeiMaSaoMiao"); //统计页面，"MainScreen"为页面名称，可自定义
        previewSv.getHolder().addCallback(this);
        mCameraManager = new CameraManager(getActivity());
        mCameraManager.setPreviewFrameShotListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("dakaisexi....","22222222222");
        mCameraManager.initCamera(holder);
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }
        mCameraManager.startPreview();
        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("JNZWTErWeiMaSaoMiao");
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        previewSv.getHolder().removeCallback(this);
        mCameraManager.setPreviewFrameShotListener(null);
        mCameraManager.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPreviewFrame(byte[] data, Size dataSize) {
        if (!mCameraManager.isCameraAvailable()) {
            return;
        }
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(captureView.getFrameRect());
        }
        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(data, dataSize, previewFrameRect);
        mDecodeThread = new DecodeThread(luminanceSource, CaptureFragment.this);
        isDecoding = true;
        mDecodeThread.execute();
    }

    @Override
    public void onDecodeSuccess(Result result, LuminanceSource source, Bitmap bitmap) {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
        isDecoding = false;
        if (bitmap.getWidth() > 100 || bitmap.getHeight() > 100) {
            Matrix matrix = new Matrix();
            matrix.postScale(100f / bitmap.getWidth(), 100f / bitmap.getHeight());
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = resizeBmp;
        }
        String resultStr = result.getText();

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = resultStr.indexOf("cardId=");
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            ActivityJumpUtil.jumpActivityByString(getActivity(), TextHintActivity.class, "无效二维码!", "textStr");
            return;
        }
        /* 开始截取 */
        final String cardId = resultStr.substring(strStartIndex, resultStr.length()).substring("cardId=".length());
        if (cardId == null || TextUtils.isEmpty(cardId)) {
            ActivityJumpUtil.jumpActivityByString(getActivity(), TextHintActivity.class, "无效二维码!", "textStr");
            getActivity().finish();
            return;
        }
        ActivityJumpUtil.jumpActivityByString(getActivity(), RichScanQRCodeActivity.class, cardId, "cardId");
        getActivity().finish();
    }

    @Override
    public void onDecodeFailed(LuminanceSource source) {
        if (source instanceof RGBLuminanceSource) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }
        isDecoding = false;
        mCameraManager.requestPreviewFrameShot();
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        captureView.addPossibleResultPoint(point);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mCameraManager.enableFlashlight();
        } else {
            mCameraManager.disableFlashlight();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == getActivity().RESULT_OK && data != null) {
            Bitmap cameraBitmap = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String path = DocumentUtil.getPath(getActivity(), data.getData());
                cameraBitmap = DocumentUtil.getBitmap(path);
            } else {
                // Not supported in SDK lower that KitKat
            }
            if (cameraBitmap != null) {
                if (mDecodeThread != null) {
                    mDecodeThread.cancel();
                }
                int width = cameraBitmap.getWidth();
                int height = cameraBitmap.getHeight();
                int[] pixels = new int[width * height];
                cameraBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                RGBLuminanceSource luminanceSource = new RGBLuminanceSource(pixels, new Size(width, height));
                mDecodeThread = new DecodeThread(luminanceSource, CaptureFragment.this);
                isDecoding = true;
                mDecodeThread.execute();
            }
        }
    }

}
