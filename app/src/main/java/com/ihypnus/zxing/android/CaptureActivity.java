/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ihypnus.zxing.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.multilanguage.MultiLanguageUtil;
import com.ihypnus.zxing.android.camera.CameraManager;
import com.ihypnus.zxing.android.camera.CameraUtil;
import com.ihypnus.zxing.android.decode.CaptureActivityHandler;
import com.ihypnus.zxing.android.decode.DecodeFormatManager;
import com.ihypnus.zxing.android.decode.DecodeThread;
import com.ihypnus.zxing.android.decode.InactivityTimer;
import com.ihypnus.zxing.android.decode.IntentSource;
import com.ihypnus.zxing.android.view.ViewfinderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private Gson mGson = new Gson();
    public CameraManager cameraManager;
    public CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private Result lastResult;
    private boolean hasSurface;
    private IntentSource source;
    private String sourceUrl;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    public SurfaceView surfaceView;
    public TextView txt_light;
    public TextView txt_cancle;
    //    public TextView txt_photo;
//    public View viewPhoto;
    private View focusIndex;
    /**
     * 电量广播
     */
    public BatteryReceiver batteryReceiver;

//    private DisplayImageOptions mImgDisplayOptions;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        EventBus.getDefault().register(this);
        CameraUtil.init(this);


//        mImgDisplayOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_stub) // 设置图片下载期间显示的图片
//                .imageScaleType(ImageScaleType.EXACTLY)//像将完全按比例缩小的目标大小
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .showImageForEmptyUri(R.mipmap.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中 一定要设置缓存磁盘，不然base64图片不显示
//                .build();

/*    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.capture);

    hasSurface = false;
    inactivityTimer = new InactivityTimer(this);
    beepManager = new BeepManager(this);
    ambientLightManager = new AmbientLightManager(this);

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);*/

        // 1, 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 2, 找到界面组件
        setContentView(R.layout.activity_capture);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceView.setDrawingCacheEnabled(true);
        surfaceView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);
//        img_left = (ImageView) findViewById(R.id.capture_img_left);
        txt_light = (TextView) findViewById(R.id.capture_txt_light);
        txt_cancle = (TextView) findViewById(R.id.capture_txt_cancle);
//        txt_photo = (TextView) findViewById(R.id.capture_txt_photo);
//        viewPhoto = findViewById(R.id.capture_layout_gallery);
        focusIndex = findViewById(R.id.focus_index);
        // 3, 保持屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        cameraManager = new CameraManager(getApplication());
//        viewfinderView.setCameraManager(cameraManager);

        // 4,
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        widgetListener();
        registerReceiver();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public void widgetListener() {
//        img_left.setOnClickListener(this);
//        txt_photo.setOnClickListener(this);
        txt_cancle.setOnClickListener(this);
        txt_light.setOnClickListener(this);
    }

    /**
     * (注册广播.)
     */
    protected void registerReceiver() {
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();

        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        handler = null;
        lastResult = null;

        resetStatusView();

        inactivityTimer.onResume();

        source = IntentSource.NONE;
        sourceUrl = null;
        decodeFormats = null;
        characterSet = null;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_270:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (inactivityTimer != null) {
            inactivityTimer.onPause();
        }
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
        }
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
//        if (null != soundPool) {
//            soundPool.release();
//            soundPool = null;
//        }
//        关闭成功声音
        if (successPlay != null) {
            successPlay.stop();
            successPlay = null;
        }
