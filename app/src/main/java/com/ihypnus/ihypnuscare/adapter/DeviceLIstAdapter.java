/*
 * Copyright 2016 Yan Zhenjie
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
package com.ihypnus.ihypnuscare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceInfoVO;

import java.util.List;

/**
 * 设备管理列表adapter
 */
public class DeviceLIstAdapter extends BaseAdapter<DeviceLIstAdapter.ViewHolder> {
    private OnItemClickListener mClickListener;
    private final Context mAct;
    private List<DeviceInfoVO> mDataList;
    private DeviceCheckListener mDeviceCheckListener;
    private int mOldPosition;

    public DeviceLIstAdapter(Context context) {
        super(context);
        mAct = context;
    }

    public void notifyDataSetChanged(List<DeviceInfoVO> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.item_device_list, parent, false),mClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DeviceInfoVO deviceInfoVO = mDataList.get(position);
        int isChecked = deviceInfoVO.getIsChecked();
        if (isChecked == 1) {
            mOldPosition = position;
            holder.ivSelected.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.ic_circle_checked));
        } else {
            holder.ivSelected.setImageDrawable(mAct.getResources().getDrawable(R.mipmap.ic_circle_unchecked));
        }

        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改选中的设备状态

                //将时间回调给fragment,刷新报告页面数据
                if (mDeviceCheckListener != null) {
                    DeviceInfoVO deviceInfoVO = mDataList.get(position);
//                    deviceInfoVO.setIsChecked(1);
                    mDeviceCheckListener.setDeviceCheckedCallback(mOldPosition, position, holder.ivSelected);
                }
            }
        });
        holder.setData(mDataList.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener mListener;// 声明自定义的接口
        TextView tvDeviceModel;
        TextView tvDeviceNo;
        ImageView ivSelected;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            // 为ItemView添加点击事件
            itemView.setOnClickListener(this);
            tvDeviceModel = (TextView) itemView.findViewById(R.id.tv_device_model);
            tvDeviceNo = (TextView) itemView.findViewById(R.id.tv_device_no);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }

        public void setData(DeviceInfoVO deviceInfoVO) {
            this.tvDeviceModel.setText(deviceInfoVO.getDeviceModel());
            this.tvDeviceNo.setText(deviceInfoVO.getDeviceNo());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                // getpostion()为Viewholder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
                mListener.onItemClick(view, getPosition());
            }
        }
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
}
