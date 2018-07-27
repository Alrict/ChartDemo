package com.ihypnus.ihypnuscare.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.ClipActivity;
import com.ihypnus.ihypnuscare.activity.PersonalInformationActivity;
import com.ihypnus.ihypnuscare.activity.SettingActivity;
import com.ihypnus.ihypnuscare.bean.OssTokenVO;
import com.ihypnus.ihypnuscare.bean.PersonMesVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.ImageUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.TakePhotosUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.kye.smart.multi_image_selector_library.MultiImageSelector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 我的fragment
 * @date: 2018/5/28 18:35
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyIhyFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mLayoutPersonInfo;
    private RelativeLayout mLayoutHelp;
    private RelativeLayout mLayoutSuggestions;
    private RelativeLayout mLayoutSettings;
    private CircleImageView mCircleImageView;
    private File mOutputImageFile;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CLIP = 3;
    private Bitmap mAvatarBitmap;
    private ImageView mIvDefaultPhoto;
    private static final String bucketName = "hypnus-app-resource";
    private static final String sObjectKeyPath = "app_header_image/photo_";
    private OSSClient mOssClient;
    private boolean mIsFirst = true;
    private byte[] imageByteArray;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mDownLoadPic != null) {
                        mCircleImageView.setImageBitmap(mDownLoadPic);
                        mIvDefaultPhoto.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if (imageByteArray != null) {
                        mDownLoadPic = getPicFromBytes(imageByteArray, null);
                        if (mDownLoadPic != null) {
                            mCircleImageView.setImageBitmap(mDownLoadPic);
                            mIvDefaultPhoto.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    };
    private Bitmap mDownLoadPic;
    private TextView mTvUserName;
    private PersonMesVO mPersonMesVO;

    @Override
    protected int setView() {
        return R.layout.fragment_my_ihy;
    }

    @Override
    protected void findViews() {
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mCircleImageView = (CircleImageView) findViewById(R.id.avatar);
        mIvDefaultPhoto = (ImageView) findViewById(R.id.iv_default_photo);
        mLayoutPersonInfo = (RelativeLayout) findViewById(R.id.layout_person_info);
        mLayoutHelp = (RelativeLayout) findViewById(R.id.layout_help);
        mLayoutSuggestions = (RelativeLayout) findViewById(R.id.layout_suggestions);
        mLayoutSettings = (RelativeLayout) findViewById(R.id.layout_settings);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initEvent() {
        mCircleImageView.setOnClickListener(this);
        mLayoutPersonInfo.setOnClickListener(this);
        mLayoutHelp.setOnClickListener(this);
        mLayoutSuggestions.setOnClickListener(this);
        mLayoutSettings.setOnClickListener(this);

    }

    @Override
    protected void loadData() {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在加载");
//        mTvUserName.setText(IhyApplication.mInstance.getUserInfo().getUserInfo().getAccount());
        getInfos(true);
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.layout_person_info:
                jumpToActivity(101, PersonalInformationActivity.class);
                break;

            case R.id.layout_help:
                BaseDialogHelper.showMsgTipDialog(mAct, getString(R.string.tv_dialog_model_close));
//                jumpToActivity(102, HelpCenterActivity.class);
                break;

            case R.id.layout_suggestions:
                BaseDialogHelper.showMsgTipDialog(mAct, getString(R.string.tv_dialog_model_close));
//                jumpToActivity(103, FeedbackActivity.class);
                break;

            case R.id.layout_settings:
                jumpToActivity(104, SettingActivity.class);
                break;
            case R.id.avatar:
                //头像
                //检测相机拍照和存储权限
                checkPermissions();
                break;
        }

    }

    /**
     * 相机拍照及存储权健检测
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int photoPermission = ContextCompat.checkSelfPermission(mAct, Manifest.permission.CAMERA);
            int readPermission = ContextCompat.checkSelfPermission(mAct, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (photoPermission != PackageManager.PERMISSION_GRANTED
                    || readPermission != PackageManager.PERMISSION_GRANTED
                    || writePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mAct,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            } else {
                createSelectDialog();
            }
        } else {
            createSelectDialog();
        }
    }

    /**
     * 拍照或相册选择
     */
    private void createSelectDialog() {

        WindowManager wm = (WindowManager) mAct.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        View dialogview = LayoutInflater.from(mAct).inflate(R.layout.dialog_select, null);
        final Dialog selectDialog = new Dialog(mAct, R.style.upload_image_methods_dialog);
        selectDialog.setContentView(dialogview);
        Window window = selectDialog.getWindow();
        WindowManager.LayoutParams dialogParams = window.getAttributes();
        dialogParams.gravity = Gravity.BOTTOM;
        dialogParams.width = width;
        window.setAttributes(dialogParams);

        // 相机拍照
        dialogview.findViewById(R.id.get_from_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查相机权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestCameraPermission();
                } else {
                    //拍照
                    mOutputImageFile = TakePhotosUtils.takePicture(mAct, REQUEST_CAMERA);
                }
                selectDialog.dismiss();
            }
        });

        // 从相册中选择
        dialogview.findViewById(R.id.get_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector selector = MultiImageSelector.create();
                selector.showCamera(false);
                selector.count(1);
                selector.multi();
                selector.origin(null);
                selector.start(mAct, REQUEST_GALLERY);
                selectDialog.dismiss();
            }
        });
        // 取消
        dialogview.findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
            }
        });
        selectDialog.show();
    }


    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(mAct, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mAct, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            mOutputImageFile = TakePhotosUtils.takePicture(mAct, REQUEST_CAMERA);
        }
    }

    private void jumpToActivity(int requestCode, Class<?> cls) {
        Intent intent = new Intent(mAct, cls);
        if (requestCode == 101 && mPersonMesVO != null) {
            intent.putExtra("PERSON_MSG", mPersonMesVO);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    //个人资料
                    getInfos(false);
                    break;

                case 102:
                    //帮助中心

                    break;

                case 103:
                    //意见反馈

                    break;

                case 104:
                    //设置

                    break;
                case REQUEST_CAMERA:
                    if (mOutputImageFile == null || !mOutputImageFile.exists()) {
                        return;
                    }

                    Intent clipIntent = new Intent(mAct, ClipActivity.class);
                    clipIntent.putExtra("data", mOutputImageFile.getAbsolutePath());
                    startActivityForResult(clipIntent, REQUEST_CLIP);
                    break;
                case REQUEST_GALLERY:
                    ArrayList<String> selectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    Intent intent = new Intent(mAct, ClipActivity.class);
                    if (selectPath != null && selectPath.size() > 0) {
                        intent.putExtra("data", selectPath.get(0));
                    }
                    startActivityForResult(intent, REQUEST_CLIP);
                    break;
                case REQUEST_CLIP:
                    if (data != null) {
                        String objectKeyPath = "";
                        String imagePath = data.getStringExtra("images_path");
                        mAvatarBitmap = ImageUtils.getBitmap(imagePath);
                        mCircleImageView.setImageBitmap(mAvatarBitmap);
                        mIvDefaultPhoto.setVisibility(View.GONE);
                        if (mPersonMesVO != null) {
                            String account = mPersonMesVO.getAccount();
                            objectKeyPath = sObjectKeyPath + account;
                        }
                        compressImage(imagePath, objectKeyPath);
                    }

                    break;
            }
        }
    }

    /**
     * 压缩图片
     *
     * @param imagePath
     * @param objectKeyPath
     */
    private void compressImage(final String imagePath, final String objectKeyPath) {
        Luban.with(mAct)
                .load(imagePath)//传入原图
                .ignoreBy(100)//不压缩的阈值，单位为K
                .setTargetDir(getPath())//缓存压缩图片路径
                .filter(new CompressionPredicate() {//设置开启压缩条件
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        showResult(new File(imagePath), file);
                        uploadUserPhoto(bucketName, objectKeyPath, file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    /**
     * 获取缓存目录
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/hypnus/image/cache/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 打印压缩前后的文件大小
     *
     * @param file1
     * @param file2
     */
    private void showResult(File file1, File file2) {
        int[] originSize = computeSize(file1);
        int[] thumbSize = computeSize(file2);
        String originArg = String.format(Locale.CHINA, "原图参数：%d*%d, %dk", originSize[0], originSize[1], file1.length() >> 10);
        String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file2.length() >> 10);
        LogOut.d("llw", originArg);
        LogOut.d("llw", thumbArg);
    }

    /**
     * 计算文件大小
     *
     * @param srcImg
     * @return
     */
    private int[] computeSize(File srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg.getAbsolutePath(), options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;

        return size;
    }

    /**
     * 申请权限的回调，
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults 多个权限一起返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意相机权限
                //拍照
                mOutputImageFile = TakePhotosUtils.takePicture(mAct, REQUEST_CAMERA);
            } else {
                //拒接,再次申请权限
                requestCameraPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 上传用户头像
     *
     * @param bucketName     oss储存区域的名字
     * @param objectKey      保存在oss的文件名
     * @param uploadFilePath 被上传文件的路径
     */
    private void uploadUserPhoto(String bucketName, String objectKey, String uploadFilePath) {
        if (mOssClient == null) return;
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("llw", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = mOssClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("llw", "UploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("llw", "oss onFailure ErrorCode:" + serviceException.getErrorCode());
                    Log.e("llw", "oss onFailure RequestId:" + serviceException.getRequestId());
                    Log.e("llw", "oss onFailure HostId:" + serviceException.getHostId());
                    Log.e("llw", "oss onFailure RawMessage:" + serviceException.getRawMessage());
                }
            }
        });
    }

    /*
     * 从oss下载
     */
    private void downLoadUserPhoto(String bucketName, String objectKey) {
        if (mOssClient == null) return;
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);
        OSSAsyncTask task = mOssClient.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                Log.d("llw", "oss图片加载成功,size:" + (result.getContentLength() / 1024) + "KB");
                InputStream inputStream = result.getObjectContent();
                byte[] buffer = new byte[2048];
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int len;
                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 处理下载的数据
                        outStream.write(buffer, 0, len);
                        Log.d("asyncGetObjectSample", "read length: " + len);
                    }

                    outStream.flush();
                    imageByteArray = outStream.toByteArray();
                    outStream.close();


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(2);
                    }
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("llw", "ErrorCode:" + serviceException.getErrorCode());
                    Log.e("llw", "RequestId:" + serviceException.getRequestId());
                    Log.e("llw", "HostId:" + serviceException.getHostId());
                    Log.e("llw", "RawMessage:" + serviceException.getRawMessage());
                }
            }
        });
