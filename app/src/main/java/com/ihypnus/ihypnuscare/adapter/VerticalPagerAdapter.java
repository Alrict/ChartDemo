package com.ihypnus.ihypnuscare.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.ihypnus.ihypnuscare.controller.BaseController;

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
    private ArrayList<BaseController> mPageFragmentList;

    public VerticalPagerAdapter(ArrayList<BaseController> pageFragmentList) {
        mPageFragmentList = pageFragmentList;
    }

    public List<BaseController> getViews() {
        return mPageFragmentList;
    }

    public View getPage(int position) {

        return mPageFragmentList.get(position).getRootView();
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
        container.removeView(mPageFragmentList.get(position).getRootView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseController view = mPageFragmentList.get(position);
        View rootView = view.getRootView();
        container.addView(rootView);
        return rootView;
    }
}
