package com.yvision.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yvision.AddNewAttenderActivity;
import com.yvision.AddNewViperActivity;
import com.yvision.R;
import com.yvision.adapter.RegisterListAdapter;
import com.yvision.base.BaseFragment;
import com.yvision.common.MyException;
import com.yvision.dialog.Loading;
import com.yvision.helper.UserHelper;
import com.yvision.model.OldEmployeeModel;
import com.yvision.utils.PageUtil;
import com.yvision.widget.FloatActionButton;

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


    private TextView tv_name;
    private TextView tv_id;
    private TextView tv_company;
    private TextView tv_dept;
    private TextView tv_gender;
    private TextView tv_attendType;
    private com.yvision.widget.FloatActionButton floatActionButton;

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
        initListener();
    }


    /**
     * 数据详细操作
     *
     * @param view
     */
    private void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.listView);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        tv_company = (TextView) view.findViewById(R.id.tv_company);
        tv_dept = (TextView) view.findViewById(R.id.tv_dept);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        tv_attendType = (TextView) view.findViewById(R.id.tv_attendType);
        floatActionButton = (FloatActionButton) view.findViewById(R.id.floatActionButton);

        adapter = new RegisterListAdapter(getActivity());
        listView.setAdapter(adapter);
        model = new OldEmployeeModel();
    }

    /**
     * 监听
     */

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OldEmployeeModel model = (OldEmployeeModel) adapter.getItem(position);
                setShow(model);
            }
        });
        floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setCancelable(false)    //不响应back按钮
                        .setTitle("选择注册类型：")
                        .setItems(new String[]{"考勤", "VIP"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(getActivity(),AddNewAttenderActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(getActivity(),AddNewViperActivity.class);
                                        startActivity(intent2);
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }

    //获取数据
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

    private void setShow(OldEmployeeModel model) {
        tv_name.setText(model.getEmployeeName() != null ? model.getEmployeeName() : "无");
        tv_gender.setText(model.getGender() != null ? model.getGender() : "无");
        tv_id.setText(model.getWrokId() != null ? model.getWrokId() : "无");
        tv_dept.setText(model.getDeptName() != null ? model.getDeptName() : "无");
        tv_company.setText(model.getCompany() != null ? model.getCompany() : "无");

        StringBuilder builder = new StringBuilder();
        if (model.getIsAttend().contains("1")) {
            builder.append(" 考勤 ");
            tv_attendType.setText(builder.toString());
        } else if (model.getIsDoorAccess().contains("1")) {
            builder.append(" 门禁 ");
            tv_attendType.setText(builder.toString());

        } else if (model.getIsVip().contains("1")) {
            builder.append(" VIP ");
            tv_attendType.setText(builder.toString());

        } else if (model.getIsVisitor().contains("1")) {
            builder.append(" 访客 ");
            tv_attendType.setText(builder.toString());

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
