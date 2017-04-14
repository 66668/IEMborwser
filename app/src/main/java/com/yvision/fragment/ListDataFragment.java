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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yvision.R;
import com.yvision.adapter.MainJpushListAdapter;
import com.yvision.base.BaseFragment;
import com.yvision.common.HttpParameter;
import com.yvision.common.ImageLoadingConfig;
import com.yvision.common.MyException;
import com.yvision.db.sql.SQLAttend;
import com.yvision.dialog.Loading;
import com.yvision.helper.UserHelper;
import com.yvision.model.AttendDetailModel;
import com.yvision.model.AttendModel;
import com.yvision.model.DoorAccessModel;
import com.yvision.model.VipModel;
import com.yvision.utils.PageUtil;
import com.yvision.utils.Utils;
import com.yvision.widget.CircleImageView;
import com.yvision.widget.JpushMsgToast;

import java.util.ArrayList;

/**
 * app在线，正常接收推送，数据只保存一天，sql处理。
 * 当由离线到在线，这期间数据会根据mintime和maxtime确定，maxtime为当前时间
 * （1）当天清除数据后，再登陆，无旧数据，min为空，
 * （2）登录退出有数据，再登陆，有旧数据，min为第一条数据time
 * Created by sjy on 2017/4/5.
 */

public class ListDataFragment extends BaseFragment {
    //常量
    private static final int MSG_RECEIVE = -40;
    private static final String TAG = "SJY";
    private static final int GET_VIP_SUCCESS = -41;
    private static final int GET_DOOR_SUCCESS = -42;
    private static final int GET_ATTEND_SUCCESS = -43;
    private static final int GET_VISITOR_SUCCESS = -44;
    private static final int OFFLINE_SUCCESS = -45;
    private static final int OFFLINE_SUCCESS02 = -46;

    private static final int GET_DOOR_FAILED = -39;
    private static final int GET_VISITOR_FAILED = -38;
    private static final int GET_ATTEND_FAILED = -37;
    private static final int GET_VIP_FAILED = -36;
    private static final int OFFLINE_FAILED02 = -35;

    //
    private boolean isRightChange = true;//标记

    private ListView listView;

    //普通考勤只有部门 地图考勤只有地址
    private RelativeLayout layout_dept;
    private RelativeLayout layout_adress;
    private RelativeLayout layout_id;
    private TextView tv_dept;
    private TextView tv_adress;

    //考勤 vip 门禁 通用参数
    private CircleImageView photo_default;
    private CircleImageView photo_cap;

    private TextView tv_name;
    private TextView tv_id;
    private TextView tv_company;
    private TextView tv_captime;
    private TextView tv_score;


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

    //图片缓存
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

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
        //

        listView = (ListView) view.findViewById(R.id.listView);

        //通用参数
        photo_default = (CircleImageView) view.findViewById(R.id.photo_default);
        photo_cap = (CircleImageView) view.findViewById(R.id.photo_cap);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        tv_company = (TextView) view.findViewById(R.id.tv_company);
        tv_captime = (TextView) view.findViewById(R.id.tv_captime);
        tv_score = (TextView) view.findViewById(R.id.tv_score);

        //普通考勤
        layout_dept = (RelativeLayout) view.findViewById(R.id.layout_dept);
        tv_dept = (TextView) view.findViewById(R.id.tv_dept);

        //地图考勤
        layout_adress = (RelativeLayout) view.findViewById(R.id.layout_address);
        tv_adress = (TextView) view.findViewById(R.id.tv_address);

        //vip 无id
        layout_id = (RelativeLayout) view.findViewById(R.id.layout_id);
        isForeground = true;

        adapter = new MainJpushListAdapter(getActivity());
        listView.setAdapter(adapter);
        model = new AttendModel();
        registerMessageReceiver();  // used for receive msgdao
        dao = new SQLAttend(getActivity());

