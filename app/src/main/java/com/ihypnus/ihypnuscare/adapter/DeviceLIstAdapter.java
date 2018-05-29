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

    private List<DeviceInfoVO> mDataList;
    private DeviceCheckListener mDeviceCheckListener;

    public DeviceLIstAdapter(Context context) {
        super(context);
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
        return new ViewHolder(getInflater().inflate(R.layout.item_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改选中的设备状态

                //将时间回调给fragment,刷新报告页面数据
                if (mDeviceCheckListener != null) {
                    mDeviceCheckListener.setDeviceCheckedCallback(mDataList.get(position));
                }
            }
        });
        holder.setData(mDataList.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceModel;
        TextView tvDeviceNo;
        ImageView ivSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDeviceModel = (TextView) itemView.findViewById(R.id.tv_device_model);
            tvDeviceNo = (TextView) itemView.findViewById(R.id.tv_device_no);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }

        public void setData(DeviceInfoVO deviceInfoVO) {
            this.tvDeviceModel.setText(deviceInfoVO.getDeviceModel());
            this.tvDeviceNo.setText(deviceInfoVO.getDeviceNo());
        }

    }

    public interface DeviceCheckListener {
        void setDeviceCheckedCallback(DeviceInfoVO deviceInfoVO);
    }

    public void setOnDeviceCheckedListener(DeviceCheckListener listener) {
        this.mDeviceCheckListener = listener;
    }

}
