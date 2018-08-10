package com.ihypnus.ihypnuscare.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2017/1/12 20:45
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description FragmentPagerAdapter 里面用的
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class IhyFragmentPagerAdapter extends PagerAdapter {

    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public IhyFragmentPagerAdapter(FragmentManager fm) {
        this.mFragmentManager = fm;
    }

    public abstract Fragment getItem(int var1);

    public void startUpdate(ViewGroup container) {
    }

    public Object instantiateItem(ViewGroup container, int position) {

        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        long itemId = this.getItemId(position);
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
        if (fragment != null && fragment == this.getItem(position)) {
            this.mCurTransaction.attach(fragment);
        } else {
            fragment = this.getItem(position);
            this.mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        }

        if (fragment != this.mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        this.mCurTransaction.detach((Fragment) object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }

            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    public void finishUpdate(ViewGroup container) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }

    }

    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public long getItemId(int position) {
        return (long) position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
