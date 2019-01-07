package com.hikvision.sdk.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hikvision.sdk.R;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.app.Constants;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.bean.RootCtrlCenter;
import com.hikvision.sdk.net.bean.SubResourceNodeBean;
import com.hikvision.sdk.net.bean.SubResourceParam;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.CNetSDKLog;
import com.hikvision.sdk.utils.UIUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>监控点资源展示Activity</p>
 *
 * @author zhangwei59 2017/3/10 14:34
 * @version V1.0.0
 */
public class ResourceListActivity extends ListActivity {
    /**
     * 关闭加载进度条
     */
    public static final int CANCEL_LOADING_PROGRESS = 1;
    /**
     * 加载成功
     */
    public static final int LOADING_SUCCESS = 2;
    /**
     * 加载失败
     */
    public static final int LOADING_FAILED = 3;

    /**
     * list显示数据适配器
     */
    private ArrayAdapter<String> mAdapter = null;
    /**
     * list显示数据
     */
    private ArrayList<String> mData = new ArrayList<>();
    /**
     * 资源源数据
     */
    private ArrayList<Object> mSource = new ArrayList<>();
    /**
     * 发送消息的对象
     */
    private Handler mHandler = null;

    /***
     * UI处理Handler
     */
    private static class ViewHandler extends Handler {

        private final WeakReference<ResourceListActivity> mActivity;

        ViewHandler(ResourceListActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case CANCEL_LOADING_PROGRESS:
                    UIUtils.cancelProgressDialog();
                    break;
                case LOADING_SUCCESS:
                    UIUtils.cancelProgressDialog();
                    mActivity.get().mAdapter.notifyDataSetChanged();
                    break;
                case LOADING_FAILED:
                    UIUtils.cancelProgressDialog();
                    UIUtils.showToast(mActivity.get(), R.string.loading_failed);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mHandler = new ViewHandler(this);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.IntentKey.GET_ROOT_NODE)) {
            // 初次加载根节点数据
            getRootControlCenter();
        } else if (intent.hasExtra(Constants.IntentKey.GET_SUB_NODE)) {
            // 加载子节点列表
            int parentNodeType = intent.getIntExtra(Constants.IntentKey.PARENT_NODE_TYPE, 0);
            int parentId = intent.getIntExtra(Constants.IntentKey.PARENT_ID, 0);
            getSubResourceList(parentNodeType, parentId);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int nodeType;
        final Object node = mSource.get(position);
        if (node instanceof SubResourceNodeBean) {
            nodeType = ((SubResourceNodeBean) node).getNodeType();
            if (SDKConstant.NodeType.TYPE_CAMERA_OR_DOOR == nodeType) {
                // 构造camera对象
                final SubResourceNodeBean cameraData = (SubResourceNodeBean) node;
                int isOnline = cameraData.getIsOnline();
//                if (isOnline == 1) {
                    new AlertDialog.Builder(this).setSingleChoiceItems(R.array.camera_select_items, 0,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    switch (which) {
                                        case 0:
                                            // 预览
                                            if (VMSNetSDK.getInstance().checkLivePermission(cameraData)) {
                                                goLive(cameraData);
                                            } else {
                                                UIUtils.showToast(ResourceListActivity.this, R.string.no_permission);
                                            }
                                            break;
                                        case 1:
                                            // 回放
                                            if (VMSNetSDK.getInstance().checkPlayBackPermission(cameraData)) {
                                                goPlayBack(cameraData);
                                            } else {
                                                UIUtils.showToast(ResourceListActivity.this, R.string.no_permission);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }).show();
//                } else {
//                    UIUtils.showToast(this, R.string.device_not_online);
//                }
            } else {
                // 请求此item的下级资源
                Object obj = mSource.get(position);
                int parentNodeType = ((SubResourceNodeBean) obj).getNodeType();
                int pId = ((SubResourceNodeBean) obj).getId();
                Intent intent = new Intent(this, ResourceListActivity.class);
                intent.putExtra(Constants.IntentKey.GET_SUB_NODE, true);
                intent.putExtra(Constants.IntentKey.PARENT_NODE_TYPE, parentNodeType);
                intent.putExtra(Constants.IntentKey.PARENT_ID, pId);
                startActivity(intent);
            }
        }
    }

    /**
     * 回放监控点
     *
     * @param subResourceNodeBean 监控点资源
     */
    private void goPlayBack(SubResourceNodeBean subResourceNodeBean) {
        if (subResourceNodeBean == null) {
            UIUtils.showToast(this, R.string.empty);
        } else {
            Intent it = new Intent(this, PlayBackActivity.class);
            it.putExtra(Constants.IntentKey.CAMERA, subResourceNodeBean);
            startActivity(it);
        }
    }

    /**
     * 预览监控点
     *
     * @param subResourceNodeBean 监控点资源
     */
    private void goLive(SubResourceNodeBean subResourceNodeBean) {
        if (subResourceNodeBean != null) {
            Intent it = new Intent(this, LiveActivity.class);
            it.putExtra(Constants.IntentKey.CAMERA, subResourceNodeBean);
            startActivity(it);
        } else {
            UIUtils.showToast(this, R.string.empty);
        }
    }

    /**
     * 获取根控制中心
     */
    public void getRootControlCenter() {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip);
        VMSNetSDK.getInstance().getRootCtrlCenterInfo(1, SDKConstant.SysType.TYPE_VIDEO, 999, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                mHandler.sendEmptyMessage(LOADING_FAILED);
            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                if (obj instanceof RootCtrlCenter) {
                    int parentNodeType = Integer.parseInt(((RootCtrlCenter) obj).getNodeType());
                    int parentId = ((RootCtrlCenter) obj).getId();
                    getSubResourceList(parentNodeType, parentId);
                }
            }
        });
    }

    /**
     * 获取父节点资源列表
     *
     * @param parentNodeType 父节点类型
     * @param pId            父节点ID
     */
    private void getSubResourceList(int parentNodeType, int pId) {
        UIUtils.showLoadingProgressDialog(this, R.string.loading_process_tip);
        VMSNetSDK.getInstance().getSubResourceList(1, 999, SDKConstant.SysType.TYPE_VIDEO, parentNodeType, String.valueOf(pId), new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                super.onFailure();
                mHandler.sendEmptyMessage(LOADING_FAILED);
            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                if (obj instanceof SubResourceParam) {
                    List<SubResourceNodeBean> list = ((SubResourceParam) obj).getNodeList();
                    mData.clear();
                    mSource.clear();
                    if (list != null && list.size() > 0) {
                        for (SubResourceNodeBean bean : list) {
                            mData.add(bean.getName());
                            mSource.add(bean);
                            //读取监控点在线数和总数
                            CNetSDKLog.info("cameraOnline:  " + bean.getCameraOnline() + "cameraTotal: " + bean.getCameraTotal());
                        }
                        mHandler.sendEmptyMessage(LOADING_SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(LOADING_FAILED);
                    }
                }
            }
        });
    }

}
