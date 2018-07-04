package com.ihypnus.ihypnuscare.controller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.nfc.FormatException;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.UsageInfos;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;

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
    private TextView mLayoutTime;
    private TextView mTvUsageLongData;
    private TextView mTvDeviceModel;
    private TextView mTvNhaleKpa;
    private TextView mTvExpirationKpa;
    private TextView mTvAverageAirLeak;
    private TextView mTvAhi;

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
        mLayoutTime = (TextView) rootView.findViewById(R.id.layout_time);
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
            setDate(System.currentTimeMillis());
            mTvData.setText(date);
        } catch (FormatException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void loadData() {
        loadDataByNet(getStartTime(), getEndTime(), null);
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
        float score = usageInfos.getScore();
        startAni(score);
        UsageInfos.EventsBean events = usageInfos.getEvents();
        UsageInfos.UseInfoBean useInfo = usageInfos.getUseInfo();
        int averageUseTime = useInfo.getAverageUseTime();
//        mTvHours.setText();
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
        loadDataByNet(getStartTime(), getEndTime(), null);
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
        }
    }

    /**
     * @param type -1 前一天 1 后一天
     */
    private void updateData(int type) {
        long date = getDate();
        long lastDay = date + ((24L * 60L * 60L * 1000L) * type);
        String dateByTime = DateTimeUtils.getStringDateByMounthDay(lastDay);
        LogOut.d("llw", "当前日期:" + dateByTime);
        try {
            String dayLast = DateTimeUtils.date2Chinese(dateByTime);
            setDate(lastDay);
            refreshDatas(dayLast);
        } catch (FormatException e) {
            e.printStackTrace();

        }
    }

    public void reLoad(ImageView imageView) {
        loadDataByNet(getStartTime(), getEndTime(), imageView);
    }
}