//        关闭失败声音
        if (errorPlayer != null) {
            errorPlayer.stop();
            errorPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (source == IntentSource.NATIVE_APP_INTENT) {
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;
            // Use volume up/down to turn on light

            //监听音量键，实现开关灯：
            /*case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;*/
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE_CODE) { // 图库选择图片后返回
//                if (data != null) {
//                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
//                    if (mSelectPath == null || mSelectPath.size() == 0) {
//                        return;
//                    }
//
//
//                    ImageLoader.getInstance().loadImage("file://" + mSelectPath.get(0), mImgDisplayOptions, new SimpleImageLoadingListener() {
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            super.onLoadingComplete(imageUri, view, loadedImage);
//                            if (loadedImage == null)
//                                ToastUtils.showToastInCenter(CaptureActivity.this, "loadedImage==null");
//                            Result result;
//                            result = scanningImage(loadedImage);
//                            float scaleFactor = 0.0f;
//                            handleDecode(result, loadedImage, scaleFactor, CaptureActivity.TYPE_LOAD_IMAGE);
//                        }
//                    });
//                }
//            }
        }
    }

    public Result scanningImage(Bitmap bitmap) {
        /**
         * 首先，和生成二维码一样，我们要告诉系统解析二维码的设置参数。这里我选择了支持主流的三类方式，其中一种为一维码（条形码）。设置解析的字符位UTF8。
         * 如果不设置字符解析方式，它会自己去识别内容，然后自己判断该用哪种方式。
         * */

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
                2);
        // 可以解析的编码类型
        Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);//一维码
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);//二维码
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);//Data Matrix
        decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);//Aztec
        decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);//PDF417

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        // 设置继续的字符编码格式为UTF8
        // zxing源码中对UTF8的定义字符串内容不是UTF-8，而是UTF8。
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;

       /* // 设置解析配置参数
        Result[] results = new Result[]{};
        try {
            results = multiFormatReader.decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))), hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } finally {
            multiFormatReader.reset();
        }*/
        try {
            LuminanceSource source = new PlanarYUVLuminanceSource(
                    rgb2YUV(bitmap), bitmap.getWidth(),
                    bitmap.getHeight(), 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), false);
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(
                    new HybridBinarizer(source)));
            LogOut.d(TAG, rawResult.getText());
            return rawResult;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] rgb2YUV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult The contents of the barcode.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor, int type) {
        inactivityTimer.onActivity();
        //        if (mAutoPlayBeepSound) {
        //            playBeepSoundAndVibrate();
        //        }
        showResult(rawResult, barcode, type);
    }

    /* 扫描回调* */
    public void showResult(final Result rawResult, Bitmap barcode, int type) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, 2000);
        }

        if (rawResult == null) {
            playErrorBeepSoundAndVibrate();//解码错误的声音播放
            ToastUtils.showToastInCenter(CaptureActivity.this, getString(R.string.toasts_scan_failed));
            return;
        }
        String num = rawResult.getText().trim();
        String model = "";

        if (num.startsWith("{") && num.endsWith("}") && num.contains("Ver") && num.contains("ID") && num.contains("SN") && num.contains("Model")) {
            //V2版本二维码
            ScanDeviceBean bean = mGson.fromJson(num, ScanDeviceBean.class);
            if (bean != null && !StringUtils.isNullOrEmpty(bean.getSN())) {
                num = bean.getSN();
                model = bean.getModel();
            } else {
                ToastUtils.showToastInCenter(CaptureActivity.this, getString(R.string.tv_toast_scan_error));
                finish();
                return;
            }

        } else if (num.contains("Ver") && num.contains("ID") && num.contains("SN")) {
            //V1.9版本二维码
            //“Ver: V1.19-00036\nID:393035393436470b00390029\nSN:CP70100506S”
            int start = num.lastIndexOf("SN");
            if (num.length() > start + 3) {
                num = num.substring(start + 3, num.length());
            } else {
                ToastUtils.showToastInCenter(CaptureActivity.this, getString(R.string.tv_toast_scan_error));
                finish();
                return;
            }
        } else {
            ToastUtils.showToastInCenter(CaptureActivity.this, getString(R.string.tv_toast_scan_error));
            finish();
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("id", num);
        intent.putExtra("model", model);
        playBeepSoundAndVibrate();//解码正确的声音播放
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
     *
     * @param barcode     A bitmap of the captured image.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param rawResult   The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4 &&
                    (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
                            rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(),
                    scaleFactor * a.getY(),
                    scaleFactor * b.getX(),
                    scaleFactor * b.getY(),
                    paint);
        }
    }

    public void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            showError();
            finish();
            return;
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            showError();
            finish();
            return;
        }
    }


    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    //——————————————————— 分隔线 —————————————————————

    /**
     * 是否是第一次进来
     */
    public boolean isFirst = true;
    public static final long VIBRATE_DURATION = 200L;
    private boolean mAutoPlayBeepSound = true;              // 是否播放Beep的声音（默认为true）
    private MediaPlayer successPlay;
    private MediaPlayer errorPlayer;
    public boolean playBeep;
    public boolean vibrate;
    public static final String CAMERA_ERROR = "无法调取摄像头，请重试或检查权限";

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.capture_txt_cancle) {
            finish();
        } else if (vId == R.id.capture_txt_light) {
            try {
                if (txt_light.getText().toString().equals(getString(R.string.activity_scan_open))) {
                    txt_light.setText(getString(R.string.activity_scan_close));
                } else {
                    txt_light.setText(getString(R.string.activity_scan_open));
                }
                if (cameraManager != null)
                    cameraManager.switchFlashLight();
            } catch (Exception e) {
            }
        }
    }

    /**
     * (广播接收者.)
     * <h3>Version</h3>1.0
     * <h3>CreateTime</h3> 2016/5/17,18:56
     * <h3>UpdateTime</h3> 2016/5/17,18:56
     * <h3>CreateAuthor</h3>（Geoff）
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     */
    public class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
