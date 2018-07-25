package com.ihypnus.ihypnuscare.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.AddNwedeviceActivity;
import com.ihypnus.ihypnuscare.activity.DeviceDetailActivity;
import com.ihypnus.ihypnuscare.adapter.DeviceLIstAdapter;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private Button mBtnAdd;
    private boolean mNeedRefresh = false;
    private String mDeviceId;

    @Override
    protected int setView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_device;
    }

    @Override
    protected void findViews() {
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
    }


    @Override
    protected void init() {
        mInfoList = new ArrayList<>();
        mLayoutManager = createLayoutManager();
//        mItemDecoration = createItemDecoration();
        //设置侧滑
        mRecyclerView.setSwipeMenuCreator(this);
        //设置滑动点击事件
        mRecyclerView.setSwipeMenuItemClickListener(this);
        mAdapter = new DeviceLIstAdapter(mAct, mInfoList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(null);

    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(mAct);
    }

//    protected RecyclerView.ItemDecoration createItemDecoration() {
//        return new DefaultItemDecoration(ContextCompat.getColor(mAct, R.color.main_color_blue));
//    }

    @Override
    protected void initEvent() {
        mIvAdd.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mAdapter.setOnDeviceCheckedListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在加载...");
        getDataList();
    }

    @Subscribe
    public void onEventMainThread(BaseFactory.RefreshDeviceListInfoEvent event) {
        LogOut.d("llw", "添加新设备成功,刷新列表数据");
        getDataList();
    }

    /**
     * 获取用户绑定的设备列表信息
     */
    public void getDataList() {
        IhyRequest.getDeviceListInfos(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                mInfoList.clear();
                DeviceListVO listVO = (DeviceListVO) var1;
                if (listVO != null && listVO.getContent() != null && listVO.getContent().size() > 0) {
                    List<DeviceListVO.ContentBean> content = listVO.getContent();
                    mInfoList.addAll(content);
                    sortList();
                }
                mAdapter.setList(mInfoList);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    private void sortList() {
        if (mInfoList.size() > 0) {
            for (int i = 0; i < mInfoList.size(); i++) {
                String deviceId = mInfoList.get(i).getDevice_id();
                String defaultDeviceId = Constants.DEVICEID;
                if (!StringUtils.isNullOrEmpty(defaultDeviceId) && !StringUtils.isNullOrEmpty(defaultDeviceId) && defaultDeviceId.equals(deviceId)) {
                    mInfoList.get(i).setIsChecked(1);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.iv_add:
                jumpToAddNewDevice();
                break;

            case R.id.btn_add:
                if (mInfoList.size() == 0) {
                    ToastUtils.showToastDefault("请绑定您的设备");
                    return;
                }
                if (mNeedRefresh) {
                    setDefaultDeviceId();

                }
                break;
        }
    }

    /**
     * 设置默认设备
     */
    private void setDefaultDeviceId() {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在设置...");
        IhyRequest.setDefaultDeviceId(Constants.JSESSIONID, true, mDeviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                BaseDialogHelper.showMsgTipDialog(mAct, "设置成功");
                Constants.DEVICEID = mDeviceId;
                EventBus.getDefault().post(new BaseFactory.RefreshReportInfoEvent(mDeviceId));
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        int width = getResources().getDimensionPixelSize(R.dimen.w280);

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

            if (menuPosition == 0) {
                //删除
                String device_id = mInfoList.get(adapterPosition).getDevice_id();
                if (device_id.equals(Constants.DEVICEID)) {
                    BaseDialogHelper.showMsgTipDialog(mAct, "此为当前使用设备,更改后方能删除");
                    return;
                }
                removeDeviceId(adapterPosition, device_id);

            } /*else if (menuPosition == 1) {
                //添加设备
                jumpToAddNewDevice();
            }*/
        } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

        }
    }

    /**
     * 移除绑定设备id
     *
     * @param position
     * @param device_id
     */
    private void removeDeviceId(int position, String device_id) {
        BaseDialogHelper.showLoadingDialog(mAct, true, "正在解绑...");
        IhyRequest.unbindDevice(Constants.JSESSIONID, true, device_id, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                mInfoList.clear();
                getDataList();
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
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
        if (oldPosition == -1) {
            mInfoList.get(position).setIsChecked(1);
        } else {
            mInfoList.get(oldPosition).setIsChecked(0);
            mInfoList.get(position).setIsChecked(1);
        }

        mDeviceId = mInfoList.get(position).getDevice_id();
        mAdapter.setList(mInfoList);
        mNeedRefresh = oldPosition != position;


    }

    private void jumpToDeviceDetail(DeviceListVO.ContentBean deviceInfoVO) {
        Intent intent = new Intent(mAct, DeviceDetailActivity.class);
        intent.putExtra("DATA_BEAN", deviceInfoVO);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int postion) {
        DeviceListVO.ContentBean contentBean = mInfoList.get(postion);
        if (contentBean != null && !StringUtils.isNullOrEmpty(contentBean.getDevice_id())) {
            jumpToDeviceDetail(contentBean);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseFactory.UpdateLanguageEvent event) {
        LogOut.d("llw", "fragment页面更新了语言");
        ViewUtils.updateViewLanguage(findViewById(android.R.id.content));
    }
}
