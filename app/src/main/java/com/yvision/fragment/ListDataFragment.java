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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.yvision.R;
import com.yvision.adapter.MainJpushListAdapter;
import com.yvision.base.BaseFragment;
import com.yvision.db.sql.SQLAttend;
import com.yvision.model.AttendModel;
import com.yvision.widget.JpushMsgToast;

import java.util.ArrayList;

/**
 * Created by sjy on 2017/4/5.
 */

public class ListDataFragment extends BaseFragment {
    private static final String TAG = "JSY";
    //
    private static final int MSG_RECEIVE = -40;
    //
    private ListView listView;

    //
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static boolean isForeground = false;//推送 判断
    private AttendModel model;
    //listView
    private MainJpushListAdapter adapter;
    private ArrayList<AttendModel> listdate;
    //存储
    private SQLAttend dao;

    //单例模式
    public static ListDataFragment newInstance() {
        ListDataFragment messageFragment = new ListDataFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_msg, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //布局详细操作（可添加多个方法）
        initViews(view);
        initListener();
        getData();
    }


    /**
     * 数据详细操作
     *
     * @param view
     */
    private void initViews(View view) {
        isForeground = true;
        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new MainJpushListAdapter(getActivity());
        listView.setAdapter(adapter);
        model = new AttendModel();
        registerMessageReceiver();  // used for receive msgdao
        dao = new SQLAttend(getActivity());
    }

    //从存储中获取数据
    private void getData() {
        listdate = dao.getModelList();
        if (listdate == null || listdate.size() <= 0) {
            return;
        } else {
            adapter = new MainJpushListAdapter(getActivity(), listdate);
            listView.setAdapter(adapter);
        }
    }

    //数据保存
    private void saveModel(AttendModel model) {
        dao.saveModel(model);
        Log.d("SJY", "以保存");
    }

    /**
     * 接受自定义消息使用
     */
    public void registerMessageReceiver() {

        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        this.getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                model = (AttendModel) intent.getSerializableExtra("AttendModel");
                Log.d(TAG, "接收到Jush的model,可以在main中做自定义处理 model.getMessage=" + model.getMessage());
                handler.sendMessage(handler.obtainMessage(MSG_RECEIVE, model));
            }
        }
    }

    //弹窗显示数据
    private void dialogShow(AttendModel model) {
        String message = model.getMessage();
        String Type = model.getType();
        //弹窗提示
        JpushMsgToast.makeText(getActivity(), Type, message, Toast.LENGTH_SHORT).show();
        //listView显示
        listdate = new ArrayList<>();
        listdate.clear();
        listdate.add(model);
        adapter.insertEntityList(listdate);
        listView.setAdapter(adapter);
    }

    /**
     * 界面跳转
     */
    private void initListener() {

    }

    /**
     * handler sendMessage的处理
     */
    @SuppressLint("HandlerLeak") // 确保类内部的handler不含有对外部类的隐式引用
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 调用下边的方法处理信息
            switch (msg.what) {
                case MSG_RECEIVE:
                    AttendModel model = (AttendModel) msg.obj;
                    saveModel(model);
                    dialogShow(model);
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
            public void onReceive(Context context, Intent intent){
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
        isForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(mMessageReceiver);
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
