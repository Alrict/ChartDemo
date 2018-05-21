/*
 * Copyright (C) 2010 ZXing authors
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

package com.ihypnus.zxing.android.decode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.zxing.android.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class DecodeHandler extends Handler {

    private static boolean isUseHybridBinarizer;

    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    public DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);

        } else if (message.what == R.id.quit) {
            running = false;
            Looper.myLooper().quit();

        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {

        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
        byte[] rotatedData = new byte[data.length];
        long start1 = System.currentTimeMillis();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        Log.i("qrcode", "旋转耗时 ：" + (System.currentTimeMillis() - start1));
        long start = System.currentTimeMillis();

        // 宽高也要调整
        int tmp = width;
        width = height;
        height = tmp;

        Log.i("decode", width + "");
        Log.i("decode", height + "");


        Result rawResult = null;
//        PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, size.width, size.height);
//        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData,
//                size.width, size.height, 0, 0, size.width, size.height, false);

        data = rotatedData;
        PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);

        if (source != null) {
            //如果觉得HybridBinarizer速度慢的话，可以替换算法，改成GlobalHistogramBinarizer
            //  BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            BinaryBitmap bitmap;
            if (isUseHybridBinarizer) {
                bitmap = new BinaryBitmap(new HybridBinarizer(source));
            } else {
                bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            }
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
                Log.i("qrcode", "解析成功 ：" + (System.currentTimeMillis() - start));
            } catch (ReaderException re) {
                // continue
                Log.e("qrcode", "解析失败 ：" + (System.currentTimeMillis() - start), re);
            } finally {
                multiFormatReader.reset();
            }
        }

        Handler handler = activity.getHandler();
        if (rawResult != null) {
            // Don't log the barcode contents for security.
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.decode_failed);
                message.sendToTarget();
            }
        }
    }
/*
    *//**
     * A factory method to build the appropriate LuminanceSource object based on
     * the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     *//*
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        // 扫码区域大小，可以调整扫码区域大小
        Rect rect = activity.getCropRect();
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect
                .height(), false);
    }*/
}
