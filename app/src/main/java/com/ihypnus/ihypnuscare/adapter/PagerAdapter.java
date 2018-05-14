package com.ihypnus.ihypnuscare.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihypnus.ihypnuscare.R;

import java.util.ArrayList;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description: 垂直方向viewPagerAdapter
 * @date: 2018/5/14 13:49
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class PagerAdapter extends android.support.v4.view.PagerAdapter {
private ArrayList<Fragment> mPageFragmentList;

    public PagerAdapter(ArrayList<Fragment> pageFragmentList) {
        mPageFragmentList = pageFragmentList;
    }

    @Override
    public int getCount() {
        return mPageFragmentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_room_item, null);
        view.setId(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.findViewById(position));
    }
}