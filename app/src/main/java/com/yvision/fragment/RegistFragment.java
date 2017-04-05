package com.yvision.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yvision.R;
import com.yvision.adapter.RegisterListAdapter;
import com.yvision.base.BaseFragment;
import com.yvision.common.MyException;
import com.yvision.dialog.Loading;
import com.yvision.helper.UserHelper;
import com.yvision.model.OldEmployeeModel;
import com.yvision.utils.PageUtil;

import java.util.ArrayList;

/**
 * Created by sjy on 2017/4/5.
 */

public class RegistFragment extends BaseFragment {
    //
    private ListView listView;
    //
    private OldEmployeeModel model;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    //listView
    private RegisterListAdapter adapter;
    private boolean ifLoading = false;//标记
    private ArrayList<OldEmployeeModel> listdate;
    private int pageSize = 20;
    // 常量
    private static final int GET_DATA_SUCCESS = -39;// 获取所有数据列表 标志
    private static final int GET_NONE_NEWDATA = -35;//没有新数据

    //单例模式
    public static RegistFragment newInstance() {
        RegistFragment messageFragment = new RegistFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_vip, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //布局详细操作（可添加多个方法）
        initViews(view);
        getData();
    }


    /**
     * 数据详细操作
     *
     * @param view
     */
    private void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new RegisterListAdapter(getActivity());
        listView.setAdapter(adapter);
        model = new OldEmployeeModel();
    }

    //从存储中获取数据
    private void getData() {
        if (ifLoading) {
            return;
        }

        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                ifLoading = true;
                try {
                    adapter.IsEnd = false;

                    ArrayList<OldEmployeeModel> visitorModelList = UserHelper.getOldEmployList(
                            getActivity());

                    if (visitorModelList == null) {
                        adapter.IsEnd = true;
                    } else if (visitorModelList.size() < pageSize) {
                        adapter.IsEnd = true;
                    }
                    handler.sendMessage(handler.obtainMessage(GET_DATA_SUCCESS, visitorModelList));

                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, e.getMessage()));
                    ifLoading = false;
                }
            }
        });
    }


    /**
     * handler sendMessage的处理
     */
    @SuppressLint("HandlerLeak") // 确保类内部的handler不含有对外部类的隐式引用
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESS://加载全部/今天
                    listdate = (ArrayList) msg.obj;

                    adapter.setEntityList(listdate);
                    //数据处理，只存最小值
                    ifLoading = false;
                    break;
                case GET_NONE_NEWDATA://没有获取新数据
                    PageUtil.DisplayToast((String) msg.obj);
                    ifLoading = false;
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //注册广播
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");//建议把它写一个公共的变量，这里方便阅读就不写了。
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("OK");
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    @Override
    public boolean hasMultiFragment() {
        return false;
    }

    @Override
    protected String setFragmentName() {
        return null;
    }

    @Override
    public String getFragmentName() {
        return "ListFragment";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //重新进入页面后，刷新数据
    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }
}
