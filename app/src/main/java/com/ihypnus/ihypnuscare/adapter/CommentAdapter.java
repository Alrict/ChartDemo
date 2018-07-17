package com.ihypnus.ihypnuscare.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.adapter
 * @author: llw
 * @Description:
 * @date: 2018/7/17 22:20
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class CommentAdapter extends IhyBaseAdapter<String> {
    public CommentAdapter(Activity act, List<String> list) {
        super(act, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflateView(R.layout.item_normal);
        }
        String content = mList.get(position);
        TextView textView = ViewHolder.get(convertView, R.id.tv_content);
        textView.setText(content);
        return convertView;
    }
}
