package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.PersonMesVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

    //    private RadioGroup mRg_gender;
//        private RadioButton mRbMan;
//    private RadioButton mRbFemale;
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
    private int mType;
    private String mKg;
    private String mHeight;
    private String mGender = "2";
    private int mHeigthIndex = 100;
    private int mWeightIndex = 50;
    private ImageView mTvGenderArrow;
    private ArrayList<String> mList;
    private int mGenderIndex = 1;
    private ImageView mIvGender;

    @Override
    protected int setView() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void findViews() {
        //选择性别
        mIvGender = (ImageView) findViewById(R.id.iv_gender);
//        mRg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        //选中 男性
//        mRbMan = (RadioButton) findViewById(R.id.rb_man);
        //女性
//        mRbFemale = (RadioButton) findViewById(R.id.rb_female);
        //帐号
        mTvName = (TextView) findViewById(R.id.tv_person_name);
        mTvNameArrow = (ImageView) findViewById(R.id.tv_name_arrow);


        //性别
        mTvPersonGender = (TextView) findViewById(R.id.tv_person_gender);
        mTvGenderArrow = (ImageView) findViewById(R.id.tv_gender_arrow);
//        mTvPersonGender = (TextView) findViewById(R.id.tv_person_gender);


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
        setTitle(getString(R.string.tv_person_msg));
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
                .setCancelText(getString(R.string.cancel))//取消按钮文字
                .setSubmitText(getString(R.string.ok))//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText(getString(R.string.tv_text_birthday))//标题文字
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

        //性别
        mList = new ArrayList<>();
        String[] gender = getResources().getStringArray(R.array.gender);
        mList.addAll(Arrays.asList(gender));
    }

    @Override
    protected void initEvent() {
        mTvPersonGender.setOnClickListener(this);
        mTvGenderArrow.setOnClickListener(this);
//        mRg_gender.setOnCheckedChangeListener(this);

//        mTvName.setOnClickListener(this);
//        mTvNameArrow.setOnClickListener(this);

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
        Intent intent = getIntent();
        mType = intent.getIntExtra("TYPE", 0);
        String account = intent.getStringExtra("ACCOUNT");
        LogOut.d("llw001", "个人信息页面接受到type:" + mType + ",account:" + account);
        PersonMesVO personMsgBean = intent.getParcelableExtra("PERSON_MSG");
        if (personMsgBean == null && mType == 1 && !StringUtils.isNullOrEmpty(account)) {
            //注册界面 跳转过来的
            mTvName.setText(account);
//            getInfos();
        } else if (personMsgBean != null) {
            bindView(personMsgBean);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
//            case R.id.rb_man:
//                mTvPersonGender.setText(getResources().getString(R.string.tv_man));
//                mGender = "1";
//                break;
//
//            case R.id.rb_female:
//                mTvPersonGender.setText(getResources().getString(R.string.tv_female));
//                mGender = "2";
//                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {


            //性别
            case R.id.tv_person_gender:
            case R.id.tv_gender_arrow:
                BaseDialogHelper.showArrayWheelDialog(PersonalInformationActivity.this, mGenderIndex, getString(R.string.tv_title_select_gender), mList, new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {

                    }

                    @Override
                    public void onNumberSelectListener(final int l) {
                        mGenderIndex = l;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGender = (l == 0 ? "1" : "2");
                                mIvGender.setImageDrawable(l == 0 ? getResources().getDrawable(R.mipmap.icon_man_unchecked) : getResources().getDrawable(R.mipmap.icon_female_unchecked));
                                mTvPersonGender.setText(mList.get(l));
                            }
                        });

                    }
                });

                break;

            case R.id.tv_person_height:
            case R.id.tv_height_arrow:
                //修改身高

                BaseDialogHelper.showNumberWheelDialog(PersonalInformationActivity.this, 50, 250, mHeigthIndex, getString(R.string.tv_tip_yout_heigth), new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {

                    }

                    @Override
                    public void onNumberSelectListener(int l) {
                        mHeigthIndex = l - 50;
                        mTvPersonHeight.setText(String.format("%s cm", l));
                    }
                });
                break;


            case R.id.tv_person_body_weight:
            case R.id.tv_body_weight_arrow:
                //修改体重
