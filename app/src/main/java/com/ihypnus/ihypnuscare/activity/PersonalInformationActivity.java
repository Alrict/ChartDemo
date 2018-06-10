package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

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
        mBtnLogin = (Button) findViewById(R.id.btn_login);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("个人信息");
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
            case R.id.tv_person_height:
            case R.id.tv_name_arrow:
                //修改姓名

                break;

            case R.id.tv_person_name:
            case R.id.tv_height_arrow:
                //修改身高

                break;


            case R.id.tv_person_body_weight:
            case R.id.tv_body_weight_arrow:
                //修改体重

                break;

            case R.id.tv_person_date_birth:
            case R.id.iv_date_birth:
                //修改出生日期

                break;

            case R.id.btn_login:
                //确定
//                jumpToAddNewDevice();
                finish();
                break;
        }
    }

    /**
     * 跳转至添加新设备页面
     */
    private void jumpToAddNewDevice() {
        Intent intent = new Intent(this, AddNwedeviceActivity.class);
        startActivity(intent);
    }
}
