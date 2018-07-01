package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 个人信息
 * @date: 2018/6/10 12:14
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class PersonalInformationActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup mRg_gender;
    private RadioButton mRbMan;
    private RadioButton mRbFemale;
    private TextView mTvName;
    private ImageView mTvNameArrow;
    private TextView mTvPersonGender;
    private TextView mTvPersonHeight;
    private ImageView mTvHeightArrow;
    private TextView mTvPersonBodyWeight;
    private ImageView mTvBodyWeightArrow;
    private TextView mTvPersonDateBirth;
    private ImageView mIvDateBirth;
    private Button mBtnLogin;
    private TimePickerView mPvTime;

    @Override
    protected int setView() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void findViews() {
        //选择性别
        mRg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        //选中 男性
        mRbMan = (RadioButton) findViewById(R.id.rb_man);
        //女性
        mRbFemale = (RadioButton) findViewById(R.id.rb_female);
        //昵称
        mTvName = (TextView) findViewById(R.id.tv_person_name);
        mTvNameArrow = (ImageView) findViewById(R.id.tv_name_arrow);


        //性别
        mTvPersonGender = (TextView) findViewById(R.id.tv_person_gender);


        //身高
        mTvPersonHeight = (TextView) findViewById(R.id.tv_person_height);
        mTvHeightArrow = (ImageView) findViewById(R.id.tv_height_arrow);

        //体重
        mTvPersonBodyWeight = (TextView) findViewById(R.id.tv_person_body_weight);
        mTvBodyWeightArrow = (ImageView) findViewById(R.id.tv_body_weight_arrow);

        //出生日期
        mTvPersonDateBirth = (TextView) findViewById(R.id.tv_person_date_birth);
        mIvDateBirth = (ImageView) findViewById(R.id.iv_date_birth);

        //确定
        mBtnLogin = (Button) findViewById(R.id.btn_ok);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("个人信息");
        //时间选择器
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1950, 0, 1);
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTvPersonDateBirth.setText(DateTimeUtils.getTimeFromDate(date, "yyyy-MM-dd"));
            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("出生日期")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(getResources().getColor(R.color.text_content))//标题文字颜色
                .setSubmitColor(Color.parseColor("#26a8ff"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#26a8ff"))//取消按钮文字颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, selectedDate)//起始终止年月日设定
//                .setLabel("年", "月", "日")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
    }

    @Override
    protected void initEvent() {
        mRg_gender.setOnCheckedChangeListener(this);

        mTvName.setOnClickListener(this);
        mTvNameArrow.setOnClickListener(this);

        mTvPersonHeight.setOnClickListener(this);
        mTvHeightArrow.setOnClickListener(this);

        mTvPersonBodyWeight.setOnClickListener(this);
        mTvBodyWeightArrow.setOnClickListener(this);

        mTvPersonDateBirth.setOnClickListener(this);
        mIvDateBirth.setOnClickListener(this);


        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_man:
                mTvPersonGender.setText(getResources().getString(R.string.tv_man));
                break;

            case R.id.rb_female:
                mTvPersonGender.setText(getResources().getString(R.string.tv_female));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_person_name:
            case R.id.tv_name_arrow:
                //修改姓名
                BaseDialogHelper.showInputNameDialog(PersonalInformationActivity.this, "请输入您的昵称", new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {
                        mTvName.setText(l);
                    }
                });
                break;


            case R.id.tv_person_height:
            case R.id.tv_height_arrow:
                //修改身高
                BaseDialogHelper.showInputNumberDialog(PersonalInformationActivity.this, "请输入您的身高(单位:cm)", new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {
                        mTvPersonHeight.setText(l + " cm");
                    }
                });
                break;


            case R.id.tv_person_body_weight:
            case R.id.tv_body_weight_arrow:
                //修改体重
                BaseDialogHelper.showInputNumberDialog(PersonalInformationActivity.this, "请输入您的体重(单位:kg)", new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {
                        mTvPersonBodyWeight.setText(l + " kg");
                    }
                });
                break;

            case R.id.tv_person_date_birth:
            case R.id.iv_date_birth:
                //修改出生日期
                mPvTime.show();
                break;

            case R.id.btn_ok:
                //确定
                submitPersonInfos();
                break;
        }
    }

    private void submitPersonInfos() {
        BaseDialogHelper.showLoadingDialog(this, true, "正在上传...");
        IhyRequest.updateinfo(Constants.JSESSIONID, true, mTvPersonBodyWeight.getText().toString(), mTvPersonHeight.getText().toString(), "", new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                jumpToAddNewDevice();
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    /**
     * 跳转至添加新设备页面
     */
    private void jumpToAddNewDevice() {
        Intent intent = new Intent(this, AddNwedeviceActivity.class);
        startActivity(intent);
    }
}
