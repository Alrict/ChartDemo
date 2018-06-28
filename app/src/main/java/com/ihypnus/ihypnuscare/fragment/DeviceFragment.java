package com.ihypnus.ihypnuscare.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.AddNwedeviceActivity;
import com.ihypnus.ihypnuscare.activity.DeviceDetailActivity;
import com.ihypnus.ihypnuscare.adapter.DeviceLIstAdapter;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 主页fragment
 * @date: 2018/5/14 14:42
 */
public class DeviceFragment extends BaseFragment implements View.OnClickListener, SwipeMenuCreator, SwipeMenuItemClickListener, DeviceLIstAdapter.DeviceCheckListener, DeviceLIstAdapter.OnItemClickListener {
    private static final String TAG = "DeviceFragment";
    private ImageView mIvAdd;
    private SwipeMenuRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private DeviceLIstAdapter mAdapter;
    private ArrayList<DeviceListVO.ContentBean> mInfoList;
    public static final int REQUEST_CODE = 131;

    @Override
    protected int setView() {
        return R.layout.fragment_device;
    }

    @Override
    protected void findViews() {
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
    }


    @Override
    protected void init() {
        mInfoList = new ArrayList<>();
        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        //设置侧滑
        mRecyclerView.setSwipeMenuCreator(this);
        //设置滑动点击事件
        mRecyclerView.setSwipeMenuItemClickListener(this);
        mAdapter = new DeviceLIstAdapter(mAct, mInfoList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);

    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(mAct);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(mAct, R.color.main_color_blue));
    }

    @Override
    protected void initEvent() {
        mIvAdd.setOnClickListener(this);
        mAdapter.setOnDeviceCheckedListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        getDataList();
    }

    private void getDataList() {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在加载...");
        IhyRequest.getDeviceListInfos(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                mInfoList.clear();
                DeviceListVO listVO = (DeviceListVO) var1;
                if (listVO != null && listVO.getContent() != null && listVO.getContent().size() > 0) {
                    List<DeviceListVO.ContentBean> content = listVO.getContent();
                    mInfoList.addAll(content);

                }
                mAdapter.setList(mInfoList);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.iv_add:
                jumpToAddNewDevice();
                break;
        }
    }

    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        int width = getResources().getDimensionPixelSize(R.dimen.w300);

        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        SwipeMenuItem deleteItem = new SwipeMenuItem(mAct)
                .setBackground(R.drawable.selector_red)
                .setImage(R.mipmap.ic_action_delete)
                .setText("删除")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);
        swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

//        SwipeMenuItem addItem = new SwipeMenuItem(mAct)
//                .setBackground(R.drawable.selector_green)
//                .setText("添加")
//                .setTextColor(Color.WHITE)
//                .setWidth(width)
//                .setHeight(height);
//        swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge) {
        menuBridge.closeMenu();
        // 左侧还是右侧菜单。
        int direction = menuBridge.getDirection();

        //列表的postion
        int adapterPosition = menuBridge.getAdapterPosition();

        //侧滑menu的按钮position
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            Toast.makeText(mAct, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            if (menuPosition == 0) {
                //删除
                mInfoList.remove(adapterPosition);
                mAdapter.notifyDataSetChanged(mInfoList);
            } else if (menuPosition == 1) {
                //添加设备
                jumpToAddNewDevice();
            }
        } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

            Toast.makeText(mAct, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加新设备
     */
    private void jumpToAddNewDevice() {
        Intent intent = new Intent(mAct, AddNwedeviceActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            getDataList();
        }
    }

    @Override
    public void setDeviceCheckedCallback(int oldPosition, int position, ImageView imageView) {
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_checked));
        mInfoList.get(position).setIsChecked(1);
        mInfoList.get(oldPosition).setIsChecked(0);
        mAdapter.notifyDataSetChanged();
    }

    private void jumpToDeviceDetail(DeviceListVO.ContentBean deviceInfoVO) {
        Intent intent = new Intent(mAct, DeviceDetailActivity.class);
        intent.putExtra("DATA_BEAN", deviceInfoVO);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int postion) {
        DeviceListVO.ContentBean contentBean = mInfoList.get(postion);
        jumpToDeviceDetail(contentBean);
    }
}