//                //电量的总刻度
//                int scale = intent.getIntExtra("scale", 100);
                // 小于20%的电量就关闭闪灯
                if (level > 0 && level < 20) {
                    if (isFirst) {
                        isFirst = false;
                        ToastUtils.showToastInCenter(context, getString(R.string.activity_scan_battery_low_msg));
                    }
                    txt_light.setTextColor(getResources().getColor(R.color.gray));
                    txt_light.setEnabled(false);

                } else {
                    txt_light.setTextColor(getResources().getColor(R.color.white));
                    txt_light.setEnabled(true);
                }
            }
        }

    }

    /**
     * 选择本地二维码图片---返回码
     */
    public static final int SELECT_PICTURE_CODE = 1;
    public static final int TYPE_SCAN = 1;                  // 扫描
    public static final int TYPE_LOAD_IMAGE = 2;            // 加载图片

    /**
     * 播放正确提示音，并震动
     */
    public void playBeepSoundAndVibrate() {
        if (playBeep && successPlay != null && !successPlay.isPlaying()) {
            LogOut.d(TAG, "播放声音");
            successPlay.start();
        }
//        TODO
//        if (playBeep && soundPool != null) {
//            soundPool.play(mSuccSoundID, .1f, .1f, 1, 0, 1);
//        }

        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 播放错误提示音，并震动
     */
    public void playErrorBeepSoundAndVibrate() {
        if (playBeep && errorPlayer != null && !errorPlayer.isPlaying()) {
            LogOut.d(TAG, "播放声音");
            errorPlayer.start();
        }

        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public boolean isAutoPlayBeepSound() {
        return mAutoPlayBeepSound;
    }

    public void setAutoPlayBeepSound(boolean autoPlayBeepSound) {
        mAutoPlayBeepSound = autoPlayBeepSound;
    }

    public void showError() {
        ToastUtils.showToastInCenter(this, CAMERA_ERROR);
    }

    /**
     * 在取消之前调用
     */
    protected void beforeCancel() {
    }

    public void initBeepSound() {
        if (playBeep && successPlay == null) {
            successPlay = MediaPlayer.create(this, R.raw.success_beep);
            errorPlayer = MediaPlayer.create(this, R.raw.error_beep);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int screenHeight = CameraUtil.screenHeight;
                if (event.getY() > screenHeight * 0.8) {
                    return true;
                }
                showFocusArea(event.getX(), event.getY());
                cameraManager.setFocus(event.getX(), event.getY());
                break;
        }
        return true;
    }

    private void showFocusArea(float pointX, float pointY) {
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
        layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
        focusIndex.setLayoutParams(layout);
        focusIndex.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);
        focusIndex.startAnimation(sa);

        if (handler == null) return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                focusIndex.setVisibility(View.INVISIBLE);
            }
        }, 700);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
//        recreate();
        LogOut.d("llw", "CaptureActivity页面更新了语言");
//        recreate();
        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase, getAppLanguage(newBase)));
    }

    private Locale getAppLanguage(Context context) {
        MultiLanguageUtil.init(context);
        return MultiLanguageUtil.getInstance().getLanguageLocale();
    }
}
