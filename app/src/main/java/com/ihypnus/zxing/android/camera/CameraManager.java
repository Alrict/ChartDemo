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

package com.ihypnus.zxing.android.camera;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.ihypnus.zxing.android.camera.open.OpenCameraInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    private final Context context;
    private final CameraConfigurationManager configManager;
    private static CameraManager cameraManager;
    /**
     * Preview frames are delivered here, which we pass on to the registered
     * handler. Make sure to clear the handler so it will only receive one
     * message.
     */
    private final PreviewCallback previewCallback;
    private Camera camera;
    private AutoFocusManager autoFocusManager;
    private boolean initialized;
    private boolean previewing;
    private int requestedCameraId = CAMERA_FACING_BACK;

    private float focusAreaSize = 150;
    private boolean isReleased;

    public CameraManager(Context context) {
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
        configManager.setCameraId(requestedCameraId);
        previewCallback = new PreviewCallback(configManager);
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder The surface object which the camera will draw preview frames
     *               into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = camera;
        if (theCamera == null) {

            if (requestedCameraId >= 0) {
                theCamera = OpenCameraInterface.open(requestedCameraId);
            } else {
                theCamera = OpenCameraInterface.open();
            }

            if (theCamera == null) {
                throw new IOException();
            }
            camera = theCamera;
        }
        theCamera.setPreviewDisplay(holder);

        isReleased = false;
        if (!initialized) {
            initialized = true;
            configManager.initFromCameraParameters(theCamera);
        }

        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save
        // these,
        // temporarily
        try {
            configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

    }

    /**
     * Gets the CameraManager singleton instance.
     *
     * @return A reference to the CameraManager singleton.
     */
    public static CameraManager get() {
        return cameraManager;
    }

    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            isReleased = true;
            camera = null;
            // Make sure to clear these each time we close the camera, so that
            // any scanning rect
            // requested by intent is forgotten.
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public synchronized void startPreview() {
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
//            autoFocusManager = new AutoFocusManager(context, camera);
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data
     * will arrive as byte[] in the message.obj field, with width and height
     * encoded as message.arg1 and message.arg2, respectively.
     *
     * @param handler The handler to send the message to.
     * @param message The what field of the message to be sent.
     */
    public synchronized void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = camera;
        if (theCamera != null && previewing) {
            previewCallback.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(previewCallback);
        }
    }

    /**
     * Allows third party apps to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means
     *                 "no preference".
     */
    public synchronized void setManualCameraId(int cameraId) {
        requestedCameraId = cameraId;
    }

    /**
     * 获取相机分辨率
     *
     * @return
     */
    public Size getCameraResolution() {
        return configManager.getCameraResolution();
    }

    public Size getPreviewSize() {
        if (null != camera) {
            return camera.getParameters().getPreviewSize();
        }
        return null;
    }

    public void switchFlashLight() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (parameter != null) {
                if (parameter.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                } else {
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }

                camera.setParameters(parameter);
            }
        }
    }

    public void setFocus(final float x, final float y) {
        try {
            if (camera == null) return;
            camera.cancelAutoFocus();// 取消自动聚焦
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getMaxNumFocusAreas() > 0) {
                ArrayList<Camera.Area> area = new ArrayList<>();
                Rect focusRect = calculateTapArea(x, y);
                if (focusRect == null) return;
                area.add(new Camera.Area(focusRect, 800));
                parameters.setMeteringAreas(area);
            }
            final String currentFocusMode = parameters.getFocusMode();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//        setAutoFocusInternal(parameters);
            camera.setParameters(parameters);

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                    if (success && !isReleased) {
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFocusMode(currentFocusMode);
                        camera.setParameters(parameters);
                    } else {
                        setFocus(x, y);
                    }
                }
            });
        } catch (Exception e) {
//            CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
        }
    }

    public void setAutoFocusInternal(Camera.Parameters parameters) {
        final List<String> modes = parameters.getSupportedFocusModes();
        if (modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        } else {
            parameters.setFocusMode(modes.get(0));
        }
    }

    /**
     * 获得聚焦区域
     *
     * @param x
     * @param y
     * @return
     */
    private Rect calculateTapArea(float x, float y) {
//        Log.i(TAG, "x: " + x + " y: " + y);
        int areaSize = Float.valueOf(focusAreaSize * 1.0f).intValue();
        int centerX = (int) ((x / CameraUtil.screenWidth) * 2000 - 1000);
        int centerY = (int) ((y / CameraUtil.screenHeight) * 2000 - 1000);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);
//        Log.i(TAG, "left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
        RectF rectF = new RectF(left, top, right, bottom);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on the format
     * of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
      /*  Rect rect = getFramingRectInPreview();
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                rect.width(), rect.height(), false);*/
        // 直接返回整幅图像的数据，而不计算聚焦框大小。
        return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
    }
}
