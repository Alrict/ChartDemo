package com.ihypnus.ihypnuscare.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description:
 * @date: 2018/5/14 15:45
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class VerticalPagerAdapter extends android.support.v4.view.PagerAdapter {
    private ArrayList<View> mPageFragmentList;

    public VerticalPagerAdapter(ArrayList<View> pageFragmentList) {
        mPageFragmentList = pageFragmentList;
    }

    public List<View> getViews() {
        return mPageFragmentList;
    }

    public View getPage(int position) {
//        Preconditions.checkState(position >= 0 && position < mPageFragmentList.size(), "参数越界:ExtPageAdapter.getPage()");
        return mPageFragmentList.get(position);
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPageFragmentList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPageFragmentList.get(position);
        container.addView(view);
        return view;
    }
}
