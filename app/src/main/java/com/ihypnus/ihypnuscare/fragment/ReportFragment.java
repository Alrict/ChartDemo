package com.ihypnus.ihypnuscare.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.SelectDateActivity;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.bean.HistogramData;
import com.ihypnus.ihypnuscare.controller.BaseController;
import com.ihypnus.ihypnuscare.controller.ChartsPage1Controller;
import com.ihypnus.ihypnuscare.controller.ChartsPage2Controller;
import com.ihypnus.ihypnuscare.controller.ChartsPage3Controller;
import com.ihypnus.ihypnuscare.controller.HomePageController;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 报告fragment
 * @date: 2018/5/28 18:34
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ReportFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, HomePageController.ChangeDateListener, ChartsPage1Controller.CharsDataChangedListener {
    private static final String TAG = "ReportFragment";
    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mPagerAdapter;
    private Animation mReLoadingAnim;
    private ImageView mIvRefresh;
    private ImageView mIvData;
    public static final int REQUEST_START_TIME = 200;
    public static final int RESPONSE_SELECT_OK = 202;
    private HomePageController mHomePageController;
    private ChartsPage1Controller mChartsPage1Controller;
    private ChartsPage2Controller mChartsPage2Controller;
    private ChartsPage3Controller mChartsPage3Controller;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private float mOldPositionOffset;
    private ArrayList<BaseController> mFragmentList;
    public static long sCurrentTime = System.currentTimeMillis();

    @Override
    protected int setView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_report;
    }

    @Override
    protected void findViews() {
        //头部刷新
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        //头部 日历
        mIvData = (ImageView) findViewById(R.id.iv_data);
        //viewPager
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);


    }


    @Override
    protected void init() {

        mFragmentList = new ArrayList<>();
        mHomePageController = new HomePageController(mAct);
        mChartsPage1Controller = new ChartsPage1Controller(mAct);
        mChartsPage2Controller = new ChartsPage2Controller(mAct);
//        mChartsPage3Controller = new ChartsPage3Controller(mAct);
        mFragmentList.add(mHomePageController);
        mFragmentList.add(mChartsPage1Controller);
        mFragmentList.add(mChartsPage2Controller);
//        mFragmentList.add(mChartsPage3Controller);
        mPagerAdapter = new VerticalPagerAdapter(mFragmentList);

        //设置最小偏移量
        mViewPager.setMinPageOffset(0.15f);
        mViewPager.setAdapter(mPagerAdapter);

        //头部刷新旋转动画
        mReLoadingAnim = AnimationUtils.loadAnimation(mAct, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mReLoadingAnim.setInterpolator(lin);
    }

    @Override
    protected void initEvent() {
        mIvData.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mHomePageController.setOnChangeDateListener(this);
        mChartsPage1Controller.setOnCharsDataChangedListener(this);
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_refresh:
                //刷新
                mIvRefresh.startAnimation(mReLoadingAnim);
                refreshCharsWeek();

                break;
            case R.id.iv_data:
                String currentDate = DateTimeUtils.getCurrentDate();
                jumpSelecteDateActivity("日期选择", currentDate, REQUEST_START_TIME);
                break;
        }
    }

    /**
     * 跳转选择日期
     *
     * @param title
     * @param time
     * @param requestCode
     */
    private void jumpSelecteDateActivity(String title, String time, int requestCode) {
        Intent intent = new Intent(mAct, SelectDateActivity.class);
        intent.putExtra("time", time);
        intent.putExtra("title", title);
        this.startActivityForResult(intent, requestCode);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("llw", "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Subscribe
    public void onEventMainThreadEvent(BaseFactory.RefreshReportInfoEvent event) {
        LogOut.d("llw", "刷新报告页面数据,设备id:" + event.getDeviceId());
        refreshCharsWeek();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_START_TIME && resultCode == RESPONSE_SELECT_OK) {
            if (null != data) {
                String time = initTime(data);
                long longTime = DateTimeUtils.getSimpleLongTime(time);
                if (longTime > System.currentTimeMillis()) {
                    ToastUtils.showToastDefault(mAct.getString(R.string.tv_toast_data_error));
                    return;
                }
                sCurrentTime = longTime;
                refreshCharsWeek();

            }
        }
    }

    /**
     * 刷新对应页面数据
     */
    private void refreshCharsWeek() {
        refreshListData(0);
        refreshListData(1);
        refreshListData(2);
        refreshListData(3);
    }

    private String initTime(Intent data) {
        int year = data.getIntExtra("year", -1);
        int month = data.getIntExtra("month", -1);
        int day = data.getIntExtra("day", -1);
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

    /**
     * 刷新指定页面的数据
     *
     * @param index
     */
    public void refreshListData(int index) {
        if (index < 0 || index > mFragmentList.size() - 1) {
            return;
        }
        mFragmentList.get(index).refreshData();
    }

    @Override
    public void onChangeDateListener() {
        //更新柱状图数据
        refreshCharsWeek();
    }

    /**
     * 刷新柱状图之后更新ui
     *
     * @param data
     */
    @Override
    public void onCharsDataChangedListener(HistogramData data) {
        mIvRefresh.clearAnimation();
        if (mChartsPage2Controller != null) {
            mChartsPage2Controller.updateUI(data.getTpInValues(), data.getTpExValues(), data.getAhiValues());
        }

        if (mChartsPage3Controller != null) {
            mChartsPage3Controller.updateUI(data.getTpInValues(), data.getAhiValues());
        }

    }

    /**
     * 获取数据失败回调
     *
     * @param errMsg
     */
    @Override
    public void onCharsDataChangedErrorListener(String errMsg) {
        mIvRefresh.clearAnimation();
        ToastUtils.showToastDefault(errMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
        LogOut.d("llw", "报告页面更新了语言");
        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateDataLanguage event) {
        if (mHomePageController != null) {
            mHomePageController.setData();
        }
        if (mChartsPage1Controller != null) {
            mChartsPage1Controller.setWeekDate();
        }
        if (mChartsPage2Controller != null) {
            mChartsPage2Controller.setWeekDate();
        }
    }
}
