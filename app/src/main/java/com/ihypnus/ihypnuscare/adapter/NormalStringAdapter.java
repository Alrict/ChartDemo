package com.ihypnus.ihypnuscare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.UsageInfos;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description:
 * @date: 2018/7/8 21:59
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class NormalStringAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<UsageInfos.UsetimesBean> mList;

    public NormalStringAdapter(Context context, List<UsageInfos.UsetimesBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_normal, null);
            holder = new ViewHolder();
            holder.mContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String starttime = mList.get(i).getStarttime();
        String endTime = mList.get(i).getEndTime();

        if (starttime.length() >= 16 && endTime.length() >= 16) {
            starttime = starttime.substring(11, 16);
            endTime = endTime.substring(11, 16);
            holder.mContent.setText(starttime + "~" + endTime);
        } else {
            holder.mContent.setText("--~--");
        }

        return convertView;
    }

    class ViewHolder {
        TextView mContent;
    }
}