// task.cancel(); // 可以取消任务
// task.waitUntilFinished(); // 如果需要等待任务完成

    }


    /**
     * 获取用户个人信息
     *
     * @param b 是否需要更新用户头像
     */
    public void getInfos(final boolean b) {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在加载");
        IhyRequest.getinfos(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                mPersonMesVO = (PersonMesVO) var1;
                if (mPersonMesVO != null) {
                    mTvUserName.setText(mPersonMesVO.getAccount());
                    if (b) {
                        getStsToken();
                    }
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }


    private void getStsToken() {
        IhyRequest.getSTSToken(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                OssTokenVO tokenVO = (OssTokenVO) var1;
                String accessKeyId = tokenVO.getAccessKeyId();
                String accessKeySecret = tokenVO.getAccessKeySecret();
                String expiration = tokenVO.getExpiration();
                String securityToken = tokenVO.getSecurityToken();
                String statusCode = tokenVO.getStatusCode();
                initOSSClient(accessKeyId, accessKeySecret, securityToken);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void initOSSClient(String accessKeyId, String scretKeyId, String securityToken) {
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
        OSSStsTokenCredentialProvider ossStsTokenCredentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, scretKeyId, securityToken);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(1); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOssClient = new OSSClient(mAct.getApplicationContext(), endpoint, ossStsTokenCredentialProvider);
        LogOut.d("llw", "初始化");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String objectKeyPath = "";
                if (mPersonMesVO != null) {
                    objectKeyPath = sObjectKeyPath + mPersonMesVO.getAccount();
                } else {
                    objectKeyPath = sObjectKeyPath;
                }
                LogOut.d("llw", "objectKeyPath:" + objectKeyPath);
                downLoadUserPhoto(bucketName, objectKeyPath);
            }
        }).start();

    }


    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } else {
            Log.e("llw", "bad");
        }
        return null;

    }
}