        //初始化imageloader
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.default_photo);
    }

    /**
     * 界面跳转
     */
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRightChange = false;//点击item,取消自动更新状态，只显示item内容
                AttendModel model = (AttendModel) adapter.getItem(position);
                selectdataByType(model);

            }
        });
    }

    private void selectdataByType(AttendModel model) {
        switch (model.getType()) {
            case "考勤":
                getAttendDetailDate(model.getId());
                break;
            case "VIP":
                getVIPDetailDate(model.getId());
                break;

            case "门禁":
                getDoorDetailDate(model.getId());
                break;

        }
    }

    /**
     * 获取考勤详情
     *
     * @param id
     */
    private void getAttendDetailDate(final String id) {
        //        if(isRightChange){
        //            return;
        //        }
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    AttendDetailModel detailModel = UserHelper.getAttendDetail(getActivity(), id);
                    handler.sendMessage(handler.obtainMessage(GET_ATTEND_SUCCESS, detailModel));
                } catch (MyException e) {
                    Log.d(TAG, "异常=" + e.toString());
                    handler.sendMessage(handler.obtainMessage(GET_ATTEND_FAILED, e.toString()));
                }
            }
        });
    }

    /**
     * 获取VIP详情
     *
     * @param id
     */
    private void getVIPDetailDate(final String id) {
        //        if(isRightChange){
        //            return;
        //        }
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    VipModel detailModel = UserHelper.getVIPDetail(getActivity(), id);
                    handler.sendMessage(handler.obtainMessage(GET_VIP_SUCCESS, detailModel));
                } catch (MyException e) {
                    Log.d(TAG, "异常=" + e.toString());
                    handler.sendMessage(handler.obtainMessage(GET_VIP_FAILED, e.toString()));
                }
            }
        });
    }

    /**
     * 获取VIP详情
     *
     * @param id
     */
    private void getDoorDetailDate(final String id) {
        //        if(isRightChange){
        //            return;
        //        }
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    DoorAccessModel detailModel = UserHelper.getDoorAccessDetail(getActivity(), id);
                    handler.sendMessage(handler.obtainMessage(GET_DOOR_SUCCESS, detailModel));
                } catch (MyException e) {
                    Log.d(TAG, "异常=" + e.toString());
                    handler.sendMessage(handler.obtainMessage(GET_DOOR_FAILED, e.toString()));
                }
            }
        });
    }

    /**
     * 从存储中获取数据
     */
    private void getData() {
        listdate = dao.getModelList();
        if (listdate == null || listdate.size() <= 0) {
            //app安装后无数据，调接口获取今天的离线数据
            getOffLineDate02();//app列表没有数据
        } else {
            //判断数据是否是今日，不是就清空存储,同时调接口获取今天的离线数据
            String oldTime = listdate.get(listdate.size() - 1).getCapTime();
            Log.d(TAG, "数据个数：" + listdate.size() + "--时间格式：" + oldTime + "--time=" + listdate.get(1).getCapTime() + "--Type=" + listdate.get(1).getType() +
                    "--id=" + listdate.get(1).getId() + "--Message=" + listdate.get(1).getMessage() + "\npic=" + listdate.get(1).getPic());
            String newTime = Utils.getCurrentDate();
            String oldDay = oldTime.substring(oldTime.lastIndexOf("-"), oldTime.indexOf(" "));
            String newDay = newTime.substring(newTime.lastIndexOf("-"), newTime.indexOf(" "));

            if (oldDay.contains(newDay)) {//日期相同，表示同一天
                //列表展示
                adapter = new MainJpushListAdapter(getActivity(), listdate);
                listView.setAdapter(adapter);

                //详情展示（展示最新一条存储数据）
                getAttendDetailDate(listdate.get(0).getId());
            } else {//只保留当天记录，清除数据库
                dao.clearDb();
            }
            //获取app离线的数据，并保存
            getOffLineDate01(listdate);//数据库有记录

        }

    }

    //数据库离线记录
    private void getOffLineDate01(final ArrayList<AttendModel> oldListDate) {
        Log.d(TAG, "getOffLineDate01: ");
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: storeId=" + UserHelper.getCurrentUser().getStoreID() + "\nCurrentTime=" + Utils.getCurrentDate()
                            + "\noldListDate.get(0).getCapTime()=" + oldListDate.get(0).getCapTime());

                    ArrayList<AttendModel> offLineDate = UserHelper.getOffLineDate(getActivity(), HttpParameter.create()
                            .add("storeId", UserHelper.getCurrentUser().getStoreID())
                            .add("maxTime", Utils.getCurrentDate())// Utils.getCurrentDate()
                            .add("minTime", oldListDate.get(0).getCapTime()));

                    handler.sendMessage(handler.obtainMessage(OFFLINE_SUCCESS, offLineDate));
                } catch (MyException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //数据库无记录
    private void getOffLineDate02() {
        final String CurrentTime = Utils.getCurrentDate();
        Log.d(TAG, "getOffLineDate02: ");
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: storeId=" + UserHelper.getCurrentUser().getStoreID() + "\nCurrentTime=" + CurrentTime);
                    ArrayList<AttendModel> offLineDate = new ArrayList<AttendModel>();
                    offLineDate = UserHelper.getOffLineDate(getActivity(), HttpParameter.create()
                            .add("storeId", UserHelper.getCurrentUser().getStoreID())
                            .add("maxTime", "")
                            .add("minTime", CurrentTime));
                    handler.sendMessage(handler.obtainMessage(OFFLINE_SUCCESS02, offLineDate));
                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(OFFLINE_FAILED02, e.getMessage()));
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
            // 调用下边的方法处理信息
            switch (msg.what) {
                case MSG_RECEIVE:
                    AttendModel model = (AttendModel) msg.obj;
                    saveModel(model);
                    dialogShow(model);
                    //listView显示
                    listdate = new ArrayList<>();
                    listdate.clear();
                    listdate.add(model);
                    adapter.insertEntityList(listdate);
                    listView.setAdapter(adapter);
                    break;
                case GET_ATTEND_SUCCESS:
                    AttendDetailModel detailModel = (AttendDetailModel) msg.obj;
                    showAttendDetail(detailModel);
                    break;
                case GET_ATTEND_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;

                case GET_VIP_SUCCESS:
                    VipModel VipModel = (VipModel) msg.obj;
                    showVIPDetail(VipModel);
                    break;
                case GET_VIP_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
                case GET_DOOR_SUCCESS:
                    DoorAccessModel DOOR = (DoorAccessModel) msg.obj;
                    showDoorDetail(DOOR);
                    break;
                case GET_DOOR_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;

                case OFFLINE_SUCCESS:
                    ArrayList<AttendModel> offLineDate = new ArrayList<>();
                    offLineDate = (ArrayList<AttendModel>) msg.obj;
                    Log.d(TAG, "run: 离线数据是否为空=" + (offLineDate == null) + "==个数：" + offLineDate.size());
                    saveListModel(offLineDate);
                    adapter.insertEntityList((ArrayList) offLineDate);
                    listView.setAdapter(adapter);
                    break;

                case OFFLINE_SUCCESS02:
                    ArrayList<AttendModel> offLineDate2 = new ArrayList<>();
                    //保存
                    offLineDate2 = (ArrayList<AttendModel>) msg.obj;
                    Log.d(TAG, "run: 离线数据是否为空=" + (offLineDate2 == null) + "==个数：" + offLineDate2.size());
                    saveListModel(offLineDate2);
                    adapter.setEntityList((ArrayList) offLineDate2);
                    listView.setAdapter(adapter);
                    break;

                case OFFLINE_FAILED02:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
            }
        }
    };

    //弹窗显示数据
    private void dialogShow(AttendModel model) {
        String message = model.getMessage();
        String Type = model.getType();
        String time = model.getCapTime();
        //弹窗提示
        JpushMsgToast.makeText(getActivity(), Type, message, time, Toast.LENGTH_SHORT).show();

    }

    /**
     * 普通考勤展示
     *
     * @param model
     */

    private void showAttendDetail(AttendDetailModel model) {
        //普通考勤没有地址，设置隐藏
        layout_adress.setVisibility(View.GONE);
        layout_dept.setVisibility(View.VISIBLE);
        layout_id.setVisibility(View.VISIBLE);
        //
        tv_dept.setText(model.getDepartmentName());
        tv_score.setText(model.getScore());
        tv_company.setText(model.getCompanyName());
        tv_captime.setText(model.getCapTime());
        tv_name.setText(model.getEmployeeName());
        tv_id.setText(model.getWrokId());
        imgLoader.displayImage(model.getImagePath(), photo_default, imgOptions);
        imgLoader.displayImage(model.getCapImagePath(), photo_cap, imgOptions);
    }

    /**
     * VIP详情展示
     *
     * @param model
     */

    private void showVIPDetail(VipModel model) {
        //普通考勤没有地址，设置隐藏
        layout_adress.setVisibility(View.GONE);
        layout_dept.setVisibility(View.GONE);
        layout_id.setVisibility(View.GONE);
        //
        tv_score.setText(model.getScore());
        tv_company.setText(model.getCompanyName());
        tv_captime.setText(model.getCapTime());
        tv_name.setText(model.getEmployeeName());
        imgLoader.displayImage(model.getImagePath(), photo_default, imgOptions);
        imgLoader.displayImage(model.getCapImagePath(), photo_cap, imgOptions);
    }

    /**
     * 门禁详情展示 无部门
     *
     * @param model
     */

    private void showDoorDetail(DoorAccessModel model) {
        //普通考勤没有地址，设置隐藏
        layout_adress.setVisibility(View.GONE);
        layout_dept.setVisibility(View.GONE);
        layout_id.setVisibility(View.VISIBLE);
        //
        tv_score.setText(model.getScore());
        tv_company.setText(model.getCompanyName());
        tv_captime.setText(model.getCapTime());
        tv_name.setText(model.getEmployeeName());
        tv_id.setText(model.getWrokId());
        imgLoader.displayImage(model.getImagePath(), photo_default, imgOptions);
        imgLoader.displayImage(model.getCapImagePath(), photo_cap, imgOptions);
    }

    //model数据保存
    private void saveModel(AttendModel model) {
        String js = (new Gson()).toJson(model);
        Log.d(TAG, "接受自定义消息的数据-->saveModel: " + js);
        dao.saveModel(model);
    }

    //model数据保存
    private void saveListModel(ArrayList<AttendModel> list) {

        dao.saveListModel(list);
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
                //接受到model
                model = (AttendModel) intent.getSerializableExtra("AttendModel");
                Log.d(TAG, "接收到Jush的model,可以在main中做自定义处理 model.getMessage=" + model.getMessage());
                handler.sendMessage(handler.obtainMessage(MSG_RECEIVE, model));
            }
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        //
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
        //item的imgLoader清空
        adapter.destroy();

        //使用 RefWatcher 监控 Fragment
        //        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        //        refWatcher.watch(this);
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