//                BaseDialogHelper.showInputNumberDialog(PersonalInformationActivity.this, getString(R.string.tv_tip_your_weigth), getString(R.string.tv_input_your_weigth), new BaseDialogHelper.NumberInputListener() {
//                    @Override
//                    public void onNumberInputListener(String l) {
//                        mTvPersonBodyWeight.setText(String.format("%s kg", l));
//                    }
//                });

                BaseDialogHelper.showNumberWheelDialog(PersonalInformationActivity.this, 10, 200, mWeightIndex, getString(R.string.tv_tip_your_weigth), new BaseDialogHelper.NumberInputListener() {
                    @Override
                    public void onNumberInputListener(String l) {

                    }

                    @Override
                    public void onNumberSelectListener(int l) {
                        mWeightIndex = l - 10;
                        mTvPersonBodyWeight.setText(String.format("%s kg", l));
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
        String weight = mTvPersonBodyWeight.getText().toString().trim();
        mHeight = mTvPersonHeight.getText().toString().trim();
        String birthday = mTvPersonDateBirth.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(mHeight)) {
            ToastUtils.showToastDefault(getString(R.string.tv_tip_yout_heigth));
            return;
        }
        if (StringUtils.isNullOrEmpty(weight)) {
            ToastUtils.showToastDefault(getString(R.string.tv_input_your_weigth));
            return;
        }

        if (StringUtils.isNullOrEmpty(birthday)) {
            ToastUtils.showToastDefault(getString(R.string.tv_input_your_birthday));
            return;
        }
        double bmi = getBMI();

        String nickname = mTvName.getText().toString().trim();
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
        IhyRequest.updateinfo(Constants.JSESSIONID, true, nickname, mGender, birthday, mKg, mHeight, String.valueOf(bmi), new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                if (mType == 1) {
                    jumpToHomeActivity();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    /**
     * 跳转至报告页面
     */
    private void jumpToHomeActivity() {
        LogOut.d("llw001", "跳转到报告页面");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void getInfos() {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
        IhyRequest.getinfos(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                PersonMesVO personMesVO = (PersonMesVO) var1;
                if (personMesVO != null) {
                    bindView(personMesVO);
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    private void bindView(PersonMesVO personMesVO) {
        mTvName.setText(personMesVO.getAccount());
        String weight = personMesVO.getWeight();
        if (StringUtils.isNullOrEmpty(weight)) {
            mTvPersonBodyWeight.setText("");
        } else {
            int i = Integer.parseInt(weight);
            if (i >= 10) {
                mWeightIndex = i - 10;
            }
            mTvPersonBodyWeight.setText(String.format("%s kg", personMesVO.getWeight()));
        }
        String gender = personMesVO.getGender();
//        mRbFemale.setChecked(gender.equals("1"));
        if (gender.equals("1")) {
            mGenderIndex = 0;
            mIvGender.setImageDrawable(getResources().getDrawable(R.mipmap.icon_man_unchecked));
            mTvPersonGender.setText(getString(R.string.tv_man));
        } else {
            mGenderIndex = 1;
            mIvGender.setImageDrawable(getResources().getDrawable(R.mipmap.icon_female_unchecked));
            mTvPersonGender.setText(getString(R.string.tv_female));
        }

        String height = personMesVO.getHeight();
        if (StringUtils.isNullOrEmpty(height)) {
            mTvPersonHeight.setText("");
        } else {
            int i = Integer.parseInt(height);
            if (i >= 50) {
                mHeigthIndex = i - 50;
            }
            mTvPersonHeight.setText(String.format("%s cm", personMesVO.getHeight()));
        }
        mTvPersonDateBirth.setText(personMesVO.getBirthday());
    }

    /**
     * 计算BMI体脂
     *
     * @return
     */
    public double getBMI() {
        double weightKg;
        double heightCm;
        double bmi;
        String weight = mTvPersonBodyWeight.getText().toString().trim();
        int i1 = weight.indexOf(" kg");
        if (i1 == -1) {
            i1 = 0;
        }
        mKg = weight.substring(0, i1);
        try {
            weightKg = Double.parseDouble(mKg);
        } catch (NumberFormatException e) {
            weightKg = 0;
        }


        mHeight = mTvPersonHeight.getText().toString().trim();
        int i2 = mHeight.indexOf(" cm");
        if (i2 >= 0) {
            mHeight = mHeight.substring(0, i2);
            try {
                heightCm = Double.parseDouble(mHeight);
            } catch (NumberFormatException e) {
                heightCm = 0;
            }
        } else {
            heightCm = 0;
        }


        double heightM = heightCm / 100d;
        double resultNo = heightM * heightM;
        if (resultNo == 0) {
            bmi = 0;
        } else {
            bmi = weightKg / (heightM * heightM);
        }


        BigDecimal b = new BigDecimal(bmi);
        bmi = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//bmi四舍五入
        LogOut.d("llw", "BMI指数:" + bmi);
        return bmi;

    }
}
