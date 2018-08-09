package com.ihypnus.ihypnuscare.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.widget.AppTextView;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description:
 * @date: 2018/8/9 17:20
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DiviceListAdapter extends IhyBaseAdapter<DeviceListVO.ContentBean> {
    private int mOldPosition = -1;
    private DeviceCheckListener mDeviceCheckListener;
    private OnItemClickListener mClickListener;

    public DiviceListAdapter(Activity act, List<DeviceListVO.ContentBean> list) {
        super(act, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflateView(R.layout.item_device_list);
        }
        DeviceListVO.ContentBean contentBean = mList.get(position);
        ImageView ivIcon = ViewHolder.get(convertView, R.id.iv_icon);
        AppTextView tvDeviceModel = ViewHolder.get(convertView, R.id.tv_device_model);
        AppTextView tvDeviceNo = ViewHolder.get(convertView, R.id.tv_device_no);
        final ImageView ivSelected = ViewHolder.get(convertView, R.id.iv_selected);
        String model = contentBean.getModel();
        String device_id = contentBean.getDevice_id();

        tvDeviceModel.setText(StringUtils.isNullOrEmpty(model) ? mAct.getString(R.string.tv_unknow) : model);
        tvDeviceNo.setText(StringUtils.isNullOrEmpty(device_id) ? "" : device_id);

        int isChecked = contentBean.getIsChecked();
        if (isChecked == 1) {
            mOldPosition = position;
            contentBean.setIsChecked(1);
            ivIcon.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.icon_device_checked));
            ivSelected.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.ic_circle_checked));
            tvDeviceModel.setTextColor(Color.WHITE);
            tvDeviceNo.setTextColor(Color.WHITE);
        } else {
            ivIcon.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.icon_device_normal));
            ivSelected.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.ic_circle_unchecked));
            tvDeviceModel.setTextColor(Color.GRAY);
            tvDeviceNo.setTextColor(Color.GRAY);
        }

        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }
        });

        tvDeviceModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }
        });

        tvDeviceNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }
        });

        ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改选中的设备状态

                //将时间回调给fragment,刷新报告页面数据
                if (mDeviceCheckListener != null) {
                    mDeviceCheckListener.setDeviceCheckedCallback(mOldPosition, position, ivSelected);
                }
            }
        });
        return convertView;
    }


    public interface DeviceCheckListener {
        void setDeviceCheckedCallback(int oldPosition, int position, ImageView imageView);
    }

    public void setOnDeviceCheckedListener(DeviceCheckListener listener) {
        this.mDeviceCheckListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
}
