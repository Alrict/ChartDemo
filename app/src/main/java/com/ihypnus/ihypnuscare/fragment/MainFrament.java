package com.ihypnus.ihypnuscare.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ihypnus.ihypnuscare.R;

import java.util.Random;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description:
 * @date: 2018/5/14 14:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MainFrament extends BaseFragment {
    private Button mButton;
    private Random random = new Random();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
