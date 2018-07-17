package com.ihypnus.ihypnuscare.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description: adapter的基类
 * @date: 2018/7/17 22:23
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public abstract class IhyBaseAdapter <T> extends android.widget.BaseAdapter {
    private LayoutInflater mInflater;
    protected List<T> mList;
    protected AdapterView.OnItemClickListener itemClicklistener;
    protected Activity mAct;
    // 用于记录需要删除的view的position
    protected ArrayList<Integer> mRemoveList = new ArrayList<Integer>();

    public IhyBaseAdapter(Activity act, List<T> list) {
        mInflater = LayoutInflater.from(act);
        this.mList = list;
        mAct = act;
    }

    public IhyBaseAdapter(Activity act) {
        mInflater = LayoutInflater.from(act);
        mAct = act;
    }

    public View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (mList != null) mList.addAll(list);
        else mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void clearListNoNotify() {
        if (mList != null && mList.size() > 0) mList.clear();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View convertView, int resId) {
            SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                convertView.setTag(viewHolder);
            }
            View childView = viewHolder.get(resId);
            if (childView == null) {
                childView = convertView.findViewById(resId);
                viewHolder.put(resId, childView);
            }
            return (T) childView;
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.itemClicklistener = listener;
    }
}
