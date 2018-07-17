package com.ihypnus.ihypnuscare.controller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.AddNwedeviceActivity;
import com.ihypnus.ihypnuscare.bean.DefaultDeviceIdVO;
import com.ihypnus.ihypnuscare.bean.UsageInfos;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.controller
 * @author: llw
 * @Description: 报告 首页
 * @date: 2018/6/21 14:31
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HomePageController extends BaseController implements View.OnClickListener {
    private TextView mTvData;
    private CircleProgressBarView mPb;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private ImageView mIvLastWeek;
    private ImageView mIvNextWeek;
    private TextView mTvHours;
    private TextView mTvMinues;
    private RelativeLayout mLayoutTime;
    private TextView mTvUsageLongData;
    private TextView mTvDeviceModel;
    private TextView mTvNhaleKpa;
    private TextView mTvExpirationKpa;
    private TextView mTvAverageAirLeak;
    private TextView mTvAhi;
    private List<UsageInfos.UsetimesBean> mUsetimes;
    private ChangeDateListener mChangeDateListener;

    public HomePageController(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_home_first, null);
        //日期
        mTvData = (TextView) rootView.findViewById(R.id.tv_data);
        //前一天
        mIvLastWeek = (ImageView) rootView.findViewById(R.id.iv_last_week);
        //后一天
        mIvNextWeek = (ImageView) rootView.findViewById(R.id.iv_next_week);
        //进度条
        mPb = (CircleProgressBarView) rootView.findViewById(R.id.pb);
        //周统计数据
        mLayoutWeekData = (LinearLayout) rootView.findViewById(R.id.layout_week_data);
        //周统计数据时间范围
        mTvDate = (TextView) rootView.findViewById(R.id.tv_date);


        //使用时长
        //小时
        mTvHours = (TextView) rootView.findViewById(R.id.tv_hours);
        //分钟
        mTvMinues = (TextView) rootView.findViewById(R.id.tv_minues);

        //使用时间段
        mLayoutTime = (RelativeLayout) rootView.findViewById(R.id.layout_time);
        mTvUsageLongData = (TextView) rootView.findViewById(R.id.tv_usage_long_data);

        //设备模式
        mTvDeviceModel = (TextView) rootView.findViewById(R.id.tv_device_model);

        //90%吸气压力
        mTvNhaleKpa = (TextView) rootView.findViewById(R.id.tv_nhale_kpa);
        //呼气压力
        mTvExpirationKpa = (TextView) rootView.findViewById(R.id.tv_expiration_kpa);
        //平均漏气
        mTvAverageAirLeak = (TextView) rootView.findViewById(R.id.tv_average_air_leak);
        //AHI
        mTvAhi = (TextView) rootView.findViewById(R.id.tv_ahi);

        return rootView;
    }

    @Override
    public void initData() {
        mIvLastWeek.setOnClickListener(this);
        mIvNextWeek.setOnClickListener(this);
        try {
            String currentDate = DateTimeUtils.getCurrentDate();
            String date = DateTimeUtils.date2Chinese(currentDate);
//            setDate(System.currentTimeMillis(),this);
            mTvData.setText(date);
        } catch (FormatException e) {
            e.printStackTrace();

        }

        mLayoutTime.setOnClickListener(this);


    }

    @Override
    public void loadData() {
        getDefaultDeviceInfos();
    }

    private void getDefaultDeviceInfos() {
        BaseDialogHelper.showLoadingDialog(mContext, true, "正在加载...");
        IhyRequest.getDefaultDeviceId(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                DefaultDeviceIdVO defaultDeviceIdVO = (DefaultDeviceIdVO) var1;
                if (defaultDeviceIdVO != null && !StringUtils.isNullOrEmpty(defaultDeviceIdVO.getDeviceId())) {
                    LogOut.d("llw", "获取默认设备id:" + defaultDeviceIdVO.getDeviceId());
                    Constants.DEVICEID = defaultDeviceIdVO.getDeviceId();

                    if (mChangeDateListener != null) {
                        mChangeDateListener.onChangeDateListener();
                    }
//                    loadDataByNet(getStartTime(currentTime), getEndTime(currentTime), null);

                } else {
                    BaseDialogHelper.showSimpleDialog(mContext, "温馨提示", "您目前未绑定任何相关设备", "前去绑定", new DialogListener() {
                        @Override
                        public void onClick(BaseType baseType) {
                            jumpToBindDeviceActivity();
                        }

                        @Override
                        public void onItemClick(long postion, String s) {

                        }
                    });
                }

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    /**
     * 跳转至绑定设备页面
     */
    private void jumpToBindDeviceActivity() {
        Intent intent = new Intent(mContext, AddNwedeviceActivity.class);
        mContext.startActivity(intent);
        Activity context = (Activity) mContext;
        context.finish();

    }

    @Override
    public void refreshData() {
        LogOut.d("llw", "报告页首页刷新");
        long date = ReportFragment.sCurrentTime;
        loadDataByNet(getStartTime(date), getEndTime(date), null);
        String dateTime = DateTimeUtils.getStringDateTime(date);
        String time = null;
        try {
            time = DateTimeUtils.date2Chinese(dateTime);
        } catch (FormatException e) {
            e.printStackTrace();
            time = "";
        }
        mTvData.setText(time);
    }


    private void loadDataByNet(String startTime, String endTime, final ImageView imageView) {
        BaseDialogHelper.showLoadingDialog(mContext, true, "正在加载...");
        IhyRequest.getEvents(Constants.JSESSIONID, Constants.DEVICEID, startTime, endTime, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                UsageInfos infos = (UsageInfos) var1;
                if (infos != null) {
                    UsageInfos.LeakBean leak = infos.getLeak();
                    UsageInfos.EventsBean events = infos.getEvents();
                    setViews(infos);

                }
                if (imageView != null) {
                    imageView.clearAnimation();
                }

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
                if (imageView != null) {
                    imageView.clearAnimation();
                }
            }
        });
    }

    /**
     * 设置视图
     *
     * @param usageInfos
     */
    private void setViews(UsageInfos usageInfos) {
        float score = (float) usageInfos.getScore();
        startAni(score);

        UsageInfos.EventsBean events = usageInfos.getEvents();
        UsageInfos.UseInfoBean useInfo = usageInfos.getUseInfo();
        //平均使用时长
        int useseconds = useInfo.getUseseconds();
        if (useseconds <= 0) {
            mTvHours.setText("--");
            mTvMinues.setText("--");
            mTvUsageLongData.setText("--~--");
            mTvDeviceModel.setText("--");
            mTvNhaleKpa.setText("--");
            mTvExpirationKpa.setText("--");
            mTvAverageAirLeak.setText("--");
            mTvAhi.setText("--");

        } else {
            //使用时间段
            mUsetimes = usageInfos.getUsetimes();
            int hours = useseconds / 3600;
            int minues = (useseconds - hours * 3600) / 60;
            mTvHours.setText(String.valueOf(hours));
            mTvMinues.setText(String.valueOf(minues));
            if (mUsetimes.size() > 0) {
                String starttime = mUsetimes.get(0).getStarttime();
                String endTime = mUsetimes.get(0).getEndTime();
                if (starttime.length() >= 16 && endTime.length() >= 16) {
                    starttime = starttime.substring(11, 16);
                    endTime = endTime.substring(11, 16);
                    mTvUsageLongData.setText(starttime + "~" + endTime);
                } else {
                    mTvUsageLongData.setText("--~--");
                }
            }

            UsageInfos.UseParamsBean useParams = usageInfos.getUseParams();
            if (useParams != null) {
                int mode = useParams.getMode();
                String modeName = getModelName(mode);
                //设备模式
                mTvDeviceModel.setText(modeName);
            }

            String nHaleKpa;
            String expirationKpa;
            String averageLeakVolume;
            String ahi = "";

            UsageInfos.PressureBean pressure = usageInfos.getPressure();
            if (pressure != null) {
                nHaleKpa = String.valueOf(pressure.getTpIn()) + "cmH2O";
                expirationKpa = String.valueOf(pressure.getTpEx()) + "cmH2O";
            } else {
                nHaleKpa = "--";
                expirationKpa = "--";
            }
            mTvNhaleKpa.setText(nHaleKpa);
            mTvExpirationKpa.setText(expirationKpa);

            UsageInfos.LeakBean leak = usageInfos.getLeak();
            if (leak != null) {
                averageLeakVolume = leak.getAverageLeakVolume() + "L/min";
            } else {
                averageLeakVolume = "--";
            }
            mTvAverageAirLeak.setText(averageLeakVolume);

            if (events != null) {
                ahi = events.getAhi();
            }
            mTvAhi.setText(StringUtils.isNullOrEmpty(ahi) ? "--" : String.valueOf(ahi));
        }

    }

    private String getModelName(int mode) {
        //        设备模式 0 --”CPAP”，1--”APAP”, 2--"BPAP-S", 3--"AutoBPAP-S", 4--"BPAP-T",
        // 5--"BPAP-ST"   100 -- “No Data”  200--”多种模式”需要读取参数 “modeInfo”，
        // 根据邝勇最新需求，当放回值为200时，默认显示最长使用时间段的模式，也就是从usetimes中最长的那条读取”mode”显示出来
        String modeName = "";
        switch (mode) {
            case 0:
                modeName = "CPAP";
                break;

            case 1:
                modeName = "APAP";
                break;

            case 2:
                modeName = "BPAP-S";
                break;

            case 3:
                modeName = "AutoBPAP-S";
                break;

            case 4:
                modeName = "BPAP-T";
                break;

            case 5:
                modeName = "BPAP-ST";
                break;

            case 100:
                modeName = "No Data";
                break;

            case 200:
                if (mUsetimes != null && mUsetimes.size() > 0) {
                    UsageInfos.UsetimesBean usetimesBean = mUsetimes.get(mUsetimes.size() - 1);
                    int mode1 = usetimesBean.getMode();
                    if (mode1 != 200) {
                        modeName = getModelName(mode1);
                    }
                } else {
                    modeName = "未知";
                }
                break;
        }
        return modeName;
    }

    @Override
    public void onDestroy() {

    }

    private void startAni(float sweep) {
        if (sweep == -1) {
            mPb.setProgress(sweep);
        } else {
            ObjectAnimator a = ObjectAnimator.ofFloat(mPb, "progress", 0f, sweep);
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            a.setDuration(3000);
            a.start();
        }

    }

    public void refreshDatas(String date) {
        mTvData.setText(date);
        long currentTime = ReportFragment.sCurrentTime;
        loadDataByNet(getStartTime(currentTime), getEndTime(currentTime), null);
        //刷新柱状图数据

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.iv_last_week:
                updateData(-1);

                break;

            case R.id.iv_next_week:
                updateData(1);
                break;

            case R.id.layout_time:
//               使用时间段
                showPopDialog(mUsetimes);
                break;
        }
    }

    /**
     * 显示使用时间段
     *
     * @param usetimes
     */
    private void showPopDialog(List<UsageInfos.UsetimesBean> usetimes) {
        if (usetimes == null || usetimes.size() == 0) {
            return;
        }
        BaseDialogHelper.showListDialog(mContext, "使用时段", "返回", usetimes);
//        BaseDialogHelper.showMsgTipDialog(mContext, "显示使用时间段");
    }

    /**
     * @param type -1 前一天 1 后一天
     */
    private void updateData(int type) {
        long date = ReportFragment.sCurrentTime;
        long lastDay = date + (24L * 60L * 60L * 1000L * type);
        if (lastDay > System.currentTimeMillis()) {
            ToastUtils.showToastDefault("查询日期超出范围");
            return;
        }
        ReportFragment.sCurrentTime = lastDay;
        String dateByTime = DateTimeUtils.getStringDateByMounthDay(lastDay);
        LogOut.d("llw", "当前日期:" + dateByTime);

        if (mChangeDateListener != null) {
            mChangeDateListener.onChangeDateListener();
        }

    }

    public void reLoad(ImageView imageView) {
        long currentTime = ReportFragment.sCurrentTime;
        loadDataByNet(getStartTime(currentTime), getEndTime(currentTime), imageView);
    }

    public interface ChangeDateListener {
        void onChangeDateListener();
    }

    public void setOnChangeDateListener(ChangeDateListener listener) {
        mChangeDateListener = listener;
    }

}
