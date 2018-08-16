package com.ihypnus.ihypnuscare.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ihypnus.multilanguage.MultiLanguageUtil;

import java.util.Locale;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description:
 * @date: 2018/5/14 14:43
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mAct;
    private View mLayoutView;

    public BaseFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAct = activity;
    }

    public void preCreate() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayoutView == null) {
            preCreate();
            onPreInflate(inflater, container, savedInstanceState);
            mLayoutView = inflate(inflater);
            findViews();
            init();
            initEvent();
            loadData();
        } else {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null)
                parent.removeView(mLayoutView);
            onRefreshData();
        }
        return mLayoutView;
    }

    /**
     * 在视图数据被释放后再次载入时刷新数据
     */
    protected void onRefreshData() {

    }


    /**
     * 设置activity的布局文件
     *
     * @return 布局文件的resId
     */
    protected abstract int setView();

    /**
     * 获取界面元素
     */
    protected abstract void findViews();

    /**
     * 对象的初始化工作
     */
    protected abstract void init();

    /**
     * 设置事件
     */
    protected abstract void initEvent();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    protected void onPreInflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    protected View findViewById(int viewId) {
        return mLayoutView.findViewById(viewId);
    }

    protected View inflate(LayoutInflater inflater) {
        return inflater.inflate(setView(), null);
    }

    protected void back() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    public void showInput(boolean show) {
        try {
            if (mAct == null) {
                return;
            }
            if (show) {
                InputMethodManager imm = (InputMethodManager) mAct.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod((mAct).getCurrentFocus().getApplicationWindowToken(), 0);
            } else {
                InputMethodManager imm = (InputMethodManager) mAct.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mAct.getCurrentFocus().getApplicationWindowToken(), 0);
            }
        } catch (NullPointerException e1) {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(MultiLanguageUtil.attachBaseContext(context, getAppLanguage(context)));
    }

    private Locale getAppLanguage(Context context) {
        MultiLanguageUtil.init(context);
        return MultiLanguageUtil.getInstance().getLanguageLocale(context);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
////        LogOut.d("llw", "fragment页面更新了语言");
////        getActivity().recreate();
////        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
//    }
}
