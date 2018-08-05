package com.ihypnus.ihypnuscare.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * wifi adapter
 */
public class WifiListAdapter extends ArrayAdapter<ScanResult> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private int mResource;

    public WifiListAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
            AutoUtils.autoSize(convertView);
        }

        TextView name = (TextView) convertView.findViewById(R.id.wifi_name);
        TextView signl = (TextView) convertView.findViewById(R.id.wifi_signal);

        ScanResult scanResult = getItem(position);
        name.setText(scanResult.SSID);

        int level = scanResult.level;
        if (level <= 0 && level >= -50) {
            signl.setText(mContext.getResources().getString(R.string.semaphore_good));
        } else if (level < -50 && level >= -70) {
            signl.setText(mContext.getResources().getString(R.string.semaphore_better));
        } else if (level < -70 && level >= -80) {
            signl.setText(mContext.getResources().getString(R.string.semaphore_standard));
        } else if (level < -80 && level >= -100) {
            signl.setText(mContext.getResources().getString(R.string.semaphore_week));
        } else {
            signl.setText(mContext.getResources().getString(R.string.semaphore_poor));
        }

        return convertView;
    }

}
